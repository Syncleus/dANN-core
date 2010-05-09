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

/*
** Derived from the Public-Domain sources found at
** http://www.cs.princeton.edu/introcs/97data/ as of 9/13/2009.
*/

package com.syncleus.dann.dataprocessing.signal.transform;

import com.syncleus.dann.math.ComplexNumber;
import java.util.Arrays;

public class CooleyTukeyFastFourierTransformer implements FastFourierTransformer
{
	private int blockSize;
	private int bitrate;

	public CooleyTukeyFastFourierTransformer(final int blockSize, final int bitrate)
	{
		this.setBlockSize(blockSize);
		this.setBitrate(bitrate);
	}

	public DiscreteFourierTransform transform(final double[] signal)
	{
		final double[] signalPadded = pad(signal);
		final ComplexNumber[] frequencyDomain = transformMatrix(doubleArrayToComplexArray(signalPadded));
		return new DiscreteFourierTransform(frequencyDomain,getBitrate());
	}

	public double[] inverseTransform(final DiscreteFourierTransform transform)
	{
		final ComplexNumber[] complexSignal = inverseTransformMatrix(pad(transform.getTransform()));
		return complexArrayToDoubleArray(complexSignal);
	}

	public double[] circularConvolve(final double[] first, final double[] second)
	{
		final ComplexNumber[] firstComplex = pad(doubleArrayToComplexArray(first));
		final ComplexNumber[] secondComplex = pad(doubleArrayToComplexArray(second));

		final ComplexNumber[] resultComplex = circularConvolveMatrix(firstComplex, secondComplex);
		return complexArrayToDoubleArray(resultComplex);
	}

	public double[] linearConvolve(final double[] first, final double[] second)
	{
		final ComplexNumber[] firstComplex = pad(doubleArrayToComplexArray(first));
		final ComplexNumber[] secondComplex = pad(doubleArrayToComplexArray(second));

		final ComplexNumber[] resultComplex = linearConvolveMatrix(firstComplex, secondComplex);
		return complexArrayToDoubleArray(resultComplex);
	}

	public int getBlockSize()
	{
		return blockSize;
	}

	public void setBlockSize(final int blockSize)
	{
		final double exponentOf2 = Math.log(blockSize)/Math.log(2.0);
		if ( Math.abs(exponentOf2 - Math.floor(exponentOf2)) > .0000000001 )
			this.blockSize = (int) Math.pow(2.0, Math.ceil(exponentOf2));
		this.blockSize = blockSize;
	}

	public int getBitrate()
	{
		return bitrate;
	}

	public void setBitrate(final int bitrate)
	{
		this.bitrate = bitrate;
	}
	
	private static ComplexNumber[] doubleArrayToComplexArray(final double[] from)
	{
		final ComplexNumber[] complexNumbers = new ComplexNumber[from.length];
		for(int index = 0; index < from.length; index++)
			complexNumbers[index] = new ComplexNumber(from[index],0.0);
		return complexNumbers;
	}

	private static double[] complexArrayToDoubleArray(final ComplexNumber[] from)
	{
		final double[] doubleNumbers = new double[from.length];
		for(int index = 0; index < from.length; index++)
			doubleNumbers[index] = from[index].absScalar();
		return doubleNumbers;
	}

	private double[] pad(final double[] signal)
	{
		if(signal.length != this.blockSize)
			return Arrays.copyOf(signal, this.blockSize);
		return signal;
	}

	private ComplexNumber[] pad(final ComplexNumber[] signal)
	{
		if(signal.length < this.blockSize)
		{
			final ComplexNumber[] paddedSignal = Arrays.copyOf(signal, this.blockSize);
			for(int index = signal.length; index < this.blockSize; index++)
				paddedSignal[index] = ComplexNumber.ZERO;
			return paddedSignal;
		}
		else if(signal.length > this.blockSize)
			return Arrays.copyOf(signal, this.blockSize);
		
		return signal;
	}

	private static boolean isPowerOf2(final int value)
	{
		final double exponentOf2 = Math.log(value)/Math.log(2.0);
		if ( Math.abs(exponentOf2 - Math.floor(exponentOf2)) > .0000000001 )
			return false;
		return true;
	}
 

	public static ComplexNumber[] transformMatrix(final ComplexNumber[] dataPoints)
	{
		final int dataPointCount = dataPoints.length;
		if(!isPowerOf2(dataPointCount))
			throw new IllegalArgumentException("dataPoints size is not a power of 2");

        if (dataPointCount == 1)
			return new ComplexNumber[]{ dataPoints[0] };

		//process odd points
		final ComplexNumber[] oddDataPoints  = new ComplexNumber[dataPointCount/2];
		for (int oddIndex = 0; oddIndex < (dataPointCount/2); oddIndex++)
			oddDataPoints[oddIndex] = dataPoints[2*oddIndex + 1];
		final ComplexNumber[] oddTransform = transformMatrix(oddDataPoints);

		//process even points
		final ComplexNumber[] evenDataPoints = new ComplexNumber[dataPointCount/2];
		for (int evenIndex = 0; evenIndex < (dataPointCount/2); evenIndex++)
			evenDataPoints[evenIndex] = dataPoints[evenIndex*2];
		final ComplexNumber[] evenTransform = transformMatrix(evenDataPoints);

		final ComplexNumber[] completeTransform = new ComplexNumber[dataPointCount];
		for (int transformIndex = 0; transformIndex < (dataPointCount/2); transformIndex++)
		{
			final double kth = -2 * Math.PI * transformIndex / dataPointCount;
			final ComplexNumber wk = new ComplexNumber(Math.cos(kth), Math.sin(kth));
			completeTransform[transformIndex] = evenTransform[transformIndex].add(wk.multiply(oddTransform[transformIndex]));
			completeTransform[transformIndex+(dataPointCount/2)] = evenTransform[transformIndex].subtract(wk.multiply(oddTransform[transformIndex]));
		}
		return completeTransform;
	}


	public static ComplexNumber[] inverseTransformMatrix(final ComplexNumber[] transforms)
	{
		final int transformSize = transforms.length;

		ComplexNumber[] signal = new ComplexNumber[transformSize];

		for (int signalIndex = 0; signalIndex < transformSize; signalIndex++)
			signal[signalIndex] = transforms[signalIndex].conjugate();

		signal = transformMatrix(signal);

		for (int signalIndex = 0; signalIndex < transformSize; signalIndex++)
			signal[signalIndex] = signal[signalIndex].conjugate();

		for (int signalIndex = 0; signalIndex < transformSize; signalIndex++)
			signal[signalIndex] = signal[signalIndex].multiply(1.0/((double)transformSize));

		return signal;
	}

	public static ComplexNumber[] circularConvolveMatrix(final ComplexNumber[] first, final ComplexNumber[] second)
	{
		if (first.length != second.length)
			throw new IllegalArgumentException("first and second must have the same number of elements");

		final int dataPointsSize = first.length;

		final ComplexNumber[] firstTransformed = transformMatrix(first);
		final ComplexNumber[] secondTransformed = transformMatrix(second);

		final ComplexNumber[] result = new ComplexNumber[dataPointsSize];
		for (int dataPointIndex = 0; dataPointIndex < dataPointsSize; dataPointIndex++)
			result[dataPointIndex] = firstTransformed[dataPointIndex].multiply(secondTransformed[dataPointIndex]);

		return inverseTransformMatrix(result);
	}

	public static ComplexNumber[] linearConvolveMatrix(final ComplexNumber[] first, final ComplexNumber[] second)
	{
		final ComplexNumber[] firstLinear = Arrays.copyOf(first, first.length*2);
		for (int firstLinearIndex = first.length; firstLinearIndex < (first.length*2); firstLinearIndex++)
			firstLinear[firstLinearIndex] = ComplexNumber.ZERO;

		final ComplexNumber[] secondLinear = Arrays.copyOf(second, second.length*2);
		for (int secondLinearIndex = second.length; secondLinearIndex < (second.length*2); secondLinearIndex++)
			secondLinear[secondLinearIndex] = ComplexNumber.ZERO;

		return circularConvolveMatrix(firstLinear, secondLinear);
	}
}
