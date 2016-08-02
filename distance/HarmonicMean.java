package distance;

public class HarmonicMean implements MeanAlgorithm {

	@Override
	public double getMean(double d1, double d2) {
		return (2 * d1 * d2) / (d1 + d2);
	}

	@Override
	public String getName() {
		return "HP";
	}

}
