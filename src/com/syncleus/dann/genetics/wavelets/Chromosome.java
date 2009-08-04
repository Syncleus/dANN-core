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
import com.syncleus.dann.genetics.MutableDouble;
import org.apache.log4j.Logger;

public class Chromosome implements Cloneable
{
	private WaveletChromatid leftChromatid;
	private WaveletChromatid rightChromatid;
	private final static Random RANDOM = Mutation.getRandom();
	private final static Logger LOGGER = Logger.getLogger(Chromosome.class);

	private double mutability;

	public Chromosome()
	{
		this.leftChromatid = WaveletChromatid.newRandomWaveletChromatid();
		this.rightChromatid = WaveletChromatid.newRandomWaveletChromatid();
	}

	public Chromosome(Chromosome copy)
	{
		this.leftChromatid = new WaveletChromatid(copy.leftChromatid);
		this.rightChromatid = new WaveletChromatid(copy.rightChromatid);
		this.mutability = copy.mutability;
	}

	Set<SignalKey> getExpressedSignals(final boolean external)
	{
		final HashSet<SignalKey> allSignals = new HashSet<SignalKey>(this.leftChromatid.getExpressedSignals(external));
		allSignals.addAll(this.rightChromatid.getExpressedSignals(external));
		return Collections.unmodifiableSet(allSignals);
	}
	
	public Set<AbstractKey> getKeys()
	{
		final HashSet<AbstractKey> allKeys = new HashSet<AbstractKey>();
		allKeys.addAll(this.leftChromatid.getKeys());
		allKeys.addAll(this.rightChromatid.getKeys());
		return Collections.unmodifiableSet(allKeys);
	}

	public void preTick()
	{
		this.leftChromatid.preTick();
		this.rightChromatid.preTick();
	}

	public void tick()
	{
		this.leftChromatid.tick();
		this.rightChromatid.tick();
	}

	public boolean bind(final SignalKeyConcentration concentration, final boolean isExternal)
	{
		boolean bound = false;
		if( this.leftChromatid.bind(concentration, isExternal))
			bound = true;
		if( this.rightChromatid.bind(concentration, isExternal))
			bound = true;
		return bound;
	}

	protected WaveletChromatid getLeftChromatid()
	{
		return this.leftChromatid;
	}

	protected WaveletChromatid getRightChromatid()
	{
		return this.rightChromatid;
	}

	public List<AbstractWaveletGene> getGenes()
	{
		final List<AbstractWaveletGene> genes = new ArrayList<AbstractWaveletGene>(this.leftChromatid.getGenes());
		genes.addAll(this.rightChromatid.getGenes());
		return Collections.unmodifiableList(genes);
	}

	public List<PromoterGene> getPromoterGenes()
	{
		final List<PromoterGene> promoters = new ArrayList<PromoterGene>(this.leftChromatid.getPromoterGenes());
		promoters.addAll(this.rightChromatid.getPromoterGenes());
		return Collections.unmodifiableList(promoters);
	}

	public List<SignalGene> getLocalSignalGenes()
	{
		final List<SignalGene> localSignalGenes = new ArrayList<SignalGene>(this.leftChromatid.getLocalSignalGenes());
		localSignalGenes.addAll(this.rightChromatid.getLocalSignalGenes());
		return Collections.unmodifiableList(localSignalGenes);
	}

	public List<ExternalSignalGene> getExternalSignalGenes()
	{
		final List<ExternalSignalGene> externalSignalGenes = new ArrayList<ExternalSignalGene>(this.leftChromatid.getExternalSignalGenes());
		externalSignalGenes.addAll(this.rightChromatid.getExternalSignalGenes());
		return Collections.unmodifiableList(externalSignalGenes);
	}

	private void crossover(final double deviation)
	{
		//find the crossover position
		int crossoverPosition;
		if(RANDOM.nextBoolean())
		{
			final int length = ( this.leftChromatid.getCentromerePosition() < this.rightChromatid.getCentromerePosition() ? this.leftChromatid.getCentromerePosition() : this.rightChromatid.getCentromerePosition());

			final int fromEnd = (int) Math.abs( (new MutableDouble(0d)).mutate(deviation).doubleValue() );
			if(fromEnd > length)
				crossoverPosition = 0;
			else
				crossoverPosition = -1 * (length - fromEnd);
		}
		else
		{
			final int leftLength = this.leftChromatid.getGenes().size() - this.leftChromatid.getCentromerePosition();
			final int rightLength = this.rightChromatid.getGenes().size() - this.leftChromatid.getCentromerePosition();

			final int length = ( leftLength < rightLength ? leftLength : rightLength);

			final int fromEnd = (int) Math.abs( (new MutableDouble(0d)).mutate(deviation).doubleValue() );
			if(fromEnd > length)
				crossoverPosition = 0;
			else
				crossoverPosition = (length - fromEnd);
		}

		//perform the crossover.
		final List<AbstractWaveletGene> leftGenes = this.leftChromatid.crossover(crossoverPosition);
		final List<AbstractWaveletGene> rightGenes = this.rightChromatid.crossover(crossoverPosition);

		this.leftChromatid.crossover(rightGenes, crossoverPosition);
		this.rightChromatid.crossover(leftGenes, crossoverPosition);
	}

	@Override
	public Chromosome clone()
	{
		try
		{
			final Chromosome copy = (Chromosome) super.clone();
			copy.leftChromatid = new WaveletChromatid(this.leftChromatid);
			copy.rightChromatid = new WaveletChromatid(this.rightChromatid);
			copy.mutability = this.mutability;
			return copy;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

	public void mutate(final Set<AbstractKey> keyPool)
	{
		if( Mutation.mutationEvent(mutability) )
			this.crossover(this.mutability);

		this.leftChromatid.mutate(keyPool);
		this.rightChromatid.mutate(keyPool);

		if( Mutation.mutationEvent(this.mutability) )
			this.mutability = Mutation.mutabilityMutation(this.mutability);
	}
}
