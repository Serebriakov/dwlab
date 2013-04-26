'
' Mouse-oriented game menu - Digital Wizard's Lab framework template
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Rem
bbdoc: Class for popping out tool tip.
about: You should also define sprite named "SensitiveArea" in same layer which will define sensitive area of the tool tip where mouse cursor should be situated to pop tooltip out.
End Rem
Type LTToolTip Extends LTLayer
	Global Stack:TList = New TList

	Global DelayBeforeAppearing:Double = 0.5
	Global AppearanceTime:Double = 0.1
	
	Field SensitiveArea:LTSprite
	Field Collision:Int
	
	Rem
	bbdoc: Tooltip initialization method.
	about: You can set different properties using object parameters in editor:
	<ul><li>"delay" - delay in seconds before tooltip appearing
	<li>"appearance_time" - time of tooltip full appearance</ul>
	End Rem		
	Method Init()
		Super.Init()
		SensitiveArea = LTSprite( FindShape( "SensitiveArea" ) )
		If ParameterExists( "delay" ) Then DelayBeforeAppearing = GetParameter( "delay" ).ToDouble()
		If ParameterExists( "appearance_time" ) Then AppearanceTime = GetParameter( "appearance_time" ).ToDouble()
	End Method
	
	Method Act()
		Super.Act()
		If SensitiveArea.CollidesWithSprite( L_Cursor ) Then
			If Not Collision Then
				Visualizer.Alpha = 0.0
				Local WaitingModel:LTFixedWaitingModel = LTFixedWaitingModel.Create( DelayBeforeAppearing )
				WaitingModel.NextModels.AddLast( LTAlphaChangingModel.Create( 1.0, AppearanceTime ) )
				AttachModel( WaitingModel )
				If Not Stack.Contains( Self ) Then Stack.AddLast( Self )
			End If
			Collision = True
		ElseIf Collision Then
			RemoveModel( "LTFixedWaitingModel" )
			Local AlphaModel:LTAlphaChangingModel = LTAlphaChangingModel.Create( 0.0, AppearanceTime )
			AlphaModel.NextModels.AddLast( New LTRemoveToolTipFromStack )
			AttachModel( AlphaModel )
			Collision = False
		End If
	End Method
End Type



Type LTRemoveToolTipFromStack Extends LTBehaviorModel
	Method ApplyTo( Shape:LTShape )
		LTToolTip.Stack.Remove( Shape )
		Remove( Shape )
	End Method
End Type