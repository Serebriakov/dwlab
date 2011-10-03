'
' Mouse-oriented game menu - Digital Wizard's Lab framework template
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type LTSelectProfileWindow Extends LTWindow
	Field RemoveProfileButton:LTButton
	
	Method Init()
		RemoveProfileButton = LTButton( FindShapeWithParameter( "LTButton", "text", "RemoveProfile" ) )
		Menu.SelectedProfile = L_CurrentProfile
		Super.Init()
	End Method
	
	Method Act()
		If Menu.Profiles.Count() >= 2 Then RemoveProfileButton.Activate() Else RemoveProfileButton.Deactivate()
		Super.Act()
	End Method
	
	Method Save()
		L_CurrentProfile = Menu.SelectedProfile
	End Method
End Type