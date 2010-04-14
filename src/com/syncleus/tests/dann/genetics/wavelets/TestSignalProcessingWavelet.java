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
		while(population.size() < 100)
		{
			SignalProcessingWavelet processor = new SignalProcessingWavelet(xAxis, output);
			processor = mutateXor(processor, xAxis, yAxis);
			System.out.println("initialization processor wave count: " + processor.getWaveCount());
			System.out.println("initialization processor signal count: " + processor.getSignals().size());

			processor.preTick();
			processor.tick();
			final double initialFitness = checkXorFitness(processor.getWavelet());
			System.out.println("initialization fitness: " + initialFitness);
			
			population.put(initialFitness, processor);
			System.out.println("initialization population size: " + population.size());
		}

		//run through several generations
		for(int generationIndex = 0; generationIndex < 1000; generationIndex++)
		{
			//fill off all but the top 10 performing members
			while(population.size() > 10)
			{
				population.pollFirstEntry();
				System.out.println("extinction population size: " + population.size());
			}

			//repopulate to 100 members
			ArrayList<SignalProcessingWavelet> populationArray = new ArrayList<SignalProcessingWavelet>(population.values());
			while(population.size() < 100)
			{
				SignalProcessingWavelet processor = populationArray.get(RANDOM.nextInt(populationArray.size()));
				System.out.println("generation processor pre wave count: " + processor.getWaveCount());
				System.out.println("generation processor pre signal count: " + processor.getSignals().size());
				processor = mutateXor(processor, xAxis, yAxis);
				System.out.println("generation processor post wave count: " + processor.getWaveCount());
				System.out.println("generation processor post signal count: " + processor.getSignals().size());

				processor.preTick();
				processor.tick();
				final double initialFitness = checkXorFitness(processor.getWavelet());
				System.out.println("generation fitness: " + initialFitness);

				population.put(initialFitness, processor);
				System.out.println("generation population size: " + population.size());
			}
		}

		final double bestFitness = population.lastKey();
		System.out.println("best final fitness: " + bestFitness);
	}

	private static SignalProcessingWavelet mutateXor(SignalProcessingWavelet processor, GlobalSignalConcentration xAxis, GlobalSignalConcentration yAxis) throws CloneNotSupportedException
	{
		do
		{
			for(int mutationIndex = 0; mutationIndex < Math.abs(RANDOM.nextGaussian() * 5); mutationIndex++ )
				processor = processor.mutate(Math.abs(RANDOM.nextGaussian() * 2.0), xAxis);
			for(int mutationIndex = 0; mutationIndex < Math.abs(RANDOM.nextGaussian() * 5); mutationIndex++ )
				processor = processor.mutate(Math.abs(RANDOM.nextGaussian() * 2.0), yAxis);
			for(int mutationIndex = 0; mutationIndex < Math.abs(RANDOM.nextGaussian() * 5); mutationIndex++ )
				processor = processor.mutate(Math.abs(RANDOM.nextGaussian() * 2.0));
		} while( processor.getSignals().size() < 3 );

		return processor;
	}

	private static double checkXorFitness(Function xorAttempt) throws CloneNotSupportedException
	{
//		System.out.println("number of xorAttempt parameters: " + xorAttempt.getParameterCount());
		xorAttempt.setParameter(0, 0.0);
		xorAttempt.setParameter(1, 0.0);
		double result00 = xorAttempt.calculate();

		xorAttempt.setParameter(0, 1.0);
		xorAttempt.setParameter(1, 0.0);
		double result10 = xorAttempt.calculate();

		xorAttempt.setParameter(0, 0.0);
		xorAttempt.setParameter(1, 1.0);
		double result01 = xorAttempt.calculate();

		xorAttempt.setParameter(0, 1.0);
		xorAttempt.setParameter(1, 1.0);
		double result11 = xorAttempt.calculate();

		int fitnessWhole =
			(result00 < 0.0 ? 1 : 0) +
			(result10 > 0.0 ? 1 : 0) +
			(result01 > 0.0 ? 1 : 0) +
			(result11 < 0.0 ? 1 : 0);

		double fitnessFine =
			Math.tanh(-1.0 * result00) +
			Math.tanh(result10) +
			Math.tanh(result01) +
			Math.tanh(-1.0 * result11);
		fitnessFine /= 4.0;

		return ((double)fitnessWhole) + fitnessFine;
	}
}
