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
package im.bci.jnuit.controls;

public class Action {
    private String name;
    private final Control[] controls;
    
    public Action(String name, Control... ctrls) {
        this.name = name;
        controls = new Control[2];
        int i=0;
        for(int n= Math.min(controls.length, ctrls.length); i<n; ++i) {
            controls[i] = ctrls[i];
        }
        for(;i<controls.length; ++i) {
            controls[i] = NullControl.INSTANCE;
        }
    }

    public Action(Action action) {
        this.name = action.name;
        this.controls = action.controls.clone();
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public Control[] getControls() {
        return controls;
    }

    public Control getMainControl() {
        return controls[0];
    }
    
    public void setMainControl(Control control) {
        controls[0] = control;
    }
    
    public Control getAlternativeControl() {
        return controls[1];
    }
    
    public void setAlternativeControl(Control control) {
        controls[1] = control;
    }
}
