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
package im.bci.lwjgl.nuit.widgets;

import im.bci.lwjgl.nuit.NuitFont;
import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.controls.Action;
import im.bci.lwjgl.nuit.controls.Control;
import im.bci.lwjgl.nuit.controls.ControlActivatedDetector;
import im.bci.lwjgl.nuit.controls.ControlsUtils;
import im.bci.lwjgl.nuit.controls.NullControl;
import im.bci.lwjgl.nuit.utils.WidgetVisitor;

import java.util.ArrayList;
import java.util.List;

public class ControlsConfigurator extends Table {

    private List<ControlActivatedDetector> possibleControls;
    private final List<Action> actions;
    private final NuitToolkit toolkit;
    private List<Action> resets;
    private final List<Action> defaults;

    public ControlsConfigurator(NuitToolkit toolkit, List<Action> actions, List<Action> defaults) {
        super(toolkit);
        this.toolkit = toolkit;
        this.actions = actions;
        this.defaults = defaults;
        initResets();
        initPossibleControls();
        initUI(toolkit);
    }

    private void initResets() {
        this.resets = new ArrayList<>();
        for (Action action : actions) {
            resets.add(new Action(action));
        }
    }

    private void initUI(NuitToolkit toolkit) {
        this.defaults().expand();
        this.cell(new Label(toolkit, "nuit.controls.configurator.action"));
        this.cell(new Label(toolkit, "nuit.controls.configurator.control"));
        this.cell(new Label(toolkit, "nuit.controls.configurator.alternative"));
        this.row();
        for (final Action action : actions) {
            this.cell(new Label(toolkit, action.getName()));
            ControlConfigurator mainConfigurator = new ControlConfigurator() {
                @Override
                public Control getControl() {
                    return action.getMainControl();
                }

                @Override
                public void setControl(Control control) {
                    action.setMainControl(control);
                }
            };
            this.cell(mainConfigurator).fill();
            ControlConfigurator alternativeConfigurator = new ControlConfigurator() {
                @Override
                public Control getControl() {
                    return action.getAlternativeControl();
                }

                @Override
                public void setControl(Control control) {
                    action.setAlternativeControl(control);
                }
            };
            this.cell(alternativeConfigurator).fill();
            this.row();
        }

        this.cell(new Button(toolkit, "nuit.controls.configurator.back") {
            @Override
            public void onOK() {
                onBack();
            }
        });
        this.cell(new Button(toolkit, "nuit.controls.configurator.resets") {
            @Override
            public void onOK() {
                onReset();
            }
        });
        this.cell(new Button(toolkit, "nuit.controls.configurator.defaults") {
            @Override
            public void onOK() {
                onDefaults();
            }
        });
    }

    public abstract class ControlConfigurator extends Widget {

        private boolean suckFocus;
        private Control controlToBeConfirmed;

        public String getText() {
            String text = null;
            if (suckFocus) {
                if (null != controlToBeConfirmed) {
                    text = toolkit.getMessage("nuit.controls.configurator.press.key.again");
                } else {
                    text = toolkit.getMessage("nuit.controls.configurator.press.key");
                }
            } else if (null != getControl()) {
                text = toolkit.getMessage(getControl().getName());
            }
            return text;
        }        

        public abstract Control getControl();

        public abstract void setControl(Control control);

        @Override
        public void accept(WidgetVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public boolean isFocusWhore() {
            return true;
        }

        @Override
        public void suckFocus() {
            for (ControlActivatedDetector control : possibleControls) {
                control.reset();
            }
            suckFocus = true;
        }

        @Override
        public void onMouseClick(float mouseX, float mouseY) {
            suckFocus();
        }

        @Override
        public boolean isSuckingFocus() {
            return suckFocus;
        }

        @Override
        public void update() {
            if (isSuckingFocus()) {
                for (ControlActivatedDetector control : possibleControls) {
                    control.poll();
                    if (control.isActivated()) {
                        if (null == controlToBeConfirmed) {
                            if (isCancelControl(control)) {
                                controlToBeConfirmed = control.getControl();
                                toolkit.resetInputPoll();
                                return;
                            }
                        }
                        suckFocus = false;
                        if (null == controlToBeConfirmed || controlToBeConfirmed.equals(control.getControl())) {
                            setControl(control.getControl());
                        } else {
                            setControl(NullControl.INSTANCE);
                        }
                        controlToBeConfirmed = null;
                        toolkit.resetInputPoll();
                    }
                }
            }
        }

        @Override
        public float getMinWidth() {
            NuitFont font = toolkit.getFont();
            return Math.max(font.getWidth(toolkit.getMessage("nuit.controls.configurator.press.key")), font.getWidth(getControl().getName()));
        }

        @Override
        public float getMinHeight() {
            NuitFont font = toolkit.getFont();
            return Math.max(font.getHeight(toolkit.getMessage("nuit.controls.configurator.press.key")), font.getHeight(getControl().getName()));
        }

        private boolean isCancelControl(ControlActivatedDetector control) {
            for (Control c : toolkit.getMenuCancel().getControls()) {
                if (c.equals(control.getControl())) {
                    return true;
                }
            }
            return false;
        }

    }

    protected void onDefaults() {
        if (null != defaults) {
            for (int i = 0; i < defaults.size(); ++i) {
                actions.get(i).setMainControl(defaults.get(i).getMainControl());
                actions.get(i).setAlternativeControl(defaults.get(i).getAlternativeControl());
            }
        }
    }

    protected void onReset() {
        for (int i = 0; i < resets.size(); ++i) {
            actions.get(i).setMainControl(resets.get(i).getMainControl());
            actions.get(i).setAlternativeControl(resets.get(i).getAlternativeControl());
        }
    }

    public void onBack() {
    }

    @Override
    public void onShow() {
        initResets();
    }

    private void initPossibleControls() {
        possibleControls = new ArrayList<>();
        for (Control control : ControlsUtils.getPossibleControls()) {
            possibleControls.add(new ControlActivatedDetector(control));
        }
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

}
