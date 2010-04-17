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
package com.syncleus.tests.dann.genetics.wavelets;

import com.syncleus.dann.genetics.wavelets.*;
import org.junit.*;
import com.syncleus.dann.genetics.wavelets.SignalProcessingWavelet.GlobalSignalConcentration;
import com.syncleus.dann.math.Function;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class TestSignalProcessingWavelet
{
	private static final Random RANDOM = new Random();
	private static final int POPULATION_SIZE = 20;
	private static final int EXTINCTION_SIZE = 3;
	private static final int GENERATIONS = 100;
	private static final int XOR_MUTATION_COUNT = 100;
	private static final double XOR_MUTABILITY = 10000.0;

	@Test
	public void testMutations() throws CloneNotSupportedException
	{
		for(int testCount = 0; testCount < 100; testCount++)
			testMutationOnce();
	}

	@Test
	public void testMutationOnce() throws CloneNotSupportedException
	{
        GlobalSignalConcentration xAxis = new GlobalSignalConcentration();
        GlobalSignalConcentration yAxis = new GlobalSignalConcentration();
        GlobalSignalConcentration output = new GlobalSignalConcentration();
        SignalProcessingWavelet processor = new SignalProcessingWavelet(xAxis, output);
        for(int index = 0;index < 500 ;index++)
        {
			processor = processor.mutate(1.0, xAxis);
			processor = processor.mutate(1.0, yAxis);
			processor = processor.mutate(1.0);
        }

        processor.preTick();
        processor.tick();
	}

	@Test
	public void testXorEvolve() throws CloneNotSupportedException
	{
		final TreeMap<Double, SignalProcessingWavelet> population = new TreeMap<Double, SignalProcessingWavelet>();

		//initialize the population
		final GlobalSignalConcentration xAxis = new GlobalSignalConcentration();
		final GlobalSignalConcentration yAxis = new GlobalSignalConcentration();
		final GlobalSignalConcentration output = new GlobalSignalConcentration();
		while(population.size() < POPULATION_SIZE)
		{
			SignalProcessingWavelet processor = new SignalProcessingWavelet(xAxis, output);
			processor = mutateXor(processor, xAxis, yAxis);

			processor.preTick();
			processor.tick();
			final double initialFitness = checkXorFitness(processor.getWavelet(), processor.getWaveCount());
			
			population.put(initialFitness, processor);
		}

		//run through several generations
		for(int generationIndex = 0; generationIndex < GENERATIONS; generationIndex++)
		{
			//check if we reached the goal prematurely.
			if(population.lastKey() >= 4.0)
				break;
			
			//fill off all but the top EXTINCTION_SIZE performing members
			while(population.size() > EXTINCTION_SIZE)
				population.pollFirstEntry();

			//repopulate to POPULATION_SIZE members
			ArrayList<SignalProcessingWavelet> populationArray = new ArrayList<SignalProcessingWavelet>(population.values());
			while(population.size() < POPULATION_SIZE)
			{
				SignalProcessingWavelet processor = populationArray.get(RANDOM.nextInt(populationArray.size()));
				processor = mutateXor(processor, xAxis, yAxis);

				processor.preTick();
				processor.tick();
				final double initialFitness = checkXorFitness(processor.getWavelet(), processor.getWaveCount());

				population.put(initialFitness, processor);
			}
		}

		final double bestFitness = population.lastKey();
		Assert.assertTrue("did not successfully match XOR truth table: fitness: " + bestFitness, bestFitness >= 4.0);
	}

	private static SignalProcessingWavelet mutateXor(SignalProcessingWavelet processor, GlobalSignalConcentration xAxis, GlobalSignalConcentration yAxis) throws CloneNotSupportedException
	{
		//mutate until there are atleast 2 input signals and 1 output signal, 3 total
		//there will always be exactly 1 output signal
		do
		{
			for(int mutationIndex = 0; mutationIndex < XOR_MUTATION_COUNT; mutationIndex++ )
			{
				processor = processor.mutate(XOR_MUTABILITY, xAxis);
//			for(int mutationIndex = 0; mutationIndex < 100; mutationIndex++ )
				processor = processor.mutate(XOR_MUTABILITY, yAxis);
//			for(int mutationIndex = 0; mutationIndex < 100; mutationIndex++ )
				processor = processor.mutate(XOR_MUTABILITY);
			}
		} while( processor.getSignals().size() < 3 );

		return processor;
	}

	private static double checkXorFitness(Function xorAttempt, int waveCount) throws CloneNotSupportedException
	{
		if(waveCount <= 0)
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

		return ((double)fitnessWhole) + fitnessFine;
	}
}
