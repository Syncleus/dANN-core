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

import com.syncleus.dann.graph.AbstractBidirectedGraph;
import com.syncleus.dann.graph.BidirectedWalk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBayesianNetwork extends AbstractBidirectedGraph<BayesianNode, BayesianEdge, BidirectedWalk<BayesianNode,BayesianEdge>> implements BayesianNetwork
{
	protected static class NodeConnectivity extends HashMap<BayesianNode,Set<BayesianEdge>>
	{
		@Override
		public Set<BayesianEdge> get(Object keyObject)
		{
			Set<BayesianEdge> edges = super.get(keyObject);
			if(keyObject instanceof BayesianNode)
			{
				if(edges == null)
				{
					edges = new HashSet<BayesianEdge>();
					BayesianNode key = (BayesianNode) keyObject;
					super.put(key, edges);
				}
			}
			return edges;
		}
	}

	private final Set<BayesianNode> nodes = new HashSet<BayesianNode>();
	private final Set<BayesianEdge> edges = new HashSet<BayesianEdge>();
	private final Map<BayesianNode,Set<BayesianEdge>> outMap = new NodeConnectivity();
	private final Map<BayesianNode,Set<BayesianEdge>> inMap = new NodeConnectivity();

	protected boolean connect(BayesianNode source, BayesianNode destination)
	{
		if( source == null )
			throw new IllegalArgumentException("source can not be null");
		if( destination == null )
			throw new IllegalArgumentException("destination can not be null");
		if( ! this.nodes.contains( source ) )
			throw new IllegalArgumentException("source is not a member of this graph");
		if( ! this.nodes.contains( destination ) )
			throw new IllegalArgumentException("destination is not a member of this graph");

		return this.add(new SimpleBayesianEdge(source, destination));
	}

	protected boolean disconnect(BayesianNode source, BayesianNode destination)
	{
		if( source == null )
			throw new IllegalArgumentException("source can not be null");
		if( destination == null )
			throw new IllegalArgumentException("destination can not be null");
		if( ! this.nodes.contains( source ) )
			throw new IllegalArgumentException("source is not a member of this graph");
		if( ! this.nodes.contains( destination ) )
			throw new IllegalArgumentException("destination is not a member of this graph");

		return this.remove(new SimpleBayesianEdge(source, destination));
	}

	protected boolean add(BayesianNode node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");

		return this.nodes.add(node);
	}

	protected boolean add(BayesianEdge edge)
	{
		if( edge == null )
			throw new IllegalArgumentException("edge can not be null");
		if( ! this.nodes.containsAll(edge.getNodes()) )
			throw new IllegalArgumentException("edge has a node as an end point that is not part of the graph");

		if( this.edges.add(edge) )
		{
			this.outMap.get(edge.getSourceNode()).add(edge);
			this.inMap.get(edge.getDestinationNode()).add(edge);
			return true;
		}

		return false;
	}

	protected boolean remove(BayesianNode node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");

		if( this.nodes.remove(node) )
		{
			Set<BayesianEdge> removeEdges = new HashSet<BayesianEdge>();
			if( this.outMap.containsKey(node) )
				removeEdges.addAll(this.outMap.remove(node));
			if( this.inMap.containsKey(node) )
				removeEdges.addAll(this.inMap.remove(node));
			this.edges.removeAll(removeEdges);
			return true;
		}
		return false;
	}

	protected boolean remove(BayesianEdge edge)
	{
		if( edge == null )
			throw new IllegalArgumentException("edge can not be null");

		if( this.edges.remove(edge) )
		{
			if( this.outMap.containsKey(edge.getSourceNode()) )
				this.outMap.get(edge.getSourceNode()).remove(edge);
			if( this.inMap.containsKey(edge.getDestinationNode()) )
				this.inMap.get(edge.getDestinationNode()).remove(edge);
			return true;
		}
		return false;
	}

	public Set<BayesianNode> getNodes()
	{
		return Collections.unmodifiableSet(this.nodes);
	}

	@Override
	public List<BayesianEdge> getEdges()
	{
		return Collections.unmodifiableList(new ArrayList<BayesianEdge>(this.edges));
	}

	public List<BayesianEdge> getEdges(BayesianNode node)
	{
		List<BayesianEdge> nodeEdges = new ArrayList<BayesianEdge>();
		if( this.outMap.containsKey(node) )
			nodeEdges.addAll( this.outMap.get(node) );
		if( this.inMap.containsKey(node) )
			nodeEdges.addAll( this.inMap.get(node) );
		return Collections.unmodifiableList(nodeEdges);
	}

	public List<BayesianEdge> getTraversableEdges(BayesianNode node)
	{
		return this.getOutEdges(node);
	}

	public List<BayesianEdge> getOutEdges(BayesianNode node)
	{
		if( this.outMap.containsKey(node) )
			 return Collections.unmodifiableList( new ArrayList<BayesianEdge>(this.outMap.get(node)) );
		return Collections.emptyList();
	}

	public List<BayesianEdge> getInEdges(BayesianNode node)
	{
		if( this.inMap.containsKey(node) )
			 return Collections.unmodifiableList( new ArrayList<BayesianEdge>(this.inMap.get(node)) );
		return Collections.emptyList();
	}


	public boolean isConnected(BayesianNode leftNode, BayesianNode rightNode)
	{
		if( this.edges.contains(new SimpleBayesianEdge(leftNode, rightNode)) )
			return true;
		else if( this.edges.contains(new SimpleBayesianEdge(rightNode, leftNode)) )
			return true;
		return false;
	}

	public List<BayesianNode> getNeighbors(BayesianNode node)
	{
		List<BayesianEdge> nodeEdges = this.getEdges(node);
		List<BayesianNode> neighbors = new ArrayList<BayesianNode>();
		for(BayesianEdge nodeEdge : nodeEdges)
			neighbors.add( (nodeEdge.getLeftNode().equals(node) ? nodeEdge.getRightNode() : nodeEdge.getLeftNode() ) );
		return Collections.unmodifiableList(neighbors);
	}

	public List<BayesianNode> getTraversableNeighbors(BayesianNode node)
	{
		List<BayesianEdge> nodeEdges = this.getOutEdges(node);
		List<BayesianNode> neighbors = new ArrayList<BayesianNode>();
		for(BayesianEdge nodeEdge : nodeEdges)
			neighbors.add( (nodeEdge.getLeftNode().equals(node) ? nodeEdge.getRightNode() : nodeEdge.getLeftNode() ) );
		return Collections.unmodifiableList(neighbors);
	}

	public void learnStates()
	{
		for(BayesianNode node : this.nodes)
			node.learnState();
	}

	public double jointProbability()
	{
		double probabilityProduct = 1.0;
		for(BayesianNode node : this.nodes)
			probabilityProduct *= node.stateProbability();
		return probabilityProduct;
	}

	public double conditionalProbability(Set<BayesianNode> goals, Set<BayesianNode> influences)
	{
		List<BayesianNode> varyingNodes = new ArrayList<BayesianNode>(this.nodes);

		//calculate numerator
		varyingNodes = new ArrayList<BayesianNode>(this.nodes);
		varyingNodes.removeAll(influences);
		varyingNodes.removeAll(goals);
		resetNodeStates(varyingNodes);
		double numerator = 0.0;
		do
		{
			numerator += this.jointProbability();
		} while(!incrementNodeStates(varyingNodes));

		//calculate denominator
		varyingNodes = new ArrayList<BayesianNode>(this.nodes);
		varyingNodes.removeAll(influences);
		resetNodeStates(varyingNodes);
		double denominator = 0.0;
		do
		{
			denominator += this.jointProbability();
		} while(!incrementNodeStates(varyingNodes));

		//all done
		return numerator / denominator;
	}

	@SuppressWarnings("unchecked")
	private static void resetNodeStates(List<BayesianNode> incNodes)
	{
		for(BayesianNode incNode : incNodes)
			incNode.setState((incNode.getLearnedStates().toArray())[0]);
	}

	private static boolean incrementNodeStates(List<BayesianNode> incNodes)
	{
		for(BayesianNode incNode : incNodes)
			if( !incrementNodeState(incNode) )
				return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	private static boolean incrementNodeState(BayesianNode incNode)
	{
		List stateTypes = Arrays.asList(incNode.getLearnedStates().toArray());
		int currentStateIndex = stateTypes.indexOf(incNode.getState());
		if((currentStateIndex+1) >= stateTypes.size())
		{
			incNode.setState(stateTypes.get(0));
			return true;
		}
		else
		{
			incNode.setState(stateTypes.get(currentStateIndex+1));
			return false;
		}
	}
}
