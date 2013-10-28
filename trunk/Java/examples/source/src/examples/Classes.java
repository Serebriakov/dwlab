package examples;

import dwlab.base.Obj;

public class Classes {
	public static void register() {
		Obj.classes.put( "BinaryFile", dwlab.base.files.BinaryFile.class );
		Obj.classes.put( "TextFile", dwlab.base.files.TextFile.class );
		Obj.classes.put( "BitmapFont", dwlab.base.images.BitmapFont.class );
		Obj.classes.put( "Image", dwlab.base.images.Image.class );
		Obj.classes.put( "ImageBuffer", dwlab.base.images.ImageBuffer.class );
		Obj.classes.put( "RasterFrame", dwlab.base.images.RasterFrame.class );
		Obj.classes.put( "Obj", dwlab.base.Obj.class );
		Obj.classes.put( "Project", dwlab.base.Project.class );
		Obj.classes.put( "Action", dwlab.base.service.Action.class );
		Obj.classes.put( "Align", dwlab.base.service.Align.class );
		Obj.classes.put( "Drag", dwlab.base.service.Drag.class );
		Obj.classes.put( "IntVector", dwlab.base.service.IntVector.class );
		Obj.classes.put( "Service", dwlab.base.service.Service.class );
		Obj.classes.put( "Vector", dwlab.base.service.Vector.class );
		Obj.classes.put( "XMLObject", dwlab.base.XMLObject.class );
		Obj.classes.put( "AnimationModel", dwlab.behavior_models.AnimationModel.class );
		Obj.classes.put( "BehaviorModel", dwlab.behavior_models.BehaviorModel.class );
		Obj.classes.put( "ChainedModel", dwlab.behavior_models.ChainedModel.class );
		Obj.classes.put( "ColorChangingModel", dwlab.behavior_models.ColorChangingModel.class );
		Obj.classes.put( "ConditionalModel", dwlab.behavior_models.ConditionalModel.class );
		Obj.classes.put( "DistanceJoint", dwlab.behavior_models.DistanceJoint.class );
		Obj.classes.put( "FixedJoint", dwlab.behavior_models.FixedJoint.class );
		Obj.classes.put( "FixedWaitingModel", dwlab.behavior_models.FixedWaitingModel.class );
		Obj.classes.put( "FollowingModel", dwlab.behavior_models.FollowingModel.class );
		Obj.classes.put( "IsButtonActionDown", dwlab.behavior_models.IsButtonActionDown.class );
		Obj.classes.put( "IsModelActive", dwlab.behavior_models.IsModelActive.class );
		Obj.classes.put( "ModelActivator", dwlab.behavior_models.ModelActivator.class );
		Obj.classes.put( "ModelDeactivator", dwlab.behavior_models.ModelDeactivator.class );
		Obj.classes.put( "ModelStack", dwlab.behavior_models.ModelStack.class );
		Obj.classes.put( "MovingModel", dwlab.behavior_models.MovingModel.class );
		Obj.classes.put( "RandomWaitingModel", dwlab.behavior_models.RandomWaitingModel.class );
		Obj.classes.put( "ResizingModel", dwlab.behavior_models.ResizingModel.class );
		Obj.classes.put( "RevoluteJoint", dwlab.behavior_models.RevoluteJoint.class );
		Obj.classes.put( "TemporaryModel", dwlab.behavior_models.TemporaryModel.class );
		Obj.classes.put( "TimedMovementModel", dwlab.behavior_models.TimedMovementModel.class );
		Obj.classes.put( "ValueChangingModel", dwlab.behavior_models.ValueChangingModel.class );
		Obj.classes.put( "VectorSpriteCollisionsModel", dwlab.behavior_models.VectorSpriteCollisionsModel.class );
		Obj.classes.put( "ButtonAction", dwlab.controllers.ButtonAction.class );
		Obj.classes.put( "Key", dwlab.controllers.Key.class );
		Obj.classes.put( "KeyboardKey", dwlab.controllers.KeyboardKey.class );
		Obj.classes.put( "MouseButton", dwlab.controllers.MouseButton.class );
		Obj.classes.put( "MouseWheel", dwlab.controllers.MouseWheel.class );
		Obj.classes.put( "Pushable", dwlab.controllers.Pushable.class );
		Obj.classes.put( "AddLineToGraph", dwlab.shapes.graphs.AddLineToGraph.class );
		Obj.classes.put( "AddPivotToGraph", dwlab.shapes.graphs.AddPivotToGraph.class );
		Obj.classes.put( "Graph", dwlab.shapes.graphs.Graph.class );
		Obj.classes.put( "RemoveLineFromGraph", dwlab.shapes.graphs.RemoveLineFromGraph.class );
		Obj.classes.put( "RemovePivotFromGraph", dwlab.shapes.graphs.RemovePivotFromGraph.class );
		Obj.classes.put( "EditorData", dwlab.shapes.layers.EditorData.class );
		Obj.classes.put( "Layer", dwlab.shapes.layers.Layer.class );
		Obj.classes.put( "World", dwlab.shapes.layers.World.class );
		Obj.classes.put( "Line", dwlab.shapes.Line.class );
		Obj.classes.put( "LineSegment", dwlab.shapes.line_segments.LineSegment.class );
		Obj.classes.put( "DoubleMap", dwlab.shapes.maps.DoubleMap.class );
		Obj.classes.put( "IntMap", dwlab.shapes.maps.IntMap.class );
		Obj.classes.put( "Map", dwlab.shapes.maps.Map.class );
		Obj.classes.put( "SpriteMap", dwlab.shapes.maps.SpriteMap.class );
		Obj.classes.put( "TileCategory", dwlab.shapes.maps.tilemaps.TileCategory.class );
		Obj.classes.put( "TileMap", dwlab.shapes.maps.tilemaps.TileMap.class );
		Obj.classes.put( "TileMapCollisionModel", dwlab.shapes.maps.tilemaps.TileMapCollisionModel.class );
		Obj.classes.put( "TileMapLoadingErrorHandler", dwlab.shapes.maps.tilemaps.TileMapLoadingErrorHandler.class );
		Obj.classes.put( "TileMapPathFinder", dwlab.shapes.maps.tilemaps.TileMapPathFinder.class );
		Obj.classes.put( "TilePos", dwlab.shapes.maps.tilemaps.TilePos.class );
		Obj.classes.put( "TileRule", dwlab.shapes.maps.tilemaps.TileRule.class );
		Obj.classes.put( "TileSet", dwlab.shapes.maps.tilemaps.TileSet.class );
		Obj.classes.put( "Parameter", dwlab.shapes.Parameter.class );
		Obj.classes.put( "Shape", dwlab.shapes.Shape.class );
		Obj.classes.put( "Camera", dwlab.shapes.sprites.Camera.class );
		Obj.classes.put( "Sprite", dwlab.shapes.sprites.Sprite.class );
		Obj.classes.put( "SpriteAndLineSegmentCollisionHandler", dwlab.shapes.sprites.SpriteAndLineSegmentCollisionHandler.class );
		Obj.classes.put( "SpriteAndTileCollisionHandler", dwlab.shapes.sprites.SpriteAndTileCollisionHandler.class );
		Obj.classes.put( "SpriteCollisionHandler", dwlab.shapes.sprites.SpriteCollisionHandler.class );
		Obj.classes.put( "VectorSprite", dwlab.shapes.sprites.VectorSprite.class );
		Obj.classes.put( "TitleGenerator", dwlab.shapes.TitleGenerator.class );
		Obj.classes.put( "AnimatedTileMapVisualizer", dwlab.visualizers.AnimatedTileMapVisualizer.class );
		Obj.classes.put( "Color", dwlab.visualizers.Color.class );
		Obj.classes.put( "ContourVisualizer", dwlab.visualizers.ContourVisualizer.class );
		Obj.classes.put( "DebugVisualizer", dwlab.visualizers.DebugVisualizer.class );
		Obj.classes.put( "MarchingAnts", dwlab.visualizers.MarchingAnts.class );
		Obj.classes.put( "Visualizer", dwlab.visualizers.Visualizer.class );
		Obj.classes.put( "WindowedVisualizer", dwlab.visualizers.WindowedVisualizer.class );
		Obj.classes.put( "ActionExample", examples.ActionExample.class );
		Obj.classes.put( "ActiveExample", examples.ActiveExample.class );
		Obj.classes.put( "All", examples.All.class );
		Obj.classes.put( "AwPossum", examples.AwPossum.class );
		Obj.classes.put( "BehaviorModelExample", examples.BehaviorModelExample.class );
		Obj.classes.put( "BitmapFontExample", examples.BitmapFontExample.class );
		Obj.classes.put( "ButtonActionExample", examples.ButtonActionExample.class );
		Obj.classes.put( "CameraExample", examples.CameraExample.class );
		Obj.classes.put( "Classes", examples.Classes.class );
		Obj.classes.put( "CloneExample", examples.CloneExample.class );
		Obj.classes.put( "CollidesWithSpriteExample", examples.CollidesWithSpriteExample.class );
		Obj.classes.put( "CorrectHeightExample", examples.CorrectHeightExample.class );
		Obj.classes.put( "DirectAsExample", examples.DirectAsExample.class );
		Obj.classes.put( "DirectToExample", examples.DirectToExample.class );
		Obj.classes.put( "DistanceExample", examples.DistanceExample.class );
		Obj.classes.put( "DistanceJointExample", examples.DistanceJointExample.class );
		Obj.classes.put( "DistanceToExample", examples.DistanceToExample.class );
		Obj.classes.put( "DrawCircleExample", examples.DrawCircleExample.class );
		Obj.classes.put( "DrawEmptyRectangleExample", examples.DrawEmptyRectangleExample.class );
		Obj.classes.put( "DrawTileExample", examples.DrawTileExample.class );
		Obj.classes.put( "DrawUsingLineExample", examples.DrawUsingLineExample.class );
		Obj.classes.put( "DrawUsingSpriteExample", examples.DrawUsingSpriteExample.class );
		Obj.classes.put( "EnframeExample", examples.EnframeExample.class );
		Obj.classes.put( "GameObject", examples.GameObject.class );
		Obj.classes.put( "GetTileForPointExample", examples.GetTileForPointExample.class );
		Obj.classes.put( "GetTileValueExample", examples.GetTileValueExample.class );
		Obj.classes.put( "GraphExample", examples.GraphExample.class );
		Obj.classes.put( "IntInLimitsExample", examples.IntInLimitsExample.class );
		Obj.classes.put( "Jelly", examples.Jelly.class );
		Obj.classes.put( "LeftXExample", examples.LeftXExample.class );
		Obj.classes.put( "LimitByWindowShapeExample", examples.LimitByWindowShapeExample.class );
		Obj.classes.put( "LimitIntExample", examples.LimitIntExample.class );
		Obj.classes.put( "LimitWithExample", examples.LimitWithExample.class );
		Obj.classes.put( "LogicFPSExample", examples.LogicFPSExample.class );
		Obj.classes.put( "MarchingAntsExample", examples.MarchingAntsExample.class );
		Obj.classes.put( "MoveTowardsExample", examples.MoveTowardsExample.class );
		Obj.classes.put( "MoveUsingKeysExample", examples.MoveUsingKeysExample.class );
		Obj.classes.put( "OverlapsExample", examples.OverlapsExample.class );
		Obj.classes.put( "ParallaxExample", examples.ParallaxExample.class );
		Obj.classes.put( "PasteExample", examples.PasteExample.class );
		Obj.classes.put( "PerlinNoiseExample", examples.PerlinNoiseExample.class );
		Obj.classes.put( "PlaceBetweenExample", examples.PlaceBetweenExample.class );
		Obj.classes.put( "PrintTextExample", examples.PrintTextExample.class );
		Obj.classes.put( "Profession", examples.Profession.class );
		Obj.classes.put( "RasterFrameExample", examples.RasterFrameExample.class );
		Obj.classes.put( "RevoluteJointExample", examples.RevoluteJointExample.class );
		Obj.classes.put( "SaveToFileExample", examples.SaveToFileExample.class );
		Obj.classes.put( "SetAsTileExample", examples.SetAsTileExample.class );
		Obj.classes.put( "SetAsViewportExample", examples.SetAsViewportExample.class );
		Obj.classes.put( "SetCornerCoordsExample", examples.SetCornerCoordsExample.class );
		Obj.classes.put( "SetFacingExample", examples.SetFacingExample.class );
		Obj.classes.put( "SpriteMapExample", examples.SpriteMapExample.class );
		Obj.classes.put( "StretchExample", examples.StretchExample.class );
		Obj.classes.put( "TurnExample", examples.TurnExample.class );
		Obj.classes.put( "VectorSpriteExample", examples.VectorSpriteExample.class );
		Obj.classes.put( "WedgeOffWithSpriteExample", examples.WedgeOffWithSpriteExample.class );
		Obj.classes.put( "Worker", examples.Worker.class );
		Obj.classes.put( "WrapExample", examples.WrapExample.class );
		Obj.classes.put( "XMLIOExample", examples.XMLIOExample.class );
	}
}