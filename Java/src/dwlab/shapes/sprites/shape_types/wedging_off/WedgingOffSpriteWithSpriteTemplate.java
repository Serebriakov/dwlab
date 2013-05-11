package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;

public class WedgingOffSpriteWithSpriteTemplate extends WedgingOffSprites {
	public static WedgingOffSpriteWithSpriteTemplate instance = new WedgingOffSpriteWithSpriteTemplate();

	public Sprite serviceSprite = new Sprite();

	@Override
	public boolean spritesCollide( Sprite sprite, Sprite spriteTemplateSprite ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) spriteTemplateSprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( spriteTemplateSprite, templateSprite, serviceSprite );
			serviceSprite.shapeType = templateSprite.shapeType;
			if( sprite.collidesWithSprite( serviceSprite ) ) return true;
		}
		return false;
	}
}
