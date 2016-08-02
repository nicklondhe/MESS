package distance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Split {
	/*
	 * This is the full combination
	 */
	
	private List<String> list;
	private int length;
	private double probability;
	private String str;
	private boolean term;
	private String rawStr;
	
	public Split() {
		list = new ArrayList<String>();
		length = 0;
		probability = 1d;
		str = "";
		rawStr = "";
		term = false;
	}
	
	public Split(String asStr, List<String> asLst, double prb) {
		list = asLst;
		str = asStr;
		rawStr = str.replaceAll("_", "");
		probability = prb;
		length = list.size();
	}
	
	@Override
	public String toString() {
		return str;
	}
	
	@Override
	public int hashCode() {
		return str.hashCode();
	}
	
	public void terminate(String compare) {
			term = compare.equals(rawStr);
	}
	
	public boolean isTerminated() {
		return term;
	}
	
	public List<String> getList() {
		return list;
	}
	
	public Split add(String s, double prb) {
		List<String> lst = new ArrayList<String>(list);
		String ss = str;
		double p = probability;
		lst.add(s);
		ss += (ss.isEmpty()) ? s : "_" + s;
		p *= prb;
		return new Split(ss, lst, p);
	}
	
	public int getLength() {
		return length;
	}
	
	public int getRawLength() {
		return rawStr.length();
	}
	
	public double getProbability() {
		return probability;
	}
	
	public void normalize(double factor) {
		probability /= factor;
	}
	
	public static Set<Split> getSplits(String string, HashMap<String, Double> prior) {
		Set<Split> set = new HashSet<Split>();
		set.add(new Split());
		boolean allTerminated = false;
		
		Collection<Split> tempColl, fullColl = new ArrayList<Split>();
		while (!allTerminated) {
			fullColl.clear();
			for (Split s : set) {
				tempColl = getSplits(string, prior, s);
				fullColl.addAll(tempColl);
			}
			
			set.clear();
			set.addAll(fullColl);
			
			allTerminated = areAllTerminated(set);
		}
		
		return set;
	}
	
	private static Collection<Split> getSplits(String string, HashMap<String, Double> prior, Split current) {
		Collection<Split> coll = new ArrayList<Split>();
		
		if (current.isTerminated()) {
			coll.add(current);
		} else {
			boolean isValid = true;
			String substr;
			
			int startIdx = current.getRawLength();
			int idx = startIdx + 1;
			int len = string.length();
			
			Split s;
			while (isValid && idx <= len) {
				substr = string.substring(startIdx, idx);
				
				if (prior.containsKey(substr)) {
					s = current.add(substr, prior.get(substr));
					s.terminate(string);
					coll.add(s);
					idx++;
				} else {
					isValid = false;
				}
			}
		}
		
		
		return coll;
	}
	
	private static boolean areAllTerminated(Set<Split> set) {
		boolean rv = true;
		
		for (Split s : set) {
			rv &= s.isTerminated();
			
			if (!rv)
				break;
		}
		return rv;
	}
 }
