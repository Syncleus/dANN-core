package com.syncleus.tests.dann.associativemap;

import com.syncleus.dann.associativemap.Hyperpoint;
import org.junit.*;

public class TestHyperpoint
{
	@Test
	public void testDistances()
	{
		double[] valuesA = {1.0, 1.0, 1.0};
		double[] valuesB = {3.0, 3.0, 3.0};

		Hyperpoint pointA = new Hyperpoint(valuesA);
		Hyperpoint pointB = new Hyperpoint(valuesB);

		double result = pointA.calculateRelativeTo(pointB).getDistance();
		Assert.assertTrue("invalid distance dimension (relative): " + result, Math.abs(result - 3.46410161513775458705d) < 0.00001d);

		result = pointA.getDistance();
		Assert.assertTrue("invalid distance dimension (A): " + result, Math.abs(result - 1.73205080756887729352d) < 0.00001d);

		result = pointB.getDistance();
		Assert.assertTrue("invalid distance dimension (B): " + result, Math.abs(result - 5.19615242270663188058d) < 0.00001d);
	}

	@Test
	public void testAngles()
	{
		double[] valuesA = {1.0, 1.0, 1.0};
		double[] valuesB = {3.0, 3.0, 3.0};

		Hyperpoint pointA = new Hyperpoint(valuesA);
		Hyperpoint pointB = new Hyperpoint(valuesB);

		double result = pointA.calculateRelativeTo(pointB).getAngularComponent(1);
		Assert.assertTrue("angle(relative 1): " + result, Math.abs(result - 2.186276035465284d) < 0.00001d);

		result = pointA.calculateRelativeTo(pointB).getAngularComponent(2);
		Assert.assertTrue("angle(relative 2): " + result, Math.abs(result - 2.356194490192345d) < 0.00001d);

		result = pointA.getAngularComponent(1);
		Assert.assertTrue("angle(A 1): " + result, Math.abs(result - 0.9553166181245093d) < 0.00001d);

		result = pointA.getAngularComponent(2);
		Assert.assertTrue("angle(A 2): " + result, Math.abs(result - 0.7853981633974483d) < 0.00001d);

		result = pointB.getAngularComponent(1);
		Assert.assertTrue("angle(B 1): " + result, Math.abs(result - 0.9553166181245092d) < 0.00001d);

		result = pointB.getAngularComponent(2);
		Assert.assertTrue("angle(B 2): " + result, Math.abs(result - 0.7853981633974483d) < 0.00001d);

		valuesA = new double[]{1.0, 1.0};
		pointA = new Hyperpoint(valuesA);

		result = pointA.getAngularComponent(1);
		Assert.assertTrue("angle(45 degree): " + result, Math.abs(result - 0.7853981633974483d) < 0.00001d);
	}

	@Test
	public void testAddition()
	{
		double[] valuesA = {1.0, 1.0, 1.0};
		double[] valuesB = {3.0, 3.0, 3.0};

		Hyperpoint pointA = new Hyperpoint(valuesA);
		Hyperpoint pointB = new Hyperpoint(valuesB);

		double result = pointA.add(pointB).getAngularComponent(1);
		Assert.assertTrue("add(1): " + result, Math.abs(result - 0.9553166181245093d) < 0.00001d);

		result = pointA.add(pointB).getAngularComponent(2);
		Assert.assertTrue("add(2): " + result, Math.abs(result - 0.7853981633974483d) < 0.00001d);
	}
}
