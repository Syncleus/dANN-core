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
package com.syncleus.dann.genetics.wavelets;

import com.syncleus.dann.genetics.MutableDouble;
import java.util.Random;

public class Mutation
{
	private final static Random RANDOM = new Random();

	public static double mutabilityMutation(double mutability)
	{
		double mutabilityMutation = new MutableDouble(0.0).mutate(mutability).doubleValue();
		if(mutabilityMutation > 0)
			return mutability + mutabilityMutation;
		else
		{
			double returnValue = mutability - (mutability * (1 - 1/(Math.abs(mutabilityMutation) + 1)));
			if(returnValue == 0.0)
				returnValue = Double.MIN_VALUE;

			return returnValue;
		}
	}

	public static Random getRandom()
	{
		return RANDOM;
	}

	public static boolean mutationEvent(double mutability)
	{
		if(Mutation.getRandom().nextDouble() < Math.tanh(Math.abs(mutability)))
			return true;
		return false;
	}
}
