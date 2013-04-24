Include "TFireball.bmx"

Type TMario Extends LTVectorSprite
	Global Collisions:Int
	
	Global Jumping:TJumping = New TJumping
	
	Global DyingAnimation:LTAnimationModel = LTAnimationModel.Create( False, 1, 1, 6 )
	Global FiringAnimation:LTAnimationModel = LTAnimationModel.Create( False, 1, 1, 6 )
	Global JumpingAnimation:LTAnimationModel = LTAnimationModel.Create( False, 1, 1, 4 )
	Global SlidingAnimation:LTAnimationModel = LTAnimationModel.Create( False, 1, 1, 5 )
	Global MovementAnimation:LTAnimationModel = LTAnimationModel.Create( True, 1, 1, 1 )
	Global StandingAnimation:LTAnimationModel = LTAnimationModel.Create( True, 1, 1, 0 )
   
	Global Combo:Int = TScore.s100
	Global Big:Int
	
    Global SmallMario:LTImage = LTImage.FromFile( "media\SmallMario.png", 9, 4 )
    Global SuperMario:LTImage = LTImage.FromFile( "media\SuperMario.png", 9, 5 )
    Global Growth:LTImage = LTImage.FromFile( "media\Growth.png", 3 )
    Global Powerup:TSound = TSound.Load( "media\Powerup.ogg", False )	
    Global Pipe:TSound = TSound.Load( "media\Pipe.ogg", False )
	
    Global FrameShift:Int	  
	Global OnLand:Int
	
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
	   AnimationStack.Add( FiringAnimation, False )
	   AnimationStack.Add( JumpingAnimation, False )
	   AnimationStack.Add( SlidingAnimation, False )
	   AnimationStack.Add( MovementAnimation, False )
	   AnimationStack.Add( StandingAnimation )
	End Method
   
   Method Act()
		OnLand = False
		Super.Act()
       
       LimitHorizontallyWith( Game.Level.Bounds )
       
       L_CurrentCamera.JumpTo( Self )
       L_CurrentCamera.LimitWith( Game.Level.Bounds )
	   
	   If TopY() > Game.Tilemap.BottomY() And Not FindModel( "TDying" ) Then AttachModel( TDying.Create( True ) )
	   
	   If Big And KeyDown( Key_Down ) Then If Not FindModel( "TSitting" ) Then AttachModel( New TSitting )
	   
	   Frame = ( Frame Mod 9 ) + FrameShift * 9
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
		If SharedTileCollision( Sprite, TileMap, TileX, TileY ) Then Return
		LTVectorSprite( Sprite ).DX = 0
	End Method
End Type


	
Function SharedTileCollision:Int( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int )
	Local TileNum:Int = TileMap.GetTile( TileX, TileY )
	If TileNum = TTIles.Coin Then
	   TileMap.SetTile( TileX, TileY, TTiles.DarkEmptyBlock )
	   Game.Coins :+ 1
	   TCoin.CoinFlip.Play()
	   Return True
	Else
	   Sprite.PushFromTile( TileMap, TileX, TileY )
	End If
End Function



Type TMarioCollidedWithFloor Extends LTSpriteAndTileCollisionHandler
	Global Instance:TMarioCollidedWithFloor = New TMarioCollidedWithFloor
	
	Method HandleCollision( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionSprite:LTSprite )
		If SharedTileCollision( Sprite, TileMap, TileX, TileY ) Then Return
		Local VectorSprite:LTVectorSprite = LTVectorSprite( Sprite )
		If VectorSprite.DY >= 0 Then
			TMario.OnLand = True
			TMario.JumpingAnimation.DeactivateModel( Sprite )
			TMario.Combo = TScore.s100
		Else
               Local TileNum:Int = TileMap.GetTile( TileX, TileY )
               Select TileNum
                   Case TTiles.QuestionBlock, TTiles.Bricks, TTiles.MushroomBlock, TTiles.CoinsBlock, TTiles.Mushroom1UPBlock, ..
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
		Local KoopaTroopa:TKoopaTroopa = TKoopaTroopa( Sprite2 )
		If KoopaTroopa Then
	   		If KoopaTroopa.ShellFrame.Active And KoopaTroopa.DX = 0 Then
				KoopaTroopa.Push()
				Return
			End if
		End If
	
        If TBonus( Sprite2 ) Then
             Game.MovingObjects.RemoveSprite( Sprite2 )
             TBonus( Sprite2 ).Collect()
        ElseIf TEnemy( Sprite2 ) Then
            If Sprite1.FindModel( "TInvulnerable" ) Then
               Sprite2.AttachModel( New TKicked )
           ElseIf Sprite1.BottomY() < Sprite2.Y Then
               TEnemy( Sprite2 ).Stomp()
			   Sprite1.PushFromSprite( Sprite2 )
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
           Frame :+ Abs( Game.PerSecond( VectorSprite.DX ) ) * AnimationSpeed
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
   
   Global Jump:TSound = TSound.Load( "media\Jump.ogg", False )

   Method ApplyTo( Shape:LTShape )
		If KeyDown( Key_A ) And TMario.OnLand Then
			LTVectorSprite( Shape ).DY = Strength
			TMario.JumpingAnimation.ActivateModel( Shape )
			Jump.Play()
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
       Shape.RemoveModel( "TFireable" )
       Shape.DeactivateAllModels()
       Game.Level.Active = False
       Local Sprite:LTSprite = LTSprite( Shape )
       Sprite.Visualizer.SetImage( TMario.Growth )
       Sprite.Frame = 0
       PlaySound( TMario.Pipe )
       StartingTime = Game.Time
       Shape.AttachModel( New TInvisible )
	   TMario.FrameShift = 0
   End Method
   
   Method ApplyTo( Shape:LTShape )
       LTSprite( Shape ).Animate( Speed, , , StartingTime - 2.0 * Speed, True )
       If Game.Time > StartingTime + Phases * Speed Then Remove( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
       Shape.ActivateAllModels()
       Game.Level.Active = True
       Shape.SetSize( 0.8, 1.0 )
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



Type TFlashing Extends LTBehaviorModel
   Const AnimationSpeed:Double = 0.05
   Const Period:Double = 0.8
   
   Field StartingTime:Double
   
   Method Init( Shape:LTShape )
       Shape.DeactivateAllModels()
       Game.Level.Active = False
       PlaySound( TMario.Powerup )
       StartingTime = Game.Time
   End Method
   
   Method ApplyTo( Shape:LTShape )
       TMario.FrameShift = 2 + ( Floor( Game.Time / AnimationSpeed ) Mod 3 )
       If Game.Time > StartingTime + Period Then Remove( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
       Shape.ActivateAllModels()
       Game.Level.Active = True
       TMario.FrameShift = 4
	   If Not Shape.FindModel( "TFireable" ) Then Shape.AttachModel( New TFireable )
   End Method
End Type



Type TInvulnerable Extends LTBehaviorModel
   Const AnimationSpeed:Double = 0.05
   Const Period:Double = 13.0
   Const FadingAnimationSpeed:Double = 0.1
   Const FadingPeriod:Double = 2.0
   
   Field StartingTime:Double
   Field Fading:Int
   
   Method Activate( Shape:LTShape )
	   L_Music.ClearMusic()
	   L_Music.Add( "inv", True )
	   L_Music.Start()
       StartingTime = Game.Time
   End Method
   
   Method ApplyTo( Shape:LTShape )
       Local Mario:TMario = TMario( Shape )
       If Game.Time < StartingTime + Period Then
           Mario.FrameShift = 1 + ( Floor( Game.Time / AnimationSpeed ) Mod 3 )
       ElseIf Game.Time < StartingTime + Period + FadingPeriod Then
			If Not Fading Then
				L_Music.ClearMusic()
				Game.StartMusic()
            	Fading = True
			End If
			Mario.FrameShift = 1 + ( Floor( Game.Time / FadingAnimationSpeed ) Mod 3 )
       Else
           Remove( Shape )
       End If
   End Method

   Method Deactivate( Shape:LTShape )
       TMario( Shape ).FrameShift = 0
   End Method
End Type



Type TFireable Extends LTBehaviorModel
   Const Period:Double = 0.25
   Const AnimationPeriod:Double = 0.1
   
   Field StartingTime:Double
   
   Method ApplyTo( Shape:LTShape )
       TMario.FrameShift = 4
       If Game.Time >= StartingTime + Period Then
           If KeyDown( Key_S ) Then
               StartingTime = Game.Time
               TMario.FiringAnimation.ActivateModel( Game.Mario )
               TFireball.Fire()
           End If
       ElseIf Game.Time >= StartingTime + AnimationPeriod Then
           TMario.FiringAnimation.DeactivateModel( Game.Mario )
       Else
           TMario.FiringAnimation.ActivateModel( Game.Mario )
       End If
   End Method
End Type



Type TSlidingDownThePole Extends LTBehaviorModel
   Const MarioSpeed:Double = 4.0
   Const FlagSpeed:Double = 8.0
   
   Global FlagPole:TSound = TSound.Load( "media\FlagPole.ogg", False )
   
   Field Pole:LTShape = Game.Level.FindShapeWithType( "TPole" )
   Field Flag:LTShape = Game.Level.FindShape( "Flag" )
   
   Method Activate( Shape:LTShape )
       Shape.DeactivateModel( "LTSpriteMapCollisionModel" )
       Shape.DeactivateModel( "TGravity" )
       Shape.DeactivateModel( "TMoving" )
       Shape.DeactivateModel( "TJumping" )
       Shape.DeactivateModel( "TSitting" )
	   Shape.DeactivateModel( "LTModelStack" )
       Game.Level.Active = False
       Game.Mario.X = Pole.X - 0.3 * Game.Mario.GetFacing()
	   Game.Mario.DY = 0.0
       Game.Mario.Frame = 8
       L_Music.ClearMusic()
	   L_Music.StopMusic()
       FlagPole.Play()
       TScore.FromSprite( Game.Mario, L_LimitInt( ( Pole.BottomY() - Game.Mario.BottomY() ) / Pole.Height * 11, TScore.s100, TScore.s8000 ) )
   End Method
   
   Method ApplyTo( Shape:LTShape )
       If Game.Mario.BottomY() < Pole.BottomY() Then Game.Mario.Move( 0.0, MarioSpeed )
       If Flag.BottomY() < Pole.BottomY() Then
           Flag.Move( 0.0, FlagSpeed )
       ElseIf Game.Mario.BottomY() >= Pole.BottomY() Then
           DeactivateModel( Shape )
       End If
   End Method
   
   Method Deactivate( Shape:LTShape )
		Shape.AttachModel( New TWalkingToExit )
   End Method
End Type



Type TWalkingToExit Extends LTBehaviorModel
   Const WalkingSpeed:Double = 5.0
   Const WalkingAnimationSpeed:Double = 0.15
   
   Global StageClear:TSound = TSound.Load( "media\StageClear.ogg", False )
   
   Field FinalExit:LTShape = Game.Level.FindShape( "FinalExit" )
   Field AnimationStartingTime:Double
   
   Method Activate( Shape:LTShape )
       Shape.ActivateModel( "TGravity" )
       Game.Mario.SetFacing( LTSprite.RightFacing )
       Game.Mario.Frame = 4
       StageClear.Play()
   End Method
   
   Method ApplyTo( Shape:LTShape )
       Game.Mario.DX = WalkingSpeed
       If TMario.OnLand Then
           Game.Mario.Animate( WalkingAnimationSpeed, 3, 1, AnimationStartingTime )
       Else
           AnimationStartingTime = Game.Time
       End If
       If Game.Mario.X >= FinalExit.X Then DeactivateModel( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
		Shape.AttachModel( New TExiting )
   End Method
End Type



Type TExiting Extends LTBehaviorModel
   Field FinalExit:LTShape = Game.Level.FindShape( "FinalExit" )
   
   Method Activate( Shape:LTShape )
       Game.Mario.LimitByWindowShape( FinalExit )
   End Method
   
   Method ApplyTo( Shape:LTShape )
       Game.Mario.DX = TWalkingToExit.WalkingSpeed
       If Game.Mario.LeftX() >= FinalExit.RightX() Then DeactivateModel( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
		Shape.AttachModel( New TRaisingFlag )
		Shape.AttachModel( New TTimeToScore )
   End Method
End Type



Type TRaisingFlag Extends LTBehaviorModel
   Const CastleFlagSpeed:Double = 0.8
   
   Global FlagOnCastle:LTVisualizer = LTVisualizer.FromFile( "media\FlagOnCastle.png" )
   
   Field CastleFlagSpace:LTShape = Game.Level.FindShape( "CastleFlagSpace" )
   Field CastleFlag:LTSprite
   
   Method Activate( Shape:LTShape )
       Game.Mario.Visible = False
       Game.Mario.DX = 0.0
       CastleFlag = New LTSprite
       Game.Level.AddLast( CastleFlag )
       CastleFlag.SetCoords( CastleFlagSpace.X, CastleFlagSpace.Y + 1.0 )
       CastleFlag.SetSize( 1.0, 1.0 )
       CastleFlag.Visualizer = FlagOnCastle
       CastleFlag.LimitByWindowShape( CastleFlagSpace )
       Game.Level.AddLast( CastleFlag )
   End Method
   
   Method ApplyTo( Shape:LTShape )
       CastleFlag.Move( 0, -CastleFlagSpeed )
       If CastleFlagSpace.Y >= CastleFlag.Y Then DeactivateModel( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
		Shape.AttachModel( New TLaunchFireworks )
   End Method
End Type



Type TLaunchFireworks Extends LTBehaviorModel
   Const TotalFireworks:Int = 5
   Const ExplodingSpeed:Double = 0.2
   
   Global Fireworks:TSound = TSound.Load( "media\Fireworks.ogg", False )
   
   Field Firework:LTSprite
   Field FireworksLeft:Int = TotalFireworks
   Field FireworkExplodingTime:Double
   Field CastleFlag:LTShape = Game.Level.FindShape( "CastleFlagSpace" )
   
   Method Activate( Shape:LTShape )
       Firework = New LTSprite
       Firework.SetSize( 1.0, 1.0 )
       Firework.Visualizer = TFireball.Explosion
       Game.Level.AddLast( Firework )
   End Method
   
   Method ApplyTo( Shape:LTShape )
       If Game.Time >= FireworkExplodingTime + ExplodingSpeed * 3 Then
           If FireworksLeft = 0 Then
               Game.Level.Remove( Firework )
           Else
               Firework.SetCoords( CastleFlag.X + Rnd( -5.0, 5.0 ), CastleFlag.Y - Rnd( 5.0 ) )
               Fireworks.Play()
               FireworkExplodingTime = Game.Time
               FireworksLeft :- 1
           End If
       Else
           Firework.Animate( ExplodingSpeed, , , FireworkExplodingTime )
       End If
   End Method
End Type



Type TTimeToScore Extends LTBehaviorModel
   Const TimeToScoreSpeed:Double = 0.02
   
   Field LastTimeToScoreSwap:Double
   
   Method ApplyTo( Shape:LTShape )
       If Game.TimeLeft > 0 And Game.Time > LastTimeToScoreSwap + TimeToScoreSpeed Then
           Game.TimeLeft :- 1
           Game.Score :+ 50
           LastTimeToScoreSwap = Game.Time
       End If
   End Method
End Type