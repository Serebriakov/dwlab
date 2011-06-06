'
' Super Mario Bros - Digital Wizard's Lab example
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TBlock Extends LTVectorSprite
	Const Acceleration:Float = 5.0

	Field LowestY:Float
	Field TileX:Int, TileY:Int, TileNum:Int
	
	
	
	Function FromTile( TileX:Int, TileY:Int, TileNum:Int )
		Local Block:TBlock = New TBlock
		Block.SetAsTile( Game.TileMap, TileX, TileY )
		Game.TileMap.SetTile( TileX, TileY, 63 )
		Block.TileX = TileX
		Block.TileY = TileY
		Block.LowestY = Block.Y
		Block.DY = -1.0
		Block.Frame = TTiles.SolidBlock
		Select TileNum
			Case 9
				TCoin.FromTile( TileX, TileY )
			Case 10, 27
				Block.Frame = TileNum 
		End Select

		Game.Bump.Play()
		Game.Level.AddLast( Block )
	End Function
	
	
	
	Method Act()
		DY :+ Game.PerSecond( Acceleration )
		If Y >= LowestY And DY > 0 Then
			Game.Tilemap.SetTile( TileX, TileY, Frame )
			Game.Level.Remove( Self )
		Else
			MoveForward()
		End If
	End Method
End Type