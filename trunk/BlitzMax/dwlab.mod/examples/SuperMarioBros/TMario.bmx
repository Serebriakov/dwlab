Type TMario Extends LTVectorSprite
	Global Collisions:Int
	
	Global Jumping:TJumping = New TJumping
	
	Global DyingAnimation:LTAnimationModel = LTAnimationModel.Create( False, 1, 1, 6 )
	Global JumpingAnimation:LTAnimationModel = LTAnimationModel.Create( False, 1, 1, 4 )
	Global SlidingAnimation:LTAnimationModel = LTAnimationModel.Create( False, 1, 1, 5 )
	Global MovementAnimation:LTAnimationModel = LTAnimationModel.Create( True, 0.2, 1, 1 )
	Global StandingAnimation:LTAnimationModel = LTAnimationModel.Create( True, 1, 1, 0 )
   
	Global Combo:Int = TScore.s100
	Global Big:Int
	
    Global SmallMario:LTImage = LTImage.FromFile( "media\SmallMario.png", 9, 4 )
    Global SuperMario:LTImage = LTImage.FromFile( "media\SuperMario.png", 9, 5 )
    Global Growth:LTImage = LTImage.FromFile( "media\Growth.png", 3 )
    Global Powerup:TSound = TSound.Load( "media\Powerup.ogg", False )	
    Global Pipe:TSound = TSound.Load( "media\Pipe.ogg", False )
	   
    Method Init()
       BehaviorModels.Clear()
		
       AttachModel( New LTHorizontalMovementModel )
       AttachModel( LTTileMapCollisionModel.Create( Game.Tilemap, TMarioCollidedWithWall.Instance ) )
       AttachModel( New LTVerticalMovementModel )
       AttachModel( LTTileMapCollisionModel.Create( Game.Tilemap, TMarioCollidedWithFloor.Instance ) )
       AttachModel( LTSpriteMapCollisionModel.Create( Game.MovingObjects, TMarioCollidedWithSprite.Instance ) )
       AttachModel( New TGravity )
       AttachModel( New TMoving )
	   AttachModel( Jumping )
	   
	   Local AnimationStack:LTModelStack = New LTModelStack
	   AttachModel( AnimationStack )
	   AnimationStack.Add( DyingAnimation, False )
	   AnimationStack.Add( JumpingAnimation, False )
	   AnimationStack.Add( SlidingAnimation, False )
	   AnimationStack.Add( MovementAnimation, False )
	   AnimationStack.Add( StandingAnimation )
	End Method
   
   Method Act()
		Jumping.DeactivateModel( Self )
		Super.Act()
       
       LimitHorizontallyWith( Game.Level.Bounds )
       
       L_CurrentCamera.JumpTo( Self )
       L_CurrentCamera.LimitWith( Game.Level.Bounds )
	   
	   If TopY() > Game.Tilemap.BottomY() And Not FindModel( "TDying" ) Then AttachModel( TDying.Create( True ) )
	   
	   If Big And KeyDown( Key_Down ) Then If Not FindModel( "TSitting" ) Then AttachModel( New TSitting )
   End Method
   
   Method Damage()
       If Not FindModel( "TInvisible" ) Then
           If Big Then
               If Not FindModel( "TShrinking" ) Then AttachModel( New TShrinking )
           Else
               AttachModel( TDying.Create( False ) )
           End If
       End If        
   End Method
End Type



Type TMarioCollidedWithWall Extends LTSpriteAndTileCollisionHandler
	Global Instance:TMarioCollidedWithWall = New TMarioCollidedWithWall
	
	Method HandleCollision( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionSprite:LTSprite )
		Sprite.PushFromTile( TileMap, TileX, TileY )
		LTVectorSprite( Sprite ).DX = 0
	End Method
End Type



Type TMarioCollidedWithFloor Extends LTSpriteAndTileCollisionHandler
	Global Instance:TMarioCollidedWithFloor = New TMarioCollidedWithFloor
	
	Method HandleCollision( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionSprite:LTSprite )
		Sprite.PushFromTile( TileMap, TileX, TileY )
		Local VectorSprite:LTVectorSprite = LTVectorSprite( Sprite )
		If VectorSprite.DY >= 0 Then
			TMario.Jumping.ActivateModel( Sprite )
			TMario.JumpingAnimation.DeactivateModel( Sprite )
			TMario.Combo = TScore.s100
		Else
               Local TileNum:Int = TileMap.GetTile( TileX, TileY )
               Select TileNum
                   Case TTiles.QuestionBlock, TTiles.Bricks, TTiles.MushroomBlock, TTiles.Mushroom1UPBlock, TTiles.CoinsBlock, ..
				   			TTiles.StarmanBlock, TTiles.ShadyBricks
						If TileNum = TTiles.Bricks Or TileNum = TTiles.ShadyBricks And TMario.Big Then
							TBricks.FromTile( TileX, TileY, TileNum )
						Else
                       		TBlock.FromTile( TileX, TileY, TileNum )
						End If
               End Select		
		End If
		VectorSprite.DY = 0
	End Method
End Type



Type TMarioCollidedWithSprite Extends LTSpriteCollisionHandler
	Const HopStrength:Double = -4.0
	
	Global Instance:TMarioCollidedWithSprite = New TMarioCollidedWithSprite
	
	Method HandleCollision( Sprite1:LTSprite, Sprite2:LTSprite )
        If TMushroom( Sprite2 ) Then
           TScore.FromSprite( Sprite1, TScore.s1000 )
           Sprite1.AttachModel( New TGrowing )
           Game.Level.Remove( Sprite2 )
           Game.MovingObjects.RemoveSprite( Sprite2 )
        ElseIf TGoomba( Sprite2 ) Then
           If Sprite1.BottomY() < Sprite2.Y Then
               Sprite2.AttachModel( New TStomped )
               LTVectorSprite( Sprite1 ).DY = HopStrength
               TScore.FromSprite( Sprite2, TMario.Combo )
               If TMario.Combo < TScore.s400 Then TMario.Combo :+ 1
           Else
               TMario( Sprite1 ).Damage()
           End If
       End If
	End Method
End Type



Type TMoving Extends LTBehaviorModel
   Const Acceleration:Double = 20.0
   Const AnimationSpeed:Double = 1.5
   Const MaxWalkingSpeed:Double = 7.0
   Const MaxRunningSpeed:Double = 20.0
   Const Friction:Double = 40.0
   Const Speed:Double = 5.0
   
    Field Frame:Double
	
   Method ApplyTo( Shape:LTShape )
       Local VectorSprite:LTVectorSprite = LTVectorSprite( Shape )
        Local Direction:Double = Sgn( VectorSprite.DX )
       Local Force:Double = 0.0
       If KeyDown( Key_Left ) Then Force = -1.0
       If KeyDown( Key_Right ) Then Force = 1.0

	   TMario.SlidingAnimation.DeactivateModel( VectorSprite )
        If Force <> Direction And Direction Then
           Frame = 0.0
           If Force Then TMario.SlidingAnimation.ActivateModel( VectorSprite )
           If Abs( VectorSprite.DX ) < Game.PerSecond( Friction ) Then
               VectorSprite.DX = 0
           Else
               VectorSprite.DX :- Direction * Game.PerSecond( Friction )
           End If
       ElseIf Force Then
           Local MaxSpeed:Double = MaxWalkingSpeed
           If KeyDown( Key_S ) Then MaxSpeed = MaxRunningSpeed
           if Abs( VectorSprite.DX ) < MaxSpeed Then VectorSprite.DX :+ Force * Game.PerSecond( Acceleration )
           Frame :+ Game.PerSecond( VectorSprite.DX ) * AnimationSpeed
           TMario.MovementAnimation.FrameStart = Floor( Frame - Floor( Frame / 3.0 ) * 3.0 ) + 1
       End If
	   
	   If Force Then
			TMario.MovementAnimation.ActivateModel( Shape )
			VectorSprite.SetFacing( Force )
		Else
			TMario.MovementAnimation.DeactivateModel( Shape )
	   End If
   End Method
   
   Method Deactivate( Shape:LTShape )
       LTVectorSprite( Shape ).DX = 0.0
   End Method
End Type



Type TJumping Extends LTBehaviorModel
   Const Strength:Double = -17.0

   Method ApplyTo( Shape:LTShape )
		If KeyDown( Key_A ) Then
			LTVectorSprite( Shape ).DY = Strength
			TMario.JumpingAnimation.ActivateModel( Shape )
		End If
	End Method
End Type



Type TDying Extends LTBehaviorModel
   Const Period:Double = 3.5
   
   Global GameOver:TSound = TSound.Load( "media\GameOver.ogg", False )
   
   Field Chasm:Int
   Field StartingTime:Double
   
   Function Create:TDying( Chasm:Int )
       Local Dying:TDying = New TDying
       Dying.Chasm = Chasm
       Return Dying
   End Function
   
   Method Activate( Shape:LTShape )
       Local Mario:TMario = TMario( Shape )
	   Mario.DeactivateModel( "LTTileMapCollisionModel" )
	   Mario.DeactivateModel( "LTSpriteMapCollisionModel" )
       Mario.DeactivateModel( "TMoving" )
       Mario.DeactivateModel( "TJumping" )
	   
       Mario.DX = 0.0
       If Not Chasm Then
           Mario.DY = TJumping.Strength
           TMario.DyingAnimation.ActivateModel( Mario )
       End If
       
	   L_Music.ClearMusic()
	   L_Music.Add( "dying" )
	   L_Music.Start()
       StartingTime = Game.Time
   End Method
   
    Method ApplyTo( Shape:LTShape )
       If Game.Time > StartingTime + Period Then
           Game.Lives :- 1
           If Game.Lives = 0 Then
               Local Channel:TChannel = GameOver.Play()
               While Channel.Playing()
               Wend
               End
           End If
           Game.InitLevel()
       End If
   End Method
End Type



Type TGrowing Extends LTBehaviorModel
   Const Speed:Double = 0.08
   Const Phases:Int = 10
   
   Field StartingTime:Double

   Method Init( Shape:LTShape )
       Shape.DeactivateAllModels()
       Game.Level.Active = False
       Local Sprite:LTSprite = LTSprite( Shape )
       Sprite.AlterCoords( 0.0, -0.5 )
       Sprite.SetHeight( 2.0 )
       Sprite.Visualizer.SetImage( TMario.Growth )
       Sprite.Frame = 0
       PlaySound( TMario.Powerup )
       StartingTime = Game.Time
   End Method
   
   Method ApplyTo( Shape:LTShape )
       LTSprite( Shape ).Animate( Speed, , , StartingTime, True )
       If Game.Time > StartingTime + Phases * Speed Then Remove( Shape )
   End Method

   Method Deactivate( Shape:LTShape )
       Shape.ActivateAllModels()
       Game.Level.Active = True
       Shape.Visualizer.SetImage( TMario.SuperMario )
	   TMario.Big = 1
   End Method
End Type



Type TShrinking Extends TGrowing
   Method Init( Shape:LTShape )
       Shape.DeactivateAllModels()
       Game.Level.Active = False
       Local Sprite:LTSprite = LTSprite( Shape )
       Sprite.Visualizer.SetImage( TMario.Growth )
       Sprite.Frame = 0
       PlaySound( TMario.Pipe )
       StartingTime = Game.Time
       Shape.AttachModel( New TInvisible )
   End Method
   
   Method ApplyTo( Shape:LTShape )
       LTSprite( Shape ).Animate( Speed, , , StartingTime - 2.0 * Speed, True )
       If Game.Time > StartingTime + Phases * Speed Then Remove( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
       Shape.ActivateAllModels()
       Game.Level.Active = True
       Shape.SetSize( 1.0, 1.0 )
       Shape.AlterCoords( 0.0, 0.5 )
       Shape.Visualizer.SetImage( TMario.SmallMario )
       TMario.Big = False
   End Method
End Type



Type TInvisible Extends LTBehaviorModel
   Const Period:Double = 2.0
   Const BlinkingSpeed:Double = 0.05
   
   Field StartingTime:Double
   
   Method Activate( Shape:LTShape )
       StartingTime = Game.Time
   End Method
   
   Method ApplyTo( Shape:LTShape )
       Shape.Visible = Floor( Game.Time / BlinkingSpeed ) Mod 2
       If Game.Time > StartingTime + Period Then Remove( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
       Shape.RemoveModel( "TSitting" )
       Shape.Visible = True
   End Method
End Type



Type TSitting Extends LTBehaviorModel
   Method Activate( Shape:LTShape )
       Shape.SetHeight( 1.4 )
       Shape.AlterCoords( 0, 0.3 )
       Shape.Visualizer.DY = -0.3 / 2.0
       Shape.Visualizer.YScale = 2.0 / 1.4
       Shape.DeactivateModel( "TMoving" )
   End Method
   
   Method ApplyTo( Shape:LTShape )
       LTSprite( Shape ).Frame = 7
       If Not KeyDown( Key_Down ) Then Remove( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
       Shape.AlterCoords( 0, -0.3 )
       Shape.SetHeight( 2.0 )
       Shape.Visualizer.DY = 0.0
       Shape.Visualizer.YScale = 1.0
       Shape.ActivateModel( "TMoving" )
   End Method
End Type