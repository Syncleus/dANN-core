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

/**
 * Rerpesents a population governed by Genetic Algorithm parameters. This class
 * is abstract and should be extended to assign fitness functions to each
 * member of the population. This class handles each generation of the
 * population.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 */
public abstract class GeneticAlgorithmPopulation
{
	private static Random random = new Random();
	private TreeSet<GeneticAlgorithmFitnessFunction> population;
	private double mutationDeviation;
	private double crossoverPercentage;
	private double dieOffPercentage;
	private int generations;

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
	public GeneticAlgorithmPopulation(Set<GeneticAlgorithmChromosome> initialChromosomes, double mutationDeviation, double crossoverPercentage, double dieOffPercentage)
	{
		if(initialChromosomes.size() <4)
			throw new IllegalArgumentException("Must have a population of atleast 4");

		this.population  = new TreeSet<GeneticAlgorithmFitnessFunction>();

		for(GeneticAlgorithmChromosome chromosome : initialChromosomes)
		{
			this.population.add(this.packageChromosome(chromosome));
		}

		this.mutationDeviation = mutationDeviation;
		this.crossoverPercentage = crossoverPercentage;
		this.dieOffPercentage = dieOffPercentage;
	}

	/**
	 * Creates a new population with an initial population consisting of the
	 * specified chromosomes and using default Genetic Algorithm parameters.
	 *
	 * @param initialChromosomes The initial chromosomes for the first
	 * generation of the population.
	 * @since 2.0
	 */
	public GeneticAlgorithmPopulation(Set<GeneticAlgorithmChromosome> initialChromosomes)
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
	public Set<GeneticAlgorithmChromosome> getChromosomes()
	{
		HashSet<GeneticAlgorithmChromosome> chromosomes = new HashSet<GeneticAlgorithmChromosome>();
		for(GeneticAlgorithmFitnessFunction member : population)
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
	public GeneticAlgorithmChromosome getWinner()
	{
		return this.population.last().getChromosome();
	}

	/**
	 * Gets the number of generations this population has went through.
	 *
	 * @return The number of generations this population has went through.
	 * @since 2.0
	 */
	public int getGenerations()
	{
		return this.generations;
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

		//randomly select parents to beed to replenish population
		double chanceOfSelection = ((double)lostPopulation)/((double)remainingPopulation);
		ArrayList<GeneticAlgorithmChromosome> parents = new ArrayList<GeneticAlgorithmChromosome>();
		while(parents.size() < lostPopulation)
		{
			for(GeneticAlgorithmFitnessFunction member : this.population)
			{
				if(random.nextDouble() < chanceOfSelection)
					parents.add(member.getChromosome());
			}
		}

		//breed children through mutation and crossover
		while(this.population.size() < populationSize)
		{
			GeneticAlgorithmChromosome child1 = parents.remove(0).mutate(this.mutationDeviation);
			GeneticAlgorithmChromosome child2 = parents.remove(0).mutate(this.mutationDeviation);

			if(random.nextDouble() < this.crossoverPercentage)
			{
				int crossoverPoint = random.nextInt(child1.getGenes().size() - 1) + 1;

				List<ValueGene> child1Segment = child1.crossover(crossoverPoint);
				List<ValueGene> child2Segment = child2.crossover(crossoverPoint);

				child1.crossover(child2Segment, crossoverPoint);
				child2.crossover(child1Segment, crossoverPoint);
			}

			this.population.add(this.packageChromosome(child1));
			this.population.add(this.packageChromosome(child2));
		}
	}

	/**
	 * An abstract method that must be implemented to package a supplied
	 * chromosome into an appropriate fitness function wrapper.
	 *
	 * @param chromosome Chromosome to wrap into a fitness function class.
	 * @return A fitness function wrapping the chromosome.
	 * @since 2.0
	 */
	protected abstract GeneticAlgorithmFitnessFunction packageChromosome(GeneticAlgorithmChromosome chromosome);
}
