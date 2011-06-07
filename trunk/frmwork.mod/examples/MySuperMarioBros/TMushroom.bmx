'
' Super Mario Bros - Digital Wizard's Lab example
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TMushroom Extends LTVectorSprite
	Const Speed:Float = 2.0
	
	

	Function FromTile( TileX:Int, TileY:Int )
		Local Mushroom:TMushroom = New TMushroom
		Mushroom.SetAsTile( Game.TileMap, TileX, TileY )
		Mushroom.Visualizer = Game.Mushroom
		Mushroom.AttachModel( New TAppearing )
	End Function
End Type





Type TAppearing Extends LTBehaviorModel
	Const Speed:Float = 1.0

	Field DestinationY:Float

	
	
	Method Activate( Shape:LTShape )
		Local Sprite:LTVectorSprite = LTVectorSprite( Shape )
		DestinationY = Sprite.Y - Sprite.Height
		Sprite.ShapeType = LTSprite.Circle
		Sprite.Frame = 0
		Sprite.DX = TMushroom.Speed
		Sprite.LimitByWindow( Sprite.X, Sprite.Y - 1.0, 1.0, 1.0 )
		PlaySound( Game.PowerupAppears )
		Game.Level.AddLast( Sprite )
		Game.MovingObjects.InsertSprite( Sprite )
	End Method
	
	
	
	Method ApplyTo( Shape:LTShape )
		If Shape.Y <= DestinationY Then
			Remove( Shape )
		Else
			Shape.Move( 0, -Speed )
		End If
	End Method
	
	
	
	Method Deactivate( Shape:LTShape )
		Shape.RemoveWindowLimit()
		Local Collisions:TCollisions = New TCollisions
		Collisions.SetCollisions( True, False )
		Shape.AttachModel( Collisions )
		Shape.AttachModel( New TGravity )
		Shape.AttachModel( New TBumpingTiles )
		Shape.AttachModel( New TRemoveIfOutside )
	End Method
End Type