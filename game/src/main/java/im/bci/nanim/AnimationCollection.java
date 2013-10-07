package im.bci.nanim;

import im.bci.lwjgl.nuit.utils.LwjglHelper;
import im.bci.nanim.NanimParser.Nanim;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class AnimationCollection {

    LinkedHashMap<String/*animation name*/, Animation> animations;
    private final Map<String, AnimationImage> images;

    public AnimationCollection(Nanim nanim) {
        images = loadImages(nanim);
        animations = new LinkedHashMap<>(nanim.getAnimationsCount());
        for (im.bci.nanim.NanimParser.Animation nanimation : nanim.getAnimationsList()) {
            addAnimation(new Animation(nanimation, images));
        }
    }

    private static Map<String, AnimationImage> loadImages(Nanim nanim) {

        Map<String, AnimationImage> images = new HashMap<>();
        for (im.bci.nanim.NanimParser.Image nimage : nanim.getImagesList()) {
            AnimationImage image = loadImage(nimage);
            images.put(nimage.getName(), image);
        }
        return images;
    }

    private static AnimationImage loadImage(im.bci.nanim.NanimParser.Image nimage) {
        int texWidth = nimage.getWidth();
        int texHeight = nimage.getHeight();
        AnimationImage texture = new AnimationImage(nimage.getFormat().equals(NanimParser.PixelFormat.RGBA_8888));

        ByteBuffer imageBuffer = ByteBuffer.allocateDirect(nimage.getPixels().size());
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(nimage.getPixels().asReadOnlyByteBuffer());
        imageBuffer.flip();

        // produce a texture from the byte buffer
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        LwjglHelper.setupGLTextureParams();
        int pixelFormat = nimage.getFormat().equals(NanimParser.PixelFormat.RGBA_8888) ? GL11.GL_RGBA
                : GL11.GL_RGB;
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelFormat, texWidth,
                texHeight, 0, pixelFormat, GL11.GL_UNSIGNED_BYTE, imageBuffer);
        return texture;
    }

    private void addAnimation(Animation animation) {
        animations.put(animation.getName(), animation);
    }

    public Animation getFirst() {
        return animations.values().iterator().next();
    }

    public Animation getAnimationByName(String name) {
        return animations.get(name);
    }

    public Map<String, AnimationImage> getImages() {
        return images;
    }
}
