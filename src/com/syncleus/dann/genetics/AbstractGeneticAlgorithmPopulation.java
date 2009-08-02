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
package com.syncleus.dann.genetics;

import java.util.*;
import java.util.concurrent.*;
import org.apache.log4j.Logger;
import com.syncleus.dann.DannRuntimeException;
import com.syncleus.dann.InterruptedDannRuntimeException;

/**
 * Rerpesents a population governed by Genetic Algorithm parameters. This class
 * is abstract and should be extended to assign fitness functions to each
 * member of the population. This class handles each generation of the
 * population.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 */
public abstract class AbstractGeneticAlgorithmPopulation
{
	private final static Random RANDOM = new Random();
	private final TreeSet<AbstractGeneticAlgorithmFitnessFunction> population;
	private final double mutationDeviation;
	private final double crossoverPercentage;
	private final double dieOffPercentage;
	private int generations;
	private final ThreadPoolExecutor threadExecutor;
	private final static Logger LOGGER = Logger.getLogger(AbstractGeneticAlgorithmPopulation.class);

	private static class Process implements Runnable
	{
		private AbstractGeneticAlgorithmFitnessFunction fitnessFunction;
		private final static Logger LOGGER = Logger.getLogger(Process.class);

		public Process(AbstractGeneticAlgorithmFitnessFunction fitnessFunction)
		{
			this.fitnessFunction = fitnessFunction;
		}

		public void run()
		{
			try
			{
				this.fitnessFunction.process();
			}
			catch(Throwable caught)
			{
				LOGGER.error("A throwable was caught!", caught);
				throw new DannRuntimeException("Throwable caught: " + caught, caught);
			}
		}
	}

		/**
	 * Creates a new population with an initial population consisting of the
	 * specified chromosomes and with the given Genetic Algorithm parameters.
	 *
	 * @param initialChromosomes The initial chromosomes for the first
	 * generation of the population.
	 * @param mutationDeviation The deviation used when mutating each chromosome
	 * in the population.
	 * @param crossoverPercentage The percentage change crossover will take
	 * place.
	 * @param dieOffPercentage The percentage of the population to die off in
	 * each generation.
	 * @since 2.0
	 */
	public AbstractGeneticAlgorithmPopulation(Set<GeneticAlgorithmChromosome> initialChromosomes, double mutationDeviation, double crossoverPercentage, double dieOffPercentage)
	{
		this(initialChromosomes, mutationDeviation, crossoverPercentage, dieOffPercentage, new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors()*5, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()));
	}

	/**
	 * Creates a new population with an initial population consisting of the
	 * specified chromosomes and with the given Genetic Algorithm parameters.
	 *
	 * @param initialChromosomes The initial chromosomes for the first
	 * generation of the population.
	 * @param mutationDeviation The deviation used when mutating each chromosome
	 * in the population.
	 * @param crossoverPercentage The percentage change crossover will take
	 * place.
	 * @param dieOffPercentage The percentage of the population to die off in
	 * each generation.
	 * @since 2.0
	 */
	public AbstractGeneticAlgorithmPopulation(Set<GeneticAlgorithmChromosome> initialChromosomes, double mutationDeviation, double crossoverPercentage, double dieOffPercentage, ThreadPoolExecutor threadExecutor)
	{
		if(initialChromosomes.size() <4)
			throw new IllegalArgumentException("Must have a population of atleast 4");

		this.population  = new TreeSet<AbstractGeneticAlgorithmFitnessFunction>();
		this.mutationDeviation = mutationDeviation;
		this.crossoverPercentage = crossoverPercentage;
		this.dieOffPercentage = dieOffPercentage;
		this.threadExecutor = threadExecutor;

		this.addAll(initialChromosomes);
	}

	private void addAll(Collection<GeneticAlgorithmChromosome> chromosomes)
	{
		//create all the fitness functions and then process them in parallel
		ArrayList<AbstractGeneticAlgorithmFitnessFunction> initialPopulation = new ArrayList<AbstractGeneticAlgorithmFitnessFunction>();
		ArrayList<Future> futures = new ArrayList<Future>();
		for(GeneticAlgorithmChromosome chromosome : chromosomes)
		{
			AbstractGeneticAlgorithmFitnessFunction fitnessFunction = this.packageChromosome(chromosome);
			initialPopulation.add(fitnessFunction);
			futures.add(this.threadExecutor.submit(new Process(fitnessFunction)));
		}
		//wait for processing to finish
		try
		{
			for(Future future : futures)
				future.get();
		}
		catch(InterruptedException caught)
		{
			LOGGER.error("Unexpected interuption of Process(fitnessFunction)", caught);
			throw new InterruptedDannRuntimeException("Unexpected interuption. Get should block indefinately", caught);
		}
		catch(ExecutionException caught)
		{
			LOGGER.error("Unexpected execution exception thrown from within Process(fitnessFunction)", caught);
			throw new AssertionError("Unexpected execution exception. Get should block indefinately");
		}

		//add to thetree set and sort
		for(AbstractGeneticAlgorithmFitnessFunction initialMember : initialPopulation)
		{
			this.population.add(initialMember);
		}
	}

	/**
	 * Creates a new population with an initial population consisting of the
	 * specified chromosomes and using default Genetic Algorithm parameters.
	 *
	 * @param initialChromosomes The initial chromosomes for the first
	 * generation of the population.
	 * @since 2.0
	 */
	public AbstractGeneticAlgorithmPopulation(Set<GeneticAlgorithmChromosome> initialChromosomes)
	{
		this(initialChromosomes, 0.25d, 0.75d, 0.90d);
	}

	/**
	 * Gets all the chromosomes consisting of the current generation of the
	 * population.
	 *
	 * @return An unmodifiable set of GeneticAlgorithmChromosomes consiting of
	 * the current population.
	 * @since 2.0
	 */
	public final Set<GeneticAlgorithmChromosome> getChromosomes()
	{
		HashSet<GeneticAlgorithmChromosome> chromosomes = new HashSet<GeneticAlgorithmChromosome>();
		for(AbstractGeneticAlgorithmFitnessFunction member : population)
		{
			chromosomes.add(member.getChromosome());
		}

		return Collections.unmodifiableSet(chromosomes);
	}

	/**
	 * Gets the most successful member of the current population according to
	 * its fitness function.
	 *
	 * @return the most successful member of the current population according to
	 * its fitness function.
	 * @since 2.0
	 */
	public final GeneticAlgorithmChromosome getWinner()
	{
		return this.population.last().getChromosome();
	}

	/**
	 * Gets the number of generations this population has went through.
	 *
	 * @return The number of generations this population has went through.
	 * @since 2.0
	 */
	public final int getGenerations()
	{
		return this.generations;
	}

	private final GeneticAlgorithmChromosome getRandomMember()
	{
		int randomIndex = RANDOM.nextInt(this.population.size());
		int currentIndex = 0;
		for(AbstractGeneticAlgorithmFitnessFunction member : this.population)
		{
			if(currentIndex == randomIndex)
				return member.getChromosome();

			currentIndex++;
		}

		throw new AssertionError("randomIndex was out of bounds!");
	}

	/**
	 * Proceeds to the next generation of the population. This includes killing
	 * off the worst preforming of the population and randomly selecting parents
	 * to replace them. Parents produce children through mutation and crossover.
	 *
	 * @since 2.0
	 */
	public void nextGeneration()
	{
		this.generations++;

		//calculate population sizes
		int populationSize = this.population.size();
		int lostPopulation = (int)((double)populationSize * this.dieOffPercentage);
		//ensure the population to kill off is even.
		if( lostPopulation%2 != 0)
			lostPopulation--;
		int remainingPopulation = populationSize - lostPopulation;

		//remove least performing members of the population
		while(this.population.size() > remainingPopulation)
			this.population.pollFirst();

		//breed children through mutation and crossover
		ArrayList<GeneticAlgorithmChromosome> children = new ArrayList<GeneticAlgorithmChromosome>();
		while(this.population.size() + children.size() < populationSize)
		{
			//obtain parents and mutate into children
			GeneticAlgorithmChromosome child1 = this.getRandomMember();
			GeneticAlgorithmChromosome child2 = this.getRandomMember();

			child1 = child1.mutate(mutationDeviation);
			child2 = child2.mutate(mutationDeviation);

			//crossover performed on children
			if(RANDOM.nextDouble() < this.crossoverPercentage)
			{
				int crossoverPoint = RANDOM.nextInt(child1.getGenes().size() - 1) + 1;

				List<AbstractValueGene> child1Segment = child1.crossover(crossoverPoint);
				List<AbstractValueGene> child2Segment = child2.crossover(crossoverPoint);

				child1.crossover(child2Segment, crossoverPoint);
				child2.crossover(child1Segment, crossoverPoint);
			}

			//store the new children
			children.add(child1);
			children.add(child2);
		}

		//add children to the population
		this.addAll(children);
	}

	/**
	 * An abstract method that must be implemented to package a supplied
	 * chromosome into an appropriate fitness function wrapper.
	 *
	 * @param chromosome Chromosome to wrap into a fitness function class.
	 * @return A fitness function wrapping the chromosome.
	 * @since 2.0
	 */
	protected abstract AbstractGeneticAlgorithmFitnessFunction packageChromosome(GeneticAlgorithmChromosome chromosome);
}
