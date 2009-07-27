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
import java.util.Set;
public class ExternalSignalGene extends SignalGene
{
	private boolean outward;

	public ExternalSignalGene(ReceptorKey initialReceptor, SignalKey initialSignal, boolean outward)
	{
		super(initialReceptor, initialSignal);

		this.outward = outward;
	}
	
	public ExternalSignalGene(ExternalSignalGene copy)
	{
		super(copy);
	}

	public boolean isOutward()
	{
		return this.outward;
	}

	@Override
	public ExternalSignalGene clone()
	{
		return new ExternalSignalGene(this);
	}

	@Override
	public void mutate(Set<Key> keyPool)
	{
		super.mutate(keyPool);

		if(this.getRandom().nextDouble() < Math.tanh(this.getMutability()))
			this.outward = !this.outward;
	}
}
