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
package im.bci.jnuit.widgets;


import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.visitors.WidgetVisitor;

public class Toggle extends Widget {

    private final NuitToolkit toolkit;
    private boolean enabled;

    public Toggle(NuitToolkit toolkit) {
        this.toolkit = toolkit;
    }

    private String getEnabledText() {
        return toolkit.getMessage("nuit.toggle.yes");
    }

    private String getDisabledText() {
        return toolkit.getMessage("nuit.toggle.no");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void onOK() {
        this.enabled = !this.enabled;
    }

    @Override
    public void onMouseClick(float mouseX, float mouseY) {
        onOK();
    }

    @Override
    public float getMinWidth() {
        return Math.max(toolkit.getFont().getWidth(getEnabledText()), toolkit.getFont().getWidth(getDisabledText()));
    }

    @Override
    public float getMinHeight() {
        return Math.max(toolkit.getFont().getHeight(getEnabledText()), toolkit.getFont().getHeight(getDisabledText()));
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return enabled ? getEnabledText() : getDisabledText();
    }
}
