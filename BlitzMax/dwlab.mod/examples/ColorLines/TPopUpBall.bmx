'
' Color Lines - Digital Wizard's Lab example
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TPopUpBall Extends LTBehaviorModel
	Const Period:Double = 0.15
	Const StartingAngle:Double = 45
	Const EndingAngle:Double = 120
	
	Field X:Int, Y:Int
	Field StartingTime:Double
	
	Function Create:TPopUpBall( X:Int, Y:Int, TileNum:Int )
		Local Model:TPopUpBall = New TPopUpBall
		Model.X = X
		Model.Y = Y
		Model.StartingTime = Game.Time
		
		Local Sprite:LTSprite = New LTSprite
		Sprite.SetAsTile( Profile.Balls, X, Y )
		Sprite.Visualizer.SetVisualizerScales( 0.0 )
		Sprite.Frame = TileNum
		Sprite.AttachModel( Model )
		
		Game.Objects.AddLast( Sprite )
		Profile.Balls.SetTile( X, Y, Sprite.Frame )
		Game.HiddenBalls[ X, Y ] = True
		Game.Locked = True
	End Function
	
	Method ApplyTo( Shape:LTShape )
		Local Angle:Double = StartingAngle + ( Game.Time - StartingTime ) * ( EndingAngle - StartingAngle ) / Period
		Local Scale:Double = Sin( Angle ) / Sin( EndingAngle )
		Shape.Visualizer.SetVisualizerScale( Scale, 1.0 * Scale )
		If Game.Time > StartingTime + Period Then Remove( Shape )
	End Method
	
	Method Deactivate( Shape:LTShape )
		Game.HiddenBalls[ X, Y ] = False
		Game.Objects.Remove( Shape )
		Game.Locked = False
		TCheckLines.Execute( LTSprite( Shape ).Frame )
	End Method
End Type