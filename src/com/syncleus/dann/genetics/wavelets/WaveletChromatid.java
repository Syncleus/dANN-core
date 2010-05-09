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
import com.syncleus.dann.genetics.*;
import org.apache.log4j.Logger;

public class WaveletChromatid implements Chromatid<AbstractWaveletGene>, Cloneable
{
	//contains all the genes as their sequenced in the chromatid
	private List<AbstractWaveletGene> sequencedGenes;
	//contains all the promoter genes in an arbitrary order
	private List<PromoterGene> promoters;
	//contains just the local (non-external) signal genes in an arbitrary order.
	private List<SignalGene> localSignalGenes;
	//contains al the external signal genes in an arbitrary order.
	private List<ExternalSignalGene> externalSignalGenes;
	//Logger used to log debugging information.
	private final static Logger LOGGER = Logger.getLogger(WaveletChromatid.class);
	//Random used for all random values.
	private final static Random RANDOM = Mutations.getRandom();
	//Position of the gene's centromere. This is the origin where chromatid
	//pairs are joined.
	private int centromerePosition;
	//This chomatids chance of mutating. This value itself will mutate.
	private double mutability;

	private WaveletChromatid()
	{
		mutability = Mutations.getRandom().nextDouble() * 10.0;
	}

	public WaveletChromatid(final WaveletChromatid copy)
	{
		this.centromerePosition = copy.centromerePosition;
		this.mutability = copy.mutability;
		this.sequencedGenes = new ArrayList<AbstractWaveletGene>();
		this.promoters = new ArrayList<PromoterGene>();
		this.localSignalGenes = new ArrayList<SignalGene>();
		this.externalSignalGenes = new ArrayList<ExternalSignalGene>();
		for(final AbstractWaveletGene currentGene : copy.sequencedGenes)
			this.sequencedGenes.add(currentGene.clone());
		for(final PromoterGene currentGene : copy.promoters)
			this.promoters.add(currentGene.clone());
		for(final SignalGene currentGene : copy.localSignalGenes)
			this.localSignalGenes.add(currentGene.clone());
		for(final ExternalSignalGene currentGene : copy.externalSignalGenes)
			this.externalSignalGenes.add(currentGene.clone());
	}

	public static WaveletChromatid newRandomWaveletChromatid()
	{
		final WaveletChromatid newChromatid = new WaveletChromatid();
		while (newChromatid.sequencedGenes.size() <= 0)
			newChromatid.mutate(null);
		while (Mutations.mutationEvent(newChromatid.mutability))
			newChromatid.mutate(null);
		return newChromatid;
	}

	Set<SignalKey> getExpressedSignals(final boolean external)
	{
		//calculate the signal concentrations
		final HashSet<SignalKey> allSignals = new HashSet<SignalKey>();
		for(final AbstractWaveletGene waveletGene : this.sequencedGenes)
		{
			//if the current gene doesnt express a signal then skip it.
			if (!(waveletGene instanceof SignalGene))
				continue;
			//convert the gene's type
			final SignalGene gene = (SignalGene) waveletGene;
			//check if the gene's signal is internal or external. continue if
			//it doesnt match
			if (external)
			{
				//if its not an outward pointing external gene then just skip it
				if (!(gene instanceof ExternalSignalGene))
					continue;
				else if (!((ExternalSignalGene) gene).isOutward())
					continue;
			}
			else
			{
				//if its an outward pointing external than just skip it.
				if ((gene instanceof ExternalSignalGene) && (((ExternalSignalGene) gene).isOutward()))
					continue;
			}
			allSignals.add(gene.getOutputSignal());
		}
		return Collections.unmodifiableSet(allSignals);
	}

	public Set<AbstractKey> getKeys()
	{
		final HashSet<AbstractKey> allKeys = new HashSet<AbstractKey>();
		for(final AbstractWaveletGene gene : this.sequencedGenes)
			allKeys.addAll(gene.getKeys());
		return Collections.unmodifiableSet(allKeys);
	}

	public void preTick()
	{
		for(final AbstractWaveletGene gene : this.sequencedGenes)
			gene.preTick();
	}

	public void tick()
	{
		//first we need to calculate the promotion of each site
		final Hashtable<Integer, Double> promotions = new Hashtable<Integer, Double>();
		for(final PromoterGene promoter : this.promoters)
		{
			final int promoterIndex = this.sequencedGenes.indexOf(promoter);
			final int promotedIndex = promoter.getTargetDistance() + promoterIndex;
			if (promotedIndex < this.sequencedGenes.size())
			{
				double promotion = 0.0;
				if (promotions.contains(promotedIndex))
					promotion = promotions.get(promotedIndex);
				final double newPromotion = promotion + promoter.expressionActivity();
				if (newPromotion != 0.0)
					promotions.put(promotedIndex, newPromotion);
			}
		}
		for(int sequenceIndex = 0; sequenceIndex < this.sequencedGenes.size(); sequenceIndex++)
		{
			this.sequencedGenes.get(sequenceIndex).tick(promotions.get(sequenceIndex));
		}
	}

	public boolean bind(final SignalKeyConcentration concentration, final boolean isExternal)
	{
		boolean bound = false;
		for(final AbstractWaveletGene gene : this.sequencedGenes)
			if (gene.bind(concentration, isExternal))
				bound = true;
		return bound;
	}

	public int getCentromerePosition()
	{
		return this.centromerePosition;
	}

	public List<AbstractWaveletGene> getGenes()
	{
		return Collections.unmodifiableList(this.sequencedGenes);
	}

	public List<PromoterGene> getPromoterGenes()
	{
		return Collections.unmodifiableList(this.promoters);
	}

	public List<SignalGene> getLocalSignalGenes()
	{
		return Collections.unmodifiableList(this.localSignalGenes);
	}

	public List<ExternalSignalGene> getExternalSignalGenes()
	{
		return Collections.unmodifiableList(this.externalSignalGenes);
	}

	public List<AbstractWaveletGene> crossover(final int point)
	{
		final int index = point + this.centromerePosition;
		if ((index < 0) || (index > this.sequencedGenes.size()))
			return null;
		if ((index == 0) || (index == this.sequencedGenes.size()))
			return Collections.unmodifiableList(new ArrayList<AbstractWaveletGene>());
		if (point < 0)
			return Collections.unmodifiableList(this.sequencedGenes.subList(0, index));
		else
			return Collections.unmodifiableList(this.sequencedGenes.subList(index, this.sequencedGenes.size()));
	}

	public void crossover(final List<AbstractWaveletGene> geneticSegment, final int point)
	{
		final int index = point + this.centromerePosition;
		if ((index < 0) || (index > this.sequencedGenes.size()))
			throw new IllegalArgumentException("point is out of range for crossover");
		//calculate new centromere position
		final int newCentromerePostion = this.centromerePosition - (index - geneticSegment.size());
		//create new sequence of genes after crossover
		final ArrayList<AbstractWaveletGene> newGenes;
		final List<AbstractWaveletGene> oldGenes;
		if (point < 0)
		{
			newGenes = new ArrayList<AbstractWaveletGene>(geneticSegment);
			newGenes.addAll(this.sequencedGenes.subList(index, this.sequencedGenes.size()));
			oldGenes = this.sequencedGenes.subList(0, index);
		}
		else
		{
			newGenes = new ArrayList<AbstractWaveletGene>(this.sequencedGenes.subList(0, index));
			newGenes.addAll(geneticSegment);
			oldGenes = this.sequencedGenes.subList(index, this.sequencedGenes.size());
		}
		//remove displaced genes from specific gene type lists
		for(final AbstractWaveletGene oldGene : oldGenes)
		{
			if (oldGene instanceof PromoterGene)
				this.promoters.remove(oldGene);
			else if (oldGene instanceof ExternalSignalGene)
				this.externalSignalGenes.remove(oldGene);
			else if (oldGene instanceof SignalGene)
				this.localSignalGenes.remove(oldGene);
		}
		//add new genes to the specific gene type list
		for(final AbstractWaveletGene newGene : geneticSegment)
		{
			if (newGene instanceof PromoterGene)
				this.promoters.add((PromoterGene) newGene);
			else if (newGene instanceof ExternalSignalGene)
				this.externalSignalGenes.add((ExternalSignalGene) newGene);
			else if (newGene instanceof SignalGene)
				this.localSignalGenes.add((SignalGene) newGene);
		}
		//update sequence genes to use the new genes
		this.sequencedGenes = newGenes;
		this.centromerePosition = newCentromerePostion;
	}

	@Override
	public WaveletChromatid clone()
	{
		try
		{
			final WaveletChromatid copy = (WaveletChromatid) super.clone();
			copy.centromerePosition = this.centromerePosition;
			copy.mutability = this.mutability;
			copy.sequencedGenes = new ArrayList<AbstractWaveletGene>();
			copy.promoters = new ArrayList<PromoterGene>();
			copy.localSignalGenes = new ArrayList<SignalGene>();
			copy.externalSignalGenes = new ArrayList<ExternalSignalGene>();
			for(final AbstractWaveletGene currentGene : this.sequencedGenes)
				copy.sequencedGenes.add(currentGene.clone());
			for(final PromoterGene currentGene : this.promoters)
				copy.promoters.add(currentGene.clone());
			for(final SignalGene currentGene : this.localSignalGenes)
				copy.localSignalGenes.add(currentGene.clone());
			for(final ExternalSignalGene currentGene : this.externalSignalGenes)
				copy.externalSignalGenes.add(currentGene.clone());
			return copy;
		}
		catch (CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

	private static AbstractKey randomKey(final Set<AbstractKey> keyPool)
	{
		if ((keyPool != null) && (!keyPool.isEmpty()))
		{
			//select a random key from the pool
			AbstractKey randomKey = null;
			int keyIndex = RANDOM.nextInt(keyPool.size());
			for(final AbstractKey key : keyPool)
			{
				if (keyIndex <= 0)
				{
					randomKey = key;
					break;
				}
				else
					keyIndex--;
			}
			assert randomKey != null;
			return new ReceptorKey(randomKey);
		}
		return new ReceptorKey();
	}

	public void mutate(final Set<AbstractKey> keyPool)
	{
		//there is a chance we will add a new gene to the chromatid
		if (Mutations.mutationEvent(mutability))
		{
			//generate the new receptorKey used in the new gene
			ReceptorKey newReceptorKey = new ReceptorKey(randomKey(keyPool));
			//mutate new receptorKey before using it
			while (Mutations.mutationEvent(this.mutability))
				newReceptorKey = newReceptorKey.mutate(mutability);
			//create a new gene using the new receptor
			final AbstractWaveletGene newGene;
			final SignalKey newSignalKey = new SignalKey(randomKey(keyPool));
			switch (RANDOM.nextInt(3))
			{
			case 0:
				final MutableInteger initialDistance = (new MutableInteger(0)).mutate(mutability);
				newGene = new PromoterGene(newReceptorKey, initialDistance.intValue());
				this.promoters.add((PromoterGene) newGene);
				break;
			case 1:
				newGene = new SignalGene(newReceptorKey, newSignalKey);
				this.localSignalGenes.add((SignalGene) newGene);
				break;
			default:
				newGene = new ExternalSignalGene(newReceptorKey, newSignalKey, RANDOM.nextBoolean());
				this.externalSignalGenes.add((ExternalSignalGene) newGene);
			}
			//add the new gene to the sequence. there is an equal chance the
			//gene will be added to the head and tail
			if (RANDOM.nextBoolean())
				this.sequencedGenes.add(0, newGene);
			else
				this.sequencedGenes.add(newGene);
		}
		//mutate each gene (the gene itself will handle if it actually mutates)
		for(final AbstractWaveletGene currentGene : this.sequencedGenes)
			currentGene.mutate(keyPool);
		//mutate the mutability factor.
		if (Mutations.mutationEvent(mutability))
			this.mutability = Mutations.mutabilityMutation(this.mutability);
	}
}
