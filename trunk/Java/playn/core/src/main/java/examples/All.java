package examples;

import dwlab.base.service.Service;
import dwlab.platform.LWJGL;

public class All {
	public static void main(String[] argv) {
		LWJGL.init();
		//Service.generateClassesList( "examples", "src/examples/Classes.java" ); System.exit( 0 );
		Classes.register();
		ActionExample.main();
		ActiveExample.main();
		BehaviorModelExample.main();
		//BitmapFontExample.main();
		ButtonActionExample.main();
		CameraExample.main();
		CloneExample.main();
		CollidesWithSpriteExample.main();
		CorrectHeightExample.main();
		DirectAsExample.main();
		DirectToExample.main();
		DistanceExample.main();
		DistanceJointExample.main();
		DistanceToExample.main();
		DrawCircleExample.main();
		DrawEmptyRectangleExample.main();
		DrawTileExample.main();
		DrawUsingLineExample.main();
		DrawUsingSpriteExample.main();
		EnframeExample.main();
		GetTileForPointExample.main();
		GetTileValueExample.main();
		GraphExample.main();
		IntInLimitsExample.main();
		LeftXExample.main();
		LimitByWindowShapeExample.main();
		LimitIntExample.main();
		LimitWithExample.main();
		LogicFPSExample.main();
		MarchingAntsExample.main();
		MoveTowardsExample.main();
		MoveUsingKeysExample.main();
		OverlapsExample.main();
		ParallaxExample.main();
		PasteExample.main();
		PerlinNoiseExample.main();
		PlaceBetweenExample.main();
		PrintTextExample.main();
		RasterFrameExample.main();
		RevoluteJointExample.main();
		SaveToFileExample.main();
		SetAsTileExample.main();
		SetAsViewportExample.main();
		SetCornerCoordsExample.main();
		SetFacingExample.main();
		SpriteMapExample.main();
		StretchExample.main();
		TurnExample.main();
		VectorSpriteExample.main();
		WedgeOffWithSpriteExample.main();
		WrapExample.main();
		XMLIOExample.main();
	}
}
