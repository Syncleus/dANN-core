/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann.dataprocessing.signal.transform;

import com.syncleus.dann.math.ComplexNumber;
import com.syncleus.dann.dataprocessing.signal.transform.DiscreteFourierTransform;
import java.util.Random;
import org.junit.*;

public class TestDiscreteFourierTransform
{
	private static final Random RANDOM = new Random();
	private static final int DATA_POINT_COUNT = 1024;

	private static ComplexNumber[] generateRandomComplex(int count)
	{
		ComplexNumber[] randomComplex = new ComplexNumber[count];
		for(int randomComplexIndex = 0; randomComplexIndex < randomComplex.length; randomComplexIndex++)
			randomComplex[randomComplexIndex] = new ComplexNumber(RANDOM.nextDouble() * 10.0, RANDOM.nextDouble() * 10.0);
		return randomComplex;
	}

	@Test
	public void testFrequencyMapping()
	{
		DiscreteFourierTransform dft = new DiscreteFourierTransform(generateRandomComplex(DATA_POINT_COUNT), DATA_POINT_COUNT/4);
		Assert.assertTrue("minimum frequency mapping is incorrect: " + dft.getMinimumFrequency(), Math.abs(dft.getMinimumFrequency()  - 0.0) < 0.001);
		Assert.assertTrue("maximum frequency mapping is incorrect: " + dft.getMaximumFrequency(), Math.abs(dft.getMaximumFrequency()  - 128.0) < 0.001);

		dft = new DiscreteFourierTransform(generateRandomComplex(DATA_POINT_COUNT), DATA_POINT_COUNT);
		Assert.assertTrue("minimum frequency mapping is incorrect: " + dft.getMinimumFrequency(), Math.abs(dft.getMinimumFrequency()  - 0.0) < 0.001);
		Assert.assertTrue("maximum frequency mapping is incorrect: " + dft.getMaximumFrequency(), Math.abs(dft.getMaximumFrequency()  - 512.0) < 0.001);
	}
}
