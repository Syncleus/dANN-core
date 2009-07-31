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

import com.syncleus.dann.genetics.Gene;
import com.syncleus.dann.math.AbstractMathFunction;
import java.util.*;

public abstract class AbstractWaveletGene implements Gene
{
	private double currentActivity;
	private double pendingActivity;
	protected ExpressionFunction expressionFunction;

	protected HashSet<SignalKeyConcentration> receivingConcentrations;

	private double mutability;

	private static Random random = Mutation.getRandom();

	protected AbstractWaveletGene(ReceptorKey initialReceptor)
	{
		this.expressionFunction = new ExpressionFunction(initialReceptor);
		this.mutability = 1d;
		this.receivingConcentrations = new HashSet<SignalKeyConcentration>();
	}

	protected AbstractWaveletGene(AbstractWaveletGene copy)
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

	protected Random getRandom()
	{
		return random;
	}

	protected double getMutability()
	{
		return this.mutability;
	}

	public AbstractMathFunction getExpressionActivityMathFunction()
	{
		return this.expressionFunction.getWaveletMathFunction();
	}

	public double expressionActivity()
	{
		return this.currentActivity;
	}

	public boolean bind(SignalKeyConcentration concentration, boolean isExternal)
	{
		if(isExternal)
			return false;

		if( this.expressionFunction.receives(concentration.getSignal()))
		{
			this.receivingConcentrations.add(concentration);
			return true;
		}

		return false;
	}

	public void preTick()
	{
		this.pendingActivity = this.expressionFunction.calculate(receivingConcentrations);
	}

	public void tick(double promotion)
	{
		this.currentActivity = this.pendingActivity + (this.pendingActivity * promotion);
	}


	public void mutate(Set<AbstractKey> keyPool)
	{
		this.currentActivity = 0.0;
		this.pendingActivity = 0.0;

		if((keyPool != null)||(keyPool.isEmpty()))
		{
			ReceptorKey newReceptor = new ReceptorKey(new ArrayList<AbstractKey>(keyPool).get(random.nextInt(keyPool.size())));
			this.expressionFunction.mutate(mutability, newReceptor);
		}
		else
			this.expressionFunction.mutate(mutability);

		if( Mutation.mutationEvent(this.mutability) )
			this.mutability = Mutation.mutabilityMutation(mutability);
	}

	@Override
	public abstract AbstractWaveletGene clone();
}
