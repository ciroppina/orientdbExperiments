package transitive.dependencies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Dependencies {

	private Map<String, String[]> _map;
	
	public Dependencies() {
		_map = new HashMap<String, String[]>();
	}

	public void add_direct(String item, String... deps) {
		for (String dep : deps) {
			//check for dep's circularities/loops before mapping
			if (!checkDependent(dep, item) ) _map.put(item, deps);
		}
	}

	//NEEDED TO CHECK CIRCULARITIES IN DEPENDENCIES
	private boolean checkDependent(String dep, String item) {
		boolean isDependent = false;
		String[] deps = dependencies_for(dep);

		for (String e : deps) {
			isDependent  = (e.equals(item)) ? true : false;
		}
		return isDependent;
	}

	private List<String> _res = new ArrayList<String>();
	public String[] dependencies_for(String key) {
		String[] deps = _map.get(key);
		if(deps == null) return getSorted(); //guard clause
		
		for (String dep : deps) recourse_for(dep);
		
		return getSorted();
	}

	private void recourse_for(String key) {
		if(! _res.contains(key)) _res.add(key);
		
		String[] deps = _map.get(key);
		if(deps==null) return; //guard clause
		
		for (String dep : deps)	recourse_for(dep);
	}

	private String[] getSorted() {
		String[] toBeSorted = _res.toArray(new String[0]) ; 
		_res = new ArrayList<String>();
		Arrays.sort(toBeSorted);
		return toBeSorted;
	}
	
	public void printAll() {
		for (String key : _map.keySet()) {
			String[] deps = dependencies_for(key);
			System.out.println(key
				+ " depends on: "
				+ Arrays.toString(deps)
			);
		}
	}

	@Override
	public String toString() {
		String[] collection = new String[_map.size()];
		int i = 0;
		for (String key : _map.keySet()) {
			String[] deps = dependencies_for(key);
			collection[i++] = key + " depends on: "
				+ Arrays.toString(deps);
		}
		
		return Arrays.toString(collection);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Dependencies))return false;
	    if (this.toString().trim().toLowerCase() 
    		.equals(other.toString().trim().toLowerCase()) ) return true;
	    return false;
	}
}
