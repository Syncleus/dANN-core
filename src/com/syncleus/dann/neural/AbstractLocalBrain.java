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

import com.syncleus.dann.UnexpectedDannError;
import java.util.*;
import java.util.concurrent.*;
import com.syncleus.dann.graph.AbstractBidirectedGraph;
import java.io.Serializable;

/**
 * Represents a single artificial brain typically belonging to a single
 * artificial organism. It will contain a set of input and output neurons which
 * corelates to a specific dataset pattern.<br/>
 * <br/>
 * This class is abstract and must be extended in order to be used.
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 *
 */
public abstract class AbstractLocalBrain extends AbstractBidirectedGraph<Neuron, Synapse> implements Brain, Serializable
{
	private static class NodeConnectivity extends HashMap<Neuron,Set<Synapse>>
	{
		private static final long serialVersionUID = -2956514569529162804L;
		
		@Override
		public Set<Synapse> get(Object keyObject)
		{
			if(!(keyObject instanceof Neuron))
				throw new UnexpectedDannError("keyObject was not a Neuron");
			Set<Synapse> edges = super.get((Neuron)keyObject);
			if( edges == null )
			{
				edges = new HashSet<Synapse>();
				Neuron key = (Neuron) keyObject;
				super.put(key, edges);
			}
			return edges;
		}
	}

	private static final long serialVersionUID = -7626975911198443367L;

    private Set<Neuron> neurons = new HashSet<Neuron>();
    private Set<OutputNeuron> outputNeurons = new HashSet<OutputNeuron>();
    private Set<InputNeuron> inputNeurons = new HashSet<InputNeuron>();

	private final Set<Synapse> synapses = new HashSet<Synapse>();
	private final Map<Neuron,Set<Synapse>> outMap = new NodeConnectivity();
	private final Map<Neuron,Set<Synapse>> inMap = new NodeConnectivity();

	private static final Random RANDOM = new Random();

	private ThreadPoolExecutor threadExecutor;


	/**
	 * Uses the given threadExecutor for executing tasks.
	 *
	 * @param threadExecutor executor to use for executing tasks.
	 * @since 2.0
	 */
	public AbstractLocalBrain(ThreadPoolExecutor threadExecutor)
	{
		this.threadExecutor = threadExecutor;
	}

	/**
	 * Default constructor initializes a default threadExecutor based on the
	 * number of processors.
	 *
	 * @since 2.0
	 */
	public AbstractLocalBrain()
	{
		this.threadExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors()*5, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	protected boolean add(Synapse newSynapse)
	{
		if( newSynapse == null )
			throw new IllegalArgumentException("newSynapse can not be null");
		if( ! this.neurons.containsAll(newSynapse.getNodes()) )
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
	 * Adds a new neuron to the brain. The construction of the brain is done
	 * by the child class so this method is protected.
	 *
	 *
	 * @param newNeuron The neuron to add to the brain.
	 * @since 1.0
	 */
    protected boolean add(Neuron newNeuron)
    {
		if(newNeuron == null)
			throw new IllegalArgumentException("newNeuron can not be null");

		if( this.neurons.add(newNeuron) )
		{
			this.outMap.put(newNeuron, new HashSet<Synapse>());
			this.inMap.put(newNeuron, new HashSet<Synapse>());
			if (newNeuron instanceof OutputNeuron)
				this.outputNeurons.add((OutputNeuron) newNeuron);
			if (newNeuron instanceof InputNeuron)
				this.inputNeurons.add((InputNeuron) newNeuron);
			return true;
		}

		return false;
    }

	/**
	 * Adds a new collection of neurons to the brain. The construction of the
	 * brain is done by the child class so this method is protected.
	 *
	 * 
	 * @param newNeurons The collection of neurons to add.
	 * @since 1.0
	 */
	protected boolean add(Collection<? extends Neuron> newNeurons)
	{
		if( newNeurons == null )
			throw new IllegalArgumentException("newNeurons can not be null");

		if( newNeurons.size() <= 0 )
			return false;

		boolean added = this.neurons.addAll(newNeurons);

		for(Neuron newNeuron : newNeurons)
		{
			this.outMap.put(newNeuron, new HashSet<Synapse>());
			this.inMap.put(newNeuron, new HashSet<Synapse>());
			if (newNeuron instanceof OutputNeuron)
				this.outputNeurons.add((OutputNeuron) newNeuron);
			if (newNeuron instanceof InputNeuron)
				this.inputNeurons.add((InputNeuron) newNeuron);
		}

		return added;
	}

	protected boolean connect(Neuron source, Neuron destination)
	{
		return this.connect(source, destination, ((RANDOM.nextDouble() * 2.0) - 1.0) / 10000.0);
	}

	protected boolean connect(Neuron source, Neuron destination, double initialWeight)
	{
		if( source == null )
			throw new IllegalArgumentException("source can not be null");
		if( destination == null )
			throw new IllegalArgumentException("destination can not be null");
		if( ! this.neurons.contains( source ) )
			throw new IllegalArgumentException("source is not a member of this graph");
		if( ! this.neurons.contains( destination ) )
			throw new IllegalArgumentException("destination is not a member of this graph");

		return this.add(new SimpleSynapse(source, destination, initialWeight));
	}

	protected boolean disconnect(Neuron source, Neuron destination)
	{
		if( source == null )
			throw new IllegalArgumentException("source can not be null");
		if( destination == null )
			throw new IllegalArgumentException("destination can not be null");
		if( ! this.neurons.contains( source ) )
			throw new IllegalArgumentException("source is not a member of this graph");
		if( ! this.neurons.contains( destination ) )
			throw new IllegalArgumentException("destination is not a member of this graph");

		return this.remove(new SimpleSynapse(source, destination, 0.0));
	}

	protected boolean remove(Synapse removeSynapse)
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
	 * 
	 * @param removeNeuron The neuron to remove.
	 * @since 1.0
	 */
    protected boolean remove(Neuron removeNeuron)
    {
		if( removeNeuron == null )
			throw new IllegalArgumentException("node can not be null");

		if( this.neurons.remove(removeNeuron) )
		{
			Set<Synapse> removeEdges = new HashSet<Synapse>();
			if( this.outMap.containsKey(removeNeuron) )
				removeEdges.addAll(this.outMap.remove(removeNeuron));
			if( this.inMap.containsKey(removeNeuron) )
				removeEdges.addAll(this.inMap.remove(removeNeuron));
			this.synapses.removeAll(removeEdges);

			if (removeNeuron instanceof OutputNeuron)
				this.outputNeurons.remove((OutputNeuron) removeNeuron);
			if (removeNeuron instanceof InputNeuron)
				this.inputNeurons.remove((InputNeuron) removeNeuron);

			return true;
		}
		return false;
    }



	/**
	 * Obtains all InputNeurons contained within the brain.
	 *
	 * 
	 * @return An unmodifiable Set of InputNeurons.
	 * @since 1.0
	 */
    public Set<InputNeuron> getInputNeurons()
    {
        return Collections.unmodifiableSet(this.inputNeurons);
    }



	/**
	 * Obtains all OutputNeurons contained within the brain.
	 *
	 * 
	 * @return An unmodifiable Set of OutputNeurons
	 * @since 1.0
	 */
    public Set<OutputNeuron> getOutputNeurons()
    {
        return Collections.unmodifiableSet(this.outputNeurons);
    }



	/**
	 * Obtains all Neurons, including InputNeurons and OutputNeurons contained
	 * within the brain.
	 *
	 * 
	 * @return An unmodifiable Set of all Neurons.
	 * @since 1.0
	 */
    public Set<Neuron> getNodes()
    {
        return Collections.unmodifiableSet(this.neurons);
    }

	@Override
	public Set<Synapse> getEdges()
	{
		return Collections.unmodifiableSet(this.synapses);
	}

	public Set<Synapse> getAdjacentEdges(Neuron node)
	{
		Set<Synapse> nodeSynapses = new HashSet<Synapse>();
		if( this.outMap.containsKey(node) )
			nodeSynapses.addAll( this.outMap.get(node) );
		if( this.inMap.containsKey(node) )
			nodeSynapses.addAll( this.inMap.get(node) );
		return Collections.unmodifiableSet(nodeSynapses);
	}

	@Override
	public Set<Synapse> getTraversableEdges(Neuron node)
	{
		if( this.inMap.containsKey(node) )
			 return Collections.unmodifiableSet( this.outMap.get(node) );
		return Collections.emptySet();
	}

	public Set<Synapse> getInEdges(Neuron node)
	{
		if( this.inMap.containsKey(node) )
			 return Collections.unmodifiableSet( this.inMap.get(node) );
		return Collections.emptySet();
	}

	public int getIndegree(Neuron node)
	{
		return this.inMap.get(node).size();
	}

	public int getOutdegree(Neuron node)
	{
		return this.outMap.get(node).size();
	}


	@Override
	public boolean isStronglyConnected(Neuron leftNode, Neuron rightNode)
	{
		Set<Synapse> outSet = this.getTraversableEdges(leftNode);
		Set<Synapse> inSet = this.getInEdges(rightNode);
		Set<Synapse> jointSet = new HashSet<Synapse>(outSet);
		jointSet.retainAll(inSet);

		return ( !jointSet.isEmpty() );
	}

	public List<Neuron> getAdjacentNodes(Neuron node)
	{
		Set<Synapse> nodeSynapses = this.getAdjacentEdges(node);
		List<Neuron> neighbors = new ArrayList<Neuron>();
		for(Synapse nodeSynapse : nodeSynapses)
			neighbors.add( (nodeSynapse.getLeftNode().equals(node) ? nodeSynapse.getRightNode() : nodeSynapse.getLeftNode() ) );
		return Collections.unmodifiableList(neighbors);
	}

	/**
	 * threadExecutor used to execute processes.
	 *
	 * @return the threadExecutor used to execute processes.
	 * @since 2.0
	 */
	protected ThreadPoolExecutor getThreadExecutor()
	{
		return threadExecutor;
	}
}
