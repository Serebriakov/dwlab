Type TGoomba Extends TEnemy
   Const WalkingAnimationSpeed:Double = 0.3

   Field Animation:LTAnimationModel = LTAnimationModel.Create( True, WalkingAnimationSpeed, 2 )
   
   Method Init()
       AttachModel( Animation )
       AttachModel( New LTHorizontalMovementModel )
       AttachModel( LTTileMapCollisionModel.Create( Game.Tilemap, TCollisionWithWall.Instance ) )
       AttachModel( LTSpriteMapCollisionModel.Create( Game.MovingObjects, TSpritesHorizontalCollision.Instance ) )
       AttachModel( New LTVerticalMovementModel )
       AttachModel( LTTileMapCollisionModel.Create( Game.Tilemap, TCollisionWithFloor.Instance ) )
       AttachModel( LTSpriteMapCollisionModel.Create( Game.MovingObjects, TSpritesVerticalCollision.Instance ) )
       AttachModel( New TGravity )
       AttachModel( New TRemoveIfOutside )
   End Method
   
   Method Stomp()
       AttachModel( New TStomped )
   End Method
End Type



Type TStomped Extends LTBehaviorModel
   Const FlatPeriod:Double = 1.0
   
   Global Stomp:TSound = TSound.Load( "media\Stomp.ogg", False )
   
   Field StartingTime:Double
   
   Method Activate( Shape:LTShape )
       Local Goomba:TGoomba = TGoomba( Shape )
       Goomba.DeactivateAllModels()
       Goomba.Frame = 2
       Game.MovingObjects.RemoveSprite( Goomba )
	   Goomba.SpriteMap = Null
       Game.Level.AddLast( Goomba )
       
       Stomp.Play()
     
       StartingTime = Game.Time
   End Method
   
   Method ApplyTo( Shape:LTShape )
       If Game.Time > StartingTime + FlatPeriod Then Game.Level.Remove( Shape )
   End Method
End Type



Type TKicked Extends LTBehaviorModel
   Const Strength:Double = -6.0
   
   Global Sound:TSound = TSound.Load( "media\Kick.ogg", False )

   Method Init( Shape:LTShape )
       Local Sprite:LTVectorSprite = LTVectorSprite( Shape )
       Sprite.DY = Strength
	   Sprite.DeactivateModel( "LTTileMapCollisionModel" )
	   Sprite.DeactivateModel( "LTSpriteMapCollisionModel" )
       Sprite.DeactivateModel( "LTAnimationModel" )
       Sprite.Visualizer.YScale :* -1.0
       PlaySound( Sound )
       TScore.FromSprite( Sprite, TScore.s100 )
       Game.MovingObjects.RemoveSprite( Sprite )
	   Game.Level.AddLast( Sprite )
   End Method
End Type