package examples;

import dwlab.platform.Platform;

import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.IntVector;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.maps.tilemaps.TileSet;
import dwlab.shapes.sprites.VectorSprite;

public class SetAsTileExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new SetAsTileExample() ).act();
	}
	
	
	int tileMapWidth = 16;
	int tileMapHeight = 12;

	public TileSet tileSet = new TileSet( new Image( "res/tiles.png", 8, 4 ) );
	public TileMap tileMap = TileMap.create( tileSet, tileMapWidth, tileMapHeight );
	static Layer pieces = new Layer();

	ButtonAction dropPiece = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );

	
	@Override
	public void init() {
		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		for( int y = 0; y < tileMapHeight; y++ ) {
			for( int x = 0; x < tileMapWidth; x++ ) {
				tileMap.setTile( x, y, (int) Service.random( 1, 31 ) );
			}
		}
	}
	

	@Override
	public void logic() {
		if( dropPiece.wasPressed() ) {
			IntVector tile = new IntVector();
			tileMap.getTileForPoint( cursor.getX(), cursor.getY(), tile );
			if( tileMap.getTile( tile.x, tile.y ) > 0 ) {
				VectorSprite piece = new VectorSprite(){
					double gravity = 8.0;

					public double startingTime = time;
					public double angularDirection =  -1 + 2 * ( (int) Service.random( 0, 1 ) );

					@Override
					public void act() {
						moveForward();
						angle = Math.PI * 0.25 * ( time - startingTime ) * angularDirection;
						dY += perSecond( gravity );
						if( topY() > tileMap.bottomY() ) removeFrom( pieces );
					}
				};
				pieces.addFirst( piece );
				
				piece.setAsTile( tileMap, tile.x, tile.y );
				tileMap.setTile( tile.x, tile.y, 0 );
			}
		}
		pieces.act();
	}
	

	@Override
	public void render() {
		tileMap.draw();
		pieces.draw();
		showDebugInfo();
		printText( "Click on tiles to make them fall" );
		printText( "SetAsTile example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}



