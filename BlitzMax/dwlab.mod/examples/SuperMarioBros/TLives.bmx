Type TLives Extends LTProject
   Method Init()
       Game.Mario.JumpTo( Game.HUD.FindShapeWithType( "TBackground" ) )
       Game.Mario.AlterCoords( -0.5, 0.0 )
   End Method
   
   Method Logic()
       If GetChar() Then Exiting = True
   End Method
   
   Method Render()
       L_CurrentCamera = Game.HUDCamera
       Game.HUD.Draw()
       Game.Mario.Draw()
       
       Game.Font.Print( "X" + Game.Lives, Game.Mario.RightX(), Game.Mario.Y, 0.5, LTAlign.ToLeft, LTAlign.ToCenter )
       L_CurrentCamera = Game.LevelCamera
   End Method
End Type