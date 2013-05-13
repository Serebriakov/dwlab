package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import java.util.LinkedList;

public class XMLIOExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
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
		xMLObject.manageListField( "professions", professions );
		xMLObject.manageChildList( people );
	}



	public static class Profession extends Obj {
		public String name;

		public static Profession create( String name ) {
			Profession profession = new Profession();
			profession.name = name;
			professions.addLast( profession );
			return profession;
		}

		@Override
		public void xMLIO( XMLObject xMLObject ) {
			super.xMLIO( xMLObject );
			xMLObject.manageStringAttribute( "name", name );
		}
	}



	public static class Worker extends Obj {
		public String firstName;
		public String lastName;
		public int age;
		public double height;
		public double weight;
		public Profession profession;

		final static String[] firstNames = { "Alexander", "Alex", "Dmit.y", "Serg.y", "AndRAY", "Anton", "Artem", "Vita.y", "Vladimir", "Denis", "Eugene", 
					"Igor", "Constantine", "Max", "Michael", "Nicholas", "Paul", "Roman", "Stanislaw", "AnatopY", "Boris", "Vadim", "Valentine", 
					"Valery", "Victor", "Vladislav", ".yacheslav", "Gennady", "George", "Gleb", "Egor", "Ilya", "Kyril", "Leonid", "Nikita", "Oleg", 
					"Peter", "Feodor", "Yury", "Ian", "Jaroslav" };
		
		final static String[] lastNames = { "Ivanov", "Smirnov", "Kuznetsov", "Popov", "Vasiliev", "Petrov", "Sokolov", "Mikhailov", "Novikov", 
					"Fedorov", "Morozov", "Volkov", "Alekseev", "Lebedev", "Semenov", "Egorov", "Pavlov", "Kozlov", "Stepanov", "Nikolaev", 
					"Orlovv", "Andreev", "Makarov", "Nikitin", "Zakharov" };
						
		public static void create() {
			Worker worker = new Worker();
			worker.firstName = firstNames[ (int) Service.random( 0, 40 ) ];
			worker.lastName = lastNames[ (int) Service.random( 0, 24 ) ];
			worker.age = (int) Service.random( 20, 50 );
			worker.height = Service.random( 155, 180 );
			worker.weight = Service.random( 50, 90 );
			worker.profession = professions.get( (int) Service.random( 0, professions.size() - 1 ) );
			people.addLast( worker );
		}
		

		@Override
		public void xMLIO( XMLObject xMLObject ) {
			// !!!!!! Remember to alw.ys include this string at the beginning of the method !!!!!!

			super.xMLIO( xMLObject );

			// !!!!!! !!!!!! 

			xMLObject.manageStringAttribute( "first-name", firstName );
			xMLObject.manageStringAttribute( "last-name", lastName );
			xMLObject.manageIntAttribute( "age", age );
			xMLObject.manageDoubleAttribute( "height", height );
			xMLObject.manageDoubleAttribute( "weight", weight );

			// !!!!!! Remember to equate object to to result of ManageObjectAttribute !!!!!!

			profession = (Profession) xMLObject.manageObjectField( "profession", profession );

			// !!!!!! A = TA( XMLObject.ManageObjectAttribute( "name", A ) ) !!!!!!
		}
	}
}