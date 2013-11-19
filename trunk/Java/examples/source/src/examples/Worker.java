package examples;

import static dwlab.platform.Functions.*;

import dwlab.base.Obj;
import dwlab.base.XMLObject;
import dwlab.base.service.Service;

public class Worker extends Obj {
	public String firstName;
	public String lastName;
	public int age;
	public double height;
	public double weight;
	public Profession profession;

	final static String[] firstNames = { "Alexander", "Alex", "Dmitry", "Sergey", "Andrew", "Anton", "Artem", "Vitaly", "Vladimir", "Denis", "Eugene", 
				"Igor", "Constantine", "Max", "Michael", "Nicholas", "Paul", "Roman", "Stanislaw", "Anatoly", "Boris", "Vadim", "Valentine", 
				"Valery", "Victor", "Vladislav", "Vyacheslav", "Gennady", "George", "Gleb", "Egor", "Ilya", "Cyril", "Leonid", "Nikita", "Oleg", 
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
		worker.profession = XMLIOExample.professions.get( (int) Service.random( 0, XMLIOExample.professions.size() - 1 ) );
		XMLIOExample.people.addLast( worker );
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		// !!!!!! Remember to always include this string at the beginning of the method !!!!!!

		super.xMLIO( xMLObject );

		// !!!!!! Remember to equate each parameter to the result of Manage function !!!!!!

		firstName = xMLObject.manageStringAttribute( "first-name", firstName );
		lastName = xMLObject.manageStringAttribute( "last-name", lastName );
		age = xMLObject.manageIntAttribute( "age", age );
		height = xMLObject.manageDoubleAttribute( "height", height );
		weight = xMLObject.manageDoubleAttribute( "weight", weight );
		profession = xMLObject.manageObjectField( "profession", profession );

		// !!!!!!
	}
}