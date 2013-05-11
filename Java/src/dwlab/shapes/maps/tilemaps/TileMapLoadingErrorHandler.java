package dwlab.shapes.maps.tilemaps;

import dwlab.base.Obj;

/**
 * Tile map loading error handler.
 * Intended to handle situation when binary tilemap file is missing.
 */
public class TileMapLoadingErrorHandler extends Obj {
	public void handleError( String fileName ) {
		error( "Tile map " + fileName + " cannot be loaded or not found." );
	}
}

