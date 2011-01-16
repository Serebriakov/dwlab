'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type LTPage Extends LTObject
	Field TileMap:LTTileMap
	Field Sprites:LTList = New LTList
	
	
	
	Method Draw()
		If Tilemap Then Tilemap.Draw()
		Sprites.Draw()
	End Method
	
	
	
	Method XMLIO( XMLObject:LTXMLObject )
		Super.XMLIO( XMLObject )
		
		TileMap = LTTileMap( XMLObject.ManageObjectField( "tilemap", TileMap ) )
		XMLObject.ManageChildList( Sprites.Children )
	End Method
End Type