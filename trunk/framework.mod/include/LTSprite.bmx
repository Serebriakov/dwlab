'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Include "LTCamera.bmx"
Include "Collisions.bmx"
Include "Physics.bmx"

Const L_Pivot:Int = 0
Const L_Circle:Int = 1
Const L_Rectangle:Int = 2

Const L_Left:Int = 0
Const L_Right:Int = 1
Const L_Up:Int = 2
Const L_Down:Int = 3

Type LTSprite Extends LTShape
	Field ShapeType:Int = L_Rectangle
	Field Angle:Float = 0.0
	Field Velocity:Float = 0.0
	Field Frame:Int
	Field CollisionMap:LTCollisionMap
	
	' ==================== Drawing ===================	
	
	Method Draw()
		If Visible Then Visualizer.DrawUsingSprite( Self )
	End Method
	
	
	
	Method DrawUsingVisualizer( Vis:LTVisualizer )
		If Visible Then Vis.DrawUsingSprite( Self )
	End Method
	
	' ==================== Collisions ===================
	
	Method CollidesWith:Int( Shape:LTShape )
		Return Shape.CollidesWithSprite( Self )
	End Method
	
	
	
	Method CollidesWithSprite:Int( Sprite:LTSprite )
		Select ShapeType
			Case L_Pivot
				Select Sprite.ShapeType
					Case L_Pivot
						Return L_PivotWithPivot( X, Y, Sprite.X, Sprite.Y )
					Case L_Circle
						Return L_PivotWithCircle( X, Y, Sprite.X, Sprite.Y, Sprite.Width )
					Case L_Rectangle
						Return L_PivotWithRectangle( X, Y, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
			Case L_Circle
				Select Sprite.ShapeType
					Case L_Pivot
						Return L_PivotWithCircle( Sprite.X, Sprite.Y, X, Y, Width )
					Case L_Circle
						Return L_CircleWithCircle( X, Y, Width, Sprite.X, Sprite.Y, Sprite.Width )
					Case L_Rectangle
						Return L_CircleWithRectangle( X, Y, Width, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
			Case L_Rectangle
				Select Sprite.ShapeType
					Case L_Pivot
						Return L_PivotWithRectangle( Sprite.X, Sprite.Y, X, Y, Width, Height )
					Case L_Circle
						Return L_CircleWithRectangle( Sprite.X, Sprite.Y, Sprite.Width, X, Y, Width, Height )
					Case L_Rectangle
						Return L_RectangleWithRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
		End Select
	End Method
	
	
	
	Method CollidesWithLine:Int( Line:LTLine )
		Select ShapeType
			Case L_Pivot
				'Return L_PivotWithLine( Self, Line )
			Case L_Circle
				Return L_CircleWithLine( X, Y, Width, Line.Pivot[ 0 ].X, Line.Pivot[ 0 ].Y, Line.Pivot[ 1 ].X, Line.Pivot[ 1 ].Y )
			Case L_Rectangle
				'Return L_RectangleWithLine( Self, Line )
		End Select
	End Method
	
	
	
	Method TileCollidesWithSprite:Int( Sprite:LTSprite, DX:Float, DY:Float, XScale:Float, YScale:Float )
		Select Sprite.ShapeType
			Case L_Pivot
				Select ShapeType
					Case L_Pivot
						Return L_PivotWithPivot( Sprite.X, Sprite.Y, X * XScale + DX, Y * YScale + DY )
					Case L_Circle
						Return L_PivotWithCircle( Sprite.X, Sprite.Y, X * XScale + DX, Y * YScale + DY, Width * XScale )
					Case L_Rectangle
						Return L_PivotWithRectangle( Sprite.X, Sprite.Y, X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale )
				End Select
			Case L_Circle
				Select ShapeType
					Case L_Pivot
						Return L_PivotWithCircle( X * XScale + DX, Y * YScale + DY, Sprite.X, Sprite.Y, Sprite.Width )
					Case L_Circle
						Return L_CircleWithCircle( Sprite.X, Sprite.Y, Sprite.Width, X * XScale + DX, Y * YScale + DY, Width * XScale )
					Case L_Rectangle
						Return L_CircleWithRectangle( Sprite.X, Sprite.Y, Sprite.Width, X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale )
				End Select
			Case L_Rectangle
				Select ShapeType
					Case L_Pivot
						Return L_PivotWithRectangle( X * XScale + DX, Y * YScale + DY, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case L_Circle
						Return L_CircleWithRectangle( X * XScale + DX, Y * YScale + DY, Width * XScale, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
					Case L_Rectangle
						Return L_RectangleWithRectangle( Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, X * XScale + DX, Y * YScale + DY, Width * XScale, Height * YScale )
				End Select
		End Select
	End Method
	
	
	
	Method Overlaps:Int( Sprite:LTSprite )
		Select ShapeType
			Case L_Pivot
				 L_Error( "Pivot overlapping is not supported" )
			Case L_Circle
				Select Sprite.ShapeType
					Case L_Pivot
						Return L_CircleOverlapsCircle( X, Y, Width, Sprite.X, Sprite.Y, 0 )
					Case L_Circle
						Return L_CircleOverlapsCircle( X, Y, Width, Sprite.X, Sprite.Y, Sprite.Width )
					Case L_Rectangle
						Return L_RectangleOverlapsRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
			Case L_Rectangle
				Select Sprite.ShapeType
					Case L_Pivot
						Return L_RectangleOverlapsRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, 0, 0 )
					Case L_Circle
						Return L_RectangleOverlapsRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Width )
					Case L_Rectangle
						Return L_RectangleOverlapsRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height )
				End Select
		End Select
	End Method
	
	
	
	Method CollisionsWith( Shape:LTShape )
		Shape.CollisionsWithSprite( Self )
	End Method
	
	
	
	Method CollisionsWithSprite( Sprite:LTSprite )
		If CollidesWithSprite( Sprite ) Then Sprite.HandleCollisionWith( Self, GetCollisionType( Sprite ) )
	End Method
	
	' ==================== Wedging off ====================
	
	Method WedgeOffWith( Shape:LTShape, SelfMass:Float, ShapeMass:Float )
		Shape.WedgeOffWithSprite( Self, ShapeMass, SelfMass )
	End Method


	
	Method WedgeOffWithSprite( Sprite:LTSprite, SelfMass:Float, SpriteMass:Float )
		Local DX:Float, DY:Float
		Select ShapeType
			Case L_Pivot
				Select Sprite.ShapeType
					Case L_Pivot
						Return
					Case L_Circle
						L_WedgingValuesOfCircleAndCircle( X, Y, 0, Sprite.X, Sprite.Y, Sprite.Width, DX, DY )
					Case L_Rectangle
						L_WedgingValuesOfRectangleAndRectangle( X, Y, 0, 0, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, DX, DY )
				End Select
			Case L_Circle
				Select Sprite.ShapeType
					Case L_Pivot
						L_WedgingValuesOfCircleAndCircle( X, Y, Width, Sprite.X, Sprite.Y, 0, DX, DY )
					Case L_Circle
						L_WedgingValuesOfCircleAndCircle( X, Y, Width, Sprite.X, Sprite.Y, Sprite.Width, DX, DY )
					Case L_Rectangle
						L_WedgingValuesOfCircleAndRectangle( X, Y, Width, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, DX, DY )
				End Select
			Case L_Rectangle
				Select Sprite.ShapeType
					Case L_Pivot
						L_WedgingValuesOfRectangleAndRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, 0, 0, DX, DY )
					Case L_Circle
						L_WedgingValuesOfCircleAndRectangle( Sprite.X, Sprite.Y, Sprite.Width, X, Y, Width, Height, DX, DY )
						L_Separate( Sprite, Self, DX, DY, SpriteMass, SelfMass )
						Return
					Case L_Rectangle
						L_WedgingValuesOfRectangleAndRectangle( X, Y, Width, Height, Sprite.X, Sprite.Y, Sprite.Width, Sprite.Height, DX, DY )
				End Select
		End Select
		L_Separate( Self, Sprite, DX, DY, SelfMass, SpriteMass )
	End Method
	
	
	
	Method PushFromTile( TileMap:LTTileMap, TileX:Int, TileY:Int )
		Local TileShape:LTShape = TileMap.GetTileTemplate( TileX, TileY )
		Local TileSprite:LTSprite = LTSprite( TileShape )
		Local CellWidth:Float = TileMap.GetCellWidth()
		Local CellHeight:Float = TileMap.GetCellHeight()
		If TileSprite Then
			PushFromTileSprite( TileSprite, TileMap.CornerX() + CellWidth * TileX, TileMap.CornerY() + CellHeight * TileY, CellWidth, CellHeight )
		Else
			Local TileGroup:LTGroup = LTGroup( TileShape )
			If TileGroup Then PushFromTileGroup( TileGroup, TileMap.CornerX() + CellWidth * TileX, TileMap.CornerY() + CellHeight * TileY, CellWidth, CellHeight )
		End If
	End Method
	
	
	
	Method PushFromTileGroup( TileGroup:LTGroup, DX:Float, DY:Float, XScale:Float, YScale:Float )
		For Local Shape:LTShape = Eachin TileGroup
			Local Sprite:LTSprite = LTSprite( Shape )
			If Sprite Then
				PushFromTileSprite( Sprite, DX, DY, XScale, YScale )
			Else
				Local ChildGroup:LTGroup = LTGroup( Shape )
				If ChildGroup Then PushFromTileGroup( ChildGroup, DX, DY, XScale, YScale )
			End If
		Next
	End Method


	
	Method PushFromTileSprite( TileSprite:LTSprite, DX:Float, DY:Float, XScale:Float, YScale:Float )
		Local PushingDX:Float, PushingDY:Float
		Select ShapeType
			Case L_Pivot
				Select TileSprite.ShapeType
					Case L_Pivot
						Return
					Case L_Circle
						L_WedgingValuesOfCircleAndCircle( X, Y, 0, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, PushingDX, PushingDY )
					Case L_Rectangle
						L_WedgingValuesOfRectangleAndRectangle( X, Y, 0, 0, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, PushingDX, PushingDY )
				End Select
			Case L_Circle
				Select TileSprite.ShapeType
					Case L_Pivot
						L_WedgingValuesOfCircleAndCircle( X, Y, Width, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, 0, PushingDX, PushingDY )
					Case L_Circle
						L_WedgingValuesOfCircleAndCircle( X, Y, Width, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, PushingDX, PushingDY )
					Case L_Rectangle
						L_WedgingValuesOfCircleAndRectangle( X, Y, Width, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, PushingDX, PushingDY )
				End Select
			Case L_Rectangle
				Select TileSprite.ShapeType
					Case L_Pivot
						L_WedgingValuesOfRectangleAndRectangle( X, Y, Width, Height, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, 0, 0, PushingDX, PushingDY )
					Case L_Circle
						L_WedgingValuesOfCircleAndRectangle( TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, X, Y, Width, Height, PushingDX, PushingDY )
						L_Separate( TileSprite, Self, PushingDX, PushingDY, 1.0, 0.0 )
						Return
					Case L_Rectangle
						L_WedgingValuesOfRectangleAndRectangle( X, Y, Width, Height, TileSprite.X * XScale + DX, TileSprite.Y * YScale + DY, TileSprite.Width * XScale, TileSprite.Height * YScale, PushingDX, PushingDY )
				End Select
		End Select
		L_Separate( Self, TileSprite, PushingDX, PushingDY, 0.0, 1.0 )
	End Method

	' ==================== Position and size ====================
	
	Method SetCoords( NewX:Float, NewY:Float )
		If CollisionMap Then CollisionMap.RemoveSprite( Self )
		
		X = NewX
		Y = NewY
		
		Update()
		If CollisionMap Then CollisionMap.InsertSprite( Self )
	End Method
	
	
	
	Method MoveTowardsShape( Shape:LTShape )
		Local Angle:Float = DirectionToShape( Shape )
		Local DX:Float = Cos( Angle ) * Velocity * L_DeltaTime
		Local DY:Float = Sin( Angle ) * Velocity * L_DeltaTime
		If Abs( DX ) >= Abs( X - Shape.X ) And Abs( DY ) >= Abs( Y - Shape.Y ) Then
			SetCoords( Shape.X, Shape.Y )
		Else
			SetCoords( X + DX, Y + DY )
		End If
	End Method
	
	
	
	Method MoveForward()
		SetCoords( X + GetDX(), Y + GetDY() )
	End Method
	
	
	
	Method MoveUsingWSAD()
		MoveUsingKeys( Key_W, Key_S, Key_A, Key_D )
	End Method
	
	
	
	Method MoveUsingArrows()
		MoveUsingKeys( Key_Up, Key_Down, Key_Left, Key_Right )
	End Method
	
	
	
	Method MoveUsingKeys( KUp:Int, KDown:Int, KLeft:Int, KRight:Int )
		Local DX:Float = KeyDown( KRight ) - KeyDown( KLeft )
		Local DY:Float = KeyDown( KDown ) - KeyDown( KUp )
		If DX * DY Then
			DX :/ Sqr( 2 )
			DY :/ Sqr( 2 )
		End If
		SetCoords( X + DX * Velocity * L_DeltaTime, Y + DY * Velocity * L_DeltaTime )
	End Method
	
	
	
	Method SetCoordsRelativeToSprite( Sprite:LTSprite, NewX:Float, NewY:Float )
		Local Angle:Float = DirectionToPoint( NewX, NewY ) + Sprite.GetAngle()
		Local Radius:Float = Sqr( NewX * NewX + NewY * NewY )
		SetCoords( Sprite.X + Radius * Cos( Angle ), Sprite.Y + Radius * Sin( Angle ) )
	End Method
	
	
	
	Method SetSize( NewWidth:Float, NewHeight:Float )
		If CollisionMap Then CollisionMap.RemoveSprite( Self )
		
		Width = NewWidth
		Height = NewHeight

		Update()
		If CollisionMap Then CollisionMap.InsertSprite( Self )
	End Method

	' ==================== Angle ====================
	
	Method GetAngle:Float()
		Return Angle
	End Method
	
	
	
	Method SetAngle:Float( NewAngle:Float )
		Angle = NewAngle
	End Method
	
	
	
	Method DirectAsSprite( Sprite:LTSprite )
		Angle = Sprite.Angle
	End Method
	
	
	
	Method Turn( TurningSpeed:Float )
		Angle :+ L_DeltaTime * TurningSpeed
	End Method
	
	
	
	Method DirectToSprite( Sprite:LTSprite )
		Angle = ATan2( Sprite.Y - Y, Sprite.X - X )
	End Method
	
	' ==================== Moving vector ====================
	
	Method GetDX:Float()
		Return Velocity * Cos( Angle )
	End Method
	
	
	
	Method AlterDX( DDX:Float )
		SetDX( GetDX() + DDX )
	End Method
	
	
	
	Method SetDX( NewDX:Float )
		Local DY:Float = GetDY()
		Angle = ATan2( DY, NewDX )
		Velocity = Sqr( NewDX * NewDX + DY * DY )
	End Method
	
	
	
	Method GetDY:Float()
		Return Velocity * Sin( Angle )
	End Method
	
	
	
	Method AlterDY( DDY:Float )
		SetDY( GetDY() + DDY )
	End Method
	
	
	
	Method SetDY( NewDY:Float )
		Local DX:Float = GetDX()
		Angle = ATan2( NewDY, DX )
		Velocity = Sqr( DX * DX + NewDY * NewDY )
	End Method
	
	
	
	Method AlterDXDY( DDX:Float, DDY:Float )
		Local DX:Float = GetDX() + DDX
		Local DY:Float = GetDY() + DDY
		Angle = ATan2( DY, DX )
		Velocity = Sqr( DX * DX + DY * DY )
	End Method
	
	
	
	Method SetDXDY( NewDX:Float, NewDY:Float )
		Angle = ATan2( NewDY, NewDX )
		Velocity = Sqr( NewDX * NewDX + NewDY * NewDY )
	End Method
	
	' ==================== Velocity ====================
	
	Method GetVelocity:Float()
		Return Velocity
	End Method
	
	
	
	Method SetVelocity:Float( NewVelocity:Float )
		Velocity = NewVelocity
	End Method
	
	' ==================== Animation ====================
	
	Method Animate( Project:LTProject, Speed:Float, FramesQuantity:Int, FrameStart:Int = 0, Time:Float = 0.0 )
		Frame = ( Floor( ( Project.Time - Time ) / Speed ) Mod FramesQuantity ) + FrameStart
	End Method
	
	' ==================== Other ====================
	
	Method Clone:LTShape()
		Local NewSprite:LTSprite = New LTSprite
		CopySpriteTo( NewSprite )
		Return NewSprite
	End Method
	
	
	
	Method CopySpriteTo( Sprite:LTSprite )
		CopyShapeTo( Sprite )
		Sprite.ShapeType = ShapeType
		Sprite.Angle = Angle
		Sprite.Velocity = Velocity
		Sprite.Frame = Frame
	End Method

	
	
	Method XMLIO( XMLObject:LTXMLObject )
		Super.XMLIO( XMLObject )
		
		XMLObject.ManageIntAttribute( "shape", ShapeType )
		XMLObject.ManageFloatAttribute( "angle", Angle )
		XMLObject.ManageFloatAttribute( "velocity", Velocity )
		XMLObject.ManageIntAttribute( "frame", Frame )
	End Method
End Type