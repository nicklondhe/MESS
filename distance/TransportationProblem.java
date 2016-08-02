package distance;

import java.util.ArrayList;
import java.util.List;

public class TransportationProblem {
	double[] required;
	double[] stock;
	double[][] cost;

	List<Variable> feasible = new ArrayList<TransportationProblem.Variable>();

	int stockSize;
	int requiredSize;

	public TransportationProblem(int stockSize, int requiredSize, 
			 double[] sck, double[] reqd, double[][] cst) {
		this.stockSize = stockSize;
		this.requiredSize = requiredSize;

		stock = new double[stockSize];
		required = new double[requiredSize];
		cost = new double[stockSize][requiredSize];

		for (int i = 0; i < (requiredSize + stockSize - 1); i++)
			feasible.add(new Variable());
		
		this.required = reqd;
		this.stock = sck;
		this.cost = cst;
	}
	
	
	public void setStock(double value, int index) {
		stock[index] = value;
	}

	public void setRequired(double value, int index) {
		required[index] = value;
	}

	public void setCost(double value, int stock, int required) {
		cost[stock][required] = value;
	}

	/**
	 * initializes the feasible solution list using the North-West Corner
	 * 
	 * @return time elapsed
	 */

	public void northWestCorner() {
		//long start = System.nanoTime();

		double min;
		int k = 0; // feasible solutions counter

		// isSet is responsible for annotating cells that have been allocated
		boolean[][] isSet = new boolean[stockSize][requiredSize];
		for (int j = 0; j < requiredSize; j++) {
			for (int i = 0; i < stockSize; i++) {
				isSet[i][j] = false;
			} 	
		}
		
		// the for loop is responsible for iterating in the 'north-west' manner
		for (int j = 0; j < requiredSize; j++) {
			for (int i = 0; i < stockSize; i++) {
				if (!isSet[i][j]) {

					// allocating stock in the proper manner
					min = Math.min(required[j], stock[i]);

					feasible.get(k).setRequired(j);
					feasible.get(k).setStock(i);
					feasible.get(k).setValue(min);
					k++;

					required[j] -= min;
					stock[i] -= min;

					// allocating null values in the removed row/column
					if (stock[i] == 0)
						for (int l = 0; l < requiredSize; l++)
							isSet[i][l] = true;
					else
						for (int l = 0; l < stockSize; l++)
							isSet[l][j] = true;
				}
			}
		}
		
		System.out.println("-------------------------------------------");
		for (Variable v : feasible) {
			System.out.println(v);
		}
		
		
		//return (System.nanoTime() - start) * 1.0e-9;
	}

	/**
	 * initializes the feasible solution list using the Least Cost Rule
	 * 
	 * it differs from the North-West Corner rule by the order of candidate
	 * cells which is determined by the corresponding cost
	 * 
	 * @return double: time elapsed
	 */

	public void leastCostRule() {
		//long start = System.nanoTime();

		double min;
		int k = 0; // feasible solutions counter

		// isSet is responsible for annotating cells that have been allocated
		boolean[][] isSet = new boolean[stockSize][requiredSize];
		for (int j = 0; j < requiredSize; j++) {
			for (int i = 0; i < stockSize; i++) {
				isSet[i][j] = false;
			}
		}
		
		int i = 0, j = 0;
		Variable minCost = new Variable();

		// this will loop is responsible for candidating cells by their least
		// cost
		while (k < (stockSize + requiredSize - 1)) {

			minCost.setValue(Double.MAX_VALUE);
			// picking up the least cost cell
			for (int m = 0; m < stockSize; m++)
				for (int n = 0; n < requiredSize; n++)
					if (!isSet[m][n])
						if (cost[m][n] < minCost.getValue()) {
							minCost.setStock(m);
							minCost.setRequired(n);
							minCost.setValue(cost[m][n]);
						}

			i = minCost.getStock();
			j = minCost.getRequired();

			// allocating stock in the proper manner
			min = Math.min(required[j], stock[i]);

			feasible.get(k).setRequired(j);
			feasible.get(k).setStock(i);
			feasible.get(k).setValue(min);
			k++;

			required[j] -= min;
			stock[i] -= min;

			// allocating null values in the removed row/column
			if (stock[i] == 0)
				for (int l = 0; l < requiredSize; l++)
					isSet[i][l] = true;
			else
				for (int l = 0; l < stockSize; l++)
					isSet[l][j] = true;

		}
		
		//System.out.println("--------------------------------------");
		//for (Variable v : feasible) {
			//System.out.println(v);
		//}

		//return (System.nanoTime() - start) * 1.0e-9;

	}

	public double[][] getSolution() {
		double[][] rv = new double[stockSize][requiredSize];
		
		for (int i = 0; i < stockSize; i++) {
			for (int j = 0; j < requiredSize; j++) {
				rv[i][j] = 0;
			}
		}
		
		for (Variable v : feasible) {
			rv[v.stock][v.required] = v.value;
		}
		
		return rv;

	}
	
	
	
	
	
	public static void main(String[] args) {
		TransportationProblem tp = new TransportationProblem(3, 3, new double[] {5,7,3},
				new double[] {7,3,5}, new double[][]{{3,1,Double.MAX_VALUE}, {4,2,4},{Double.MAX_VALUE,3,3,}}); //TODO
		tp.leastCostRule();
		
		System.out.println("------------------------------");
		tp = new TransportationProblem(3, 3, new double[] {5,7,3},
				new double[] {7,3,5}, new double[][]{{3,1,Double.MAX_VALUE}, {4,2,4},{Double.MAX_VALUE,3,3,}});
		tp.northWestCorner();
	}

	private class Variable {
		private int stock;
		private int required;
		private double value;

		public Variable() {
			this.stock = 0;
			this.required = 0;
		}

		/**
		 * @return the stock
		 */
		public int getStock() {
			return stock;
		}

		/**
		 * @param stock
		 *            the stock to set
		 */
		public void setStock(int stock) {
			this.stock = stock;
		}

		/**
		 * @return the required
		 */
		public int getRequired() {
			return required;
		}

		/**
		 * @param required
		 *            the required to set
		 */
		public void setRequired(int required) {
			this.required = required;
		}

		/**
		 * @return the value
		 */
		public double getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(double value) {
			this.value = value;
		}

		public String toString() {
			return "x[" + (this.stock + 1) + "," + (this.required + 1) + "] = "
					+ this.value;
		}
	}
}
