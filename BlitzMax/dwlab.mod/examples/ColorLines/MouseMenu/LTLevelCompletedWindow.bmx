'
' Mouse-oriented game menu - Digital Wizard's Lab framework template
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type LTLevelCompletedWindow Extends LTAudioWindow
	Method Init()
		Project.Locked = True
		L_CurrentProfile.AddHighScore( LTLevelFailedWindow( Self ) = Null )
		ChangeLabelTitle( "Score", "You scored XXX points", Profile.Score )
		ChangeLabelTitle( "Time", "Spent", ToTime( Profile.LevelTime ) )
		ChangeLabelTitle( "Turns", "And made XXX turns", Profile.Turns )
		Super.Init()
	End Method

	Method OnButtonUnPress( Gadget:LTGadget, ButtonAction:LTButtonAction )
		If ButtonAction <> L_LeftMouseButton Then Return
		Select Gadget.GetName()
			Case "SelectLevel"
				Project.CloseWindow( Self )
				Project.LoadWindow( Menu.Interface, "LTLevelSelectionWindow" )
			Case "Restart"
				Project.CloseWindow( Self )
				Menu.LoadLevel( Profile.LevelName )
				Menu.Project.Locked = False
			Case "NextLevel"
				Project.CloseWindow( Self )
				If Profile.LevelName = Profile.FirstLockedLevel Then Menu.NextLevel()
				Menu.LoadLevel( Profile.FirstLockedLevel )
				Menu.Project.Locked = False
		End Select
	End Method
End Type