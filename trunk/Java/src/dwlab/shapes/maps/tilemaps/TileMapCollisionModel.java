package dwlab.shapes.maps.tilemaps;

import dwlab.behavior_models.BehaviorModel;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteAndTileCollisionHandler;

/**
 * Tile map collision model.
 * Checks collisions of shape and given tile map and launches given collision handler upon them.
 */
public class TileMapCollisionModel extends BehaviorModel<Sprite> {
	public TileMap tileMap;
	public SpriteAndTileCollisionHandler collisionHandler;

	
	public static TileMapCollisionModel create( TileMap tileMap, SpriteAndTileCollisionHandler collisionHandler ) {
		TileMapCollisionModel model = new TileMapCollisionModel();
		model.collisionHandler = collisionHandler;
		model.tileMap = tileMap;
		return model;
	}
	

	@Override
	public void applyTo( Sprite sprite ) {
		sprite.collisionsWithTileMap( tileMap, collisionHandler );
	}
}
