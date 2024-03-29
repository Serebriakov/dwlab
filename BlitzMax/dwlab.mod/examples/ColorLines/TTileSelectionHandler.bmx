'
' Color Lines - Digital Wizard's Lab example
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TTileSelectionHandler Extends LTSpriteAndTileCollisionHandler
	Method HandleCollision( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionSprite:LTSprite )
		Game.SelectedTileX = TileX
		Game.SelectedTileY = TileY
		Local TileNum:Int = TileMap.GetTile( TileX, TileY )
		Local BallNum:Int = Profile.Balls.GetTile( TileX, TileY )
		Local ModifierNum:Int = Profile.Modifiers.GetTile( TileX, TileY )
		'If KeyHit( Key_E ) Then TExplosion.Create( TileX, TileY )
		If Game.LeftMouse.WasPressed() Then
			'DebugStop
			If Game.Selected Then Game.Selected.Remove( Null )
			If TileNum = Profile.Void Then Return
			If BallNum = Profile.NoBall
				If Not Game.Selected Then Return
				If Profile.NoBallMove Then
					Profile.PlaySnd( Game.WrongTurnSound )
					Return
				End If
				TMoveAlongPath.Create( Game.PathFinder.FindPath( Game.Selected.X, Game.Selected.Y, TileX, TileY ), TileX, TileY )
			Else
				If TileNum Mod 11 = Profile.Glue Then
					Profile.PlaySnd( Game.WrongTurnSound )
					Return
				End If
				Game.Selected = TSelected.Create( TileX, TileY )
				Profile.PlaySnd( Game.SelectSound )
			End If
		ElseIf Game.RightMouse.WasPressed() 
			if BallNum = Profile.Bomb Then
				if Game.Selected Then Game.Selected.Deactivate( Game.Selected.Sprite )
				TExplosion.Create( TileX, TileY )
			ElseIf ModifierNum = Profile.AnyColor Then
				if Game.Selected Then Game.Selected.Deactivate( Game.Selected.Sprite )
				Local Project:TColorSelection = New TColorSelection
				Project.TileX = TileX
				Project.TileY = TileY
				Game.SwitchTo( Project )
			Else If Game.Selected And Profile.Swap Then
				If BallNum = Profile.NoBall Then Return
				If TileNum Mod 11 = Profile.Glue Then 
					Profile.PlaySnd( Game.WrongTurnSound )
					Return
				End If
				If Game.Selected Then Game.Selected.Remove( Null )
				If Abs( Game.Selected.X - TileX ) + Abs( Game.Selected.Y - TileY ) = 1 Then
					TMoveBall.Create( Game.Selected.X, Game.Selected.Y, TileX - Game.Selected.X, TileY - Game.Selected.Y, True )
					TMoveBall.Create( TileX, TileY, Game.Selected.X - TileX, Game.Selected.Y - TileY, True )
					Profile.Balls.SwapTiles( TileX, TileY, Game.Selected.X, Game.Selected.Y )
					Profile.Modifiers.SwapTiles( TileX, TileY, Game.Selected.X, Game.Selected.Y )
					Profile.PlaySnd( Game.SwapSound )
				Else
					Profile.PlaySnd( Game.WrongTurnSound )
				End If
			End If
		End If
	End Method
End Type



Type TColorSelection Extends LTProject
	Field TileX:Int, TileY:Int
	Field Balls:LTLayer = New LTLayer
	Field X:Double, Y:Double
	
	Method Init()
		Local BallNum:Int = Profile.Balls.GetTile( TileX, TileY )
		For Local N:Int = 1 To 7
			Local Sprite:LTSprite = LTSprite.FromShape( , , , , LTSprite.Oval )
			Sprite.SetAsTile( Profile.Balls, TileX, TileY )
			Sprite.SetDiameter( 0.0 )
			Sprite.Visualizer.DY :+ 0.1
			Sprite.Visualizer.YScale :* 0.75
			Sprite.Frame = N
			Sprite.AttachModel( LTResizingModel.Create( 1.0, 0.3 ) )
			If N <> BallNum Then
				Local Angle:Double = 60.0 * ( N - ( N > BallNum ) - 1 )
				Sprite.AttachModel( LTTimedMovementModel.Create( Sprite.X + Cos( Angle ), Sprite.Y + Sin( Angle ), 0.3 ) )
			End If
			Balls.AddLast( Sprite )
		Next
	End Method

	Method Logic()
		If Game.RightMouse.WasPressed() Or Profile.BossKey.WasPressed() Or Profile.ExitToMenu.WasPressed() Then Exiting = True
		If Game.LeftMouse.WasPressed() Then
			Local Ball:LTSprite = Balls.LayerFirstSpriteCollision( L_Cursor )
			If Ball Then
				Profile.Balls.SetTile( TileX, TileY, Ball.Frame )
				TCheckLines.Execute( Ball.Frame )
				Exiting = True
			End If
		End If
		Balls.Act()
		If Profile.ExitToMenu.WasPressed() Then Exiting = True
	End Method

	Method Render()
		Game.Render()
		Game.WindowsRender()
		L_CurrentCamera.Darken( 0.6 )
		Balls.Draw()
	End Method
	
	Method OnCloseButton()
		Exiting = True
	End Method
	
	Method OnWindowResize()
		Game.OnWindowResize()
	End Method
End Type