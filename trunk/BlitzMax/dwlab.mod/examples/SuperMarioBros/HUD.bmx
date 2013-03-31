Type TBackground Extends LTVectorSprite
   Method Init()
       Game.HUDCamera.JumpTo( Self )
       Visible = False
   End Method
End Type



Type TScoreBar Extends LTVectorSprite
	Method Draw( DrawingAlpha:Double = 1.0 )
       Game.Font.PrintInShape( L_FirstZeroes( Game.Score, 8 ), Self, 0.5, LTAlign.ToLeft, LTAlign.ToCenter )
   End Method
End Type



Type TSmallCoin Extends LTVectorSprite
	Method Draw( DrawingAlpha:Double = 1.0 )
       Frame = Max( ( Floor( Game.Time / TTiles.FadingSpeed ) Mod ( TTiles.FadingPeriod + 4 ) ) - TTiles.FadingPeriod, 0 )
       If Frame = 3 Then Frame = 1
       Super.Draw()
   End Method
End Type



Type TCoinsQuantity Extends LTVectorSprite
	Method Draw( DrawingAlpha:Double = 1.0 )
       Game.Font.PrintInShape( "x" + L_FirstZeroes( Game.Coins, 2 ), Self, 0.5, LTAlign.ToLeft, LTAlign.ToCenter )
   End Method
End Type



Type TTimeBar Extends LTVectorSprite
	Method Draw( DrawingAlpha:Double = 1.0 )
       Game.Font.PrintInShape( "TIME:" + L_FirstZeroes( Game.TimeLeft, 3 ), Self, 0.5, LTAlign.ToRight, LTAlign.ToCenter )
   End Method
End Type