package transitive.dependencies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import transitive.dependencies.DependencyTree.Node;

public class DependencyTreeTests {

	private DependencyTree _nodesTree;

	@Before
	public void setUp() throws Exception {
		_nodesTree = new DependencyTree();
	}

	@After
	public void tearDown() throws Exception {
		_nodesTree = null;
	}

	/**
	 * @param x
	 */
	private void buildTree(DependencyTree x) {
		Node A = x.new Node("A");
		Node B = x.new Node("B");
		Node C = x.new Node("C");
		A.append(B).append(C);
		x.add_to_root(A);		//adding Node A
		
		Node E = x.new Node("E");
		B.append(C).append(E);
		x.add_to_root(B);		//adding Node B
		
		Node G = x.new Node("G");
		C.append(G);
		x.add_to_root(C);		//adding Node C
		
		Node D = x.new Node("D");
		Node F = x.new Node("F");
		D.append(A).append(F);
		x.add_to_root(D);		//adding Node D
		
		E.append(F);
		x.add_to_root(E);		//adding Node E
		
		Node H = x.new Node("H");
		F.append(H);
		x.add_to_root(F);		//adding Node F
		
		A.append(H);
		x.add_to_root(A);		//adding Node A again, will not be added
		
		x.add_to_root(G);
		x.add_to_root(H);
	}
	
	@Test
	public final void testTreeBuilding() {
		DependencyTree x = _nodesTree;
		buildTree(x);

		assertTrue(x.size() == 8);
		//x.printAll(); //Kata output
		//System.out.println("_TREE_\n" + x.toString() );
	}

	@Test
	public final void testKataGivenInput() {
		//Kata 18 input
		DependencyTree x = _nodesTree;
		buildTree(x);
		
		//Kata 18 output
		String exp;
		//x.find("A").printDependenciesMatrix();	//matrix print
		
		exp = "A depends on: B C E F G H";
		assertTrue(exp.equals( x.find("A") .dependenciesLine()) );	//line print

		exp = "B depends on: C E F G H";
		assertTrue(exp.equals( x.find("B") .dependenciesLine()) );	//line print
		
		exp = "C depends on: G";
		assertTrue(exp.equals( x.find("C") .dependenciesLine()) );	//line print

		exp = "D depends on: A B C E F G H";
		assertTrue(exp.equals( x.find("D") .dependenciesLine()) );	//line print

		exp = "E depends on: F H";
		assertTrue(exp.equals( x.find("E") .dependenciesLine()) );	//line print

		exp = "F depends on: H";
		assertTrue(exp.equals( x.find("F") .dependenciesLine()) );	//line print

		exp = "G depends on: ";
		assertTrue(exp.equals( x.find("G") .dependenciesLine()) );	//line print

		exp = "H depends on: ";
		assertTrue(exp.equals( x.find("H") .dependenciesLine()) );	//line print
		
		Node expA = x.new Node("A");
		expA.append(x.new Node("B")).append(x.new Node("C")).append(x.new Node("H"));
		assertTrue(x.find("A") .equals(expA));
		expA = x.new Node("A");
		expA.append(x.new Node("B")).append(x.new Node("C")).append(x.new Node("G"));
		assertFalse(x.find("A") .equals(expA));
		
		String a = x.toString();
		_nodesTree = new DependencyTree();
		buildTree(_nodesTree);
		String e = _nodesTree.toString();
		assertTrue( a .equals(e) );
	}

	@Test
	public final void testDependencyLoopInput() {
		//circular input
		DependencyTree x = _nodesTree;
		//buildTree(x);
		Node A = x.new Node("A");
		Node B = x.new Node("B");
		Node C = x.new Node("C");
		x.add_to_root(A);
		x.add_to_root(B);
		x.add_to_root(C);
		
		A.append(B); B.append(C);
		//ERROR: looooooooooop, should not be added
		C.append(A); 
		
		//output
		String exp = "A depends on: B C";
		try {
			assertTrue(exp.equals( x.find("A") .dependenciesLine()) );
			//console 
			//x.printAll();
		} catch (Error e) {
			assertTrue(e instanceof StackOverflowError);
		}
	}

	@Test
	public final void testMyFamilyDependency() {
		DependencyTree x = _nodesTree;
		
		//input		
		Node CIRO = x.new Node("CIRO");
		Node PINA = x.new Node("PINA");
		Node ENRI = x.new Node("ENRI");
		Node LALA = x.new Node("LALA");
		Node SOLDI = x.new Node("SOLDI");
		
		x.add_to_root(CIRO); x.add_to_root(PINA);
		x.add_to_root(ENRI); x.add_to_root(LALA);
		x.add_to_root(SOLDI);
		
		CIRO.append(PINA).append(SOLDI);
		PINA.append(SOLDI);
		ENRI.append(CIRO).append(PINA).append(SOLDI);
		LALA.append(PINA).append(SOLDI);
		
		//output
		String lala = LALA.dependenciesLine();
		String enri = ENRI.dependenciesLine();
		String pina = PINA.dependenciesLine();
		String ciro = CIRO.dependenciesLine();
		String soldi = SOLDI.dependenciesLine();
		
		assertTrue( lala .equals("LALA depends on: PINA SOLDI"));
		assertTrue( enri .equals("ENRI depends on: CIRO PINA SOLDI"));
		assertTrue( pina .equals("PINA depends on: SOLDI"));
		assertTrue( ciro .equals("CIRO depends on: PINA SOLDI"));
		assertFalse(soldi.equals("SOLDI depends on: CIRO PINA ENRI LALA"));

		//console 
		//x.printAll();
	}

	@Test
	public final void testEquals() {
		DependencyTree x = _nodesTree;
		
		//tree
		Node CIRO = x.new Node("CIRO");
		Node PINA = x.new Node("PINA");
		Node ENRI = x.new Node("ENRI");
		Node LALA = x.new Node("LALA");
		Node SOLDI = x.new Node("SOLDI");
		
		x.add_to_root(CIRO); x.add_to_root(PINA);
		x.add_to_root(ENRI); x.add_to_root(LALA);
		x.add_to_root(SOLDI);
		
		CIRO.append(PINA).append(SOLDI);
		PINA.append(SOLDI);
		ENRI.append(CIRO).append(PINA).append(SOLDI);
		LALA.append(PINA).append(SOLDI);
		
		//another tree
		DependencyTree anOther = new DependencyTree();
		Node ORIC = anOther.new Node("CIRO");
		Node ANIP = anOther.new Node("PINA");
		Node IRNE = anOther.new Node("ENRI");
		Node ALAL = anOther.new Node("LALA");
		Node IDLOS = anOther.new Node("SOLDI");
		
		anOther.add_to_root(ORIC); anOther.add_to_root(ANIP);
		anOther.add_to_root(IRNE); anOther.add_to_root(ALAL);
		anOther.add_to_root(IDLOS);
		
		ORIC.append(ANIP).append(IDLOS);
		ANIP.append(IDLOS);
		IRNE.append(ORIC).append(ANIP).append(IDLOS);
		ALAL.append(ANIP).append(IDLOS);

		assertTrue(x .equals(anOther) );
	}
	
	@Test
	public final void aPerformanceTest() {
		DependencyTree x = _nodesTree;
		Long start;
		//tree
		Node CIRO = x.new Node("CIRO");
		Node PINA = x.new Node("PINA");
		Node ENRI = x.new Node("ENRI");
		Node LALA = x.new Node("LALA");
		Node SOLDI = x.new Node("SOLDI");
		
		start = System.currentTimeMillis();
		x.add_to_root(CIRO); 
		assertTrue(howLong(start) < 1.0d); //no-debug
		start = System.currentTimeMillis();
		x.add_to_root(PINA); x.add_to_root(ENRI); x.add_to_root(LALA);
		x.add_to_root(SOLDI);
		assertTrue(howLong(start) < 1.0d); //no-debug
		
		start = System.currentTimeMillis();
		CIRO.append(PINA).append(SOLDI);
		PINA.append(SOLDI);
		ENRI.append(CIRO).append(PINA).append(SOLDI);
		LALA.append(PINA).append(SOLDI);
		assertTrue(howLong(start) < 1.0d); //no-debug
		
		//output
		start = System.currentTimeMillis();
		LALA.dependenciesLine();
		ENRI.dependenciesLine();
		PINA.dependenciesLine();
		CIRO.dependenciesLine();
		SOLDI.dependenciesLine();
		assertTrue(howLong(start) < 1.0d); //no-debug
		
		//console 
		start = System.currentTimeMillis();
		x.printAll();
		assertTrue(howLong(start) < 1.0d); //no-debug
	}

	private Double howLong(Long start) {
		Long now = System.currentTimeMillis();
		Double res = (now - start) / 1000.0d ;
		System.out.println("it tooks " + res + " secs");
		return res;
	}
}
