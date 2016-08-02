/**
 * 
 */
package distance;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author nicarus
 *
 */
public class TranslitEarthMoversDistance implements Distance {
	private HashMap<String, Double> srcPrior;
	private HashMap<String, Double> tgtPrior;
	private HashMap<String, HashMap<String, Double>> trnsProb;
	
	public TranslitEarthMoversDistance(HashMap<String, Double> srcPrior,
					HashMap<String, Double> tgtPrior, HashMap<String, HashMap<String, Double>> trnsMap) {
		this.srcPrior = srcPrior;
		this.tgtPrior = tgtPrior;
		this.trnsProb = trnsMap;
						
	}
	
	/* (non-Javadoc)
	 * @see distance.Distance#getDistance(java.lang.Object, java.lang.Object)
	 */
	@Override
	public double getDistance(Object o1, Object o2) {
		if (o1 != null && o2 != null)  {
			String s1 = o1.toString(), s2 = o2.toString();
			Set<Split> srcSplits = Split.getSplits(s1, srcPrior),
					tgtSplits = Split.getSplits(s2, tgtPrior);
			
			TransportationProblem tp;
			double[][] solution, weights;
			double minDist = Double.MAX_VALUE, currDist;
			if (srcSplits != null && tgtSplits != null) {
				for (Split supply : srcSplits) {
					for (Split demand : tgtSplits) {
						tp = initTp(supply, demand);
						tp.leastCostRule();
						solution = tp.getSolution();
						weights = tp.cost;
						currDist = compute(solution, weights);
						
						if (currDist < minDist) {
							minDist = currDist;
						}
					}
				}
			}
			
			return minDist;
		}
		
		return Double.MAX_VALUE;
	}

	private double compute(double[][] solution, double[][] weights) {
		double numer = 0, denom = 0;
		int rows = solution.length, cols = solution[0].length;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				numer += (solution[i][j] * weights[i][j]);
				denom += solution[i][j];
			}
		}
		
		return (numer / denom);
	}

	private TransportationProblem initTp(Split supply, Split demand) {
		List<String> spList = supply.getList(), dmList = demand.getList();
		int splen = spList.size(), dmlen = dmList.size();
		
		double[] spcost = new double[splen], dmcost = new double[dmlen];
		double[][] cost = new double[splen][dmlen];
		
		String sp, dm;
		
		for (int i = 0; i < splen; i++) {
			sp = spList.get(i);
			spcost[i] = srcPrior.containsKey(sp) ? srcPrior.get(sp) : 0d;
			for (int j = 0; j < dmlen; j++) {
				dm = dmList.get(j);
				dmcost[j] = tgtPrior.containsKey(dm) ? tgtPrior.get(dm) : 0d;
			}
			
			cost[i] = getCost(sp, dmList.toArray());
		}
		
		return new TransportationProblem(splen, dmlen, spcost, dmcost, cost);
	}

	private double[] getCost(String sp, Object... dm) {
		int len = dm.length;
		double[] rv = new double[len];
		String d;
		if (trnsProb.containsKey(sp)) {
			HashMap<String, Double> temp = trnsProb.get(sp);
			
			for (int i = 0; i < len; i++) {
				d = dm[i].toString();
				rv[i] = temp.containsKey(d) ? (1 - temp.get(d)) : 1d;
			}
		} else {
			for (int i = 0; i < len; i++) {
				rv[i] = 1;
			}
		}
		return rv;
	}

	/* (non-Javadoc)
	 * @see distance.Distance#getName()
	 */
	@Override
	public String getName() {
		return "TEMD";
	}

}
