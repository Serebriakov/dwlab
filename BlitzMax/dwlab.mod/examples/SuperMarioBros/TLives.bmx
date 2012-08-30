'
' Super Mario Bros - Digital Wizard's Lab example
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TLives Extends LTProject
	Method Init()
		Mario.JumpTo( Game.HUD.FindShapeWithType( "TBackground" ) )
		Mario.AlterCoords( -0.5, 0.0 )
	End Method
	
	
	
	Method Logic()
		If GetChar() Then Exiting = True
	End Method
	
	
	
	Method Render()
		L_CurrentCamera = Game.HUDCamera
		Game.HUD.Draw()
		Mario.Draw()
		
		Local X:Double = Mario.RightX()
		Local Y:Double = Mario.Y
		Game.Font.Print( "X" + Game.Lives, X, Y, 0.5, LTAlign.ToLeft, LTAlign.ToCenter )		
		Game.Font.Print( "Press left and right arrows to walk", X - 10.0, Y + 5.5, 0.5 )
		Game.Font.Print( "Press down arrow to sit while big", X - 10.0, Y + 6.0, 0.5 )
		Game.Font.Print( "Press ~qA~q key to jump", X - 10.0, Y + 6.5, 0.5 )
		Game.Font.Print( "Press ~qS~q key to run and fire", X - 10.0, Y + 7.0, 0.5 )
		L_CurrentCamera = Game.LevelCamera
	End Method
End Type