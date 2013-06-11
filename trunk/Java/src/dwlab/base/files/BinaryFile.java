package dwlab.base.files;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryFile {
	private FileChannel channel;
	private BufferedOutputStream outStream = null;
	
	public BinaryFile( String fileName ) {
		channel = FileChannel.open( new File( fileName ) );
	}

	public void close() {
		try {
			if( inStream != null ) inStream.close();
			if( outStream != null ) outStream.close();
		} catch ( IOException ex ) {
			Logger.getLogger( BinaryFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}

	public void seek( int offset ) {
		inStream.
	}

	public int readByte() {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	public int readShort() {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	public int readInt() {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	public void writeByte( int i ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	public void writeShort( int i ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	public void writeInt( int i ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}
}
