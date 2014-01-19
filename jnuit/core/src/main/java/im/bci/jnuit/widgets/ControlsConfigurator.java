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

import im.bci.jnuit.NuitFont;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.border.ColoredBorder;
import im.bci.jnuit.controls.Action;
import im.bci.jnuit.controls.Control;
import im.bci.jnuit.controls.ControlActivatedDetector;
import im.bci.jnuit.controls.NullControl;
import im.bci.jnuit.visitors.WidgetVisitor;

import java.util.ArrayList;
import java.util.List;

public class ControlsConfigurator extends Table {

    private List<ControlActivatedDetector> possibleControls;
    private final List<Action> actions;
    private final NuitToolkit toolkit;
    private List<Action> resets;
    private final List<Action> defaults;
    private Button defaultButton;
    private Button resetButton;
    private Button backButton;
    private Label alternativeLabel;
    private Label controlLabel;
    private Label actionLabel;
    private List<Label> actionNameLabels;
    private List<ControlConfigurator> mainActionControls;
    private List<ControlConfigurator> alternativeActionControls;

    public ControlsConfigurator(NuitToolkit toolkit, List<Action> actions, List<Action> defaults) {
        super(toolkit);
        this.toolkit = toolkit;
        this.actions = actions;
        this.defaults = defaults;
        this.actionNameLabels = new ArrayList<Label>(actions.size());
        this.mainActionControls = new ArrayList<ControlConfigurator>(actions.size());
        this.alternativeActionControls = new ArrayList<ControlConfigurator>(actions.size());
        initResets();
        initPossibleControls();
        initUI(toolkit);
    }

    public List<Label> getActionNameLabels() {
        return actionNameLabels;
    }

    public List<ControlConfigurator> getMainActionControls() {
        return mainActionControls;
    }

    public List<ControlConfigurator> getAlternativeActionControls() {
        return alternativeActionControls;
    }

    public Button getDefaultButton() {
        return defaultButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Label getAlternativeLabel() {
        return alternativeLabel;
    }

    public Label getControlLabel() {
        return controlLabel;
    }

    public Label getActionLabel() {
        return actionLabel;
    }

    private void initResets() {
        this.resets = new ArrayList<>();
        for (Action action : actions) {
            resets.add(new Action(action));
        }
    }

    private void initUI(NuitToolkit toolkit) {
        this.defaults().expand();
        actionLabel = new Label(toolkit, "nuit.controls.configurator.action");
        final ColoredBorder border = new ColoredBorder(0.7f, 0.7f, 0.7f, 1f, 2f);
        actionLabel.setBottomBorder(border);
        this.cell(actionLabel).fill();
        controlLabel = new Label(toolkit, "nuit.controls.configurator.control");
        controlLabel.setBottomBorder(border);
        this.cell(controlLabel).fill();
        alternativeLabel = new Label(toolkit, "nuit.controls.configurator.alternative");
        alternativeLabel.setBottomBorder(border);
        this.cell(alternativeLabel).fill();
        this.row();
        for (final Action action : actions) {
            final Label actionNameLabel = new Label(toolkit, action.getName());
            actionNameLabel.setRightBorder(border);
            this.cell(actionNameLabel).fill();
            actionNameLabels.add(actionNameLabel);
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
            mainActionControls.add(mainConfigurator);
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
            alternativeActionControls.add(alternativeConfigurator);
        }
        backButton = new Button(toolkit, "nuit.controls.configurator.back") {
            @Override
            public void onOK() {
                onBack();
            }
        };
        backButton.setTopBorder(border);
        this.cell(backButton).fill();

        resetButton = new Button(toolkit, "nuit.controls.configurator.resets") {
            @Override
            public void onOK() {
                onReset();
            }
        };
        this.cell(resetButton).fill();
        resetButton.setTopBorder(border);

        defaultButton = new Button(toolkit, "nuit.controls.configurator.defaults") {
            @Override
            public void onOK() {
                onDefaults();
            }
        };
        this.cell(defaultButton).fill();
        defaultButton.setTopBorder(border);
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
        for (Control control : toolkit.getPossibleControls()) {
            possibleControls.add(new ControlActivatedDetector(control));
        }
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

}
