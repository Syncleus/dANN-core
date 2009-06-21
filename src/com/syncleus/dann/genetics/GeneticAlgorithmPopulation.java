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

public abstract class GeneticAlgorithmPopulation
{
	protected static Random random = new Random();
	private TreeSet<GeneticAlgorithmFitnessFunction> population;
	private double mutationDeviation;
	private double crossoverPercentage;
	private double dieOffPercentage;
	private int generations;

	public GeneticAlgorithmPopulation(Set<GeneticAlgorithmChromosome> initialChromosomes)
	{
		if(initialChromosomes.size() <4)
			throw new IllegalArgumentException("Must have a population of atleast 4");

		this.population  = new TreeSet<GeneticAlgorithmFitnessFunction>();

		for(GeneticAlgorithmChromosome chromosome : initialChromosomes)
		{
			this.population.add(this.packageChromosome(chromosome));
		}

		this.mutationDeviation = 1d;
		this.crossoverPercentage = 0.1d;
		this.dieOffPercentage = 0.25d;
	}

	public Set<GeneticAlgorithmChromosome> getChromosomes()
	{
		HashSet<GeneticAlgorithmChromosome> chromosomes = new HashSet<GeneticAlgorithmChromosome>();
		for(GeneticAlgorithmFitnessFunction member : population)
		{
			chromosomes.add(member.getChromosome());
		}

		return Collections.unmodifiableSet(chromosomes);
	}
	
	public int getGenerations()
	{
		return this.generations;
	}

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
				int crossoverPoint = random.nextInt(child1.getGenes().size());

				List<ValueGene> child1Segment = child1.crossover(crossoverPoint);
				List<ValueGene> child2Segment = child2.crossover(crossoverPoint);

				child1.crossover(child2Segment, crossoverPoint);
				child2.crossover(child1Segment, crossoverPoint);
			}

			this.population.add(this.packageChromosome(child1));
			this.population.add(this.packageChromosome(child2));
		}
	}

	protected abstract GeneticAlgorithmFitnessFunction packageChromosome(GeneticAlgorithmChromosome chromosome);
}
