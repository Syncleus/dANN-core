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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Nucleus implements Cloneable
{
	private ArrayList<Chromosome> chromosomes;

	public Nucleus(Nucleus copy)
	{
		this.chromosomes = new ArrayList<Chromosome>();
		for(Chromosome chromosome : copy.chromosomes)
			this.chromosomes.add(chromosome.clone());
	}

	protected List<Chromosome> getChromosomes()
	{
		return Collections.unmodifiableList(this.chromosomes);
	}

	public void tick(Set<SignalKeyConcentration> externalSignalConcentrations)
	{
	}

	public boolean bind(SignalKeyConcentration concentration, boolean isExternal)
	{
		return false;
	}

	/*
	public List<ExternalSignalGene> getExternalSignalGenes()
	{
		HashMap<SignalKey, Double> externalSignals = new HashMap<SignalKey, Double>();
		for(Chromosome chromosome : this.chromosomes)
		{
			for(ExternalSignalGene gene : chromosome.getExternalSignalGenes())
			{
				Double currentConcentration = externalSignals.get(gene);
				if(currentConcentration == null)
					currentConcentration = 0D;
				externalSignals.put(gene.getOutputSignal(), currentConcentration + gene.expressionActivity());
			}

		}
		return Collections.unmodifiableMap(externalSignals);
	}*/

	@Override
	public Nucleus clone()
	{
		return new Nucleus(this);
	}

	public void mutate()
	{
		for(Chromosome chromosome : this.chromosomes)
			chromosome.mutate(null);
	}
}
