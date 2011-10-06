'
' Color Lines - Digital Wizard's Lab example
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TGame Extends LTGUIProject
	Const BallsPerTurn:Int = 3
	
	Const Void:Int = 0
	Const Plate:Int = 1
	
	Const NoBall:Int = 0
	
	Field World:LTWorld
	Field GameField:LTTileMap
	Field Balls:LTTileMap
	Field HUD:LTWindow
	Field Background:LTShape
	Field Objects:LTLayer = New LTLayer
	Field Particles:LTLayer = New LTLayer
	
	Field Cursor:TCursor = New TCursor
	Field Selected:TSelected
	Field EmptyCells:TList = New TList
	Field PathFinder:TPathFinder = New TPathFinder
	Field Score:Int
	Field GameOver:Int
	
	Field Font:LTBitmapFont

	Field SwapSound:TSound
	Field RushSound:TSound
	Field StopSound:TSound
	Field SelectSound:TSound
	Field ExplosionSound:TSound
	
	Method Init()
		World = LTWorld.FromFile( "levels.lw" )
	
		L_ScreenWidthGrain = 76
		L_ScreenHeightGrain = 57
		'debugstop
		Menu.ProfileTypeID = TTypeID.ForName( "TGameProfile" )
		Menu.InitSystem( Self )
		HUD = LoadWindow( World, , "THUD" )
		Background = HUD.FindShape( "Background" )
		HUD.Remove( Background )
		Menu.AddPanels()
		
		Cursor.ShapeType = LTSprite.Pivot
		Cursor.SetDiameter( 0.1 )
		'debugstop
		L_CurrentProfile.Load()
	End Method
	
	Method LoadLevel( Profile:TGameProfile )
		Local Layer:LTLayer = Null
		LoadAndInitLayer( Layer, LTLayer( World.FindShapeWithParameter( "level_num", "1" ) ) )
		Profile.GameField = LTTileMap( Layer.FindShape( "Field" ) )
		Profile.Balls = LTTileMap( Layer.FindShape( "Balls" ) )
		
		For Local N:Int = 1 To 3
			Repeat
				Local X:Int = Rand( 0, Profile.GameField.XQuantity - 1 )
				Local Y:Int = Rand( 0, Profile.GameField.YQuantity - 1 )
				If Profile.GameField.Value[ X, Y ] = Plate And Profile.Balls.Value[ X, Y ] = NoBall Then
					Profile.Balls.Value[ X, Y ] = Rand( 1, 7 )
					Exit
				End If
			Forever
		Next
		
		Locked = True
	End Method
		
	Method InitLevel()
		L_CurrentCamera.JumpTo( GameField )
		L_CurrentCamera.SetMagnification( L_CurrentCamera.Viewport.Width / L_ScreenWidthGrain * 3.0 )
		Background.JumpTo( L_CurrentCamera )
		Background.SetSize( L_CurrentCamera.Width, L_CurrentCamera.Height )
		
		PathFinder.Map = GameField
	End Method
	
	Method InitSound()
		SwapSound = TSound.Load( "incbin::swap.ogg", False )
		RushSound = TSound.Load( "incbin::rush.ogg", False )
		StopSound = TSound.Load( "incbin::stop.ogg", False )
		SelectSound = TSound.Load( "incbin::select.ogg", False )
		ExplosionSound = TSound.Load( "incbin::explosion.ogg", False )
	End Method
	
	Method Render()
		Background.Draw()
		GameField.Draw()
		Balls.Draw()
		Objects.Draw()
		Particles.Draw()
	End Method
	
	Method Logic()
		Cursor.SetMouseCoords()
		If Not Locked Then Cursor.CollisionsWithTileMap( GameField )
		Objects.Act()
		Particles.Act()
	End Method
	
	Method DeInit()
		L_CurrentProfile.Save()
		Menu.SaveToFile( "settings.xml" )
	End Method
	
	Method CreateBalls()
		RefreshEmptyCells()
		If EmptyCells.Count() < 3 Then
			Menu.LoadGameOverWindow()
			Locked = True
			Return
		End If
		For Local N:Int = 0 Until BallsPerTurn
			Local Cell:TCell = TCell.PopFrom( EmptyCells )
			TPopUpBall.Create( Cell.X, Cell.Y, Rand( 1, 7 ) )
		Next
	End Method
	
	Method RefreshEmptyCells()
		EmptyCells.Clear()
		For Local Y:Int = 0 Until GameField.YQuantity
			For Local X:Int = 0 Until GameField.XQuantity
				If GameField.Value[ X, Y ] = Plate And Balls.Value[ X, Y ] = NoBall Then
					EmptyCells.AddLast( TCell.Create( X, Y ) )
				End If
			Next
		Next
	End Method
	
	Method TileToSprite:LTSprite( Model:LTBehaviorModel, X:Int, Y:Int )
		Local Sprite:LTSprite = New LTSprite
		Sprite.SetAsTile( Game.Balls, X, Y )
		Game.Objects.AddLast( Sprite )
		Sprite.AttachModel( Model )
		Game.Balls.SetTile( X, Y, NoBall )
		Return Sprite
	End Method
End Type



Type TCell
	Field X:Int, Y:Int
	
	Function Create:TCell( X:Int, Y:Int )
		Local Cell:TCell = New TCell
		Cell.X = X
		Cell.Y = Y
		Return Cell
	End Function
	
	Function PopFrom:TCell( List:TList )
		Local Num:Int = Rand( 0, List.Count() - 1 )
		Local Link:TLink = List.FirstLink()
		For Local N:Int = 1 To Num
			Link = Link.NextLink()
		Next
		Local Cell:TCell = TCell( Link.Value() )
		Link.Remove()
		Return Cell
	End Function
End Type