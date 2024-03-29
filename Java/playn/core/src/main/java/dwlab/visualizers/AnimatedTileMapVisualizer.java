/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.visualizers;

import dwlab.shapes.maps.tilemaps.TileMap;

/**
 * This visualizer provides simple tile animation mechanism.
 */
public class AnimatedTileMapVisualizer extends Visualizer {
	/**
	 * Array of destination tile indexes which will override real file indexes of tilemap.
	 */
	public int tileNum[];



	@Override
	public int getTileValue( TileMap tileMap, int tileX, int tileY ) {
		return tileNum[ tileMap.value[ tileMap.wrapY( tileY ) ][ tileMap.wrapX( tileX ) ] ];
	}
}
