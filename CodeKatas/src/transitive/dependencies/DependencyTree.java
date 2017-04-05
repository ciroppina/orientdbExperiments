package transitive.dependencies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DependencyTree {
	
	class Node implements Serializable, Comparable<Node> {
		private static final long serialVersionUID = 244002012097768169L;
		private String _name="";
		public String get_name() { return _name; }

		private Node[] _dependencies = new Node[0];
		public Node[] get_dependencies() { return _dependencies; }

		public Node(String _name, Node... dependencies) {
			super();
			this._name = _name;
			this.concat(dependencies);
		}
		
		void concat(Node[] deps) {
			Node[] result = Arrays.copyOf(_dependencies, _dependencies.length + deps.length);
			System.arraycopy(deps, 0, result, _dependencies.length, deps.length);
			this._dependencies = result;
		}

		Node append(Node node) {
			Node[] aDep = {node};
			this.concat(aDep);
			return this;
		}
		
		public Node[] get(String nodeName) {
			for (Node n : _dependencies) {
				if (n.get_name().equals(nodeName)) {
					return n.get_dependencies();
				}
			}
			return null;
		}

		public String dependenciesLine() {
			String res= "";
			Node[] deps = this.get_dependencies();
			for (Node n : deps) {
				if (this.equals(n)) break;	//circularities guard
				Node[] nodeDeps = n.get_dependencies();
				if (! res.contains(" " + n.get_name() + " "))
					res += n.get_name()	+ " ";
				res	= inDeep(nodeDeps, res);
			}
			String[] resArray = res.split(" " );
			Arrays.sort(resArray);
			res = "";
			for (String item : resArray) {
				res += item + " ";
			}
			System.out.println(""+ this.get_name() + " depends on: " + res.trim() );
			
			return this.get_name() + " depends on: " + res.trim();
		}

		private String inDeep(Node[] deps, String res) {
			for (Node n : deps) {
				if (this.equals(n)) break;	//circularities guard
				String name = n.get_name();
				if (! res.contains(" " + name + " "))
					res += name + " ";
				res = inDeep(n.get_dependencies(), res);
			}
			return res;
		}

		public void printDependenciesMatrix() {
			Node[] deps = this.get_dependencies();
			System.out.println(this.get_name()
					+ " depends on: "
					+ names(deps)
			);
			for (Node n : deps) {
				Node[] nodeDeps = dependencies_for(n);
				System.out.println(n.get_name()
					+ " depends on: "
					+ names(nodeDeps)
				);
			}
		}
		
		@Override
		public String toString() {
			String[] collection = new String[this.get_dependencies().length + 1];
			collection[0] = this.get_name() + " depends on: "
				+ names(this.get_dependencies()) + "\n";
			int i = 1;
			for (Node n: this.get_dependencies()) {
				Node[] deps = dependencies_for(n);
				collection[i++] = n.get_name() + " depends on: "
					+ names(deps) + "\n";
			}
			
			return Arrays.toString(collection);
		}
		
		@Override
		public int compareTo(Node n) {
			return (this.get_name().compareTo(n.get_name()));
		}
		
		@Override
		public boolean equals(Object other) {
			if (other == null) return false;
		    if (other == this) return true;
		    if (!(other instanceof Node)) return false;
		    Node n = (Node) other;
		    if (! this._name
	    		.equals( n.get_name()) ) {
		    	return false;
		    }
		    if (this.get_dependencies().length
	    		!= n.get_dependencies().length ) {
		    	return false;
		    }
		    Node[] tDep = this._dependencies;
		    Node[] nDep = n.get_dependencies();
		    for(int i=0; i < nDep.length; i++) {
		    	if (! tDep[i]._name .equals(nDep[i].get_name()) ) return false;
		    }
		    return true;
		}
	}
	
	private Node _root;

	public DependencyTree() {
		_root = new Node ("_", new Node[0]);
	}

	//for Kata 18 add_direct
	public void add_to_root(Node n) {
		//check for dep's circularities/loops before mapping
		if (checkDependent(n, n.get_name()) ) return;
		for (Node dep : _root._dependencies) {
			if (dep.get_name() .equals(n.get_name())) return;
		}
		_root.append(n);
	}

	//NEEDED TO CHECK CIRCULARITIES IN DEPENDENCIES
	private boolean checkDependent(Node dep, String item) {
		boolean isDependent = false;
		Node[] deps = dependencies_for(dep);

		for (Node e : deps) {
			isDependent  = (e.get_name().equals(item)) ? true : false;
		}
		return isDependent;
	}

	private List<Node> _res = new ArrayList<Node>();
	public Node[] dependencies_for(Node aNode) {
		Node[] deps = aNode.get_dependencies();
		if(deps == null) return getSorted(); //guard clause
		
		for (Node dep : deps) recourse_for(dep);
		
		return getSorted();
	}

	private void recourse_for(Node aNode) {
		if(! _res.contains(aNode)) _res.add(aNode);
		
		Node[] deps = aNode.get_dependencies();
		if(deps==null) return; //guard clause
		
		for (Node dep : deps)	recourse_for(dep);
	}
	
	public Node find(String nodeName) {
		Node res = null;
		for (Node node : _root._dependencies) {
			if (node.get_name() .equals(nodeName)) return node;
			Node[] deps = node .get_dependencies();
			
			res = recoursive_find(deps, nodeName);
		} 
		
		return res;
	}

	private Node recoursive_find(Node[] deps, String nodeName) {
		Node res = null;
		for (Node node : deps) {
			if (node.get_name() .equals(nodeName)) return node;
			Node[] dependencies = node.get_dependencies();
			
			res = recoursive_find(dependencies, nodeName);
		} 
		return res;
	}

	private Node[] getSorted() {
		Node[] toBeSorted = new Node[_res.size()];
		toBeSorted = _res.toArray(toBeSorted); 
		_res = new ArrayList<Node>();
		Arrays.sort(toBeSorted);
		return toBeSorted;
	}
	
	public void printAll() {
		System.out.println("___all_the_tree___");
		Node[] deps = _root.get_dependencies();
		for (Node n : deps) {
			Node[] nodeDeps = dependencies_for(n);
			System.out.println(n.get_name()
				+ " depends on: "
				+ names(nodeDeps)
			);
		}
		System.out.println("___end_of_tree___");
	}

	private String names(Node[] nodeDeps) {
		String res = "";
		for (Node node : nodeDeps) {
			res += "" + node.get_name() + " ";
		}
		return res;
	}

	@Override
	public String toString() {
		String[] collection = new String[_root.get_dependencies().length];
		int i = 0;
		for (Node n: _root.get_dependencies()) {
			Node[] deps = dependencies_for(n);
			collection[i++] = n.get_name() + " depends on: "
				+ names(deps) + "\n";
		}
		
		return Arrays.toString(collection);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof DependencyTree))return false;
	    if (this.toString().trim().toLowerCase() 
    		.equals(other.toString().trim().toLowerCase()) ) return true;
	    return false;
	}
	
	public long size() {
		return new Long(_root._dependencies.length);
	}
}
