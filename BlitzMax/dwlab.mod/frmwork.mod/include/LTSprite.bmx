'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Include "LTShapeType.bmx"
Include "LTVectorSprite.bmx"
Include "LTSpriteHandler.bmx"
Include "LTSpritesInteraction.bmx"
Include "CollisionHandlers.bmx"

Rem
bbdoc: Sprite is the main basic shape of the framework to draw, move and check collisions.
about: See also: #LTVectorSprite
End Rem
Type LTSprite Extends LTShape
	Rem
	bbdoc: Type of the sprite shape: pivot. It's a point on game field with (X, Y) coordinates.
	End Rem
	Global Pivot:LTPivot = New LTPivot
	
	Rem
	bbdoc: Type of the sprite shape: oval / circle which is inscribed in shape's rectangle.
	End Rem
	Global Oval:LTOval = New LTOval
	Global Circle:LTOval = Oval
	
	Rem
	bbdoc: Type of the sprite shape: rectangle.
	End Rem
	Global Rectangle:LTRectangle = New LTRectangle
	
	Rem
	bbdoc: Type of the sprite shape: ray which starts in (X, Y) and directed as Angle.
	End Rem
	Global Ray:LTRay = New LTRay
	
	Rem
	bbdoc: Type of the sprite shape: right triangle which is inscribed in shape's rectangle and have right angle situated in corresponding corner.
	End Rem
	Global TopLeftTriangle:LTTopLeftTriangle = New LTTopLeftTriangle
	Global TopRightTriangle:LTTopLeftTriangle = New LTTopRightTriangle
	Global BottomLeftTriangle:LTBottomLeftTriangle = New LTBottomLeftTriangle
	Global BottomRightTriangle:LTBottomRightTriangle = New LTBottomRightTriangle
	
	Rem
	bbdoc: Type of the sprite shape: mask of raster image which is inscribed in shape's rectangle.
	End Rem
	Global Raster:LTRaster = New LTRaster
	
	Global SpriteTemplate:LTSpriteTemplate = New LTSpriteTemplate
	
	Rem
	bbdoc: Type of the sprite shape.
	about: See also: #Pivot, #Oval, #Rectangle
	End Rem
	Field ShapeType:LTShapeType = Rectangle

	Rem
	bbdoc: Direction of the sprite
	about: See also: #MoveForward, #MoveTowards
	End Rem
	Field Angle:Double
	
	Rem
	bbdoc: Angle of displaying image.
	about: Displaying angle is relative to sprite's direction if visualizer's rotating flag is set to True.
	End Rem
	Field DisplayingAngle:Double = 0.0
	
	Rem
	bbdoc: Velocity of the sprite in units per second.
	about: See also: #MoveForward, #MoveTowards
	End Rem
	Field Velocity:Double = 1.0
	
	Rem
	bbdoc: Frame of the sprite image.
	about: Can only be used with visualizer which have images.
	End Rem
	Field Frame:Int
	
	Field SpriteMap:LTSpriteMap
	
	
	
	Method GetClassTitle:String()
		Return "Sprite"
	End Method
	
	' ==================== Creating ===================	
	
	Rem
	bbdoc: Creates sprite using given shape type.
	returns: Created sprite.
	End Rem
	Function FromShapeType:LTSprite( ShapeType:LTShapeType = Null )
		Local Sprite:LTSprite = New LTSprite
		If ShapeType Then Sprite.ShapeType = ShapeType Else Sprite.ShapeType = LTSprite.Rectangle
		Return Sprite
	End Function
	
	
	
	Rem
	bbdoc: Creates sprite using given coordinates, size, shape type, angle and velocity.
	returns: Created sprite.
	about: See also #Overlaps example.
	End Rem
	Function FromShape:LTSprite( X:Double = 0.0, Y:Double = 0.0, Width:Double = 1.0, Height:Double = 1.0, ShapeType:LTShapeType = Null, ..
			Angle:Double = 0.0, Velocity:Double = 1.0 )
		Local Sprite:LTSprite = New LTSprite
		Sprite.SetCoords( X, Y )
		Sprite.SetSize( Width, Height )
		If ShapeType Then Sprite.ShapeType = ShapeType Else Sprite.ShapeType = LTSprite.Rectangle
		Sprite.Angle = Angle
		Sprite.Velocity = Velocity
		Return Sprite
	End Function
	
	' ==================== Drawing ===================	
	
	Method Draw( DrawingAlpha:Double = 1.0 )
		Visualizer.DrawUsingSprite( Self, Self, DrawingAlpha )
	End Method
	
	
	
	Method DrawUsingVisualizer( Vis:LTVisualizer, DrawingAlpha:Double = 1.0 )
		Vis.DrawUsingSprite( Self, Self, DrawingAlpha )
	End Method
	
	' ==================== Collisions ===================
	
	Method TileShapeCollisionsWithSprite( Sprite:LTSprite, X:Double, Y:Double, XScale:Double, YScale:Double, TileMap:LTTileMap, TileX:Int, TileY:Int, ..
			Handler:LTSpriteAndTileCollisionHandler )
		If Sprite.CollidesWithSprite( ShapeType.GetTileSprite( Self, X, Y, XScale, YScale ) ) Then Handler.HandleCollision( Sprite, TileMap, TileX, TileY, Self )
	End Method
	
	

	Rem
	bbdoc: Checks if this sprite collides with given sprite.
	returns: True if the sprite collides with given sprite, False otherwise.
	End Rem
	Method CollidesWithSprite:Int( Sprite:LTSprite )
		?debug
		L_CollisionChecks :+ 1
		?
		Local ShapeTypeNum:Int = ShapeType.GetNum()
		Local SpriteShapeTypeNum:Int = Sprite.ShapeType.GetNum()
		If ShapeTypeNum <= SpriteShapeTypeNum Then
			Local Interaction:LTSpritesInteraction = LTSpritesInteraction.InteractionsArray[ ShapeTypeNum, SpriteShapeTypeNum ]
			If Interaction Then Return Interaction.SpritesCollide( Self, Sprite )
		Else
			Local Interaction:LTSpritesInteraction = LTSpritesInteraction.InteractionsArray[ SpriteShapeTypeNum, ShapeTypeNum ]
			If Interaction Then Return Interaction.SpritesCollide( Sprite, Self )
		End If
	End Method
	
	
	
	Rem
	bbdoc: Checks if the sprite collides with given line.
	returns: True if the sprite collides with given line, otherwise false.
	about: Only collision of line and Oval is yet implemented.
	End Rem
	Method CollidesWithLineSegment:Int( LineSegment:LTLineSegment )
		?debug
		L_CollisionChecks :+ 1
		?
		Local Handler:LTSpriteHandler = LTSpriteHandler.HandlersArray[ ShapeType.GetNum() ]
		If Handler Then Return Handler.SpriteCollidesWithLineSegment( Self, LineSegment.Pivot[ 0 ], LineSegment.Pivot[ 1 ] )
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
		Local Interaction:LTSpritesInteraction = LTSpritesInteraction.InteractionsArray[ ShapeType.GetNum(), Sprite.ShapeType.GetNum() ]
		If Interaction Then Return Interaction.SpriteOverlapsSprite( Self, Sprite )
	End Method
	
	
	
	Rem
	bbdoc: Searches the layer for first sprite which collides with given.
	Returns: First found sprite which collides with given.
	about: Included layers will be also checked.
	
	See also: #Clone example
	End Rem	
	Method FirstCollidedSpriteOfLayer:LTSprite( Layer:LTLayer )
		For Local Shape:LTShape = EachIn Layer
			If Shape <> Self Then
				Local Collided:LTSprite = Shape.LayerFirstSpriteCollision( Self )
				If Collided Then Return Collided
			End If
		Next
	End Method
	
	
	
	Method LayerFirstSpriteCollision:LTSprite( Sprite:LTSprite )
		If CollidesWithSprite( Sprite ) Then Return Self
	End Method
	
	
	
	Rem
	bbdoc: Executes given collision handler for collision of sprite with given sprite.
	about: If sprites collide then HandleCollisionWithSprite() method will be executed and given sprite will be passed to this method.
	You can specify collision type which will be passed to this method too.
	
	See also: #CollisionsWithLayer, #CollisionsWithTileMap, #CollisionsWithLine, #CollisionsWithSpriteMap, #Horizontal, #Vertical
	End Rem
	Method CollisionsWithSprite( Sprite:LTSprite, Handler:LTSpriteCollisionHandler )
		If CollidesWithSprite( Sprite ) Then Handler.HandleCollision( Self, Sprite )
	End Method
	
	
	
	Rem
	bbdoc: Executes given collision handler for collision of sprite with shapes in given group.
	about: For every collided shape collision handling method will be executed and corresponding parameters will be passed to this method.
	You can specify collision type which will be passed to this method too.
	
	See also: #CollisionsWithSprite, #CollisionsWithTileMap, #CollisionsWithLine, #CollisionsWithSpriteMap, #Horizontal, #Vertical
	End Rem
	Method CollisionsWithLayer( Layer:LTLayer, Handler:LTSpriteCollisionHandler )
		For Local Shape:LTShape = EachIn Layer
			If Shape <> Self Then Shape.SpriteLayerCollisions( Self, Handler )
		Next
	End Method
	
	
	
	Method SpriteLayerCollisions( Sprite:LTSprite, Handler:LTSpriteCollisionHandler )
		If Sprite.CollidesWithSprite( Self ) Then Handler.HandleCollision( Sprite, Self )
	End Method
	
	
	
	Rem
	bbdoc: Executes given collision handler for collision of sprite with given line.
	about: If sprite collides with line then HandleCollisionWithLine() method will be executed and line will be passed to this method.
	You can specify collision type which will be passed to this method too.
	
	See also: #CollisionsWithLayer, #CollisionsWithSprite, #CollisionsWithTileMap, #CollisionsWithSpriteMap, #Horizontal, #Vertical
	End Rem
	Method CollisionsWithLineSegment( LineSegment:LTLineSegment, Handler:LTSpriteAndLineSegmentCollisionHandler )
		If CollidesWithLineSegment( LineSegment ) Then Handler.HandleCollision( Self, LineSegment )
	End Method

	
	
	
	Rem
	bbdoc: Executes given collision handler for collision of sprite with tiles in given tilemap.
	about: For every collided tile HandleCollisionWithTile() method will be executed and tilemap with tile indexes will be passed to this method.
	You can specify collision type which will be passed to this method too.
	
	See also: #CollisionsWithLayer, #CollisionsWithSprite, #CollisionsWithLine, #CollisionsWithSpriteMap, #Horizontal, #Vertical, #LTVectorSprite example
	End Rem
	Method CollisionsWithTileMap( TileMap:LTTileMap, Handler:LTSpriteAndTileCollisionHandler )
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
					If Shape Then Shape.TileShapeCollisionsWithSprite( Self, X0 + CellWidth * TileX, Y0 + CellHeight * TileY, CellWidth, CellHeight, ..
							TileMap, TileX, TileY, Handler )
				End If
			Case Ray
			Default
				Local X1:Int = Floor( ( X - 0.5:Double * Width - X0 ) / CellWidth )
				Local Y1:Int = Floor( ( Y - 0.5:Double * Height - Y0 ) / CellHeight )
				Local X2:Int = Floor( ( X + 0.5:Double * Width - X0 - L_Inaccuracy ) / CellWidth )
				Local Y2:Int = Floor( ( Y + 0.5:Double * Height - Y0 - L_Inaccuracy ) / CellHeight )
				
				If X2 >= 0 And Y2 >= 0 And X1 < XQuantity And Y1 < YQuantity Then
					X1 = L_LimitInt( X1, 0, XQuantity - 1 )
					Y1 = L_LimitInt( Y1, 0, YQuantity - 1 )
					X2 = L_LimitInt( X2, 0, XQuantity - 1 )
					Y2 = L_LimitInt( Y2, 0, YQuantity - 1 )
					
					For Local TileY:Int = Y1 To Y2
						For Local TileX:Int = X1 To X2
							Local Shape:LTShape = Tileset.CollisionShape[ TileMap.Value[ TileX, TileY ] ]
							If Shape Then Shape.TileShapeCollisionsWithSprite( Self, X0 + CellWidth * TileX, Y0 + CellHeight * TileY, CellWidth, CellHeight, ..
									TileMap, TileX, TileY, Handler )
						Next
					Next
				End If
		End Select
	End Method
	
	
	
	Rem
	bbdoc: Executes reaction for collision of sprite with sprites in sprite map.
	about: For every collided sprite HandleCollisionWithSprite() method will be executed and collided srite will be passed to this method.
	You can specify collision type which will be passed to this method too.
	Map parameter allows you to specify map to where collided sprites will be added as keys.
	
	See also: #CollisionsWithGroup, #CollisionsWithSprite, #CollisionsWithTileMap, #CollisionsWithLine, #Horizontal, #Vertical, #LTSpriteMap example
	End Rem
	Method CollisionsWithSpriteMap( SpriteMap:LTSpriteMap, Handler:LTSpriteCollisionHandler, Map:TMap = Null )
		If Not Map Then Map = New TMap
		Select ShapeType
			Case Pivot
				Local XX:Int = Int( X / SpriteMap.CellWidth ) & SpriteMap.XMask
				Local YY:Int = Int( Y / SpriteMap.CellHeight ) & SpriteMap.YMask
				For Local N:Int = 0 Until SpriteMap.ListSize[ XX, YY ]
					Local MapSprite:LTSprite = SpriteMap.Lists[ XX, YY ][ N ]
					If Self = MapSprite Then Continue
					If CollidesWithSprite( MapSprite ) Then
						If Not Map.Contains( MapSprite ) Then
							Map.Insert( MapSprite, Null )
							Handler.HandleCollision( Self, MapSprite )
						End If
					End If
				Next
			Case Ray
			Default
				Local MapX1:Int = Floor( ( X - 0.5:Double * Width ) / SpriteMap.CellWidth )
				Local MapY1:Int = Floor( ( Y - 0.5:Double * Height ) / SpriteMap.CellHeight )
				Local MapX2:Int = Floor( ( X + 0.5:Double * Width - L_Inaccuracy ) / SpriteMap.CellWidth )
				Local MapY2:Int = Floor( ( Y + 0.5:Double * Height - L_Inaccuracy ) / SpriteMap.CellHeight )
				
				For Local CellY:Int = MapY1 To MapY2
					For Local CellX:Int = MapX1 To MapX2
						For Local N:Int = 0 Until SpriteMap.ListSize[ CellX & SpriteMap.XMask, CellY & SpriteMap.YMask ]
							Local MapSprite:LTSprite = SpriteMap.Lists[ CellX & SpriteMap.XMask, CellY & SpriteMap.YMask ][ N ]
							If Self = MapSprite Then Continue
							If CollidesWithSprite( MapSprite ) Then
								If Not Map.Contains( MapSprite ) Then
									Map.Insert( MapSprite, Null )
									Handler.HandleCollision( Self, MapSprite )
								End If
							End If
						Next
					Next
				Next
		End Select
	End Method
	
	' ==================== Wedging off ====================
	
	Rem
	bbdoc: Wedges off sprite with given sprite.
	about: Pushes sprites from each other until they stops colliding. More the moving resistance, less the sprite will be moved.
	<ul>
	<li> If each sprite's moving resistance is zero, or each sprite's moving resistance is less than 0 then sprites will be moved on same distance.
	<li> If one of the sprite has zero moving resistance and other's moving resistance is non-zero, only zero-moving-resistance sprite will be moved
	<li> If one of the sprite has moving resistance less than 0 and other has moving resistance more or equal to 0, then only zero-or-more-moving-resistance sprite will be moved.
	</ul>
	End Rem
	Method WedgeOffWithSprite( Sprite:LTSprite, SelfMovingResistance:Double = 0.5, SpriteMovingResistance:Double = 0.5 )
		Local DX:Double, DY:Double
		Local ShapeTypeNum:Int = ShapeType.GetNum()
		Local SpriteShapeTypeNum:Int = Sprite.ShapeType.GetNum()
		If ShapeTypeNum <= SpriteShapeTypeNum Then
			Local Interaction:LTSpritesInteraction = LTSpritesInteraction.InteractionsArray[ ShapeTypeNum, SpriteShapeTypeNum ]
			If Interaction Then 
				Interaction.WedgeOffSprites( Self, Sprite, DX, DY )
				Separate( Self, Sprite, DX, DY, SelfMovingResistance, SpriteMovingResistance )
			End If
		Else
			Local Interaction:LTSpritesInteraction = LTSpritesInteraction.InteractionsArray[ SpriteShapeTypeNum, ShapeTypeNum ]
			If Interaction Then 
				Interaction.WedgeOffSprites( Sprite, Self, DX, DY )
				Separate( Sprite, Self, DX, DY, SpriteMovingResistance, SelfMovingResistance )
			End If
		End If
	End Method
	
	
	
	Function Separate( Pivot1:LTSprite, Pivot2:LTSprite, DX:Double, DY:Double, Pivot1MovingResistance:Double, Pivot2MovingResistance:Double )
		Local K1:Double, K2:Double
		
		If Pivot1MovingResistance < 0 then
			If Pivot2MovingResistance < 0 Then
				Return
			End If
			Pivot1MovingResistance = 1.0
			Pivot1MovingResistance = 0.0
		ElseIf Pivot2MovingResistance < 0 Then
			Pivot1MovingResistance = 0.0
			Pivot2MovingResistance = 1.0		
		End If
		
		Local MovingResistanceSum:Double = Pivot1MovingResistance + Pivot2MovingResistance
		If MovingResistanceSum Then
			K1 = Pivot2MovingResistance / MovingResistanceSum
			K2 = Pivot1MovingResistance / MovingResistanceSum
		Else
			K1 = 0.5
			K2 = 0.5
		End If
		
		IF K1 <> 0.0 Then Pivot1.AlterCoords( K1 * DX, K1 * DY )
		IF K2 <> 0.0 Then Pivot2.AlterCoords( -K2 * DX, -K2 * DY )
	End Function
	
	
	
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
			PushFromSprite( Sprite.ShapeType.GetTileSprite( Sprite, X, Y, CellWidth, CellHeight ) )
		Else
			For Sprite = EachIn LTLayer( Shape ).Children
				Local TileSprite:LTSprite = Sprite.ShapeType.GetTileSprite( Sprite, X, Y, CellWidth, CellHeight )
				If CollidesWithSprite( TileSprite ) Then PushFromSprite( TileSprite )
			Next
		End If
	End Method
	
	
	
	Rem
	bbdoc: Forces sprite to bounce off the inner bounds of the shape.
	about: See also: #Active example
	End Rem
	Method BounceInside( Shape:LTShape, LeftSide:Int = True, TopSide:Int = True, RightSide:Int = True, BottomSide:Int = True )
		If LeftSide Then
			If LeftX() < Shape.LeftX() Then
				X = Shape.LeftX() + 0.5 * Width
				Angle = 180 - Angle
			End If
		End If
		If TopSide Then
			If TopY() < Shape.TopY() Then
				Y = Shape.TopY() + 0.5 * Height
				Angle = -Angle
			End If
		End If
		If RightSide Then
			If RightX() > Shape.RightX() Then
				X = Shape.RightX() - 0.5 * Width
				Angle = 180 - Angle
			End If
		End If
		If BottomSide Then
			If BottomY() > Shape.BottomY() Then
				Y = Shape.BottomY() - 0.5 * Height
				Angle = -Angle
			End If
		End If
	End Method

	' ==================== Position and size ====================
	
	Method SetCoords( NewX:Double, NewY:Double )
		If SpriteMap Then SpriteMap.RemoveSprite( Self, False )
		
		X = NewX
		Y = NewY
		
		Update()
		If SpriteMap Then SpriteMap.InsertSprite( Self, False )
	End Method
	
	

	Method SetCoordsAndSize( X1:Double, Y1:Double, X2:Double, Y2:Double )
		If SpriteMap Then SpriteMap.RemoveSprite( Self, False )
		
		X = 0.5:Double * ( X1 + X2 )
		Y = 0.5:Double * ( Y1 + Y2 )
		Width = X2 - X1
		Height = Y2 - Y1
		
		Update()
		If SpriteMap Then SpriteMap.InsertSprite( Self, False )
	End Method
	
	
	
	Rem
	bbdoc: Moves sprite forward.
	about: See also: #Move, #MoveBackward, #Turn example
	End Rem
	Method MoveForward()
		SetCoords( X + Cos( Angle ) * Velocity * L_DeltaTime, Y + Sin( Angle ) * Velocity * L_DeltaTime )
	End Method
	
	
	
	Rem
	bbdoc: Moves sprite backward.
	about: See also: #Move, #MoveForward, #Turn example
	End Rem
	Method MoveBackward()
		SetCoords( X - Cos( Angle ) * Velocity * L_DeltaTime, Y - Sin( Angle ) * Velocity * L_DeltaTime )
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
	
	See also: #GetTileForPoint example
	End Rem
	Method SetAsTile( TileMap:LTTileMap, TileX:Int, TileY:Int )
		Width = TileMap.GetTileWidth()
		Height = TileMap.GetTileHeight()
		X = TileMap.LeftX() + Width * ( 0.5 + TileX )
		Y = TileMap.TopY() + Height * ( 0.5 + TileY )
		Visualizer = Tilemap.Visualizer.Clone()
		Visualizer.Image = Tilemap.TileSet.Image
		Frame = TileMap.GetTile( TileX, TileY )
	End Method

	' ==================== Limiting ====================
	
	Method LimitLeftWith( Rectangle:LTShape, Handler:LTSpriteCollisionHandler = Null )
		Local RectLeftX:Double = Rectangle.LeftX()
		If LeftX() < RectLeftX Then
			SetX( RectLeftX + 0.5 * Width )
			If Handler Then Handler.HandleCollision( Self, Null )
		End If
	End Method
	
	
	
	Method LimitTopWith( Rectangle:LTShape, Handler:LTSpriteCollisionHandler = Null )
		Local RectTopY:Double = Rectangle.TopY()
		If TopY() < RectTopY Then
			SetY( RectTopY + 0.5 * Height )
			If Handler Then Handler.HandleCollision( Self, Null )
		End If
	End Method
	
	
	
	Method LimitRightWith( Rectangle:LTShape, Handler:LTSpriteCollisionHandler = Null )
		Local RectRightX:Double = Rectangle.RightX()
		If RightX() > RectRightX Then
			SetX( RectRightX - 0.5 * Width )
			If Handler Then Handler.HandleCollision( Self, Null )
		End If
	End Method
	
	
	
	Method LimitBottomWith( Rectangle:LTShape, Handler:LTSpriteCollisionHandler = Null )
		Local RectBottomY:Double = Rectangle.BottomY()
		If BottomY() > RectBottomY Then
			SetY( RectBottomY - 0.5 * Height )
			If Handler Then Handler.HandleCollision( Self, Null )
		End If
	End Method
	
	' ==================== Angle ====================
	
	Rem
	bbdoc: Directs sprite as given angular sprite. 
	about: See also: #DirectTo
	End Rem
	Method DirectAs( Sprite:LTSprite )
		Angle = Sprite.Angle
	End Method
	
	
	
	Rem
	bbdoc: Turns the sprite.
	about: Turns the sprite with given speed per second.
	End Rem
	Method Turn( TurningSpeed:Double )
		Angle :+ L_DeltaTime * TurningSpeed
	End Method
	
	
	
	Rem
	bbdoc: Direct the sprite to center of the given shape.
	about: See also: #DirectAs
	End Rem
	Method DirectTo( Shape:LTShape )
		Angle = ATan2( Shape.Y - Y, Shape.X - X )
	End Method
	
	
	
	Rem
	bbdoc: Reverses sprite direction.
	about: See also: #Turn
	End Rem
	Method ReverseDirection()
		Angle = Angle + 180
	End Method
	
	
	
	Rem
	bbdoc: Alters angle by given value.
	about: See also: #Clone example
	End Rem
	Method AlterAngle( DAngle:Double )
		Angle :+ DAngle
	End Method
	
	' ==================== Animation ====================
	
	Rem
	bbdoc: Animates the sprite.
	End Rem
	Method Animate( Speed:Double, FramesQuantity:Int = 0, FrameStart:Int = 0, StartingTime:Double = 0.0, PingPong:Int = False )
		If FramesQuantity = 0 Then FramesQuantity = Visualizer.GetImage().FramesQuantity()
		Local ModFactor:Int = FramesQuantity
		If PingPong Then ModFactor = FramesQuantity * 2 - 2
		Frame = Floor( ( L_CurrentProject.Time - StartingTime ) / Speed ) Mod ModFactor
		If PingPong And Frame >= FramesQuantity Then Frame = ModFactor - Frame
		Frame :+ FrameStart
	End Method
	
	' ==================== Methods for oval ====================	
	
	Method ToCircle:LTSprite( Pivot1:LTSprite, CircleSprite:LTSprite = Null )
		If Width = Height Then Return Self
		If Not CircleSprite Then CircleSprite = LTSprite.FromShapeType( Oval )
		If Width > Height Then
			CircleSprite.X = L_LimitDouble( Pivot1.X, X - 0.5 * ( Width - Height ), X + 0.5 * ( Width - Height ) )
			CircleSprite.Y = Y
			CircleSprite.Width = Height
			CircleSprite.Height = Height
		Else
			CircleSprite.X = X
			CircleSprite.Y = L_LimitDouble( Pivot1.Y, Y - 0.5 * ( Height - Width ), Y + 0.5 * ( Height - Width ) )
			CircleSprite.Width = Width
			CircleSprite.Height = Width
		End If
		Return CircleSprite
	End Method
	
	
	
	Method ToCircleUsingLine:LTSprite( Line:LTLine, CircleSprite:LTSprite = Null )
		If Width = Height Then Return CircleSprite
		If Not CircleSprite Then CircleSprite = LTSprite.FromShapeType( Oval )
		If Width > Height Then
			Local DWidth:Double = 0.5 * ( Width - Height )
			Local O1:Double = Line.A * ( X - DWidth ) + Line.B * Y + Line.C
			Local O2:Double = Line.A * ( X + DWidth ) + Line.B * Y + Line.C
			If Sgn( O1 ) <> Sgn( O2 ) Then
				CircleSprite.X = -( Line.B * Y + Line.C ) / Line.A
			ElseIf Abs( O1 ) < Abs( O2 ) Then
				CircleSprite.X = X - DWidth
			Else
				CircleSprite.X = X + DWidth
			End If
			CircleSprite.Y = Y
		Else
			Local DHeight:Double = 0.5 * ( Height - Width )
			Local O1:Double = Line.A * X + Line.B * ( Y - DHeight ) + Line.C
			Local O2:Double = Line.A * X + Line.B * ( Y + DHeight ) + Line.C
			If Sgn( O1 ) <> Sgn( O2 ) Then
				CircleSprite.Y = -( Line.A * X + Line.C ) / Line.B
			ElseIf Abs( O1 ) < Abs( O2 ) Then
				CircleSprite.Y = Y - DHeight
			Else
				CircleSprite.Y = Y + DHeight
			End If
			CircleSprite.X = X
		End If
		Return CircleSprite
	End Method

	' ==================== Methods for rectangle ====================	
	
	Method GetBounds( LeftX:Double Var, TopY:Double Var, RightX:Double Var, BottomY:Double Var )
		Local DWidth:Double = 0.5 * Width
		Local DHeight:Double = 0.5 * Height
		LeftX = X - DWidth
		TopY = Y - DHeight
		RightX = X + DWidth
		BottomY = Y + DHeight
	End Method
	
	' ==================== Methods for ray ====================	
	
	Method ToLine:LTLine( Line:LTLine )
		If Not Line Then Line = New LTLine
		Line.UsePoints( X, Y, X + Cos( Angle ), Y + Sin( Angle ) )
		Return Line
	End Method
	
	
	
	Method HasPoint:Int( X1:Double, Y1:Double )
		Local Ang:Double = L_WrapDouble( Angle, 360.0 )
		If Ang < 45.0 Or Ang >= 315.0 Then
			Return X1 >= X
		ElseIf Ang < 135.0 Then
			Return Y1 >= Y
		ElseIf Ang < 225.0 Then
			Return X1 <= X
		Else
			Return Y1 <= Y
		End If
	End Method
	
	
	
	Method HasPivot:Int( Pivot:LTSprite )
		Return HasPoint( Pivot.X, Pivot.Y )
	End Method
	
	' ==================== Methods for triangle ====================	
	
	Function GetMedium:LTSprite( Pivot1:LTSprite, Pivot2:LTSprite, ToPivot:LTSprite = Null )
		If Not ToPivot Then ToPivot = LTSprite.FromShape( 0, 0, 0, 0, Pivot )
		ToPivot.X = 0.5 * ( Pivot1.X + Pivot2.X )
		ToPivot.Y = 0.5 * ( Pivot1.Y + Pivot2.Y )
		Return ToPivot
	End Function
	
	
	
	Method GetHypotenuse:LTLine( Line:LTLine = Null )
		If Not Line Then Line = New LTLine
		Local ShapeTypeNum:Int = ShapeType.GetNum()
		If ShapeTypeNum = LTSprite.TopLeftTriangle.GetNum() Or ShapeTypeNum = LTSprite.BottomRightTriangle.GetNum() Then
			LTLine.FromPoints( X, Y, X - Width, Y + Height, Line )
		Else
			LTLine.FromPoints( X, Y, X + Width, Y + Height, Line )
		End If
		Return Line
	End Method
	
	
	
	Method GetRightAngleVertex:LTSprite( Vertex:LTSprite = Null )
		If Not Vertex Then Vertex = LTSprite.FromShape( 0, 0, 0, 0, Pivot )
		Local ShapeTypeNum:Int = ShapeType.GetNum()
		If ShapeTypeNum = LTSprite.TopLeftTriangle.GetNum() Or ShapeTypeNum = LTSprite.BottomLeftTriangle.GetNum() Then
			Vertex.X = X - 0.5 * Width
		Else
			Vertex.X = X + 0.5 * Width
		End If
		If ShapeTypeNum = LTSprite.TopLeftTriangle.GetNum() Or ShapeTypeNum = LTSprite.TopRightTriangle.GetNum() Then
			Vertex.Y = Y - 0.5 * Height
		Else
			Vertex.Y = Y + 0.5 * Height
		End If
		Return Vertex
	End Method
	
	
	
	Method GetOtherVertices( Pivot1:LTSprite, Pivot2:LTSprite )
		Local ShapeTypeNum:Int = ShapeType.GetNum()
		If ShapeTypeNum = LTSprite.TopRightTriangle.GetNum() Or ShapeTypeNum = LTSprite.BottomLeftTriangle.GetNum() Then
			GetBounds( Pivot1.X, Pivot1.Y, Pivot2.X, Pivot2.Y )
		Else
			GetBounds( Pivot1.X, Pivot2.Y, Pivot2.X, Pivot1.Y )
		End If
	End Method	
	
	' ==================== Cloning ===================	
	
	Method Clone:LTShape()
		Local NewSprite:LTSprite = New LTSprite
		CopySpriteTo( NewSprite )
		Return NewSprite
	End Method

	
	
	Method CopySpriteTo( Sprite:LTSprite )
		CopyShapeTo( Sprite )
		
		Sprite.ShapeType = ShapeType
		Sprite.Angle = Angle
		Sprite.DisplayingAngle = DisplayingAngle
		Sprite.Velocity = Velocity
		Sprite.Frame = Frame
		Sprite.UpdateFromAngularModel()
	End Method

	
	
	Method CopyTo( Shape:LTShape )
		Local Sprite:LTSprite = LTSprite( Shape )
		
		?debug
		If Not Sprite Then L_Error( "Trying to copy sprite ~q" + Shape.GetTitle() + "~q data to non-sprite" )
		?
		
		CopySpriteTo( Sprite )
	End Method

	' ==================== Other ====================
	
	Method UpdateFromAngularModel()
	End Method
	
	
	
	Method XMLIO( XMLObject:LTXMLObject )
		Super.XMLIO( XMLObject )
		
		If L_XMLMode = L_XMLGet Then
			If XMLObject.FieldExists( "shape-type" ) Then
				ShapeType = LTShapeType( XMLObject.ManageObjectField( "shape-type", ShapeType ) )
			Else
				ShapeType = LTShapeType.GetByNum( XMLObject.GetAttribute( "shape" ).ToInt() )
			End If
		ElseIf ShapeType.Singleton() Then
			XMLObject.SetAttribute( "shape", String( ShapeType.GetNum() ) )
		Else
			ShapeType = LTShapeType( XMLObject.ManageObjectField( "shape-type", ShapeType ) )
		End If
		
		XMLObject.ManageDoubleAttribute( "moving-angle", Angle )
		XMLObject.ManageDoubleAttribute( "disp-angle", DisplayingAngle )
		XMLObject.ManageDoubleAttribute( "velocity", Velocity, 1.0 )
		XMLObject.ManageIntAttribute( "frame", Frame )
	End Method
End Type