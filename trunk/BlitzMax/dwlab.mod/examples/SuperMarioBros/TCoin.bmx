Type TCoin Extends LTVectorSprite
   Const Gravity:Double = 16.0
   
   Global Vis:LTVisualizer = LTVisualizer.FromFile( "media\FlippingCoin.png", 4 )
   Global CoinFlip:TSound = TSound.Load( "media\Coin.ogg", False )
   
   Field LowestY:Double
   
   Function FromTile( TileX:Int, TileY:Int )
       CoinFlip.Play()
       Local Coin:TCoin = New TCoin
       Coin.SetAsTile( Game.Tilemap, TileX, TileY )
       Coin.LowestY = Coin.Y
       Coin.SetSize( 0.5, 0.5 )
       Coin.DY = -10.0
       Coin.Visualizer = Vis
       Coin.Frame = 0
       Coin.LimitByWindow( Coin.X, Coin.Y - 3.5, 1.0, 6.0 )
       Game.Level.AddLast( Coin )
   End Function
   
   Method Act()
       Animate( 0.1, 4 )
       DY :+ Game.PerSecond( Gravity )
       MoveForward()
       If Y > LowestY Then
           Game.Level.Remove( Self )
           TScore.FromSprite( Self, TScore.s200 )
           Game.Coins :+ 1
       End If
   End Method
End Type