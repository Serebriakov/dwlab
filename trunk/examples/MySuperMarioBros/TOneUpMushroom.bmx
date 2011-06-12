'
' Super Mario Bros - Digital Wizard's Lab example
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TOneUpMushroom Extends TBonus
	Const Speed:Float = 2.0
	
	
	
	Function FromTile( TileX:Int, TileY:Int )
		Local Mushroom:TOneUpMushroom = New TOneUpMushroom
		Mushroom.SetAsTile( Game.TileMap, TileX, TileY )
		Mushroom.Visualizer = Game.OneUpMushroom
		Mushroom.DX = Speed
		Mushroom.AttachModel( New TAppearing )
	End Function
	
	

	Method Collect()
		TScore.FromSprite( Self, TScore.s1up )
		Game.OneUp.Play()
		Game.Lives :+ 1
	End Method
End Type