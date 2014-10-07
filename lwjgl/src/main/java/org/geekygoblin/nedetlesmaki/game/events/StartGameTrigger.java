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
package org.geekygoblin.nedetlesmaki.game.events;

import org.geekygoblin.nedetlesmaki.core.AbstractStartGameTrigger;
import com.artemis.Entity;
import im.bci.tmxloader.TmxTileInstance;
import java.util.Random;
import com.google.inject.Inject;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.artemis.sprite.IsometricSpriteProjector;
import im.bci.jnuit.lwjgl.assets.TmxAsset;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.NamedEntities;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.game.LwjglAssets;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;

/**
 *
 * @author devnewton natir
 */
public class StartGameTrigger extends AbstractStartGameTrigger {

    private TmxAsset tmx;

    @Inject
    public StartGameTrigger(LwjglAssets assets, @NamedEntities.MainMenu Entity mainMenu, @NamedEntities.InGameUI Entity inGameUI, @NamedEntities.IngameControls Entity ingameControls, LevelIndex indexSystem, Random random) {
        super(assets, mainMenu, inGameUI, ingameControls, indexSystem, random);
    }

    @Override
    protected IAnimationCollection getTileAnimationCollection(TmxTileInstance tile) {
        return tmx.getTileAnimationCollection(tile);
    }

    @Override
    protected void createProjector(NedGame game) {
        final float tileWidth = map.getTilewidth();
        final float tileHeight = map.getTileheight();
        game.getSystem(DrawSystem.class).setSpriteProjector(new IsometricSpriteProjector(tileWidth, tileHeight));
    }

    @Override
    protected State load(NedGame game) {
        this.tmx = ((LwjglAssets) assets).getTmx(levelName);
        this.map = tmx.getMap();
        return State.BUILD;
    }
}
