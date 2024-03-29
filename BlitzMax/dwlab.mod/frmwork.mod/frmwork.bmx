'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

SuperStrict

Rem
bbdoc: Digital Wizard's Lab Framework
End Rem
Module dwlab.frmwork

ModuleInfo "Version: 1.5"
ModuleInfo "Author: Matt Merkulov"
ModuleInfo "License: Artistic License 2.0"
ModuleInfo "Modserver: DWLAB"


ModuleInfo "History: &nbsp; &nbsp; "
ModuleInfo "History: v1.5 (26.04.13)"
ModuleInfo "History: &nbsp; &nbsp; Everything is fixed, documented and updated."
ModuleInfo "History: v1.4.34 (18.04.13)"
ModuleInfo "History: &nbsp; &nbsp; Tilemaps now will be stored in one file with same name as project and lwbin extension."
ModuleInfo "History: v1.4.33 (04.01.13)"
ModuleInfo "History: &nbsp; &nbsp; Implemented layer alpha effect."
ModuleInfo "History: &nbsp; &nbsp; Added speed parameter to value changing behavior models."
ModuleInfo "History: v1.4.32 (05.12.12)"
ModuleInfo "History: &nbsp; &nbsp; Implemented sprite templates."
ModuleInfo "History: &nbsp; &nbsp; Object array serialization method is rewritten."
ModuleInfo "History: &nbsp; &nbsp; Tileset blocks serialization algorythm is rewritten."
ModuleInfo "History: v1.4.31 (30.11.12)"
ModuleInfo "History: &nbsp; &nbsp; Enchanced LTLayer's InsertSprite method."
ModuleInfo "History: &nbsp; &nbsp; Separated sprites interactions and sprite handlers."
ModuleInfo "History: &nbsp; &nbsp; Included sprite shape drawing in sprite handlers."
ModuleInfo "History: v1.4.30.1 (23.11.12)"
ModuleInfo "History: &nbsp; &nbsp; Fixed some bugs and tested all command examples."
ModuleInfo "History: v1.4.30 (19.11.12)"
ModuleInfo "History: &nbsp; &nbsp; Implemented object shape types and registrable interactions."
ModuleInfo "History: v1.4.29 (09.11.12)"
ModuleInfo "History: &nbsp; &nbsp; Tilemaps are now stored in binary format in special folder which is in same place as lw file."
ModuleInfo "History: &nbsp; &nbsp; Added tilemap loading error handler."
ModuleInfo "History: v1.4.28 (08.11.12)"
ModuleInfo "History: &nbsp; &nbsp; Added image loading error handler."
ModuleInfo "History: v1.4.27 (18.09.12)"
ModuleInfo "History: &nbsp; &nbsp; Added ManageStringObjectMap and ManageStringMap functions to LTXMLObject."
ModuleInfo "History: v1.4.26 (08.09.12)"
ModuleInfo "History: &nbsp; &nbsp; Enchanced LTWindowedVisualizer to support several vierwports."
ModuleInfo "History: &nbsp; &nbsp; Added LimitByWindowShapes() LTShape method."
ModuleInfo "History: v1.4.25 (01.09.12)"
ModuleInfo "History: &nbsp; &nbsp; Implemented mechanism of calculating loading time and updating loading bars."
ModuleInfo "History: v1.4.24 (31.08.12)"
ModuleInfo "History: &nbsp; &nbsp; Removed tile generator class and integrated it firmly into shape."
ModuleInfo "History: &nbsp; &nbsp; Fixed some parameter names (replaced underscopes to dashes)."
ModuleInfo "History: &nbsp; &nbsp; LTShape's PrintText() method now includes margins parameters."
ModuleInfo "History: &nbsp; &nbsp; DebugVisualizer now prints text inside sprite using parameters."
ModuleInfo "History: v1.4.23.1 (17.08.12)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bug in circular shape drawing."
ModuleInfo "History: v1.4.23 (13.08.12)"
ModuleInfo "History: &nbsp; &nbsp; Sprite maps are now array-list-based."
ModuleInfo "History: v1.4.22.3 (10.08.12)"
ModuleInfo "History: &nbsp; &nbsp; Added Scaling parameter to LTContourVisualizer's FromWidthAndHexColor() and FromWidthAndRGBColor() methods."
ModuleInfo "History: &nbsp; &nbsp; Added Scale and Scaling parameters to LTVisualizer's FromHexColor() and FromRGBColor() methods."
ModuleInfo "History: &nbsp; &nbsp; Fixed bug of Get/SetFacing() LTShape methods which appears while using LTWindowedVisualizer."
ModuleInfo "History: v1.4.22.2 (09.08.12)"
ModuleInfo "History: &nbsp; &nbsp; Now camera can be specified in SetMouseCoords() LTShape method."
ModuleInfo "History: v1.4.22.1 (08.08.12)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bugs in loading of world and its components."
ModuleInfo "History: v1.4.22 (03.08.12)"
ModuleInfo "History: &nbsp; &nbsp; Added Copy<ClassName>To methods to some shape classes."
ModuleInfo "History: v1.4.21 (02.08.12)"
ModuleInfo "History: &nbsp; &nbsp; Added physics module, Box2D system, sprite and tile map class."
ModuleInfo "History: &nbsp; &nbsp; Implemented correct rotation while rendering sprite and tile map collision shapes in LTDebugVisualizer (for Box2D shapes)."
ModuleInfo "History: &nbsp; &nbsp; Fixed bug and improved trimming converted Double function."
ModuleInfo "History: v1.4.20 (30.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Added new character sequence - %n (new line)."
ModuleInfo "History: v1.4.19 (27.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Added InsertBeforeShape method to LTLayer (it works with shape lists too)."
ModuleInfo "History: v1.4.18.1 (27.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Fixed some bugs."
ModuleInfo "History: v1.4.18 (26.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Tile collision shapes now can contain Layer instead of SpriteGroup."
ModuleInfo "History: &nbsp; &nbsp; Added L_Encoding and L_Decoding functions for encode int values in string chunks which consist of symbols from 48 to 127."
ModuleInfo "History: &nbsp; &nbsp; Chunk size depends on values range (80 or less - one symbol, less than 6400 - 2 symbols and so on)."
ModuleInfo "History: &nbsp; &nbsp; Tile map array is now represented as values encoded in chunk string with new encoding functions."
ModuleInfo "History: &nbsp; &nbsp; Block sizes of tilesets attributes are now encoded with this function too."
ModuleInfo "History: &nbsp; &nbsp; Escape character for XML is changed from / to % due to its presence in 80-sized symbol block for encoding."
ModuleInfo "History: v1.4.17 (25.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Converted strings to UTF8."
ModuleInfo "History: &nbsp; &nbsp; Added L_VerisonToInt function."
ModuleInfo "History: &nbsp; &nbsp; Service conversion between symbol and its code functions for /#NNNN symbols are converted to UTF8 too."
ModuleInfo "History: v1.4.16 (16.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Moved displaying angle from LTVisualizer to the LTSprite."
ModuleInfo "History: v1.4.15 (15.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Implemented full sprite grouping support with group inclusion and rotation."
ModuleInfo "History: &nbsp; &nbsp; Added SpriteShape parameter to the LTVisualizer's DrawSprite method."
ModuleInfo "History: &nbsp; &nbsp; Fixed documentation examples according to system changes."
ModuleInfo "History: &nbsp; &nbsp; LTGroup renamed to LTSpriteGroup."
ModuleInfo "History: v1.4.14 (14.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Implemented collision detection of ray and oval, rectangle, triangle and another ray."
ModuleInfo "History: &nbsp; &nbsp; Implemented collision detection of line segment and oval, rectangle, triangle, ray, line and another line segment."
ModuleInfo "History: &nbsp; &nbsp; Implemented overlapping detection of circle and triangle."
ModuleInfo "History: v1.4.13 (13.07.12)"
ModuleInfo "History: &nbsp; &nbsp; Completed wedging off 2 triangles."
ModuleInfo "History: &nbsp; &nbsp; Implemented image path/object map to exclude loading same image from different worlds twice."
ModuleInfo "History: v1.4.12.1 (14.06.12)"
ModuleInfo "History: &nbsp; &nbsp; ..WithLine methods changed to ..WithLineSegment"
ModuleInfo "History: v1.4.12 (13.06.12)"
ModuleInfo "History: &nbsp; &nbsp; Added functions for wedging off pivot and oval / rectangle / triangle."
ModuleInfo "History: &nbsp; &nbsp; Completed wedging off oval / rectangle and triangle."
ModuleInfo "History: &nbsp; &nbsp; Added UsePoints and UsePivots methods to LTLine."
ModuleInfo "History: &nbsp; &nbsp; Added ToLine and HasPoint methods for ray sprite."
ModuleInfo "History: &nbsp; &nbsp; Added L_Cathetus service function."
ModuleInfo "History: v1.4.11 (02.06.12)"
ModuleInfo "History: &nbsp; &nbsp; Rearranged project class to write GUI project class more easily."
ModuleInfo "History: &nbsp; &nbsp; Controllers system is rewritten and now is based on events."
ModuleInfo "History: v1.4.10 (27.05.12)"
ModuleInfo "History: &nbsp; &nbsp; Added Triangle type of the sprite."
ModuleInfo "History: &nbsp; &nbsp; Added triangle collision functions."
ModuleInfo "History: &nbsp; &nbsp; Rewrote oval overlapping function."
ModuleInfo "History: &nbsp; &nbsp; Renamed LTLine to LTLineSegment."
ModuleInfo "History: &nbsp; &nbsp; Created LTLine class."
ModuleInfo "History: &nbsp; &nbsp; Added some service methods to LTSprite: GetBounds, ToCircle, ToCircleUsingLine."
ModuleInfo "History: &nbsp; &nbsp; Added algebraic geometry methods for LTSprite: GetHypotenuse, GetRightAnglePivot."
ModuleInfo "History: &nbsp; &nbsp; Added algebraic geometry methods for LTLine: DistanceTo, PivotProjection, IntersectionWith, PivotOrientation."
ModuleInfo "History: v1.4.9 (22.05.12)"
ModuleInfo "History: &nbsp; &nbsp; Rewrote collision and physics system to be faster and more flexible."
ModuleInfo "History: v1.4.8 (22.05.12)"
ModuleInfo "History: &nbsp; &nbsp; Added FieldExists and RemoveField XMLObject methods."
ModuleInfo "History: v1.4.7 (21.05.12)"
ModuleInfo "History: &nbsp; &nbsp; Added CollisionLayer LTShape parameter."
ModuleInfo "History: &nbsp; &nbsp; Added CollisionShape variable to the HandleCollisionWithTile method."
ModuleInfo "History: v1.4.6 (20.05.12)"
ModuleInfo "History: &nbsp; &nbsp; Added AttributeExists XMLObject method."
ModuleInfo "History: &nbsp; &nbsp; New class LTColor is splitted from LTVisualizer."
ModuleInfo "History: &nbsp; &nbsp; Moved some LTWorld parameters related to the editor to the newly added LTEditorData class."
ModuleInfo "History: v1.4.5 (22.03.12)"
ModuleInfo "History: &nbsp; &nbsp; Added another algorhytm of displaying raster frame."
ModuleInfo "History: v1.4.4 (22.02.12)"
ModuleInfo "History: &nbsp; &nbsp; Added ParameterExists() and IsAtPositionOfPoint() methods to the LTShape."
ModuleInfo "History: &nbsp; &nbsp; Added LTChainedModel, LTValueChangingModel, LTResizingModel, LTAlphaChangingModel classes."
ModuleInfo "History: &nbsp; &nbsp; Added LTColorChangingModel, LTTimedMovementModel, LTMovingModel, LTFollowingModel classes."
ModuleInfo "History: v1.4.3 (21.02.12)"
ModuleInfo "History: &nbsp; &nbsp; Added SwapTiles() method to LTTileMap."
ModuleInfo "History: v1.4.2 (15.02.12)"
ModuleInfo "History: &nbsp; &nbsp; XML loading system is now have 2 passes and don't need definitions list, so xml file size and complexity is reduced."
ModuleInfo "History: v1.4.1 (15.02.12)"
ModuleInfo "History: &nbsp; &nbsp; DebugVisualizer now shows half-transparent invisible sprites."
ModuleInfo "History: v1.4 (01.01.12)"
ModuleInfo "History: &nbsp; &nbsp; Everything is fixed, documented and updated."
ModuleInfo "History: v1.3.19 (28.12.11)"
ModuleInfo "History: &nbsp; &nbsp; Behavior models now doesn't being activated/deacivated if they already did."
ModuleInfo "History: &nbsp; &nbsp; Fixed bug in animation behavior model."
ModuleInfo "History: &nbsp; &nbsp; Added Info() method for behavior models for more info in ShowModels method."
ModuleInfo "History: &nbsp; &nbsp; Added Permanent flag to LTModelActivator / LTModelDeactivator."
ModuleInfo "History: v1.3.18 (27.12.11)"
ModuleInfo "History: &nbsp; &nbsp; Added collision handlers and rewrote collision methods to support them, CollisionType is removed."
ModuleInfo "History: v1.3.17 (26.12.11)"
ModuleInfo "History: &nbsp; &nbsp; Seriously complemented behavior model system - added a lot of standard classes."
ModuleInfo "History: &nbsp; &nbsp; Removed Default...() methods from LTBehaviorModel."
ModuleInfo "History: &nbsp; &nbsp; Project parameter is removed from Animate() method."
ModuleInfo "History: &nbsp; &nbsp; Added AttachModels() method to LTShape."
ModuleInfo "History: v1.3.16 (20.12.11)"
ModuleInfo "History: &nbsp; &nbsp; Added L_Cursor to the LTProject which will be always at mouse coords."
ModuleInfo "History: &nbsp; &nbsp; Added SwitchTo() method to LTProject."
ModuleInfo "History: v1.3.15 (14.12.11)"
ModuleInfo "History: &nbsp; &nbsp; Extended LTRevoluteJoint, now you can define hinge point inside child sprite other than its center."
ModuleInfo "History: &nbsp; &nbsp; Made extension of LTBehavior model and new behavior model templates."
ModuleInfo "History: v1.3.14 (12.12.11)"
ModuleInfo "History: &nbsp; &nbsp; Added graphicsdrivers.mod, audiodrivers.mod and alldrivers.mod to the modules list."
ModuleInfo "History: &nbsp; &nbsp; Removed driver addition from the frmwork.mod."
ModuleInfo "History: &nbsp; &nbsp; LTPath is changed to simple TList and path finding method is moved to LTGraph."
ModuleInfo "History: &nbsp; &nbsp; Added methods for managing sets in LTXMLObject class."
ModuleInfo "History: &nbsp; &nbsp; Added CollidesWithLine method to LTLineSegment."
ModuleInfo "History: &nbsp; &nbsp; Rewrote FindPath method of LTGraph."
ModuleInfo "History: &nbsp; &nbsp; Added Define() method to LTButtonAction."
ModuleInfo "History: &nbsp; &nbsp; Added Drawpath() function to LTGraph."
ModuleInfo "History: &nbsp; &nbsp; Replaced pause flag by pause project in the LTProject"
ModuleInfo "History: &nbsp; &nbsp; Addded L_DoubleInLimits() function."
ModuleInfo "History: v1.3.13 (08.12.11)"
ModuleInfo "History: &nbsp; &nbsp; Added AlterDiameter() method to the LTShape."
ModuleInfo "History: &nbsp; &nbsp; Added AlterAngle() method to the LTSprite."
ModuleInfo "History: v1.3.12 (07.12.11)"
ModuleInfo "History: &nbsp; &nbsp; Added FixedAngle parameter to LTDistanceJoint."
ModuleInfo "History: &nbsp; &nbsp; Created different BounceInside() method for LTVectorSprite."
ModuleInfo "History: v1.3.11 (30.11.11)"
ModuleInfo "History: &nbsp; &nbsp; Added SetParameter() and RemoveParameter() methods to LTShape."
ModuleInfo "History: &nbsp; &nbsp; Added creation function to LTDoubleMap."
ModuleInfo "History: &nbsp; &nbsp; Fixed a bug in LTShape SetAsViewport() method."
ModuleInfo "History: v1.3.10 (29.11.11)"
ModuleInfo "History: &nbsp; &nbsp; Added L_PrintText function."
ModuleInfo "History: &nbsp; &nbsp; Added Length function to LTLineSegment."
ModuleInfo "History: v1.3.9 (28.11.11)"
ModuleInfo "History: &nbsp; &nbsp; Added GetTileForPoint() method for LTTileMap."
ModuleInfo "History: &nbsp; &nbsp; Added FirstCollidedSpriteOfGroup() method to LTSprite."
ModuleInfo "History: &nbsp; &nbsp; Added AlterSize method to LTShape."
ModuleInfo "History: &nbsp; &nbsp; Forms.mod module is merged into world editor and removed from modules directory."
ModuleInfo "History: &nbsp; &nbsp; Sound.mod module is merged into MindStorm example and removed from modules directory."
ModuleInfo "History: &nbsp; &nbsp; Added AlterSize() method to the LTShape."
ModuleInfo "History: &nbsp; &nbsp; Added FirstCollidedSpriteOfGroup() method to LTSprite."
ModuleInfo "History: &nbsp; &nbsp; Added GetTileForPoint() method to LTTileMap."
ModuleInfo "History: v1.3.8 (27.11.11)"
ModuleInfo "History: &nbsp; &nbsp; LTVisualizer now have FromRGBColor and FromHexColor creation functions."
ModuleInfo "History: &nbsp; &nbsp; LTContourVisualizer now have FromWidthAndRGBColor and FromWidthAndHexColor creation functions."
ModuleInfo "History: &nbsp; &nbsp; Added MoveBackwards() and BounseInside() methods to LTSprite."
ModuleInfo "History: &nbsp; &nbsp; LTLineSegment's creation function renamed to FromPivots()."
ModuleInfo "History: &nbsp; &nbsp; Added FromFileAndBorders() creation function to LTRasterFrame."
ModuleInfo "History: &nbsp; &nbsp; Added SetRandomColor() method to LTVisualizer."
ModuleInfo "History: v1.3.7 (22.11.11)"
ModuleInfo "History: &nbsp; &nbsp; Tile number retrieving method is extracted from tile drawing method in LTVisualizer"
ModuleInfo "History: &nbsp; &nbsp; Added AddParameter() method to the LTShape."
ModuleInfo "History: v1.3.6 (18.11.11)"
ModuleInfo "History: &nbsp; &nbsp; Added creation methods for LTSprite and LTVectorSprite."
ModuleInfo "History: v1.3.5.1 (17.11.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bugs in mouse wheel action names."
ModuleInfo "History: &nbsp; &nbsp; LTEmptyPrimitve renamed to LTContourVisualizer."
ModuleInfo "History: v1.3.5 (16.11.11)"
ModuleInfo "History: &nbsp; &nbsp; Added LTDistanceJoint class."
ModuleInfo "History: v1.3.4 (14.11.11)"
ModuleInfo "History: &nbsp; &nbsp; XML now supports quotes and UTF symbols in text attribute strings."
ModuleInfo "History: v1.3.3 (09.11.11)"
ModuleInfo "History: &nbsp; &nbsp; Tile enframing system now always sets tile number to first number from tile number array of tile rule (previously it chose one of them randomly) and only if current tile number is not in this array." 
ModuleInfo "History: &nbsp; &nbsp; This allows to place differend kinds of tile of same type as existent on enframed area."
ModuleInfo "History: v1.3.2.1 (12.10.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bug with adding multiple equal keys to button action."
ModuleInfo "History: v1.3.2 (07.10.11)"
ModuleInfo "History: &nbsp; &nbsp; Added possibility to bind multiple buttons to the button action."
ModuleInfo "History: v1.3.1 (07.10.11)"
ModuleInfo "History: &nbsp; &nbsp; Added SetSizeAs() method to the LTShape."
ModuleInfo "History: v1.3 (06.10.11)"
ModuleInfo "History: &nbsp; &nbsp; Implemented controllers system."
ModuleInfo "History: &nbsp; &nbsp; Added Contour() and SetAsViewport() methods to the LTShape."
ModuleInfo "History: &nbsp; &nbsp; FindShapeWithParameter() parameters are rearranged."
ModuleInfo "History: v1.2.7 (03.10.11)"
ModuleInfo "History: &nbsp; &nbsp; Added PrintText() method to the LTShape."
ModuleInfo "History: &nbsp; &nbsp; Fixed bug in LTCamera.SetCameraViewport() method."
ModuleInfo "History: v1.2.6 (02.10.11)"
ModuleInfo "History: &nbsp; &nbsp; LTSprite is merged with LTSprite and LTVectorSprite is cleaned of unuseful code."
ModuleInfo "History: v1.2.5 (29.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Added ApplyColor(), Lighten() and Darken() methods to the LTCamera."
ModuleInfo "History: v1.2.4.1 (26.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bug of wrong displaying of non-scaled sprite."
ModuleInfo "History: v1.2.4 (22.09.11)"
ModuleInfo "History: &nbsp; &nbsp; LTRasterFrameVisualizer is deprecated, LTRasterFrame derived from LTImage created instead."
ModuleInfo "History: v1.2.3.1 (19.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed parameter cloning bug."
ModuleInfo "History: &nbsp; &nbsp; Fixed bug of Time variable of LTProject not initialized."
ModuleInfo "History: v1.2.3 (14.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Added possibility to search for shapes inside layers' sprite maps."
ModuleInfo "History: &nbsp; &nbsp; Added Range parameter to the pathfinder."
ModuleInfo "History: v1.2.2 (13.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Added ShowModels() debugging method to shape."
ModuleInfo "History: v1.2.1 (12.09.11)"
ModuleInfo "History: &nbsp; &nbsp; TileX and TileY which are passing to visualizer's DrawTile() method are now not wrapped (wrapped inside the method)."
ModuleInfo "History: &nbsp; &nbsp; Fixed bug in DebugVisualizer shape displaying."
ModuleInfo "History: v1.2 (11.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Added parameters list to the shapes."
ModuleInfo "History: &nbsp; &nbsp; Object Name parameter is deprecated."
ModuleInfo "History: &nbsp; &nbsp; Framework modules are structurized."
ModuleInfo "History: v1.1.5 (09.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Added FirstPosition() and LastPosition() methods to tilemap position class for path finding."
ModuleInfo "History: &nbsp; &nbsp; Added RemoveSame() method for LTBehaviorModel."
ModuleInfo "History: v1.1.4.1 (06.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bug of displaying layers with another visualizer."
ModuleInfo "History: v1.1.4 (02.09.11)"
ModuleInfo "History: &nbsp; &nbsp; Added 'mixed' flag to layers."
ModuleInfo "History: &nbsp; &nbsp; Fixed glitches of tilemap pathfinder."
ModuleInfo "History: v1.1.3 (01.09.11)"
ModuleInfo "History: &nbsp; &nbsp; MoveTowards() method is moved to LTShape and have now Velocity parameter."
ModuleInfo "History: &nbsp; &nbsp; Added MoveTowardsPoint() method to LTShape."
ModuleInfo "History: v1.1.2 (31.08.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bug of sprite map displaying by isometric camera."
ModuleInfo "History: &nbsp; &nbsp; Implemented sprite map loading within layer and cloning."
ModuleInfo "History: &nbsp; &nbsp; Added sprites list field to sprite map."
ModuleInfo "History: &nbsp; &nbsp; Fixed bugs of collision shapes displaying."
ModuleInfo "History: v1.1.1 (30.08.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed isometric objects displaying (now displaying image is tied to rectangle escribed circum object parallelogram)."
ModuleInfo "History: &nbsp; &nbsp; Fixed non-scaled objects displaying."
ModuleInfo "History: &nbsp; &nbsp; Tilemap displaying method now supports different tile displaying orders and displays wrapped tilemaps correctly."
ModuleInfo "History: &nbsp; &nbsp; Isomeric and orthogonal tilemap displaying methods are merged (note for custom tilemap visualizers)."
ModuleInfo "History: &nbsp; &nbsp; Added margins to the tile map."
ModuleInfo "History: v1.1 (28.08.11)"
ModuleInfo "History: &nbsp; &nbsp; Finished isometric camera implementation."
ModuleInfo "History: &nbsp; &nbsp; Implemented isometric shape displaying."
ModuleInfo "History: &nbsp; &nbsp; MoveUsingKeys() method now works correctly with isometric cameras."
ModuleInfo "History: &nbsp; &nbsp; Added class for tilemap pathfinding and raster frame visualizer."
ModuleInfo "History: v1.0.7 (26.08.11)"
ModuleInfo "History: &nbsp; &nbsp; Added isometric camera support to marching ants rectangle."
ModuleInfo "History: &nbsp; &nbsp; Added camera and incbin parametes to the world class."
ModuleInfo "History: v1.0.6 (24.08.11)"
ModuleInfo "History: &nbsp; &nbsp; Implemented incbin support."
ModuleInfo "History: v1.0.5 (03.08.11)"
ModuleInfo "History: &nbsp; &nbsp; Partially implemented isometric cameras."
ModuleInfo "History: &nbsp; &nbsp; Added camera saving/loading method."
ModuleInfo "History: &nbsp; &nbsp; Added pivot mode to sprite maps."
ModuleInfo "History: &nbsp; &nbsp; Added Flipping flag to the LTProject."
ModuleInfo "History: &nbsp; &nbsp; EmptyTile parameter was moved from LTTIleMap to LTTileSet."
ModuleInfo "History: v1.0.4.1 (01.08.11)"
ModuleInfo "History: &nbsp; &nbsp; Removed support for different axis magnification."
ModuleInfo "History: &nbsp; &nbsp; Added camera field into the LTWorld."
ModuleInfo "History: v1.0.4 (22.07.11)"
ModuleInfo "History: &nbsp; &nbsp; Added full support of Oval shape type (replaced Circle)."
ModuleInfo "History: v1.0.3.1 (19.07.11)"
ModuleInfo "History: &nbsp; &nbsp; Removed multiple collision reactions when executing CollisionsWithSpriteMap method."
ModuleInfo "History: v1.0.3 (18.07.11)"
ModuleInfo "History: &nbsp; &nbsp; Added Parallax() function."
ModuleInfo "History: &nbsp; &nbsp; Added sprite map clearing method."
ModuleInfo "History: &nbsp; &nbsp; Fixed bug in LTDoubleMap.ToNewImage() method."
ModuleInfo "History: v1.0.2.1 (07.07.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bug of visualizer's DX/DY not saving/loading."
ModuleInfo "History: v1.0.2 (06.07.11)"
ModuleInfo "History: &nbsp; &nbsp; CollisionsWithSpriteMap() method now have Map parameter to add collided sprites to."
ModuleInfo "History: &nbsp; &nbsp; Added visualizer cloning method."
ModuleInfo "History: &nbsp; &nbsp; ImageVisualizer is merged with Visualizer."
ModuleInfo "History: &nbsp; &nbsp; Fixed bug in SetSize (old method cleared collision map)."
ModuleInfo "History: &nbsp; &nbsp; Collision maps are renamed to sprite maps."
ModuleInfo "History: &nbsp; &nbsp; Added sprite maps saving/loading method."
ModuleInfo "History: &nbsp; &nbsp; Added GetSprites() method."
ModuleInfo "History: v1.0.1.1 (05.07.11)"
ModuleInfo "History: &nbsp; &nbsp; ShowDebugInfo() method is now without parameters."
ModuleInfo "History: &nbsp; &nbsp; MoveUsingKeys() methods are now in LTShape and have velocity parameter."
ModuleInfo "History: &nbsp; &nbsp; Max2D drivers import is now inside framework."
ModuleInfo "History: v1.0.1 (04.07.11)"
ModuleInfo "History: &nbsp; &nbsp; Added sorting parameter to collision maps."
ModuleInfo "History: &nbsp; &nbsp; Border parameter of collision map is turned to 4 margin parameters."
ModuleInfo "History: &nbsp; &nbsp; Now setting all collision map margins to one value is possible by using SetBorder() method."
ModuleInfo "History: &nbsp; &nbsp; Visualizer's DX and DY parameters are now image-relative."
ModuleInfo "History: v1.0.0.1 (30.06.11)"
ModuleInfo "History: &nbsp; &nbsp; Fixed bug of ChopFilename() function under Mac."
ModuleInfo "History: v1.0 (28.06.11)"
ModuleInfo "History: &nbsp; &nbsp; Initial release."

Import brl.random
Import brl.reflection
Import brl.retro
Import brl.map
Import brl.max2d
Import brl.eventqueue

Const L_Version:String = "1.5"

SeedRnd( MilliSecs() )

Include "include\LTObject.bmx"
Include "include\LTProject.bmx"
Include "include\LTShape.bmx"
Include "include\LTColor.bmx"
Include "include\LTBehaviorModel.bmx"
Include "include\LTFont.bmx"
Include "include\Controllers.bmx"
Include "include\LTDrag.bmx"
Include "include\LTAction.bmx"
Include "include\XML.bmx"
Include "include\Service.bmx"
Include "include\Deprecated.bmx"




Global L_ErrorSoundPlayer:TSoundPlayer = New TSoundPlayer

Type TSoundPlayer
	Method PlayErrorSound()
	End Method
End Type

Function L_Error( Text:String )
	L_ErrorSoundPlayer.PlayErrorSound()
	Notify( Text, True )
	DebugStop
	End
End Function



Global L_Incbin:String = ""

Function L_SetIncbin( Value:Int )
	If Value Then L_Incbin = "incbin::" Else L_Incbin = ""
End Function



LTSpriteHandler.Init()
LTSpritesInteraction.Init()