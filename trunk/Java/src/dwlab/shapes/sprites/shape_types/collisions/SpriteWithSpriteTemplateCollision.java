package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;

public class SpriteWithSpriteTemplateCollision extends SpritesCollision {
	public static SpriteWithSpriteTemplateCollision instance = new SpriteWithSpriteTemplateCollision();

	public Sprite serviceSprite = new Sprite();

	@Override
	public boolean check( Sprite sprite, Sprite spriteTemplateSprite ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) spriteTemplateSprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( spriteTemplateSprite, templateSprite, serviceSprite );
			serviceSprite.shapeType = templateSprite.shapeType;
			if( sprite.collidesWithSprite( serviceSprite ) ) return true;
		}
		return false;
	}
}
