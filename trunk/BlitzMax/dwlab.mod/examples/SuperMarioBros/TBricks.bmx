Type TBricks Extends LTVectorSprite
   Const RotationSpeed:Double = 180.0

   Global Vis:LTVisualizer = LTVisualizer.FromFile( "media\Bricks.png", 2 )
   Global BreakBlock:TSound = TSound.Load( "media\BreakBlock.ogg", False )
   
   Function FromTile( TileX:Int, TileY:Int, TileNum:Int )
       If TileNum = TTiles.ShadyBricks Then
           Game.TileMap.SetTile( TileX, TileY, TTiles.DarkEmptyBlock )
       Else
           Game.TileMap.SetTile( TileX, TileY, TTiles.EmptyBlock )
       End If

     For Local Y:Int = -1 To 1 Step 2
           For Local X:Int = -1 To 1 Step 2
               Local Bricks:TBricks = New TBricks
               Bricks.SetAsTile( Game.TileMap, TileX, TileY )
               Bricks.SetCoords( Bricks.X + 0.25 * X, Bricks.Y + 0.25 * Y )
               Bricks.SetSize( Bricks.Width * 0.5, Bricks.Height * 0.5 )
               Bricks.DX = 3.0 * X
               Bricks.DY = 3.0 * ( Y - 3.0 )
               Bricks.Visualizer = Vis
               If TileNum = TTiles.ShadyBricks Then Bricks.Frame = 1
               Bricks.AttachModel( New TGravity )
               Bricks.AttachModel( New TRemoveIfOutside )
               Game.Level.AddLast( Bricks )
           Next
       Next
       BreakBlock.Play()
   End Function
   
   Method Act()
       Super.Act()
	   DisplayingAngle :+ DX * Game.PerSecond( RotationSpeed )
       MoveForward()
   End Method
End Type