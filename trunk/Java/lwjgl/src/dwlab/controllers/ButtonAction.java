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
import dwlab.base.XMLObject;
import static dwlab.platform.Functions.*;
import java.util.LinkedList;

/**
 * Class for action which can be triggered by activating pushable object (presing a key, mouse button, etc).
 * Key can be binded to several actions and several keys can be binded to one action.
 */	
public class ButtonAction extends Obj {
	public static ButtonAction ctrlAction = ButtonAction.create( "ctrl", Button.Name.LCONTROL, Button.Name.RCONTROL );
	public static ButtonAction altAction = ButtonAction.create( "alt", Button.Name.LALT, Button.Name.RALT );
	public static ButtonAction shiftAction =ButtonAction.create( "shift", Button.Name.LSHIFT, Button.Name.RSHIFT );
	
	public String name;
	public LinkedList<Button> buttonList = new LinkedList();
	public boolean ctrl = false, alt = false, shift = false;
	
	/**
	 * Maximum quantity of buttons in button list (0 means unlimited).
	 */	
	public int maxButtons = 3;


	/**
	 * Creates button action with given pushable object (button) and name (optional).
	 * @return New button action with one pushable object (button).
	 */
	public static ButtonAction create( String name, String modifiers, Button.Name... buttonNames ) {
		ButtonAction action = new ButtonAction();
		action.name = name;
		for( Button.Name buttonName : buttonNames ) action.addButton( createButton( buttonName ) );
		modifiers = modifiers.toLowerCase();
		if( modifiers.contains( "ctrl" ) ) action.ctrl = true;
		if( modifiers.contains( "alt" ) ) action.alt = true;
		if( modifiers.contains( "shift" ) ) action.shift = true;
		controllers.add( action );
		return action;
	}
	
	public static ButtonAction create( Button.Name... buttonNames ) {
		return create( "", "", buttonNames );
	}
	
	public static ButtonAction create( String name, Button.Name... buttonNames ) {
		return create( name, "", buttonNames );
	}
	

	public boolean modifiersPressed() {
		if( ctrl && !ctrlAction.isDown() ) return false;
		if( alt && !altAction.isDown() ) return false;
		if( shift && !shiftAction.isDown() ) return false;
		return true;
	}


	/**
	 * Adds given pushable object (button) to the button action button list.
	 */
	public void addButton( Button button ) {
		for( Button oldButton: buttonList ) {
			if( oldButton.compareTo( button ) == 0 ) return;
		}
		buttonList.addLast( button );
		if( maxButtons > 0 ) if( buttonList.size() > maxButtons ) buttonList.removeFirst();
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
		for( Button button: buttonList ) {
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
		for( Button button: buttonList ) {
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
		for( Button button: buttonList ) {
			if( button.wasUnpressed() ) return true;
		}
		return false;
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );
		name = xMLObject.manageStringAttribute( "name", name );
		buttonList = xMLObject.manageChildList( buttonList );
		if( XMLObject.xMLGetMode() ) controllers.add( this );
	}
}
