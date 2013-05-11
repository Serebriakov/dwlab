/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2012, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sound {
	Audio sound;
	
	
	public Sound( String fileName ) {
		String format;
		fileName = fileName.toLowerCase();
		if( fileName.endsWith( ".wav" ) ) {
			format = "WAV";
		} else if( fileName.endsWith( ".ogg" ) ) {
			format = "OGG";
		} else {
			return;
		}
		
		try {
			sound = AudioLoader.getAudio( format, ResourceLoader.getResourceAsStream( fileName ) );
		} catch ( IOException ex ) {
			Logger.getLogger( Sound.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}
	
	
	public void play() {
		sound.playAsSoundEffect( 1f, 1f, false );
	}
	
	
	public void playLooped() {
		sound.playAsSoundEffect( 1f, 1f, true );
	}
	
	
	public void stop() {
		sound.stop();
	}
	

	public boolean isPlaying() {
		return sound.isPlaying();
	}	
}
