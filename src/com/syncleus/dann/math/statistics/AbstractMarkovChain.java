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
package com.syncleus.dann.math.statistics;

public abstract class AbstractMarkovChain<S> implements MarkovChain<S>
{
	public S generateTransition()
	{
		return this.generateTransition(true);
	}

	public S getCurrentState()
	{
		return this.getStateHistory().get(0);
	}

	public double getProbability(S futureState, int steps)
	{
		return this.getProbability(steps).get(futureState).doubleValue();
	}

	public double getSteadyStateProbability(S futureState)
	{
		return this.getSteadyStateProbability().get(futureState).doubleValue();
	}
}
