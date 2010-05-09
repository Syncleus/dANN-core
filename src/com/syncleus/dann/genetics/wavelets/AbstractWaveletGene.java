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
import com.syncleus.dann.genetics.Gene;
import com.syncleus.dann.math.AbstractFunction;
import org.apache.log4j.Logger;

public abstract class AbstractWaveletGene implements Gene, Cloneable
{
	private double currentActivity;
	private double pendingActivity;
	private double mutability;
	private static final Logger LOGGER = Logger.getLogger(AbstractWaveletGene.class);
	protected ExpressionFunction expressionFunction;
	protected Set<SignalKeyConcentration> receivingConcentrations;
	protected static final Random RANDOM = Mutations.getRandom();

	protected AbstractWaveletGene(final ReceptorKey initialReceptor)
	{
		this.expressionFunction = new ExpressionFunction(initialReceptor);
		this.mutability = 1d;
		this.receivingConcentrations = new HashSet<SignalKeyConcentration>();
	}

	protected AbstractWaveletGene(final AbstractWaveletGene copy)
	{
		this.currentActivity = copy.currentActivity;
		this.pendingActivity = copy.pendingActivity;
		this.expressionFunction = copy.expressionFunction;
		this.mutability = copy.mutability;
		this.receivingConcentrations = new HashSet<SignalKeyConcentration>(copy.receivingConcentrations);
	}

	public Set<AbstractKey> getKeys()
	{
		return Collections.unmodifiableSet(new HashSet<AbstractKey>(this.expressionFunction.getReceptors()));
	}

	protected final double getMutability()
	{
		return this.mutability;
	}

	public final AbstractFunction getExpressionActivityMathFunction()
	{
		return this.expressionFunction.getWaveletMathFunction();
	}

	public final double expressionActivity()
	{
		return this.currentActivity;
	}

	public boolean bind(final SignalKeyConcentration concentration, final boolean isExternal)
	{
		if (isExternal)
			return false;
		if (this.expressionFunction.receives(concentration.getSignal()))
		{
			this.receivingConcentrations.add(concentration);
			return true;
		}
		return false;
	}

	public void preTick()
	{
		this.pendingActivity = this.expressionFunction.calculate(this.receivingConcentrations);
	}

	public void tick(final double promotion)
	{
		this.currentActivity = this.pendingActivity + (this.pendingActivity * promotion);
	}

	public void mutate(final Set<AbstractKey> keyPool)
	{
		this.currentActivity = 0.0;
		this.pendingActivity = 0.0;
		if ((keyPool != null) && (keyPool.isEmpty()))
		{
			final ReceptorKey newReceptor = new ReceptorKey(new ArrayList<AbstractKey>(keyPool).get(RANDOM.nextInt(keyPool.size())));
			this.expressionFunction.mutate(this.mutability, newReceptor);
		}
		else
			this.expressionFunction.mutate(this.mutability);
		if (Mutations.mutationEvent(this.mutability))
			this.mutability = Mutations.mutabilityMutation(this.mutability);
	}

	@Override
	public AbstractWaveletGene clone()
	{
		try
		{
			final AbstractWaveletGene copy = (AbstractWaveletGene) super.clone();
			copy.currentActivity = this.currentActivity;
			copy.pendingActivity = this.pendingActivity;
			copy.expressionFunction = this.expressionFunction.clone();
			copy.mutability = this.mutability;
			copy.receivingConcentrations = new HashSet<SignalKeyConcentration>(this.receivingConcentrations);
			return copy;
		}
		catch (CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}
}
