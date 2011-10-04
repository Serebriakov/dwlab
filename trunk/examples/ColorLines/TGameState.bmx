'
' Color Lines - Digital Wizard's Lab example
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TGameState Extends LTObject
	Method XMLIO( XMLObject:LTXMLObject )
		Game.Balls = LTTileMap( XMLObject.ManageObjectField( "balls", Game.Balls ) )
		XMLObject.ManageIntAttribute( "score", Game.Score )
	End Method
End Type