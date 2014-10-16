/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

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
package org.geekygoblin.nedetlesmaki.playn.core.events;

import com.artemis.Entity;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.artemis.sprite.IsometricSpriteProjector;
import im.bci.jnuit.artemis.sprite.SpriteProjector;
import im.bci.jnuit.playn.animation.PlaynAnimation;
import im.bci.jnuit.playn.animation.PlaynAnimationCollection;
import im.bci.jnuit.playn.animation.PlaynAnimationImage;
import im.bci.tmxloader.TmxFrame;
import im.bci.tmxloader.TmxImage;
import im.bci.tmxloader.TmxLoader;
import im.bci.tmxloader.TmxMap;
import im.bci.tmxloader.TmxTile;
import im.bci.tmxloader.TmxTileInstance;
import im.bci.tmxloader.TmxTileset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geekygoblin.nedetlesmaki.core.AbstractStartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.NamedEntities;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.playn.core.systems.PlaynSpriteSystem;
import playn.core.Image;
import playn.core.PlayN;
import playn.core.util.Callback;

/**
 *
 * @author devnewton natir
 */
public class PlaynStartGameTrigger extends AbstractStartGameTrigger {

    private static final Logger LOGGER = Logger.getLogger(PlaynStartGameTrigger.class.getName());
    private final HashMap<TmxTileInstance, IAnimationCollection> tileAnimations = new HashMap<TmxTileInstance, IAnimationCollection>();

   public PlaynStartGameTrigger(IAssets assets, @NamedEntities.MainMenu Entity mainMenu, @NamedEntities.InGameUI Entity inGameUI, @NamedEntities.IngameControls Entity ingameControls, LevelIndex indexSystem, Random random) {
        super(assets, mainMenu, inGameUI, ingameControls, indexSystem, random);
    }

    @Override
    protected State load(NedGame game) {
        if (null == map) {
            map = new TmxMap();
            final TmxLoader loader = new TmxLoader();
            final String tmxFile = levelName;
            final String tmxDir = tmxFile.substring(0, tmxFile.lastIndexOf("/"));
            final String tsxDir = tmxDir.substring(0, tmxDir.lastIndexOf("/"));
            PlayN.assets().getText(tmxFile, new Callback<String>() {
                @Override
                public void onSuccess(String result) {
                    loader.parseTmx(map, result);
                    loadNextTileset();
                }

                @Override
                public void onFailure(Throwable cause) {
                    LOGGER.log(Level.SEVERE, "Cannot load " + tmxFile, cause);
                }

                private void loadNextTileset() {
                    for (final TmxTileset tileset : map.getTilesets()) {
                        if (!tileset.isReady() && null != tileset.getSource()) {
                            //TODO real path resolve...
                            final String tsxFile = tsxDir + "/" + tileset.getSource().replaceAll("../", "");
                            PlayN.assets().getText(tsxFile, new Callback<String>() {

                                @Override
                                public void onSuccess(String result) {
                                    loader.parseTsx(map, tileset, result);
                                    HashSet<TmxImage> imagesWithSourceAdjusted = new HashSet<TmxImage>();
                                    for (TmxTile tile : tileset.getTiles()) {
                                        final TmxImage image = tile.getFrame().getImage();
                                        if(!imagesWithSourceAdjusted.contains(image)) {
                                            image.setSource(tsxDir + "/" + tile.getFrame().getImage().getSource());
                                            imagesWithSourceAdjusted.add(image);
                                        }
                                    }
                                    loadNextTileset();
                                }

                                @Override
                                public void onFailure(Throwable cause) {
                                    LOGGER.log(Level.SEVERE, "Cannot load " + tsxFile, cause);
                                }
                            });
                            return;
                        }
                    }
                    loader.decode(map);
                }
            });
        }
        if (map.isReady()) {
            return State.BUILD;
        } else {
            return State.LOAD;
        }
    }

    @Override
    protected IAnimationCollection getTileAnimationCollection(TmxTileInstance tile) {
        IAnimationCollection animationCollection = tileAnimations.get(tile);
        if (null == animationCollection) {
            animationCollection = createAnimationFromTile(tile);
            tileAnimations.put(tile, animationCollection);
        }
        return animationCollection;
    }

    @Override
    protected void createProjector(NedGame game) {
        final float tileWidth = map.getTilewidth();
        final float tileHeight = map.getTileheight();
        final SpriteProjector spriteProjector = new IsometricSpriteProjector(tileWidth, tileHeight);
        //final SpriteProjector spriteProjector = new OrthogonalSpriteProjector(tileWidth, tileHeight);
        game.getSystem(PlaynSpriteSystem.class).setSpriteProjector(spriteProjector);
    }

    private IAnimationCollection createAnimationFromTile(TmxTileInstance tile) {
        TmxFrame frame = tile.getFrame();
        Image.Region image = PlayN.assets().getImage(frame.getImage().getSource()).subImage(frame.getX1(), frame.getY1(), frame.getX2() - frame.getX1(), frame.getY2() - frame.getY1());
        PlaynAnimationCollection collection = new PlaynAnimationCollection();
        PlaynAnimation animation = new PlaynAnimation("default");
        animation.addFrame(new PlaynAnimationImage(image), 1000000);
        collection.addAnimation(animation);
        collection.setReady(true);
        return collection;
    }
}
