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

public class ActionActivatedDetector {
    private final Action action;
    private Float[] previousStates;
    private boolean activated;
    
    public ActionActivatedDetector(Action action) {
        this.action = action;
    }
    
    public Action getAction() {
        return action;
    }
    
    public boolean isActivated() {
        return activated;
    }
    
    public boolean isPressed() {
        for(Control control : action.getControls()) {
            if(control.getValue() > control.getDeadZone()) {
                return true;
            }
        }
        return false;
    }
        
    public void poll() {
        Control[] controls = action.getControls();
        int nbActions = controls.length;
        if(null == previousStates || nbActions != previousStates.length) {
            previousStates = new Float[nbActions];
        }
        activated = false;
        for(int i=0; i<nbActions; ++i) {
            Control control = controls[i];
            float newState =  control.getValue();
            if(null != previousStates[i]) {
                if(newState > control.getDeadZone() && previousStates[i] <= control.getDeadZone()) {
                    activated = true;
                }
            }
            previousStates[i] = newState;
        }        
    }
    
    public void reset() {
        previousStates = null;
    }
}
