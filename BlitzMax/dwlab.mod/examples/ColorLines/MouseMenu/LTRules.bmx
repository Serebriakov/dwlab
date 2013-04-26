'
' Mouse-oriented game menu - Digital Wizard's Lab framework template
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type LTRules Extends LTAudioWindow
	Method Init()
		ChangeLabelTitle( "LevelTimeDescription", ConditionString( Profile.TotalLevelTime = 0, "Unlimited" ) + "LevelTimeDescription", ToTime( Profile.TotalLevelTime ) )
		If Profile.TotalLevelTime > 0 Then ChangeLabelTitle( "LevelTime", ToTime( Profile.TotalLevelTime ) )
		ChangeLabelTitle( "TurnTimeDescription", ConditionString( Profile.TotalTurnTime = 0, "Unlimited" ) + "TurnTimeDescription", ToTime( Profile.TotalTurnTime ) )
		If Profile.TotalTurnTime > 0 Then ChangeLabelTitle( "LevelTime", ToTime( Profile.TotalLevelTime ) )
		ChangeLabelTitle( "TurnsQuantityDescription", ConditionString( Profile.TotalTurns = 0, "Unlimited" ) + "TurnsQuantityDescription", Profile.TotalTurns )
		If Profile.TotalTurns > 0 Then ChangeLabelTitle( "TurnsQuantity", Profile.TotalTurns )
		ChangeLabelTitle( "BallsPerTurnDescription", "BallsPerTurnDescription", Profile.BallsPerTurn )
		ChangeLabelTitle( "BallsPerTurnQuantity", Profile.BallsPerTurn )
		ChangeLabelTitle( "LineBallsDescription", ConditionString( Profile.OrthogonalLines, ConditionString( Profile.DiagonalLines, ..
				"All" ), "Diagonal" ) + "Description", Profile.BallsInLine )
		ChangeLabelTitle( "LineBallsQuantity", Profile.BallsInLine )
		ChangeLabelTitle( "SwapDescription", ConditionString( Not Profile.Swap, "No" ) + "SwapDescription" )
		LTSprite( FindShape( "LineBallsIcon" ) ).Frame = Profile.OrthogonalLines * 2 + Profile.DiagonalLines
		Super.Init()
	End Method
End Type