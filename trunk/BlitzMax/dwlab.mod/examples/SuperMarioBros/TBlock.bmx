Include "TCoin.bmx"
Include "TBricks.bmx"

Type TBlock Extends LTVectorSprite
   Global Bump:TSound = TSound.Load( "media\Bump.ogg", False )
   
   Const Gravity:Double = 8.0
   Const Impulse:Double = 1.5
   
   Field LowestY:Double
   Field TileX:Int, TileY:Int, TileNum:Int
  
   Function FromTile( TileX:Int, TileY:Int, TileNum:Int )
       Local Block:TBlock = New TBlock
       Block.SetAsTile( Game.TileMap, TileX, TileY )
       Game.TileMap.SetTile( TileX, TileY, 53 )
       Block.TileX = TileX
       Block.TileY = TileY
       Block.LowestY = Block.Y
       Block.DY = -Impulse
       Block.Frame = TTiles.SolidBlock
       Select TileNum
           Case TTiles.QuestionBlock
               TCoin.FromTile( TileX, TileY )
         Case TTiles.MushroomBlock
               If TMario.Big Then
                   TFireFlower.FromTile( TileX, TileY )
               Else
                   TMushroom.FromTile( TileX, TileY )
               End If
           Case TTiles.CoinsBlock
               TCoin.FromTile( TileX, TileY )
               Game.Level.AttachModel( TTileChange.Create( TileX, TileY ) )
               Block.Frame = TileNum
           Case TTiles.Bricks, TTiles.ShadyBricks
               Block.Frame = TileNum
           Case TTiles.Mushroom1UPBlock
               TOneUpMushroom.FromTile( TileX, TileY )
            Case TTiles.StarmanBlock
               TStarMan.FromTile( TileX, TileY )
       End Select
	          
       Bump.Play()
       Game.Level.AddLast( Block )
   End Function

   Method Act()
       DY :+ Game.PerSecond( Gravity )
       If Y >= LowestY And DY > 0 Then
           Game.Tilemap.SetTile( TileX, TileY, Frame )
           Game.Level.Remove( Self )
       Else
           MoveForward()
       End If
   End Method
End Type



Type TTileChange Extends LTBehaviorModel
   Const Period:Double = 5.0    
   
   Field TileX:Int, TileY:Int
   Field StartingTime:Double
   
   Function Create:TTileChange( TileX:Int, TileY:Int )
       Local TileChange:TTileChange = New TTileChange
       TileChange.TileX = TileX
       TileChange.TileY = TileY
       Return TileChange
   End Function
   
   Method Activate( Shape:LTShape )
       StartingTime = Game.Time
   End Method
   
   Method ApplyTo( Shape:LTShape )
       If Game.Time >= StartingTime + Period Then
           If Game.Tilemap.GetTile( TileX, TileY ) <> 53 Then
               Game.Tilemap.SetTile( TileX, TileY, TTiles.SolidBlock )
               Remove( Shape )
           End If
       End If
   End Method
End Type