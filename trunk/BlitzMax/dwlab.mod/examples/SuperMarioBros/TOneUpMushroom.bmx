Type TOneUpMushroom Extends TBonus
   Const Speed:Double = 2.0
   
   Global Vis:LTVisualizer = LTVisualizer.FromFile( "media\1upMushroom.png" )
   Global Sound:TSound = TSound.Load( "media\1-up.ogg", False )
   
   Function FromTile( TileX:Int, TileY:Int )
       Local Mushroom:TOneUpMushroom = New TOneUpMushroom
       Mushroom.SetAsTile( Game.TileMap, TileX, TileY )
       Mushroom.Visualizer = Vis
       Mushroom.DX = Speed
       Mushroom.AttachModel( New TAppearing )
   End Function

   Method Collect()
       TScore.FromSprite( Self, TScore.s1up )
       Sound.Play()
   End Method
End Type