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

/**
 * An abstract wrapper class for a GeneticAlgorithmChromosome that can evaluate
 * the relative fitness of one chromosome over another. The relative fitness is
 * represented by its natural ordering through implementation of the Comparable
 * interface.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 */
public abstract class GeneticAlgorithmFitnessFunction implements Comparable<GeneticAlgorithmFitnessFunction>
{
	private GeneticAlgorithmChromosome chromosome;

	/**
	 * Initializes a new instance of this class acting as a wrapper for the
	 * specified chromosome.
	 *
	 * @param chromosome Chromosome to wrap and evaluate the fitness of.
	 * @since 2.0
	 */
	public GeneticAlgorithmFitnessFunction(GeneticAlgorithmChromosome chromosome)
	{
		this.chromosome = chromosome;
	}

	/**
	 * Gets the chromosome this class wraps.
	 *
	 * @return The chromosome this class is evaluating and wrapping.
	 * @since 2.0
	 */
	public GeneticAlgorithmChromosome getChromosome()
	{
		return this.chromosome;
	}

	/**
	 * Evaluates the fitness of the chromosome being wrapped relative to the
	 * specified chromosome.
	 *
	 * @param compareWith The fitness function containing a chromosome to
	 * compare to.
	 * @return If this chromosome is more fit it will return a
	 * positive value, if it is less fit it will be negative. If they are
	 * both equally as fit it will return 0.
	 * @since 2.0
	 */
	public abstract int compareTo(GeneticAlgorithmFitnessFunction compareWith);

	@Override
	public boolean equals(Object compareWith)
	{
		if(compareWith instanceof GeneticAlgorithmFitnessFunction)
			return ((GeneticAlgorithmFitnessFunction)compareWith).chromosome.equals(this.chromosome);
		else
			return false;
	}

	@Override
	public int hashCode()
	{
		return this.chromosome.hashCode();
	}
}
