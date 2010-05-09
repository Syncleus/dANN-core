/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann.math;

import com.syncleus.dann.math.Vector;
import java.util.ArrayList;
import org.junit.*;

public class TestHyperpoint
{
	private static final double TEST_ACCEPTABLE_ERROR = 0.00001d;

	private static final double[] TEST_POINT_A = {1.0, 1.0, 1.0};
	private static final double[] TEST_POINT_B = {3.0, 3.0, 3.0};
	private static final double TEST_DISTANCE_A = 1.73205080756887729352d;
	private static final double TEST_DISTANCE_B = 5.19615242270663188058d;
	private static final double TEST_DISTANCE_AB = 3.46410161513775458705d;
	private static final double TEST_RELATIVE_AB_ANGLE_1 = -0.9553166181245093d;
	private static final double TEST_RELATIVE_AB_ANGLE_2 = 2.356194490192345d;
	private static final double TEST_A_ANGLE_1 = 0.9553166181245093d;
	private static final double TEST_A_ANGLE_2 = 0.7853981633974483d;
	private static final double TEST_B_ANGLE_1 = 0.9553166181245092d;
	private static final double TEST_B_ANGLE_2 = 0.7853981633974483d;


	@Test
	public void testDistances()
	{
		final Vector pointA = new Vector(TEST_POINT_A);
		final Vector pointB = new Vector(TEST_POINT_B);

		double result = pointA.calculateRelativeTo(pointB).getDistance();
		Assert.assertTrue("invalid distance (relative): " + result, Math.abs(result - TEST_DISTANCE_AB) < TEST_ACCEPTABLE_ERROR);

		result = pointA.getDistance();
		Assert.assertTrue("invalid distance dimension (A): " + result, Math.abs(result - TEST_DISTANCE_A) < TEST_ACCEPTABLE_ERROR);

		result = pointB.getDistance();
		Assert.assertTrue("invalid distance dimension (B): " + result, Math.abs(result - TEST_DISTANCE_B) < TEST_ACCEPTABLE_ERROR);
	}

	@Test
	public void testAngles()
	{
		Vector pointA = new Vector(TEST_POINT_A);
		final Vector pointB = new Vector(TEST_POINT_B);

		double result = pointA.calculateRelativeTo(pointB).getAngularComponent(1);
		Assert.assertTrue("angle(relative 1): " + result, Math.abs(result - TEST_RELATIVE_AB_ANGLE_1) < TEST_ACCEPTABLE_ERROR);

		result = pointA.calculateRelativeTo(pointB).getAngularComponent(2);
		Assert.assertTrue("angle(relative 2): " + result, Math.abs(result - TEST_RELATIVE_AB_ANGLE_2) < TEST_ACCEPTABLE_ERROR);

		result = pointA.getAngularComponent(1);
		Assert.assertTrue("angle(A 1): " + result, Math.abs(result - TEST_A_ANGLE_1) < TEST_ACCEPTABLE_ERROR);

		result = pointA.getAngularComponent(2);
		Assert.assertTrue("angle(A 2): " + result, Math.abs(result - TEST_A_ANGLE_2) < TEST_ACCEPTABLE_ERROR);

		result = pointB.getAngularComponent(1);
		Assert.assertTrue("angle(B 1): " + result, Math.abs(result - TEST_B_ANGLE_1) < TEST_ACCEPTABLE_ERROR);

		result = pointB.getAngularComponent(2);
		Assert.assertTrue("angle(B 2): " + result, Math.abs(result - TEST_B_ANGLE_2) < TEST_ACCEPTABLE_ERROR);

		final double distance = pointA.getDistance();
		pointA.setAngularComponent(1.2345, 1);
		pointA.setAngularComponent(0.5432, 2);
		Assert.assertTrue("distance drifted after setting angle", Math.abs(pointA.getDistance() - distance) < TEST_ACCEPTABLE_ERROR);

		pointA = new Vector(new double[]{1.0, 1.0});

		result = pointA.getAngularComponent(1);
		Assert.assertTrue("angle(45 degree): " + result, Math.abs(result - 0.7853981633974483d) < TEST_ACCEPTABLE_ERROR);
	}

	@Test
	public void testAddition()
	{
		final Vector pointA = new Vector(TEST_POINT_A);
		final Vector pointB = new Vector(TEST_POINT_B);

		double result = pointA.add(pointB).getAngularComponent(1);
		Assert.assertTrue("add(1): " + result, Math.abs(result - 0.9553166181245093d) < TEST_ACCEPTABLE_ERROR);

		result = pointA.add(pointB).getAngularComponent(2);
		Assert.assertTrue("add(2): " + result, Math.abs(result - 0.7853981633974483d) < TEST_ACCEPTABLE_ERROR);
	}

	@Test
	public void testAccessors()
	{
		Vector testPoint = new Vector(3);
		Assert.assertTrue("dimensions value is incorrect", testPoint.getDimensions() == 3);

		final Vector anotherTestPoint = new Vector(testPoint);
		Assert.assertTrue("dimensions value is incorrect", anotherTestPoint.getDimensions() == 3);

		final ArrayList<Double> points = new ArrayList<Double>();
		points.add(Double.valueOf(1.0d));
		points.add(Double.valueOf(2.0d));
		points.add(Double.valueOf(3.0d));
		testPoint = new Vector(points);

		Assert.assertTrue("dimensions value is incorrect", testPoint.getDimensions() == 3);
		Assert.assertTrue("coordinate 1 incorrect", testPoint.getCoordinate(1) == 1.0d);
		Assert.assertTrue("coordinate 2 incorrect", testPoint.getCoordinate(2) == 2.0d);
		Assert.assertTrue("coordinate 3 incorrect", testPoint.getCoordinate(3) == 3.0d);

		testPoint = testPoint.setCoordinate(5.0d, 1);
		testPoint = testPoint.setCoordinate(4.0d, 2);
		testPoint = testPoint.setCoordinate(3.0d, 3);
		double angle1 = testPoint.getAngularComponent(1);
		double angle2 = testPoint.getAngularComponent(2);
		testPoint = testPoint.setDistance(5.0d);
		Assert.assertTrue("distance didnt set properly", Math.abs(testPoint.getDistance() - 5.0d) < TEST_ACCEPTABLE_ERROR);
		Assert.assertTrue("angle drifted after setting distance", Math.abs(testPoint.getAngularComponent(1) - angle1) < TEST_ACCEPTABLE_ERROR);
		Assert.assertTrue("angle drifted after setting distance", Math.abs(testPoint.getAngularComponent(2) - angle2) < TEST_ACCEPTABLE_ERROR);

		testPoint = testPoint.setCoordinate(15.0d, 1);
		testPoint = testPoint.setCoordinate(14.0d, 2);
		testPoint = testPoint.setCoordinate(13.0d, 3);
		testPoint = testPoint.setDistance(10.0d);
		angle1 = testPoint.getAngularComponent(1);
		angle2 = testPoint.getAngularComponent(2);
		testPoint = testPoint.setDistance(5.0d);
		Assert.assertTrue("distance didnt set properly -> 5.0:" + testPoint.getDistance(), Math.abs(testPoint.getDistance() - 5.0d) < TEST_ACCEPTABLE_ERROR);
		Assert.assertTrue("angle drifted after setting distance -> " + angle1 + ':' + testPoint.getAngularComponent(1), Math.abs(testPoint.getAngularComponent(1) - angle1) < TEST_ACCEPTABLE_ERROR);
		Assert.assertTrue("angle drifted after setting distance -> " + angle2 + ':' + testPoint.getAngularComponent(2), Math.abs(testPoint.getAngularComponent(2) - angle2) < TEST_ACCEPTABLE_ERROR);

		testPoint = new Vector(2);
		testPoint = testPoint.setCoordinate(1.0d, 1);
		testPoint = testPoint.setCoordinate(1.0d, 2);
		testPoint = testPoint.setAngularComponent(Math.PI/8.0d, 1);
		Assert.assertTrue("angular component didnt set properly", Math.abs(testPoint.getAngularComponent(1) - Math.PI/8.0d) < TEST_ACCEPTABLE_ERROR);

		testPoint = new Vector(4);
		testPoint = testPoint.setCoordinate(1.0d, 1);
		testPoint = testPoint.setCoordinate(1.0d, 2);
		testPoint = testPoint.setCoordinate(1.0d, 3);
		testPoint = testPoint.setCoordinate(1.0d, 4);
		testPoint = testPoint.setAngularComponent(Math.PI/8.0d, 1);
		Assert.assertTrue("angular component didnt set properly", Math.abs(testPoint.getAngularComponent(1) - Math.PI/8.0d) < TEST_ACCEPTABLE_ERROR);

		testPoint = testPoint.setCoordinate(1.0d, 1);
		testPoint = testPoint.setCoordinate(1.0d, 2);
		testPoint = testPoint.setCoordinate(1.0d, 3);
		testPoint = testPoint.setCoordinate(1.0d, 4);
		testPoint = testPoint.setAngularComponent(Math.PI/8.0d, 2);
		Assert.assertTrue("angular component didnt set properly", Math.abs(testPoint.getAngularComponent(2) - Math.PI/8.0d) < TEST_ACCEPTABLE_ERROR);

		testPoint = testPoint.setCoordinate(1.0d, 1);
		testPoint = testPoint.setCoordinate(1.0d, 2);
		testPoint = testPoint.setCoordinate(1.0d, 3);
		testPoint = testPoint.setCoordinate(1.0d, 4);
		testPoint = testPoint.setAngularComponent(Math.PI/8.0d, 3);
		Assert.assertTrue("angular component didnt set properly", Math.abs(testPoint.getAngularComponent(3) - Math.PI/8.0d) < TEST_ACCEPTABLE_ERROR);
	}

	@Test
	public void testToString()
	{
		final ArrayList<Double> points = new ArrayList<Double>();
		points.add(Double.valueOf(1.0d));
		points.add(Double.valueOf(2.0d));
		points.add(Double.valueOf(3.0d));
		final Vector testPoint = new Vector(points);

		Assert.assertTrue(testPoint.toString() + " != {1.0,2.0,3.0}", testPoint.toString().compareToIgnoreCase("{1.0,2.0,3.0}") == 0);
	}
}
