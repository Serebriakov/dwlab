'
' Color Lines - Digital Wizard's Lab example
' Copyright (C) 2011, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Type TExplosion
  Const MaxRadius:Int = 3
  Const CircleWidth:Double = 0.12
  Const ParticleSize:Double = 0.18
  Const DSize:Double = 0.05
  Const ParticleDensity:Double = 6.0
  Const Shift:Double = 0.03
  Const DShift:Double = 0.1
  Const ExplosionK:Double = 18.0
  
  Function Create( X:Int, Y:Int )
    Local TileNum:Int = Game.Level.GetTile( X, Y )
    If TileNum = 0 Then Return
    For Local Radius:Double = 1 To MaxRadius
      Local Angle:Double = 0
      While Angle < 360.0
        Local Sprite:LTVectorSprite = New LTVectorSprite
        Local DX:Double = Radius * CircleWidth * Cos( Angle )
        Local DY:Double = Radius * CircleWidth * Sin( Angle )
        Sprite.PositionOnTilemap( Game.Level, DX + X + Rnd( -Shift, Shift ), DY + Y + Rnd( -Shift, Shift ) )
        Sprite.SetSize( ParticleSize + Rnd( -DSize, DSize ), ParticleSize + Rnd( -DSize, DSize ) )
        Sprite.DX = ( DX + Rnd( -DShift, DShift ) ) * ExplosionK
        Sprite.DY = ( DY + Rnd( -DShift, DShift ) ) * ExplosionK
        Sprite.AttachModel( New TMoveParticle )
        Sprite.Visualizer.Image = Game.Level.TileSet.Image
        Sprite.Frame = TileNum
        Game.Particles.AddLast( Sprite )
        Angle :+ 360.0 / ParticleDensity / Radius
      WEnd
    Next
    Game.Level.SetTile( X, Y, TVisualizer.Empty )
    Game.Score :+ 1
  End Function
End Type

Type TMoveParticle Extends LTBehaviorModel
  Const Gravity:Double = 12.0

  Method ApplyTo( Shape:LTShape )
    Local Particle:LTVectorSprite = LTVectorSprite( Shape )
    Particle.DY :+ Game.PerSecond( Gravity )
    Particle.MoveForward()
    If Particle.TopY() > L_CurrentCamera.BottomY() Then Game.Particles.Remove( Shape )
  End Method
End Type
