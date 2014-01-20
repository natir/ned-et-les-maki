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
package im.bci.jnuit.lwjgl.assets;

import im.bci.jnuit.lwjgl.TrueTypeFont;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.lwjgl.animation.NanimationCollection;
import java.lang.ref.ReferenceQueue;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devnewton
 */
public class GarbageCollectedAssets implements IAssets {

    private final AssetsLoader assets;
    private final HashMap<String/* name */, TextureWeakReference> textures = new HashMap<>();
    private final ReferenceQueue<Texture> texturesReferenceQueue = new ReferenceQueue<>();
    private final HashMap<String/* name */, AnimationCollectionWeakReference> animations = new HashMap<>();
    private final ReferenceQueue<NanimationCollection> animationsReferenceQueue = new ReferenceQueue<>();
    private final HashMap<String/* name */, TrueTypeFontWeakReference> fonts = new HashMap<>();
    private final ReferenceQueue<TrueTypeFont> fontsReferenceQueue = new ReferenceQueue<>();
    private static final Logger logger = Logger.getLogger(GarbageCollectedAssets.class.getName());

    public GarbageCollectedAssets(AssetsLoader assets) {
        this.assets = assets;
    }

    @Override
    public void clearAll() {
        for (TextureWeakReference ref : textures.values()) {
            ref.delete();
        }
        textures.clear();

        for (AnimationCollectionWeakReference ref : animations.values()) {
            ref.delete();
        }
        animations.clear();

        for (TrueTypeFontWeakReference ref : fonts.values()) {
            ref.delete();
        }
        fonts.clear();
    }

    @Override
    public void clearUseless() {
        System.gc();
        TextureWeakReference tex;
        while ((tex = (TextureWeakReference) texturesReferenceQueue.poll()) != null) {
            tex.delete();
        }

        AnimationCollectionWeakReference ani;
        while ((ani = (AnimationCollectionWeakReference) animationsReferenceQueue.poll()) != null) {
            ani.delete();
        }
    }

    @Override
    public IAnimationCollection getAnimations(String name) {
        if (name.endsWith("png")) {
            return new TextureAnimationCollectionWrapper(this, name, 0, 0, 1, 1);
        } else {
            AnimationCollectionWeakReference animRef = animations.get(name);
            if (null != animRef) {
                NanimationCollection anim = animRef.get();
                if (null != anim) {
                    return anim;
                } else {
                    animations.remove(name);
                }
            }
            NanimationCollection anim = assets.loadAnimations(name);
            putAnim(name, anim);
            return anim;
        }
    }

    @Override
    public TrueTypeFont getFont(String name) {
        TrueTypeFontWeakReference fontRef = fonts.get(name);
        if (fontRef != null) {
            TrueTypeFont font = fontRef.get();
            if (font != null) {
                return font;
            } else {
                fonts.remove(name);
            }
        }
        TrueTypeFont font = assets.loadFont(name);
        putFont(name, font);
        return font;
    }

    @Override
    public ITexture getTexture(String name) {
        TextureWeakReference textureRef = textures.get(name);
        if (textureRef != null) {
            Texture texture = textureRef.get();
            if (texture != null) {
                return texture;
            } else {
                textures.remove(name);
            }
        }
        Texture texture = assets.loadTexture(name);
        putTexture(name, texture);
        return texture;

    }

    @Override
    public TmxAsset getTmx(String name) {
        TmxAssetLoader tmxLoader = new TmxAssetLoader(this);
        return tmxLoader.loadTmx(name);
    }

    @Override
    public Texture grabScreenToTexture() {
        final String name = "!screenCapture_" + new Date().getTime();
        Texture texture = assets.grabScreenToTexture();
        putTexture(name, texture);
        return texture;
    }

    @Override
    public void setIcon() {
        assets.setIcon();
    }

    private void putAnim(String name, NanimationCollection anim) {
        animations.put(name, new AnimationCollectionWeakReference(name, anim, animationsReferenceQueue));
    }

    private void putTexture(String name, Texture texture) {
        textures.put(name, new TextureWeakReference(name, texture, texturesReferenceQueue));
    }

    private void putFont(String name, TrueTypeFont font) {
        fonts.put(name, new TrueTypeFontWeakReference(name, font, fontsReferenceQueue));
    }

    @Override
    public String getText(String name) {
        return assets.loadText(name);
    }

    @Override
    public void forceAnimationUnload(String name) {
        clearUseless();
        AnimationCollectionWeakReference animation = animations.get(name);
        if (null != animation) {
            if (null != animation.get()) {
                logger.log(Level.WARNING, "Force still referenced animation ''{0}'' unload.", name);
            }
            animation.delete();
            animations.remove(name);
        }
    }

}
