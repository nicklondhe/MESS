package distance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Class to compute Translit hamming distance
 */
public class TransliterationHammingDistance implements Distance {
	//Maps to store probabilities
	HashMap<String, HashMap<String, Double>> transitionProbability;
	HashMap<String, Double> srcPrior;
	HashMap<String, Double> tgtPrior;
	
	//placeholder for mean computation - final impl uses arithmetic mean
	private MeanAlgorithm algo;
	
	/**
	 * Default constructor
	 * @arg transitionProbability : HashMap representing transition probability
	 * @arg srcPrior : HashMap representing source prior probability
	 * @arg tgtPrior : HashMap representing target prior probability
	 * @arg algo : The mean computation algorithm to use
	 */
	public TransliterationHammingDistance(HashMap<String, HashMap<String, Double>> transitionProbability,
			HashMap<String, Double> srcPrior, HashMap<String, Double> tgtPrior, MeanAlgorithm algo) {
		this.transitionProbability = transitionProbability;
		this.srcPrior = srcPrior;
		this.tgtPrior = tgtPrior;
		this.algo = algo;
	}

	@Override
	public double getDistance(Object object1, Object object2) {
		/*
		 * The basic algorithm is as follows when given two strings s1 and s2
		 * 
		 * 	1. Split s1 and s2 into char sequences of equal length based on prior probabilities
		 *  2. Distance = sum of (1 - trans prob) in case of a mismatch / length of split
		 */
		
		if (object1 != null && object2 != null) {
			String s1 = object1.toString(), s2 = object2.toString();
			int len1 = s1.length(), len2 = s2.length();
			
			/*
			 * Now minLen is the max length
			 * considering trigrams, the min length of the strings will be ceil(minLen/3)
			 */
			int maxLen = Math.min(len1, len2), minLen = (int) Math.ceil(maxLen * 1.0d / 3.0d);
			
			ArrayList<Split> left, right;
			double dist, minDist = Double.MAX_VALUE;
			HashMap<Integer, ArrayList<Split>> lsplit = getPartitionedSplits(s1, srcPrior), rsplit = getPartitionedSplits(s2, tgtPrior);
			for (int i = minLen; i <= maxLen; i++) {
				left = lsplit.get(i);
				right = rsplit.get(i);
				
				if (left != null && right != null) {
					
					for (Split ls : left) {
						for (Split rs : right) {
							dist = computeDistance(ls.getList(), rs.getList(), i);
							dist = algo.getMean(dist, ls.getProbability() *rs.getProbability());
							dist = 1 - dist;
							
							if (dist < minDist) {
								minDist = dist;
							}
						}
					}
				} 
			}
			
			return minDist == Double.MAX_VALUE ? 1 : minDist;
			
		}
		
		return Double.MAX_VALUE;
	}

	/**
	 * Method to compute distance over a collection of splits
	 * @arg lsplit : Collection of source splits
	 * @arg rsplit : Collection of target splits
	 * @arg len : Length of splits
	 */
	private double computeDistance(List<String> lsplit, List<String> rsplit, int len) {
		double d = 0;
		
		for (int i = 0; i < len; i++) {
			d += getProbability(lsplit.get(i), rsplit.get(i));
		}
		
		return d / len;
	}

	/**
	 * Method to compute the probability for a given string pair
	 * @arg src : The source string
	 * @arg tgt : The target string
	 * @return The transition probability between the two strings
	 */
	private double getProbability(String src, String tgt) {
		if (transitionProbability.containsKey(src)) {
			HashMap<String, Double> transitions = transitionProbability.get(src);
			return (transitions.containsKey(tgt)) ? transitions.get(tgt) : 0d;
		}
		return 0;
	}
	
	/**
	 * Method to get splits partitioned by length for a given string
	 * @arg string : String to be split
	 * @arg prior : A map representing the prior probability to split string on
	 * @return a map containing the length of the split as key and list of splits applicable for that length
	 */
	private HashMap<Integer, ArrayList<Split>> getPartitionedSplits(String string, HashMap<String, Double> prior) {
		return partitionBySize(Split.getSplits(string, prior));
	}
	
	/**
	 * Method to partition a set of splits by split length
	 * @arg set: The set to split
	 * @return The set converted into a map partitioned by split size
	 */
	private HashMap<Integer, ArrayList<Split>> partitionBySize(Set<Split> set) {
		HashMap<Integer, ArrayList<Split>> map = new HashMap<Integer, ArrayList<Split>>();
		ArrayList<Split> temp;
		int len;
		double norm = 0;
		for (Split s : set) {
			len = s.getLength();
			norm += s.getProbability();
			temp = map.containsKey(len) ? map.get(len) : new ArrayList<Split>();
			temp.add(s);
			map.put(len, temp);
		}
		
		for (ArrayList<Split> vals : map.values()) {
			for (Split v : vals) {
				v.normalize(norm);
			}
		}
		
		return map;
	}

	@Override
	public String getName() {
		return "TranslitHamming-"+algo.getName();
	}
	

}
