/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.layers;

import dwlab.base.Project;
import dwlab.base.XMLObject;
import dwlab.platform.Platform;
import dwlab.shapes.Shape;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Layer is the group of sprites which have bounds.
 * See also #directTo example.
 */
public class Layer extends Shape {
	/**
	 * List of shapes.
	 */
	public LinkedList<Shape> children = new LinkedList<Shape>();

	/**
	 * Rectangular shape of layer bounds.
	 */
	public Shape bounds;

	/**
	 * Flag which defines if layer content should be mixed while displaying.
	 * Some conditions should be met to display mixed content correctly:
	 * <ul><li>Mixed content layer should contain at least one tile map.
	 * <li>All maps in layer should have equal tile/cell size.
	 * <li>All tile maps in layer should have equal corner coordinates like ( N * CellWidth, M * CellHeight ) where N and M is integer.
	 * <li>All tile maps in layer should have equal horizontal and vertical size in tiles.</ul>
	 * If this layer contains sprites or other layers they will not be drawn.
	 */
	public boolean mixContent;


	@Override
	public String getClassTitle() {
		return "Layer";
	}
	

	@Override
	public Layer toLayer() {
		return this;
	}

	// ==================== Drawing ===================	

	@Override
	public void draw( Color drawingColor ) {
		drawUsingVisualizer( visualizer, drawingColor.multiplyBy( visualizer ) );
	}


	@Override
	public void drawUsingVisualizer( Visualizer vis, Color drawingColor ) {
		if( ! visible ) return;

		if( mixContent ) {
			LinkedList shapes = new LinkedList();
			TileMap mainTileMap = null;
			for( Shape shape : children ) {
				TileMap tileMap = shape.toTileMap();
				if( tileMap != null ) {
					if( tileMap.tileSet.image != null ) {
						mainTileMap = tileMap;
						shapes.addLast( shape );
					}
				} else if( shape.toSpriteMap() != null ) {
					shapes.addLast( shape );
				}
			}
			if( mainTileMap != null ) {
				if( shapes.size() == 1 ) shapes = null;
				vis.drawUsingTileMap( mainTileMap, shapes, drawingColor );
				return;
			}
		}

		if( vis == visualizer ) {
			for( Shape shape : children ) {
				shape.draw( drawingColor );
			}
		} else {
			for( Shape shape : children ) {
				shape.drawUsingVisualizer( vis, drawingColor );
			}
		}
	}

	// ==================== Managing ===================

	/**
	 * Initialization method.
	 * Every child shape will be initialized by default.
	 */
	@Override
	public void init() {
		for( Shape obj: children ) {
			obj.init();
		}
	}


	/**
	 * Acting method.
	 * Every child shape will be acted.
	 */
	@Override
	public void act() {
		if( active ) {
			super.act();
			for ( Iterator<Shape> it = children.iterator(); it.hasNext(); ) {
				Shape obj = it.next();
				if( obj.active ) {
					if( Platform.current.debug ) {
						Project.spriteActed = false;
						obj.act();
						if( obj.toSprite() != null && ! Project.spriteActed ) Project.spritesActed += 1;
					} else {
						obj.act();
					}
				}
			}
		}
	}


	@Override
	public void update() {
		for( Shape obj: children ) {
			obj.update();
		}
	}

	// ==================== Collisions ===================

	@Override
	public Sprite layerLastSpriteCollision( Sprite sprite ) {
		return sprite.lastCollidedSpriteOf( this );
	}


	@Override
	public void spriteLayerCollisions( Sprite sprite, SpriteCollisionHandler handler ) {
		sprite.collisionsWith( this, handler );
	}

	// ==================== Other ===================	

	@Override
	public Shape setCoords( double newX, double newY ) {
		for( Shape shape: children ) shape.alterCoords( newX - x, newY - y );
		setCoords( newX, newY );
		update();
		return this;
	}


	/**
	 * Sets the bounds of layer to given shape.
	 */
	public void setBounds( Shape shape ) {
		if( bounds == null ) {
			bounds = new Shape();
			bounds.visualizer = null;
		}
		bounds.jumpTo( shape );
		bounds.setSizeAs( shape );
	}


	/**
	 * Counts quantity of sprites inside the layer.
	 * @return Quantity of sprites inside layer and included layers.
	 * 
	 */
	@Override
	public int countSprites() {
		int count = 0;
		for( Shape shape: children ) {
			count += shape.countSprites();
		}
		return count;
	}


	/**
	 * Shows all behavior models attached to shape with their status.
	 */
	@Override
	public int showModels( int y, String shift ) {
		if( behaviorModels.isEmpty() ) {
			if( children.isEmpty() ) return y;
			Platform.current.drawText( shift + getTitle() + " ", 0, y );
			y += 16;
		} else {
			y = super.showModels( y, shift );
		}
		for( Shape shape: children ) {
			y = shape.showModels( y, shift + " " );
		}
		return y;
	}

	// ==================== List wrapping methods ====================

	public void addFirst( Shape shape ) {
		children.addFirst( shape );
	}




	public void addLast( Shape shape ) {
		children.addLast( shape );
	}



	public void clear() {
		children.clear();
	}



	public Shape get( int index ) {
		return children.get( index );
	}

	// ==================== Shape management ====================

	@Override
	public Shape load() {
		Layer newLayer = loadShape().toLayer();
		for( Shape shape: children ) {
			newLayer.addLast( shape.load() );
		}
		return newLayer;
	}


	@Override
	public Shape findShape( String parameterName, String parameterValue ) {
		if( getParameter( parameterName ).equals( parameterValue ) || parameterName.isEmpty() ) return this;
		for( Shape childShape: children ) {
			Shape shape = childShape.findShape( parameterName, parameterValue );
			if( shape != null ) return shape;
		}
		return null;
	}


	@Override
	public Shape findShape( Class shapeClass ) {
		if( getClass() == shapeClass ) return this;
		for( Shape childShape: children ) {
			Shape shape = childShape.findShape( shapeClass );
			if( shape != null ) return shape;
		}
		return null;
	}


	@Override
	public Shape findShape( String parameterName, String parameterValue, Class shapeClass ) {
		Shape shape = super.findShape( parameterName, parameterValue, shapeClass );
		if( shape != null ) return shape;
		for( Shape childShape: children ) {
			shape = childShape.findShape( parameterName, parameterValue, shapeClass );
			if( shape != null ) return shape;
		}
		return null;
	}
	
	
	@Override
	public boolean insert( Shape shape, Shape pivotShape, Relativity relativity ) {
		for ( ListIterator<Shape> it = children.listIterator(); it.hasNext(); ) {
			int index = it.nextIndex();
			Shape childShape = it.next();
			if( childShape == pivotShape ) {
				if( relativity == Relativity.AFTER ) index++;
				if( relativity == Relativity.INSTEAD_OF ) it.remove();
				children.set( index, shape );
				return true;
			} else {
				if( childShape.insert( shape, pivotShape, relativity ) ) return true;
			}
		}
		return false;
	}
	
	
	@Override
	public boolean insert( Collection<Shape> shapes, Shape pivotShape, Relativity relativity ) {
		for ( ListIterator<Shape> it = children.listIterator(); it.hasNext(); ) {
			int index = it.nextIndex();
			Shape childShape = it.next();
			if( childShape == pivotShape ) {
				if( relativity == Relativity.AFTER ) index++;
				if( relativity == Relativity.INSTEAD_OF ) it.remove();
				children.addAll( index, shapes );
				return true;
			} else {
				if( childShape.insert( shapes, pivotShape, relativity ) ) return true;
			}
		}
		return false;
	}
	

	@Override
	public Shape remove( Shape shape ) {
		for ( ListIterator<Shape> iterator = children.listIterator(); iterator.hasNext(); ) {
			Shape childShape = iterator.next();
			if( childShape == shape ) iterator.remove();
			childShape.remove( shape );
		}
		return this;
	}


	@Override
	public Shape remove( Class shapeClass ) {
		for ( ListIterator<Shape> iterator = children.listIterator(); iterator.hasNext(); ) {
			Shape childShape = iterator.next();
			if( childShape.getClass() == shapeClass ) iterator.remove();
			childShape.remove( shapeClass );
		}
		return this;
	}

	// ==================== Cloning ===================	

	@Override
	public Shape clone() {
		Layer newLayer = new Layer();
		copyLayerTo( newLayer );
		for( Shape shape: children ) {
			newLayer.children.addLast( shape.clone() );
		}
		return newLayer;
	}


	public void copyLayerTo( Layer layer ) {
		copyShapeTo( layer );
		if( bounds != null ) {
			layer.bounds = new Shape();
			bounds.copyTo( layer.bounds );
		}
		layer.mixContent = mixContent;
	}


	@Override
	public void copyTo( Shape shape ) {
		Layer layer = shape.toLayer();
		if( Platform.current.debug ) if( layer == null ) error( "Trying to copy layer \"" + shape.getName() + "\" data to non-layer" );
		copyLayerTo( layer );
	}

	// ==================== Saving / loading ===================

	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		children = xMLObject.manageChildList( children );
		bounds = xMLObject.manageObjectField( "bounds", bounds );
		mixContent = xMLObject.manageBooleanAttribute( "mix-content", mixContent );
	}
}
