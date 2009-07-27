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

public class Nucleus implements Cloneable
{
	private ArrayList<Chromosome> chromosomes;
	private double mutability;

	public Nucleus()
	{
		this.mutability = Mutation.getRandom().nextDouble() * 10.0;

		//make sure there is atleast one starting chromosome.
		this.chromosomes.add(new Chromosome());

		//there is a chance more chromosomes can be created
		while(Mutation.mutationEvent(mutability))
			this.chromosomes.add(new Chromosome());
	}

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

	public void preTick()
	{
		for(Chromosome chromosome : this.chromosomes)
			chromosome.preTick();
	}

	public void tick()
	{
		for(Chromosome chromosome : this.chromosomes)
			chromosome.tick();
	}

	public boolean bind(SignalKeyConcentration concentration, boolean isExternal)
	{
		boolean bound = true;
		for( Chromosome chromosome : this.chromosomes )
			if( chromosome.bind(concentration, isExternal) )
				bound = true;
		return bound;
	}

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
