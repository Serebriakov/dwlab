'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Include "LTAngularSprite.bmx"
Include "LTVectorSprite.bmx"
Include "LTCamera.bmx"
Include "Collisions.bmx"
Include "Physics.bmx"

Rem
bbdoc: Sprite is the main basic shape of the framework to draw, move and check collisions.
about: See also: #LTAngularSprite, #LTVectorSprite
End Rem
Type LTSprite Extends LTShape
	Rem
	bbdoc: Type of the sprite shape: pivot. It's a point on game field with (X, Y) coordinates.
	End Rem
	Const Pivot:Int = 0
	
	Rem
	bbdoc: Type of the sprite shape: oval / Oval.
	End Rem
	Const Circle:Int = 1
	Const Oval:Int = 1
	
	Rem
	bbdoc: Type of the sprite shape: rectangle.
	End Rem
	Const Rectangle:Int = 2

	Rem
	bbdoc: Type of the sprite shape.
	about: See also: #Pivot, #Oval, #Rectangle
	End Rem
	Field ShapeType:Int = Rectangle
	
	Rem
	bbdoc: Frame of the sprite image.
	about: Can be used with image visualizer only.
	End Rem
	Field Frame:Int
	
	Field SpriteMap:LTSpriteMap
	
	' ==================== Drawing ===================	
	
	Method Draw()
		If Visible Then Visualizer.DrawUsingSprite( Self )
	End Method
	
	
	
	Method DrawUsingVisualizer( Vis:LTVisualizer )
		If Visible Then Vis.DrawUsingSprite( Self )
	End Method
	
	' ==================== Collisions ===================
	
	Method TileShapeCollisionsWithSprite( Sprite:LTSprite, DX:Double, DY:Double, XScale:Double, YScale:Double, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionType:Int )
		If TileSpriteCollidesWithSprite( Sprite, DX, DY, XScale, YScale ) Then Sprite.HandleCollisionWithTile( TileMap, TileX, TileY, CollisionType )
	End Method
	
	

	Rem
	bbdoc: Checks if this sprite collides with given sprite.
	returns: True if the sprite collides with given sprite, False otherwise.
	End Rem
	Method CollidesWithSprite:Int( Sprite:LTSprite )
		?debug
		L_CollisionChecks :+ 1
		?
		Select ShapeType
			Case Pivot
				Select Sprite.ShapeType
					Case Pivot
						Return L_PivotWithPivot( X, Y, Sprite.X, Sprite.Y )
					Case Oval
						Return L_PivotWithOval( X, Y, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case Rectangle
						Return L_PivotWithRectangle( X, Y, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
			Case Oval
				Select Sprite.ShapeType
					Case Pivot
						Return L_PivotWithOval( Sprite.X, Sprite.Y, X, Y, Width, Height )
					Case Oval
						Return L_OvalWithOval( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case Rectangle
						Return L_OvalWithRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
			Case Rectangle
				Select Sprite.ShapeType
					Case Pivot
						Return L_PivotWithRectangle( Sprite.X, Sprite.Y, X, Y, Width, Height )
					Case Oval
						Return L_OvalWithRectangle( Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, X, Y, Width, Height )
					Case Rectangle
						Return L_RectangleWithRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
		End Select
	End Method
	
	
	
	Rem
	bbdoc: Checks if the sprite collides with given line.
	returns: True if the sprite collides with given line, otherwise false.
	about: Only collision of line and Oval is yet implemented.
	End Rem
	Method CollidesWithLine:Int( Line:LTLine )
		?debug
		L_CollisionChecks :+ 1
		?
		Select ShapeType
			Case Pivot
				L_Error( "Line with pivot collision is not yet implemented" )
				'Return L_PivotWithLine( Self, Line )
			Case Oval
				Return L_OvalWithLine( X, Y, Width, Height, Line.Pivot[ 0 ].X, Line.Pivot[ 0 ].Y, Line.Pivot[ 1 ].X, Line.Pivot[ 1 ].Y )
			Case Rectangle
				L_Error( "Line with rectangle collision is not yet implemented" )
				'Return L_RectangleWithLine( Self, Line )
		End Select
	End Method
	
	
	
	Method TileSpriteCollidesWithSprite:Int( Sprite:LTSprite, DX:Double, DY:Double, XScale:Double, YScale:Double )
		?debug
		L_CollisionChecks :+ 1
		?
		Select Sprite.ShapeType
			Case Pivot
				Select ShapeType
					Case Pivot
						Return L_PivotWithPivot( Sprite.X, Sprite.Y, X * XScale + DX, Y * YScale + DY )
					Case Oval
						Return L_PivotWithOval( Sprite.X, Sprite.Y, X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale )
					Case Rectangle
						Return L_PivotWithRectangle( Sprite.X, Sprite.Y, X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale )
				End Select
			Case Oval
				Select ShapeType
					Case Pivot
						Return L_PivotWithOval( X * XScale + DX, Y * YScale + DY, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case Oval
						Return L_OvalWithOval( Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale )
					Case Rectangle
						Return L_OvalWithRectangle( Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale )
				End Select
			Case Rectangle
				Select ShapeType
					Case Pivot
						Return L_PivotWithRectangle( X * XScale + DX, Y * YScale + DY, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case Oval
						Return L_OvalWithRectangle( X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case Rectangle
						Return L_RectangleWithRectangle( Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale )
				End Select
		End Select
	End Method
	
	
	
	Rem
	bbdoc: Checks if the sprite overlaps given sprite.
	returns: True if the sprite overlaps given sprite, otherwise false.
	about: Pivot overlapping is not supported.
	End Rem
	Method Overlaps:Int( Sprite:LTSprite )
		?debug
		L_CollisionChecks :+ 1
		?
		Select ShapeType
			Case Pivot
				 L_Error( "Pivot overlapping is not supported" )
			Case Oval
				Select Sprite.ShapeType
					Case Pivot
						Return L_OvalOverlapsOval( X, Y, Width, Height, Sprite.X, Sprite.Y, 0, 0 )
					Case Oval
						Return L_OvalOverlapsOval( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case Rectangle
						Return L_RectangleOverlapsRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
			Case Rectangle
				Select Sprite.ShapeType
					Case Pivot
						Return L_RectangleOverlapsRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, 0, 0 )
					Case Oval
						Return L_RectangleOverlapsRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case Rectangle
						Return L_RectangleOverlapsRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
		End Select
	End Method
	
	
	
	Rem
	bbdoc: Executes reaction for collision of sprite with shapes in given group.
	about: For every collided shape collision handling method will be executed and corresponding parameters will be passed to this method.
	You can specify collision type which will be passed to this method too.
	
	See also: #CollisionsWithSprite, #CollisionsWithTileMap, #CollisionsWithLine, #CollisionsWithSpriteMap, #Horizontal, #Vertical
	End Rem
	Method CollisionsWithGroup( Group:LTGroup, CollisionType:Int = 0 )
		For Local Shape:LTShape = EachIn Group
			Shape.SpriteGroupCollisions( Self, CollisionType )
		Next
	End Method
	
	
	
	Rem
	bbdoc: Executes reaction for collision of sprite with given sprite.
	about: If sprites collide then HandleCollisionWithSprite() method will be executed and given sprite will be passed to this method.
	You can specify collision type which will be passed to this method too.
	
	See also: #CollisionsWithGroup, #CollisionsWithTileMap, #CollisionsWithLine, #CollisionsWithSpriteMap, #Horizontal, #Vertical
	End Rem
	Method CollisionsWithSprite( Sprite:LTSprite, CollisionType:Int = 0 )
		If CollidesWithSprite( Sprite ) Then HandleCollisionWithSprite( Sprite, CollisionType )
	End Method

	
	
	
	Rem
	bbdoc: Executes reaction for collision of sprite with tiles in given tilemap.
	about: For every collided tile HandleCollisionWithTile() method will be executed and tilemap with tile indexes will be passed to this method.
	You can specify collision type which will be passed to this method too.
	
	See also: #CollisionsWithGroup, #CollisionsWithSprite, #CollisionsWithLine, #CollisionsWithSpriteMap, #Horizontal, #Vertical
	End Rem
	Method CollisionsWithTileMap( TileMap:LTTileMap, CollisionType:Int = 0 )
		Local X0:Double = TileMap.LeftX()
		Local Y0:Double = TileMap.TopY()
		Local CellWidth:Double = TileMap.GetTileWidth()
		Local CellHeight:Double = TileMap.GetTileHeight()
		Local XQuantity:Int = TileMap.XQuantity
		Local YQuantity:Int = TileMap.YQuantity
		Local Tileset:LTTileSet = TileMap.Tileset
				
		Select ShapeType
			Case Pivot
				Local TileX:Int = Floor( ( X - X0 ) / CellWidth )
				Local TileY:Int = Floor( ( Y - Y0 ) / CellHeight )
				
				If TileX >= 0 And TileY >= 0 And TileX < XQuantity And TileY < YQuantity Then
					Local Shape:LTShape = Tileset.CollisionShape[ TileMap.Value[ TileX, TileY ] ]
					If Shape Then Shape.TileShapeCollisionsWithSprite( Self, X0 + CellWidth * TileX, Y0 + CellHeight * TileY, CellWidth, CellHeight, TileMap, TileX, TileY, CollisionType )
				End If
			Case Oval, Rectangle
				Local X1:Int = Floor( ( X - 0.5 * Width - X0 ) / CellWidth )
				Local Y1:Int = Floor( ( Y - 0.5 * Height - Y0 ) / CellHeight )
				Local X2:Int = Floor( ( X + 0.5 * Width - X0 - L_Inaccuracy ) / CellWidth )
				Local Y2:Int = Floor( ( Y + 0.5 * Height - Y0 - L_Inaccuracy ) / CellHeight )
				
				If X2 >= 0 And Y2 >= 0 And X1 < XQuantity And Y1 < YQuantity Then
					X1 = L_LimitInt( X1, 0, XQuantity - 1 )
					Y1 = L_LimitInt( Y1, 0, YQuantity - 1 )
					X2 = L_LimitInt( X2, 0, XQuantity - 1 )
					Y2 = L_LimitInt( Y2, 0, YQuantity - 1 )
					
					For Local TileY:Int = Y1 To Y2
						For Local TileX:Int = X1 To X2
							Local Shape:LTShape = Tileset.CollisionShape[ TileMap.Value[ TileX, TileY ] ]
							If Shape Then Shape.TileShapeCollisionsWithSprite( Self, X0 + CellWidth * TileX, Y0 + CellHeight * TileY, CellWidth, CellHeight, TileMap, TileX, TileY, CollisionType )
						Next
					Next
				End If
		End Select
	End Method
	
	
	
	Rem
	bbdoc: Executes reaction for collision of sprite with given line.
	about: If sprite collides with line then HandleCollisionWithLine() method will be executed and line will be passed to this method.
	You can specify collision type which will be passed to this method too.
	
	See also: #CollisionsWithGroup, #CollisionsWithSprite, #CollisionsWithTileMap, #CollisionsWithSpriteMap, #Horizontal, #Vertical
	End Rem
	Method CollisionsWithLine( Line:LTLine, CollisionType:Int = 0 )
		If CollidesWithLine( Line ) Then HandleCollisionWithLine( Line, CollisionType )
	End Method
	
	
	
	Rem
	bbdoc: Executes reaction for collision of sprite with sprites in sprite map.
	about: For every collided sprite HandleCollisionWithSprite() method will be executed and collided srite will be passed to this method.
	You can specify collision type which will be passed to this method too.
	Map parameter allows you to specify map to where collided sprites will be added as keys.
	
	See also: #CollisionsWithGroup, #CollisionsWithSprite, #CollisionsWithTileMap, #CollisionsWithLine, #Horizontal, #Vertical
	End Rem
	Method CollisionsWithSpriteMap( SpriteMap:LTSpriteMap, CollisionType:Int = 0, Map:TMap = Null )
		If Not Map Then Map = New TMap
		Select ShapeType
			Case Pivot
				For Local MapSprite:LTSprite = EachIn SpriteMap.Lists[ Int( X / SpriteMap.CellWidth ) & SpriteMap.XMask, Int( Y / SpriteMap.CellHeight ) & SpriteMap.YMask ]
					If Self = MapSprite Then Continue
					If CollidesWithSprite( MapSprite ) Then
						If Not Map.Contains( MapSprite ) Then
							Map.Insert( MapSprite, Null )
							HandleCollisionWithSprite( MapSprite, CollisionType )
						End If
					End If
				Next
			Default
				Local MapX1:Int = Floor( ( X - 0.5 * Width ) / SpriteMap.CellWidth )
				Local MapY1:Int = Floor( ( Y - 0.5 * Height ) / SpriteMap.CellHeight )
				Local MapX2:Int = Floor( ( X + 0.5 * Width - L_Inaccuracy ) / SpriteMap.CellWidth )
				Local MapY2:Int = Floor( ( Y + 0.5 * Height - L_Inaccuracy ) / SpriteMap.CellHeight )
				
				For Local CellY:Int = MapY1 To MapY2
					For Local CellX:Int = MapX1 To MapX2
						For Local MapSprite:LTSprite = EachIn SpriteMap.Lists[ CellX & SpriteMap.XMask, CellY & SpriteMap.YMask ]
							If Self = MapSprite Then Continue
							If CollidesWithSprite( MapSprite ) Then
								If Not Map.Contains( MapSprite ) Then
									Map.Insert( MapSprite, Null )
									HandleCollisionWithSprite( MapSprite, CollisionType )
								End If
							End If
						Next
					Next
				Next
		End Select
	End Method
	
	
	
	Method SpriteGroupCollisions( Sprite:LTSprite, CollisionType:Int = 0 )
		If Sprite.CollidesWithSprite( Self ) Then Sprite.HandleCollisionWithSprite( Self, CollisionType )
	End Method
	
	
	
	Rem
	bbdoc: Sprite collision handling method.
	about: Will be executed for every collision of this sprite with another sprite found by the collision checks.
	End Rem
	Method HandleCollisionWithSprite( Sprite:LTSprite, CollisionType:Int = 0 )
		If Active Then
			For Local Model:LTBehaviorModel = EachIn BehaviorModels
				If Model.Active Then Model.HandleCollisionWithSprite( Self, Sprite, CollisionType )
			Next
		End If
	End Method
	
	
	
	Rem
	bbdoc: Tile collision handling method.
	about: Will be executed for every collision of this sprite with tile found by the collision checks for this sprite with tilemaps.
	End Rem
	Method HandleCollisionWithTile( TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionType:Int = 0 )
		If Active Then
			For Local Model:LTBehaviorModel = EachIn BehaviorModels
				If Model.Active Then Model.HandleCollisionWithTile( Self, TileMap, TileX, TileY, CollisionType )
			Next
		End If
	End Method
	

	
	Rem
	bbdoc: Line collision handling method.
	about: Will be executed for every collision of this sprite with line found by the collision checks.
	End Rem
	Method HandleCollisionWithLine( Line:LTLine, CollisionType:Int )
	End Method
	
	' ==================== Wedging off ====================
	
	Rem
	bbdoc: Wedges off sprite with given sprite.
	about: Pushes sprites from each other until they stops colliding. More the mass, less the sprite will be moved.
	<ul>
	<li> If each sprite's mass is zero, or each sprite's mass is less than 0 then sprites will be moved on same distance.
	<li> If one of the sprite has zero mass and other's mass is non-zero, only zero-mass sprite will be moved
	<li> If one of the sprite has mass less than 0 and other has mass less or equal to 0, then only zero-or-more-mass sprite will be moved.
	</ul>
	End Rem
	Method WedgeOffWithSprite( Sprite:LTSprite, SelfMass:Double, SpriteMass:Double )
		Local DX:Double, DY:Double
		Select ShapeType
			Case Pivot
				Select Sprite.ShapeType
					Case Pivot
						Return
					Case Oval
						L_WedgingValuesOfOvalAndOval( X, Y, 0, 0, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, DX, DY )
					Case Rectangle
						L_WedgingValuesOfRectangleAndRectangle( X, Y, 0, 0, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, DX, DY )
				End Select
			Case Oval
				Select Sprite.ShapeType
					Case Pivot
						L_WedgingValuesOfOvalAndOval( X, Y, Width, Height, Sprite.X, Sprite.Y, 0, 0, DX, DY )
					Case Oval
						L_WedgingValuesOfOvalAndOval( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, DX, DY )
					Case Rectangle
						L_WedgingValuesOfOvalAndRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, DX, DY )
				End Select
			Case Rectangle
				Select Sprite.ShapeType
					Case Pivot
						L_WedgingValuesOfRectangleAndRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, 0, 0, DX, DY )
					Case Oval
						L_WedgingValuesOfOvalAndRectangle( Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, X, Y, Width, Height, DX, DY )
						L_Separate( Sprite, Self, DX, DY, SpriteMass, SelfMass )
						Return
					Case Rectangle
						L_WedgingValuesOfRectangleAndRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, DX, DY )
				End Select
		End Select
		L_Separate( Self, Sprite, DX, DY, SelfMass, SpriteMass )
	End Method
	
	
	
	Rem
	bbdoc: Pushes sprite from given sprite.
	about: See also : #WedgeOffWithSprite, #PushFromTile
	End Rem
	Method PushFromSprite( Sprite:LTSprite )
		WedgeOffWithSprite( Sprite, 0.0, 1.0 )
	End Method
	
	
	
	Rem
	bbdoc: Pushes sprite from given tile.
	about: See also : #PushFromSprite
	End Rem
	Method PushFromTile( TileMap:LTTileMap, TileX:Int, TileY:Int )
		Local CellWidth:Double = TileMap.GetTileWidth()
		Local CellHeight:Double = TileMap.GetTileHeight()
		Local X:Double = TileMap.LeftX() + CellWidth * TileX
		Local Y:Double = TileMap.TopY() + CellHeight * TileY
		Local Shape:LTShape = TileMap.GetTileCollisionShape( TileX, TileY )
		Local Sprite:LTSprite = LTSprite( Shape )
		If Sprite Then
			PushFromTileSprite( Sprite, X, Y, CellWidth, CellHeight )
		Else
			For Sprite = EachIn LTGroup( Shape ).Children
				If Sprite.TileSpriteCollidesWithSprite( Self, X, Y, CellWidth, CellHeight ) Then
					PushFromTileSprite( Sprite, TileMap.LeftX() + CellWidth * TileX, TileMap.TopY() + CellHeight * TileY, CellWidth, CellHeight )
				End If
			Next
		End If
	End Method


	
	Method PushFromTileSprite( TileSprite:LTSprite, DX:Double, DY:Double, XScale:Double, YScale:Double )
		Local PushingDX:Double, PushingDY:Double
		Select ShapeType
			Case Pivot
				Select TileSprite.ShapeType
					Case Pivot
						Return
					Case Oval
						L_WedgingValuesOfOvalAndOval( X, Y, 0, 0, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, PushingDX, PushingDY )
					Case Rectangle
						L_WedgingValuesOfRectangleAndRectangle( X, Y, 0, 0, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, PushingDX, PushingDY )
				End Select
			Case Oval
				Select TileSprite.ShapeType
					Case Pivot
						L_WedgingValuesOfOvalAndOval( X, Y, Width, Height, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, 0, 0, PushingDX, PushingDY )
					Case Oval
						L_WedgingValuesOfOvalAndOval( X, Y, Width, Height, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, PushingDX, PushingDY )
					Case Rectangle
						L_WedgingValuesOfOvalAndRectangle( X, Y, Width, Height, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, PushingDX, PushingDY )
				End Select
			Case Rectangle
				Select TileSprite.ShapeType
					Case Pivot
						L_WedgingValuesOfRectangleAndRectangle( X, Y, Width, Height, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, 0, 0, PushingDX, PushingDY )
					Case Oval
						L_WedgingValuesOfOvalAndRectangle( TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, X, Y, Width, Height, PushingDX, PushingDY )
						L_Separate( TileSprite, Self, PushingDX, PushingDY, 1.0, 0.0 )
						Return
					Case Rectangle
						L_WedgingValuesOfRectangleAndRectangle( X, Y, Width, Height, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, PushingDX, PushingDY )
				End Select
		End Select
		L_Separate( Self, TileSprite, PushingDX, PushingDY, 0.0, 1.0 )
	End Method

	' ==================== Position and size ====================
	
	Method SetCoords( NewX:Double, NewY:Double )
		If SpriteMap Then SpriteMap.RemoveSprite( Self, False )
		
		X = NewX
		Y = NewY
		
		Update()
		If SpriteMap Then SpriteMap.InsertSprite( Self, False )
	End Method
	
	
	
	Rem
	bbdoc: Moves sprite forward.
	about:
	<ul>
	<li> Angular sprite will be moved using its Angle and Velocity parameters.
	<li> Vector sprite will be moved using its DX and DY parameters.
	</ul>
	
	See also: #Move
	End Rem
	Method MoveForward()
	End Method
	
	
	
	Method SetSize( NewWidth:Double, NewHeight:Double )
		If SpriteMap Then SpriteMap.RemoveSprite( Self, False )
		
		Width = NewWidth
		Height = NewHeight

		Update()
		If SpriteMap Then SpriteMap.InsertSprite( Self, False )
	End Method
	
	
	
	Rem
	bbdoc: Sets the sprite as a tile.
	about: Position, size, visualizer and frame will be changed. This method can be used to cover other shapes with the tile or voluntary moving the tile.
	End Rem
	Method SetAsTile( TileMap:LTTileMap, TileX:Int, TileY:Int )
		Width = TileMap.GetTileWidth()
		Height = TileMap.GetTileHeight()
		X = TileMap.LeftX() + Width * ( 0.5 + TileX )
		Y = TileMap.TopY() + Height * ( 0.5 + TileY )
		Visualizer = LTVisualizer.FromImage( Tilemap.TileSet.Image )
		Frame = TileMap.GetTile( TileX, TileY )
	End Method
	
	' ==================== Animation ====================
	
	Rem
	bbdoc: Animates the sprite.
	about: 
	End Rem
	Method Animate( Project:LTProject, Speed:Double, FramesQuantity:Int = 0, FrameStart:Int = 0, StartingTime:Double = 0.0, PingPong:Int = False )
		If FramesQuantity = 0 Then FramesQuantity = Visualizer.Image.FramesQuantity()
		Local ModFactor:Int = FramesQuantity
		If PingPong Then ModFactor = FramesQuantity * 2 - 2
		Frame = Floor( ( Project.Time - StartingTime ) / Speed ) Mod ModFactor
		If PingPong And Frame >= FramesQuantity Then Frame = ModFactor - Frame
		Frame :+ FrameStart
	End Method
	
	' ==================== Other ====================	

	Method Clone:LTShape()
		Local NewSprite:LTSprite = New LTSprite
		CopyTo( NewSprite )
		Return NewSprite
	End Method

	
	
	Method CopyTo( Shape:LTShape )
		Super.CopyTo( Shape )
		Local Sprite:LTSprite = LTSprite( Shape )
		
		?debug
		If Not Sprite Then L_Error( "Trying to copy sprite ~q" + Shape.GetTitle() + "~q data to non-sprite" )
		?
		
		Sprite.ShapeType = ShapeType
		Sprite.Frame = Frame
	End Method
	
	
	
	Method XMLIO( XMLObject:LTXMLObject )
		Super.XMLIO( XMLObject )
		
		XMLObject.ManageIntAttribute( "shape", ShapeType )
		XMLObject.ManageIntAttribute( "frame", Frame )
	End Method
End Type