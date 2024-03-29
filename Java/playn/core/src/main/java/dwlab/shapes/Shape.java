/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes;

import dwlab.base.*;
import static dwlab.base.Obj.classes;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.behavior_models.BehaviorModel;
import dwlab.behavior_models.ModelStack;
import dwlab.controllers.Button;
import dwlab.controllers.ButtonAction;
import static dwlab.platform.Functions.*;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.visualizers.Visualizer;
import dwlab.visualizers.WindowedVisualizer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Common object for item of game field.
 */
public class Shape extends Obj {
	public enum Relativity {
		BEFORE,
		INSTEAD_OF,
		AFTER
	}
	
	private LinkedList<Parameter> parameters = null;
	
	/**
	 * Shape coordinates in units.
	 * @see #getX, #setX, #getY, #setY
	 */
	protected double x, y;

	/**
	 * Shape size in units.
	 * @see #setWidth, #setHeight, #getDiameter, #setDiameter, 
	 */
	protected double width, height;

	/**
	 * Shape visualizer (object which displays this shape).
	 * @see #lTVisualizer, #lTDebugVisualizer, #l_DebugVisualizer
	 */
	public Visualizer visualizer = new Visualizer();

	/**
	 * Visibility flag.
	 * If False then shape will not be drawn.
	 * 
	 * @see #draw, #drawUsingVisualizer, #active example
	 */
	public boolean visible = true;

	/**
	 * Activity flag.
	 * If False then Act() method for shape will not be executed.
	 * 
	 * @see #act
	 */
	public boolean active = true;

	/**
	 * Behavior models list.
	 * Standard Act() method will apply every behavior model in this list to the shape.
	 * 
	 * @see #lTBehaviorModel
	 */
	public final LinkedList<BehaviorModel> behaviorModels = new LinkedList<BehaviorModel>();

	public int collisionLayer;

	// ==================== Drawing ===================

	private static final Vector servicePivot = new Vector();
	private static final Vector serviceSizes = new Vector();
	
	/**
	 * Prints text inside the shape.
	 * Current ImageFont is used. You can specify horizontal and vertical alignment and also horizontal and vertical shift in units.
	 */
	public void print( String text, Align horizontalAlign, Align verticalAlign, double horizontalShift, double verticalShift ) {
		double xX, yY;
		switch( horizontalAlign ) {
			case TO_LEFT:
				xX = leftX();
				break;
			case TO_RIGHT:
				xX = rightX();
				break;
			default:
				xX = x;
				break;
		}

		switch( verticalAlign ) {
			case TO_TOP:
				yY = topY();
				break;
			case TO_BOTTOM:
				yY = bottomY();
				break;
			default:
				yY = y;
				break;
		}

		Camera.current.fieldToScreen( xX + horizontalShift, yY + verticalShift, servicePivot );

		switch( horizontalAlign ) {
			case TO_CENTER:
				servicePivot.x -= 0.5d * getTextWidth( text );
				break;
			case TO_RIGHT:
				servicePivot.x -= getTextWidth( text );
				break;
		}

		switch( verticalAlign ) {
			case TO_CENTER:
				servicePivot.y -= 0.5d * getTextHeight();
				break;
			case TO_BOTTOM:
				servicePivot.y -= getTextHeight();
				break;
		}

		drawText( text, servicePivot.x, servicePivot.y );
	}
	
	public void print( String text ) {
		print( text, Align.TO_CENTER, Align.TO_CENTER, 0, 0 );
	}
	
	public void print( String text, Align horizontalAlign, Align verticalAlign ) {
		print( text, horizontalAlign, verticalAlign, 0, 0 );
	}	
	
	
	private static final Vector serviceVector1 = new Vector();
	private static final Vector serviceVector2 = new Vector();
	
	public void drawContour( double contourLineWidth, double xScale, double yScale ) {
		double oldLineWidth = lineWidth;
		lineWidth = contourLineWidth;
		Camera.current.fieldToScreen( x, y, serviceVector1 );
		Camera.current.sizeFieldToScreen( width * xScale, height * yScale, serviceVector2 );
		drawEmptyRectangle( serviceVector1.x, serviceVector1.y, serviceVector2.x, serviceVector2.y );
		lineWidth = oldLineWidth;
	}

	public void drawContour( double lineWidth ) {
		drawContour( lineWidth, 1d, 1d );
	}


	/**
	 * Sets shape's rectangle as viewport.
	 */
	public void setAsViewport() {
		Camera.current.fieldToScreen( x, y, servicePivot );
		Camera.current.sizeFieldToScreen( width, height, serviceSizes );
		if( servicePivot.x < 0 ) {
			serviceSizes.x += servicePivot.x;
			servicePivot.x = 0;
		}
		if( servicePivot.y < 0 ) {
			serviceSizes.y += servicePivot.y;
			servicePivot.y = 0;
		}
		setViewport( Service.round( servicePivot.x ), Service.round( servicePivot.y ), Service.round( serviceSizes.x ), Service.round( serviceSizes.y ) );
	}

	// ==================== Collisions ===================

	public Sprite layerLastSpriteCollision( Sprite sprite ) {
		return null;
	}



	public void spriteLayerCollisions( Sprite sprite, SpriteCollisionHandler handler ) {
	}

	// ==================== Position ====================

	public double getX() {
		return x;
	}
	
	
	public Shape setX( double newX ) {
		setCoords( newX, y );
		return this;
	}


	public double getY() {
		return y;
	}


	public Shape setY( double newY ) {
		setCoords( x, newY );
		return this;
	}


	/**
	 * Distance to point.
	 * @return Distance from the shape center to the point with given coordinates.
	 * @see #distanceTo
	 */
	public double distanceTo( double pointX, double pointY ) {
		double dX = x - pointX;
		double dY = y - pointY;
		return Math.sqrt( dX * dX + dY * dY );
	}


	/**
	 * Distance to shape.
	 * @return Distance from the shape center to center of another shape.
	 * @see #distanceToPoint, #distanceToPoint example
	 */
	public double distanceTo( Shape shape ) {
		double dX = x - shape.x;
		double dY = y - shape.y;
		return Math.sqrt( dX * dX + dY * dY );
	}


	public double distance2to( Shape shape ) {
		double dX = x - shape.x;
		double dY = y - shape.y;
		return dX * dX + dY * dY;
	}


	/**
	 * Checks if the shape is at position of another shape.
	 * @return True if shape center has same coordinates as another shape center. 
	 * @see #x, #y, #moveTowards example
	 */
	public boolean isAtPositionOf( Shape shape ) {
		return shape.x == x && shape.y == y;
	}


	public boolean isAtPositionOf( double pointX, double pointY ) {
		return pointX == x && pointY == y;
	}


	/**
	 * Sets coordinates of the shape.
	 * It's better to use this method instead of equating X and Y fields to new values.
	 * 
	 * @see #x, #y, #setCornerCoords, #alterCoords, #setMouseCoords
	 */
	public Shape setCoords( double newX, double newY ) {
		x = newX;
		y = newY;
		update();
		return this;
	}


	/**
	 * Alter coordinates of the shape.
	 * Given values will be added to the coordinates. It's better to use this method instead of incrementing X and Y fields manually.
	 * 
	 * @see #setCoords, #setCornerCoords, #setMouseCoords, #clone example
	 */
	public Shape alterCoords( double dX, double dY ) {
		setCoords( x + dX, y + dY );
		return this;
	}


	/**
	 * Moves vector to mouse position.
	 * Mouse coordinates will be transformed to field coordinates using current camera. Then shape coordinates will be equated to these.
	 * 
	 * @see #setCoords, #placeBetween example
	 */
	public Shape setMouseCoords( Camera camera ) {
		camera.screenToField( mouseX, mouseY, Camera.servicePivot );
		setCoords( Camera.servicePivot.x, Camera.servicePivot.y );
		return this;
	}
	
	
	public Shape setMouseCoords() {
		setMouseCoords( Camera.current );
		return this;
	}


	public Shape setCoordsRelativeTo( Sprite sprite, double newX, double newY ) {
		double spriteAngle = directionTo( newX, newY ) + sprite.angle;
		double radius = Math.sqrt( newX * newX + newY * newY );
		setCoords( sprite.x + radius * Math.cos( spriteAngle ), sprite.y + radius * Math.sin( spriteAngle ) );
		return this;
	}


	/**
	 * Position vector using coordinates in tilemap's coordinate system
	 * Integer TileX and TileY sets shape position to the center of given tilemap's cooresponding tile
	 */
	public Shape positionOn( TileMap tileMap, double tileX, double tileY ) {
		x = tileMap.leftX() + ( tileX + 0.5d ) * tileMap.getTileWidth();
		y = tileMap.topY() + ( tileY + 0.5d ) * tileMap.getTileHeight();
		return this;
	}


	/**
	 * Moves the vector.
	 * The shape will be moved with given horizontal and vertical speed per second.
	 * 
	 * @see #lTButtonAction example
	 */
	public Shape move( double dX, double dY ) {
		setCoords( x + dX * Project.deltaTime, y + dY * Project.deltaTime );
		return this;
	}
	

	/**
	 * Left side of the shape.
	 * @return X coordinate of left shape side in units.
	 * @see RightX#, TopY#, BottomY#, #x, #width
	 */
	public double leftX() {
 		return x - 0.5d * width;
 	}


	/**
	 * Top of the shape.
	 * @return Y coordinate of shape top in units.
	 * @see LeftX#, RightX#, BottomY#, #y, #height
	 */
	public double topY() {
 		return y - 0.5d * height;
 	}


	/**
	 * Right side of the shape.
	 * @return X coordinate of right shape side in units.
	 * @see #leftX, #topY, #bottomY, #x, #width
	 */
	public double rightX() {
 		return x + 0.5d * width;
 	}


	/**
	 * Bottom of the shape
	 * @return Y coordinate of shape bottom in units.
	 * @see LeftX#, RightX#, TopY#, #y, #height
	 */
	public double bottomY() {
 		return y + 0.5d * height;
 	}


	public Shape setCoordsAndSize( double x1, double y1, double x2, double y2 ) {
		x = 0.5d * ( x1 + x2 );
		y = 0.5d * ( y1 + y2 );
		width = x2 - x1;
		height = y2 - y1;
		update();
		return this;
	}


	/**
	 * Sets top-left corner coordinates of the shape.
	 * After this operation top-left corner of the shape will be at given coordinates.
	 * 
	 * @see #setCoords, #alterCoords, #setMouseCoords
	 */
	public Shape setCornerCoords( double newX, double newY ) {
		setCoords( newX + width * 0.5d, newY + height * 0.5d );
		return this;
	}


	/**
	 * Moves vector to another one.
	 * Center coordinates of the shape will be equated to corresponding center coordinates of given shape.
	 * 
	 * @see #isAtPositionOf, #setCoords
	 */
	public Shape jumpTo( Shape shape ) {
		setCoords( shape.x , shape.y );
		return this;
	}


	/**
	 * Moves the shape with given velocity towards shape.
	 * @see #moveForward, #moveBackward
	 */
	public Shape moveTowards( Shape shape, double velocity ) {
		moveTowards( shape.x, shape.y, velocity );
		return this;
	}


	/**
	 * Moves the shape with given velocity towards shape.
	 * @see #moveForward
	 */
	public Shape moveTowards( double destinationX, double destinationY, double velocity ) {
		double angle = directionTo( destinationX, destinationY );
		double dX = Math.cos( angle ) * velocity * Project.deltaTime;
		double dY = Math.sin( angle ) * velocity * Project.deltaTime;
		if( Math.abs( dX ) >= Math.abs( x - destinationX ) && Math.abs( dY ) >= Math.abs( y - destinationY ) ) {
			setCoords( destinationX, destinationY );
		} else {
			setCoords( x + dX, y + dY );
		}
		return this;
	}


	/**
	 * Places the shape between two another shapes.
	 * K parameter is in 0...1 interval.
	 * <ul>
	 * <li> 0 shifts shape to the center of first given shape.
	 * <li> 1 shifts shape to the center of the second given shape.
	 * <li> 0.5 shifts shape to the middle between given shapes centers.
	 * </ul>
	 */
	public Shape placeBetween( Shape shape1, Shape shape2, double k ) {
		setCoords( shape1.x + ( shape2.x - shape1.x ) * k, shape1.y + ( shape2.y - shape1.y ) * k );
		return this;
	}


	private static final ButtonAction[] keysWSAD = {
		ButtonAction.create( "up", Button.Name.W ),
		ButtonAction.create( "down", Button.Name.S ), 
		ButtonAction.create( "left", Button.Name.A ), 
		ButtonAction.create( "right", Button.Name.D )
	};

	/**
	 * Allowing moving the shape around with given velocity with WSAD keys.
	 * @see #moveUsingArrows, #moveUsingKeys, #move
	 */
	public Shape moveUsingWSAD( double velocity ) {
		moveUsingKeys( keysWSAD, velocity );
		return this;
	}


	private static final ButtonAction[] keysArrows = {
		ButtonAction.create( "up", Button.Name.UP ),
		ButtonAction.create( "down", Button.Name.DOWN ), 
		ButtonAction.create( "left", Button.Name.LEFT ), 
		ButtonAction.create( "right", Button.Name.RIGHT )
	};

	/**
	 * Allowing moving the shape around with given velocity with Arrow keys.
	 * @see #moveUsingWSAD, #moveUsingKeys, #move
	 */
	public Shape moveUsingArrows( double velocity ) {
		moveUsingKeys( keysArrows, velocity );
		return this;
	}


	/**
	 * Allowing moving the shape around with with given keys and velocity.
	 * @see #moveUsingArrows, #moveUsingWSAD, #move
	 */
	public Shape moveUsingKeys( ButtonAction[] keys, double velocity ) {
		double dX = ( keys[ 2 ].isDown() ? -1d : ( keys[ 3 ].isDown() ? 1d : 0d ) );
		double dY = ( keys[ 0 ].isDown() ? -1d : ( keys[ 1 ].isDown() ? 1d : 0d ) );
	
		Camera.current.sizeScreenToField( dX, dY, servicePivot );

		if( servicePivot.x == 0 && servicePivot.y == 0 ) return this;

		double k = velocity / Service.distance( servicePivot.x, servicePivot.y ) * Project.deltaTime;
		setCoords( x + servicePivot.x * k, y + servicePivot.y * k );
		return this;
	}


	/**
	 * Applies parallax effect for shape depending on current camera size and position relative to given shape.
	 */
	public Shape parallax( Shape shape ) {
		double dX = shape.getWidth() - Camera.current.getWidth();
		double dY = shape.getHeight() - Camera.current.getHeight();
		setCoords( shape.leftX() + 0.5 * width + ( Camera.current.leftX() - shape.leftX() ) * ( shape.getWidth() - width ) / dX,
			shape.topY() + 0.5 * height + ( Camera.current.topY() - shape.topY() ) * ( shape.getHeight() - height ) / dY );
		return this;
	}

	// ==================== Limiting ====================

	/**
	 * Limits shape with given rectangular shape.
	 * If the shape is outside given shape, it will be moved inside it. If the shape is larger than given shape, it will be moved to the center of given shape.
	 * 
	 * @see #limitHorizontallyWith, #limitVerticallyWith, #limitLeftWith, #limitRightWith, #limitTopWith, #limitBottomWith
	 */
	public Shape limitWith( Shape rectangle, SpriteCollisionHandler handler ) {
		limitHorizontallyWith( rectangle, handler );
		limitVerticallyWith( rectangle, handler );
		return this;
	}

	public Shape limitWith( Shape rectangle ) {
		limitHorizontallyWith( rectangle );
		limitVerticallyWith( rectangle );
		return this;
	}


	/**
	 * Keeps shape within limits of given shape horizontally.
	 * @see #limitWith, #limitVerticallyWith, #limitLeftWith, #limitRightWith, #limitTopWith, #limitBottomWith
	 */
	public Shape limitHorizontallyWith( Shape rectangle, SpriteCollisionHandler handler ) {
		limitHorizontallyWith( rectangle );
		return this;
	}
	
	public Shape limitHorizontallyWith( Shape rectangle ) {
		double x1 = Math.min( rectangle.x, rectangle.leftX() + 0.5 * width );
		double x2 = Math.max( rectangle.x, rectangle.rightX() - 0.5 * width );
		setX( Service.limit( x, x1, x2 ) );
		return this;
	}



	/**
	 * Keeps shape within limits of given shape vertically.
	 * @see #limitWith, #limitHorizontallyWith, #limitLeftWith, #limitRightWith, #limitTopWith, #limitBottomWith
	 */
	public Shape limitVerticallyWith( Shape rectangle, SpriteCollisionHandler handler ) {
		limitVerticallyWith( rectangle );
		return this;
	}

	public Shape limitVerticallyWith( Shape rectangle ) {
		double y1 = Math.min( rectangle.y, rectangle.topY() + 0.5 * height );
		double y2 = Math.max( rectangle.y, rectangle.bottomY() - 0.5 * height );
		setY( Service.limit( y, y1, y2 ) );
		return this;
	}


	/**
	 * Limits left side of the shape with left side of given rectangular shape.
	 * If the left side X coordinate of shape is less than left side X coordinate of given shape, left side of the shape will be equated to left side of given shape.
	 * 
	 * @see #limitWith, #limitHorizontallyWith, #limitVerticallyWith, #limitRightWith, #limitTopWith, #limitBottomWith
	 */
	public Shape limitLeftWith( Shape rectangle, SpriteCollisionHandler handler ) {
		limitLeftWith( rectangle );
		return this;
	}
	
	public Shape limitLeftWith( Shape rectangle ) {
		if( leftX() < rectangle.leftX() ) setX( rectangle.leftX() + 0.5 * width );		
		return this;
	}


	/**
	 * Limits top of the shape with top of given rectangular shape.
	 * If the top Y coordinate of shape is less than top Y coordinate of given shape, top of the shape will be equated to the top of given shape.
	 * 
	 * @see #limitWith, #limitHorizontallyWith, #limitVerticallyWith, #limitLeftWith, #limitRightWith, #limitBottomWith
	 */
	public Shape limitTopWith( Shape rectangle, SpriteCollisionHandler handler ) {
		limitTopWith( rectangle );
		return this;
	}

	public Shape limitTopWith( Shape rectangle ) {
		if( topY() < rectangle.topY() ) setY( rectangle.topY() + 0.5 * height );		
		return this;
	}


	/**
	 * Limits right side of the shape with right side of given rectangular shape.
	 * If the right side X coordinate of shape is more than right side X coordinate of given shape, right side of the shape will be equated to right side of given shape.
	 * 
	 * @see #limitWith, #limitHorizontallyWith, #limitVerticallyWith, #limitLeftWith, #limitTopWith, #limitBottomWith
	 */
	public Shape limitRightWith( Shape rectangle, SpriteCollisionHandler handler ) {
		limitRightWith( rectangle );
		return this;
	}

	public Shape limitRightWith( Shape rectangle ) {
		if( rightX() > rectangle.rightX() ) setX( rectangle.rightX() - 0.5 * width );		
		return this;
	}


	/**
	 * Limits bottom of the shape with bottom of given rectangular shape.
	 * If the bottom Y coordinate of shape is more than bottom Y coordinate of given shape, bottom of the shape will be equated to the bottom of given shape.
	 * 
	 * @see #limitWith, #limitHorizontallyWith, #limitVerticallyWith, #limitLeftWith, #limitRightWith, #limitTopWith
	 */
	public Shape limitBottomWith( Shape rectangle, SpriteCollisionHandler handler ) {
		limitBottomWith( rectangle );
		return this;
	}
	
	public Shape limitBottomWith( Shape rectangle ) {
		if( bottomY() > rectangle.bottomY() ) setY( rectangle.bottomY() - 0.5 * height );
		return this;
	}

	// ==================== Size ====================

	public double getWidth() {
		return width;
	}
	
	public Shape setWidth( double newWidth )	 {
		setSize( newWidth, height );
		return this;
	}



	public double getHeight() {
		return height;
	}
	
	public Shape setHeight( double newHeight )	 {
		setSize( width, newHeight );
		return this;
	}


	/**
	 * Returns diameter of circular shape.
	 * @return Width field of the shape.
	 * @see #setDiameter
	 */
	public double getDiameter() {
		return width;
	}


	/**
	 * Sets the diameter of the shape.
	 * @see #getDiameter
	 */
	public Shape setDiameter( double newDiameter ) {
		setSize( newDiameter, newDiameter );
		return this;
	}


	/**
	 * Sets the size of the shape.
	 * It's better to use this method instead of equating Width and Height fields to new values.
	 * 
	 * @see #width, #height, #setWidth, #setHeight, #setSizeAs, #alterSize
	 */
	public Shape setSize( double newWidth, double newHeight ) {
		width = newWidth;
		height = newHeight;
		update();
		return this;
	}


	/**
	 * Sets the size of the shape as of given shape.
	 * @see #width, #height, #setWidth, #setHeight, #setSize, #alterSize, #directAs example
	 */
	public Shape setSizeAs( Shape shape ) {
		setSize( shape.width, shape.height );
		return this;
	}


	/**
	 * Alters the size of the shape.
	 * It's better to use this method instead of equating Width and Height fields to new values.
	 * 
	 * @see #width, #height, #setWidth, #setHeight, #setSize, #setSizeAs, #stretch example
	 */
	public Shape alterSize( double dWidth, double dHeight ) {
		width *= dWidth;
		height *= dHeight;
		update();
		return this;
	}


	/**
	 * Alters both sizes of the shape (pretending they are equal).
	 * It's better to use this method instead of equating Width and Height fields to new values.
	 * 
	 * @see #clone example
	 */
	public Shape alterDiameter( double d ) {
		width *= d;
		height *= d;
		update();
		return this;
	}


	/**
	 * Corrects height to display shape image with no distortion.
	 * After this operation ratio of width to height will be the same as ratio of image width to image height.
	 * 
	 * @see #height, #setHeight, #visualizer
	 */
	public Shape correctHeight() {
		Image image = visualizer.image;
		setSize( width, width * image.getHeight() / image.getWidth() );
		return this;
	}


	public enum Facing {
		LEFT,
		RIGHT
	}

	/**
	 * Returns shape facing.
	 * @return Shape facing
	 * 
	 * <ul>
	 * <li> Returns -1.0 if shape is facing left (LeftFacing constant) 
	 * <li> Returns +1.0 if shape is facing right (RightFacing constant).
	 * </ul>
	 * Equal to the sign of visualizer XScale field.
	 * 
	 * @see #setFacing, #xScale
	 */
	public Facing getFacing() {
		return visualizer.getFacing();
	}

	/**
	 * Sets the facing of a shape.
	 * Use LeftFacing and RightFacing constants.
	 * @see #getFacing, #xScale
	 */
	public Shape setFacing( Facing newFacing ) {
		visualizer.setFacing( newFacing );
		return this;
	}

	// ==================== Angle ====================

	/**
	 * Direction to the point.
	 * @return Angle between vector from the center of the shape to the point with given coordinates and X axis.
	 * @see #directionTo, #distanceToPoint example
	 */
	public double directionTo( double pointX, double pointY ) {
		return Math.atan2( pointY - y, pointX - x );
	}


	/**
	 * Direction to shape.
	 * @return Angle between vector from the center of this shape to center of given shape and X axis.
	 * @see #directionToPoint, #distanceToPoint example
	 */
	public double directionTo( Shape shape ) {
		return Math.atan2( shape.y - y, shape.x - x );
	}

	// ==================== Behavior models ===================

	
	private class BehaviorModelAttacher extends Obj {
		BehaviorModel model;
		Shape shape;
		
		public BehaviorModelAttacher( Shape shape, BehaviorModel model ) {
			this.shape = shape;
			this.model = model;
		}
		
		@Override
		public void act() {
			shape.behaviorModels.add( model );
		}
	}
	
	
	/**
	 * Attaches behavior model to the shape.
	 * Model will be initialized and activated if necessary.
	 * 
	 * @see #lTBehaviorModel, #activate
	 */
	public Shape attachModel( BehaviorModel model, boolean activate ) {
		Project.managers.add( new BehaviorModelAttacher( this, model ) );
		model.init( this );
		if( activate ) {
			model.activate( this );
			model.active = true;
		}
		return this;
	}
	
	public Shape attachModel( BehaviorModel model ) {
		return attachModel( model, true );
	}
	
	public Shape attachModelImmediately ( BehaviorModel model, boolean activate ) {
		behaviorModels.add( model );
		model.init( this );
		if( activate ) {
			model.activate( this );
			model.active = true;
		}
		return this;
	}
	
	public Shape attachModelImmediately( BehaviorModel model ) {
		return attachModelImmediately( model, true );
	}


	/**
	 * Attaches list of behavior model to the shape.
	 */
	public Shape attachModels( LinkedList<BehaviorModel> models, boolean activate ) {
		for( BehaviorModel model: models ) {
			attachModel( model, activate );
		}
		return this;
	}
	
	public Shape attachModels( LinkedList<BehaviorModel> models ) {
		return attachModels( models, true );
	}


	/**
	 * Finds behavior model by its class name.
	 * @return First behavior model with the class of given name.
	 * @see #lTBehaviorModel
	 */
	public BehaviorModel findModel( Class modelClass ) {
		for( BehaviorModel model: behaviorModels ) {
			if( model.getClass() == modelClass ) return model;
		}
		return null;
	}


	/**
	 * Activates all behavior models of the shape.
	 * Executes Activate() method of all deactivated models and set their Active field to True.
	 * 
	 * @see #deactivateAllModels, #lTBehaviorModel, #activate
	 */
	public Shape activateAllModels() {
		for( BehaviorModel model: behaviorModels ) {
			if( ! model.active ) {
				model.activate( this );
				model.active = true;
			}
		}
		return this;
	}


	/**
	 * Deactivates all behavior models of the shape.
	 * Executes Deactivate() method of all activated models and set their Active field to False.
	 * 
	 * @see #activateAllModels, #lTBehaviorModel, #deactivate
	 */
	public Shape deactivateAllModels() {
		for( BehaviorModel model: behaviorModels ) {
			if( model.active ) {
				model.deactivate( this );
				model.active = false;
			}
		}
		return this;
	}


	/**
	 * Activates shape behavior models of class with given name.
	 * Executes Activate() method of all inactive models of class with given name and set their Active field to True.
	 * 
	 * @see #deactivateModel, #toggleModel, #lTBehaviorModel, #activate
	 */
	public Shape activateModel( Class modelClass ) {
		for( BehaviorModel model: behaviorModels ) {
			if( model.getClass() == modelClass && !model.active ) {
				model.activate( this );
				model.active = true;
			}
		}
		return this;
	}


	/**
	 * Deactivates shape behavior models of class with given name.
	 * Executes Deactivate() method of all active models of class with given name and set their Active field to False.
	 * 
	 * @see #activateModel, #toggleModel, #lTBehaviorModel, #deactivate
	 */
	public Shape deactivateModel( Class modelClass ) {
		for( BehaviorModel model: behaviorModels ) {
			if( model.getClass() == modelClass && model.active ) {
				model.deactivate( this );
				model.active = false;
			}
		}
		return this;
	}


	/**
	 * Toggles activity of shape behavior models of class with given name.
	 * Executes Activate() method of all inactive and Deactivate() method of all active models of class with given name and toggles their Active field.
	 * 
	 * @see #activateModel, #deactivateModel, #lTBehaviorModel, #activate, #deactivate
	 */
	public Shape toggleModel( Class modelClass ) {
		for( BehaviorModel model: behaviorModels ) {
			if( model.getClass() == modelClass && model.active ) {
				if( model.active ) {
					model.deactivate( this );
					model.active = false;
				} else {
					model.activate( this );
					model.active = true;
				}
			}
		}
		return this;
	}


	public Shape removeModel( BehaviorModel model ) {
		model.remove( this );
		return this;
	}
	
	
	/**
	 * Removes all shape behavior models of class with given name.
	 * Active models will be deactivated before removal.
	 * 
	 * @see #lTBehaviorModel, #deactivate
	 */
	public Shape removeModel( Class modelClass ) {
		for( BehaviorModel shapeModel: behaviorModels ) {
			if( shapeModel.getClass() == modelClass ) shapeModel.remove( this );
		}
		return this;
	}


	/**
	 * Removes every other behavior model of same type from shape's behavior models.
	 * @see #remove
	 */
	public final Shape removeSame( BehaviorModel model ) {
		Class modelClass = model.getClass();
		for( BehaviorModel shapeModel: behaviorModels ) {
			if( shapeModel.getClass() == modelClass ) shapeModel.remove( this );
		}
		return this;
	}

	
	public void addToStack( BehaviorModel animationModel, boolean activate ) {
		ModelStack stack = (ModelStack) findModel( ModelStack.class );
		if( stack == null ) {
			stack = new ModelStack();
			attachModelImmediately( stack );
		}
		stack.add( animationModel, activate );
	}
	
	public void addToStack( BehaviorModel animationModel ) {
		addToStack( animationModel, true );
	}
	

	/**
	 * Shows all behavior models attached to shape with their status.
	 */
	public int showModels( int y, String shift ) {
		if( behaviorModels.isEmpty() ) return y;
		drawText( shift + getTitle() + " ", 0, y );
		y += 16;
		for( BehaviorModel model: behaviorModels ) {
			String activeString;
			if( model.active ) activeString = "active"; else activeString =  "inactive";
			drawText( shift + model.getClass().getName() + " " + activeString + ", " + model.info( this ), 8, y );
			y += 16;
	    }
		return y;
	}
	
	public void showModels() {
		showModels( 0, "" );
	}

	// ==================== Windowed Visualizer ====================

	/**
	 * Limits sprite displaying by window with given parameters.
	 * These parameters forms a rectangle on game field which will be viewport for displaying the sprite.
	 * All sprite parts which are outside this rectangle will not be displayed.
	 * 
	 * @see #limitByWindowShape, #removeWindowLimit
	 */
	public Shape limitByWindow( double wX, double wY, double wWidth, double wHeight ) {
		WindowedVisualizer newVisualizer = new WindowedVisualizer();
		newVisualizer.visualizer = visualizer;
		newVisualizer.viewports = new Shape[ 1 ];
		Shape viewport = new Shape();
		viewport.x = wX;
		viewport.y = wY;
		viewport.width = wWidth;
		viewport.height = wHeight;
		newVisualizer.viewports[ 1 ] = viewport;
		visualizer = newVisualizer;
		return this;
	}


	/**
	 * Limits sprite displaying by given rectangular shape.
	 * All sprite parts which are outside this rectangle will not be displayed.
	 * 
	 * @see #limitByWindow, #removeWindowLimit
	 */
	public Shape limitByWindowShape( Shape shape ) {
		WindowedVisualizer newVisualizer = new WindowedVisualizer();
		newVisualizer.visualizer = visualizer;
		newVisualizer.viewports = new Shape[ 1 ];
		newVisualizer.viewports[ 0 ] = new Shape();
		shape.copyShapeTo( newVisualizer.viewports[ 0 ] );
		visualizer = newVisualizer;
		return this;
	}


	public Shape limitByWindowShapes( Shape[] shapes ) {
		WindowedVisualizer newVisualizer = new WindowedVisualizer();
		newVisualizer.visualizer = visualizer;
		newVisualizer.viewports = new Shape[ shapes.length ];
		for( int n = 0; n < shapes.length; n++ ) {
			newVisualizer.viewports[ n ] = new Shape();
			shapes[ n ].copyShapeTo( newVisualizer.viewports[ n ] );
		}
		visualizer = newVisualizer;
		return this;
	}


	/**
	 * Removes window limit.
	 * After executing this method the sprite will be displayed as usual.
	 * 
	 * @see #limitByWindow, #limitByWindowShape
	 */
	public Shape removeWindowLimit() {
		visualizer = ( (WindowedVisualizer) visualizer ).visualizer;
		return this;
	}

	// ==================== Parameters ===================	

	public boolean parameterExists( String name ) {
		if( parameters != null ) {
			for( Parameter parameter: parameters ) {
				if( parameter.name.equals( name ) ) return true;
			}
		}
		return false;
	}

	
	/**
	 * Retrieves value of object's parameter with given name.
	 * @return Value of object's parameter with given name.
	 * @see #getTitle, #getName, #lTBehaviorModel example.
	 */
	public String getParameter( String name ) {
		if( parameters != null ) {
			for( Parameter parameter: parameters ) {
				if( parameter.name.equals( name ) ) return parameter.value;
			}
		}
		return "";
	}
	
	public int getIntegerParameter( String name ) {
		return Integer.parseInt( getParameter( name ) );
	}
	
	public double getDoubleParameter( String name ) {
		return Double.parseDouble( getParameter( name ) );
	}
	
	public int[] getIntegerParameters( String name, String separator ) {
		String chunks[] = getParameter( name ).split( separator );
		int[] params = new int[ chunks.length ];
		for( int n = 0; n < chunks.length; n++ ) params[ n ] = Integer.parseInt( chunks[ n ] );
		return params;
	}
	
	public int[] getIntegerParameters( String name ) {
		return getIntegerParameters( name, "," );
	}
	
	public double[] getDoubleParameters( String name, String separator ) {
		String chunks[] = getParameter( name ).split( separator );
		double[] params = new double[ chunks.length ];
		for( int n = 0; n < chunks.length; n++ ) params[ n ] = Double.parseDouble( chunks[ n ] );
		return params;
	}
	
	public double[] getDoubleParameters( String name ) {
		return getDoubleParameters( name, "," );
	}


	public String getTitle() {
		return TitleGenerator.current.getTitle( this );
	}


	public String getClassTitle() {
		return "";
	}


	/**
	 * Retrieves name of object.
	 * @return Value of object's parameter "name".
	 * @see #getParameter, #getTitle
	 */
	public String getName() {
		return getParameter( "name" );
	}


	/**
	 * Sets shape parameter  with given name and value.
	 * Recommended to use it only if you build your own world via code.
	 * 
	 * @see #getParameter
	 */
	public Shape setParameter( String name, String value ) {
		if( parameters != null ) {
			for( Parameter parameter: parameters ) {
				if( parameter.name.equals( name ) ) {
					parameter.value = value;
					return this;
				}
			}
		}
		addParameter( name, value );
		return this;
	}


	/**
	 * Adds parameter with given name and value to the shape.
	 * Recommended to use it only if you build your own world via code.
	 * 
	 * @see #getParameter
	 */
	public Shape addParameter( String name, String value ) {
		Parameter parameter = new Parameter();
		parameter.name = name;
		parameter.value = value;
		if( parameters != null ) parameters = new LinkedList<Parameter>();
		parameters.addLast( parameter );
		return this;
	}


	/**
	 * Removes parameter with given name from the shape.
	 * Recommended to use it only if you build your own world via code.
	 * 
	 * @see #getParameter
	 */
	public Shape removeParameter( String name ) {
		if( parameters == null ) return this;
		for ( Iterator<Parameter> iterator = parameters.iterator(); iterator.hasNext(); ) {
			if( iterator.next().name.equals( name ) ) iterator.remove();
		}
		return this;
	}

	// ==================== Search ===================

	public Shape load() {
		return loadShape();
	}


	public Shape loadShape() {
		Shape newShape = null;
		try {
			if( parameterExists( "class" ) ) {
				Class newShapeClass = classes.get( getParameter( "class" ) );
				newShape = (Shape) newShapeClass.newInstance();
			} else {
				newShape = (Shape) getClass().newInstance();
			}
		} catch ( Exception ex ) {
			Logger.getLogger( Shape.class.getName() ).log( Level.SEVERE, null, ex );
		}
		copyTo( newShape );
		return newShape;
	}


	/**
	 * Finds shape by parameter of given name and value.
	 * @return First found layer shape with parameter of given name and value.
	 */
	
	public Shape findShape( String parameterName, String parameterValue ) {
		if( getParameter( parameterName ).equals( parameterValue ) || parameterName.isEmpty() ) return this; else return null;
	}
	
	/**
	 * Finds shape by name.
	 * @return First found shape with given name.
	 * 
	 * @see #parallax example
	 */
	public Shape findShape( String name ) {
		return findShape( "name", name );
	}


	public Shape findShape( Class shapeClass ) {
		if( getClass() == shapeClass ) return this; else return null;
	}


	/**
	 * Finds shape by given class and parameter of given name and value.
	 * @return First found layer shape of given class and parameter of given name and value.
	 */
	public Shape findShape( String parameterName, String parameterValue, Class shapeClass ) {
		if( getClass() == shapeClass ) {
			if( getParameter( parameterName ).equals( parameterValue ) || parameterName.isEmpty() ) return this;
		}
		return null;
	}

	/**
	 * Finds shape by name and class.
	 * @return First found shape with specified name of given class.
	 * 
	 * @see #parallax example
	 */
	public Shape findShape( String name, Class shapeClass ) {
		return findShape( "name", name, shapeClass );
	}


	/**
	 * Inserts the shape before given.
	 * Included layers and sprite maps will be also checked for given shape.
	 */
	public boolean insert( Shape shape, Shape pivotShape, Relativity relativity ) {
		return false;
	}

	/**
	 * Inserts collection of shapes before given.
	 * Included layers and sprite maps will be also checked for given shape.
	 */
	public boolean insert( Collection<Shape> shapes, Shape pivotShape, Relativity relativity ) {
		return false;
	}


	/**
	 * Removes the shape from layer.
	 * Included layers and sprite maps will be also processed.
	 */
	public Shape remove( Shape shape ) {
		return this;
	}

	/**
	 * Removes all shapes of class with given name from layer.
	 * Included layers will be also processed.
	 */
	public Shape remove( Class shapeClass ) {
		return this;
	}
	
	
	private class LayerInserter extends Obj {
		Layer layer;
		Shape shape;
		
		@Override
		public void act() {
			layer.addLast( shape );
		}
	}
	
	public void insertTo( Layer layer ) {
		LayerInserter inserter= new LayerInserter();
		inserter.layer = layer;
		inserter.shape = this;
		Project.managers.add( inserter );
	}
	
	
	private class LayerRemover extends Obj {
		Layer layer;
		Shape shape;
		
		@Override
		public void act() {
			layer.remove( shape );
		}
	}
	
	public void removeFrom( Layer layer ) {
		LayerRemover remover = new LayerRemover();
		remover.layer = layer;
		remover.shape = this;
		Project.managers.add( remover );
	}

	// ==================== Management ===================

	/**
	 * Acting method of the shape.
	 * Fill it with the shape acting commands. By default this method applies all behavior models of the shape to the shape, so if
	 * you want to have this action inside your own Act() method, use Super.Act() command.
	 * @see #lTBehaviorModel, #applyTo, #watch
	 */
	@Override
	public void act() {
		if( active ) {
			for( BehaviorModel model: behaviorModels ) {
				if( model.active ) {
					model.applyTo( this );
				} else {
					model.watch( this );
				}
			}

			if ( debug ) {
				Project.spriteActed = true;
				Project.spritesActed += 1;
			}
		}
	}


	public Shape hide() {
		active = false;
		visible = false;
		return this;
	}


	public boolean physics() {
		return false;
	}
	
	
	public int countSprites() {
		return 1;
	}

	// ==================== Methods for rectangle ====================	

	public void getBounds( Service.Margins margins ) {
		double dWidth = 0.5d * width;
		double dHeight = 0.5 * height;
		margins.min.x = x - dWidth;
		margins.min.y = y - dHeight;
		margins.max.x = x + dWidth;
		margins.max.y = y + dHeight;
	}
	
	
	public void getBounds( Shape pivot1, Shape pivot2, Shape pivot3, Shape pivot4 ) {
		double dWidth = 0.5d * width;
		double dHeight = 0.5d * height;
		if( pivot1 != null ) pivot1.setX( x - dWidth );
		if( pivot2 != null ) pivot2.setY( y - dHeight );
		if( pivot3 != null ) pivot3.setX( x + dWidth );
		if( pivot4 != null ) pivot4.setY( y + dHeight );
	}
	
	
	public void getBounds( Shape pivot1, Shape pivot2 ) {
		getBounds( pivot1, pivot2, pivot1, pivot2 );
	}
	
	
	public void getPivots( Shape pivot1, Shape pivot2, Shape pivot3, Shape pivot4 ) {
		getBounds( pivot1, pivot1, pivot3, pivot3 );
		getBounds( pivot4, pivot2, pivot2, pivot4 );
	}

	// ==================== Cloning ===================

	/**
	 * Clones the shape.
	 * @return Clone of the shape.
	 */
	@Override
	public Shape clone() {
		Shape newShape = new Shape();
		copyShapeTo( newShape );
		return newShape;
	}


	public void copyShapeTo( Shape shape ) {
		shape.parameters = parameters;
		if( visualizer != null ) shape.visualizer = visualizer.clone();
		shape.x = x;
		shape.y = y;
		shape.width = width;
		shape.height = height;
		shape.visible = visible;
		shape.active = active;
	}


	public void copyTo( Shape shape ) {
		copyShapeTo( shape );
	}

	// ==================== Saving / loading ====================

	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		parameters = xMLObject.manageListField( "parameters", parameters );
		x = xMLObject.manageDoubleAttribute( "x", x );
		y = xMLObject.manageDoubleAttribute( "y", y );
		width = xMLObject.manageDoubleAttribute( "width", width, 1.0 );
		height = xMLObject.manageDoubleAttribute( "height", height, 1.0 );
		visible = xMLObject.manageBooleanAttribute( "visible", visible, true );
		active = xMLObject.manageBooleanAttribute( "active", active, true );
		visualizer = xMLObject.manageObjectField( "visualizer", visualizer );
	}
}