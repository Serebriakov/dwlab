/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */

package dwlab.controllers;

import dwlab.base.XMLObject;
import dwlab.platform.Platform;

/**
 * Class for keyboard keys.
 */
public class KeyboardKey extends Pushable {
	public int code;

	
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
		Platform.current.processKeyboardKeyEvent( this );
	}
	

	/**
	 * Creates keyboard key object.
	 * @return New object of keyboard key with given key id.
	 */	
	public static KeyboardKey create( Key keyID ) {
		return create( Platform.current.getKeyboardCode( keyID ) );
	}
	
	public static KeyboardKey create( int code ) {
		KeyboardKey key = new KeyboardKey();
		key.code = code;
		
		for( ButtonAction action: Platform.controllers ) {
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
