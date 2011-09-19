'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Global L_CollisionChecks:Int
Global L_TilesDisplayed:Int
Global L_SpritesDisplayed:Int
Global L_SpritesActed:Int
Global L_SpriteActed:Int

Global L_DeltaTime:Double

Rem
bbdoc: Class for main project and subprojects.
End Rem
Type LTProject Extends LTObject
	Rem
	bbdoc: Quantity of logic frames per second.
	about: See also: #Logic
	End Rem
	Field LogicFPS:Double = 75
	
	Rem
	bbdoc: Minimal frames per second
	about: Game will start to go slower if this threshold will be reached.
	
	See also: #Render
	End Rem
	Field MinFPS:Double = 15
	
	Rem
	bbdoc: Current frames per second quantity.
	about: See also: #Render
	End Rem
	Field FPS:Int
	
	Rem
	bbdoc: Current logic frame number.
	End Rem
	Field Pass:Int
	
	Rem
	bbdoc: Current game time in seconds (starts from 0).
	about: See also: #PerSecond
	End Rem
	Field Time:Double
	
	Field CurrentPause:LTPause
	
	Rem
	bbdoc: Exit flag.
	about: Set it to True to exit project.
	End Rem
	Field Exiting:Int

	Rem
	bbdoc: Flipping flag.
	about: If set to True then Cls will be performed before Render() and Flip will be performed after Render().
	End Rem
	Field Flipping:Int = True
	
	
	Rem
	bbdoc: Loads and initializes layer and all its child objects from previously loaded world.
	End Rem
	Method LoadAndInitLayer( NewLayer:LTLayer Var, Layer:LTLayer )
		NewLayer = LoadLayer( Layer )
		NewLayer.Init()
	End Method
	
	
	
	Rem
	bbdoc: Loads layer from world.
	End Rem
	Method LoadLayer:LTLayer( Layer:LTLayer )
		Local NewLayer:LTLayer = LTLayer( CreateShape( Layer ) )
		For Local Shape:LTShape = Eachin Layer.Children
			Local NewShape:LTShape
			Local ChildLayer:LTLayer = LTLayer( Shape )
			If ChildLayer Then
				NewShape = LoadLayer( ChildLayer )
			Else
				Local SpriteMap:LTSpriteMap = LTSpriteMap( Shape )
				If SpriteMap Then
					Local NewSpriteMap:LTSpriteMap = LTSpriteMap( CreateShape( Shape ) )
					For Local ChildSprite:LTSprite = Eachin SpriteMap.Sprites.Keys()
						NewSpriteMap.InsertSprite( LTSprite( CreateShape( ChildSprite ) ) )
					Next
					NewShape = NewSpriteMap
				Else
					NewShape = CreateShape( Shape )
				End If
			End If
			NewLayer.AddLast( NewShape )
		Next
		Return NewLayer
	End Method
	
	
	
	Method CreateShape:LTShape( Shape:LTShape )
		Local TypeName:String = Shape.GetParameter( "class" )
		Local NewShape:LTShape
		If TypeName Then 
			NewShape = LTShape( L_GetTypeID( TypeName ).NewObject() )
		Else
			NewShape = LTShape( TTypeId.ForObject( Shape ).NewObject() )
		End If
		Shape.CopyTo( NewShape )
		Return NewShape
	End Method
	
	
	Rem
	bbdoc: Initialization method.
	about: Fill it with project initialization commands.
	
	See also: #DeInit
	End Rem
	Method Init()
	End Method
  
  
	Rem
	bbdoc: Rendering method.
	about: Fill it with objects drawing commands. Will be executed as many times as possible, while keeping logic frame rate.
	
	See also: #MinFPS, #FPS
	End Rem
	Method Render()
	End Method
	
	
	
	Rem
	bbdoc: Logic method. 
	about: Fill it with project mechanics commands. Will be executed "LogicFPS" times per second.
	
	See also: #LogicFPS
	End Rem
	Method Logic()
	End Method
	
	
	Rem
	bbdoc: Deinitialization method.
	about: It will be executed before exit.
	
	See also: #Init
	End Rem
	Method DeInit()
	End Method
	
	
	
	Rem
	bbdoc: Executes the project.
	End Rem
	Method Execute()
		FlushKeys
		FlushMouse
	    
		Exiting = False
		Pass = 1
		L_DeltaTime = 0
				
		Init()
		
		Time = 0.0
		Local StartTime:Int = MilliSecs()
		
		Local RealTime:Double = 0
		Local LastRenderTime:Double = 0
		Local MaxRenderPeriod:Double = 1.0 / MinFPS
		Local FPSCount:Int
		Local FPSTime:Int
		
		L_DeltaTime = 1.0 / LogicFPS
	    
		Repeat
			Time :+  1.0 / LogicFPS
			
			If CurrentPause Then
				CurrentPause.Update()
			Else
				?debug
				L_CollisionChecks = 0
				L_SpritesActed = 0
				?
				
				Logic()
				If Exiting Then Exit
			End If
		
			Repeat
				RealTime = 0.001 * ( Millisecs() - StartTime )
				If RealTime >= Time And ( RealTime - LastRenderTime ) < MaxRenderPeriod Then Exit
				
				If Flipping Then Cls
				
				?debug
				L_SpritesDisplayed = 0
				L_TilesDisplayed = 0
				?
				
				Render()
				If CurrentPause Then CurrentPause.Render()
				If Flipping Then Flip( False )
		      
				LastRenderTime = 0.001 * ( Millisecs() - StartTime )
				FPSCount :+ 1
			Forever
	      
			If Millisecs() >= 1000 + FPSTime Then
				FPS = FPSCount
				FPSCount = 0
				FPSTime = Millisecs()
			End If
			
			PollSystem()
			Pass :+ 1
		Forever
		
		DeInit()
	End Method
	
	
	
	Rem
	bbdoc: Converts value second to value per logic frame.
	returns: Value for logic frame using given per second value.
	about: For example, can return coordinate increment for speed per second.
	End Rem
	Method PerSecond:Double( Value:Double )
		Return Value * L_DeltaTime
	End Method
	
	
	
	Rem
	bbdoc: Draws various debugging information on screen.
	End Rem
	Method ShowDebugInfo()
		DrawText( "FPS: " + FPS, 0, 0 )
		DrawText( "Memory: " + Int( GCMemAlloced() / 1024 ) + "kb", 0, 16 )
		
		?debug
		DrawText( "Collision checks: " + L_CollisionChecks, 0, 32 )
		DrawText( "Tiles displayed: " + L_TilesDisplayed, 0, 48 )
		DrawText( "Sprites displayed: " + L_SpritesDisplayed, 0, 64 )
		DrawText( "Sprites acted: " + L_SpritesActed, 0, 80 )
		?
	End Method
	
	
	
	Rem
	bbdoc: Applies pause object to the project (to switch to pause mode).
	End Rem
	Method ApplyPause( NewPause:LTPause, Key:Int )
		NewPause.Project = Self
		NewPause.Key = Key
		NewPause.PreviousPause = CurrentPause
		CurrentPause = NewPause
	End Method
End Type