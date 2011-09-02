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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SignalGene extends AbstractWaveletGene
{
	private SignalKey outputSignal;
	private SignalKeyConcentration expressingConcentration;

	public SignalGene(final ReceptorKey initialReceptor, final SignalKey initialSignal)
	{
		super(initialReceptor);

		this.outputSignal = initialSignal;
		this.expressingConcentration = null;
	}

	public SignalGene(final SignalGene copy)
	{
		super(copy);

		this.outputSignal = copy.outputSignal;
	}

	@Override
	public boolean bind(final SignalKeyConcentration concentration, final boolean isExternal)
	{
		boolean bound;
		bound = super.bind(concentration, isExternal);
		if( concentration.getSignal().equals(this.outputSignal) )
		{
			this.expressingConcentration = concentration;
			bound = true;
		}
		return bound;
	}

	@Override
	public void tick(final double promotion)
	{
		super.tick(promotion);
		this.expressingConcentration.setConcentration(this.expressingConcentration.getConcentration() + this.expressionActivity());
	}

	@Override
	public Set<AbstractKey> getKeys()
	{
		final Set<AbstractKey> allKeys = new HashSet<AbstractKey>(super.getKeys());
		allKeys.add(this.outputSignal);
		return Collections.unmodifiableSet(allKeys);
	}

	public SignalKey getOutputSignal()
	{
		return this.outputSignal;
	}

	protected SignalKeyConcentration getExpressingConcentration()
	{
		return expressingConcentration;
	}

	protected void setExpressingConcentration(final SignalKeyConcentration newExpressingConcentration)
	{
		this.expressingConcentration = newExpressingConcentration;
	}

	@Override
	public Object clone()
	{
		final SignalGene copy = (SignalGene) super.clone();
		copy.outputSignal = this.outputSignal;
		return copy;
	}

	@Override
	public void mutate(final Set<AbstractKey> keyPool)
	{
		super.mutate(keyPool);
		this.outputSignal = (SignalKey) this.outputSignal.mutate(this.getMutability());
	}
}
