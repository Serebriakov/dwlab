package examples;

import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.XMLObject;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import java.util.LinkedList;

public class XMLIOExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		Classes.register();
		( new XMLIOExample() ).act();
	}
	
	
	static LinkedList<Worker> people = new LinkedList<Worker>();
	static LinkedList<Profession> professions = new LinkedList<Profession>();
	final static String[] professionNames = { "director", "engineer", "dispatcher", "driver", "secretary", "bookkeeper", "supply agent", "bookkeeper chief",
				"lawyer", "programmer", "administrator", "courier" };

	ButtonAction generate = ButtonAction.create( KeyboardKey.create( Key.G ) );
	ButtonAction clear = ButtonAction.create( KeyboardKey.create( Key.C ) );
	ButtonAction save = ButtonAction.create( KeyboardKey.create( Key.F2 ) );
	ButtonAction load = ButtonAction.create( KeyboardKey.create( Key.F3 ) );

	
	@Override
	public void init() {
		for( String name : professionNames ) {
			Profession.create( name );
		}
	}

	
	@Override
	public void logic() {
		if( generate.wasPressed() ) {
			people.clear();
			for( int n = 1; n <= 10; n++ ) {
				Worker.create();
			}
		} else if( clear.wasPressed() ) {
			people.clear();
		} else if( save.wasPressed() ) {
			saveToFile( "people.dat" );
		} else if( load.wasPressed() ) {
			loadFromFile( "people.dat" );
		}
	}
	

	@Override
	public void render() {
		printText( "Press G to generate data, C to clear, F2 to save, F3 to load" );
		int y = 1;
		for( Worker worker : people ) {
			printText( worker.firstName + " " + worker.lastName + ", " + worker.age + ".years, " + Service.trim( worker.height, 1 ) + " cm, " + 
					Service.trim( worker.weight, 1 ) + " kg, " + worker.profession.name , y );
			y += 1;
		}
		printText( "XMLIO, Manage... example", Align.TO_CENTER, Align.TO_BOTTOM );
	}

	
	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );
		professions = xMLObject.manageListField( "professions", professions );
		people = xMLObject.manageChildList( people );
	}
}
