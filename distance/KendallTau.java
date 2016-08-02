package distance;

import java.util.HashSet;
import java.util.Set;

public class KendallTau implements Distance {
	public static void main(String[] args) {
		KendallTau kt = new KendallTau();
		System.out.println(kt.getDistance("bat", "cat"));
		System.out.println(kt.getDistance("martha", "marhta"));
		System.out.println(kt.getDistance("duane", "dwayne"));
	}
	
	public KendallTau() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getDistance(Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			String s1 = o1.toString(), s2 = o2.toString();
			Set<Character> set = getUnion(s1, s2);
			double[] arr1 = asArray(s1, set), arr2 = asArray(s2, set);
			int cnt = 0, len = set.size();
			
			for (int i = 0; i < len; i++) {
				for(int j = i + 1; j < len; j++) {
					if ((arr1[i] < arr1[j] && arr2[i] > arr2[j]) || 
							(arr1[i] > arr1[j] && arr2[i] < arr2[j]))
						cnt++;
				}
			}
			
			return (cnt * 2.0d) / (len * len - 1); 
		}
	
		return 1d;
	}

	private double[] asArray(String s, Set<Character> set) {
		int len = set.size();
		double[] arr = new double[len];
		int idx = 0; //curr idx
		int st, sum, cnt, curr;
		double d;
		for (char c : set) {
			st = 0;
			cnt = 0;
			sum = 0;
			while ((curr = s.indexOf(c, st)) != -1) {
				sum += curr;
				cnt++;
				st = curr + 1;
				
				if (st > s.length())
					break;
			}
			
			d = (cnt == 0 && sum == 0) ? -1d : (sum * 1.0d) / cnt;
			arr[idx++] = d;
		}
		
		return arr;
	}

	private Set<Character> getUnion(String s1, String s2) {
		Set<Character> uset = asSet(s1);
		uset.addAll(asSet(s2));
		return uset;
	}
	
	private Set<Character> asSet(String s) {
		Set<Character> set = new HashSet<Character>();
		
		for (char c : s.toCharArray())
			set.add(c);
		
		return set;
	}
	
	@Override
	public String getName() {
		return "KendallTau";
	}

}
