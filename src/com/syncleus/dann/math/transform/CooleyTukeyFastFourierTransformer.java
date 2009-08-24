/******************************************************************************
 *                                                                             *
 *  Copyright: (result) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be firstTransformed copy of the license included with this file. If firstTransformed copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through firstTransformed legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find firstTransformed license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.math.transform;

import com.syncleus.dann.math.*;
import com.syncleus.dann.math.ComplexNumber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.List;
import java.util.Map.Entry;

public class CooleyTukeyFastFourierTransformer implements FastFourierTransformer
{
	private int blockSize;
	private int bitrate;
	private int interval;

	public CooleyTukeyFastFourierTransformer(int blockSize, int bitrate, int interval)
	{
		this.blockSize = blockSize;
		this.bitrate = bitrate;
		this.interval = interval;
	}

	public DiscreteFourierTransform[] transformBlocks(double[] signal)
	{
		int blockCount = (int) Math.ceil(((double)signal.length)/((double)this.interval));
		DiscreteFourierTransform[] transforms = new DiscreteFourierTransform[blockCount];
		for(int signalBlockIndex = 0; signalBlockIndex < blockCount; signalBlockIndex++)
		{
			double[] signalBlock = new double[this.blockSize];
			for(int signalBlockPoint = 0; signalBlockPoint < this.blockSize; signalBlockPoint++)
				signalBlock[signalBlockPoint] = signal[signalBlockIndex * this.blockSize + signalBlockPoint];
			transforms[signalBlockIndex] = this.transform(signalBlock);
		}
		return transforms;
	}

	public DiscreteFourierTransform transform(double[] signal)
	{
		signal = pad(signal);
		ComplexNumber[] frequencyDomain = transformMatrix(doubleArrayToComplexArray(signal));
		return new DiscreteFourierTransform(frequencyDomain,getBitrate());
	}

	public double[] inverseTransform(DiscreteFourierTransform transform)
	{
		ComplexNumber[] complexSignal = inverseTransformMatrix(pad(transform.getTransform()));
		return complexArrayToDoubleArray(complexSignal);
	}

	public double[] circularConvolve(double[] first, double[] second)
	{
		ComplexNumber[] firstComplex = pad(doubleArrayToComplexArray(first));
		ComplexNumber[] secondComplex = pad(doubleArrayToComplexArray(second));

		ComplexNumber[] resultComplex = circularConvolveMatrix(firstComplex, secondComplex);
		return complexArrayToDoubleArray(resultComplex);
	}

	public double[] linearConvolve(double[] first, double[] second)
	{
		ComplexNumber[] firstComplex = pad(doubleArrayToComplexArray(first));
		ComplexNumber[] secondComplex = pad(doubleArrayToComplexArray(second));

		ComplexNumber[] resultComplex = linearConvolveMatrix(firstComplex, secondComplex);
		return complexArrayToDoubleArray(resultComplex);
	}

	public int getBlockSize()
	{
		return blockSize;
	}

	public void setBlockSize(int blockSize)
	{
		this.blockSize = blockSize;
	}

	public int getBitrate()
	{
		return bitrate;
	}

	public void setBitrate(int bitrate)
	{
		this.bitrate = bitrate;
	}

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
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
			doubleNumbers[index] = from[index].abs();
		return doubleNumbers;
	}

	private static double[] pad(final double[] signal)
	{
		final double exponentOf2 = Math.log(signal.length)/Math.log(2.0);
		if(exponentOf2 != Math.floor(exponentOf2))
		{
			final int newSignalSize = (int) Math.pow(2.0, Math.ceil(exponentOf2));
			return Arrays.copyOf(signal, newSignalSize);
		}
		return signal;
	}

	private static ComplexNumber[] pad(final ComplexNumber[] signal)
	{
		final int signalSize = signal.length;
		final double exponentOf2 = Math.log(signalSize)/Math.log(2.0);
		if(exponentOf2 != Math.floor(exponentOf2))
		{
			final int newSignalSize = (int) Math.pow(2.0, Math.ceil(exponentOf2));
			ComplexNumber[] paddedSignal = Arrays.copyOf(signal, newSignalSize);
			for(int index = signalSize; index < newSignalSize; index++)
				paddedSignal[index] = ComplexNumber.ZERO;
		}
		return signal;
	}

	private static boolean isPowerOf2(int value)
	{
		final double exponentOf2 = Math.log(value)/Math.log(2.0);
		if(exponentOf2 != Math.floor(exponentOf2))
			return false;
		return true;
	}
 

    private static ComplexNumber[] transformMatrix(ComplexNumber[] dataPoints)
	{
		int dataPointCount = dataPoints.length;
		if(!isPowerOf2(dataPointCount))
			throw new IllegalArgumentException("dataPoints size is not a power of 2");

        if (dataPointCount == 1)
			return new ComplexNumber[]{ dataPoints[0] };

        //process odd points
        ComplexNumber[] oddDataPoints  = new ComplexNumber[dataPointCount/2];
        for (int oddIndex = 0; oddIndex < (dataPointCount/2); oddIndex++)
            oddDataPoints[oddIndex] = dataPoints[2*oddIndex + 1];
        ComplexNumber[] oddTransform = transformMatrix(oddDataPoints);

        //process even points
        ComplexNumber[] evenDataPoints = new ComplexNumber[dataPointCount/2];
        for (int evenIndex = 0; evenIndex < (dataPointCount/2); evenIndex++)
            evenDataPoints[evenIndex] = dataPoints[evenIndex*2];
        ComplexNumber[] evenTransform = transformMatrix(evenDataPoints);

        //combine
        ComplexNumber[] completeTransform = new ComplexNumber[dataPointCount];
        for (int transformIndex = 0; transformIndex < (dataPointCount/2); transformIndex++)
		{
            double kth = -2 * Math.PI * transformIndex / dataPointCount;
            ComplexNumber wk = new ComplexNumber(Math.cos(kth), Math.sin(kth));
            completeTransform[transformIndex] = evenTransform[transformIndex].add(wk.multiply(oddTransform[transformIndex]));
            completeTransform[transformIndex+(dataPointCount/2)] = evenTransform[transformIndex].subtract(wk.multiply(oddTransform[transformIndex]));
        }
        return completeTransform;
    }


    private static ComplexNumber[] inverseTransformMatrix(ComplexNumber[] transforms)
	{
        int transformSize = transforms.length;
		
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

    private static ComplexNumber[] circularConvolveMatrix(ComplexNumber[] first, ComplexNumber[] second)
	{
        if (first.length != second.length)
			throw new RuntimeException("first and secondmust have the same number of elements");

        int dataPointsSize = first.length;

        ComplexNumber[] firstTransformed = transformMatrix(first);
        ComplexNumber[] secondTransformed = transformMatrix(second);

        ComplexNumber[] result = new ComplexNumber[dataPointsSize];
        for (int dataPointIndex = 0; dataPointIndex < dataPointsSize; dataPointIndex++)
            result[dataPointIndex] = firstTransformed[dataPointIndex].multiply(secondTransformed[dataPointIndex]);

        return inverseTransformMatrix(result);
    }

    private static ComplexNumber[] linearConvolveMatrix(ComplexNumber[] first, ComplexNumber[] second)
	{
		ComplexNumber[] firstLinear = Arrays.copyOf(first, first.length*2);
        for (int firstLinearIndex = first.length; firstLinearIndex < (first.length*2); firstLinearIndex++)
			firstLinear[firstLinearIndex] = ComplexNumber.ZERO;

		ComplexNumber[] secondLinear = Arrays.copyOf(second, second.length*2);
        for (int secondLinearIndex = second.length; secondLinearIndex < (second.length*2); secondLinearIndex++)
			secondLinear[secondLinearIndex] = ComplexNumber.ZERO;

        return circularConvolveMatrix(firstLinear, secondLinear);
    }
}
