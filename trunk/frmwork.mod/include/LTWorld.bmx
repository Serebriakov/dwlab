'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Rem
bbdoc: World is the root layer which can be created in the editor and loaded from file.
about: 
End Rem
Type LTWorld Extends LTLayer
	Field Images:TList = New TList
	Field Tilesets:TList = New TList
	Field Camera:LTCamera
	Field IncbinValue:Int
	
	
	
	Rem
	bbdoc: Loads a world from file.
	returns: Loaded world.
	End Rem
	Function FromFile:LTWorld( Filename:String )
		Return LTWorld( LoadFromFile( Filename ) )
	End Function
	
	
	
	Method XMLIO( XMLObject:LTXMLObject )
		Super.XMLIO( XMLObject )
		
		XMLObject.ManageIntAttribute( "incbin", IncbinValue )
		XMLObject.ManageListField( "images", Images )
		XMLObject.ManageListField( "tilesets", Tilesets )
		Camera = LTCamera( XMLObject.ManageObjectField( "camera", Camera ) )
	End Method
End Type