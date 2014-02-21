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
package org.geekygoblin.nedetlesmaki.game.components.ui;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.Label;
import im.bci.jnuit.animation.IPlay;
import java.util.ArrayList;
import com.google.inject.Inject;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.background.Background;
import im.bci.jnuit.background.NullBackground;
import im.bci.jnuit.lwjgl.assets.IAssets;
import org.geekygoblin.nedetlesmaki.game.constants.VirtualResolution;

/**
 *
 * @author devnewton
 */
public class Dialog extends Container {

    private final ArrayList<Sentence> sentences = new ArrayList<>();
    private int currentSentenceIndex;
    private IPlay currentPlay;
    private boolean finished;
    private final Label textLabel;
    private final Label view;
    private final Button nextButton;
    public static final int BUTTON_SIZE = 64;
    private NuitToolkit toolkit;

    private Sentence getCurrentSentence() {
        if (!sentences.isEmpty()) {
            return sentences.get(currentSentenceIndex);
        } else {
            return null;
        }
    }

    private Iterable<String> splitSentence(String sentence) {
        ArrayList<String> result = new ArrayList<>();
        String currentSubsentence = "";
        String[] words = sentence.split(" ");
        for (int i =0;i<words.length ; ++i) {
            String newSubsentence = currentSubsentence;
            if(!newSubsentence.isEmpty()) {
                newSubsentence+= " ";
            }
            newSubsentence += words[i];
            if (toolkit.getFont().getWidth(newSubsentence) > textLabel.getWidth()) {
                result.add(currentSubsentence);
                currentSubsentence = i != words.length-1 ? "" : words[i];
            } else {
                currentSubsentence = newSubsentence;
            }
        }
        if(!currentSubsentence.isEmpty()) {
            result.add(currentSubsentence);
        }
        if(result.isEmpty()) {
            result.add("");
        }
        return result;
    }

    private static class Sentence {

        IPlay play;
        String text;
        int x;
        int y;
        int w;
        int h;

        private Sentence(IPlay play, int x, int y, int w, int h, String text) {
            this.play = play;
            this.text = text;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    @Inject
    public Dialog(NuitToolkit toolkit, IAssets assets) {
        IAnimationCollection dialogAnimations = assets.getAnimations("dialog_ui.nanim.gz");
        this.toolkit = toolkit;

        nextButton = new Button(toolkit, "") {

            @Override
            public void onOK() {
                onNext();
            }

        };
        nextButton.setBackground(new TexturedBackground(dialogAnimations.getAnimationByName("next").start(PlayMode.LOOP)));
        nextButton.setFocusedBackground(new TexturedBackground(dialogAnimations.getAnimationByName("next_focused").start(PlayMode.LOOP)));
        nextButton.setMustDrawFocus(false);
        nextButton.setX(VirtualResolution.WIDTH - BUTTON_SIZE);
        nextButton.setY(VirtualResolution.HEIGHT - BUTTON_SIZE);
        nextButton.setWidth(BUTTON_SIZE);
        nextButton.setHeight(BUTTON_SIZE);

        Button previousButton = new Button(toolkit, "") {
            @Override
            public void onOK() {
                onPrevious();
            }

            @Override
            public boolean isFocusable() {
                return currentSentenceIndex > 0 && super.isFocusable();
            }

            @Override
            public Background getBackground() {
                if (currentSentenceIndex > 0) {
                    return super.getBackground();
                } else {
                    return NullBackground.INSTANCE;
                }
            }
        };
        previousButton.setBackground(new TexturedBackground(dialogAnimations.getAnimationByName("previous").start(PlayMode.LOOP)));
        previousButton.setFocusedBackground(new TexturedBackground(dialogAnimations.getAnimationByName("previous_focused").start(PlayMode.LOOP)));
        previousButton.setMustDrawFocus(false);
        previousButton.setX(0);
        previousButton.setY(VirtualResolution.HEIGHT - BUTTON_SIZE);
        previousButton.setWidth(BUTTON_SIZE);
        previousButton.setHeight(BUTTON_SIZE);

        this.textLabel = new Label(toolkit, "");
        textLabel.setX(BUTTON_SIZE);
        textLabel.setY(VirtualResolution.HEIGHT - BUTTON_SIZE);
        textLabel.setWidth(VirtualResolution.WIDTH - BUTTON_SIZE * 2);
        textLabel.setHeight(BUTTON_SIZE);

        view = new Label(toolkit, "");
        view.setWidth(VirtualResolution.WIDTH);
        view.setHeight(VirtualResolution.HEIGHT);

        add(view);
        add(textLabel);
        add(nextButton);
        add(previousButton);
        setFocusedChild(nextButton);
    }

    public void addTirade(IPlay play, int x, int y, int w, int h, String... sentences) {
        for (String sentence : sentences) {
            sentence = toolkit.getMessage(sentence);
            for (String splittedSentence : splitSentence(sentence)) {
                this.sentences.add(new Sentence(play, x, y, w, h, splittedSentence));
            }
        }
        onChangeSentence();
    }

    public void addTirade(IPlay play, String... sentences) {
        addTirade(play, 0, 0, VirtualResolution.WIDTH, VirtualResolution.HEIGHT, sentences);
    }

    protected void onNext() {
        if (currentSentenceIndex < (sentences.size() - 1)) {
            ++currentSentenceIndex;
        } else {
            onFinished();
        }
        onChangeSentence();
    }

    protected void onChangeSentence() {
        Sentence currentSentence = getCurrentSentence();
        if (null != currentSentence) {
            textLabel.setText(currentSentence.text);
            if (currentPlay != currentSentence.play) {
                currentPlay = currentSentence.play;
                currentSentence.play.restart();
                view.setX(currentSentence.x);
                view.setY(currentSentence.y);
                view.setWidth(currentSentence.w);
                view.setHeight(currentSentence.h);
                view.setBackground(new TexturedBackground(currentSentence.play));
            }
        }
        if (currentSentenceIndex == 0) {
            setFocusedChild(nextButton);
        }
    }

    protected void onPrevious() {
        if (currentSentenceIndex > 0) {
            --currentSentenceIndex;
        }
        onChangeSentence();
    }

    public boolean isFinished() {
        return finished;
    }

    protected void onFinished() {
        this.finished = true;
    }
}
