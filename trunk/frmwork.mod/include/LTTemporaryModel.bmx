'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Include "LTFixedWaitingModel.bmx"
Include "LTRandomWaitingModel.bmx"

Type LTTemporaryModel Extends LTBehaviorModel
	Field StartingTime:Double
	Field Period:Double
	Field NextModels:TList = New TList
	
	
	
	Method Activate( Shape:LTShape )
		StartingTime = L_CurrentProject.Time
	End Method
	
	
	
	Method ApplyTo( Shape:LTShape )
		If L_CurrentProject.Time > StartingTime + Period Then
			Remove( Shape )
			Shape.AttachModels( NextModels )
		End If
	End Method
	
	
	
	Method Info:String( Shape:LTShape )
		Return "" + L_TrimDouble( L_CurrentProject.Time - StartingTime ) + " of " + L_TrimDouble( Period )
	End Method
End Type