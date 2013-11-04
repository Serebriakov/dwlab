/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

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
	public int[] tileNum;

	
	public static TileMapCollisionModel create( TileMap tileMap, SpriteAndTileCollisionHandler collisionHandler, int[] tileNum ) {
		TileMapCollisionModel model = new TileMapCollisionModel();
		model.collisionHandler = collisionHandler;
		model.tileMap = tileMap;
		model.tileNum = tileNum;
		return model;
	}
	

	@Override
	public void applyTo( Sprite sprite ) {
		sprite.collisionsWith( tileMap, collisionHandler, tileNum );
	}
}
