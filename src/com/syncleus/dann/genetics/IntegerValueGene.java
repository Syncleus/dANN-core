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
package com.syncleus.dann.genetics;

public class IntegerValueGene extends ValueGene<MutableInteger>
{
	public IntegerValueGene()
	{
		super(new MutableInteger(0));
	}

	public IntegerValueGene(int value)
	{
		super(new MutableInteger(value));
	}

	public IntegerValueGene(Integer value)
	{
		super(new MutableInteger(value));
	}

	public IntegerValueGene(MutableInteger value)
	{
		super(value);
	}

	public IntegerValueGene(IntegerValueGene copyGene)
	{
		super(copyGene);
	}

	public IntegerValueGene clone()
	{
		return new IntegerValueGene(this);
	}

	public IntegerValueGene mutate(double deviation)
	{
		IntegerValueGene copy = new IntegerValueGene(this);
		copy.internalMutate(deviation);

		return copy;
	}
}
