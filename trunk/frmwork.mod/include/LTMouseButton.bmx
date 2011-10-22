'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type LTMouseButton Extends LTPushable
	Field Num:Int
	
	
	
	Method GetName:String()
		Select Num
			Case 1
				Return "Left mouse button"
			Case 2
				Return "Right mouse button"
			Case 3
				Return "Middle mouse button"
		End Select
	End Method
	
	
	
	Method IsDown:Int()
		Return MouseDown( Num )
	End Method
	
	
	
	Method IsEqualTo:Int( Pushable:LTPushable )
		Local Button:LTMouseButton = LTMouseButton( Pushable )
		If Button Then Return Num = Button.Num
	End Method
	
	
	
	Rem
	bbdoc: Creates mouse button object.
	returns: New object of mouse button with given number.
	End Rem	
	Function Create:LTMouseButton( Num:Int )
		If Num <= 0 Or Num > 3 Then L_Error( "Invalid mouse button number" )
		
		For Local Button:LTMouseButton = Eachin L_Controllers
			If Button.Num = Num Then Return Button
		Next
		
		Local Button:LTMouseButton = New LTMouseButton
		Button.Num = Num
		L_Controllers.AddLast( Button )
		Return Button
	End Function
	
	
	
	Method XMLIO( XMLObject:LTXMLObject )
		Super.XMLIO( XMLObject )
		XMLObject.ManageIntAttribute( "num", Num )
	End Method
End Type