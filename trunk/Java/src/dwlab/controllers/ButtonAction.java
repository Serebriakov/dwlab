/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2012, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.controllers;

import dwlab.base.Obj;
import dwlab.base.Sys;
import dwlab.base.XMLObject;
import java.util.LinkedList;

/**
 * Class for action which can be triggered by activating pushable object (presing a key, mouse button, etc).
 * Key can be binded to several actions and several keys can be binded to one action.
 */	
public class ButtonAction extends Obj {
	public static ButtonAction ctrlAction = ButtonAction.create( KeyboardKey.create( Key.LCONTROL ), KeyboardKey.create( Key.RCONTROL ), "ctrl" );
	public static ButtonAction altAction = ButtonAction.create( KeyboardKey.create( Key.LALT ), KeyboardKey.create( Key.RALT ), "alt" );
	public static ButtonAction shiftAction =ButtonAction.create( KeyboardKey.create( Key.LSHIFT ), KeyboardKey.create( Key.RSHIFT ), "shift" );
	
	public String name;
	public LinkedList<Pushable> buttonList = new LinkedList();
	public boolean ctrl = false, alt = false, shift = false;
	
	/**
	 * Maximum quantity of buttons in button list (0 means unlimited).
	 */	
	public int maxButtons = 3;


	/**
	 * Creates button action with given pushable object (button) and name (optional).
	 * @return New button action with one pushable object (button).
	 */
	public static ButtonAction create( Pushable button, String name, String modifiers ) {
		ButtonAction action = new ButtonAction();
		action.name = name;
		action.buttonList.addLast( button );
		modifiers = modifiers.toLowerCase();
		if( modifiers.contains( "ctrl" ) ) action.ctrl = true;
		if( modifiers.contains( "alt" ) ) action.alt = true;
		if( modifiers.contains( "shift" ) ) action.shift = true;
		Sys.controllers.add( action );
		return action;
	}
	
	public static ButtonAction create( Pushable button, String name ) {
		return create( button, name, "" );
	}
	
	public static ButtonAction create( Pushable button ) {
		return create( button, "", "" );
	}
	
	public static ButtonAction create( Pushable button1, Pushable button2, String name ) {
		ButtonAction action = create( button1, name, "" );
		action.buttonList.addLast( button2 );
		return action;
	}
	

	public boolean modifiersPressed() {
		if( ctrl && !ctrlAction.isDown() ) return false;
		if( alt && !altAction.isDown() ) return false;
		if( shift && !shiftAction.isDown() ) return false;
		return true;
	}


	public String getButtonNames( boolean withBrackets ) {
		String names = "";
		for( Pushable button: buttonList ) {
			if( !names.isEmpty() ) names += ", ";
			if( withBrackets ) {
				names += "{{" + button.getName() + "}}";
			} else {
				names += button.getName();
			}
		}
		return names;
	}
	
	public String getButtonNames() {
		return getButtonNames( false );
	}


	/**
	 * Adds given pushable object (button) to the button action button list.
	 */
	public void addButton( Pushable button ) {
		for( Pushable oldButton: buttonList ) {
			if( oldButton.isEqualTo( button ) ) return;
		}
		buttonList.addLast( button );
		if( maxButtons > 0 ) if( buttonList.size() > maxButtons ) buttonList.removeFirst();
	}


	public boolean define() {
		return Sys.getPushable( this );
	}


	/**
	 * Removes all pushable objects (buttons) of the button action.
	 */
	public void clear() {
		buttonList.clear();
	}


	/**
	 * Function which checks button action pressing state.
	 * @return True if one of pushable objects (buttons) of this action is currently pressed.
	 */
	public boolean isDown() {
		if( !modifiersPressed() ) return false;
		for( Pushable button: buttonList ) {
			if( button.isDown() ) return true;
		}
		return false;
	}


	/**
	 * Function which checks button action just-pressing state.
	 * @return True if one of pushable objects (buttons) of this action was pressed in current project cycle.
	 */
	public boolean wasPressed() {
		if( !modifiersPressed() ) return false;
		for( Pushable button: buttonList ) {
			if( button.wasPressed() ) return true;
		}
		return false;
	}


	/**
	 * Function which checks button action just-unpressing state.
	 * @return True if one of pushable objects (buttons) of this action was unpressed in current project cycle.
	 */	
	public boolean wasUnpressed() {
		if( !modifiersPressed() ) return false;
		for( Pushable button: buttonList ) {
			if( button.wasUnpressed() ) return true;
		}
		return false;
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );
		name = xMLObject.manageStringAttribute( "name", name );
		buttonList = xMLObject.manageChildList( buttonList );
		if( Sys.xMLGetMode() ) Sys.controllers.add( this );
	}
}
