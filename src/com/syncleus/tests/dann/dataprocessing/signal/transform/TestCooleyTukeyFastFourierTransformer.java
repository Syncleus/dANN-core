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
package com.syncleus.tests.dann.dataprocessing.signal.transform;

import com.syncleus.dann.math.ComplexNumber;
import com.syncleus.dann.dataprocessing.signal.transform.CooleyTukeyFastFourierTransformer;
import com.syncleus.dann.dataprocessing.signal.transform.DiscreteFourierTransform;
import com.syncleus.dann.dataprocessing.signal.transform.FastFourierTransformer;
import java.util.Map.Entry;
import java.util.Random;
import org.junit.*;

public class TestCooleyTukeyFastFourierTransformer
{
	private static final int BLOCK_SIZE = 1024;

	//generates a 1 second signal of the specified size and frequency
	private static double[] generateSignal(final double frequency, final int signalSize)
	{
        final double[] dataPoints = new double[signalSize];
        for (int dataPointsIndex = 0; dataPointsIndex < signalSize; dataPointsIndex++)
		{
			dataPoints[dataPointsIndex] = Math.cos(((double)dataPointsIndex)*((Math.PI*2.0) * (frequency/((double)signalSize))));
		}
		return dataPoints;
	}

    public static boolean checkSingleFrequency(final double frequency)
	{
        final double[] dataPoints = generateSignal(frequency, BLOCK_SIZE);

		final FastFourierTransformer transformer = new CooleyTukeyFastFourierTransformer(dataPoints.length, dataPoints.length);
        final DiscreteFourierTransform transformed = transformer.transform(dataPoints);

		Entry<Double, ComplexNumber> maxEntry = null;
		for(final Entry<Double, ComplexNumber> phasorEntry : transformed.getFrequencyPhasors().entrySet())
		{
			if(maxEntry == null)
				maxEntry = phasorEntry;
			else if(maxEntry.getValue().absScalar() < phasorEntry.getValue().absScalar())
				maxEntry = phasorEntry;
		}

		if(Math.abs(maxEntry.getKey() - frequency) > 2.0)
			return false;
		return true;
    }

	public static boolean checkFrequencyRange(final double frequency)
	{
        final double[] dataPoints = generateSignal(frequency, BLOCK_SIZE);


        // FFT of original data
		final FastFourierTransformer transformer = new CooleyTukeyFastFourierTransformer(dataPoints.length, dataPoints.length);
        final DiscreteFourierTransform transformed = transformer.transform(dataPoints);

		double expectedBandPower = 0.0;
		double maxBandPower = 0.0;
		final double bandSize = transformed.getMaximumFrequency() / 10.0;
		for(double startBandFreq = 0.0; startBandFreq < (transformed.getMaximumFrequency()-0.01); startBandFreq+=bandSize)
		{
			final double endBandFreq = (startBandFreq+bandSize);
			final double currentBandPower = transformed.getBandRms(startBandFreq, (startBandFreq+bandSize));

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
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(0.0));
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(0.25));
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(0.5));
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(121.0));
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(128.0));
		Assert.assertTrue("unexpected dominant frequency range!", checkFrequencyRange(511.95));
	}

	@Test
	public void testRandomRanges()
	{
		final Random random = new Random();
		for(int testIndex = 0; testIndex < 1000; testIndex++)
		{
			//(0.025-0.075, 0.125-0.175, 0.225-0.275... 0.925-0.975
			double frequency = ((random.nextDouble() * 0.05)+0.025) + (((double)random.nextInt(10)/10.0) );
			//scale from 0-1 to 0-512
			frequency *= 512.0;
			Assert.assertTrue("unexpected random dominant frequency range: " + frequency + '!', checkFrequencyRange(frequency));
		}
	}

	@Test
	public void testFrequencies()
	{
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(0.0));
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(0.25));
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(0.50));
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(121.0));
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(128.0));
		Assert.assertTrue("unexpected dominant frequency!", checkSingleFrequency(511.95));
	}

	@Test
	public void testRandomFrequencies()
	{
		final Random random = new Random();
		for(int testIndex = 0; testIndex < 1000; testIndex++)
		{
			//(0.0-0.09, 0.1-0.19, 0.2-0.29... 0.9-0.99
			double frequency = (random.nextDouble() * 0.09) + (((double)random.nextInt(10)/10.0) );
			//scale to 0 - 512
			frequency *= 512.0;
			Assert.assertTrue("unexpected random dominant frequency: " + frequency + '!', checkSingleFrequency(frequency));
		}
	}
}
