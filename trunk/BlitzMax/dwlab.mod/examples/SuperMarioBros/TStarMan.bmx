Type TStarMan Extends TBonus
   Const Speed:Double = 2.0
   Const JumpStrength:Double = -10.0
   Const AnimationSpeed:Double = 0.2

   Global Vis:LTVisualizer = LTVisualizer.FromFile( "media\Starman.png", 4 )
   
   Function FromTile( TileX:Int, TileY:Int )
       Local StarMan:TStarMan = New TStarMan
       StarMan.SetAsTile( Game.TileMap, TileX, TileY )
       StarMan.Visualizer = Vis
       StarMan.DX = Speed
       StarMan.AttachModel( New TAppearing )
   End Function
   
   Method Act()
       Animate( AnimationSpeed )
       Super.Act()
   End Method
   
   Method Collect()
       TScore.FromSprite( Self, TScore.s1000 )
       Game.Mario.AttachModel( New TInvulnerable )
   End Method
End Type