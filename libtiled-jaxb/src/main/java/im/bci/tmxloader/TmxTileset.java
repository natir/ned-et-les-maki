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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author devnewton
 */
@XmlRootElement(name = "tileset")
public class TmxTileset {

    private String source;
    private String name;
    private int tilewidth;
    private int tileheight;
    private int spacing, margin;
    private List<TmxProperty> properties = new ArrayList<>();
    private TmxImage image;
    private int firstgid;
    private TreeMap<Integer/*id*/, TmxTile> tilesById = new TreeMap<>();
    private List<TmxTile> tiles = new ArrayList<>();

    @XmlAttribute
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @XmlAttribute
    public int getFirstgid() {
        return firstgid;
    }

    public void setFirstgid(int firstgid) {
        this.firstgid = firstgid;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public int getTilewidth() {
        return tilewidth;
    }

    public void setTilewidth(int tilewidth) {
        this.tilewidth = tilewidth;
    }

    @XmlAttribute
    public int getTileheight() {
        return tileheight;
    }

    public void setTileheight(int tileheight) {
        this.tileheight = tileheight;
    }

    @XmlAttribute
    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    @XmlAttribute
    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    public List<TmxProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<TmxProperty> properties) {
        this.properties = properties;
    }

    @XmlElement(name = "image")
    public TmxImage getImage() {
        return image;
    }

    public void setImage(TmxImage image) {
        this.image = image;
    }

    @XmlElement(name = "tile")
    public List<TmxTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<TmxTile> tiles) {
        this.tiles = tiles;
    }

    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.tilesById.clear();
        for (TmxTile tile : tiles) {
            this.tilesById.put(tile.getId(), tile);
        }
        if (null == source) {
            final int maxX = image.getWidth() - margin;
            final int maxY = image.getHeight() - margin;
            int id = 0;
            for (int y = margin; y < maxY; y += tilewidth + spacing) {
                for (int x = margin; x < maxX; x += tilewidth + spacing) {
                    TmxTile tile = tilesById.get(id);
                    if (null == tile) {
                        tile = new TmxTile();
                        tile.setId(id);
                        tilesById.put(id, tile);
                    }
                    tile.setFrame(new TmxFrame(image, x, y, x + tilewidth, y + tileheight));
                    ++id;
                }
            }
        }
    }

    TmxTile getTileById(int i) {
        return tilesById.get(i);
    }
}
