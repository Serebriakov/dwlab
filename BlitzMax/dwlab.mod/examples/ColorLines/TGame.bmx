'
' Color Lines - Digital Wizard's Lab example
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Global Profile:TGameProfile
Global GameCamera:LTCamera = L_CurrentCamera

Type TGame Extends LTGUIProject
	Field Interface:LTWorld
	Const Inc:String = ""
	
	Field HUD:LTWindow
	Field Background:TImage
	Field Objects:LTLayer = New LTLayer
	Field Particles:LTLayer = New LTLayer
	
	Field SelectedTileX:Int = -1
	Field SelectedTileY:Int
	Field Selected:TSelected
	Field GameOver:Int
	Field TotalBalls:Int
	Field LevelTime:Int
	
	Field TileIsPassable:Int[] = [ 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, ..
											0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, ..
											1 ]
	
	Field EmptyCells:TList = New TList
	Field PathFinder:TPathFinder = New TPathFinder
	Field HiddenBalls:Int[,]
	
	Field Font:LTBitmapFont
	Field TileSelectionHandler:TTileSelectionHandler = New TTileSelectionHandler

	Field LeftMouse:LTButtonAction = LTButtonAction.Create( LTMouseButton.Create( 1 ), "Click" )
	Field RightMouse:LTButtonAction = LTButtonAction.Create( LTMouseButton.Create( 2 ), "Swap" )
	
	Field SwapSound:TSound = LoadSound( Inc + "sound\swap.ogg" )
	Field RushSound:TSound = LoadSound( Inc + "sound\rush.ogg" )
	Field StopSound:TSound = LoadSound( Inc + "sound\stop.ogg" )
	Field SelectSound:TSound = LoadSound( Inc + "sound\select.ogg" )
	Field ExplosionSound:TSound = LoadSound( Inc + "sound\explosion.ogg" )
	Field WrongTurnSound:TSound = LoadSound( Inc + "sound\wrong_turn.ogg" )
	
	Field ExitWindow:Int
	
	Method Init()
		SetGraphicsDriver( D3D7Max2DDriver() )
		Interface = LTWorld.FromFile( "interface.lw" )
		Menu.Levels = LTWorld.FromFile( "levels.lw" )

		Menu.ProfileTypeID = TTypeID.ForName( "TGameProfile" )
		Menu.InitSystem( Self )
		HUD = LoadWindow( Interface, "THUD" )
		Menu.AddPanels()
	
		Profile.PlayList = New String[ 3 ]
		L_Music.AllEntries.Clear()
		For Local N:Int = 3 To 5
			L_Music.Preload( "music\level_" + N + ".ogg", N )
			Profile.PlayList[ N - 3 ] = N
		Next
		L_Music.Add( "3" )
		L_Music.Start()
		
		Profile = TGameProfile( L_CurrentProfile )
		Profile.Load()
		
		L_LoadingUpdater = Null
		If FileType( "stats.xml" ) = 1 Then TStatList.Instance = TStatList( LoadFromFile( "stats.xml", False ) )
		
		'If L_CurrentProfile.MusicQuantity > 0 Then L_CurrentProfile.MusicMode = L_CurrentProfile.Normal
		'L_CurrentProfile.StartMusic()
	End Method
	
	Method InitGraphics()
		If Profile Then Profile.InitGraphics()
	End Method
	
	Method Logic()
		Delay 5
		L_CurrentProfile.ManageSounds()
	
		If Not Locked Then
			Profile.LevelTime :+ 1.0 / L_LogicFPS
			Profile.TurnTime :+ 1.0 / L_LogicFPS
			If Not Profile.GameField Then
				Menu.LoadFirstLevel()
			Else
				Game.SelectedTileX = -1
				L_Cursor.CollisionsWithTileMap( Profile.GameField, TileSelectionHandler )
				If Profile.Goals.IsEmpty() Then
					LoadWindow( Menu.Interface, "LTLevelCompletedWindow" )
				End If
			End If
			If ( Profile.TotalLevelTime > 0 And Profile.LevelTime > Profile.TotalLevelTime ) Or ..
				( Profile.TotalTurns > 0 And Profile.Turns > Profile.TotalTurns ) Then
					LoadWindow( Menu.Interface, "LTLevelFailedWindow" )
			Else If Profile.TotalTurnTime > 0 And Profile.TurnTime > Profile.TotalTurnTime Then
				Profile.NewTurn()
			End If
			If Profile.SkipTurn.WasPressed() Then Profile.NewTurn()
			If KeyHit( KEY_F1 ) Then LoadWindow( Menu.Interface, "LTRules" )
		End If
		
		LevelTime = MilliSecs()
		
		FindWindow( "THUD" ).Active = Not Locked
		Local MenuWindow:LTMenuWindow = LTMenuWindow( FindWindow( "LTMenuWindow" ) )
		If Profile.ExitToMenu.WasPressed() And MenuWindow.Active Then MenuWindow.Switch()
		
		Objects.Act()
		Particles.Act()
		
		For Local Goal:TGoal = Eachin Profile.Goals
			If Goal.Count <= 0 Then Profile.Goals.Remove( Goal )
		Next
	End Method
	
	Method OnCloseButton()
		If Not ExitWindow Then LoadWindow( Menu.Interface, "LTExitWindow" )
		ExitWindow = True
	End Method
	
	Method OnWindowResize()
		Profile.Apply( [ LTGUIProject( Self ), LTGUIProject( Menu ) ], True, False )
	End Method
	
	Method Render()
		If Not Background Then
			Background = LoadImage( "images\background.jpg" )
			MidHandleImage( Background )
		End If
		
		Local Scale:Double = Max( 1.0 * GameCamera.Viewport.Width / ImageWidth( Background ), ..
				1.0 * GameCamera.Viewport.Height / ImageHeight( Background ) )
		SetScale Scale, Scale
		DrawImage( Background, GameCamera.Viewport.X, GameCamera.Viewport.Y )
		SetScale 1.0, 1.0
		
		If Profile.GameField Then
			Profile.SetFieldMagnification()
			Profile.GameField.Draw()
		End If
		Particles.Draw()
	End Method
	
	Method DeInit()
		Menu.SaveToFile( "settings.xml" )
	End Method
	
	Method CheckBall:Int( Shape:LTShape, X:Int, Y:Int )
		Game.HiddenBalls[ X, Y ] = False
		Game.Selected = Null
		Game.Locked = False
		If LTSprite( Shape ).Frame = Profile.BlackBall And Profile.GameField.GetTile( X, Y ) = Profile.ClosedPocket Then
			Shape.AttachModel( TFallIntoPocket.Create( X, Y ) )
		Else
			Game.Objects.Remove( Shape )
			Return True
		End If
	End Method
End Type