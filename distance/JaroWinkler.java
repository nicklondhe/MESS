package distance;

public class JaroWinkler implements Distance {
	private String compOne;
	private String compTwo;
	private String theMatchA;
	private String theMatchB;
	int mRange;
	
	public static void main(String[] args) {
		JaroWinkler jw = new JaroWinkler();
		System.out.println(jw.getDistance("dipti", "deepti"));
		System.out.println(jw.getDistance("xinyun", "shinyun"));
	}
	
	public JaroWinkler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getDistance(Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			clear();
			String s1 = o1.toString(), s2 = o2.toString();
			int l1 = s1.length(), l2 = s2.length();
			return (l1 <= l2) ? 1 - getSimilarity(s1, s2) : 1 -getSimilarity(s2, s1);
		}
		return 1d;
	}

	private void clear() {
		compOne = null;
		compTwo = null;
		theMatchA = null;
		theMatchB = null;
		mRange = -1;
		
	}

	private double getSimilarity(String s1, String s2) {
		compOne = s1;
		compTwo = s2;

		mRange = Math.max(compOne.length(), compTwo.length()) / 2 - 1;

		double res = -1;

		int m = getMatch();
		int t = 0;
		if (getMissMatch(compTwo, compOne) > 0) {
			t = (getMissMatch(compOne, compTwo) / getMissMatch(compTwo, compOne));
		}

		int l1 = compOne.length();
		int l2 = compTwo.length();

		double f = 0.3333;
		double mt = (double) (m - t) / m;
		double jw = f * ((double) m / l1 + (double) m / l2 + (double) mt);
		res = jw + getCommonPrefix(compOne, compTwo) * (0.1 * (1.0 - jw));

		return res;
	}

	private int getMatch() {

		theMatchA = "";
		theMatchB = "";

		int matches = 0;

		for (int i = 0; i < compOne.length(); i++) {
			// Look backward
			int counter = 0;
			while (counter <= mRange && i >= 0 && counter <= i) {
				if (compOne.charAt(i) == compTwo.charAt(i - counter)) {
					matches++;
					theMatchA = theMatchA + compOne.charAt(i);
					theMatchB = theMatchB + compTwo.charAt(i);
				}
				counter++;
			}

			// Look forward
			counter = 1;
			while (counter <= mRange && i < compTwo.length()
					&& counter + i < compTwo.length()) {
				if (compOne.charAt(i) == compTwo.charAt(i + counter)) {
					matches++;
					theMatchA = theMatchA + compOne.charAt(i);
					theMatchB = theMatchB + compTwo.charAt(i);
				}
				counter++;
			}
		}
		return matches;
	}

	private int getMissMatch(String s1, String s2) {
		int transPositions = 0;

		for (int i = 0; i < theMatchA.length(); i++) {
			// Look Backward
			int counter = 0;
			while (counter <= mRange && i >= 0 && counter <= i) {
				if (theMatchA.charAt(i) == theMatchB.charAt(i - counter)
						&& counter > 0) {
					transPositions++;
				}
				counter++;
			}

			// Look forward
			counter = 1;
			while (counter <= mRange && i < theMatchB.length()
					&& (counter + i) < theMatchB.length()) {
				if (theMatchA.charAt(i) == theMatchB.charAt(i + counter)
						&& counter > 0) {
					transPositions++;
				}
				counter++;
			}
		}
		return transPositions;
	}

	private int getCommonPrefix(String compOne, String compTwo) {
		int cp = 0;
		for (int i = 0; i < 4 && i < compOne.length() && i < compTwo.length(); i++) {
			if (compOne.charAt(i) == compTwo.charAt(i))
				cp++;
		}
		return cp;
	}
	
	@Override
	public String getName() {
		return "JaroWinkler";
	}
}
