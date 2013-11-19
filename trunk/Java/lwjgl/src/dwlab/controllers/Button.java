/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.controllers;

import dwlab.base.Obj;

/**
 * Common class for keys, buttons and mouse wheel rolls.
 */
public abstract class Button extends Obj implements Comparable {
	public enum Name {
		ESCAPE,
		_1,
		_2,
		_3,
		_4,
		_5,
		_6,
		_7,
		_8,
		_9,
		_0,
		MINUS,
		EQUALS,
		BACK,
		TAB,
		Q,
		W,
		E,
		R,
		T,
		Y,
		U,
		I,
		O,
		P,
		LBRACKET,
		RBRACKET,
		RETURN,
		LCONTROL,
		A,
		S,
		D,
		F,
		G,
		H,
		J,
		K,
		L,
		SEMICOLON,
		APOSTROPHE,
		GRAVE,
		LSHIFT,
		BACKSLASH,
		Z,
		X,
		C,
		V,
		B,
		N,
		M,
		COMMA,
		PERIOD,
		SLASH,
		RSHIFT,
		MULTIPLY,
		LALT,
		SPACE,
		CAPITAL,
		F1,
		F2,
		F3,
		F4,
		F5,
		F6,
		F7,
		F8,
		F9,
		F10,
		NUMLOCK,
		SCROLL,
		NUMPAD7,
		NUMPAD8,
		NUMPAD9,
		SUBTRACT,
		NUMPAD4,
		NUMPAD5,
		NUMPAD6,
		ADD,
		NUMPAD1,
		NUMPAD2,
		NUMPAD3,
		NUMPAD0,
		DECIMAL,
		F11,
		F12,
		F13,
		F14,
		F15,
		KANA,
		CONVERT,
		NOCONVERT,
		YEN,
		NUMPADEQUALS,
		CIRCUMFLEX,
		AT,
		COLON,
		UNDERLINE,
		KANJI,
		STOP,
		AX,
		UNLABELED,
		NUMPADENTER,
		RCONTROL,
		NUMPADCOMMA,
		DIVIDE,
		SYSRQ,
		RALT,
		PAUSE,
		HOME,
		UP,
		PAGE_UP,
		LEFT,
		RIGHT,
		END,
		DOWN,
		PAGE_DOWN,
		INSERT,
		DELETE,
		LMETA	,
		LEFT_MOUSE_BUTTON,
		RIGHT_MOUSE_BUTTON,
		MIDDLE_MOUSE_BUTTON,
		MOUSE_WHEEL_UP,
		MOUSE_WHEEL_DOWN
	}

	public enum State {
		JUST_PRESSED,
		PRESSED,
		JUST_UNPRESSED,
		UNPRESSED
	}

	public State state = State.UNPRESSED;


	public void reset() {
		switch( state ) {
			case JUST_PRESSED:
				state = State.PRESSED;
				break;
			case JUST_UNPRESSED:
				state = State.UNPRESSED;
				break;
		}
	}


	/**
	 * Function which checks is the object pressed.
	 * @return True if pushable object is currently pressed.
	 */
	public boolean isDown() {
		return state == State.JUST_PRESSED || state == State.PRESSED;
	}


	/**
	 * Function which checks was the object pressed.
	 * @return True if pushable object was presed during this project cycle.
	 */
	public boolean wasPressed() {
		return state == State.JUST_PRESSED;
	}


	/**
	 * Function which checks was the object unpressed.
	 * @return True if pushable object was unpresed during this project cycle.
	 */
	public boolean wasUnpressed() {
		return state == State.JUST_UNPRESSED;
	}
}
