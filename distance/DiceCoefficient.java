package distance;

import java.util.HashSet;
import java.util.Set;

public class DiceCoefficient implements Distance {
	
	public static void main(String[] args) {
		DiceCoefficient dc = new DiceCoefficient();
		System.out.println(dc.getDistance("martha", "marhta"));
		System.out.println(dc.getDistance("duane", "dwayne"));

	}
	
	public DiceCoefficient() {
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
		Set<String> bg1 = getBigrams(s1), bg2 = getBigrams(s2);
		Set<String> iset = new HashSet<String>(bg1);
		iset.retainAll(bg2);
		
		return (2.0d * iset.size()) / (bg1.size() + bg2.size());
	}

	private Set<String> getBigrams(String str) {
		int len = str.length();
		Set<String> retset = new HashSet<String>();
		
		for (int i = 0; i + 2 <= len; i++) {
			retset.add(str.substring(i, i+2));
		}
		
		return retset;
	}

	@Override
	public String getName() {
		return "DiceCoefficient";
	}



}
