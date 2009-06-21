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

public class GeneticAlgorithmChromosome implements Chromatid<ValueGene>
{
	private Vector<ValueGene> alleles;
	protected static Random random = new Random();

	public GeneticAlgorithmChromosome()
	{
		this.alleles = new Vector<ValueGene>();
	}

	public GeneticAlgorithmChromosome(GeneticAlgorithmChromosome copy)
	{
		this.alleles = new Vector<ValueGene>(copy.alleles);
	}

	//Generates new genes, all DoubleValueGenes
	public GeneticAlgorithmChromosome(int geneCount)
	{
		this();

		while(this.alleles.size() < geneCount)
		{
			this.alleles.add(new DoubleValueGene());
		}
	}

	public GeneticAlgorithmChromosome(int geneCount, double maxDeviation)
	{
		this();

		while(this.alleles.size() < geneCount)
		{
			this.alleles.add(new DoubleValueGene(((random.nextDouble()*2d)-1d) * maxDeviation));
		}
	}

	public List<ValueGene> getGenes()
	{
		return Collections.unmodifiableList(this.alleles);
	}

	public void crossover(List<ValueGene> geneticSegment, int point)
	{
		if(point <= 0)
			throw new IllegalArgumentException("point must be positive");
		if(point > this.alleles.size())
			throw new IllegalArgumentException("point can not be larger than the number of alleles");

		//remove allel replaced by crossover
		this.alleles = new Vector<ValueGene>(this.alleles.subList(0, point));

		//add the genetic segment to the end
		this.alleles.addAll(geneticSegment);
	}

	public List<ValueGene> crossover(int point)
	{
		if(point <= 0)
			throw new IllegalArgumentException("point must be positive");
		if(point > this.alleles.size())
			throw new IllegalArgumentException("point can not be larger than the number of alleles");

		return Collections.unmodifiableList(this.alleles.subList(point, this.alleles.size()));
	}

	public GeneticAlgorithmChromosome mutate(double deviation)
	{
		GeneticAlgorithmChromosome mutated = new GeneticAlgorithmChromosome();
		for(ValueGene allele : this.alleles)
		{
			mutated.alleles.add(allele.mutate(deviation));
		}

		return mutated;
	}
}
