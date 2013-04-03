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
   Field Mario:TMario
   Field HUD:LTLayer
   Field LevelCamera:LTCamera = LTCamera.Create( 960, 720, 48.0 )
   Field HUDCamera:LTCamera = LTCamera.Create( 960, 720, 48.0 )   
   Field Font:LTBitmapFont = LTBitmapFont.FromFile( "media/font.png", 32, 127, 16 )
   
   Method Init()
       L_InitGraphics( 960, 720, 48.0 ) ' initialization of graphics engine with 960x720 resolution and 48 pixels per one unit (tile will be stretched to 48x48 pixels)
       World = LTWorld.FromFile( "world.lw" ) ' loading the world into memory
	   L_Music.Preload( "media\music1intro.ogg", "intro1" )
	   L_Music.Preload( "media\music1.ogg", "1" )
	   L_Music.Preload( "media\MarioDie.ogg", "dying" )
	   InitLevel()
	   LoadAndInitLayer( HUD, LTLayer( LTWorld.FromFile( "hud.lw" ).FindShapeWithType( "LTLayer" ) ) )
   End Method
   
   Method InitLevel()
      Local LevelsQuantity:Int = World.Children.Count()
      Levels = New LTLayer[ LevelsQuantity ]

      Mario = New TMario
      Mario.SetWidth( 0.8 )
      Mario.Visualizer = New LTVisualizer.FromImage( TMario.SmallMario )
      Mario.Visualizer.XScale = 1.0 / 0.8
     
      For Local N:Int = 0 Until LevelsQuantity
		 Levels[ N ] = LoadLayer( LTLayer( World.FindShapeWithParameter( "num", N, "LTLayer" ) ) )
         Levels[ N ].AddLast( Mario )
		 MovingObjects = LTSpriteMap( Levels[ N ].FindShapeWithType( "LTSpriteMap" ) )
		 Levels[ N ].Init()
      Next
     
      SwitchToLevel( 0 )
    End Method
   
    Method SwitchToLevel( Num:Int, PointNum:Int = 0 )
       Level = Levels[ Num ]
       TileMap = LTTileMap( Level.FindShapeWithType( "TTiles" ) )
       MovingObjects = LTSpriteMap( Level.FindShapeWithType( "LTSpriteMap" ) )
       Mario.JumpTo( Level.FindShapeWithParameter( "num", PointNum, "TStart" ) )
	   L_Music.ClearMusic()
	   L_Music.Add( "intro1" )
	   L_Music.Add( "1", True )
	   L_Music.Start()
   End Method
      
   Method Logic()
       If Not Level.Active Then Level.FindShapeWithType( "TMario" ).Act()
       Level.Act()
       If KeyHit( Key_Escape ) Then End ' exit after pressing Escape
	   L_Music.Manage()
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