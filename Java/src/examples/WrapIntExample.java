package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;

public class WrapIntExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new WrapIntExample() ).act();
	}
	

	public void render() {
		Graphics.setColor( 255, 0, 0 );
		Graphics.drawEmptyRectangle( -1, -1, 102, 102 );
		Graphics.drawEmptyRectangle( 299, 249, 202, 102 );
		Graphics.setColor( 0, 255, 0 );
		Graphics.drawOval( wrapInt( mouseX(), 100 ) - 2, wrapInt( mouseY(), 100 ) - 2, 5, 5 );
		drawText( "L_WrapInt(" + mouseX() + ", 100)=" + wrapInt( mouseX(), 100 ), 0, 102 );
		Graphics.setColor( 0, 0, 255 );
		Graphics.drawOval( wrapInt2( mouseX(), 300, 500 ) - 2, wrapInt2( mouseY(), 250, 350 ) - 2, 5, 5 );
		drawText( "L_WrapInt2(" + mouseX() + ", 300, 500)=" + wrapInt2( mouseX(), 300, 500 ), 300, 352 );
		printText( "" );
		Graphics.setColor( 255, 255, 255 );
		printText( "L_WrapInt and L_WrapInt2 example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
