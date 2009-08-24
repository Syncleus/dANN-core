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
package com.syncleus.tests.dann.math.transform;

import com.syncleus.dann.math.ComplexNumber;
import com.syncleus.dann.math.transform.CooleyTukeyFastFourierTransformer;
import com.syncleus.dann.math.transform.DiscreteFourierTransform;
import java.util.Map.Entry;
import java.util.Random;
import org.junit.*;

public class TestCooleyTukeyFastFourierTransformer
{
	private static double[] generateSignal(double frequency, int signalSize)
	{
        double[] dataPoints = new double[signalSize];
        for (int dataPointsIndex = 0; dataPointsIndex < signalSize; dataPointsIndex++)
			dataPoints[dataPointsIndex] = Math.sin(((double)dataPointsIndex)*(Math.PI * frequency));
		return dataPoints;
	}

    public boolean checkSingleFrequency(double frequency)
	{
        int dataPointCount = 1024;
        double[] dataPoints = generateSignal(frequency, dataPointCount);

		CooleyTukeyFastFourierTransformer transformer = new CooleyTukeyFastFourierTransformer(0, dataPoints.length, 0);
        DiscreteFourierTransform transformed = transformer.transform(dataPoints);

		Entry<Double, ComplexNumber> maxEntry = null;
		for(Entry<Double, ComplexNumber> phasorEntry : transformed.getFrequencyPhasors().entrySet())
		{
			if(maxEntry == null)
				maxEntry = phasorEntry;
			else if(maxEntry.getValue().abs() < phasorEntry.getValue().abs())
				maxEntry = phasorEntry;
		}

		if(Math.abs(maxEntry.getKey() - frequency) > 0.01)
			return false;
		return true;
    }

	public boolean checkFrequencyRange(double frequency)
	{
        int dataPointCount = 1024;
        double[] dataPoints = generateSignal(frequency, dataPointCount);


        // FFT of original data
		CooleyTukeyFastFourierTransformer transformer = new CooleyTukeyFastFourierTransformer(0, dataPoints.length, 0);
        DiscreteFourierTransform transformed = transformer.transform(dataPoints);

		double expectedBandPower = 0.0;
		double maxBandPower = 0.0;
		final double bandSize = transformed.getMaximumFrequency() / 10.0;
		for(double startBandFreq = 0.0; startBandFreq < (transformed.getMaximumFrequency()-0.01); startBandFreq+=bandSize)
		{
			final double endBandFreq = (startBandFreq+bandSize);
			final double currentBandPower = transformed.getBandPower(startBandFreq, (startBandFreq+bandSize));

			if(maxBandPower < currentBandPower)
				maxBandPower = currentBandPower;
			if( (startBandFreq <= frequency)&&(frequency < endBandFreq))
				expectedBandPower = currentBandPower;
		}

		if(expectedBandPower == maxBandPower)
			return true;
		return false;
	}

	@Test
	public void testRanges()
	{
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(0.5));
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(0.25));
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(0.125));
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(0.0625));
	}

	@Test
	public void testRandomRanges()
	{
		final Random random = new Random();
		for(int testIndex = 0; testIndex < 1000; testIndex++)
		{
			//(0.0-0.09, 0.1-0.19, 0.2-0.29... 0.9-0.99
			final double frequency = (random.nextDouble() * 0.09) + (((double)random.nextInt(10)/10.0) );
			Assert.assertTrue("unexpected random dominant frequency range: " + frequency + "!", checkFrequencyRange(frequency));
		}
	}

	@Test
	public void testFrequencies()
	{
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(0.5));
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(0.25));
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(0.125));
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(0.0625));
	}

	@Test
	public void testRandomFrequencies()
	{
		final Random random = new Random();
		for(int testIndex = 0; testIndex < 1000; testIndex++)
		{
			//(0.0-0.09, 0.1-0.19, 0.2-0.29... 0.9-0.99
			final double frequency = (random.nextDouble() * 0.09) + (((double)random.nextInt(10)/10.0) );
			Assert.assertTrue("unexpected random dominant frequency: " + frequency + "!", checkSingleFrequency(frequency));
		}
	}
}
