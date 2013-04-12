Type TGame Extends LTProject
   Const Gravity:Double = 32.0
	
   Field Score:Int
   Field Lives:Int = 3
   Field Coins:Int
   Field TimeLeft:Int
   
   Field World:LTWorld ' this field will store our world created in editor
   Field Level:LTLayer ' this field will store layer loaded from the world
   Field Tilemap:LTTileMap
   Field MovingObjects:LTSpriteMap
   Field Levels:LTLayer[]
   Field Mario:TMario = New TMario
   Field HUD:LTLayer
   Field LevelCamera:LTCamera = LTCamera.Create( 960, 720, 48.0 )
   Field HUDCamera:LTCamera = LTCamera.Create( 960, 720, 48.0 )   
   Field Font:LTBitmapFont = LTBitmapFont.FromFile( "media/font.png", 32, 127, 16 )
   Field MusicRate:Double
   Field LivesScreen:TLives = New TLives

   Method Init()
       L_InitGraphics( 960, 720, 48.0 ) ' initialization of graphics engine with 960x720 resolution and 48 pixels per one unit (tile will be stretched to 48x48 pixels)
       World = LTWorld.FromFile( "world.lw" ) ' loading the world into memory
	   L_Music.Preload( "media\music1intro.ogg", "intro1" )
	   L_Music.Preload( "media\music1.ogg", "1" )
	   L_Music.Preload( "media\music2.ogg", "2" )
	   L_Music.Preload( "media\MarioDie.ogg", "dying" )
	   L_Music.Preload( "media\Warning.ogg", "warning" )
	   LoadAndInitLayer( HUD, LTLayer( LTWorld.FromFile( "hud.lw" ).FindShapeWithType( "LTLayer" ) ) )
	   InitLevel()
   End Method
   
   Method InitLevel()
      Local LevelsQuantity:Int = World.Children.Count()
      Levels = New LTLayer[ LevelsQuantity ]

      Mario.SetWidth( 0.8 )
      Mario.Visualizer = New LTVisualizer.FromImage( TMario.SmallMario )
      Mario.Visualizer.XScale = 1.0 / 0.8
     
      For Local N:Int = 0 Until LevelsQuantity
		 Levels[ N ] = LoadLayer( LTLayer( World.FindShapeWithParameter( "num", N, "LTLayer" ) ) )
	     TileMap = LTTileMap( Levels[ N ].FindShapeWithType( "TTiles" ) )
		 MovingObjects = LTSpriteMap( Levels[ N ].FindShapeWithType( "LTSpriteMap" ) )
         Levels[ N ].AddLast( Mario )
		 Levels[ N ].Init()
      Next
     
      MusicRate = 1.0
	  TTime.Init()
	  
	  LivesScreen.Execute()
      SwitchToLevel( 0 )
    End Method
   
    Method SwitchToLevel( Num:Int, PointNum:Int = 0 )
       Level = Levels[ Num ]
       TileMap = LTTileMap( Level.FindShapeWithType( "TTiles" ) )
       MovingObjects = LTSpriteMap( Level.FindShapeWithType( "LTSpriteMap" ) )
       Mario.JumpTo( Level.FindShapeWithParameter( "num", PointNum, "TStart" ) )
	   Mario.DY = 0
	   Mario.Init()
	   L_Music.ClearMusic()
	   StartMusic()
   End Method
   
   Method StartMusic()
	   Select Level.GetParameter( "music" ).ToInt()
	   	Case 1
	   		L_Music.Add( "intro1", , MusicRate )
			L_Music.Add( "1", True, MusicRate )
		Case 2
			L_Music.Add( "2", True, MusicRate )
	   End Select
	   L_Music.Start()
   End Method
      
   Method Logic()
       If Not Level.Active Then Level.FindShapeWithType( "TMario" ).Act()
       Level.Act()
       If KeyHit( Key_Escape ) Then End ' exit after pressing Escape
	   L_Music.Manage()
	   TTime.Act()
   End Method
   
    Method Render()
       L_CurrentCamera = LevelCamera
       Level.Draw()
       L_CurrentCamera = HUDCamera
       HUD.Draw()
       L_CurrentCamera = LevelCamera
       ShowDebugInfo()
   End Method
End Type