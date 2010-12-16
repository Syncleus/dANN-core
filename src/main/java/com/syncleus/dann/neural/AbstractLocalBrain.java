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

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.graph.AbstractBidirectedAdjacencyGraph;
import com.syncleus.dann.graph.topological.StrongConnectivityOptimizedGraph;

// TODO refactor this to be a generic following the patern of its parent classes. Specifically to use Mutable graph type.

/**
 * Represents a single artificial brain typically belonging to a single
 * artificial organism. It will contain a set of input and output neurons which
 * corelates to a specific dataset pattern.<br/> <br/> This class is abstract
 * and must be extended in order to be used.
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public abstract class AbstractLocalBrain<IN extends InputNeuron, ON extends OutputNeuron, N extends Neuron, S extends Synapse<N>> extends AbstractBidirectedAdjacencyGraph<N, S> implements Brain<IN,ON,N,S>, Serializable, StrongConnectivityOptimizedGraph<N, S>
{
	private class NodeConnectivity extends HashMap<N, Set<S>>
	{
		private static final long serialVersionUID = -2956514569529162804L;

		@Override
		public Set<S> get(final Object keyObject)
		{
			if( !(keyObject instanceof Neuron) )
				throw new UnexpectedDannError("keyObject was not a Neuron");
			Set<S> edges = super.get((N) keyObject);
			if( edges == null )
			{
				edges = new HashSet<S>();
				final N key = (N) keyObject;
				super.put(key, edges);
			}
			return edges;
		}
	}

	private static final long serialVersionUID = -7626975911198443367L;
	private final Set<N> neurons = new HashSet<N>();
	private final Set<ON> outputNeurons = new HashSet<ON>();
	private final Set<IN> inputNeurons = new HashSet<IN>();
	private final Set<S> synapses = new HashSet<S>();
	private final Map<N, Set<S>> outMap = new NodeConnectivity();
	private final Map<N, Set<S>> inMap = new NodeConnectivity();
	private static final Random RANDOM = new Random();
	private final ExecutorService threadExecutor;

	/**
	 * Uses the given threadExecutor for executing tasks.
	 *
	 * @param threadExecutor executor to use for executing tasks.
	 * @since 2.0
	 */
	protected AbstractLocalBrain(final ExecutorService threadExecutor)
	{
		this.threadExecutor = threadExecutor;
	}

	/**
	 * Default constructor initializes a default threadExecutor based on the number
	 * of processors.
	 *
	 * @since 2.0
	 */
	protected AbstractLocalBrain()
	{
		this.threadExecutor = null;
	}

	protected boolean add(final S newSynapse)
	{
		if( newSynapse == null )
			throw new IllegalArgumentException("newSynapse can not be null");
		if( !this.neurons.containsAll(newSynapse.getNodes()) )
			throw new IllegalArgumentException("newSynapse has a node as an end point that is not part of the graph");

		if( this.synapses.add(newSynapse) )
		{
			this.outMap.get(newSynapse.getSourceNode()).add(newSynapse);
			this.inMap.get(newSynapse.getDestinationNode()).add(newSynapse);
			return true;
		}

		return false;
	}

	/**
	 * Adds a new neuron to the brain. The construction of the brain is done by
	 * the child class so this method is protected.
	 *
	 * @param newNeuron The neuron to add to the brain.
	 * @since 1.0
	 */
	protected boolean add(final N newNeuron)
	{
		if( newNeuron == null )
			throw new IllegalArgumentException("newNeuron can not be null");

		if( this.neurons.add(newNeuron) )
		{
			this.outMap.put(newNeuron, new HashSet<S>());
			this.inMap.put(newNeuron, new HashSet<S>());
			// TODO fix this, its bad typing
			if( newNeuron instanceof OutputNeuron )
				this.outputNeurons.add((ON)newNeuron);
			if( newNeuron instanceof InputNeuron )
				this.inputNeurons.add((IN) newNeuron);
			return true;
		}

		return false;
	}

	/**
	 * Adds a new collection of neurons to the brain. The construction of the brain
	 * is done by the child class so this method is protected.
	 *
	 * @param newNeurons The collection of neurons to add.
	 * @since 1.0
	 */
	protected boolean add(final Collection<? extends N> newNeurons)
	{
		if( newNeurons == null )
			throw new IllegalArgumentException("newNeurons can not be null");

		if( newNeurons.size() <= 0 )
			return false;

		final boolean added = this.neurons.addAll(newNeurons);

		for(final N newNeuron : newNeurons)
		{
			this.outMap.put(newNeuron, new HashSet<S>());
			this.inMap.put(newNeuron, new HashSet<S>());
			// TODO fix this, its bad typing
			if( newNeuron instanceof OutputNeuron )
				this.outputNeurons.add((ON) newNeuron);
			if( newNeuron instanceof InputNeuron )
				this.inputNeurons.add((IN) newNeuron);
		}

		return added;
	}


	protected boolean connect(final S synapse, final boolean initializeWeight)
	{
		if(initializeWeight)
			synapse.setWeight(((RANDOM.nextDouble() * 2.0) - 1.0) / 10000.0);
		return this.connect(synapse);
	}

	/*
	protected boolean connect(final N source, final N destination, final double initialWeight)
	{
		if( source == null )
			throw new IllegalArgumentException("source can not be null");
		if( destination == null )
			throw new IllegalArgumentException("destination can not be null");
		if( !this.neurons.contains(source) )
			throw new IllegalArgumentException("source is not a member of this graph");
		if( !this.neurons.contains(destination) )
			throw new IllegalArgumentException("destination is not a member of this graph");

		return this.add((S)(new SimpleSynapse(source, destination, initialWeight)));
	} */
	protected boolean connect(final S synapse)
	{
		if( !this.neurons.containsAll(synapse.getNodes()) )
			throw new IllegalArgumentException("the synapse contains nodes not in this brain");

		return this.add(synapse);
	}


	/*
	protected boolean disconnect(final Neuron source, final Neuron destination)
	{
		if( source == null )
			throw new IllegalArgumentException("source can not be null");
		if( destination == null )
			throw new IllegalArgumentException("destination can not be null");
		if( !this.neurons.contains(source) )
			throw new IllegalArgumentException("source is not a member of this graph");
		if( !this.neurons.contains(destination) )
			throw new IllegalArgumentException("destination is not a member of this graph");

		return this.remove(new SimpleSynapse(source, destination, 0.0));
	}*/

	protected boolean remove(final S removeSynapse)
	{
		if( removeSynapse == null )
			throw new IllegalArgumentException("removeSynapse can not be null");

		if( this.synapses.remove(removeSynapse) )
		{
			if( this.outMap.containsKey(removeSynapse.getSourceNode()) )
				this.outMap.get(removeSynapse.getSourceNode()).remove(removeSynapse);
			if( this.inMap.containsKey(removeSynapse.getDestinationNode()) )
				this.inMap.get(removeSynapse.getDestinationNode()).remove(removeSynapse);
			return true;
		}
		return false;
	}

	/**
	 * Removes the specified neuron from the brain. This only removes it from
	 * the collection of neurons it does not disconnect it from other neurons.
	 *
	 * @param removeNeuron The neuron to remove.
	 * @since 1.0
	 */
	protected boolean remove(final N removeNeuron)
	{
		if( removeNeuron == null )
			throw new IllegalArgumentException("node can not be null");

		if( this.neurons.remove(removeNeuron) )
		{
			final Set<Synapse> removeEdges = new HashSet<Synapse>();
			if( this.outMap.containsKey(removeNeuron) )
				removeEdges.addAll(this.outMap.remove(removeNeuron));
			if( this.inMap.containsKey(removeNeuron) )
				removeEdges.addAll(this.inMap.remove(removeNeuron));
			this.synapses.removeAll(removeEdges);

			if( removeNeuron instanceof OutputNeuron )
				this.outputNeurons.remove(removeNeuron);
			if( removeNeuron instanceof InputNeuron )
				this.inputNeurons.remove(removeNeuron);

			return true;
		}
		return false;
	}

	/**
	 * Obtains all InputNeurons contained within the brain.
	 *
	 * @return An unmodifiable Set of InputNeurons.
	 * @since 1.0
	 */
	@Override
	public Set<IN> getInputNeurons()
	{
		return Collections.unmodifiableSet(this.inputNeurons);
	}

	/**
	 * Obtains all OutputNeurons contained within the brain.
	 *
	 * @return An unmodifiable Set of OutputNeurons
	 * @since 1.0
	 */
	@Override
	public Set<ON> getOutputNeurons()
	{
		return Collections.unmodifiableSet(this.outputNeurons);
	}

	/**
	 * Obtains all Neurons, including InputNeurons and OutputNeurons contained
	 * within the brain.
	 *
	 * @return An unmodifiable Set of all Neurons.
	 * @since 1.0
	 */
	@Override
	public Set<N> getNodes()
	{
		return Collections.unmodifiableSet(this.neurons);
	}

	@Override
	public Set<S> getEdges()
	{
		return Collections.unmodifiableSet(this.synapses);
	}

	@Override
	public Set<S> getAdjacentEdges(final N node)
	{
		final Set<S> nodeSynapses = new HashSet<S>();
		if( this.outMap.containsKey(node) )
			nodeSynapses.addAll(this.outMap.get(node));
		if( this.inMap.containsKey(node) )
			nodeSynapses.addAll(this.inMap.get(node));
		return Collections.unmodifiableSet(nodeSynapses);
	}

	@Override
	public Set<S> getTraversableEdges(final N node)
	{
		if( this.inMap.containsKey(node) )
			return Collections.unmodifiableSet(this.outMap.get(node));
		return Collections.emptySet();
	}

	@Override
	public Set<S> getInEdges(final N node)
	{
		if( this.inMap.containsKey(node) )
			return Collections.unmodifiableSet(this.inMap.get(node));
		return Collections.emptySet();
	}

	@Override
	public boolean isStronglyConnected(final N leftNode, final N rightNode)
	{
		final Set<S> outSet = this.getTraversableEdges(leftNode);
		final Set<S> inSet = this.getInEdges(rightNode);
		final Set<S> jointSet = new HashSet<S>(outSet);
		jointSet.retainAll(inSet);

		return (!jointSet.isEmpty());
	}

	@Override
	public boolean isStronglyConnected()
	{
		throw new UnsupportedOperationException("This optimization is not supported");
	}

	@Override
	public List<N> getAdjacentNodes(final N node)
	{
		final Set<S> nodeSynapses = this.getAdjacentEdges(node);
		final List<N> neighbors = new ArrayList<N>();
		for(final S nodeSynapse : nodeSynapses)
			neighbors.add((nodeSynapse.getLeftNode().equals(node) ? nodeSynapse.getRightNode() : nodeSynapse.getLeftNode()));
		return Collections.unmodifiableList(neighbors);
	}

	/**
	 * threadExecutor used to execute processes.
	 *
	 * @return the threadExecutor used to execute processes.
	 * @since 2.0
	 */
	protected ExecutorService getThreadExecutor()
	{
		return this.threadExecutor;
	}
}
