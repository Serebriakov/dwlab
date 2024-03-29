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
bbdoc: Class for GUI window.
End Rem
Type LTWindow Extends LTLayer
	Field World:LTWorld
	Field Project:LTGUIProject
	Field MouseOver:TMap = New TMap
	Field Modal:Int
	
	
	
	Method Draw( DrawingAlpha:Double = 1.0 )
		If Not Visible Then Return
		If Modal Then L_CurrentCamera.Darken( 0.6 )
		L_Window = Self
		Super.Draw()
	End Method
	
	
	
	Method Act()
		If Not Active Then Return
		
		L_Window = Self
		
		For Local Gadget:LTGadget = Eachin Children
			If Not Gadget.Active Then Continue
			
			If Gadget.CollidesWithSprite( L_Cursor ) Then
				If Not MouseOver.Contains( Gadget ) Then
					Gadget.OnMouseOver()
					OnMouseOver( Gadget )
					MouseOver.Insert( Gadget, Null )
				End If
				
				For Local ButtonAction:LTButtonAction = Eachin L_GUIButtons
					If ButtonAction.WasPressed() Then
						Gadget.OnButtonPress( ButtonAction )
						OnButtonPress( Gadget, ButtonAction )
					End If
					If ButtonAction.WasUnpressed() Then
						Gadget.OnButtonUnpress( ButtonAction )
						OnButtonUnpress( Gadget, ButtonAction )
					End If
					If ButtonAction.IsDown() Then
						Gadget.OnButtonDown( ButtonAction )
						OnButtonDown( Gadget, ButtonAction )
					Else
						Gadget.OnButtonUp( ButtonAction )
						OnButtonUp( Gadget, ButtonAction )
					End If
				Next
			ElseIf MouseOver.Contains( Gadget ) Then
				Gadget.OnMouseOut()
				OnMouseOut( Gadget )
				MouseOver.Remove( Gadget )
			End If
		Next
		
		If L_Enter.WasPressed() Then
			For Local Gadget:LTGadget = Eachin Children
				If Gadget.GetParameter( "action" )[ ..4 ] = "save" Then
					OnButtonPress( Gadget, L_LeftMouseButton )
					OnButtonUnpress( Gadget, L_LeftMouseButton )
				End If
			Next
		ElseIf L_Esc.WasPressed() Then
			For Local Gadget:LTGadget = Eachin Children
				If Gadget.GetParameter( "action" ) = "close" Then
					OnButtonPress( Gadget, L_LeftMouseButton )
					OnButtonUnpress( Gadget, L_LeftMouseButton )
				End If
			Next
		End If
	
		If L_ActiveTextField Then
			If L_ActiveTextField.Active Then
				Local LeftPart:String = L_ActiveTextField.LeftPart
				Local RightPart:String = L_ActiveTextField.RightPart
				If LeftPart Then
					If L_CharacterLeft.WasPressed() Then
						L_ActiveTextField.RightPart = LeftPart[ LeftPart.Length - 1.. ] + RightPart
						L_ActiveTextField.LeftPart = LeftPart[ ..LeftPart.Length - 1 ]
					End If
					If L_DeletePreviousCharacter.WasPressed() Then L_ActiveTextField.LeftPart = LeftPart[ ..LeftPart.Length - 1 ]
				End If
				If RightPart Then
					If L_CharacterRight.WasPressed() Then
						L_ActiveTextField.LeftPart = LeftPart + RightPart[ ..1 ]
						L_ActiveTextField.RightPart = RightPart[ 1.. ]
					End If
					If L_DeleteNextCharacter.WasPressed() Then L_ActiveTextField.RightPart = RightPart[ 1.. ]
				End If
				Local Key:Int = GetChar()
				If Key >= 32 And ( L_ActiveTextField.MaxSymbols = 0 Or Len( L_ActiveTextField.LeftPart + L_ActiveTextField.RightPart ) < L_ActiveTextField.MaxSymbols ) Then
					L_ActiveTextField.LeftPart :+ Chr( Key )
				End If
				L_ActiveTextField.Text = L_ActiveTextField.LeftPart + L_ActiveTextField.RightPart
			End If
		End If
		
		Super.Act()
	End Method
	
	
	
	Rem
	bbdoc: Button pressing event method.
	about: Called when button just being pressed on window's gadget.
	
	See also: #OnButtonUnpress, #OnButtonDown, #OnButtonUp, #OnMouseOver, #OnMouseOut
	End Rem
	Method OnButtonPress( Gadget:LTGadget, ButtonAction:LTButtonAction )
	End Method
	
	
	
	Rem
	bbdoc: Button unpressing event method
	about: Called when button just being unpressed on window's gadget.
	Some standard commands are available (can be set in editor in "action" parameter):
	<ul><li>"save" - executes window's Save() method. Intended for saving data changed by window.
	<lI>"close" - closes the window.
	<li>"end" - forces exit from current project.
	<li>"save_and_close" - performs "save" and "close" actions
	<li>"save_and_end" - performs "save" and "end" actions</ul>
	"window" parameter opens a window with given name
	"window_class" parameter opens a window of given class
	
	See also: #OnButtonPress, #OnButtonDown, #OnButtonUp, #OnMouseOver, #OnMouseOut
	End Rem
	Method OnButtonUnpress( Gadget:LTGadget, ButtonAction:LTButtonAction )
		Local Link:TLink = Project.Windows.FindLink( Self )
		Select Gadget.GetParameter( "action" )
			Case "save"
				Save()
			Case "save_and_close"
				Save()
				OnClose()
				If Link Then Project.CloseWindow( LTWindow( Link.Value() ) )
			Case "close"
				OnClose()
				If Link Then Project.CloseWindow( LTWindow( Link.Value() ) )
			Case "save_and_end"
				Save()
				OnClose()
				Project.Exiting = True
			Case "end"
				OnClose()
				Project.Exiting = True
		End Select
		
		Local Name:String = Gadget.GetParameter( "window" )
		If Name Then
			Project.LoadWindow( World, , Name ) 
		Else
			Local Class:String = Gadget.GetParameter( "window_class" )
			If Class Then Project.LoadWindow( World, Class ) 
		End If
	End Method
	
	
	
	Rem
	bbdoc: Button down event method.
	about: Called when button is currently pressed and cursor is over window's gadget.
	
	See also: #OnButtonPress, #OnButtonUnpress, #OnButtonUp, #OnMouseOver, #OnMouseOut
	End Rem
	Method OnButtonDown( Gadget:LTGadget, ButtonAction:LTButtonAction )
	End Method
	
	
	
	Rem
	bbdoc: Button up event method.
	about: Called when button is currently released and cursor is over window's gadget.
	
	See also: #OnButtonPress, #OnButtonUnpress, #OnButtonUp, #OnMouseOver, #OnMouseOut
	End Rem
	Method OnButtonUp( Gadget:LTGadget, ButtonAction:LTButtonAction )
	End Method
	
	
	
	Rem
	bbdoc: Mouse cursor entering gadget event method.
	about: Called when mouse is just entered window's gadget area.
	
	See also: #OnButtonPress, #OnButtonUnpress, #OnButtonDown, #OnButtonUp, #OnMouseOut
	End Rem
	Method OnMouseOver( Gadget:LTGadget )
	End Method
	
	
	
	Rem
	bbdoc: Mouse cursor leaving gadget event method.
	about: Called when mouse is just left window's gadget area.
	
	See also: #OnButtonPress, #OnButtonUnpress, #OnButtonDown, #OnButtonUp, #OnMouseOver
	End Rem
	Method OnMouseOut( Gadget:LTGadget )
	End Method
	
	
	
	Rem
	bbdoc: Window closing event method.
	about: Called when window is closed.
	End Rem
	Method OnClose()
	End Method
	
	
	
	Rem
	bbdoc: Window data saving method.
	about: Can be executed via window gadget using standard command.
	Usually being executed before closing of the window.
	
	See also: #OnButtonUnpress
	End Rem
	Method Save()
	End Method
	
	
	
	Method DeInit()
	End Method



	Method ChangeLabelTitle( LabelName:String, LabelTitle:String, XXXValue:String = "" )
		LabelTitle = LocalizeString( "{{" + LabelTitle + "}}" )
		If XXXValue Then LabelTitle = Replace( LabelTitle, "XXX", XXXValue )
		LTLabel( FindShape( LabelName ) ).Text = LabelTitle
	End Method
End Type