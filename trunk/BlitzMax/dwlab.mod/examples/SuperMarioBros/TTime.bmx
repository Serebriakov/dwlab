Type TTime
   Const Time:Int = 400
   Const Threshold:Int = 50
   Const WarningPeriod:Double = 2.77
   
   Global StartingTime:Double
   Global WarningStartingTime:Double
   
   Function Init()
       StartingTime = Game.Time
       WarningStartingTime = 0.0
   End Function
   
   Function Act()
       Game.TimeLeft = Ceil( 2.0 * ( StartingTime - Game.Time ) + Time )
       If Game.TimeLeft <= 0 Then
           If Not Game.Mario.FindModel( "TDying" ) Then Game.Mario.AttachModel( TDying.Create( False ) )
       Else If Game.TimeLeft <= Threshold And Not WarningStartingTime Then
	   	   L_Music.ClearMusic()
	   	   L_Music.Add( "warning" )
		   Game.MusicRate = 1.3
		   Game.StartMusic()
           WarningStartingTime = Game.Time
       End If
       If WarningStartingTime And Game.Time > WarningStartingTime + WarningPeriod Then Game.MusicRate = 1.3
   End Function
End Type