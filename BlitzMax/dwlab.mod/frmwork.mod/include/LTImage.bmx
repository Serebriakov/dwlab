'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Global L_LoadImages:Int = 1

Include "LTRasterFrame.bmx"

Rem
bbdoc: Image class.
End Rem
Type LTImage Extends LTObject
	Global Bitmaps:TMap = New TMap
	Global LoadingErrorHandler:LTImageLoadingErrorHandler = New LTImageLoadingErrorHandler
	
	Field BMaxImage:TImage
	Field Filename:String
	Field XCells:Int = 1
	Field YCells:Int = 1
	Field LoadingTime:Int
	
	
	
	Rem
	bbdoc: Creates new image from file with specified cell quantities for splitting.
	returns: New image (LTImage).
	End Rem
	Function FromFile:LTImage( Filename:String, XCells:Int = 1, YCells:Int = 1 )
		'?debug
		'If XCells <= 0 Or YCells <= 0 Then L_Error( "Cells quantity must be 1 or more" )
		'?
		
		Local Image:LTImage = New LTImage
		Image.Filename = Filename
		Image.XCells = XCells
		Image.YCells = YCells
		Image.Init()
		
		Return Image
	End Function
		
	
	
	Rem
	bbdoc: Initializes image.
	about: Splits image by XCells x YCells grid. Will be executed after loading image object from XML file.
	End Rem
	Method Init()
		Local Time:Int = Millisecs()
		
		Local Pixmap:TPixmap = LoadPixmap( L_Incbin + Filename )
		If Not Pixmap Then Pixmap = LoadingErrorHandler.HandleError( L_Incbin + Filename, Self )
		'?debug
		'If PixmapWidth( BMaxImage ) Mod XCells <> 0 Or PixmapHeight( BMaxImage ) Mod YCells <> 0 Then L_Error( "Incorrect cells quantity for splitting" )
		'?
		If XCells < 0 Then XCells = PixmapWidth( Pixmap ) / -XCells
		If YCells < 0 Then YCells = PixmapHeight( Pixmap ) / -YCells
		
		Local CellWidth:Int = PixmapWidth( Pixmap ) / XCells
		Local CellHeight:Int = PixmapHeight( Pixmap ) / YCells
		
		Local Bitmap:TImage = TImage( Bitmaps.ValueForKey( Filename ) )
		If Bitmap Then If CellWidth <> ImageWidth( Bitmap ) Or CellHeight <> ImageHeight( Bitmap ) Then Bitmap = Null
		If Bitmap Then
			BMaxImage = Bitmap
		Else
			BMaxImage = LoadAnimImage( Pixmap, CellWidth, CellHeight, 0, XCells * YCells )
			MidHandleImage( BMaxImage )
			Bitmaps.Insert( Filename, Bitmap )
		End If
		
		L_LoadingTime :+ LoadingTime
		LoadingTime = MilliSecs() - Time
		L_NewTotalLoadingTime :+ LoadingTime
		If L_TotalLoadingTime > 0 Then L_LoadingProgress = 1.0 * L_LoadingTime / L_TotalLoadingTime
		If L_LoadingUpdater Then L_LoadingUpdater.Update()
	End Method
	
	
	
	Rem
	bbdoc: Sets handle of image.
	about: Values should be in 0.0...1.0 interval.
	End Rem
	Method SetHandle( X:Double, Y:Double )
		SetImageHandle( BMaxImage, X * ImageWidth( BMaxImage ), Y * ImageHeight( BMaxImage ) )
	End Method
	
	
	
	Rem
	bbdoc: Returns frames quantity of given image.
	returns: Frames quantity of given image.
	End Rem
	Method FramesQuantity:Int()
		Return BMaxImage.frames.Dimensions()[ 0 ]
	End Method
	
	
	
	Rem
	bbdoc: Returns width of image.
	returns: Width of image in pixels.
	End Rem
	Method Width:Double()
		Return ImageWidth( BMaxImage )
	End Method
	
	
	
	Rem
	bbdoc: Returns height of image.
	returns: Height of image in pixels.
	End Rem
	Method Height:Double()
		Return ImageHeight( BMaxImage )
	End Method
	
	
	
	Rem
	bbdoc: Draws image using given coordinates and size.
	End Rem
	Method Draw( X:Double, Y:Double, Width:Double, Height:Double, Frame:Int )
		SetScale( Width / ImageWidth( BMaxImage ), Height / ImageHeight( BMaxImage ) )
		DrawImage( BMaxImage, X, Y, Frame )
	End Method
	
	
	
	Method XMLIO( XMLObject:LTXMLObject )
		Super.XMLIO( XMLObject )
		
		XMLObject.ManageStringAttribute( "filename", Filename )
		XMLObject.ManageIntAttribute( "xcells", XCells, 1 )
		XMLObject.ManageIntAttribute( "ycells", YCells, 1 )
		XMLObject.ManageIntAttribute( "loading-time", LoadingTime )
		
		'If Not L_EditorData.Images.Contains( Self ) Then L_EditorData.Images.AddLast( Self )
		
		If L_XMLMode = L_XMLGet And L_LoadImages Then Init()
	End Method
End Type




Rem
bbdoc: Image loading error handler.
about: Intended to handle situation when image file file is missing.
End Rem
Type LTImageLoadingErrorHandler
	Method HandleError:TPixmap( FileName:String, Image:LTImage )
		L_Error( "Image " + FileName + " cannot be loaded or not found." )
	End Method
End Type