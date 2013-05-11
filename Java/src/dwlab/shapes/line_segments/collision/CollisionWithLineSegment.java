package dwlab.shapes.line_segments.collision;

import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class CollisionWithLineSegment extends ServiceObjects {
	public static HashMap<ShapeType, CollisionWithLineSegment> handlersMap = new HashMap<ShapeType, CollisionWithLineSegment>();



	public static void register( ShapeType shapeType, CollisionWithLineSegment handler ) {
		handlersMap.put( shapeType, handler );
	}



	public boolean collision( Sprite sprite, LineSegment lineSegment ) {
		return check( sprite, lineSegment.pivot[ 0 ], lineSegment.pivot[ 1 ] );
	}
	
	
	public abstract boolean check( Sprite sprite, Sprite lSPivot1, Sprite lSPivot2 );


	public static CollisionWithLineSegment handlersArray[];

	public static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		handlersArray = new CollisionWithLineSegment[ quantity ];
		for( Entry<ShapeType, CollisionWithLineSegment> entry : handlersMap.entrySet() ) {
			handlersArray[ entry.getKey().getNum() ] = entry.getValue();
		}
	}


	public static boolean lineSegment( Sprite lS1pivot1, Sprite lS1pivot2, Sprite lS2pivot1, Sprite lS2pivot2 ) {
		serviceLine1.usePivots( lS1pivot1, lS1pivot2 );
		if( serviceLine1.pivotOrientation( lS2pivot1 ) == serviceLine1.pivotOrientation( lS2pivot2 ) ) return false;
		serviceLine1.usePivots( lS2pivot1, lS2pivot2 );
		if( serviceLine1.pivotOrientation( lS1pivot1 ) != serviceLine1.pivotOrientation( lS1pivot2 ) ) return true;
		return false;
	}
}



public class PivotHandler extends SpriteHandler {
	public static PivotHandler instance = new PivotHandler();
}

SpriteHandler.register( Sprite.pivot, PivotHandler.instance );





public class OvalHandler extends SpriteHandler {
	public static OvalHandler instance = new OvalHandler();



	public void drawShape( Sprite sprite, Sprite spriteShape, double sX, double sY, double sWidth, double sHeight ) {
		if( sprite.physics() ) setRotation( spriteShape.angle );
		if( sWidth == sHeight ) {
			setHandle( 0.5 * sWidth, 0.5 * sHeight );
			drawOval( sX, sY, sWidth, sHeight );
			setHandle( 0.0, 0.0 );
		} else if( sWidth > sHeight ) {
			double dWidth = sWidth - sHeight;
			setHandle( 0.5 * sWidth, 0.5 * sHeight );
			drawOval( sX, sY, sHeight, sHeight );
			setHandle( sHeight - 0.5 * sWidth, 0.5 * sHeight );
			drawOval( sX, sY, sHeight, sHeight );
			setHandle( 0.5 * dWidth, 0.5 * sHeight );
			drawRect( sX, sY, dWidth, sHeight );
			setHandle( 0.0, 0.0 );
		} else {
			double dHeight = sHeight - sWidth;
			setHandle( 0.5 * sWidth, 0.5 * sHeight );
			drawOval( sX, sY, sWidth, sWidth );
			setHandle( 0.5 * sWidth, sWidth - 0.5 * sHeight );
			drawOval( sX, sY, sWidth, sWidth );
			setHandle( 0.5 * sWidth, 0.5 * dHeight );
			drawRect( sX, sY, sWidth, dHeight );
			setHandle( 0.0, 0.0 );
		}
		setOrigin( 0.0, 0.0 );
		setRotation( 0.0 );
	}




}

SpriteHandler.register( Sprite.oval, OvalHandler.instance );





public class RectangleHandler extends SpriteHandler {
	public static RectangleHandler instance = new RectangleHandler();



	public void drawShape( Sprite sprite, Sprite spriteShape, double sX, double sY, double sWidth, double sHeight ) {
		if( sprite.physics() ) setRotation( spriteShape.angle );
		setHandle( 0.5 * sWidth, 0.5 * sHeight );
		drawRect( sX, sY, sWidth, sHeight );
		setHandle( 0.0, 0.0 );
		setRotation( 0.0 );
	}




}

SpriteHandler.register( Sprite.rectangle, RectangleHandler.instance );




public class TriangleHandler extends SpriteHandler {
	public static TriangleHandler instance = new TriangleHandler();



	public void drawShape( Sprite sprite, Sprite spriteShape, double sX, double sY, double sWidth, double sHeight ) {
		if( sprite.physics() ) setRotation( spriteShape.angle );
		setOrigin( sX, sY );
		switch( sprite.shapeType.getNum() ) {
			case Sprite.topLeftTriangle.getNum():
				drawPoly( [ float( -0.5 * sWidth ), float( -0.5 * sHeight ), float( 0.5 * sWidth ), float( -0.5 * sHeight ), 
						float( -0.5 * sWidth ), float( 0.5 * sHeight ) ] );
			case Sprite.topRightTriangle.getNum():
				drawPoly( [ float( -0.5 * sWidth ), float( -0.5 * sHeight ), float( 0.5 * sWidth ), float( -0.5 * sHeight ), 
						float( 0.5 * sWidth ), float( 0.5 * sHeight ) ] );
			case Sprite.bottomLeftTriangle.getNum():
				drawPoly( [ float( -0.5 * sWidth ), float( 0.5 * sHeight ), float( 0.5 * sWidth ), float( 0.5 * sHeight ), 
						float( -0.5 * sWidth ), float( -0.5 * sHeight ) ] );
			case Sprite.bottomRightTriangle.getNum():
				drawPoly( [ float( -0.5 * sWidth ), float( 0.5 * sHeight ), float( 0.5 * sWidth ), float( 0.5 * sHeight ), 
						float( 0.5 * sWidth ), float( -0.5 * sHeight ) ] );
		}
		setOrigin( 0.0, 0.0 );
		setRotation( 0.0 );
	}



	public int check( Sprite triangle, Sprite lSPivot1, Sprite lSPivot2 ) {
		if( PivotWithTriangle.instance.spritesCollide( lSPivot1, triangle ) ) return true;
		triangle.getOtherVertices( servicePivots[ 0 ], servicePivots[ 1 ] );
		triangle.getRightAngleVertex( servicePivots[ 2 ] );
		for( int n = 0; n <= 2; n++ ) {
			if( lineSegmentCollidesWithLineSegment( servicePivots[ n ], servicePivots[ ( n + 1 ) % 3 ], lSPivot1, lSPivot2 ) ) return true;
		}
	}
}





public class RayHandler extends SpriteHandler {
	public static RayHandler instance = new RayHandler();



	public void drawShape( Sprite sprite, Sprite spriteShape, double sX, double sY, double sWidth, double sHeight ) {
		drawOval( sX - 2, sY - 2, 5, 5 );
		double ang = wrapDouble( spriteShape.angle, 360.0 );
		if( ang < 45.0 || ang >= 315.0 ) {
			double width = currentCamera.viewport.rightX() - sX;
			if( width > 0 ) drawLine( sX, sY, sX + width, sY + width * Math.tan( ang ) );
		} else if( ang < 135.0 ) {
			double height = currentCamera.viewport.bottomY() - sY;
			if( height > 0 ) drawLine( sX, sY, sX + height / Math.tan( ang ), sY + height );
		} else if( ang < 225.0 ) {
			double width = currentCamera.viewport.leftX() - sX;
			if( width < 0 ) drawLine( sX, sY, sX + width, sY + width * Math.tan( ang ) );
		} else {
			double height = currentCamera.viewport.topY() - sY;
			if( height < 0 ) drawLine( sX, sY, sX + height / Math.tan( ang ), sY + height );
		}
	}



	public int check( Sprite ray, Sprite lSPivot1, Sprite lSPivot2 ) {
		ray.toLine( serviceLine1 );
		if( serviceLine1.intersectionWithLineSegment( lSPivot1, lSPivot2, servicePivot1 ) ) {
			if( ray.hasPivot( servicePivot1 ) ) return true;
		}
	}
}

SpriteHandler.register( Sprite.ray, RayHandler.instance );





public class SpriteTemplateHandler extends SpriteHandler {
	public static SpriteTemplateHandler instance = new SpriteTemplateHandler();

	public Sprite serviceSprite = new Sprite();



	public void drawSprite( Visualizer visualizer, Sprite sprite, double drawingAlpha ) {
		SpriteTemplate spriteTemplate = SpriteTemplate( sprite.shapeType );
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			templateSprite.visualizer.drawUsingSprite( templateSprite, serviceSprite, drawingAlpha );
		}
	}



	public void drawShape( Sprite sprite, Sprite spriteShape, double sX, double sY, double sWidth, double sHeight ) {
		SpriteTemplate spriteTemplate = SpriteTemplate( sprite.shapeType );
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			templateSprite.visualizer.drawSpriteShape( templateSprite, serviceSprite );
		}
	}



	public int check( Sprite sprite, Sprite lSPivot1, Sprite lSPivot2 ) {
		SpriteTemplate spriteTemplate = SpriteTemplate( sprite.shapeType );
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			if( SpriteHandler.handlersArray[ sprite.shapeType.getNum() ].spriteCollidesWithLineSegment( sprite, lSPivot1, lSPivot2 ) ) return true;
		}
	}
}

SpriteHandler.register( Sprite.spriteTemplate, SpriteTemplateHandler.instance );
