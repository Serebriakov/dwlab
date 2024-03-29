'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Rem
bbdoc: Class for managing global Box2D functions
End Rem
Type LTBox2DPhysics
	Global Box2DWorld:b2World
	Global VelocityIterations:Int = 10
	Global PositionIterations:Int = 8
	
	
	
	Rem
	bbdoc: Box2D world initialisations function.
	about: Should be called for world object before manipulation with its Box2D objects.
	End Rem
	Function InitWorld( World:LTLayer )
		Local Margin:Double = 0
		If World.ParameterExists( "margin" ) Then Margin = World.GetParameter( "margin" ).ToDouble()
		
		Local Gravity:Float = 10.0
		If World.ParameterExists( "gravity" ) Then Gravity = World.GetParameter( "gravity" ).ToFloat()
		
		Box2DWorld = b2World.CreateWorld( b2AABB.CreateAABB( Vec2( World.Bounds.LeftX() - Margin, World.Bounds.TopY() - Margin ), ..
				Vec2( World.Bounds.RightX() + Margin, World.Bounds.BottomY() + Margin ) ), Vec2( 0, Gravity ), True )
		ProcessLayer( World )
	End Function
	
	
	
	Function ProcessLayer( Layer:LTLayer )
		For Local Shape:LTShape = Eachin Layer.Children
			Local ChildLayer:LTLayer = LTLayer( Shape )
			If ChildLayer Then
				ProcessLayer( ChildLayer )
			ElseIf Shape.Physics() Then
				Shape.Init()
			End If
		Next
	End Function
	
	
	
	Function Logic( TimeStep:Float )
		Box2DWorld.DoStep( TimeStep, VelocityIterations, PositionIterations )
		Box2DWorld.Validate()
	End Function
End Type
