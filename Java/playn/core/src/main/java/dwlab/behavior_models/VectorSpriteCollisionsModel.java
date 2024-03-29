/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.behavior_models;

import dwlab.shapes.layers.Layer;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.SpriteAndTileCollisionHandler;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.shapes.sprites.VectorSprite;
import java.util.LinkedList;

public class VectorSpriteCollisionsModel extends BehaviorModel<VectorSprite> {
	public enum Group {
		ALL,
		HORIZONTAL,
		VERTICAL,
		LEFT,
		RIGHT,
		UP,
		DOWN
	}
	
	public LinkedList<BehaviorModel<VectorSprite>> horizontal = null, vertical = null, left = null, right = null, up = null, down = null;
	public BehaviorModel horizontalMovement = BehaviorModel.createActive();
	public BehaviorModel verticalMovement = BehaviorModel.createActive();
	public BehaviorModel collision = BehaviorModel.createActive();
	
	
	public static class LayerCollisions extends BehaviorModel<VectorSprite> {
		private Layer layer;
		private SpriteCollisionHandler handler;
		
		
		public LayerCollisions( Layer layer, SpriteCollisionHandler handler ) {
			this.layer = layer;
			this.handler = handler;
		}
		
		
		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.collisionsWith( layer, handler );
		}
	}
	
	
	public static class TileMapCollisions extends BehaviorModel<VectorSprite> {
		private TileMap map;
		private SpriteAndTileCollisionHandler handler;
		private int tileNum[];
		
		
		public TileMapCollisions( TileMap map, SpriteAndTileCollisionHandler handler, int[] tileNum ) {
			this.map = map;
			this.handler = handler;
			this.tileNum = tileNum;
		}
		
		
		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.collisionsWith( map, handler, tileNum );
		}
	}
	
	
	public static class SpriteMapCollisions extends BehaviorModel<VectorSprite> {
		private SpriteMap map;
		private SpriteCollisionHandler handler;
		
		
		public SpriteMapCollisions( SpriteMap map, SpriteCollisionHandler handler ) {
			this.map = map;
			this.handler = handler;
		}
		
		
		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.collisionsWith( map, handler );
		}
	}
	
	
	@Override
	public void applyTo( VectorSprite sprite ) {
		if( horizontalMovement.active ) {
			sprite.move( sprite.dX, 0d );
			if( collision.active ) {
				if( horizontal != null ) for( BehaviorModel<VectorSprite> model : horizontal ) model.applyTo( sprite );
				if( sprite.dX > 0 ) {
					if( right != null ) for( BehaviorModel<VectorSprite> model : right ) model.applyTo( sprite );
				} else if( sprite.dX < 0 ) {
					if( left != null ) for( BehaviorModel<VectorSprite> model : left ) model.applyTo( sprite );
				}
			}
		}
		if( verticalMovement.active ) {
			sprite.move( 0d, sprite.dY );
			if( collision.active ) {
				if( vertical != null ) for( BehaviorModel<VectorSprite> model : vertical ) model.applyTo( sprite );
				if( sprite.dY > 0 ) {
					if( down != null ) for( BehaviorModel<VectorSprite> model : down ) model.applyTo( sprite );
				} else if( sprite.dY < 0 ) {
					if( up != null ) for( BehaviorModel<VectorSprite> model : up ) model.applyTo( sprite );
				}
			}
		}
	}
}
