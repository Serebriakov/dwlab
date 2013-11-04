/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types;

import dwlab.base.XMLObject;
import dwlab.shapes.Shape;
import dwlab.shapes.Shape.Relativity;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import java.util.LinkedList;

public class SpriteTemplate extends ShapeType {
	public static Sprite serviceRectangle = new Sprite( ShapeType.rectangle );
	public static Sprite serviceSprite = new Sprite();

	public String name;
	public LinkedList<Sprite> sprites = new LinkedList<Sprite>();


	@Override
	public int getNum() {
		return 9;
	}


	@Override
	public String getName() {
		return name;
	}


	@Override
	public boolean singleton() {
		return false;
	}



	public void setShape( Sprite sprite, Sprite templateSprite, Sprite spriteShape ) {
		double angle = sprite.displayingAngle;
		if( templateSprite.visualizer.rotating ) angle += sprite.angle;

		if( angle == 0 ) {
			spriteShape.setCoords( templateSprite.getX() * sprite.getWidth() + sprite.getX(), templateSprite.getY() * sprite.getHeight() + sprite.getY() );
		} else {
			double relativeX = templateSprite.getX() * sprite.getWidth();
			double relativeY = templateSprite.getY() * sprite.getHeight();
			spriteShape.setCoords( relativeX * Math.cos( angle ) - relativeY * Math.sin( angle ) + sprite.getX(), 
					relativeX * Math.sin( angle ) + relativeY * Math.cos( angle ) + sprite.getY() );
		}
		spriteShape.setSize( sprite.getWidth() * templateSprite.getWidth(), sprite.getHeight() * templateSprite.getHeight() );
		spriteShape.angle = sprite.angle + templateSprite.angle;
		spriteShape.displayingAngle = sprite.displayingAngle + templateSprite.displayingAngle;
	}



	@Override
	public Sprite getTileSprite( Sprite sprite, double dX, double dY, double xScale, double yScale ) {
		serviceRectangle.setCoords( sprite.getX() * xScale + dX, sprite.getY() * yScale + dY );
		serviceRectangle.setSize( sprite.getWidth() * xScale, sprite.getHeight() * yScale );
		serviceRectangle.shapeType = sprite.shapeType;
		return serviceRectangle;
	}



	public static SpriteTemplate fromSprites( LinkedList<Sprite> sprites, Layer layer, Shape pivotShape, Relativity relativity ) {
		SpriteTemplate template = null;
		double leftX = 0d, topY = 0d, rightX = 0d, bottomY = 0d;
		Sprite newSprite = new Sprite();

		for( Sprite sprite : sprites ) {
			if( sprite.shapeType.getNum() == ShapeType.spriteTemplate.getNum() ) continue;
			if( template != null ) {
				leftX = Math.min( leftX, sprite.leftX() );
				topY = Math.min( topY, sprite.topY() );
				rightX = Math.max( rightX, sprite.rightX() );
				bottomY = Math.max( bottomY, sprite.bottomY() );
			} else {
				template = new SpriteTemplate();
				leftX = sprite.leftX();
				topY = sprite.topY();
				rightX = sprite.rightX();
				bottomY = sprite.bottomY();
				if( pivotShape == null ) pivotShape = sprite;
			}
		}

		if( template != null ) {
			newSprite.setCoords( 0.5d * ( leftX + rightX ), 0.5d * ( topY + bottomY ) );
			newSprite.setHeight( bottomY - topY );
			newSprite.shapeType = template;
			if( layer != null ) layer.insert( newSprite, pivotShape, relativity );

			for( Sprite sprite : sprites ) {
				if( sprite.shapeType.getNum() == ShapeType.spriteTemplate.getNum() ) continue;
				layer.remove( sprite );
				sprite.setCoords( ( newSprite.getX() - sprite.getX() ) / newSprite.getWidth(), ( newSprite.getY() - sprite.getY() ) / newSprite.getHeight() );
				sprite.setSize( sprite.getWidth() / newSprite.getWidth(), sprite.getHeight() / newSprite.getHeight() );
				template.sprites.addLast( sprite );
			}
		}

		return template;
	}
	
	public static SpriteTemplate fromSprites( LinkedList<Sprite> sprites ){
		return fromSprites( sprites, null, null, Relativity.INSTEAD_OF );
	}


	public void toSprites( Sprite sprite, Layer layer, Shape pivotShape, Relativity relativity ) {
		LinkedList newSprites = new LinkedList();
		if( pivotShape == null ) pivotShape = sprite;
		for( Sprite templateSprite : sprites ) {
			Sprite newSprite = new Sprite();
			templateSprite.copySpriteTo( newSprite );
			setShape( sprite, templateSprite, newSprite );
			newSprites.addLast( newSprite );
		}
		layer.insert( newSprites, pivotShape, relativity );
	}
	
	public void toSprites( Sprite sprite, Layer layer ) {
		toSprites( sprite, layer, null, Relativity.INSTEAD_OF );
	}

	// ==================== Saving / loading ====================

	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );
		xMLObject.manageStringAttribute( "name", name );
		xMLObject.manageChildList( sprites );
	}
}