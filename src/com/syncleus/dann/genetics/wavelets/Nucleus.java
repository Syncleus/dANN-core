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

import com.syncleus.dann.UnexpectedDannError;
import java.util.*;
import org.apache.log4j.Logger;

public class Nucleus implements Cloneable
{
	private List<Chromosome> chromosomes;
	private double mutability;
	private final static Logger LOGGER = Logger.getLogger(Nucleus.class);

	public Nucleus()
	{
		this.chromosomes = new ArrayList<Chromosome>();

		this.mutability = Mutations.getRandom().nextDouble() * 10.0;

		//make sure there is atleast one starting chromosome.
		this.chromosomes.add(new Chromosome());

		//there is a chance more chromosomes can be created
		while(Mutations.mutationEvent(mutability))
			this.chromosomes.add(new Chromosome());
	}

	public Nucleus(final Nucleus copy)
	{
		this.chromosomes = new ArrayList<Chromosome>();
		for(final Chromosome chromosome : copy.chromosomes)
			this.chromosomes.add(new Chromosome(chromosome));
	}

	protected List<Chromosome> getChromosomes()
	{
		return Collections.unmodifiableList(this.chromosomes);
	}

	public void preTick()
	{
		for(final Chromosome chromosome : this.chromosomes)
			chromosome.preTick();
	}

	public void tick()
	{
		for(final Chromosome chromosome : this.chromosomes)
			chromosome.tick();
	}

	public boolean bind(final SignalKeyConcentration concentration, final boolean isExternal)
	{
		boolean bound = true;
		for( final Chromosome chromosome : this.chromosomes )
			if( chromosome.bind(concentration, isExternal) )
				bound = true;
		return bound;
	}

	@Override
	public Nucleus clone()
	{
		try
		{
			final Nucleus copy = (Nucleus) super.clone();
			copy.chromosomes = new ArrayList<Chromosome>();
			for(final Chromosome chromosome : this.chromosomes)
				copy.chromosomes.add(chromosome.clone());
			return copy;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

	public void mutate()
	{
		final HashSet<AbstractKey> allKeys = new HashSet<AbstractKey>();
		for(final Chromosome chromosome : this.chromosomes)
			allKeys.addAll(chromosome.getKeys());

		for(final Chromosome chromosome : this.chromosomes)
			chromosome.mutate(allKeys);
	}

	public void mutate(final Set<AbstractKey> keyPool)
	{
		final HashSet<AbstractKey> allKeys = new HashSet<AbstractKey>(keyPool);
		for(final Chromosome chromosome : this.chromosomes)
			allKeys.addAll(chromosome.getKeys());
		
		for(final Chromosome chromosome : this.chromosomes)
			chromosome.mutate(allKeys);
	}

	Set<SignalKey> getExpressedSignals(final boolean external)
	{
		final HashSet<SignalKey> allSignals = new HashSet<SignalKey>();
		for(final Chromosome chromosome:this.chromosomes)
			allSignals.addAll(chromosome.getExpressedSignals(external));
		return Collections.unmodifiableSet(allSignals);
	}
}
