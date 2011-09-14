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
package com.syncleus.dann.graphicalmodel;

import java.util.*;
import com.syncleus.dann.graph.*;
import com.syncleus.dann.graphicalmodel.xml.GraphicalModelElementXml;
import com.syncleus.dann.graphicalmodel.xml.GraphicalModelXml;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;

public abstract class AbstractGraphicalModelAdjacencyGraph<N extends GraphicalModelNode, E extends BidirectedEdge<N>> extends AbstractBidirectedAdjacencyGraph<N, E> implements GraphicalModel<N, E>
{
	protected AbstractGraphicalModelAdjacencyGraph()
	{
		super();
	}

	protected AbstractGraphicalModelAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getTargets(), copyGraph.getEdges());
	}

	protected AbstractGraphicalModelAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	@Override
	public void learnStates()
	{
		for(final N node : this.getTargets())
			node.learnState();
	}

	@Override
	public double jointProbability()
	{
		// TODO implement this!
		throw new UnsupportedOperationException();
	}

	@Override
	public double conditionalProbability(final Set<N> goals, final Set<N> influences)
	{
		//first we need to preserve a map of all the starting states so we can reset the network back to its starting
		//point when we are done
		Map<N, Object> startingStates = new HashMap<N, Object>();
		for(N node : this.getTargets())
		{
			//we wont be changing influences nodes, so we can skip those
			if( !influences.contains(node) )
				startingStates.put(node, node.getState());
		}

		try
		{
			List<N> varyingNodes = new ArrayList<N>(this.getTargets());

			//calculate numerator
			varyingNodes.removeAll(influences);
			varyingNodes.removeAll(goals);
			resetNodeStates(varyingNodes);
			double numerator = 0.0;
			do
			{
				numerator += this.jointProbability();
			}
			while( !incrementNodeStates(varyingNodes) );

			//calculate denominator
			varyingNodes = new ArrayList<N>(this.getTargets());
			varyingNodes.removeAll(influences);
			resetNodeStates(varyingNodes);
			double denominator = 0.0;
			do
			{
				denominator += this.jointProbability();
			}
			while( !incrementNodeStates(varyingNodes) );

			//all done
			return numerator / denominator;
		}
		finally
		{
			//restore the initial states when we are done
			for(Map.Entry<N,Object> nodeState : startingStates.entrySet())
				nodeState.getKey().setState(nodeState.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	protected static <N extends GraphicalModelNode> void resetNodeStates(final Collection<N> incNodes)
	{
		for(final N incNode : incNodes)
			incNode.setState((incNode.getLearnedStates().toArray())[0]);
	}

	protected static <N extends GraphicalModelNode> boolean incrementNodeStates(final Collection<N> incNodes)
	{
		for(final N incNode : incNodes)
			if( !incrementNodeState(incNode) )
				return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	protected static <N extends GraphicalModelNode> boolean incrementNodeState(final N incNode)
	{
		final List stateTypes = Arrays.asList(incNode.getLearnedStates().toArray());
		final int currentStateIndex = stateTypes.indexOf(incNode.getState());
		if( (currentStateIndex + 1) >= stateTypes.size() )
		{
			incNode.setState(stateTypes.get(0));
			return true;
		}
		else
		{
			incNode.setState(stateTypes.get(currentStateIndex + 1));
			return false;
		}
	}

	@Override
	public AbstractGraphicalModelAdjacencyGraph<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractGraphicalModelAdjacencyGraph<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractGraphicalModelAdjacencyGraph<N, E> cloneAdd(final N newNode)
	{
		return (AbstractGraphicalModelAdjacencyGraph<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractGraphicalModelAdjacencyGraph<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractGraphicalModelAdjacencyGraph<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractGraphicalModelAdjacencyGraph<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractGraphicalModelAdjacencyGraph<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractGraphicalModelAdjacencyGraph<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractGraphicalModelAdjacencyGraph<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractGraphicalModelAdjacencyGraph<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractGraphicalModelAdjacencyGraph<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractGraphicalModelAdjacencyGraph<N, E> clone()
	{
		return (AbstractGraphicalModelAdjacencyGraph<N, E>) super.clone();
	}

	@Override
	public GraphicalModelXml toXml()
	{
		final GraphicalModelElementXml networkXml = new GraphicalModelElementXml();
		final Namer<Object> namer = new Namer<Object>();

		networkXml.setNodeInstances(new GraphicalModelElementXml.NodeInstances());
		networkXml.setStateInstances(new GraphicalModelElementXml.StateInstances());
		final Set<Object> writtenStates = new HashSet<Object>();
		for (N node : this.getTargets())
		{
			//add the node
			final NamedValueXml nodeXml = new NamedValueXml();
			nodeXml.setName(namer.getNameOrCreate(node));
			nodeXml.setValue(node.toXml(namer));
			networkXml.getNodeInstances().getNodes().add(nodeXml);

			//add all the node's learned states
			for (Object learnedState : node.getLearnedStates())
			{
				//only add the learnedState if it hasnt yet been added
				if (writtenStates.add(learnedState))
				{
					final NamedValueXml stateXml = new NamedValueXml();
					stateXml.setName(namer.getNameOrCreate(learnedState));
					if (learnedState instanceof XmlSerializable)
					{
						stateXml.setValue(((XmlSerializable) learnedState).toXml(namer));
					}
					else
					{
						stateXml.setValue(learnedState);
					}
					networkXml.getStateInstances().getStates().add(stateXml);
				}
			}

			//add the nodes current state if it wasnt already
			final Object state = node.getState();
			if (writtenStates.add(state))
			{
				final NamedValueXml stateXml = new NamedValueXml();
				stateXml.setName(namer.getNameOrCreate(state));
				if (state instanceof XmlSerializable)
				{
					stateXml.setValue(((XmlSerializable) state).toXml(namer));
				}
				else
				{
					stateXml.setValue(state);
				}
				networkXml.getStateInstances().getStates().add(stateXml);
			}
		}

		this.toXml(networkXml, namer);
		return networkXml;
	}

	@Override
	public GraphicalModelXml toXml(final Namer<Object> namer)
	{
		if (namer == null)
		{
			throw new IllegalArgumentException("namer can not be null");
		}

		final GraphicalModelXml xml = new GraphicalModelXml();
		this.toXml(xml, namer);
		return xml;
	}

	protected static class NodeConnectivity<N extends GraphicalModelNode, E extends BidirectedEdge<N>> extends HashMap<N, Set<E>>
	{
		private static final long serialVersionUID = -3068604309573134643L;

		public Set<E> get(final N keyNode)
		{

			Set<E> edges = super.get(keyNode);
			if( edges == null )
			{
				edges = new HashSet<E>();
				super.put(keyNode, edges);
			}
			return edges;
		}
	}
}