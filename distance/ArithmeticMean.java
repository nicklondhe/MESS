package distance;

public class ArithmeticMean implements MeanAlgorithm {

	@Override
	public double getMean(double d1, double d2) {
		return (d1 + d2) / 2;
	}

	@Override
	public String getName() {
		return "AP";
	}

}
