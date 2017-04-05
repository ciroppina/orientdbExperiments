package transitive.dependencies;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DependenciesTests {

	private Dependencies _dep;

	@Before
	public void setUp() throws Exception {
		_dep = new Dependencies();
	}

	@After
	public void tearDown() throws Exception {
		_dep = null;
	}

	@Test
	public final void testKataGivenInput() {
		//input
		_dep.add_direct("A", new String[] { "B", "C" } );
		_dep.add_direct("B", new String[] { "C", "E" } );
		_dep.add_direct("C", new String[] { "G"   	 } );
		_dep.add_direct("D", new String[] { "A", "F" } );
		_dep.add_direct("E", new String[] { "F"   	 } );
		_dep.add_direct("F", new String[] { "H"   	 } );
		
		//output
		assertArrayEquals( new String[] { "B", "C", "E", "F", "G", "H" }, _dep.dependencies_for("A") );
		assertArrayEquals( new String[] { "C", "E", "F", "G", "H" 	   }, _dep.dependencies_for("B") );
		assertArrayEquals( new String[] { "G"						   }, _dep.dependencies_for("C") );
		assertArrayEquals( new String[] { "A","B","C","E","F","G", "H" }, _dep.dependencies_for("D") );
		assertArrayEquals( new String[] { "F", "H" 					   }, _dep.dependencies_for("E") );
		assertArrayEquals( new String[] { "H" 					   	   }, _dep.dependencies_for("F") );

		//console 
		_dep.printAll();
	}

	@Test
	public final void testDependencyLoopInput() {
		//input
		_dep.add_direct("A", new String[] { "B" } );
		_dep.add_direct("B", new String[] { "C" } );
		//ERROR: looooooooooop, should not be added
		_dep.add_direct("C", new String[] { "A" } ); 
		
		//output
		assertArrayEquals( new String[] { "B", "C" }, _dep.dependencies_for("A") );
		assertArrayEquals( new String[] { "C"      }, _dep.dependencies_for("B") );
		assertArrayEquals( new String[] {          }, _dep.dependencies_for("C") ); // empty
		
		//console 
		_dep.printAll();
	}

	@Test
	public final void testMyFamilyDependency() {
		//input
		_dep.add_direct("CIRO", new String[] { "PINA", "SOLDI" } );
		_dep.add_direct("PINA", new String[] { "SOLDI" } );
		_dep.add_direct("ENRI", new String[] { "CIRO", "PINA", "SOLDI" } );
		_dep.add_direct("LALA", new String[] { "PINA", "SOLDI" } );
		
		//output
		assertArrayEquals( new String[] { "PINA", "SOLDI" }, _dep.dependencies_for("LALA") );
		assertArrayEquals( new String[] { "CIRO", "PINA", "SOLDI" }, _dep.dependencies_for("ENRI") );
		assertArrayEquals( new String[] { "PINA", "SOLDI" }, _dep.dependencies_for("CIRO") );
		
		//console 
		_dep.printAll();
	}

	@Test
	public final void testEquals() {
		//input
		_dep.add_direct("CIRO", new String[] { "PINA", "SOLDI" } );
		_dep.add_direct("PINA", new String[] { "SOLDI" } );
		_dep.add_direct("ENRI", new String[] { "CIRO", "PINA", "SOLDI" } );
		_dep.add_direct("LALA", new String[] { "PINA", "SOLDI" } );
		
		Dependencies anOther = new Dependencies();
		anOther.add_direct("CIRO", new String[] { "PINA", "SOLDI" } );
		anOther.add_direct("PINA", new String[] { "SOLDI" } );
		anOther.add_direct("ENRI", new String[] { "CIRO", "PINA", "SOLDI" } );
		anOther.add_direct("LALA", new String[] { "PINA", "SOLDI" } );

		assertTrue(_dep .equals(anOther) );
	}
}
