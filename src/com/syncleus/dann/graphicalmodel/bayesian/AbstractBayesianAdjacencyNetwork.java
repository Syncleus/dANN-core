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
package com.syncleus.dann.graphicalmodel.bayesian;

import com.syncleus.dann.graph.*;
import java.util.*;

public abstract class AbstractBayesianAdjacencyNetwork<N extends BayesianNode, E extends BayesianEdge<N>> extends AbstractBidirectedAdjacencyGraph<N, E> implements BayesianNetwork<N, E>
{
	protected AbstractBayesianAdjacencyNetwork()
	{
		super();
	}

	protected AbstractBayesianAdjacencyNetwork(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractBayesianAdjacencyNetwork(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public void learnStates()
	{
		for(final N node:this.getNodes())
			node.learnState();
	}

	public double jointProbability()
	{
		double probabilityProduct = 1.0;
		for(final N node:this.getNodes())
			probabilityProduct *= node.stateProbability();
		return probabilityProduct;
	}

	public double conditionalProbability(final Set<N> goals, final Set<N> influences)
	{
		List<N> varyingNodes = new ArrayList<N>(this.getNodes());

		//calculate numerator
		varyingNodes = new ArrayList<N>(this.getNodes());
		varyingNodes.removeAll(influences);
		varyingNodes.removeAll(goals);
		resetNodeStates(varyingNodes);
		double numerator = 0.0;
		do
			numerator += this.jointProbability();
		while(!incrementNodeStates(varyingNodes));

		//calculate denominator
		varyingNodes = new ArrayList<N>(this.getNodes());
		varyingNodes.removeAll(influences);
		resetNodeStates(varyingNodes);
		double denominator = 0.0;
		do
			denominator += this.jointProbability();
		while(!incrementNodeStates(varyingNodes));

		//all done
		return numerator / denominator;
	}

	@SuppressWarnings("unchecked")
	private static <N extends BayesianNode> void resetNodeStates(final List<N> incNodes)
	{
		for(final N incNode:incNodes)
			incNode.setState((incNode.getLearnedStates().toArray())[0]);
	}

	private static <N extends BayesianNode> boolean incrementNodeStates(final List<N> incNodes)
	{
		for(final N incNode:incNodes)
			if(!incrementNodeState(incNode))
				return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	private static <N extends BayesianNode> boolean incrementNodeState(final N incNode)
	{
		final List stateTypes = Arrays.asList(incNode.getLearnedStates().toArray());
		final int currentStateIndex = stateTypes.indexOf(incNode.getState());
		if((currentStateIndex + 1) >= stateTypes.size())
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
	public AbstractBayesianAdjacencyNetwork<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>)super.cloneAdd(newEdge);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneAdd(final N newNode)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>)super.cloneAdd(newNode);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>)super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>)super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>)super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>)super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractBayesianAdjacencyNetwork<N, E> clone()
	{
		return (AbstractBayesianAdjacencyNetwork<N, E>)super.clone();
	}

	protected static class NodeConnectivity<N extends BayesianNode, E extends BayesianEdge<N>>  extends HashMap<N, Set<E>>
	{
		private static final long serialVersionUID = -3068604309573134643L;

		public Set<E> get(final N keyNode)
		{

			Set<E> edges = super.get(keyNode);
			if(edges == null)
			{
				edges = new HashSet<E>();
				super.put(keyNode, edges);
			}
			return edges;
		}
	}
}
