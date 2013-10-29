/*
The MIT License (MIT)

Copyright (c) 2013 devnewton <devnewton@bci.im>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package im.bci.tmxloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author devnewton
 */
public class TmxLoader {

    private Unmarshaller unmarshaller;

    public TmxMap load(InputStream is) throws JAXBException, IOException {
        if (null == unmarshaller) {
            unmarshaller = JAXBContext.newInstance(TmxMap.class, TmxTile.class).createUnmarshaller();
        }
        TmxMap map = (TmxMap) unmarshaller.unmarshal(is);
        unmarshalExternalTilesets(map);
        decodeLayerData(map);
        return map;
    }

    protected InputStream openExternalTileset(String source) {
        throw new RuntimeException("Not implemented");
    }

    private void decodeLayerData(TmxMap map) {
        HashMap<TmxTileInstance, TmxTileInstance> tileInstancePool = new HashMap<>();
        for (TmxLayer layer : map.getLayers()) {
            int[][] data = new int[layer.getWidth()][layer.getHeight()];
            layer.getData().decodeTo(data);
            for (int x = 0; x < map.getWidth(); ++x) {
                for (int y = 0; y < map.getHeight(); ++y) {
                    int gid = data[x][y];
                    if (0 != gid) {
                        EnumSet<TmxTileInstanceEffect> effects = EnumSet.noneOf(TmxTileInstanceEffect.class);
                        for (TmxTileInstanceEffect effect : TmxTileInstanceEffect.values()) {
                            if ((gid & effect.gidFlag) != 0) {
                                effects.add(effect);
                            }
                            gid &= ~effect.gidFlag;
                        }
                        ListIterator<TmxTileset> it = map.getTilesets().listIterator(map.getTilesets().size());
                        while (it.hasPrevious()) {
                            TmxTileset tileset = it.previous();
                            if (tileset.getFirstgid() <= gid) {
                                TmxTileInstance instance = new TmxTileInstance(tileset.getTileById(gid - tileset.getFirstgid()), effects);
                                TmxTileInstance pooledInstance = tileInstancePool.get(instance);
                                if (null != pooledInstance) {
                                    instance = pooledInstance;
                                } else {
                                    tileInstancePool.put(instance, instance);
                                }
                                layer.setTileAt(x, y, instance);
                            }
                        }
                    }
                }
            }
        }
    }

    private void unmarshalExternalTilesets(TmxMap map) throws IOException, JAXBException {
        List<TmxTileset> newTilesets = new ArrayList<>();
        for (TmxTileset tileset : map.getTilesets()) {
            final String source = tileset.getSource();
            if (null != source) {
                try (InputStream is = openExternalTileset(source)) {
                    TmxTileset newTileset = (TmxTileset) unmarshaller.unmarshal(is);
                    newTileset.setSource(source);
                    newTileset.setFirstgid(tileset.getFirstgid());
                    String tilesetDir = source.substring(0, source.lastIndexOf('/') + 1);
                    newTileset.getImage().setSource(tilesetDir + newTileset.getImage().getSource());
                    newTilesets.add(newTileset);
                }
            } else {
                newTilesets.add(tileset);
            }
        }
        map.setTilesets(newTilesets);
    }
}
