package examples;

import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.maps.tilemaps.TileSet;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteAndTileCollisionHandler;
import dwlab.shapes.sprites.VectorSprite;

public class VectorSpriteExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new VectorSpriteExample() ).act();
	}
	
	
	int coinsQuantity = 100;
	int platformsQuantity = 100;
	int minPlatformLength = 3;
	int maxPlatformLength = 12;
	int mapSize = 128;

	int voidTile = 0;
	int bricksTile = 1;
	int coinTile = 2;

	ButtonAction jump = ButtonAction.create( KeyboardKey.create( Key.UP ) );
	ButtonAction moveLeft = ButtonAction.create( KeyboardKey.create( Key.LEFT ) );
	ButtonAction moveRight = ButtonAction.create( KeyboardKey.create( Key.RIGHT ) );

	
	public VectorSprite player = new VectorSprite() {
		double gravity = 10.0;
		double horizontalSpeed = 5.0;
		double jumpStrength = 15.0;

		boolean onLand;
		SpriteAndTileCollisionHandler horizontalCollisionHandler = new SpriteAndTileCollisionHandler(){
			@Override
			public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
				if( bricks( tileMap, tileX, tileY ) ) sprite.pushFromTile( tileMap, tileX, tileY );
			}
		};
		SpriteAndTileCollisionHandler verticalCollisionHandler = new SpriteAndTileCollisionHandler(){
			@Override
			public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
				if( bricks( tileMap, tileX, tileY ) ) {
					sprite.pushFromTile( tileMap, tileX, tileY );
					VectorSprite vectorSprite = (VectorSprite) sprite;
					if( vectorSprite.dY > 0 ) onLand = true;
					vectorSprite.setY( 0 );
				}
			}
		};

		@Override
		public void act() {
			move( dX, 0 );
			collisionsWithTileMap( tileMap, horizontalCollisionHandler );

			onLand = false;
			move( 0, dY );
			dY += perSecond( gravity );
			collisionsWithTileMap( tileMap, verticalCollisionHandler );

			dX = 0.0;
			if( moveLeft.isDown() ) {
				dX = -horizontalSpeed;
				setFacing( Facing.LEFT );
			} else if( moveRight.isDown() ) {
				dX = horizontalSpeed;
				setFacing( Facing.RIGHT );
			}

			if( onLand && jump.isDown() ) dY = -jumpStrength;
		}
		
					
		boolean bricks( TileMap tileMap, int tileX, int tileY ) {
			int tileNum = tileMap.getTile( tileX, tileY );
			if( tileNum == coinTile ) {
				tileMap.setTile( tileX, tileY, voidTile );
				coins += 1;
			} else if( tileNum == bricksTile ) {
				return true;
			}
			return false;
		}
	};
	
	public TileMap tileMap = TileMap.create( new TileSet( new Image( "res/tileset.png", 4, 1 ), 0 ), mapSize, mapSize );
	public int coins;

	
	@Override
	public void init() {
		player.setSize( 0.8, 1.8 );
		player.setCoords( 0, 2 -0.5 * mapSize );
		player.visualizer.image = new Image( "res/mario.png", 4, 1 );
			
		tileMap.setSize( mapSize, mapSize );
		for( int n = 0; n < coinsQuantity; n++ ) {
			tileMap.setTile( (int) Service.random( 1, mapSize - 2 ), (int) Service.random( 1, mapSize - 2 ), coinTile );
		}
		for( int n = 0; n < platformsQuantity; n++ ) {
			int size = (int) Service.random( minPlatformLength, maxPlatformLength );
			int x = (int) Service.random( 1, mapSize - 1 - size );
			int y = (int) Service.random( 1, mapSize - 2 );
			for( int dX = 0; dX <= size; dX++ ) tileMap.setTile( x + dX, y, bricksTile );
		}
		for( int n = 0; n < mapSize ; n++ ) {
			tileMap.setTile( n, 0, bricksTile );
			tileMap.setTile( n, mapSize - 1, bricksTile );
			tileMap.setTile( 0, n ,bricksTile );
			tileMap.setTile( mapSize - 1, n, bricksTile );
		}
		tileMap.tileSet.collisionSprites = new Sprite[ 3 ][];
		for( int n = 1; n < 3; n ++ ) tileMap.tileSet.collisionSprites[ n ] = new Sprite[ 1 ];
		tileMap.tileSet.collisionSprites[ 1 ][ 0 ] = new Sprite( 0.5, 0.5 );
		tileMap.tileSet.collisionSprites[ 2 ][ 0 ] = new Sprite( 0, 0, 0.5 );
	}
	

	@Override
	public void logic() {
		Camera.current.jumpTo( player );
		Camera.current.limitWith( tileMap );
		player.act();
	}

	
	@Override
	public void render() {
		tileMap.draw();
		player.draw();
		printText( "Move pLayer with arrow Keys" );
		printText( " Coins" + coins, 1 );
		printText( "LTVectorSprite, CollisionsWithTileMap, HandleCollisionWithTile example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}