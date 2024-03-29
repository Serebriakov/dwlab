Type TGravity Extends LTBehaviorModel
   Method ApplyTo( Shape:LTShape )
       LTVectorSprite( Shape ).DY :+ Game.PerSecond( Game.Gravity )
   End Method
End Type



Type TCollisionWithWall Extends LTSpriteAndTileCollisionHandler
	Global Instance:TCollisionWithWall = New TCollisionWithWall
	
   Method HandleCollision( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionSprite:LTSprite )
       Sprite.PushFromTile( TileMap, TileX, TileY )
       LTVectorSprite( Sprite ).DX :* -1
	   If TKoopaTroopa( Sprite ) Then TBlock.Bump.Play()
   End Method
End Type



Type TCollisionWithFloor Extends LTSpriteAndTileCollisionHandler
	Global Instance:TCollisionWithFloor = New TCollisionWithFloor
	
   Method HandleCollision( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionSprite:LTSprite )
       Sprite.PushFromTile( TileMap, TileX, TileY )
	   If TStarMan( Sprite ) And LTVectorSprite( Sprite ).DY > 0 Then
	   		LTVectorSprite( Sprite ).DY = TStarMan.JumpStrength
	   Else
	   		LTVectorSprite( Sprite ).DY = 0
	   End If
   End Method
End Type



Type TSpritesHorizontalCollision Extends LTSpriteCollisionHandler
	Global Instance:TSpritesHorizontalCollision = New TSpritesHorizontalCollision
	
   Method HandleCollision( Sprite1:LTSprite, Sprite2:LTSprite )
       If TKoopaTroopa( Sprite1 ) Then
	   		TKoopaTroopa( Sprite1 ).Touch( Sprite2 )
			Return
	   End If
       Sprite1.PushFromSprite( Sprite2 )
       LTVectorSprite( Sprite1 ).DX :* -1
   End Method
End Type



Type TSpritesVerticalCollision Extends LTSpriteCollisionHandler
	Global Instance:TSpritesVerticalCollision = New TSpritesVerticalCollision
	
   Method HandleCollision( Sprite1:LTSprite, Sprite2:LTSprite )
       If TKoopaTroopa( Sprite1 ) Then
	   		TKoopaTroopa( Sprite1 ).Touch( Sprite2 )
			Return
	   End If
	   Sprite1.PushFromSprite( Sprite2 )
   End Method
End Type



Type TRemoveIfOutside Extends LTBehaviorModel
   Method ApplyTo( Shape:LTShape )
       Local Sprite:LTSprite = LTSprite( Shape )
       If Sprite.TopY() > Game.Tilemap.BottomY() Then
           Game.Level.Remove( Sprite )
           Game.MovingObjects.RemoveSprite( Sprite )
       End If
   End Method
End Type