package distance;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MostFrequentKChars implements Distance {
	private TreeMap<Character, Integer> leftTree;
	private TreeMap<Character, Integer> rightTree;
	
	
	public static void main(String[] args) {
		MostFrequentKChars mfk = new MostFrequentKChars();
		//System.out.println(mfk.getDistance("martha", "marhta"));
		//System.out.println(mfk.getDistance("duane", "dwayne"));
		System.out.println(mfk.getDistance("martha", "martha"));


	}
	
	public MostFrequentKChars() {

	}

	@Override
	public double getDistance(Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			String s1 = o1.toString(), s2 = o2.toString();
			leftTree = getMostFreqKHash(s1);
			rightTree = getMostFreqKHash(s2);
			
			int min = 2, max = Math.min(s1.length(), s2.length()) - 1;
			double dist = 0;
			
			int ctr = max - min + 1;
			
			for (int i = min; i <= max; i++) {
				dist += getMostFreqKDistance(i);
			}
			
			return dist / ctr;
		}
		
		return 1d;
	}

	private double getMostFreqKDistance(int i) {
		Set<Character> set1 = getPrunedSet(leftTree , i), set2 = getPrunedSet(rightTree, i);
		int simil = getMostFreqSimilarity(set1, set2);
		int max = 5 * i;
		return (max - simil) * 1.0d / max;
	}

	private Set<Character> getPrunedSet(TreeMap<Character, Integer> tree,
			int n) {
		HashSet<Character> set = new HashSet<Character>(tree.keySet());
		Character arr[] = new Character[n];
		int idx = 0;
		
		for (Character c : tree.keySet()) {
			arr[idx++] = c;
			
			if (idx == n )
				break;
		}
		
		set.retainAll(Arrays.asList(arr));
		return set;
	}

	private int getMostFreqSimilarity(Set<Character> set1, Set<Character> set2) {
		Set<Character> iset = new HashSet<Character>(set1);
		iset.retainAll(set2);
		
		int sum = 0;
		
		for (Character c : iset) {
			sum += leftTree.get(c);
		}
		
		return sum;
	}

	private TreeMap<Character, Integer> getMostFreqKHash(String string) {
		HashMap<Character, Integer> cmap = new HashMap<Character, Integer>();
		int val;
		for (char c : string.toCharArray()) {
			val = (cmap.containsKey(c)) ? cmap.get(c) : 0;
			val++;
			cmap.put(c, val);
		}
		
		TreeMap<Character, Integer> tmap = new TreeMap<Character, Integer>(new TMComparator(cmap, string));
		tmap.putAll(cmap);
		
		return tmap;
	}

	
	private class TMComparator implements Comparator<Character> {
		private Map<Character, Integer> map;
		private String str;
		
		private TMComparator(Map<Character, Integer> map, String str) {
			this.map = map;
			this.str = str;
		}
		
		@Override
		public int compare(Character o1, Character o2) {
			Integer i1 = map.get(o1), i2 = map.get(o2);
			
			if (i2.equals(i1)) {
				Integer idx1 = str.indexOf(o1), idx2 = str.indexOf(o2);
				return idx1.compareTo(idx2);
			} else
				return i2.compareTo(i1);
		}
		
	}
	
	@Override
	public String getName() {
		return "MostFrequentK";
	}

}
