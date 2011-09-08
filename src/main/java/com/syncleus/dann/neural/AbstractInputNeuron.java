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
package com.syncleus.dann.neural;

public abstract class AbstractInputNeuron extends AbstractNeuron implements InputNeuron
{
	private static final long serialVersionUID = 4397150011892747140L;
	private double input = 0.0;

	protected AbstractInputNeuron(final Brain<InputNeuron, OutputNeuron, Neuron, Synapse<Neuron>> brain)
	{
		super(brain);
	}

	@Override
	public void setInput(final double inputToSet)
	{
		if( Math.abs(inputToSet) > 1.0 )
			throw new IllegalArgumentException("InputToSet must be between -1 and +1");

		this.input = inputToSet;
	}

	@Override
	public double getInput()
	{
		return this.input;
	}

	@Override
	protected double getOutput()
	{
		return this.input;
	}

	@Override
	public void tick()
	{
		for (final Synapse<Neuron> current : getBrain().getTraversableEdges(this))
		{
			current.setInput(input);
		}
	}
}
