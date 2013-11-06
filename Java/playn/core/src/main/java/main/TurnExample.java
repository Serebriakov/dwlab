package main;

import dwlab.base.Obj;
import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.platform.PlayN;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Visualizer;
import playn.core.Game;

public class TurnExample extends Game.Default {
	double turningSpeed = 0.5 * Math.PI;

	public Sprite tank = new Sprite( ShapeType.rectangle, 0, 0, 2, 2, 0, 5 );

	ButtonAction left = ButtonAction.create( KeyboardKey.create( Key.LEFT ) );
	ButtonAction right = ButtonAction.create( KeyboardKey.create( Key.RIGHT ) );
	ButtonAction up = ButtonAction.create( KeyboardKey.create( Key.UP ) );
	ButtonAction down = ButtonAction.create( KeyboardKey.create( Key.DOWN ) );

	public TurnExample() {
		super( 5 );
	}
	
	@Override
	public void init() {
		PlayN.init();
		tank.visualizer = new Visualizer( "res/tank.png" );
		Image bgImage = assets().getImage("images/bg.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		graphics().rootLayer().add(bgLayer);
	}

	@Override
	public void update( int delta ) {
		if( left.isDown() ) tank.turn( -turningSpeed );
		if( right.isDown() ) tank.turn( turningSpeed );
		if( up.isDown() ) tank.moveForward();
		if( down.isDown() ) tank.moveBackward();
	}

	@Override
	public void paint( float alpha ) {
		tank.draw();
		Obj.printText( "Press arrow Keys to move tank" );
		Obj.printText( "Turn, MoveForward, MoveBackward example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
