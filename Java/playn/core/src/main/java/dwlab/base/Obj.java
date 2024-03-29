/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base;

import dwlab.base.XMLObject.XMLAttribute;
import dwlab.base.XMLObject.XMLObjectField;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.VectorSprite;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;
import java.util.*;
import static dwlab.platform.Functions.*;

/**
 * Global object class
 */
public class Obj {
	public static HashMap<String, Class> classes = new HashMap<String, Class>();
	static HashMap<Obj, Integer> iDMap;
	static HashMap<Obj, XMLObject> removeIDMap;
	static int maxID;
	static Obj iDArray[];
	static HashSet<Obj> undefinedObjects;

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
		double x, y = getTextHeight();

		switch( horizontalAlign ) {
			case TO_CENTER:
				x = 0.5d * ( getScreenWidth() - getTextWidth( text ) );
				break;
			case TO_RIGHT:
				x = getScreenWidth() - getTextWidth( text );
				break;
			default:
				x = 0;
		}

		switch( verticalAlign ) {
			case TO_CENTER:
				y = 0.5d * ( getScreenHeight() - y * ( shift + 1 ) );
				break;
			case TO_BOTTOM:
				y = getScreenHeight() - y * ( shift + 1 );
				break;
			default:
				y *= shift;
		}

		drawText( text, (float) x, (float) y );
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
		if( XMLObject.xMLSetMode() ) xMLObject.name = getClass().getSimpleName();
	}


	public static String objectFileName;

	/**
	 * Loads object with all contents from file.
	 * @see #saveToFile, #xMLIO
	 */
	public static Obj loadFromFile( String fileName, XMLObject xMLObject ) {
		if( xMLObject == null ) {
			maxID = 0;
			xMLObject = XMLObject.readFromFile( fileName );
			TileMap.file = null;
		}
		
		if( classes.isEmpty() ) fillClasses();
		
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

		XMLObject.xMLMode = XMLObject.XMLMode.GET;
		
		object.xMLIO( xMLObject );

		return object;
	}
	
	public static Obj loadFromFile( String fileName ) {
		return loadFromFile( fileName, null );
	}
	

	private static void fillClasses() {
		Obj.classes.put( "Font", dwlab.base.images.Font.class );
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

	/**
	 * Saves object with all contents to file.
	 * @see #loadFromFile, #xMLIO
	 */
	public void saveToFile( String fileName ) {
		objectFileName = fileName;
		iDMap = new HashMap();
		removeIDMap = new HashMap();
		maxID = 1;

		XMLObject.xMLMode = XMLObject.xMLMode.SET;
		XMLObject xMLObject = new XMLObject();
		undefinedObjects = new HashSet<Obj>();

		xMLIO( xMLObject );
		if( TileMap.file != null ) {
			TileMap.file.close();
			TileMap.file = null;
		}

		xMLObject.setAttribute( "dwlab_version", version );
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
}
