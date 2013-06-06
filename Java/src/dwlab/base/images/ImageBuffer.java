package dwlab.base.images;

import dwlab.base.Obj;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ImageBuffer extends Obj {
	IntBuffer buffer;
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

	public Image toImage() {
		Image image = new Image( width, height );
		image.applyBuffer( this );
		return image;
	}
}