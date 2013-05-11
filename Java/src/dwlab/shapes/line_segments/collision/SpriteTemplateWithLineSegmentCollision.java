package dwlab.shapes.line_segments.collision;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;

public class SpriteTemplateWithLineSegmentCollision extends CollisionWithLineSegment {
	public static SpriteTemplateWithLineSegmentCollision instance = new SpriteTemplateWithLineSegmentCollision();

	
	@Override
	public boolean check( Sprite sprite, Sprite lSPivot1, Sprite lSPivot2 ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) sprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			if( CollisionWithLineSegment.handlersArray[ sprite.shapeType.getNum() ].check( sprite, lSPivot1, lSPivot2 ) ) return true;
		}
		return false;
	}
}	