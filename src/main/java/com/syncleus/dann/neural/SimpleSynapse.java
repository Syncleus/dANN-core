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

import java.util.*;
import com.syncleus.dann.graph.AbstractDirectedEdge;
import com.syncleus.dann.graph.ImmutableDirectedEdge;

/**
 * The synapse acts as a bridge between connected neurons. It is also where the
 * connection weights are stores and manipulated.
 *
 * @author Jeffrey Phillips Freeman
 * @see com.syncleus.dann.neural.Neuron
 * @since 1.0
 */
public final class SimpleSynapse<N extends Neuron> extends AbstractSynapse<N>
{
	private static final long serialVersionUID = -209835498037456098L;
	/**
	 * Creates a new instance of SimpleSynapse
	 *
	 * @param sourceToSet The incomming neuron connection.
	 * @param destinationToSet The outgoing neuron connection.
	 * @param initialWeight The initial weight of the synapse
	 * @since 1.0
	 */
	public SimpleSynapse(final N sourceToSet, final N destinationToSet, final double initialWeight)
	{
		super(sourceToSet, destinationToSet, initialWeight);
	}

	/**
	 * Creates a new instance of SimpleSynapse
	 *
	 * @param sourceToSet The incomming neuron connection.
	 * @param destinationToSet The outgoing neuron connection.
	 * @since 1.0
	 */
	public SimpleSynapse(final N sourceToSet, final N destinationToSet)
	{
		super(sourceToSet, destinationToSet);
	}

	/**
	 * Creates a new instance of SimpleSynapse
	 *
	 * @param sourceToSet The incomming neuron connection.
	 * @param destinationToSet The outgoing neuron connection.
	 * @param initialWeight The initial weight of the synapse
	 * @since 1.0
	 */
	public SimpleSynapse(final N sourceToSet, final N destinationToSet, final double initialWeight, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(sourceToSet, destinationToSet, initialWeight, allowJoiningMultipleGraphs, contextEnabled);
	}

	/**
	 * Creates a new instance of SimpleSynapse
	 *
	 * @param sourceToSet The incomming neuron connection.
	 * @param destinationToSet The outgoing neuron connection.
	 * @since 1.0
	 */
	public SimpleSynapse(final N sourceToSet, final N destinationToSet, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(sourceToSet, destinationToSet, allowJoiningMultipleGraphs, contextEnabled);
	}

	@Override
	public SimpleSynapse<N> disconnect(final N node)
	{
		return (SimpleSynapse<N>) super.disconnect(node);
	}

	@Override
	public SimpleSynapse<N> disconnect(final List<N> nodes)
	{
		return (SimpleSynapse<N>) super.disconnect(nodes);
	}

	@Override
	public SimpleSynapse<N> clone()
	{
		return (SimpleSynapse<N>) super.clone();
	}
}
