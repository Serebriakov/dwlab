/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base.service;

import dwlab.base.Obj;
import dwlab.base.files.TextFile;
import dwlab.shapes.Shape;
import dwlab.shapes.sprites.Camera;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Service extends Obj {
	public static long newTotalLoadingTime;
	public static long objectLoadingTime;
	public static long totalLoadingTime;
	public static double loadingProgress;
	public static String loadingStatus;
	public static Obj loadingUpdater = null;
	
	/**
	* Transfers hex string value to Int.
	*/
	public static int hexToInt( String hexString ) {
		int value = 0;
		hexString = hexString.toUpperCase().trim();
		for( int n=0; n < hexString.length(); n++ ) {
			if( hexString.charAt( n ) <= '9' ) {
				value = value * 16 + ( (int) hexString.charAt( n ) - '0'  );
			} else {
				value = value * 16 + ( (int) hexString.charAt( n ) - 'A'  + 10 );
			}
		}
		return value;
	}


	/**
	* Trims trailing zeroes of Double value and cuts all digits after given quantity (off by default) after point.
	*/
	public static String trim( double val, int digitsAfterDot ) {
		String strVal = String.valueOf( val ) + "00000000000000";
		int n = strVal.indexOf( "." ) + 1 + digitsAfterDot;

		while( true ) {
			n--;
			switch( strVal.charAt( n ) ) {
				case '0':
					break;
				case '.':
					return strVal.substring( 0, n );
				default:
					return strVal.substring( 0, n + 1 );
			}
		}
	}
	
	public static String trim( double val ) {
		String strVal = String.valueOf( val );
		int n = strVal.length();
		
		while( true ) {
			n--;
			switch( strVal.charAt( n ) ) {
				case '0':
					break;
				case '.':
					return strVal.substring( 0, n );
				default:
					return strVal.substring( 0, n + 1 );
			}
		}
	}


	/**
	* Adds zeroes to Int value to make resulting string length equal to given.
	* @return String with zeroes equal to Int value.
	*
	* Example:
	* printText( "345 with 8 digits total is " + Service.firstZeroes( 345, 8 ) );
	* printText( "9834 with 3 digits total is " + Service.firstZeroes( 9834, 3 ) );
	* printText( "1 with 10 digits total is " + Service.firstZeroes( 1, 10 ) );
	*
	* 345 with 8 digits total is 00000345;
	* 9834 with 3 digits total is 9834;
	* 1 with 10 digits total is 0000000001;
	*/
	public static String firstZeroes( int value, int totalDigits ) {
		String stringValue = String.valueOf( value );
		int length = stringValue.length();
		if( length < totalDigits ) {
			return symbols( "0", totalDigits - length ) + stringValue;
		} else {
			return stringValue;
		}
	}


	/**
	* Returns string made from given string repeated given number of times.
	* @return String consisting of repeated given string.
	*/
	public static String symbols( String symbol, int times ) {
		String symbols = "";
		for( int n=1; n <= times; n++ ) {
			symbols += symbol;
		}
		return symbols;
	}


	/**
	* Limits Double value with inerval defined by two Double values.
	* @return Limited Double value.

	* <ul>
	* <li> If Value is less than FromValue then function returns FromValue.
	* <li> If Value is more than ToValue then function returns ToValue.
	* <li> Otherwise function returns Value.
	* </ul>

	* @see #l_LimitInt
	*/
	public static double limit( double value, double fromValue, double toValue ) {
		if( fromValue > toValue ) error( "FromValue must be less than ToValue" );
		if( value < fromValue ) {
			return fromValue;
		} else if( value > toValue ) {
			return toValue;
		} else {
			return value;
		}
	}


	/**
	* Limits Int value with inerval defined by two Int values.
	* @return Limited Int value.

	* <ul>
	* <li> If Value is less than FromValue then function returns FromValue.
	* <li> If Value is more than ToValue then function returns ToValue.
	* <li> Otherwise function returns Value.
	* </ul>

	* @see #l_LimitDouble, #l_IntInLimits
	*/
	public static int limit( int value, int fromValue, int toValue ) {
		if( fromValue > toValue ) error( "FromValue must be less than ToValue" );
		if( value < fromValue ) {
			return fromValue;
		} else if( value > toValue ) {
			return toValue;
		} else {
			return value;
		}
	}


	/**
	* Checks if given value is the power of 2.
	* @return True is given value is power of 2 otherwise False.
	* @see #l_ToPowerOf2
	*/
	public static boolean isPowerOf2( int value ) {
		if( value + ( value - 1 ) == ( value | ( value - 1 ) ) ) return true; else return false;
	}


	/**
	* Wraps Int value using given size.
	* @return Wrapped Int value.
	* Function returns Value which will be kept in 0...Size - 1 interval.

	* @see #l_WrapInt2, #l_WrapDouble
	*/
	public static int wrap( int value, int size ) {
		return value - size * ( (int) Math.floor( ( (double) value ) / size ) );
	}


	/**
	* Wraps Int value using given interval defined by two given Int values.
	* @return Wrapped Int value.
	* Function returns Value which will be kept in FromValue...ToValue - 1 interval.

	* @see #l_WrapInt, #l_WrapDouble
	*/
	public static int wrap( int value, int fromValue, int toValue ) {
		int size = toValue - fromValue;
		return value - ( (int) Math.floor( ( (double) value - fromValue ) / size ) ) * size;
	}


	/**
	* Wraps Double value using given size.
	* @return Wrapped Double value.
	* Function returns Value which will be kept in 0...Size interval excluding Size.

	* @see #l_WrapInt
	*/
	public static double wrap( double value, double size ) {
		return value - size * Math.floor( value / size );
	}
	

	public static double distance( double dX, double dY ) {
		return Math.sqrt( dX * dX + dY * dY );
	}


	public static double distance2( double dX, double dY ) {
		return dX * dX + dY * dY;
	}
	

	public static double cathetus( double aB, double bC ) {
		return Math.sqrt( aB * aB - bC * bC );
	}


	/**
	* Adds Int value to Int array.
	* @see #l_RemoveItemFromintArray
	*/
	public static int[] addItemTo( int[] array, int item ) {
		array = Arrays.copyOf( array, array.length + 1 );
		array[ array.length - 1 ] = item;
		return array;
	}


	/**
	* Removes item with given index from Int array.
	* @see #l_AddItemToIntArray
	*/
	public static int[] removeItemFrom( int[] array, int index ) {
		int size = array.length;
		if( size == 1 ) {
			return null;
		} else {
			int[] newArray = new int[ size - 1 ];
			for( int n=0; n < size; n++ ) {
				if( n < index ) {
					newArray[ n ] = array[ n ];
				} else if( n > index ) {
					newArray[ n - 1 ] = array[ n ];
				}
			}
			return newArray;
		}
	}


	/**
	* Checks if Int value is in the interval between FromValue and ToValue.
	* @return True if Value is in FromValue...ToValue interval.
	* @see #l_LimitInt
	*/
	public static boolean inLimits( int value, int fromValue, int toValue ) {
		if( value >= fromValue && value <= toValue ) return true; else return false;
	}


	/**
	* Checks if Double value is in the interval between FromValue and ToValue.
	* @return True if Value is in FromValue...ToValue interval.
	* @see #l_IntInLimits
	*/
	public static boolean inLimits( double value, double fromValue, double toValue) {
		if( value >= fromValue && value <= toValue ) return true; else return false;
	}


	/**
	* Returns nearest power of 2.
	* @return Lowest power of 2 which is more than or equal to Value.
	* @see #l_IsPowerOf2
	*/
	public static double log2 = Math.log( 2 );

	public static int toPowerOf2( int value ) {
		return (int) Math.pow( 2.0d, Math.ceil( Math.log( value ) / log2 ) );
	}

	
	public static class Margins {
		public Vector min = new Vector();
		public Vector max = new Vector();
		
		public Margins set( double minX, double minY, double maxX, double maxY ) {
			min.x = minX;
			min.y = minY;
			max.x = maxX;
			max.y = maxY;
			return this;
		}
	}
	
	private static Vector serviceVector00 = new Vector();
	private static Vector serviceVector01 = new Vector();
	private static Vector serviceVector10 = new Vector();
	private static Vector serviceVector11 = new Vector();
	
	public static void getEscribedRectangle( double minX, double minY, double maxX, double maxY, Margins result ) {
		Shape viewport = Camera.current.viewport;
		Camera.current.screenToField( viewport.leftX(), viewport.topY(), serviceVector00 );
		Camera.current.screenToField( viewport.rightX(), viewport.topY(), serviceVector10 );
		Camera.current.screenToField( viewport.leftX(), viewport.bottomY(), serviceVector01 );
		Camera.current.screenToField( viewport.rightX(), viewport.bottomY(), serviceVector11 );
		result.min.x = Math.min( Math.min( serviceVector00.x - minX, serviceVector10.x + maxX ), 
				Math.min( serviceVector01.x - minX, serviceVector11.x + maxX ) );
		result.min.y = Math.min( Math.min( serviceVector00.y - minY, serviceVector10.y - minY ), 
				Math.min( serviceVector01.y + maxY, serviceVector11.y + maxY ) );
		result.max.x = Math.max( Math.max( serviceVector00.x - minX, serviceVector10.x + maxX ),
				Math.max( serviceVector01.x - minX, serviceVector11.x + maxX ) );
		result.max.y = Math.max( Math.max( serviceVector00.y - minY, serviceVector10.y - minY ),
				Math.max( serviceVector01.y + maxY, serviceVector11.y + maxY ) );
	}


	public static int versionToInt( String version, int totalChunks ) {
		String versions[] = version.split( "\\." );
		int intVersion = 0;
		for( int n=0; n <= totalChunks; n++ ) {
			intVersion = intVersion * 100;
			if( n < versions.length ) intVersion += Integer.parseInt( versions[ n ] );
		}
		return intVersion;
	}
	
	public static int versionToInt( String version ) {
		return versionToInt( version, 4 );
	}
	
	
	public static double random( double from, double to ) {
		return from + Math.random() * ( to - from );
	}

	public static double random( double range ) {
		return Math.random() * range;
	}
	

	public static int signum( double value ) {
		return ( value > 0d ? -1 : ( value < 0d ? 1 : 0 ) );
	}

	
	public static int floor( double value ) {
		return (int) Math.floor( value );
	}
	

	public static int ceil( double value ) {
		return (int) Math.ceil( value );
	}

	
	public static int round( double value ) {
		return (int) Math.round( value );
	}
	

	
	public static int powerOf2( int value ) {
		return value * value;
	}
	
	public static double powerOf2( double value ) {
		return value * value;
	}
	

	public static boolean inArray( int[] array, int value ) {
		if( array == null ) return true;
		for( int n = 0; n < array.length; n++ ) if( array[ n ] == value ) return true;
		return false;
	}
	
	
	private static TextFile classListFile;
	
	public static void generateClassesList( String packageName, String fileName ) {
		classListFile = TextFile.write( fileName );
		classListFile.writeLine( "package " + packageName + ";" );
		classListFile.writeLine( "import dwlab.base.Obj;" );
		classListFile.writeLine( "public class Classes {" );
		classListFile.writeLine( "\tpublic static void register() {" );
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		Enumeration<URL> resources = null;
		try {
			resources = classLoader.getResources( "" );
		} catch ( IOException ex ) {
			Logger.getLogger( Obj.class.getName() ).log( Level.SEVERE, null, ex );
		}
		List<File> dirs = new ArrayList<File>();
		while ( resources.hasMoreElements() ) {
			URL resource = resources.nextElement();
			dirs.add( new File( resource.getFile() ) );
		}
		for ( File directory : dirs ) try {
			addClasses( directory, "" );
		} catch ( ClassNotFoundException ex ) {
			Logger.getLogger( Obj.class.getName() ).log( Level.SEVERE, null, ex );
		}
		classListFile.writeLine( "\t}" );
		classListFile.writeLine( "}" );
		classListFile.close();
	}
		

	private static void addClasses( File directory, String packageName ) throws ClassNotFoundException {
		if( packageName.equals( "dwlab.shapes.sprites.shape_types" ) ) return;
		if( packageName.equals( "dwlab.shapes.line_segments.collision" ) ) return;
		if ( !directory.exists() ) return;
		File[] files = directory.listFiles();
		for (File file : files) {
			if ( file.isDirectory() ) {
				assert !file.getName().contains( "." );
				addClasses( file, packageName + ( packageName.isEmpty() ? "" : "." ) + file.getName() );
			} else if( file.getName().endsWith( ".class" ) ) {
				String name = file.getName();
				if( name.contains( "$" ) ) continue;
				Class cl = Class.forName( packageName + "." + name.substring( 0, name.length() - 6 ) );
				classListFile.writeLine( "\t\tObj.classes.put( \"" + cl.getSimpleName() + "\", " + cl.getCanonicalName() + ".class );" );
			}
		}
    }
}