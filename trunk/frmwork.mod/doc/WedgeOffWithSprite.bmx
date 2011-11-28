SuperStrict

Framework brl.basic
Import dwlab.frmwork
Import brl.pngloader

Global Example:TExample = New TExample
Example.Execute()

Type TExample Extends LTProject
	Const KoloboksQuantity:Int = 50
	
	Field Koloboks:LTLayer = New LTLayer
	Field KolobokImage:LTImage = LTImage.FromFile( "kolobok.png" )
	Field Player:TKolobok
	Field DebugMode:Int
	
	Method Init()
		For Local N:Int = 1 To KoloboksQuantity
			TKolobok.CreateKolobok()
		Next
		Player = TKolobok.CreatePlayer()
		L_InitGraphics()
	End Method
	
	Method Logic()
		Koloboks.Act()
		
		If KeyDown( Key_Left ) Then Player.Turn( -Player.TurningSpeed )
		If KeyDown( Key_Right ) Then Player.Turn( Player.TurningSpeed )
		If KeyDown( Key_Up ) Then Player.MoveForward()
		If KeyDown( Key_Down ) Then Player.MoveBackward()
		
		If KeyHit( Key_D ) Then DebugMode = Not DebugMode
		
		If AppTerminate() Or KeyHit( Key_Escape ) Then Exiting = True
	End Method

	Method Render()
		if DebugMode Then
			Koloboks.DrawUsingVisualizer( L_DebugVisualizer )
			ShowDebugInfo()
		Else
			Koloboks.Draw()
			DrawText( "Move white kolobok with arrow keys and push other koloboks and press D for debug mode", 0, 0 )
		End If
	End Method
End Type



Type TKolobok Extends LTSprite
	Const TurningSpeed:Double = 180.0
	
	Function CreatePlayer:TKolobok()
		Local Player:TKolobok = Create()
		Player.SetDiameter( 2 )
		Player.Velocity = 7
		Return Player
	End Function
	
	Function CreateKolobok:TKolobok()
		Local Kolobok:TKolobok = Create()
		Kolobok.SetCoords( Rnd( -15, 15 ), Rnd( -11, 11 ) )
		Kolobok.SetDiameter( Rnd( 1, 3 ) )
		Kolobok.Angle = Rnd( 360 )
		Kolobok.Visualizer.SetRandomColor()
		Return Kolobok
	End Function
	
	Function Create:TKolobok()
		Local Kolobok:TKolobok = New TKolobok
		Kolobok.ShapeType = LTSprite.Oval
		Kolobok.Visualizer.Image = Example.KolobokImage
		Kolobok.Visualizer.SetVisualizerScale( 1.3, 1.3 )
		Example.Koloboks.AddLast( Kolobok )
		Return Kolobok
	End Function
	
	Method Act()
		CollisionsWithGroup( Example.Koloboks )
	End Method
	
	Method HandleCollisionWithSprite( Sprite:LTSprite, CollisionType:Int = 0 )
		WedgeOffWithSprite( Sprite, Width ^ 2, Sprite.Width ^ 2 )
	End Method
End Type