Type TMushroom Extends LTVectorSprite
    Global Vis:LTVisualizer = LTVisualizer.FromFile( "media\MagicMushroom.png" )

   Const Speed:Double = 2.0

   Function FromTile( TileX:Int, TileY:Int )
       Local Mushroom:TMushroom = New TMushroom
       Mushroom.SetAsTile( Game.TileMap, TileX, TileY )
       Mushroom.Visualizer = Vis
       Mushroom.DX = Speed
       Mushroom.AttachModel( New TAppearing )
   End Function
End Type



Type TAppearing Extends LTBehaviorModel
   Const Speed:Double = 1.0
   
   Global PowerupAppears:TSound = TSound.Load( "media\PowerupAppears.ogg", False )   

   Field DestinationY:Double
   
   Method Activate( Shape:LTShape )
       Local Sprite:LTVectorSprite = LTVectorSprite( Shape )
       DestinationY = Sprite.Y - Sprite.Height
       Sprite.ShapeType = LTSprite.Circle
       Sprite.Frame = 0
       Sprite.LimitByWindow( Sprite.X, Sprite.Y - 1.0, 1.0, 1.0 )
       PlaySound( PowerupAppears )
       Game.Level.AddLast( Sprite )
   End Method
   
   Method ApplyTo( Shape:LTShape )
       If Shape.Y <= DestinationY Then
           Remove( Shape )
       Else
           Shape.Move( 0, -Speed )
       End If
   End Method
   
   Method Deactivate( Shape:LTShape )
       Shape.RemoveWindowLimit()
       Shape.AttachModel( New LTHorizontalMovementModel )
       Shape.AttachModel( LTTileMapCollisionModel.Create( Game.Tilemap, TCollisionWithWall.Instance ) )
       Shape.AttachModel( LTSpriteMapCollisionModel.Create( Game.MovingObjects, TSpritesHorizontalCollision.Instance ) )
       Shape.AttachModel( New LTVerticalMovementModel )
       Shape.AttachModel( LTTileMapCollisionModel.Create( Game.Tilemap, TCollisionWithFloor.Instance ) )
       Shape.AttachModel( LTSpriteMapCollisionModel.Create( Game.MovingObjects, TSpritesVerticalCollision.Instance ) )
       Shape.AttachModel( New TGravity )
       Shape.AttachModel( New TRemoveIfOutside )
       Game.Level.Remove( Shape )
       Game.MovingObjects.InsertSprite( LTSprite( Shape ) )
   End Method
End Type
