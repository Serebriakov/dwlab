Type TFireball Extends LTVectorSprite
   Const MovingSpeed:Double = 10.0
   Const Size:Double = 0.4
   Const RotatingSpeed:Double = 8.0 * 360.0
   Const JumpStrength:Double = -8.0
   
   Global Vis:LTVisualizer = LTVisualizer.FromFile( "media\Fireball.png" )
   Global Explosion:LTVisualizer = LTVisualizer.FromFile( "media\Explosion.png", 3 )
   Global Sound:TSound = TSound.Load( "media\Fireball.ogg", False )
   
   Function Fire()
       Local Fireball:TFireball = New TFireball
       Fireball.SetCoords( Game.Mario.X + 0.5 * Game.Mario.GetFacing(), Game.Mario.Y )
       Fireball.SetSize( Size, Size )
       Fireball.DX = MovingSpeed * Game.Mario.GetFacing()
       Fireball.Visualizer = Vis
       Fireball.ShapeType = LTSprite.Circle
       Fireball.AttachModel( New LTHorizontalMovementModel )
       Fireball.AttachModel( LTTileMapCollisionModel.Create( Game.Tilemap, TFireballCollidesWithWall.Instance ) )
       Fireball.AttachModel( New LTVerticalMovementModel )
       Fireball.AttachModel( LTTileMapCollisionModel.Create( Game.Tilemap, TFireballCollidesWithFloor.Instance ) )
       Fireball.AttachModel( LTSpriteMapCollisionModel.Create( Game.MovingObjects, TFireballCollidesWithSprite.Instance ) )
       Fireball.AttachModel( New TGravity )
       Sound.Play()
       Game.Level.AddLast( Fireball )
   End Function

   Method Act()
      Super.Act()
      DisplayingAngle :+ Game.PerSecond( RotatingSpeed )
   End Method
End Type



Type TFireballCollidesWithSprite Extends LTSpriteCollisionHandler
	Global Instance:TFireballCollidesWithSprite = New TFireballCollidesWithSprite

	Method HandleCollision( Sprite1:LTSprite, Sprite2:LTSprite )
       If TGoomba( Sprite2 ) Then
           Sprite1.AttachModel( New TExploding )
           Sprite2.AttachModel( New TKicked )
       End If
	End Method
End Type



Type TFireballCollidesWithFloor Extends LTSpriteAndTileCollisionHandler
	Global Instance:TFireballCollidesWithFloor = New TFireballCollidesWithFloor
	
	Method HandleCollision( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionSprite:LTSprite )
       Sprite.PushFromTile( TileMap, TileX, TileY )
       Local VectorSprite:LTVectorSprite = LTVectorSprite( Sprite )
       If VectorSprite.DY >= 0.0 Then
            VectorSprite.DY = TFireball.JumpStrength
       Else
            VectorSprite.DY :* -1
       End If
	End Method
End Type



Type TFireballCollidesWithWall Extends LTSpriteAndTileCollisionHandler
	Global Instance:TFireballCollidesWithWall = New TFireballCollidesWithWall
	
	Method HandleCollision( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionSprite:LTSprite )
   		Sprite.PushFromTile( TileMap, TileX, TileY )
		Sprite.AttachModel( New TExploding )
	End Method
End Type



Type TExploding Extends LTBehaviorModel
   Const ExplosionSpeed:Double = 0.1
   
   Global Vis:LTVisualizer = LTVisualizer.FromFile( "media\Explosion.png", 3 )

   Field StartingTime:Double

   Method Activate( Shape:LTShape )
       StartingTime = Game.Time
       Shape.SetSize( 1.0, 1.0 )
       Shape.Visualizer = Vis
       TBlock.Bump.Play()
       Shape.DeactivateAllModels()
   End Method
   
   Method ApplyTo( Shape:LTShape )
       If Game.Time > StartingTime + 3.0 * ExplosionSpeed Then Remove( Shape )
       LTSprite( Shape ).Frame = ( Game.Time - StartingTime ) / ExplosionSpeed
   End Method
   
   Method Deactivate( Shape:LTShape )
       Game.Level.Remove( Shape )
   End Method
End Type