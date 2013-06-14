package dwlab.base.files;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryFile {
	private ByteBuffer buffer;
	private DataOutputStream outStream = null;
	
	public static BinaryFile read( String fileName ) {
		byte[] bytes = null;
		try {
			FileInputStream stream = new FileInputStream( new File( fileName ) );
			bytes = new byte[ stream.available() ];
			stream.read( bytes );
			stream.close();
		} catch ( IOException ex ) {
			Logger.getLogger( BinaryFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
		
		BinaryFile file = new BinaryFile();
		file.buffer = ByteBuffer.wrap( bytes );
		return file;
	}
	
	public static BinaryFile write( String fileName ) {
		BinaryFile file = new BinaryFile();
		try {
			file.outStream = new DataOutputStream( new FileOutputStream( new File( fileName ) ) );
		} catch ( FileNotFoundException ex ) {
			Logger.getLogger( BinaryFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
		return file;
	}

	public void close() {
		try {
			if( outStream != null ) outStream.close();
		} catch ( IOException ex ) {
			Logger.getLogger( BinaryFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}

	public void seek( int offset ) {
		buffer.position( offset );
	}

	public int readByte() {
		return buffer.get();
	}

	public int readShort() {
		return buffer.getShort();
	}

	public int readInt() {
		return buffer.getInt();
	}

	public void writeByte( int value ) {
		try {
			outStream.writeByte( value );
		} catch ( IOException ex ) {
			Logger.getLogger( BinaryFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}

	public void writeShort( int value ) {
		try {
			outStream.writeShort( value );
		} catch ( IOException ex ) {
			Logger.getLogger( BinaryFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}

	public void writeInt( int value ) {
		try {
			outStream.writeInt( value );
		} catch ( IOException ex ) {
			Logger.getLogger( BinaryFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}
}
