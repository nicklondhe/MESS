package distance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class JaccardCoefficient implements Distance {
	public static void main(String[] args) {
		JaccardCoefficient jc = new JaccardCoefficient();
		System.out.println(jc.getDistance("martha", "marhta"));
		System.out.println(jc.getDistance("duane", "dwayne"));
	}
	

	public JaccardCoefficient() {
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
		Set<Character> set1 = new HashSet<Character>(getAsList(s1));
		Set<Character> set2 = new HashSet<Character>(getAsList(s2));
		Set<Character> uset = new HashSet<Character>(set1);
		Set<Character> iset = new HashSet<Character>(set1);
		uset.addAll(set2);
		iset.retainAll(set2);
		return (iset.size() * 1.0d) / uset.size();
	}

	private Collection<? extends Character> getAsList(String s) {
		char[] arr = s.toCharArray();
		ArrayList<Character> retlist = new ArrayList<Character>(arr.length);
		
		for (char c : arr)
			retlist.add(c);
		
		return retlist;
	}
	
	@Override
	public String getName() {
		return "JaccardDistance";
	}

}
