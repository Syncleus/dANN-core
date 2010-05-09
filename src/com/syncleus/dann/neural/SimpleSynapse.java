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

import com.syncleus.dann.graph.ImmutableDirectedEdge;
import java.util.List;
import java.util.Random;

/**
 * The synapse acts as a bridge between connected neurons. It is also where the
 * connection weights are stores and manipulated.
 * 
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 *
 * @see com.syncleus.dann.neural.Neuron
 */
public class SimpleSynapse extends ImmutableDirectedEdge<Neuron> implements Synapse, java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">

	private static final long serialVersionUID = -7939448149356677295L;
	
    /**
     * The current weight of the synapse.
     *
     * @since 1.0
     */
    private double weight;

    /**
     * The current input from the synapse.
     *
     * @since 1.0
     */
    private double input;

	private final Random RANDOM = new Random();

    /**
     * Creates a new instance of SimpleSynapse
     *
     * @since 1.0
     * @param sourceToSet The incomming neuron connection.
     * @param destinationToSet The outgoing neuron connection.
     * @param initialWeight The initial weight of the synapse
     */
    public SimpleSynapse(final Neuron sourceToSet, final Neuron destinationToSet, final double initialWeight)
    {
		super(sourceToSet, destinationToSet);

        this.weight = initialWeight;
    }

    /**
     * Creates a new instance of SimpleSynapse
     *
     * @since 1.0
     * @param sourceToSet The incomming neuron connection.
     * @param destinationToSet The outgoing neuron connection.
     * @param initialWeight The initial weight of the synapse
     */
    public SimpleSynapse(final Neuron sourceToSet, final Neuron destinationToSet)
    {
		super(sourceToSet, destinationToSet);

        this.weight = ((RANDOM.nextDouble() * 2.0) - 1.0) / 10000.0;
    }

    /**
     * Set the current input for the synapse.
	 *
     *
     * @since 1.0
     * @param newInput The new input value to set.
     */
    public void setInput(final double newInput)
    {
        this.input = newInput;
    }

	/**
	 * Set the weight of the synapse.
	 *
	 *
	 * @param newWeight new weight for the synapse.
	 * @since 1.0
	 */
	public void setWeight(final double newWeight)
	{
		this.weight = newWeight;
	}

	/**
	 * Get the weight of the synapse.
	 *
	 *
	 * @return The current weight of the synapse.
	 * @since 1.0
	 */
    public double getWeight()
    {
        return weight;
    }

	/**
	 * Get the current input of the synapse.
	 *
	 *
	 * @return The current input of the synapse.
	 * @since 1.0
	 */
	public double getInput()
	{
		return input;
	}

	@Override
	public SimpleSynapse disconnect(final Neuron node)
	{
		if(node == null)
			throw new IllegalArgumentException("node can not be null", new NullPointerException());
		if(!this.getNodes().contains(node))
			throw new IllegalArgumentException("node is not currently connected to");

		return (SimpleSynapse) this.remove(node);
	}

	@Override
	public SimpleSynapse disconnect(final List<Neuron> nodes)
	{
		if(nodes == null)
			throw new IllegalArgumentException("node can not be null", new NullPointerException());
		if(!this.getNodes().containsAll(nodes))
			throw new IllegalArgumentException("node is not currently connected to");

		return (SimpleSynapse) this.remove(nodes);
	}

	@Override
	public SimpleSynapse clone()
	{
		return (SimpleSynapse) super.clone();
	}
}
