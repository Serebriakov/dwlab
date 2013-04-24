Type TExit Extends LTVectorSprite
  Method Act()
      If Overlaps( Game.Mario ) And TMario.OnLand Then
          If DY > 0.0 Then
              If KeyDown( Key_Down ) Then Game.Mario.AttachModel( TEnteringVerticalPipe.Create( Self ) )
          Else
             Game.Mario.AttachModel( TEnteringHorizontalPipe.Create( Self ) )
          End If
      End If
  End Method
End Type



Type TEnteringVerticalPipe Extends LTBehaviorModel
   Const Speed:Double = 2.0
   
   Field ExitSprite:TExit
   
   Function Create:TEnteringVerticalPipe( ExitSprite:TExit )
       Local Enterting:TEnteringVerticalPipe = New TEnteringVerticalPipe
       Enterting.ExitSprite = ExitSprite
       Return Enterting
   End Function
   
   Method Activate( Shape:LTShape )
       Shape.DeactivateAllModels()
	   Game.Level.Active = False
       Shape.LimitByWindowShape( ExitSprite )
       TMario.Pipe.Play()
       If TMario.Big Then Game.Mario.Frame = 7
   End Method
   
   Method ApplyTo( Shape:LTShape )
       Shape.Move( 0.0, Speed )
       If Shape.Y >= ExitSprite.Y + 2.0 Then DeactivateModel( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
       Game.Mario.Frame = 4
       Shape.RemoveWindowLimit()
       Shape.ActivateAllModels()
	   Game.Level.Active = True
       Game.SwitchToLevel( ExitSprite.GetParameter( "level" ).ToInt(), ExitSprite.GetParameter( "start" ).ToInt() )
   End Method
End Type



Type TEnteringHorizontalPipe Extends LTBehaviorModel
   Const Speed:Double = 1.0
   Const AnimationSpeed:Double = 0.15
   
   Field ExitSprite:TExit
   
   Function Create:TEnteringHorizontalPipe( ExitSprite:TExit )
       Local Enterting:TEnteringHorizontalPipe = New TEnteringHorizontalPipe
       Enterting.ExitSprite = ExitSprite
       Return Enterting
   End Function
   
   Method Activate( Shape:LTShape )
       Shape.DeactivateAllModels()
       Game.Level.Active = False
       Shape.LimitByWindowShape( ExitSprite )
       TMario.Pipe.Play()
   End Method
   
   Method ApplyTo( Shape:LTShape )
       Shape.Move( ExitSprite.DX * Speed, 0.0 )
       Game.Mario.Animate( AnimationSpeed, 3, 1 )
       If ( ExitSprite.DX > 0.0 And Shape.X >= ExitSprite.X + 1.0 ) Or ( ExitSprite.DX < 0.0 And Shape.X <= ExitSprite.X - 1.0 ) Then DeactivateModel( Shape )
   End Method
   
   Method Deactivate( Shape:LTShape )
       Game.Mario.Frame = 4
       Shape.RemoveWindowLimit()
       Shape.ActivateAllModels()
       Game.Level.Active = True
       Game.SwitchToLevel( ExitSprite.GetParameter( "level" ).ToInt(), ExitSprite.GetParameter( "start" ).ToInt() )
   End Method
End Type