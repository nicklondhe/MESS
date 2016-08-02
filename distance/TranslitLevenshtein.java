/**
 * Class that implements the Translit Levenshtein distance algorithm 
 */
package distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author nicarus
 *
 */
public class TranslitLevenshtein implements Distance {
	//instance variables for probabilities
	private HashMap<String, Double> sPrior;
	private HashMap<String, Double> tPrior;
	private HashMap<String, HashMap<String, Double>> trnsPrb;
	
	/**
	 * Default constructor
	 * @arg srcPrior : HashMap representing the prior probabilities for the source language
	 * @arg tgtPrior : HashMap representing the prior probabilities for the target language
	 * @arg translitPrblty : HashMap representing the transition probability
	 */
	public TranslitLevenshtein (HashMap<String, Double> srcPrior, HashMap<String, Double> tgtPrior,
			HashMap<String, HashMap<String, Double>> translitPrblty) {
		sPrior = srcPrior;
		tPrior = tgtPrior;
		trnsPrb = translitPrblty;
	}
	
	/* (non-Javadoc)
	 * @see distance.Distance#getDistance(java.lang.Object, java.lang.Object)
	 */
	@Override
	public double getDistance(Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			String s1 = o1.toString(), s2 = o2.toString();
			
			if (!s1.isEmpty() && !s2.isEmpty()) {
				//instantiate tree and simply return 1 - distance
				Tree tree = new Tree(s1, s2);
				return 1 - tree.getDistance();
			}
			
		}
		
		return Double.MAX_VALUE;
	}
	
	/* (non-Javadoc)
	 * @see distance.Distance#getName()
	 */
	@Override
	public String getName() {
		return "TRLE";
	}

	/**
	 * Class representing a tree for building paths and iterating over them
	 * We represent the full matrix as it were for Levenshtein as a tree
	 * The root node starts at top left corner. Each node (cell) has three
	 * children ie three neighbors: bottom right (subs), bottom (add) and 
	 * right (del)
	 */	
	private class Tree {
		//One copy of source and target strings being compared
		String src;
		String tgt;
		//Lengths of strings for iteration bounds
		int srcLen;
		int tgtLen;
		
		//final computed distances
		ArrayList<Double> distances;	
		
		/**
		 * Default constructor
		 * @arg s1 : Source string
		 * @arg s2 : Target string 
		 */
		private Tree (String s1, String s2) {
			//init instance variables
			src = s1;
			tgt = s2;
			srcLen = src.length();
			tgtLen = tgt.length();
			distances = new ArrayList<Double>();
			
			//first cell is always first character on both strings
			Cell c = new Cell(0, 1, 0, 1, 0, 0, 1);
			c.walkTree(); //will update distances as it walks the tree
		}
		
		/**
		 * Method to return the shortest distance as computed by the Tree
		 * @return The shortest computed distance if the computation was successful
		 * 0 otherwise
		 */
		private double getDistance() {
			if (!distances.isEmpty()) {
				Collections.sort(distances);
				return distances.get(distances.size() - 1);
			}
			
			return 0;
		}
		
		/**
		 * Class definition of Cell. A tree consists of cells.
		 * A cell marks the location in the string pairs and the
		 * cumulative probabilities
		 */
		private class Cell {
			//end locations in strings
			int srcE;
			int tgtE;

			//cumulative probabilities 
			double prior;
			double prb;
			double priorSum;
			int depth;
			
			//children - bottom, bottom right and right
			Cell add;
			Cell subs;
			Cell del;
			
			/**
			 * Default constructor
			 * @arg s1: Start position in source string
			 * @arg e1: End position in source string
			 * @arg s2: Start position in target string
			 * @arg e2: End position in target string
			 * @arg p: Prior probability of the current cell
			 * @arg ps: Sum of prior probabilities on the path till this cell
			 * @arg dep: The depth of the cell in the tree
			 */
			private Cell (int s1, int e1, int s2, int e2, double p, double ps, int dep) {
				srcE = e1;
				tgtE = e2;
				prior = p;
				priorSum = ps;
				depth = dep;
				
				//Get the substrings and look up transition probability
				String str1 = src.substring(s1, e1);
				String str2 = tgt.substring(s2, e2);
				double [] rv = getProbability(str1, str2);
				priorSum += rv[0];
				prb = rv[1];
				
				//if not improbable
				if (prb > 0) {
					//add and del are at the same depth
					if (e1 + 1 <= srcLen) {
						add = new Cell(s1, e1 + 1, s2, e2, p, ps, depth);
					}
					
					if (e2 + 1 <= tgtLen) {
						del = new Cell(s1, e1, s2, e2 + 1, p, ps, depth);
					}
					
					//substitute is at depth + 1
					if (add != null && del != null) {
						subs = new Cell(e1, e1 + 1, e2, e2 + 1, p + prb, priorSum, depth+1);
					}
				}
			}
			
			/**
			 * Recursively through the entire tree computing probabilities for leaf nodes
			 */
			private void walkTree() {
				if (amILeaf()) {
					distances.add((prior + prb) / priorSum); //distance on the path
				} else {
					//must have at least one child
					if (add != null) {
						add.walkTree();
					}
					
					if (subs != null) {
						subs.walkTree();
					}
					
					if (del != null) {
						del.walkTree();
					}
				}
			}
			
			/**
			 * Method to check if a cell is a leaf cell
			 * @return true if a leaf cell, false otherwise
			 */
			private boolean amILeaf() {
				//System.out.println(srcE + " @@@ " + srcLen + " @@@ " + tgtE + " @@@ " + tgtLen);
				return (srcE == srcLen  && tgtE == tgtLen);		
			}
		}
		
		/**
		 * Method to look up probabilities for a given string pair
		 * @arg src: Source string
		 * @arg tgt: Target string
		 * @return an array of probabilities, 0th cell is the product of priors
		 * 1st cell is the priors times transition probability
		 */
		private double[] getProbability(String src, String tgt) {
			
			double d = (sPrior.containsKey(src) ? sPrior.get(src) : 0) * (tPrior.containsKey(tgt) ? tPrior.get(tgt) : 0);
			double [] rv = new double[2];
			rv[0] = d;
			rv[1] = 0;
			if (d > 0 && trnsPrb.containsKey(src)) {
				HashMap<String, Double> temp = trnsPrb.get(src);
				
				if (temp.containsKey(tgt))
					rv[1] = d * temp.get(tgt);
				else
					rv[1] = 0;
			}
			
			return rv;
		}
	}

}
