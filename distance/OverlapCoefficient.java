package distance;

import java.util.HashSet;
import java.util.Set;

public class OverlapCoefficient implements Distance {

	public OverlapCoefficient() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getDistance(Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			return 1 - getSimilarity(o1.toString(), o2.toString());
		}
		return 1d;
	}

	private double getSimilarity(String s1, String s2) {
		Set<Character> set1 = asSet(s1), set2 = asSet(s2);
		Set<Character> iset = new HashSet<Character>(set1);
		iset.retainAll(set2);
		return (iset.size() * 1.0d) / Math.min(set1.size(), set2.size());
	}

	private Set<Character> asSet(String s) {
		Set<Character> set = new HashSet<Character>();
		
		for (char c : s.toCharArray())
			set.add(c);
		
		return set;
	}
	
	@Override
	public String getName() {
		return "OverlapCoefficient";
	}

}
