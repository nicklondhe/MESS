package distance.test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import distance.TransliterationHammingDistance;

public class TranslitHammingDistTest {
	private static HashMap<String, Double> testPrior = new HashMap<String, Double>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testPrior.put("h", 0.145d);
		testPrior.put("e", 0.14d);
		testPrior.put("l", 0.135d);
		testPrior.put("o", 0.13d);
		testPrior.put("he", 0.09d);
		testPrior.put("el", 0.08d);
		testPrior.put("ll", 0.07d);
		testPrior.put("lo", 0.06d);
		testPrior.put("hel", 0.05d);
		testPrior.put("ell", 0.05d);
		testPrior.put("llo", 0.05d);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testPrior.clear();
		testPrior = null;
	}

	@Test
	public final void testGetSplits() {
		Method m = getMethod("getSplits", String.class, HashMap.class, int.class);
		TransliterationHammingDistance thd = new TransliterationHammingDistance(null, testPrior, null, null);
		if (m != null) {
			try {
				List<String> retval = (List<String>) m.invoke(thd, "hello", testPrior, 5);
				assertArrayEquals(new Object[] {"h","e","l","l","o"}, retval.toArray());
				
				retval = (List<String>) m.invoke(thd, "hello", testPrior, 4);
				assertArrayEquals(new Object[] {"he","l","l","o"}, retval.toArray());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Method getMethod(String methodName, Class... args) {
		try {
			Method m = TransliterationHammingDistance.class.getDeclaredMethod(methodName, args);
			m.setAccessible(true);
			return m;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
