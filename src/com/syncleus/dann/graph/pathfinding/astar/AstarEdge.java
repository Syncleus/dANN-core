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
import com.syncleus.dann.graph.NodePair;
import com.syncleus.dann.graph.WeightedEdge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AstarEdge extends AbstractDirectedEdge implements WeightedEdge, DirectedEdge
{
	private final double weight;
	private final AstarNode source;
	private final AstarNode destination;
	private final List<AstarNode> nodes;
	private final NodePair<AstarNode> pair;

	public AstarEdge(AstarNode sourceNode, AstarNode destinationNode, double weight)
	{
		this.source = sourceNode;
		this.destination = destinationNode;
		this.weight = weight;

		List<AstarNode> newNodes = new ArrayList<AstarNode>();
		newNodes.add(source);
		newNodes.add(destination);
		this.nodes = Collections.unmodifiableList(newNodes);

		this.pair = new NodePair<AstarNode>(source, destination);
	}

	public NodePair<AstarNode> getNodePair()
	{
		return this.pair;
	}

	public EndState getRightEndState()
	{
		return EndState.Outward;
	}

	public EndState getLeftEndState()
	{
		return EndState.Inward;
	}

	public List<AstarNode> getNodes()
	{
		return this.nodes;
	}

	public AstarNode getSourceNode()
	{
		return this.source;
	}

	public AstarNode getDestinationNode()
	{
		return this.destination;
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
		return this.getSourceNode().hashCode() + (this.getDestinationNode().hashCode() * this.getSourceNode().hashCode()) + this.getWeight().hashCode();
	}

	@Override
	public boolean equals(Object compareObject)
	{
		if(!(compareObject instanceof AstarEdge))
			return false;

		AstarEdge compareEdge = (AstarEdge) compareObject;

		if( (this.getSourceNode().equals(compareEdge.getSourceNode())) && (this.getDestinationNode().equals(compareEdge.getDestinationNode())) && (this.weight == compareEdge.weight) )
			return true;
		
		return false;
	}
}
