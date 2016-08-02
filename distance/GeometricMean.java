package distance;

public class GeometricMean implements MeanAlgorithm {

	@Override
	public double getMean(double d1, double d2) {
		return Math.sqrt(d1 * d2);
	}

	@Override
	public String getName() {
		return "GP";
	}

}
