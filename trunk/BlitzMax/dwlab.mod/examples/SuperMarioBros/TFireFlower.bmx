Type TFireFlower Extends TBonus
   Const AnimationSpeed:Double = 0.1
   
   Global Vis:LTVisualizer = LTVisualizer.FromFile( "media\Fireflower.png", 4 )

   Function FromTile( TileX:Int, TileY:Int )
       Local FireFlower:TFireFlower = New TFireFlower
       FireFlower.SetAsTile( Game.TileMap, TileX, TileY )
       FireFlower.Visualizer = Vis
       FireFlower.DX = 0
       FireFlower.AttachModel( New TAppearing )
   End Function
   
   Method Act()
       Animate( AnimationSpeed )
       Super.Act()
   End Method
   
   Method Collect()
       TScore.FromSprite( Self, TScore.s1000 )
       If TMario.Big Then
           Game.Mario.AttachModel( New TFlashing )
       Else
           Game.Mario.AttachModel( New TGrowing )
       End If
   End Method
End Type