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

import java.util.*;
import org.junit.*;
import org.apache.log4j.*;

public class TestSimpleMarkovChainHigherOrder
{
	private static enum WeatherState
	{
		SUNNY, RAINY
	}

	private final static Random RANDOM = new Random();
	private static final Logger LOGGER = Logger.getLogger(TestSimpleMarkovChainHigherOrder.class);

	protected void testChain(int order) {
		final MarkovChainEvidence<WeatherState> chainEvidence = new SimpleMarkovChainEvidence<WeatherState>(true, order);
		//determine initial state
		WeatherState lastState;
		if(RANDOM.nextBoolean())
			lastState = WeatherState.SUNNY;
		else
			lastState = WeatherState.RAINY;
		chainEvidence.learnStep(lastState);

		//learn 1000 times
		for(int chainStep = 0; chainStep < 100; chainStep++)
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

		//simpleChain.transition(WeatherState.SUNNY);

		int generatedTransitions = 30;
		for (int i = 0; i < generatedTransitions; i++) {
			LOGGER.info(simpleChain.generateTransition() + " ");
		}

		LOGGER.info("transition columns: " + simpleChain.getTransitionProbabilityColumns());
		LOGGER.info("transition rows: " + simpleChain.getTransitionProbabilityRows());
		LOGGER.info("steady state: " + simpleChain.getSteadyStateProbability(WeatherState.SUNNY) + " , " + simpleChain.getSteadyStateProbability(WeatherState.RAINY));

		Assert.assertTrue("Sunny steady state incorrect: " + simpleChain.getSteadyStateProbability(WeatherState.SUNNY), Math.abs(simpleChain.getSteadyStateProbability(WeatherState.SUNNY) - 0.83333333333) < 0.1);
		Assert.assertTrue("Rainy steady state incorrect: " + simpleChain.getSteadyStateProbability(WeatherState.RAINY), Math.abs(simpleChain.getSteadyStateProbability(WeatherState.RAINY) - 0.16666666666) < 0.1);
		Assert.assertTrue("Sunny 1 step incorrect: " + simpleChain.getProbability(WeatherState.SUNNY, 1), Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 1) - 0.9) < 0.1);
		Assert.assertTrue("Rainy 1 step incorrect: " + simpleChain.getProbability(WeatherState.RAINY, 1), Math.abs(simpleChain.getProbability(WeatherState.RAINY, 1) - 0.1) < 0.1);
		Assert.assertTrue("Sunny 2 step incorrect: " + simpleChain.getProbability(WeatherState.SUNNY, 2), Math.abs(simpleChain.getProbability(WeatherState.SUNNY, 2) - 0.86) < 0.1);
		Assert.assertTrue("Rainy 2 step incorrect: " + simpleChain.getProbability(WeatherState.RAINY, 2), Math.abs(simpleChain.getProbability(WeatherState.RAINY, 2) - 0.14) < 0.1);

	}

	@Test
	public void testSimpleChain()
	{
		testChain(1);
		testChain(2);
		testChain(3);
		testChain(4);
	}
}
