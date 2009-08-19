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

public class AstarWalk extends AbstractBidirectedWalk implements WeightedWalk, DirectedWalk
{
	private List<AstarEdge> steps;
	private AstarNode firstNode;
	private AstarNode lastNode;

	public AstarWalk(AstarNode firstNode, AstarNode lastNode, List<AstarEdge> steps)
	{
		this.steps = new ArrayList<AstarEdge>(steps);
		this.firstNode = firstNode;
		this.lastNode = lastNode;
	}

	public List<AstarEdge> getSteps()
	{
		return Collections.unmodifiableList(steps);
	}

	public AstarNode getFirstNode()
	{
		return this.firstNode;
	}

	public AstarNode getLastNode()
	{
		return this.lastNode;
	}

	public double getAstarWeight()
	{
		double weight = this.getFirstNode().getAstarWeight();
		AstarNode lastNode = this.getFirstNode();
		List<AstarEdge> steps = this.getSteps();
		for(AstarEdge step : steps)
		{
			weight += step.getAstarWeight();
			if(step.getSourceNode().equals(lastNode))
			{
				weight += step.getDestinationNode().getAstarWeight();
				lastNode = step.getDestinationNode();
			}
			else
			{
				weight += step.getSourceNode().getAstarWeight();
				lastNode = step.getSourceNode();
			}

		}
		return weight;
	}

	public Number getWeight()
	{
		return Double.valueOf(this.getAstarWeight());
	}
}
