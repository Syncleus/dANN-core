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
package com.syncleus.dann.math;

public final class GreatestCommonDenominators
{
	public static class ExtendedGCD
	{
		private final long x;
		private final long y;
		private final long greatestCommonDenominator;

		private ExtendedGCD(final long x, final long y, final long greatestCommonDenominator)
		{
			this.x = x;
			this.y = y;
			this.greatestCommonDenominator = greatestCommonDenominator;
		}

		public long getX()
		{
			return x;
		}

		public long getY()
		{
			return y;
		}

		public long getGreatestCommonDenominator()
		{
			return greatestCommonDenominator;
		}

		@Override
		public String toString()
		{
			return "a*" + x + " + b*" + y + " = " + greatestCommonDenominator;
		}
	}

	private GreatestCommonDenominators()
	{
		//this class is a utility class, it cant be instantiated
	}

	public static long euclideanGCD(final long firstNumerator, final long secondNumerator)
	{
		long firstEuclidean = firstNumerator;
		long secondEuclidean = secondNumerator;
		while (firstEuclidean != 0)
		{
			final long tempEuclidean = firstEuclidean;
			firstEuclidean = secondEuclidean % firstEuclidean;
			secondEuclidean = tempEuclidean;
		}
		return Math.abs(secondEuclidean);
	}

	public static ExtendedGCD extendedEuclideanGCD(final long firstNumerator, final long secondNumerator)
	{
		long x = 0;
		long y = 1;
		long lastx = 1;
		long lasty = 0;
		long a = firstNumerator;
		long b = secondNumerator;
		while (b != 0)
		{
			final long quotient = a / b;
			long temp = b;
			b = a % b;
			a = temp;
			temp = x;
			x = lastx - quotient * x;
			lastx = temp;
			temp = y;
			y = lasty - quotient * y;
			lasty = temp;
		}
		return new ExtendedGCD(lastx, lasty, Math.abs(a));
	}

	public static long binaryGCD(final long firstNumerator, final long secondNumerator)
	{
		long firstBinary = Math.abs(firstNumerator);
		long secondBinary = Math.abs(secondNumerator);
		long shift;
		if (firstBinary == 0 || secondBinary == 0)
			return firstBinary | secondBinary;
		for(shift = 0; ((firstBinary | secondBinary) & 1) == 0; ++shift)
		{
			firstBinary >>= 1;
			secondBinary >>= 1;
		}
		while ((firstBinary & 1) == 0)
			firstBinary >>= 1;
		do
		{
			while ((secondBinary & 1) == 0)
				secondBinary >>= 1;
			if (firstBinary < secondBinary)
				secondBinary -= firstBinary;
			else
			{
				final long diff = firstBinary - secondBinary;
				firstBinary = secondBinary;
				secondBinary = diff;
			}
			secondBinary >>= 1;
		} while (secondBinary != 0);
		return firstBinary << shift;
	}
}
