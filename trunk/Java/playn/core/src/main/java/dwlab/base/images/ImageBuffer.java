package dwlab.base.images;

import dwlab.base.Obj;
import static dwlab.platform.Functions.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ImageBuffer extends Obj {
	public IntBuffer buffer;
	private int width;
	private int height;
	
	
	public ImageBuffer( int width, int height ) {
		this.width = width;
		this.height= height;
		this.buffer = ByteBuffer.allocateDirect( 4 * width * height ).asIntBuffer();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getPixel( int x, int y ) {
		return buffer.get( x + y * width );
	}
	
	public void setPixel( int x, int y, int color ) {
		buffer.put( x + y * width, color );
	}
	
	public void clear( int color ) {
		for( int n = 0; n < width * height; n++ ) buffer.put( n, color );
	}

	public Image toImage() {
		AbstractTexture texture = createTexture( "" );
		texture.applyBuffer( this );
		return new Image( texture );
	}
}