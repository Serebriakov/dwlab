/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.drawing_sprites;

import dwlab.base.images.Image;
import dwlab.platform.Platform;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;
import java.util.HashMap;
import java.util.Map;

public class DrawingSprite extends ServiceObjects {
	public static DrawingSprite defaultHandler = new DrawingSprite();
	
	private static final HashMap<ShapeType, DrawingSprite> handlersMap = new HashMap<ShapeType, DrawingSprite>();

	
	static {
		register( ShapeType.spriteTemplate, DrawingSpriteTemplate.instance );
		initSystem();
	}


	public static void register( ShapeType shapeType, DrawingSprite handler ) {
		handlersMap.put( shapeType, handler );
	}



	public void perform( Visualizer visualizer, Sprite sprite, Sprite spriteShape, Color drawingColor ) {
		Image image = visualizer.image;
		if( image != null ) {
			Camera.current.fieldToScreen( spriteShape, vector1 );

			double angle;
			if( visualizer.rotating ) {
				angle = spriteShape.displayingAngle + spriteShape.angle;
			} else {
				angle = spriteShape.displayingAngle;
			}

			if( Platform.debug ) if( sprite.frame < 0 || sprite.frame >= visualizer.image.framesQuantity() ) {
				error( "Incorrect frame number ( " + sprite.frame + " ) for sprite \"" + sprite.getTitle() + "\", should be less than " + visualizer.image.framesQuantity() );
			}

			if( visualizer.scaling ) {
				Camera.current.sizeFieldToScreen( spriteShape, vector2 );
				double scaledWidth = vector2.x * visualizer.xScale;
				double scaledHeight = vector2.y * visualizer.yScale;
				image.draw( sprite.frame, vector1.x + visualizer.dX * scaledWidth, vector1.y + visualizer.dY * scaledHeight, scaledWidth, scaledHeight, angle, visualizer.multiplyBy( drawingColor ) );
			} else {
				double scaledWidth = image.getWidth() * visualizer.xScale;
				double scaledHeight = image.getHeight() * visualizer.yScale;
				image.draw( sprite.frame, vector1.x + visualizer.dX * scaledWidth, vector1.y + visualizer.dY * scaledHeight, scaledWidth, scaledHeight, angle, visualizer.multiplyBy( drawingColor ) );
			}
		} else {
			sprite.drawShape( visualizer.multiplyBy( drawingColor ), false );
		}
	}


	public static DrawingSprite handlers[];

	public static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		handlers = new DrawingSprite[ quantity ];
		for( int n = 0; n < quantity; n++ ) handlers[ n ] = defaultHandler;
		for( Map.Entry<ShapeType, DrawingSprite> entry : handlersMap.entrySet() ) {
			handlers[ entry.getKey().getNum() ] = entry.getValue();
		}
	}
}