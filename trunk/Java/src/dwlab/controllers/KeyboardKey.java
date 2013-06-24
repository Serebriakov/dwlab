/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */

package dwlab.controllers;

import dwlab.base.Sys;
import dwlab.base.XMLObject;
import org.lwjgl.input.Keyboard;

/**
 * Class for keyboard keys.
 */
public class KeyboardKey extends Pushable {
	public int code;
	

	public static int getCode( Key keyID ) {
		switch( keyID ){
			case ESCAPE: return Keyboard.KEY_ESCAPE;
			case _1: return Keyboard.KEY_1;
			case _2: return Keyboard.KEY_2;
			case _3: return Keyboard.KEY_3;
			case _4: return Keyboard.KEY_4;
			case _5: return Keyboard.KEY_5;
			case _6: return Keyboard.KEY_6;
			case _7: return Keyboard.KEY_7;
			case _8: return Keyboard.KEY_8;
			case _9: return Keyboard.KEY_9;
			case _0: return Keyboard.KEY_0;
			case MINUS: return Keyboard.KEY_MINUS;
			case EQUALS: return Keyboard.KEY_EQUALS;
			case BACK: return Keyboard.KEY_BACK;
			case TAB: return Keyboard.KEY_TAB;
			case Q: return Keyboard.KEY_Q;
			case W: return Keyboard.KEY_W;
			case E: return Keyboard.KEY_E;
			case R: return Keyboard.KEY_R;
			case T: return Keyboard.KEY_T;
			case Y: return Keyboard.KEY_Y;
			case U: return Keyboard.KEY_U;
			case I: return Keyboard.KEY_I;
			case O: return Keyboard.KEY_O;
			case P: return Keyboard.KEY_P;
			case LBRACKET: return Keyboard.KEY_LBRACKET;
			case RBRACKET: return Keyboard.KEY_RBRACKET;
			case RETURN: return Keyboard.KEY_RETURN;
			case LCONTROL: return Keyboard.KEY_LCONTROL;
			case A: return Keyboard.KEY_A;
			case S: return Keyboard.KEY_S;
			case D: return Keyboard.KEY_D;
			case F: return Keyboard.KEY_F;
			case G: return Keyboard.KEY_G;
			case H: return Keyboard.KEY_H;
			case J: return Keyboard.KEY_J;
			case K: return Keyboard.KEY_K;
			case L: return Keyboard.KEY_L;
			case SEMICOLON: return Keyboard.KEY_SEMICOLON;
			case APOSTROPHE: return Keyboard.KEY_APOSTROPHE;
			case GRAVE: return Keyboard.KEY_GRAVE;
			case LSHIFT: return Keyboard.KEY_LSHIFT;
			case BACKSLASH: return Keyboard.KEY_BACKSLASH;
			case Z: return Keyboard.KEY_Z;
			case X: return Keyboard.KEY_X;
			case C: return Keyboard.KEY_C;
			case V: return Keyboard.KEY_V;
			case B: return Keyboard.KEY_B;
			case N: return Keyboard.KEY_N;
			case M: return Keyboard.KEY_M;
			case COMMA: return Keyboard.KEY_COMMA;
			case PERIOD: return Keyboard.KEY_PERIOD;
			case SLASH: return Keyboard.KEY_SLASH;
			case RSHIFT: return Keyboard.KEY_RSHIFT;
			case MULTIPLY: return Keyboard.KEY_MULTIPLY;
			case LALT: return Keyboard.KEY_LMENU;
			case SPACE: return Keyboard.KEY_SPACE;
			case CAPITAL: return Keyboard.KEY_CAPITAL;
			case F1: return Keyboard.KEY_F1;
			case F2: return Keyboard.KEY_F2;
			case F3: return Keyboard.KEY_F3;
			case F4: return Keyboard.KEY_F4;
			case F5: return Keyboard.KEY_F5;
			case F6: return Keyboard.KEY_F6;
			case F7: return Keyboard.KEY_F7;
			case F8: return Keyboard.KEY_F8;
			case F9: return Keyboard.KEY_F9;
			case F10: return Keyboard.KEY_F10;
			case NUMLOCK: return Keyboard.KEY_NUMLOCK;
			case SCROLL: return Keyboard.KEY_SCROLL;
			case NUMPAD7: return Keyboard.KEY_NUMPAD7;
			case NUMPAD8: return Keyboard.KEY_NUMPAD8;
			case NUMPAD9: return Keyboard.KEY_NUMPAD9;
			case SUBTRACT: return Keyboard.KEY_SUBTRACT;
			case NUMPAD4: return Keyboard.KEY_NUMPAD4;
			case NUMPAD5: return Keyboard.KEY_NUMPAD5;
			case NUMPAD6: return Keyboard.KEY_NUMPAD6;
			case ADD: return Keyboard.KEY_ADD;
			case NUMPAD1: return Keyboard.KEY_NUMPAD1;
			case NUMPAD2: return Keyboard.KEY_NUMPAD2;
			case NUMPAD3: return Keyboard.KEY_NUMPAD3;
			case NUMPAD0: return Keyboard.KEY_NUMPAD0;
			case DECIMAL: return Keyboard.KEY_DECIMAL;
			case F11: return Keyboard.KEY_F11;
			case F12: return Keyboard.KEY_F12;
			case F13: return Keyboard.KEY_F13;
			case F14: return Keyboard.KEY_F14;
			case F15: return Keyboard.KEY_F15;
			case KANA: return Keyboard.KEY_KANA;
			case CONVERT: return Keyboard.KEY_CONVERT;
			case NOCONVERT: return Keyboard.KEY_NOCONVERT;
			case YEN: return Keyboard.KEY_YEN;
			case NUMPADEQUALS: return Keyboard.KEY_NUMPADEQUALS;
			case CIRCUMFLEX: return Keyboard.KEY_CIRCUMFLEX;
			case AT: return Keyboard.KEY_AT;
			case COLON: return Keyboard.KEY_COLON;
			case UNDERLINE: return Keyboard.KEY_UNDERLINE;
			case KANJI: return Keyboard.KEY_KANJI;
			case STOP: return Keyboard.KEY_STOP;
			case AX: return Keyboard.KEY_AX;
			case UNLABELED: return Keyboard.KEY_UNLABELED;
			case NUMPADENTER: return Keyboard.KEY_NUMPADENTER;
			case RCONTROL: return Keyboard.KEY_RCONTROL;
			case NUMPADCOMMA: return Keyboard.KEY_NUMPADCOMMA;
			case DIVIDE: return Keyboard.KEY_DIVIDE;
			case SYSRQ: return Keyboard.KEY_SYSRQ;
			case RALT: return Keyboard.KEY_RMENU;
			case PAUSE: return Keyboard.KEY_PAUSE;
			case HOME: return Keyboard.KEY_HOME;
			case UP: return Keyboard.KEY_UP;
			case PAGE_UP: return Keyboard.KEY_PRIOR;
			case LEFT: return Keyboard.KEY_LEFT;
			case RIGHT: return Keyboard.KEY_RIGHT;
			case END: return Keyboard.KEY_END;
			case DOWN: return Keyboard.KEY_DOWN;
			case PAGE_DOWN: return Keyboard.KEY_NEXT;
			case INSERT: return Keyboard.KEY_INSERT;
			case DELETE: return Keyboard.KEY_DELETE;
			case LMETA: return Keyboard.KEY_LMETA;
			default: return 0;
		}
	}

	
	@Override
	KeyboardKey getKeyboardKey() {
		return this;
	}


	@Override
	public boolean isEqualTo( Pushable pushable ) {
		KeyboardKey key = pushable.getKeyboardKey();
		if( key != null ) return code == key.code;
		return false;
	}
	

	@Override
	public void processKeyboardEvent() {
		if( Keyboard.getEventKey() == code ) {
			if( Keyboard.getEventKeyState() ) {
				state = State.JUST_PRESSED;
			} else {
				state = State.JUST_UNPRESSED;
			}
		}
	}
	

	/**
	 * Creates keyboard key object.
	 * @return New object of keyboard key with given key id.
	 */	
	public static KeyboardKey create( Key keyID ) {
		return create( getCode( keyID ) );
	}
	
	public static KeyboardKey create( int code ) {
		KeyboardKey key = new KeyboardKey();
		key.code = code;
		
		for( ButtonAction action: Sys.controllers ) {
			for( Pushable pushable: action.buttonList ) {
				if( pushable.isEqualTo( key ) ) return pushable.getKeyboardKey();
			}
		}

		return key;
	}
	

	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );
		code = xMLObject.manageIntAttribute( "code", code );
	}
}
