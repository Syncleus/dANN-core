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

import java.util.List;
import java.util.Random;
import com.syncleus.dann.graph.AbstractDirectedEdge;

public abstract class AbstractSynapse<N extends Neuron> extends AbstractDirectedEdge<N> implements Synapse<N>, java.io.Serializable
{
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
	private static final Random RANDOM = new Random();

	/**
	 * Creates a new instance of SimpleSynapse
	 *
	 * @param sourceToSet The incomming neuron connection.
	 * @param destinationToSet The outgoing neuron connection.
	 * @param initialWeight The initial weight of the synapse
	 * @since 1.0
	 */
	protected AbstractSynapse(final N sourceToSet, final N destinationToSet, final double initialWeight)
	{
		super(sourceToSet, destinationToSet);
		this.weight = initialWeight;
	}

	/**
	 * Creates a new instance of SimpleSynapse
	 *
	 * @param sourceToSet The incomming neuron connection.
	 * @param destinationToSet The outgoing neuron connection.
	 * @since 1.0
	 */
	protected AbstractSynapse(final N sourceToSet, final N destinationToSet)
	{
		super(sourceToSet, destinationToSet);
		this.weight = ((RANDOM.nextDouble() * 2.0) - 1.0) / 10000.0;
	}

	/**
	 * Creates a new instance of SimpleSynapse
	 *
	 * @param sourceToSet The incomming neuron connection.
	 * @param destinationToSet The outgoing neuron connection.
	 * @param initialWeight The initial weight of the synapse
	 * @since 1.0
	 */
	protected AbstractSynapse(final N sourceToSet, final N destinationToSet, final double initialWeight, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(sourceToSet, destinationToSet, allowJoiningMultipleGraphs, contextEnabled);
		this.weight = initialWeight;
	}

	/**
	 * Creates a new instance of SimpleSynapse
	 *
	 * @param sourceToSet The incomming neuron connection.
	 * @param destinationToSet The outgoing neuron connection.
	 * @since 1.0
	 */
	protected AbstractSynapse(final N sourceToSet, final N destinationToSet, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(sourceToSet, destinationToSet, allowJoiningMultipleGraphs, contextEnabled);
		this.weight = ((RANDOM.nextDouble() * 2.0) - 1.0) / 10000.0;
	}

	/**
	 * Set the current input for the synapse.
	 *
	 * @param newInput The new input value to set.
	 * @since 1.0
	 */
	@Override
	public void setInput(final double newInput)
	{
		this.input = newInput;
	}

	/**
	 * Set the weight of the synapse.
	 *
	 * @param newWeight new weight for the synapse.
	 * @since 1.0
	 */
	@Override
	public void setWeight(final double newWeight)
	{
		this.weight = newWeight;
	}

	/**
	 * Get the weight of the synapse.
	 *
	 * @return The current weight of the synapse.
	 * @since 1.0
	 */
	@Override
	public double getWeight()
	{
		return this.weight;
	}

	/**
	 * Get the current input of the synapse.
	 *
	 * @return The current input of the synapse.
	 * @since 1.0
	 */
	@Override
	public double getInput()
	{
		return this.input;
	}

	@Override
	public AbstractSynapse<N> disconnect(final N node)
	{
		return (AbstractSynapse<N>) super.disconnect(node);
	}

	@Override
	public AbstractSynapse<N> disconnect(final List<N> nodes)
	{
		return (AbstractSynapse<N>) super.disconnect(nodes);
	}

	@Override
	public AbstractSynapse<N> clone()
	{
		return (AbstractSynapse<N>) super.clone();
	}
}
