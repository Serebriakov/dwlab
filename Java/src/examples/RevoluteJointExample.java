package examples;
import dwlab.behavior_models.FixedJoint;
import java.lang.Math;
import dwlab.behavior_models.RevoluteJoint;
import dwlab.base.service.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.layers.World;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;

public class RevoluteJointExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new RevoluteJointExample() ).act();
	}
	
	
	double period = 2.0;
	public World world;
	public Layer Layer;
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
		Layer = (Layer) world.findShape( Layer.getClass() );
		body = (Sprite) Layer.findShape( "body" );
		Layer.findShape( "head" ).attachModel( FixedJoint.create( body ) );
		for( int n = 0; n <= 1; n++ ) {
			String prefix = prefixes[ n ];
			upperArm[ n ] = (Sprite) Layer.findShape( prefix + "upper_arm" );
			lowerArm[ n ] = (Sprite) Layer.findShape( prefix + "lower_arm" );
			upperArm[ n ].attachModel( new RevoluteJoint( body, 0, -0.333 ) );
			lowerArm[ n ].attachModel( new RevoluteJoint( upperArm[ n ], 0, -0.333 ) );
			Layer.findShape( prefix + "fist" ).attachModel( FixedJoint.create( lowerArm[ n ]  ) );
			upperLeg[ n ] = (Sprite) Layer.findShape( prefix + "upper_leg" );
			lowerLeg[ n ] = (Sprite) Layer.findShape( prefix + "lower_leg" );
			foot[ n ] = (Sprite) Layer.findShape( prefix + "foot" );
			upperLeg[ n ].attachModel( new RevoluteJoint( body, 0, -0.375 ) );
			lowerLeg[ n ].attachModel( new RevoluteJoint( upperLeg[ n ], 0, -0.375 ) );
			foot[ n ].attachModel( FixedJoint.create( lowerLeg[ n ] ) );
		}
		Camera.current.jumpTo( body );
		body.angle = 16;
	}

	
	@Override
	public void logic() {
		double angle = 360 / period * time;
		body.setY( -Math.sin( angle * 2 + 240 ) * 0.25 - 5.5 );

		upperArm[ 0 ].angle = -Math.sin( angle + 90 ) * 60;
		lowerArm[ 0 ].angle = upperArm[ 0 ].angle - 45 - Math.sin( angle + 90 ) * 30;
		upperLeg[ 0 ].angle = Math.cos( angle ) * 45;
		lowerLeg[ 0 ].angle = upperLeg[ 0 ].angle + Math.sin( angle + 60 ) * 60 + 60;

		upperArm[ 1 ].angle = Math.sin( angle + 90 ) * 60;
		lowerArm[ 1 ].angle = upperArm[ 1 ].angle - 45 + Math.sin( angle + 90 ) * 30;
		upperLeg[ 1 ].angle = Math.cos( angle + 180 ) * 45;
		lowerLeg[ 1 ].angle = upperLeg[ 1 ].angle + Math.sin( angle + 240 ) * 60 + 60;

		Layer.act();
	}
	

	@Override
	public void render() {
		Layer.draw();
		printText( "LTFixedJoint, LTRevoluteJoint example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
