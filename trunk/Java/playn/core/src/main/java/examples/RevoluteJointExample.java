package examples;

import dwlab.platform.Platform;
import dwlab.behavior_models.FixedJoint;
import dwlab.behavior_models.RevoluteJoint;
import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.platform.LWJGL;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.layers.World;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;

public class RevoluteJointExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new RevoluteJointExample() ).act();
		Platform.current.initCamera();
	}
	
	
	static final double gr15 = Math.PI / 12d;
	double period = 2.0;
	public World world;
	public Layer layer;
	public Sprite body;
	Sprite[] upperArm = new Sprite[ 2 ];
	Sprite[] lowerArm = new Sprite[ 2 ];
	Sprite[] upperLeg = new Sprite[ 2 ];
	Sprite[] lowerLeg = new Sprite[ 2 ];
	Sprite[] foot = new Sprite[ 2 ];
	String[] prefixes = { "inner_", "outer_" };

	
	@Override
	public void init() {
		world = World.fromFile( "res/human.lw" );
		layer = (Layer) world.findShape( Layer.class );
		body = (Sprite) layer.findShape( "body" );
		layer.findShape( "head" ).attachModel( FixedJoint.create( body ) );
		for( int n = 0; n <= 1; n++ ) {
			String prefix = prefixes[ n ];
			upperArm[ n ] = (Sprite) layer.findShape( prefix + "upper_arm" );
			lowerArm[ n ] = (Sprite) layer.findShape( prefix + "lower_arm" );
			upperArm[ n ].attachModel( new RevoluteJoint( body, 0, -0.333 ) );
			lowerArm[ n ].attachModel( new RevoluteJoint( upperArm[ n ], 0, -0.333 ) );
			layer.findShape( prefix + "fist" ).attachModel( FixedJoint.create( lowerArm[ n ]  ) );
			upperLeg[ n ] = (Sprite) layer.findShape( prefix + "upper_leg" );
			lowerLeg[ n ] = (Sprite) layer.findShape( prefix + "lower_leg" );
			foot[ n ] = (Sprite) layer.findShape( prefix + "foot" );
			upperLeg[ n ].attachModel( new RevoluteJoint( body, 0, -0.375 ) );
			lowerLeg[ n ].attachModel( new RevoluteJoint( upperLeg[ n ], 0, -0.375 ) );
			foot[ n ].attachModel( FixedJoint.create( lowerLeg[ n ] ) );
		}
		Camera.current.jumpTo( body );
		body.angle = gr15;
	}

	
	@Override
	public void logic() {
		double angle = gr15 * 24d / period * time;
		body.setY( -Math.sin( angle * 2d + gr15 * 16d ) * 0.25 - 5.5 );

		upperArm[ 0 ].angle = -Math.sin( angle + gr15 * 6d ) * gr15 * 4d;
		lowerArm[ 0 ].angle = upperArm[ 0 ].angle - gr15 * 3d - Math.sin( angle + gr15 * 6d ) * gr15 * 2d;
		upperLeg[ 0 ].angle = Math.cos( angle ) * gr15 * 3d;
		lowerLeg[ 0 ].angle = upperLeg[ 0 ].angle + Math.sin( angle + gr15 * 4d ) * gr15 * 4d + gr15 * 4d;

		upperArm[ 1 ].angle = Math.sin( angle + gr15 * 6d ) * gr15 * 4d;
		lowerArm[ 1 ].angle = upperArm[ 1 ].angle - gr15 * 3d + Math.sin( angle + gr15 * 6d ) * gr15 * 2d;
		upperLeg[ 1 ].angle = Math.cos( angle + gr15 * 12d ) * gr15 * 3d;
		lowerLeg[ 1 ].angle = upperLeg[ 1 ].angle + Math.sin( angle + gr15 * 16d ) * gr15 * 4d + gr15 * 4d;

		layer.act();
	}
	

	@Override
	public void render() {
		layer.draw();
		printText( "LTFixedJoint, LTRevoluteJoint example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
