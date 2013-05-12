package dwlab.base;

import dwlab.controllers.*;
import java.util.LinkedList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Sys {
	public static LinkedList<ButtonAction> controllers = new LinkedList<ButtonAction>();
	public static LinkedList<Integer> keys = new LinkedList<Integer>();
	
	public static final boolean debug = true;
	
	
	public enum XMLMode {
		GET,
		SET
	}
	
	public static XMLMode xMLMode;

	public static boolean xMLGetMode() {
		return xMLMode == XMLMode.GET;
	}	

	public static boolean xMLSetMode() {
		return xMLMode == XMLMode.SET;
	}
	
	
	public static int getChar() {
		if( keys.isEmpty() ) return 0;
		int code = keys.getFirst();
		keys.removeFirst();
		return code;
	}
	
	
	public static int mouseX() {
		return Mouse.getX();
	}	
	
	public static int mouseY() {
		return Graphics.getScreenHeight() - Mouse.getY();
	}
	
	
	public static void processEvents( Project project ) {
		for( ButtonAction controller: controllers ) {
			for( Pushable pushable : controller.buttonList ) {
				pushable.reset();
			}
		}
		
		while ( Keyboard.next() ) {
			for( ButtonAction controller: controllers ) {
				for( Pushable pushable : controller.buttonList ) {
					pushable.processKeyboardEvent();
					if( project != null ) project.onKeyboardEvent();
					if( Keyboard.getEventKeyState() ) Sys.keys.add( Keyboard.getEventKey() );
				}
			}
		}
		
		while ( Mouse.next() ) {
			for( ButtonAction controller: controllers ) {
				for( Pushable pushable : controller.buttonList ) {
					pushable.processMouseEvent();
					if( project != null ) project.onMouseEvent();
				}
			}
		}
		
		if( Display.isCloseRequested() ) if( project != null ) project.onCloseButton();
	}
	

	public static void flushControllers() {
		boolean exiting = false;
		while( !exiting ) {
			exiting = true;
			
			Display.processMessages();
			processEvents( null );
			
			for( ButtonAction controller: controllers ) {
				if( controller.isDown() ) exiting = false;
			}
		}
	}
	

	public static boolean getPushable( ButtonAction action ) {
		while ( Keyboard.next() ) {
			if( Keyboard.getEventKeyState() ) {
				action.addButton( KeyboardKey.create( Keyboard.getEventKey() ) );
				return true;
			}
		}
		
		while ( Mouse.next() ) {
			if( Mouse.getEventButtonState() ) {
				action.addButton( MouseButton.create( Mouse.getEventButton() ) );
				return true;
			}
			
			int dWheel = Mouse.getEventDWheel();
			if( dWheel !=0 ) {
				action.addButton( MouseWheel.create( dWheel ) );
				return true;
			}
		}
		
		return false;
	}

	public static void waitForKey() {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}
}
