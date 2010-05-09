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
package com.syncleus.tests.dann.math.statistics;

import com.syncleus.dann.math.statistics.MarkovChain;
import com.syncleus.dann.math.statistics.SimpleMarkovChain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class TestSimpleMarkovChain
{
	private static enum WeatherState{ SUNNY, RAINY}

    @Test
	public void testSimpleChain()
	{
		final Map<WeatherState, Map<WeatherState, Double>> transitionProbabilities = new HashMap<WeatherState, Map<WeatherState, Double>>();
		final Map<WeatherState, Double> sunnyTransitions = new HashMap<WeatherState, Double>();
		final Map<WeatherState, Double> rainyTransitions = new HashMap<WeatherState, Double>();

		sunnyTransitions.put(WeatherState.SUNNY, 0.9);
		sunnyTransitions.put(WeatherState.RAINY, 0.1);
		transitionProbabilities.put(WeatherState.SUNNY, sunnyTransitions);

		rainyTransitions.put(WeatherState.SUNNY, 0.5);
		rainyTransitions.put(WeatherState.RAINY, 0.5);
		transitionProbabilities.put(WeatherState.RAINY, rainyTransitions);

		final Set<WeatherState> states = new HashSet<WeatherState>();
		states.add(WeatherState.SUNNY);
		states.add(WeatherState.RAINY);

		final MarkovChain<WeatherState> simpleChain = new SimpleMarkovChain<WeatherState>(transitionProbabilities, states);
		simpleChain.transition(WeatherState.SUNNY);

		Assert.assertTrue("Sunny steady state incorrect", Math.abs(simpleChain.getSteadyStateProbability(WeatherState.SUNNY) - 0.83333333333) < 0.0001);
		Assert.assertTrue("Rainy steady state incorrect", Math.abs(simpleChain.getSteadyStateProbability(WeatherState.RAINY) - 0.16666666666) < 0.0001);
		Assert.assertTrue("Sunny 1 step incorrect", Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 1) - 0.9) < 0.0001);
		Assert.assertTrue("Rainy 1 step incorrect", Math.abs(simpleChain.getProbability(WeatherState.RAINY, 1) - 0.1) < 0.0001);
		Assert.assertTrue("Sunny 2 step incorrect", Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 2) - 0.86) < 0.0001);
		Assert.assertTrue("Rainy 2 step incorrect", Math.abs(simpleChain.getProbability(WeatherState.RAINY, 2) - 0.14) < 0.0001);
	}
}
