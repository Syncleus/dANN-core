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
package com.syncleus.dann.graph.pathfinding.astar;

import com.syncleus.dann.graph.AbstractDirectedNode;
import com.syncleus.dann.graph.DirectedNode;
import com.syncleus.dann.graph.Node;
import com.syncleus.dann.graph.WeightedNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AstarNode<L> extends AbstractDirectedNode<AstarEdge<L>> implements WeightedNode<AstarEdge<L>>, DirectedNode<AstarEdge<L>>
{
	private L label;
	private double weight;
	private List<AstarEdge<L>> edges;

	public AstarNode(L label, double weight)
	{
		this.label = label;
		this.weight = weight;
		this.edges = new ArrayList<AstarEdge<L>>();
	}

	@Override
	public List<AstarEdge<L>> getTraversableEdges()
	{
		List<AstarEdge<L>> traversableEdges = new ArrayList<AstarEdge<L>>();
		for(AstarEdge<L> edge : this.edges)
			if(edge.getAstarNodePair().getDestinationNode() != this)
				traversableEdges.add(edge);
		return Collections.unmodifiableList(traversableEdges);
	}

	public void disconnect(AstarEdge<L> edge)
	{
		edge.getAstarNodePair().getSourceNode().disconnectFrom(edge);
		edge.getAstarNodePair().getDestinationNode().disconnectFrom(edge);
	}

	private void disconnectFrom(AstarEdge<L> edge)
	{
		this.edges.remove(edge);
	}

	public AstarEdge<L> connectTo(AstarNode<L> destination, double weight)
	{
		AstarEdge<L> newEdge = new AstarEdge<L>(this, destination, weight);
		destination.connectFrom(newEdge);
		this.edges.add(newEdge);
		return newEdge;
	}

	private void connectFrom(AstarEdge<L> source)
	{
		this.edges.add(source);
	}

	public List<AstarEdge<L>> getEdges()
	{
		return Collections.unmodifiableList(edges);
	}

	public double getAstarWeight()
	{
		return this.weight;
	}

	public Number getWeight()
	{
		return Double.valueOf(weight);
	}

	public L getLabel()
	{
		return this.label;
	}

	@Override
	public int hashCode()
	{
		return this.label.hashCode();
	}

	@Override
	public boolean equals(Object compareObject)
	{
		if(compareObject instanceof AstarNode)
			return this.label.equals(((AstarNode)compareObject).label);
		else if((compareObject instanceof Node)&&(this.label instanceof Node))
			return this.label.equals(compareObject);

		return false;
	}
}
