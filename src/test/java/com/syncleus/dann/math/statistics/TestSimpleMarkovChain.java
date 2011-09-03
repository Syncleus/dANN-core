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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class TestSimpleMarkovChain
{
	private static enum WeatherState
	{
		RAINY, SUNNY
	}

	private static final Logger LOGGER = Logger.getLogger(TestSimpleMarkovChain.class);
	private final static Random RANDOM = new Random(1);

	@Test
	public void testExplicitChainFirstOrder()
	{
		final Map<WeatherState, Map<WeatherState, Double>> transitionProbabilities = new HashMap<WeatherState, Map<WeatherState, Double>>();
		final Map<WeatherState, Double> sunnyTransitions = new HashMap<WeatherState, Double>();
		final Map<WeatherState, Double> rainyTransitions = new HashMap<WeatherState, Double>();

/*
		final Map<WeatherState, Double> initialTransitions = new HashMap<WeatherState, Double>();
		initialTransitions.put(WeatherState.SUNNY, 0.83333333333);
		initialTransitions.put(WeatherState.RAINY, 0.16666666666);
		transitionProbabilities.put(null, initialTransitions);
*/

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

		LOGGER.info("transition columns: " + simpleChain.getTransitionProbabilityColumns());
		LOGGER.info("transition rows: " + simpleChain.getTransitionProbabilityRows());
		LOGGER.info("transition matrix: " + simpleChain.getTransitionProbabilityMatrix());
        LOGGER.info("steady state: " + simpleChain.getSteadyStateProbability(WeatherState.SUNNY) + " , " + simpleChain.getSteadyStateProbability(WeatherState.RAINY));

		Assert.assertEquals("Sunny steady state incorrect", 0.83333333333, Math.abs(simpleChain.getSteadyStateProbability(WeatherState.SUNNY)), 0.001);
		Assert.assertEquals("Rainy steady state incorrect", 0.16666666666, Math.abs(simpleChain.getSteadyStateProbability(WeatherState.RAINY)), 0.001);
		Assert.assertEquals("Sunny 1 step incorrect", 0.9, Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 1)), 0.001);
		Assert.assertEquals("Rainy 1 step incorrect", 0.1, Math.abs(simpleChain.getProbability(WeatherState.RAINY, 1)), 0.001);
		Assert.assertEquals("Sunny 2 step incorrect", 0.86, Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 2)), 0.001);
		Assert.assertEquals("Rainy 2 step incorrect", 0.14, Math.abs(simpleChain.getProbability(WeatherState.RAINY, 2)), 0.001);
	}

	@Test
	public void testExplicitChainSecondOrder()
	{
System.out.println("===ESO begining===");
		final Map<List<WeatherState>, Map<WeatherState, Double>> transitionProbabilities = new HashMap<List<WeatherState>, Map<WeatherState, Double>>();

/*
		final List<WeatherState> initialState = new ArrayList<WeatherState>();
		final Map<WeatherState, Double> initialTransitions = new HashMap<WeatherState, Double>();
		initialTransitions.put(WeatherState.SUNNY, 0.83333333333);
		initialTransitions.put(WeatherState.RAINY, 0.16666666666);
		transitionProbabilities.put(initialState, initialTransitions);
*/


		final List<WeatherState> sunnyState = new ArrayList<WeatherState>();
		sunnyState.add(WeatherState.SUNNY);
		final Map<WeatherState, Double> sunnyTransitions = new TreeMap<WeatherState, Double>();
		sunnyTransitions.put(WeatherState.SUNNY, 0.9);
		sunnyTransitions.put(WeatherState.RAINY, 0.1);
		transitionProbabilities.put(sunnyState, sunnyTransitions);

		final List<WeatherState> rainyState = new ArrayList<WeatherState>();
		rainyState.add(WeatherState.RAINY);
		final Map<WeatherState, Double> rainyTransitions = new TreeMap<WeatherState, Double>();
		rainyTransitions.put(WeatherState.SUNNY, 0.5);
		rainyTransitions.put(WeatherState.RAINY, 0.5);
		transitionProbabilities.put(rainyState, rainyTransitions);

		final List<WeatherState> sunnySunnyState = new ArrayList<WeatherState>();
		sunnySunnyState.add(WeatherState.SUNNY);
		sunnySunnyState.add(WeatherState.SUNNY);
		final Map<WeatherState, Double> sunnySunnyTransitions = new TreeMap<WeatherState, Double>();
		sunnySunnyTransitions.put(WeatherState.SUNNY, 0.9);
		sunnySunnyTransitions.put(WeatherState.RAINY, 0.1);
		transitionProbabilities.put(sunnySunnyState, sunnySunnyTransitions);

		final List<WeatherState> sunnyRainyState = new ArrayList<WeatherState>();
		sunnyRainyState.add(WeatherState.SUNNY);
		sunnyRainyState.add(WeatherState.RAINY);
		final Map<WeatherState, Double> sunnyRainyTransitions = new TreeMap<WeatherState, Double>();
		sunnyRainyTransitions.put(WeatherState.SUNNY, 0.5);
		sunnyRainyTransitions.put(WeatherState.RAINY, 0.5);
		transitionProbabilities.put(sunnyRainyState, sunnyRainyTransitions);

		final List<WeatherState> rainySunnyState = new ArrayList<WeatherState>();
		rainySunnyState.add(WeatherState.RAINY);
		rainySunnyState.add(WeatherState.SUNNY);
		final Map<WeatherState, Double> rainySunnyTransitions = new TreeMap<WeatherState, Double>();
		rainySunnyTransitions.put(WeatherState.SUNNY, 0.9);
		rainySunnyTransitions.put(WeatherState.RAINY, 0.1);
		transitionProbabilities.put(rainySunnyState, rainySunnyTransitions);

		final List<WeatherState> rainyRainyState = new ArrayList<WeatherState>();
		rainyRainyState.add(WeatherState.RAINY);
		rainyRainyState.add(WeatherState.RAINY);
		final Map<WeatherState, Double> rainyRainyTransitions = new TreeMap<WeatherState, Double>();
		rainyRainyTransitions.put(WeatherState.SUNNY, 0.5);
		rainyRainyTransitions.put(WeatherState.RAINY, 0.5);
		transitionProbabilities.put(rainyRainyState, rainyRainyTransitions);


		final Set<WeatherState> states = new HashSet<WeatherState>();
		states.add(WeatherState.SUNNY);
		states.add(WeatherState.RAINY);

		final MarkovChain<WeatherState> simpleChain = new SimpleMarkovChain<WeatherState>(transitionProbabilities, 2, states);
		simpleChain.transition(WeatherState.SUNNY);

		LOGGER.info("transition columns: " + simpleChain.getTransitionProbabilityColumns());
		LOGGER.info("transition rows: " + simpleChain.getTransitionProbabilityRows());
		LOGGER.info("transition matrix: " + simpleChain.getTransitionProbabilityMatrix());
		LOGGER.info("steady state: " + simpleChain.getSteadyStateProbability(WeatherState.SUNNY) + " , " + simpleChain.getSteadyStateProbability(WeatherState.RAINY));
System.out.println("ESO testing sunny steady:");
		Assert.assertEquals("Sunny steady state incorrect", 0.83333333333, Math.abs(simpleChain.getSteadyStateProbability(WeatherState.SUNNY)), 0.001);
System.out.println("ESO sunny test done");
		Assert.assertEquals("Rainy steady state incorrect", 0.16666666666, Math.abs(simpleChain.getSteadyStateProbability(WeatherState.RAINY)), 0.001);
		Assert.assertEquals("Sunny 1 step incorrect", 0.9, Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 1)), 0.001);
		Assert.assertEquals("Rainy 1 step incorrect", 0.1, Math.abs(simpleChain.getProbability(WeatherState.RAINY, 1)), 0.001);
		Assert.assertEquals("Sunny 2 step incorrect", 0.86, Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 2)), 0.001);
		Assert.assertEquals("Rainy 2 step incorrect", 0.14, Math.abs(simpleChain.getProbability(WeatherState.RAINY, 2)), 0.001);
System.out.println("===ESO ending===");
	}

	@Test
	public void testSampledChainFirstOrder()
	{
		final MarkovChainEvidence<WeatherState> chainEvidence = new SimpleMarkovChainEvidence<WeatherState>(true, 1);
		//determine initial state
		WeatherState lastState;
		if(RANDOM.nextBoolean())
			lastState = WeatherState.SUNNY;
		else
			lastState = WeatherState.RAINY;
		chainEvidence.learnStep(lastState);

		//learn 1000 times
		for(int chainStep = 0; chainStep < 1000; chainStep++)
		{
			chainEvidence.newChain();
			for(int step = 0; step < 1000; step++)
			{
				if(lastState == WeatherState.SUNNY)
				{
					if(RANDOM.nextDouble() > 0.9)
					{
						lastState = WeatherState.RAINY;
					}
				}
				else if(lastState == WeatherState.RAINY)
				{
					if(RANDOM.nextBoolean())
					{
						lastState = WeatherState.SUNNY;
					}
				}

				chainEvidence.learnStep(lastState);
			}
		}

		final MarkovChain<WeatherState> simpleChain = chainEvidence.getMarkovChain();

		LOGGER.info("transition matrix: " + simpleChain.getTransitionProbabilityMatrix());

		simpleChain.transition(WeatherState.SUNNY);

		LOGGER.info("transition columns: " + simpleChain.getTransitionProbabilityColumns());
		LOGGER.info("transition rows: " + simpleChain.getTransitionProbabilityRows());
		LOGGER.info("steady state: " + simpleChain.getSteadyStateProbability(WeatherState.SUNNY) + " , " + simpleChain.getSteadyStateProbability(WeatherState.RAINY));

		Assert.assertEquals("Sunny steady state incorrect", 0.83333333333, Math.abs(simpleChain.getSteadyStateProbability(WeatherState.SUNNY)), 0.025);
		Assert.assertEquals("Rainy steady state incorrect", 0.16666666666, Math.abs(simpleChain.getSteadyStateProbability(WeatherState.RAINY)), 0.025);
		Assert.assertEquals("Sunny 1 step incorrect", 0.9, Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 1)), 0.025);
		Assert.assertEquals("Rainy 1 step incorrect", 0.1, Math.abs(simpleChain.getProbability(WeatherState.RAINY, 1)), 0.025);
		Assert.assertEquals("Sunny 2 step incorrect", 0.86, Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 2)), 0.025);
		Assert.assertEquals("Rainy 2 step incorrect", 0.14, Math.abs(simpleChain.getProbability(WeatherState.RAINY, 2)), 0.025);
	}

	@Test
	public void testSampledChainSecondOrder()
	{
		final MarkovChainEvidence<WeatherState> chainEvidence = new SimpleMarkovChainEvidence<WeatherState>(true, 2);
		//determine initial state
		WeatherState lastState;
		if(RANDOM.nextBoolean())
			lastState = WeatherState.SUNNY;
		else
			lastState = WeatherState.RAINY;
		chainEvidence.learnStep(lastState);

		//learn 1000 times
		for(int chainStep = 0; chainStep < 1000; chainStep++)
		{
			chainEvidence.newChain();
			for(int step = 0; step < 1000; step++)
			{
				if(lastState == WeatherState.SUNNY)
				{
					if(RANDOM.nextDouble() > 0.9)
					{
						lastState = WeatherState.RAINY;
					}
				}
				else if(lastState == WeatherState.RAINY)
				{
					if(RANDOM.nextBoolean())
					{
						lastState = WeatherState.SUNNY;
					}
				}

				chainEvidence.learnStep(lastState);
			}
		}

		final MarkovChain<WeatherState> simpleChain = chainEvidence.getMarkovChain();

		LOGGER.info("transition matrix: " + simpleChain.getTransitionProbabilityMatrix());

		simpleChain.transition(WeatherState.SUNNY);

		LOGGER.info("transition columns: " + simpleChain.getTransitionProbabilityColumns());
		LOGGER.info("transition rows: " + simpleChain.getTransitionProbabilityRows());
		LOGGER.info("steady state: " + simpleChain.getSteadyStateProbability(WeatherState.SUNNY) + " , " + simpleChain.getSteadyStateProbability(WeatherState.RAINY));

		Assert.assertEquals("Sunny steady state incorrect", 0.83333333333, Math.abs(simpleChain.getSteadyStateProbability(WeatherState.SUNNY)), 0.025);
		Assert.assertEquals("Rainy steady state incorrect", 0.16666666666, Math.abs(simpleChain.getSteadyStateProbability(WeatherState.RAINY)), 0.025);
		Assert.assertEquals("Sunny 1 step incorrect", 0.9, Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 1)), 0.025);
		Assert.assertEquals("Rainy 1 step incorrect", 0.1, Math.abs(simpleChain.getProbability(WeatherState.RAINY, 1)), 0.025);
		Assert.assertEquals("Sunny 2 step incorrect", 0.86, Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 2)), 0.025);
		Assert.assertEquals("Rainy 2 step incorrect", 0.14, Math.abs(simpleChain.getProbability(WeatherState.RAINY, 2)), 0.025);
	}

	@Test
	public void testFailProbability()
	{
		final int RUNS = 10;
		int failures = 0;
		for(int run = 0; run < RUNS; run++)
		{
			try
			{
				testExplicitChainFirstOrder();
				testExplicitChainSecondOrder();
				testSampledChainFirstOrder();
				testSampledChainSecondOrder();
			}
			catch (java.lang.AssertionError err)
			{
				err.printStackTrace();
				failures++;
			}
		}
		Assert.assertTrue("testSampledChainSecondOrder - failed runs: " + failures + "/" + RUNS + " -> " + ((double)failures / RUNS), failures == 0);
	}
}
