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

import java.util.*;
import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.genetics.MutableDouble;
import org.apache.log4j.Logger;

/**
 * A Chromosome is a mutable collection of Chromatids for use in a geneti algorithm.
 */
public class Chromosome implements Cloneable
{
	private WaveletChromatid leftChromatid;
	private WaveletChromatid rightChromatid;
	private static final Random RANDOM = Mutations.getRandom();
	private static final Logger LOGGER = Logger.getLogger(Chromosome.class);
	private double mutability;

	/**
	 * Creates a Chromasome with random left and right chromatids.
	 */
	public Chromosome()
	{
		this.leftChromatid = WaveletChromatid.newRandomWaveletChromatid();
		this.rightChromatid = WaveletChromatid.newRandomWaveletChromatid();
	}

	/**
	 * Creates a chromosome as a copy of another.
	 * @param copy The chromosome to topy
	 */
	public Chromosome(final Chromosome copy)
	{
		this.leftChromatid = new WaveletChromatid(copy.leftChromatid);
		this.rightChromatid = new WaveletChromatid(copy.rightChromatid);
		this.mutability = copy.mutability;
	}

	/**
	 * Gets the expressed signals of this chromosome.
	 * @param external Whether the caller is external to the Chromosome
	 * @return The set of SignalKeys
	 */
	Set<SignalKey> getExpressedSignals(final boolean external)
	{
		final HashSet<SignalKey> allSignals = new HashSet<SignalKey>(this.leftChromatid.getExpressedSignals(external));
		allSignals.addAll(this.rightChromatid.getExpressedSignals(external));
		return Collections.unmodifiableSet(allSignals);
	}

	/**
	 * Gets an unmodifiable set keys of this Chromosome.
	 * @return The expressed keys
	 */
	public Set<AbstractKey> getKeys()
	{
		final HashSet<AbstractKey> allKeys = new HashSet<AbstractKey>();
		allKeys.addAll(this.leftChromatid.getKeys());
		allKeys.addAll(this.rightChromatid.getKeys());
		return Collections.unmodifiableSet(allKeys);
	}

	/**
	 * Prepares both chromatids to tick.
	 * @see com.syncleus.dann.genetics.wavelets.WaveletChromatid#preTick()
	 */
	public void preTick()
	{
		this.leftChromatid.preTick();
		this.rightChromatid.preTick();
	}

	/**
	 * Ticks both chromatids.
	 * @see com.syncleus.dann.genetics.wavelets.WaveletChromatid#tick()
	 */
	public void tick()
	{
		this.leftChromatid.tick();
		this.rightChromatid.tick();
	}

	/**
	 * Binds a SignalKeyConcentration to both Chromatids
	 * @param concentration The signal key concentration
	 * @param isExternal Whether this signal is external to the Chromasome
	 * @return Whether the bind was successful
	 * @see com.syncleus.dann.genetics.wavelets.WaveletChromatid#bind(SignalKeyConcentration, boolean)
	 */
	public boolean bind(final SignalKeyConcentration concentration, final boolean isExternal)
	{
		boolean bound = false;
		if( this.leftChromatid.bind(concentration, isExternal) )
			bound = true;
		if( this.rightChromatid.bind(concentration, isExternal) )
			bound = true;
		return bound;
	}

	/**
	 * Gets the left chromatid.
	 * @return The left chromatid
	 */
	protected WaveletChromatid getLeftChromatid()
	{
		return this.leftChromatid;
	}

	/**
	 * Gets the right chromatid
	 * @return The right chromatid
	 */
	protected WaveletChromatid getRightChromatid()
	{
		return this.rightChromatid;
	}

	/**
	 * Gets all genes in the chromosome.
	 * @return An unmodifiable list of genes
	 */
	public List<AbstractWaveletGene> getGenes()
	{
		final List<AbstractWaveletGene> genes = new ArrayList<AbstractWaveletGene>(this.leftChromatid.getGenes());
		genes.addAll(this.rightChromatid.getGenes());
		return Collections.unmodifiableList(genes);
	}

	/**
	 * Gets all promoter genes in the chromosome.
	 * @return An unmodifiable list of promoter genes.
	 * @see WaveletChromatid#getPromoterGenes()
	 */
	public List<PromoterGene> getPromoterGenes()
	{
		final List<PromoterGene> promoters = new ArrayList<PromoterGene>(this.leftChromatid.getPromoterGenes());
		promoters.addAll(this.rightChromatid.getPromoterGenes());
		return Collections.unmodifiableList(promoters);
	}

	/**
	 * Gets all local signal genes expressed on the chromosome.
	 * @return An unmodifiable list of local signal genes
	 * @see WaveletChromatid#getLocalSignalGenes()
	 */
	public List<SignalGene> getLocalSignalGenes()
	{
		final List<SignalGene> localSignalGenes = new ArrayList<SignalGene>(this.leftChromatid.getLocalSignalGenes());
		localSignalGenes.addAll(this.rightChromatid.getLocalSignalGenes());
		return Collections.unmodifiableList(localSignalGenes);
	}

	/**
	 * Gets all external signal genes expressed on the chromosome.
	 * @return An unmodifiable list of external signal genes
	 * @see WaveletChromatid#getExternalSignalGenes()
	 */
	public List<ExternalSignalGene> getExternalSignalGenes()
	{
		final List<ExternalSignalGene> externalSignalGenes = new ArrayList<ExternalSignalGene>(this.leftChromatid.getExternalSignalGenes());
		externalSignalGenes.addAll(this.rightChromatid.getExternalSignalGenes());
		return Collections.unmodifiableList(externalSignalGenes);
	}

	/**
	 * Crosses over the two chromatids.
	 * @param deviation The amount of deviation allowable
	 */
	private void crossover(final double deviation)
	{
		//find the crossover position
		final int crossoverPosition;
		if( RANDOM.nextBoolean() )
		{
			final int length = (this.leftChromatid.getCentromerePosition() < this.rightChromatid.getCentromerePosition() ? this.leftChromatid.getCentromerePosition() : this.rightChromatid.getCentromerePosition());

			final int fromEnd = (int) Math.abs((new MutableDouble(0d)).mutate(deviation).doubleValue());
			if( fromEnd > length )
				crossoverPosition = 0;
			else
				crossoverPosition = -1 * (length - fromEnd);
		}
		else
		{
			final int leftLength = this.leftChromatid.getGenes().size() - this.leftChromatid.getCentromerePosition();
			final int rightLength = this.rightChromatid.getGenes().size() - this.leftChromatid.getCentromerePosition();

			final int length = (leftLength < rightLength ? leftLength : rightLength);

			final int fromEnd = (int) Math.abs((new MutableDouble(0d)).mutate(deviation).doubleValue());
			if( fromEnd > length )
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

	/**
	 * Creates a deep clone of the current object.
	 * @return A clone of a chromosome.
	 */
	@Override
	public Chromosome clone()
	{
		try
		{
			final Chromosome copy = (Chromosome) super.clone();
			copy.leftChromatid = this.leftChromatid.clone();
			copy.rightChromatid = this.rightChromatid.clone();
			copy.mutability = this.mutability;
			return copy;
		}
		catch(CloneNotSupportedException caught)
		{
			final String userMessage = "CloneNotSupportedException caught but not expected!";
			LOGGER.error(userMessage, caught);
			throw new UnexpectedDannError(userMessage, caught);
		}
	}

	/**
	 * Mutates the chromosome by the given key pool.
	 * @param keyPool The key pool to mutate by
	 * @see com.syncleus.dann.genetics.wavelets.WaveletChromatid#mutate(java.util.Set)
	 */
	public void mutate(final Set<AbstractKey> keyPool)
	{
		if( Mutations.mutationEvent(this.mutability) )
			this.crossover(this.mutability);
		this.leftChromatid.mutate(keyPool);
		this.rightChromatid.mutate(keyPool);
		if( Mutations.mutationEvent(this.mutability) )
			this.mutability = Mutations.mutabilityMutation(this.mutability);
	}
}
