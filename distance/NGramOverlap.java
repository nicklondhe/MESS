package distance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NGramOverlap implements Distance {
	public static void main(String[] args) {
		NGramOverlap ngo = new NGramOverlap();
		System.out.println(ngo.getDistance("martha", "marhta"));
		System.out.println(ngo.getDistance("dwayne", "duane"));
	}
	
	public NGramOverlap() {
		
	}

	@Override
	public double getDistance(Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			return 1 - getSimilarity(o1.toString(), o2.toString());
		}
		return 1d;
	}

	private double getSimilarity(String s1, String s2) {
		List<String> s1grams = getNGrams(s1), s2grams = getNGrams(s2);
		Set<String> uset = new HashSet<String>(s1grams);
		Set<String> iset = new HashSet<String>(s1grams);
		
		uset.addAll(s2grams);
		iset.retainAll(s2grams);
		return (iset.size() * 1.0d) / uset.size();
	}

	private List<String> getNGrams(String str) {
		List<String> rlist = new ArrayList<String>();
		int len = str.length();
		
		for (int i = 1; i < 4; i++) { //unigrams thru trigrams
			for (int j = 0; j + i <= len; j++) {
				rlist.add(str.substring(j, i+j));
			}
		}
		
		return rlist;
	}
	
	@Override
	public String getName() {
		return "NGramOverlap";
	}

}
