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

public class SignalGene extends AbstractWaveletGene implements Cloneable
{
	private SignalKey outputSignal;
	protected SignalKeyConcentration expressingConcentration;

	public SignalGene(ReceptorKey initialReceptor, SignalKey initialSignal)
	{
		super(initialReceptor);

		this.outputSignal = initialSignal;
	}

	public SignalGene(SignalGene copy)
	{
		super(copy);

		this.outputSignal = copy.outputSignal;
	}

	@Override
	public boolean bind(SignalKeyConcentration concentration, boolean isExternal)
	{
		boolean bound;
		bound = super.bind(concentration, isExternal);
		if( concentration.getSignal().equals(this.outputSignal))
		{
			this.expressingConcentration = concentration;
			bound = true;
		}
		return bound;
	}
	
	public void tick(double promotion)
	{
		super.tick(promotion);
		this.expressingConcentration.setConcentration(this.expressingConcentration.getConcentration() + this.expressionActivity());
	}

	@Override
	public Set<AbstractKey> getKeys()
	{
		HashSet<AbstractKey> allKeys = new HashSet<AbstractKey>(super.getKeys());
		allKeys.add(this.outputSignal);
		return Collections.unmodifiableSet(allKeys);
	}

	public SignalKey getOutputSignal()
	{
		return this.outputSignal;
	}

	@Override
	public SignalGene clone() throws CloneNotSupportedException
	{
		SignalGene copy = (SignalGene) super.clone();
		copy.outputSignal = this.outputSignal;
		return copy;
	}

	@Override
	public void mutate(Set<AbstractKey> keyPool) throws CloneNotSupportedException
	{
		super.mutate(keyPool);
		this.outputSignal = this.outputSignal.mutate(this.getMutability());
	}
}
