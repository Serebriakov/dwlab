'
' MindStorm - Digital Wizard's Lab example
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TFire Extends LTSprite
	Field Chaingun:TChaingun
	
	
	
	Method Draw( DrawingAlpha:Double = 1.0 )
		if Game.Time < Chaingun.FiringStartingTime + Chaingun.FiringPeriod Then
			Visualizer.Alpha = 1.5 * ( Chaingun.FiringStartingTime + Chaingun.FiringPeriod - Game.Time ) / Chaingun.FiringPeriod
			Super.Draw()
		End If
	End Method
End Type