package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.base.Service;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;

public class DrawingRayShape extends DrawingShape {
	@Override
	public void perform( Sprite sprite, Sprite spriteShape, Vector coords, Vector sizes ) {
		double ang = Service.wrap( sprite.angle, 360.0 );
		if( ang < 45.0 || ang >= 315.0 ) {
			double width = Camera.current.viewport.rightX() - coords.x;
			if( width > 0 ) Graphics.drawLine( coords.x, coords.y, coords.x + width, coords.y + width * Math.tan( ang ) );
		} else if( ang < 135.0 ) {
			double height = Camera.current.viewport.bottomY() - coords.y;
			if( height > 0 ) Graphics.drawLine( coords.x, coords.y, coords.x + height / Math.tan( ang ), coords.y + height );
		} else if( ang < 225.0 ) {
			double width = Camera.current.viewport.leftX() - coords.x;
			if( width < 0 ) Graphics.drawLine( coords.x, coords.y, coords.x + width, coords.y + width * Math.tan( ang ) );
		} else {
			double height = Camera.current.viewport.topY() - coords.y;
			if( height < 0 ) Graphics.drawLine( coords.x, coords.y, coords.x + height / Math.tan( ang ), coords.y + height );
		}
	}
}
