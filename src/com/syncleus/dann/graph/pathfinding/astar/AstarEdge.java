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

import com.syncleus.dann.graph.AbstractDirectedEdge;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.DirectedNodePair;
import com.syncleus.dann.graph.WeightedEdge;
import com.syncleus.dann.graph.WeightedNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AstarEdge<L> extends AbstractDirectedEdge implements WeightedEdge, DirectedEdge
{
	private final double weight;
	private final DirectedNodePair<AstarNode<L>> pair;
	private final List<WeightedNode> weightedNodes;

	public AstarEdge(AstarNode<L> sourceNode, AstarNode<L> destinationNode, double weight)
	{
		super(sourceNode, destinationNode);

		this.weight = weight;
		this.pair = new DirectedNodePair<AstarNode<L>>(sourceNode, destinationNode);
		this.weightedNodes = Collections.unmodifiableList(new ArrayList<WeightedNode>(this.pair.getNodes()));
	}

	public 	List<WeightedNode> getWeightedNodes()
	{
		return this.weightedNodes;
	}

	public final DirectedNodePair<AstarNode<L>> getAstarNodePair()
	{
		return this.pair;
	}

	public double getAstarWeight()
	{
		return weight;
	}

	public Number getWeight()
	{
		return Double.valueOf(weight);
	}

	@Override
	public int hashCode()
	{
		return this.getAstarNodePair().getSourceNode().hashCode() + (this.getAstarNodePair().getDestinationNode().hashCode() * this.getAstarNodePair().getSourceNode().hashCode()) + this.getWeight().hashCode();
	}

	@Override
	public boolean equals(Object compareObject)
	{
		if(!(compareObject instanceof AstarEdge))
			return false;

		AstarEdge compareEdge = (AstarEdge) compareObject;

		if( (this.getAstarNodePair().getSourceNode().equals(compareEdge.getAstarNodePair().getSourceNode())) && (this.getAstarNodePair().getDestinationNode().equals(compareEdge.getAstarNodePair().getDestinationNode())) && (this.weight == compareEdge.weight) )
			return true;
		
		return false;
	}
}
