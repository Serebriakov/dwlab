package dwlab.base.files;

import dwlab.base.Obj;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextFile extends Obj {
	private BufferedReader reader;
	private PrintWriter writer;
	
	
	public static TextFile read( String Filename ) {
		TextFile file = new TextFile();
		InputStream stream = null;
		try {
			stream = new FileInputStream( Filename );
		} catch ( FileNotFoundException ex ) {
			error( "Файл \"" + Filename + "\" не найден."  );
		}
		
		try {
			file.reader = new BufferedReader( new InputStreamReader( stream, "UTF8" ) );
		} catch ( UnsupportedEncodingException ex ) {
			Logger.getLogger( TextFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
		
		return file;
	}
	
	
	public static TextFile write( String Filename ) {
		TextFile file = new TextFile();
		try {
			file.writer = new PrintWriter( new FileWriter( Filename ) );
		} catch ( IOException ex ) {
			error( "Файл \"" + Filename + "\" не найден."  );
		}
		
		return file;
	}
	
	
	public String readLine() {
		String string = null;
		try {
			string = reader.readLine();
		} catch ( IOException ex ) {
			Logger.getLogger( TextFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
		return string;
	}
	

	public void writeLine( String string ) {
		writer.println( string );
	}
	

	public void close() {
		try {
			if( reader != null ) reader.close();
			if( writer != null ) writer.close();
		} catch ( IOException ex ) {
			Logger.getLogger( TextFile.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}
}
