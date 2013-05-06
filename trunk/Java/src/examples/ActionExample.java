package examples;

import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.Sprite.ShapeType;
import org.lwjgl.input.Keyboard;


public class ActionExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new ActionExample() ).act();
	}
	
	static ButtonAction undoKey = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_Z ), "undo", "ctrl" );
	static ButtonAction redoKey = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_Y ), "redo", "ctrl" );
	static ButtonAction saveKey = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_F2 ), "save" );
	static ButtonAction loadKey = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_F3 ), "load" );
	
	final int spritesQuantity = 50;

	static Layer sprites = new Layer();
	static Image spriteImage = new Image( "res/kolobok.png" );
	static MoveDrag drag = new MoveDrag();
	
	
	@Override
	public void init() {
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Sprite sprite = new Sprite( ShapeType.OVAL, Service.random( -15, 15 ), Service.random( -11, 11 ), 0, 0 );
			sprite.setDiameter( Service.random( 1, 3 ) );
			sprite.displayingAngle= Service.random( 360 );
			sprite.visualizer.setRandomColor();
			sprite.visualizer.image = spriteImage;
			sprite.visualizer.setVisualizerScales( 1.3 );
			sprites.addLast( sprite );
		}
	}
	

	@Override
	public void logic() {
		drag.act();

		Action.pushActionsList();
		if( undoKey.wasPressed() ) Action.undoStep();
		if( redoKey.wasPressed() ) Action.redoStep();

		if( saveKey.wasPressed() ) sprites.saveToFile( "sprites2.lw" );
		if( loadKey.wasPressed() ) sprites = Obj.loadFromFile( "sprites2.lw" ).toLayer();
	}
	

	@Override
	public void render() {
		sprites.draw();
		printText( "Drag sprites with left mouse button, press CTRL-Z to undo, CTRL-Y to redo, F2 to save, F3 to load" );
		printText( "LTAction, L_Undo, L_Redo, L_PushActionsList, LTDrag example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
	

	public static class MoveDrag extends Drag {
		ButtonAction key = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
						
		Shape shape;
		MoveAction action;
		double dX, dY;

		
		@Override
		public boolean dragKey() {
			return key.isDown();
		}
		

		@Override
		public void startDragging() {
			shape = cursor.lastCollidedSpriteOfLayer( sprites );
			if( shape != null ) {
				action = new MoveAction( shape );
				dX = shape.getX() - cursor.getX();
				dY = shape.getY() - cursor.getY();
			} else {
				draggingState = false;
			}
		}
		

		@Override
		public void dragging() {
			shape.setCoords( cursor.getX() + dX, cursor.getY() + dY );
		}
		

		@Override
		public void endDragging() {
			action.newX = shape.getX();
			action.newY = shape.getY();
			action.perform();
		}
	}


	public static class MoveAction extends Action {
		Shape shape;
		double oldX, oldY;
		double newX, newY;

		
		public MoveAction( Shape shape ) {
			this.shape = shape;
			this.oldX = shape.getX();
			this.oldY = shape.getY();
		}

		
		@Override
		public void perform() {
			shape.setCoords( newX, newY );
			super.perform();
		}

		@Override
		public void undo() {
			shape.setCoords( oldX, oldY );
			super.undo();
		}
	}
}
