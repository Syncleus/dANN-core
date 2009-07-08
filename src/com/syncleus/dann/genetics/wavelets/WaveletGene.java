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
import com.syncleus.dann.math.MathFunction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class WaveletGene implements Gene
{
	private double currentActivity;
	private double pendingActivity;
	private ExpressionFunction expressionFunction;

	private HashSet<SignalKeyConcentration> signalConcentrations;

	private double mutability;

	private static Random random = new Random();

	protected WaveletGene(ReceptorKey initialReceptor)
	{
		this.expressionFunction = new ExpressionFunction(initialReceptor);
		this.mutability = 1d;
		this.signalConcentrations = new HashSet<SignalKeyConcentration>();
	}

	protected WaveletGene(WaveletGene copy)
	{
		this.currentActivity = copy.currentActivity;
		this.pendingActivity = copy.pendingActivity;
		this.expressionFunction = copy.expressionFunction;

		this.mutability = copy.mutability;
		this.signalConcentrations = new HashSet<SignalKeyConcentration>(copy.signalConcentrations);
	}

	protected Random getRandom()
	{
		return random;
	}

	protected double getMutability()
	{
		return this.mutability;
	}

	public MathFunction getExpressionActivityMathFunction()
	{
		return this.expressionFunction.getWaveletMathFunction();
	}

	public double expressionActivity()
	{
		return this.currentActivity;
	}

	public boolean bind(SignalKeyConcentration concentration, boolean isExternal)
	{
		if( this.expressionFunction.receives(concentration.getSignal()))
		{
			this.signalConcentrations.add(concentration);
			return true;
		}

		return false;
	}

	public void preTick()
	{
		this.pendingActivity = this.expressionFunction.calculate(signalConcentrations);
	}

	public void tick()
	{
		this.currentActivity = this.pendingActivity;
	}


	public void mutate(Set<Key> keyPool)
	{
		this.currentActivity = 0.0;
		this.pendingActivity = 0.0;

		if(keyPool != null)
		{
			ReceptorKey newReceptor = new ReceptorKey(new ArrayList<Key>(keyPool).get(random.nextInt(keyPool.size())));
			this.expressionFunction.mutate(mutability, newReceptor);
		}
		else
			this.expressionFunction.mutate(mutability);

		this.mutability = Mutation.mutabilityMutation(mutability);
	}

	@Override
	public abstract WaveletGene clone();
}
