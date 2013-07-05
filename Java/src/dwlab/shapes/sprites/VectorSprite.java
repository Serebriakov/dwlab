/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */

package dwlab.shapes.sprites;

import dwlab.base.Project;
import dwlab.base.service.Service;
import dwlab.behavior_models.BehaviorModel;
import dwlab.behavior_models.HorizontalMovementModel;
import dwlab.behavior_models.VectorSpriteCollisionsModel;
import dwlab.behavior_models.VectorSpriteCollisionsModel.Group;
import dwlab.behavior_models.VectorSpriteCollisionsModel.LayerCollisions;
import dwlab.behavior_models.VectorSpriteCollisionsModel.SpriteMapCollisions;
import dwlab.behavior_models.VectorSpriteCollisionsModel.TileMapCollisions;
import dwlab.behavior_models.VerticalMovementModel;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.LinkedList;

/**
 * Vector sprite has horizontal and vertical velocities forming velocity vector.
 * Handy for projects with gravity, platformers for example.

 * @see #lTSprite
 */
public class VectorSprite extends Sprite {
	/**
	 * Horizontal velocity of the sprite.
	 */
	public double dX;

	/**
	 * Vertical velocity of the sprite.
	 */
	public double dY;


	public VectorSprite() {
	}
	
	public VectorSprite( ShapeType shapeType, double x, double y, double width, double height ) {
		this.setCoords( x, y );
		this.setSize( width, height );
		this.shapeType = shapeType;
	}
	
	public VectorSprite( ShapeType shapeType, double x, double y, double width, double height, double dX, double dY ) {
		this.setCoords( x, y );
		this.setSize( width, height );
		this.shapeType = shapeType;
		this.dX = dY;
		this.dX = dY;
	}


	@Override
	public String getClassTitle() {
		return "Vector sprite";
	}
	
	
	@Override
	public VectorSprite toVectorSprite() {
		return this;
	}


	@Override
	public void init() {
		updateFromAngularModel();
	}

	// ==================== Limiting ====================

	@Override
	public Sprite bounceInside( Sprite sprite, boolean leftSide, boolean topSide, boolean rightSide, boolean bottomSide, SpriteCollisionHandler handler ) {
		if( leftSide ) {
			if( leftX() < sprite.leftX() ) {
				x = sprite.leftX() + 0.5 * width;
				dX = Math.abs( dX );
				if( handler != null ) handler.handleCollision( this, sprite );
			}
		}
		if( topSide ) {
			if( topY() < sprite.topY() ) {
				y = sprite.topY() + 0.5 * height;
				dY = Math.abs( dY );
				if( handler != null ) handler.handleCollision( this, sprite );
			}
		}
		if( rightSide ) {
			if( rightX() > sprite.rightX() ) {
				x = sprite.rightX() - 0.5 * width;
				dX = -Math.abs( dX );
				if( handler != null ) handler.handleCollision( this, sprite );
			}
		}
		if( bottomSide ) {
			if( bottomY() > sprite.bottomY() ) {
				y = sprite.bottomY() - 0.5 * height;
				dY = -Math.abs( dY );
				if( handler != null ) handler.handleCollision( this, sprite );
			}
		}
		return this;
	}


	@Override
	public void updateFromAngularModel() {
		dX = Math.cos( angle ) * velocity;
		dY = Math.sin( angle ) * velocity;
	}


	public void updateAngularModel() {
		angle = Math.atan2( dY, dX );
		velocity = Service.distance( dX, dY );
	}


	@Override
	public Sprite moveForward() {
		setCoords( x + dX * Project.deltaTime, y + dY * Project.deltaTime );
		return this;
	}


	@Override
	public Sprite directTo( Shape shape ) {
		double vectorLength = Service.distance( dX, dY );
		dX = shape.getX() - x;
		dY = shape.getY() - y;
		if( vectorLength > 0 ) {
			double newVectorLength = Service.distance( dX, dY );
			if( newVectorLength > 0 ) {
				dX *= vectorLength / newVectorLength;
				dY *= vectorLength / newVectorLength;
			}
		}
		return this;
	}


	@Override
	public Sprite reverseDirection() {
		dX = -dX;
		dY = -dY;
		return this;
	}


	@Override
	public VectorSprite clone() {
		VectorSprite newSprite = new VectorSprite();
		copySpriteTo( newSprite );
		return newSprite;
	}
	
	
	public void addLayerCollisions( Group group, Layer layer, SpriteCollisionHandler handler ) {
		addToGroup( new LayerCollisions( layer, handler ), group );
	}
	
	
	public void addTileMapCollisions( Group group, TileMap map, SpriteAndTileCollisionHandler handler, int[] tileNum ) {
		addToGroup( new TileMapCollisions( map, handler, tileNum ), group );
	}
	
	
	public void addSpriteMapCollisions( Group group, SpriteMap map, SpriteCollisionHandler handler ) {
		addToGroup( new SpriteMapCollisions( map, handler ), group );
	}

	
	private void addToGroup( BehaviorModel model, Group group ) {
		VectorSpriteCollisionsModel collisions = null;
		for( BehaviorModel existingModel : behaviorModels ) {
			if( existingModel instanceof VectorSpriteCollisionsModel ) {
				collisions = (VectorSpriteCollisionsModel) existingModel;
				break;
			}
		}
		if( collisions == null ) {
			collisions = new VectorSpriteCollisionsModel();
			attachModel( collisions );
		}
		
		model.init( this );
		model.activate( this );
		model.active = true;
		
		switch( group ) {
			case HORIZONTAL:
				if( collisions.horizontal == null ) collisions.horizontal = new LinkedList<BehaviorModel<VectorSprite>>();
				collisions.horizontal.add( model );
				break;
			case ALL:
			case VERTICAL:
				if( collisions.vertical == null ) collisions.vertical = new LinkedList<BehaviorModel<VectorSprite>>();
				collisions.vertical.add( model );
				break;
			case LEFT:
				if( collisions.left == null ) collisions.left = new LinkedList<BehaviorModel<VectorSprite>>();
				collisions.left.add( model );
				break;
			case RIGHT:
				if( collisions.right == null ) collisions.right = new LinkedList<BehaviorModel<VectorSprite>>();
				collisions.right.add( model );
				break;
			case UP:
				if( collisions.up == null ) collisions.up = new LinkedList<BehaviorModel<VectorSprite>>();
				collisions.up.add( model );
				break;
			case DOWN:
				if( collisions.down == null ) collisions.down = new LinkedList<BehaviorModel<VectorSprite>>();
				collisions.down.add( model );
				break;
		}
	}
	
	public VectorSpriteCollisionsModel сollisionsModel() {
		return (VectorSpriteCollisionsModel) findModel( VectorSpriteCollisionsModel.class );
	}
	
	public HorizontalMovementModel horizontalMovementModel() {
		VectorSpriteCollisionsModel model = сollisionsModel();
		if( model == null ) return null;
		return model.horizontalMovementModel;
	}
	
	public VerticalMovementModel verticalMovementModel() {
		VectorSpriteCollisionsModel model = сollisionsModel();
		if( model == null ) return null;
		return model.verticalMovementModel;
	}
}