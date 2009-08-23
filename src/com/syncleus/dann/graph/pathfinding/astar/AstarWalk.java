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

import com.syncleus.dann.graph.AbstractBidirectedWalk;
import com.syncleus.dann.graph.DirectedWalk;
import com.syncleus.dann.graph.WeightedWalk;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AstarWalk<L> extends AbstractBidirectedWalk<AstarNode<L>,AstarEdge<L>> implements WeightedWalk<AstarNode<L>,AstarEdge<L>>, DirectedWalk<AstarNode<L>,AstarEdge<L>>
{
	private List<AstarEdge<L>> steps;
	private AstarNode<L> firstNode;
	private AstarNode<L> lastNode;

	public AstarWalk(AstarNode<L> firstNode, AstarNode<L> lastNode, List<AstarEdge<L>> steps)
	{
		this.steps = new ArrayList<AstarEdge<L>>(steps);
		this.firstNode = firstNode;
		this.lastNode = lastNode;
	}

	public List<AstarEdge<L>> getSteps()
	{
		return Collections.unmodifiableList(steps);
	}

	public AstarNode<L> getFirstNode()
	{
		return this.firstNode;
	}

	public AstarNode<L> getLastNode()
	{
		return this.lastNode;
	}

	public double getAstarWeight()
	{
		double weight = this.getFirstNode().getAstarWeight();
		AstarNode<L> lastNode = this.getFirstNode();
		List<AstarEdge<L>> steps = this.getSteps();
		for(AstarEdge<L> step : steps)
		{
			weight += step.getAstarWeight();
			if(step.getAstarNodePair().getSourceNode().equals(lastNode))
			{
				weight += step.getAstarNodePair().getDestinationNode().getAstarWeight();
				lastNode = step.getAstarNodePair().getDestinationNode();
			}
			else
			{
				weight += step.getAstarNodePair().getSourceNode().getAstarWeight();
				lastNode = step.getAstarNodePair().getSourceNode();
			}

		}
		return weight;
	}

	public Number getWeight()
	{
		return Double.valueOf(this.getAstarWeight());
	}
}
