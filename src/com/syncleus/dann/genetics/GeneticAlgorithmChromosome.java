/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.genetics;

import java.util.*;
import org.apache.log4j.Logger;
import com.syncleus.dann.genetics.wavelets.Mutations;
import com.syncleus.dann.UnexpectedDannError;

/**
 * A Genetic Algorithm based Chromosome. While technically a Chromatid in
 * Genetic Algorithms a Chromotid is called a Chromosome. Since this is Genetic
 * Algorithm based it only contains ValueGenes.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 *
 */
public class GeneticAlgorithmChromosome implements Chromatid<AbstractValueGene>, Cloneable
{
	private Vector<AbstractValueGene> alleles;
	private final static Random RANDOM = Mutations.getRandom();
	private final static Logger LOGGER = Logger.getLogger(GeneticAlgorithmChromosome.class);

	private GeneticAlgorithmChromosome()
	{
		this.alleles = new Vector<AbstractValueGene>();
	}

	/**
	 * Initializes a new instance of this class that is a copy of the specefied
	 * object.
	 *
	 * @param copy The object to copy.
	 * @since 2.0
	 */
	public GeneticAlgorithmChromosome(GeneticAlgorithmChromosome copy)
	{
		this.alleles = new Vector<AbstractValueGene>(copy.alleles);
	}

	/**
	 * Initiates a new instance of this class with the specefied number of
	 * initial geneCount. All initial genes will be DoubleGeneValue with an
	 * initial value of 0.
	 *
	 * @param geneCount The number of genes to create in this chromosome.
	 * @since 2.0
	 */
	public GeneticAlgorithmChromosome(int geneCount)
	{
		this();

		while(this.alleles.size() < geneCount)
		{
			this.alleles.add(new DoubleValueGene());
		}
	}

	/**
	 * Initiates a new instance of this class with the specefied number of
	 * initial geneCount. All initial genes will be DoubleGeneValue with an
	 * initial value with a normal distribution around 0 multipled by
	 * maxDeviation.
	 * 
	 * @param geneCount The number of genes to create in this chromosome.
	 * @param maxDeviation The multiplier for the normal distribution of
	 * initial values for this object's genes.
	 * @since 2.0
	 */
	public GeneticAlgorithmChromosome(int geneCount, double maxDeviation)
	{
		this();

		while(this.alleles.size() < geneCount)
		{
			this.alleles.add(new DoubleValueGene(((RANDOM.nextDouble()*2d)-1d) * maxDeviation));
		}
	}

	/**
	 * Gets an unmodifiable List of all the genes. The index of the genes
	 * indicates its position in the chromatid.
	 *
	 * @return An unmodifiable List of all genes in the chromatid
	 * @since 2.0
	 */
	public final List<AbstractValueGene> getGenes()
	{
		return Collections.unmodifiableList(this.alleles);
	}

	/**
	 * The first step in crossover. This will return a segment of genes from
	 * the point specified to the end of the chromatid. This will not modify
	 * the Chromatid.
	 *
	 * @param point the point (index) in the chromatid to act as the crossover
	 * point
	 * @return A List of the genetic segment crossing over.
	 */
	public List<AbstractValueGene> crossover(int point)
	{
		if(point <= 0)
			throw new IllegalArgumentException("point must be positive");
		if(point > this.alleles.size())
			throw new IllegalArgumentException("point can not be larger than the number of alleles");

		return Collections.unmodifiableList(this.alleles.subList(point, this.alleles.size()));
	}

	/**
	 * The second step in crossover. This will replace its own genetic code
	 * with the specefied genetic segment at the specefied crossover point.
	 *
	 * @param geneticSegment Segmet of genetic code crossing over.
	 * @param point Crossover point (index) where genes are spliced
	 * @since 2.0
	 * @see com.syncleus.dann.genetics.Chromatid#crossover(int)
	 */
	public void crossover(List<AbstractValueGene> geneticSegment, int point)
	{
		if(point <= 0)
			throw new IllegalArgumentException("point must be positive");
		if(point > this.alleles.size())
			throw new IllegalArgumentException("point can not be larger than the number of alleles");

		//remove allel replaced by crossover
		this.alleles = new Vector<AbstractValueGene>(this.alleles.subList(0, point));

		//add the genetic segment to the end
		this.alleles.addAll(geneticSegment);
	}

	/**
	 * Creates a new instance that is a copy of this object.
	 *
	 * @return an exact copy of this object.
	 * @since 2.0
	 */
	@Override
	public GeneticAlgorithmChromosome clone()
	{
		try
		{
			GeneticAlgorithmChromosome copy = (GeneticAlgorithmChromosome) super.clone();
			copy.alleles = new Vector<AbstractValueGene>(this.alleles);
			return copy;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

	/**
	 * This will make a copy of the object and mutate it. The mutation has
	 * a normal distribution multiplied by the deviation. This will be applied
	 * to each gene in the chromosome.
	 *
	 * @param deviation A double indicating how extreme the mutation will be.
	 * The greater the deviation the more drastically the object will mutate.
	 * A deviation of 0 should cause no mutation.
	 * @return A copy of the current object with potential mutations.
	 * @since 2.0
	 */
	public GeneticAlgorithmChromosome mutate(double deviation)
	{
		GeneticAlgorithmChromosome mutated = this.clone();
		mutated.alleles.clear();
		for(AbstractValueGene allele : this.alleles)
		{
			mutated.alleles.add(allele.mutate(deviation));
		}
		return mutated;
	}

	/**
	 * Gets the number of genes in the chromosome.
	 *
	 * @return the number of genes in the chromosome.
	 * @snce 2.0
	 */
	public final int getGeneCount()
	{
		return this.alleles.size();
	}
}
