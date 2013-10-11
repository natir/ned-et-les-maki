/*
 * Copyright (c) 2013 devnewton <devnewton@bci.im>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'devnewton <devnewton@bci.im>' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package im.bci.tmxloader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
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

    public TmxMap load(InputStream is) throws JAXBException {
        if (null == unmarshaller) {
            unmarshaller = JAXBContext.newInstance(TmxMap.class).createUnmarshaller();
        }
        TmxMap map = (TmxMap) unmarshaller.unmarshal(is);
        decodeLayerData(map);
        return map;
    }

    private void decodeLayerData(TmxMap map) {
        HashMap<TmxTileInstance, TmxTileInstance> tileInstancePool = new HashMap<>();
        for (TmxLayer layer : map.getLayers()) {
            int[][] data = new int[layer.getWidth()][layer.getHeight()];
            layer.getData().decodeTo(data);
            for (int x = 0; x < map.getWidth(); ++x) {
                for (int y = 0; y < map.getHeight(); ++y) {
                    int gid = data[x][y];
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

    public static void main(String[] args) throws JAXBException, IOException {
        TmxLoader loader = new TmxLoader();
        try (FileInputStream is = new FileInputStream("/home/bcolombi/dev/ned-et-les-maki/game/data/levels/test.tmx")) {
            TmxMap map = loader.load(is);
            System.out.println(map);
        }
    }
}
