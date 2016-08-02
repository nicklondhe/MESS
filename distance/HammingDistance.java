package distance;

public class HammingDistance implements Distance {

	public HammingDistance() {
		
	}

	@Override
	public double getDistance(Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			String s1 = o1.toString(), s2 = o2.toString();
			int len = s1.length();
			if (len == s2.length()) {
				double dist = 0;
				char l,r;
				for (int i = 0; i < len; i++) {
					l = s1.charAt(i);
					r = s2.charAt(i);
					
					dist += (l == r) ? 0 : 1;
				}
				
				return dist * 1d/len;
			}
			
			return 1;
		}
		return 1;
	}
	
	@Override
	public String getName() {
		return "Hamming";
	}

}
