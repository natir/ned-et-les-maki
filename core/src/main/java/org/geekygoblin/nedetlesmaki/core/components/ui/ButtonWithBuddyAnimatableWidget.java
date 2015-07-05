package org.geekygoblin.nedetlesmaki.core.components.ui;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.background.Background;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Widget;

public class ButtonWithBuddyAnimatableWidget extends Button {
	private Widget buddy;
	private Background buddyFocusedBackground;
	private Background buddyBackground;

	public ButtonWithBuddyAnimatableWidget(NuitToolkit toolkit, String text) {
		super(toolkit, text);
	}

	public void setBuddy(Widget buddy) {
		this.buddy = buddy;
		if (null != buddy) {
			this.buddyFocusedBackground = buddy.getFocusedBackground();
			this.buddyBackground = buddy.getBackground();
		}
	}

	@Override
	public void onGainFocus() {
		if (null != buddy) {
			buddy.setBackground(buddyFocusedBackground);
		}
	}

	@Override
	public void onLoseFocus() {
		if (null != buddy) {
			buddy.setBackground(buddyBackground);
		}
	}
}