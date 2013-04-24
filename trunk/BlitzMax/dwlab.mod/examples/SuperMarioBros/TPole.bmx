Type TPole Extends LTVectorSprite
   Method Act()
       If CollidesWithSprite( Game.Mario ) Then Game.Mario.AttachModel( New TSlidingDownThePole )
   End Method
End Type