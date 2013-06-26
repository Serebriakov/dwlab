/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base;

import static dwlab.base.Project.deltaTime;
import dwlab.base.Sys.XMLMode;
import dwlab.base.XMLObject.XMLAttribute;
import dwlab.base.XMLObject.XMLObjectField;
import dwlab.base.files.BinaryFile;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.VectorSprite;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global object class
 */
public class Obj {
	public static HashMap<String, Class> classes = null;
	static HashMap<Obj, Integer> iDMap;
	static HashMap<Obj, XMLObject> removeIDMap;
	static int maxID;
	static Obj iDArray[];
	static HashSet<Obj> undefinedObjects;

	public static boolean remove = true;
	public static double inaccuracy = 0.000001;	
	

	public Layer toLayer() {
		return null;
	}
	
	public Sprite toSprite() {
		return null;
	}
	
	public VectorSprite toVectorSprite() {
		return null;
	}

	public TileMap toTileMap() {
		return null;
	}

	public SpriteMap toSpriteMap() {
		return null;
	}

	// ==================== Drawing ===================

	/**
	 * Draws the shape.
	 * You can fill it with drawing commands for object and its parts.
	 * 
	 * @see #drawUsingVisualizer, #lTVisualizer
	 */
	public void draw( Color drawingColor ) {
	}
	
	public final void draw() {
		draw( Color.white );
	}


	/**
	 * Draws the shape using another visualizer.
	 * You can fill it with drawing commands for object and its parts using another visualizer.
	 * 
	 * @see #draw, #lTVisualizer
	 */
	public void drawUsingVisualizer( Visualizer vis, Color drawingColor ) {
	}
	
	public void drawUsingVisualizer( Visualizer vis ) {
		drawUsingVisualizer( vis, Color.white );
	}
	
	// ==================== Printing text ===================
	
	public static void printText( String text, Align horizontalAlign, Align verticalAlign, int shift, Color color ) {
		double x, y = Graphics.getTextHeight();

		switch( horizontalAlign ) {
			case TO_CENTER:
				x = 0.5d * ( Graphics.getScreenWidth() - Graphics.getTextWidth( text ) );
				break;
			case TO_RIGHT:
				x = Graphics.getScreenWidth() - Graphics.getTextWidth( text );
				break;
			default:
				x = 0;
		}

		switch( verticalAlign ) {
			case TO_CENTER:
				y = 0.5d * ( Graphics.getScreenHeight() - y * ( shift + 1 ) );
				break;
			case TO_BOTTOM:
				y = Graphics.getScreenHeight() - y * ( shift + 1 );
				break;
			default:
				y *= shift;
		}

		org.newdawn.slick.Color oldContourColor = Graphics.getContourColor();
		Graphics.setContourColor( 0f, 0f, 0f );
		Graphics.drawText( text, (float) x, (float) y );
		Graphics.setContourColor( oldContourColor );
	}

	public static void printText( String text, Align horizontalAlign, Align verticalAlign, int shift ) {
		printText( text, horizontalAlign, verticalAlign, shift, Color.white );
	}
	public static void printText( String text, Align horizontalAlign, Align verticalAlign ) {
		printText( text, horizontalAlign, verticalAlign, 0, Color.white );
	}
	
	public static void printText( String string, int shift ) {
		printText( string, Align.TO_LEFT, Align.TO_TOP, shift, Color.white );
	}
	
	public static void printText( String string ) {
		printText( string, Align.TO_LEFT, Align.TO_TOP, 0, Color.white );
	}
	
	// ==================== Management ===================

	/**
	 * Initialization method of the object.
	 * Fill it with object initialization commands.
	 */
	public void init() {
	}
	
	
	/**
	 * Acting method of object.
	 * Fill it with the object acting commands.
	 */
	public void act() {
	}


	/**
	 * Method for updating object.
	 * Fill it with the object updating commands.
	 */
	public void update() {
	}
	
	
	/**
	 * Method called before destruction of object.
	 * Fill it with the commands which should be executed before object destruction.
	 */
	public void destroy() {
	}

	// ==================== Loading / saving ===================

	/**
	 * Method for loading / saving object.
	 * This method is for storing object fields into XML object for saving and retrieving object fields from XML object for loading.
	 * You can put different XMLObject commands and your own algorithms for loading / saving data structures here.
	 * 
	 * @see #manageChildArray, #manageChildList, #manageDoubleAttribute, #manageIntArrayAttribute
	 * #manageIntAttribute, #manageListField, #manageObjectArrayField, #manageObjectAttribute, #manageObjectField
	 * #manageObjectMapField, #manageStringAttribute 
	 */
	public void xMLIO( XMLObject xMLObject ) {
		if( Sys.xMLSetMode() ) xMLObject.name = getClass().getSimpleName();
	}


	public static String objectFileName;

	/**
	 * Loads object with all contents from file.
	 * @see #saveToFile, #xMLIO
	 */
	public static Obj loadFromFile( String fileName, XMLObject xMLObject ) {
		if( classes == null ) getClasses();
		
		if( xMLObject == null ) {
			maxID = 0;
			xMLObject = XMLObject.readFromFile( fileName );
		}		
		
		objectFileName = fileName;
		Service.loadingStatus = "Serializing objects...";
		Service.totalLoadingTime = xMLObject.getIntegerAttribute( "total-loading-time" );
		Service.newTotalLoadingTime = 0;

		iDArray = new Obj[ maxID + 1 ];
		fillIDArray( xMLObject );
		
		Class objectClass = classes.get( xMLObject.name );
		
		Obj object = null;
		try {
			object = (Obj) objectClass.newInstance();
		} catch ( InstantiationException ex ) {
			error( "\"" + xMLObject.name + "\" is abstract class or interface" );
		} catch ( IllegalAccessException ex ) {
			error( "Class \"" + xMLObject.name + "\" is unaccessible" );
		}

		Sys.xMLMode = XMLMode.GET;
		
		TileMap.file = BinaryFile.read( fileName + "bin" );
		object.xMLIO( xMLObject );

		return object;
	}
	
	public static Obj loadFromFile( String fileName ) {
		return loadFromFile( fileName, null );
	}


	public static void fillIDArray( XMLObject xMLObject ) {
		if( xMLObject.name.equals( "Object" ) ) return;
		
		if( xMLObject.name.equals( "TList" ) ) xMLObject.name = "List";
		
		if( !xMLObject.name.equals( "List" ) && !xMLObject.name.equals( "Array" ) ) {
			int iD = 0;
			if( xMLObject.attributeExists( "id" ) ) iD = Integer.parseInt( xMLObject.getAttribute( "id" ) );
		
			if( xMLObject.name.startsWith( "LT" ) ) xMLObject.name = xMLObject.name.substring( 2 );
			Class objectClass = classes.get( xMLObject.name );
			if( objectClass == null ) error( "Class \"" + xMLObject.name + "\" not found" );

			if( iD > 0 ) try {
				iDArray[ iD ] = (Obj) objectClass.newInstance();
			} catch ( InstantiationException ex ) {
				error( "\"" + xMLObject.name + "\" is abstract class or interface" );
			} catch ( IllegalAccessException ex ) {
				error( "Class \"" + xMLObject.name + "\" is unaccessible" );
			}
		}
		
		for( XMLObject child: xMLObject.children ) {
			fillIDArray( child );
		}
		for( XMLObjectField objectField: xMLObject.fields ) {
			fillIDArray( objectField.value );
		}
	}
	

    private static void getClasses() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		classes = new HashMap<String, Class>();
		Enumeration<URL> resources = null;
		try {
			resources = classLoader.getResources( "dwlab" );
		} catch ( IOException ex ) {
			Logger.getLogger( Obj.class.getName() ).log( Level.SEVERE, null, ex );
		}
		List<File> dirs = new ArrayList<File>();
		while ( resources.hasMoreElements() ) {
				URL resource = resources.nextElement();
				dirs.add( new File( resource.getFile() ) );
		}
		for ( File directory : dirs ) try {
			addClasses( directory, "dwlab" );
		} catch ( ClassNotFoundException ex ) {
			Logger.getLogger( Obj.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}

	private static void addClasses( File directory, String packageName ) throws ClassNotFoundException {
		if ( !directory.exists() ) return;
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains( "." );
				addClasses( file, packageName + "." + file.getName() );
			} else if( file.getName().endsWith( ".class" ) ) {
				Class cl = Class.forName( packageName + '.' + file.getName().substring( 0, file.getName().length() - 6 ) );
				classes.put( cl.getSimpleName(), cl );
			}
		}
    }

	/**
	 * Saves object with all contents to file.
	 * @see #loadFromFile, #xMLIO
	 */
	public void saveToFile( String fileName ) {
		objectFileName = fileName;
		iDMap = new HashMap();
		removeIDMap = new HashMap();
		maxID = 1;

		Sys.xMLMode = Sys.XMLMode.SET;
		XMLObject xMLObject = new XMLObject();
		undefinedObjects = new HashSet<Obj>();

		TileMap.offset = 0;
		TileMap.file = BinaryFile.write( fileName + "bin" );
		xMLIO( xMLObject );
		TileMap.file.close();

		xMLObject.setAttribute( "dwlab_version", Service.version );
		xMLObject.setAttribute( "total-loading-time", Service.newTotalLoadingTime );

		for( XMLObject xMLObject2 : removeIDMap.values() ) {
			for ( Iterator<XMLAttribute> it = xMLObject2.attributes.iterator(); it.hasNext(); ) {
				if( it.next().name.equals( "id" ) ) it.remove();
			}
		}

		iDMap = null;
		removeIDMap = null;

		xMLObject.writeToFile( fileName );
	}
	
	
	public static void error( String Text ) {
		System.out.println( Text );
		System.exit( 0 );
	}


	public static double perSecond( double value ) {
		return value * deltaTime;
	}
}
