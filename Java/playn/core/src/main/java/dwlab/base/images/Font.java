/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base.images;

import dwlab.base.XMLObject;
import dwlab.base.service.Align;
import dwlab.base.service.Vector;
import dwlab.shapes.Shape;
import dwlab.shapes.sprites.Camera;

/**
 * Bitmap font class.
 */
public class Font extends Image {
	private static final Vector servicePivot = new Vector();
	
	public int letterWidth[];
	public int fromNum;
	public int toNum;
	public boolean variableLength;


	/**
	 * Prints text using bitmap font.
	 * You should specify text, coordinates, font height and alignment.
	 * 
	 * @see #lTAlign, #printInShape
	 */
	public void print( String text, double x, double y, double height, Align horizontalAlignment, Align verticalAlignment ) {
		Camera.current.fieldToScreen( x, y, servicePivot );

		double scale = Camera.current.k * height / getHeight();
		
		switch( horizontalAlignment ) {
			case TO_CENTER:
				servicePivot.x = 0.5 * getWidth( text ) * scale;
				break;
			case TO_RIGHT:
				servicePivot.x = getWidth( text ) * scale;
				break;
		}

		switch( verticalAlignment ) {
			case TO_CENTER:
				servicePivot.y = 0.5 * height * scale;
				break;
			case TO_BOTTOM:
				servicePivot.y = height * scale;
				break;
		}

		for( int n=0; n < text.length(); n++ ) {
			if( text.charAt( n ) < fromNum || text.charAt( n ) > toNum ) error( "String contains letter that is out of font range" );
			int frame = text.charAt( n ) - fromNum;
			double realLetterWidth = scale;
			if( variableLength ) {
				realLetterWidth *= letterWidth[ frame ];
			} else {
				realLetterWidth *= getWidth();
			}
			draw( frame, servicePivot.x, servicePivot.y, scale * realLetterWidth, scale * height, 0 );
			servicePivot.x += realLetterWidth;
		}
	}

	public void print( String text, double x, double y, double fontHeightInUnits ) {
		print( text, x, y, fontHeightInUnits, Align.TO_CENTER, Align.TO_CENTER );
	}


	/**
	 * Prints text inside given shape using bitmap font.
	 * You should specify text, shape and alignment.
	 * 
	 * @see Align, print
	 */
	public void printInShape( String text, Shape shape, double height, Align horizontalAlignment, Align verticalAlignment ) {
		double x, y;

		switch( horizontalAlignment ) {
			case TO_LEFT:
				x = shape.leftX();
				break;
			case TO_RIGHT:
				x = shape.rightX();
				break;
			default:
				x = shape.getX();
				break;
		}

		switch( verticalAlignment ) {
			case TO_TOP:
				y = shape.topY();
				break;
			case TO_BOTTOM:
				y = shape.bottomY();
				break;
			default:
				y = shape.getY();
				break;
		}

		print( text, x, y, height, horizontalAlignment, verticalAlignment );
	}



	/**
	 * Returns text width in pixels.
	 * @return Text width in pixels for current bitmap font.
	 */
	public double getWidth( String text ) {
		if( !variableLength ) return getWidth() * text.length();
			
		double x = 0;
		for( int n=0; n < text.length(); n++ ) {
			if( text.charAt( n ) < fromNum || text.charAt( n ) > toNum ) error( "String contains letter that is out of font range" );
			if( variableLength ) {
				x += letterWidth[ text.charAt( n ) - fromNum ];
			} else {
				x += getWidth();
			}
		}
		return x;
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		fromNum = xMLObject.manageIntAttribute( "from-num", fromNum, 32 );
		toNum = xMLObject.manageIntAttribute( "to-num", toNum, 127 );
		letterWidth = xMLObject.manageIntArrayAttribute( "letter-width", letterWidth );
	}
}
