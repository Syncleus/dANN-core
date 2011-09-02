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

import java.util.*;
import com.syncleus.dann.genetics.wavelets.SignalProcessingWavelet.GlobalSignalConcentration;
import com.syncleus.dann.math.Function;
import org.apache.log4j.Logger;
import org.junit.*;

public class TestSignalProcessingWavelet
{
	private static final Random RANDOM = new Random();
	private static final int POPULATION_SIZE = 100;
	private static final int EXTINCTION_SIZE = 90;
	private static final int GENERATIONS = 50000;
	private static final double XOR_MUTABILITY = 100.0;
	private static final int TEST_MUTATIONS_REPEATS = 1000;
	private static final int TEST_XOR_REPEATS = 50;
	private static final Logger LOGGER = Logger.getLogger(TestSignalProcessingWavelet.class);

	@Test
	public void testMutations() throws CloneNotSupportedException
	{
		for(int testCount = 0; testCount < TEST_MUTATIONS_REPEATS; testCount++)
		{
			LOGGER.debug("performing testMutations test #" + testCount);
			testMutationOnce();
		}
	}

	@Test
	public void testMutationOnce()
	{
		final GlobalSignalConcentration xAxis = new GlobalSignalConcentration();
		final GlobalSignalConcentration yAxis = new GlobalSignalConcentration();
		final GlobalSignalConcentration output = new GlobalSignalConcentration();
		SignalProcessingWavelet processor = new SignalProcessingWavelet(xAxis, output);
		processor = processor.mutate(1.0, xAxis);
		processor = processor.mutate(1.0, yAxis);
		processor = processor.mutate(1.0);

		processor.preTick();
		processor.tick();
	}

	@Test
	public void testXorEvolves()
	{
		for(int testIndex = 0; testIndex < TEST_XOR_REPEATS; testIndex++)
		{
			LOGGER.debug("performing testXorEvolves test #" + testIndex);
			this.testXorEvolveOnce();
		}
	}

	@Test
	public void testXorEvolveOnce()
	{
		LOGGER.debug("begining testXorEvolveOnce");
		final TreeMap<Double, SignalProcessingWavelet> population = new TreeMap<Double, SignalProcessingWavelet>();
		//initialize the population
		LOGGER.info("Initializing population");
		final GlobalSignalConcentration xAxis = new GlobalSignalConcentration();
		final GlobalSignalConcentration yAxis = new GlobalSignalConcentration();
		final GlobalSignalConcentration output = new GlobalSignalConcentration();
		while( (population.size() < POPULATION_SIZE) && ((population.isEmpty() ? 0.0 : population.lastKey()) < 4.0) )
		{
			SignalProcessingWavelet processor = new SignalProcessingWavelet(xAxis, output);
			processor = mutateXor(processor, xAxis, yAxis);
			final double initialFitness = checkXorFitness(processor.getWavelet(), processor.getWaveCount());
			population.put(initialFitness, processor);
			LOGGER.debug("Initialized " + population.size() + " members, last members fitness: " + initialFitness);
		}
		//run through several generations
		LOGGER.debug("population initalized, proceeding with generations");
		int generationIndex;
		for(generationIndex = 0; (generationIndex < GENERATIONS) && (population.lastKey() < 4.0); generationIndex++)
		{
			LOGGER.debug("Begining generation " + generationIndex + ", current fitness: " + population.lastKey());
			//fill off all but the top EXTINCTION_SIZE performing members
			while( population.size() > EXTINCTION_SIZE )
				population.pollFirstEntry();
			//repopulate to POPULATION_SIZE members
			final ArrayList<SignalProcessingWavelet> populationArray = new ArrayList<SignalProcessingWavelet>(population.values());
			while( population.size() < POPULATION_SIZE )
			{
				SignalProcessingWavelet processor;
				if( RANDOM.nextFloat() < (float) 0.5 )
					processor = populationArray.get(RANDOM.nextInt(populationArray.size()));
				else
					processor = new SignalProcessingWavelet(xAxis, output);
				processor = mutateXor(processor, xAxis, yAxis);
				final double initialFitness = checkXorFitness(processor.getWavelet(), processor.getWaveCount());
				population.put(initialFitness, processor);
			}
		}
		final double bestFitness = population.lastKey();
		LOGGER.debug("evolution completed in " + generationIndex + " generations.");
		if( bestFitness < 4.0 )
			LOGGER.warn("did not successfully match XOR truth table: fitness: " + bestFitness);
		Assert.assertTrue("did not successfully match XOR truth table: fitness: " + bestFitness, bestFitness >= 4.0);
	}

	private static SignalProcessingWavelet mutateXor(final SignalProcessingWavelet processor, final GlobalSignalConcentration xAxis, final GlobalSignalConcentration yAxis)
	{
		//mutate until there are atleast 2 input signals and 1 output signal, 3 total
		//there will always be exactly 1 output signal
		SignalProcessingWavelet mutatedProcessor = processor;
		do
		{
			mutatedProcessor = mutatedProcessor.mutate(XOR_MUTABILITY, xAxis);
			mutatedProcessor = mutatedProcessor.mutate(XOR_MUTABILITY, yAxis);
			mutatedProcessor = mutatedProcessor.mutate(XOR_MUTABILITY);
		} while( mutatedProcessor.getSignals().size() < 3 );

		return mutatedProcessor;
	}

	private static double checkXorFitness(final Function xorAttempt, final int waveCount)
	{
		if( waveCount <= 0 )
			throw new IllegalArgumentException("waveCount must be >0");

		//testing against xor at 4 points only (0,0 0,1 1,0 1,1)
		xorAttempt.setParameter(0, 0.0);
		xorAttempt.setParameter(1, 0.0);
		final double result00 = xorAttempt.calculate();

		xorAttempt.setParameter(0, 1.0);
		xorAttempt.setParameter(1, 0.0);
		final double result10 = xorAttempt.calculate();

		xorAttempt.setParameter(0, 0.0);
		xorAttempt.setParameter(1, 1.0);
		final double result01 = xorAttempt.calculate();

		xorAttempt.setParameter(0, 1.0);
		xorAttempt.setParameter(1, 1.0);
		final double result11 = xorAttempt.calculate();

		//calculates the whole number portion of the fitness, should be between -4 and +4
		final int fitnessWhole =
				(result00 < 0.0 ? 1 : 0) +
						(result10 > 0.0 ? 1 : 0) +
						(result01 > 0.0 ? 1 : 0) +
						(result11 < 0.0 ? 1 : 0);

		//calculates the decimal portion of the fitness , should be >= 0 and < 1
		final double fitnessFine = 1.0 - Math.tanh(waveCount);
		final double fitnessSuperFine = Math.abs((double) xorAttempt.hashCode()) / ((double) Integer.MAX_VALUE);

		return ((double) fitnessWhole) + (fitnessFine * 0.99999) + (fitnessSuperFine * 0.00001);
	}
}
