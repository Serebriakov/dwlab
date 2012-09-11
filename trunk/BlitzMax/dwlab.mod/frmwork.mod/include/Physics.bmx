'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

For Local N:Int = 0 To 3
	If N < 2 Then LTWedge.ServiceLines[ N ] = New LTLine
	LTWedge.ServicePivots[ N ] = LTSprite.FromShape( 0, 0, 0, 0, LTSprite.Pivot )
Next

Type LTWedge
	Global ServiceLines:LTLine[] = New LTLine[ 2 ]
	Global ServicePivots:LTSprite[] = New LTSprite[ 4 ]
	Global ServiceOval1:LTSprite = LTSprite.FromShapeType( LTSprite.Oval )
	Global ServiceOval2:LTSprite = LTSprite.FromShapeType( LTSprite.Oval )

	
	
	Function PivotAndOval( Pivot:LTSprite, Oval:LTSprite, DX:Double Var, DY:Double Var )
		Oval = Oval.ToCircle( Pivot, ServiceOval1 )
		Local K:Double = 0.5 * Oval.Width / Oval.DistanceTo( Pivot ) - 1.0
		DX = ( Pivot.X - Oval.X ) * K
		DY = ( Pivot.Y - Oval.Y ) * K
	End Function



	Function PivotAndRectangle( Pivot:LTSprite, Rectangle:LTSprite, DX:Double Var, DY:Double Var )
		if Abs( Pivot.Y - Rectangle.Y ) * Rectangle.Width >= Abs( Pivot.X - Rectangle.X ) * Rectangle.Height Then
			DY = Rectangle.Y + 0.5 * Rectangle.Height * Sgn( Pivot.Y - Rectangle.Y ) - Pivot.Y
		Else
			DX = Rectangle.X + 0.5 * Rectangle.Width * Sgn( Pivot.X - Rectangle.X ) - Pivot.X
		End If
	End Function
	
	
	
	Function PivotAndTriangle( Pivot:LTSprite, Triangle:LTSprite, DX:Double Var, DY:Double Var )
		DY = ServiceLines[ 0 ].GetY( Pivot.X ) - Pivot.Y
		
		Local DX1:Double, DY1:Double
		PivotAndRectangle( Pivot, Triangle, DX1, DY1 )
		If L_Distance2( DX1, DY1 ) < DY * DY Then
			DX = DX1
			DY = DY1
		End If
	End Function
	
	
	
	Function OvalAndOval( Oval1:LTSprite, Oval2:LTSprite, DX:Double Var, DY:Double Var )
		Oval1 = Oval1.ToCircle( Oval2, ServiceOval1 )
		Oval2 = Oval2.ToCircle( Oval1, ServiceOval2  )
		Local K:Double = 0.5 * ( Oval1.Width + Oval2.Width ) / Oval1.DistanceTo( Oval2 ) - 1.0
		DX = ( Oval1.X - Oval2.X ) * K
		DY = ( Oval1.Y - Oval2.Y ) * K
	End Function



	Function OvalAndRectangle( Oval:LTSprite, Rectangle:LTSprite, DX:Double Var, DY:Double Var )
		Local A:Int = ( Abs( Oval.Y - Rectangle.Y ) * Rectangle.Width >= Abs( Oval.X - Rectangle.X ) * Rectangle.Height )
		If ( Oval.X > Rectangle.LeftX() And Oval.X < Rectangle.RightX() ) And A Then
			DX = 0
			DY = ( 0.5 * ( Rectangle.Height + Oval.Height ) - Abs( Rectangle.Y - Oval.Y ) ) * Sgn( Oval.Y - Rectangle.Y )
		ElseIf Oval.Y > Rectangle.TopY() And Oval.Y < Rectangle.BottomY() And Not A Then
			DX = ( 0.5 * ( Rectangle.Width + Oval.Width ) - Abs( Rectangle.X - Oval.X ) ) * Sgn( Oval.X - Rectangle.X )
			DY = 0
		Else
			ServicePivots[ 0 ].X = Rectangle.X + 0.5 * Rectangle.Width * Sgn( Oval.X - Rectangle.X )
			ServicePivots[ 0 ].Y = Rectangle.Y + 0.5 * Rectangle.Height * Sgn( Oval.Y - Rectangle.Y )
			Oval = Oval.ToCircle( ServicePivots[ 0 ], ServiceOval1 )
			Local K:Double = 1.0 - 0.5 * Oval.Width / Oval.DistanceTo( ServicePivots[ 0 ] )
			DX = ( ServicePivots[ 0 ].X - Oval.X ) * K
			DY = ( ServicePivots[ 0 ].Y - Oval.Y ) * K
		End If
	End Function
	
	
	
	Function OvalAndTriangle( Oval:LTSprite, Triangle:LTSprite, DX:Double Var, DY:Double Var )
		Triangle.GetRightAngleVertex( ServicePivots[ 2 ] )
		Triangle.GetOtherVertices( ServicePivots[ 0 ], ServicePivots[ 1 ] )
		ServiceOval1 = Oval.ToCircle( ServicePivots[ 2 ], ServiceOval1 )
		Local VDistance:Double = 0.5 * L_Distance( Triangle.Width, Triangle.Height ) * ServiceOval1.Width / Triangle.Width
		Local DHeight:Double = 0.5 * ( Oval.Height - ServiceOval1.Height )
		Local DDX:Double = 0.5 * ServiceOval1.Width / VDistance * L_Cathetus( VDistance, 0.5 * ServiceOval1.Width )
		Local Dir:Int = -1
		If Triangle.ShapeType = LTSprite.BottomLeftTriangle Or Triangle.ShapeType = LTSprite.BottomRightTriangle Then Dir = 1
		If Triangle.ShapeType = LTSprite.TopRightTriangle Or Triangle.ShapeType = LTSprite.BottomRightTriangle Then DDX = -DDX
		If ServiceOval1.X < Triangle.LeftX() + DDX Then
			DY = ServicePivots[ 0 ].Y - Dir * L_Cathetus( ServiceOval1.Width * 0.5, ServiceOval1.X - ServicePivots[ 0 ].X ) - ServiceOval1.Y
		ElseIf ServiceOval1.X > Triangle.RightX() + DDX Then
			DY = ServicePivots[ 1 ].Y - Dir * L_Cathetus( ServiceOval1.Width * 0.5, ServiceOval1.X - ServicePivots[ 1 ].X ) - ServiceOval1.Y
		Else
			DY = ServiceLines[ 0 ].GetY( ServiceOval1.X ) - Dir * ( VDistance + DHeight ) - Oval.Y
		End If
	
		Local DX1:Double, DY1:Double
		OvalAndRectangle( Oval, Triangle, DX1, DY1 )
		If L_Distance2( DX1, DY1 ) < DY * DY Then
			DX = DX1
			DY = DY1
		End If
	End Function
	
	
	
	Function RectangleAndRectangle( Rectangle1:LTSprite, Rectangle2:LTSprite, DX:Double Var, DY:Double Var )
		DX = 0.5 * ( Rectangle1.Width + Rectangle2.Width ) - Abs( Rectangle1.X - Rectangle2.X )
		DY = 0.5 * ( Rectangle1.Height + Rectangle2.Height ) - Abs( Rectangle1.Y - Rectangle2.Y )
		
		If DX < DY Then
			DX :* Sgn( Rectangle1.X - Rectangle2.X )
			DY = 0
		Else
			DX = 0
			DY :* Sgn( Rectangle1.Y - Rectangle2.Y )
		End If
	End Function
	
	
	
	Function RectangleAndTriangle( Rectangle:LTSprite, Triangle:LTSprite, DX:Double Var, DY:Double Var )
		Local X:Double
		If Triangle.ShapeType = LTSprite.TopLeftTriangle Or Triangle.ShapeType = LTSprite.BottomLeftTriangle Then
			X = Rectangle.LeftX()
		Else
			X = Rectangle.RightX()
		End If

		Triangle.GetHypotenuse( ServiceLines[ 0 ] )
		If Triangle.ShapeType = LTSprite.TopLeftTriangle Or Triangle.ShapeType = LTSprite.TopRightTriangle
			DY = Min( ServiceLines[ 0 ].GetY( X ), Triangle.BottomY() ) - Rectangle.TopY()
		Else
			DY = Max( ServiceLines[ 0 ].GetY( X ), Triangle.TopY() ) - Rectangle.BottomY()
		End If
		
		Local DX1:Double, DY1:Double
		RectangleAndRectangle( Rectangle, Triangle, DX1, DY1 )
		If L_Distance2( DX1, DY1 ) < DY * DY Then
			DX = DX1
			DY = DY1
		End If
	End Function
	
	
	
	Function PopAngle( Triangle1:LTSprite, Triangle2:LTSprite, DY:Double Var )
		Triangle2.GetRightAngleVertex( ServicePivots[ 0 ] )
		Triangle2.GetHypotenuse( ServiceLines[ 0 ] )
		Triangle1.GetOtherVertices( ServicePivots[ 1 ], ServicePivots[ 2 ] )
		Local O:Int = ServiceLines[ 0 ].PivotOrientation( ServicePivots[ 0 ] )
		For Local N:Int = 1 To 2
			If O = ServiceLines[ 0 ].PivotOrientation( ServicePivots[ N ] ) Then
				If L_DoubleInLimits( ServicePivots[ N ].X, Triangle2.LeftX(), Triangle2.RightX() ) Then
					DY = Max( DY, Abs( ServiceLines[ 0 ].GetY( ServicePivots[ N ].X ) - ServicePivots[ N ].Y ) )
				End If
			End If
		Next
	End Function
	
	
	
	Function TriangleAndTriangle( Triangle1:LTSprite, Triangle2:LTSprite, DX:Double Var, DY:Double Var )
		Local DX1:Double, DY1:Double
		RectangleAndTriangle( Triangle1, Triangle2, DX1, DY1 )
		Local D1:Double = L_Distance2( DX1, DY1 )
		
		Local DX2:Double, DY2:Double
		RectangleAndTriangle( Triangle2, Triangle1, DX2, DY2 )
		Local D2:Double = L_Distance2( DX2, DY2 )
		
		Repeat
			Select Triangle1.ShapeType
				Case LTSprite.TopLeftTriangle
					if Triangle2.ShapeType <> LTSprite.BottomRightTriangle Then Exit
				Case LTSprite.TopRightTriangle
					if Triangle2.ShapeType <> LTSprite.BottomLeftTriangle Then Exit
				Case LTSprite.BottomLeftTriangle
					if Triangle2.ShapeType <> LTSprite.TopRightTriangle Then Exit
				Case LTSprite.BottomRightTriangle
					if Triangle2.ShapeType <> LTSprite.TopLeftTriangle Then Exit
			End Select
		
			Local DY3:Double = 0
			PopAngle( Triangle1, Triangle2, DY3 )
			PopAngle( Triangle2, Triangle1, DY3 )
			If DY3 = 0 Then Exit
			
			Local DY32:Double = DY3 * DY3
			If DY32 < D1 And DY32 < D2 Then
				Triangle1.GetRightAngleVertex( ServicePivots[ 0 ] )
				Triangle2.GetRightAngleVertex( ServicePivots[ 1 ] )
				DX = 0
				DY = DY3 * Sgn( ServicePivots[ 0 ].Y - ServicePivots[ 1 ].Y )
				Return
			Else
				Exit
			End If
		Forever
		
		If D1 < D2 Then
			DX = DX1
			DY = DY1
		Else
			DX = -DX2
			DY = -DY2
		End If
	End Function
	
	
	
	Function Separate( Pivot1:LTSprite, Pivot2:LTSprite, DX:Double, DY:Double, Pivot1MovingResistance:Double, Pivot2MovingResistance:Double )
		Local K1:Double, K2:Double
		
		If Pivot1MovingResistance < 0 then
			If Pivot2MovingResistance < 0 Then
				Return
			End If
			Pivot1MovingResistance = 1.0
			Pivot1MovingResistance = 0.0
		ElseIf Pivot2MovingResistance < 0 Then
			Pivot1MovingResistance = 0.0
			Pivot2MovingResistance = 1.0		
		End If
		
		Local MovingResistanceSum:Double = Pivot1MovingResistance + Pivot2MovingResistance
		If MovingResistanceSum Then
			K1 = Pivot2MovingResistance / MovingResistanceSum
			K2 = Pivot1MovingResistance / MovingResistanceSum
		Else
			K1 = 0.5
			K2 = 0.5
		End If
		
		IF K1 <> 0.0 Then Pivot1.AlterCoords( K1 * DX, K1 * DY )
		IF K2 <> 0.0 Then Pivot2.AlterCoords( -K2 * DX, -K2 * DY )
	End Function
End Type