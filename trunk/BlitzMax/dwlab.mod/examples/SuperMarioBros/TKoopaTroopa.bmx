Type TKoopaTroopa Extends TEnemy
   Const Shell:Int = 2
   Const ShellSpeed:Double = 15.0
   Const WalkingAnimationSpeed:Double = 0.3
   
   Field ShellFrame:LTAnimationModel = LTAnimationModel.Create( True, 1, 1, 2 )
   Field WalkingAnimation:LTAnimationModel = LTAnimationModel.Create( True, WalkingAnimationSpeed, 2 )
   
   Method Init()
	   Local AnimationStack:LTModelStack = New LTModelStack
	   AttachModel( AnimationStack )
	   AnimationStack.Add( ShellFrame, False )
	   AnimationStack.Add( WalkingAnimation )
	   
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
       TStomped.Stomp.Play()
       If ShellFrame.Active Then
           If DX = 0.0 Then
               Push()
           Else
               DX = 0.0
           End If
       Else
           ShellFrame.Active = True
		   Frame = TKoopaTroopa.Shell
		   DX = 0.0
       End If
   End Method
   
   Method Push()
		DX = ShellSpeed * Sgn( X - Game.Mario.X )
   End Method
   
   Method Touch( Sprite:LTSprite )
       If ShellFrame.Active And DX Then Sprite.AttachModel( New TKicked )
   End Method
End Type