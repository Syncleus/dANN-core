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
package com.syncleus.dann.graph.pathfinding.dijkstra;

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.SimpleWalk;
import com.syncleus.dann.graph.Weighted;
import com.syncleus.dann.graph.WeightedWalk;
import com.syncleus.dann.graph.pathfinding.PathFinder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraPathFinder<G extends Graph<N, ? extends Edge<N>, ?>, N> implements PathFinder<N,WeightedWalk>
{
	private final class DijkstraWeightedWalk extends SimpleWalk<N,Edge<N>> implements WeightedWalk<N,Edge<N>>
	{
		private final double totalWeight;

		public DijkstraWeightedWalk(N firstNode, N lastNode, List<Edge<N>> edges, List<PathedStep> steps)
		{
			super(firstNode,lastNode,edges);

			double newTotalWeight = 0.0;
			for(PathedStep step : steps)
				newTotalWeight += step.getCachedPathWeight();
			this.totalWeight = newTotalWeight;
		}

		public double getWeight()
		{
			return this.totalWeight;
		}
	}

	private final class PathedStep implements Comparable<PathedStep>
	{
		private N node;
		private PathedStep parent;
		private Edge<N> parentEdge;
		private double cachedPathWeight;

		public PathedStep(N node)
		{
			if( node == null )
				throw new IllegalArgumentException("node can not be null");

			this.node = node;
		}

		public boolean updateParent(PathedStep newParent, Edge<N> newParentEdge)
		{
			if( newParent == null )
				throw new IllegalArgumentException("newParent can not be null");
			if( newParent.equals(this))
				throw new IllegalArgumentException("newParent can not be this node");
			if( newParentEdge == null)
				throw new IllegalArgumentException("newParentEdge can not be null");
			if( ! newParentEdge.getNodes().contains(newParent.getNode()))
				throw new IllegalArgumentException("newParentEdge must connect to new Parent");

			boolean parentHasEdge = false;
			for( Edge<N> edge : graph.getTraversableEdges(this.node) )
				if(edge.getNodes().contains(newParent.getNode()))
				{
					parentHasEdge = true;
					break;
				}

			if(!parentHasEdge)
				throw new IllegalArgumentException("newParent is not connected to this node");

			final double edgeWeight = (newParentEdge instanceof Weighted ? ((Weighted)newParentEdge).getWeight() : 1.0);
			if((this.parent == null) || ( newParent.getCachedPathWeight() + edgeWeight < this.getCachedPathWeight()))
			{
				this.parent = newParent;
				this.parentEdge = newParentEdge;
				this.cachedPathWeight = newParent.getCachedPathWeight() + edgeWeight;
				return true;
			}
			else
				return false;
		}

		public N getNode()
		{
			return node;
		}

		public PathedStep getParent()
		{
			return parent;
		}

		public double getCachedPathWeight()
		{
			return cachedPathWeight;
		}

		@Override
		public boolean equals(Object compareToObj)
		{
			if(!(compareToObj.getClass().isInstance(this)))
				return false;

			PathedStep compareTo = (PathedStep) compareToObj;
			return ((this.node.equals(compareTo))||(this.node.equals(compareTo.node)));
		}

		@Override
		public int hashCode()
		{
			return this.node.hashCode();
		}

		public int compareTo(PathedStep compareWith)
		{
			//the natural ordering is inverse cause the smallest path weight is
			//the best weight.
			if(this.getCachedPathWeight() < compareWith.getCachedPathWeight())
				return 1;
			else if(this.getCachedPathWeight() > compareWith.getCachedPathWeight())
				return -1;
			else
				return 0;
		}

		public Edge<N> getParentEdge()
		{
			return parentEdge;
		}
	}

	private G graph;

	public DijkstraPathFinder(G graph)
	{
		this.graph = graph;
	}

	public WeightedWalk getBestPath(N begin, N end)
	{
		if(begin == null)
			throw new IllegalArgumentException("begin can not be null");
		if(end == null)
			throw new IllegalArgumentException("end can not be null");
		if(begin.equals(end))
			throw new IllegalArgumentException("begin can not be equal to end");

		//initalize candidate nodes queue containing potential edges as a
		//solution
		Map<N, PathedStep> nodeStepMapping = new HashMap<N, PathedStep>();
		PriorityQueue<PathedStep> candidateSteps = new PriorityQueue<PathedStep>();
		PathedStep beginStep = new PathedStep(begin);
		nodeStepMapping.put(begin, beginStep);
		candidateSteps.add(beginStep);

		//all nodes that have been closed cannolonger be traversed
		Set<PathedStep> closedSteps = new HashSet<PathedStep>();

		//lets iterate through each step from the begining
		while(!candidateSteps.isEmpty())
		{
			PathedStep currentStep = candidateSteps.poll();
			if( currentStep.getNode().equals(end) )
				return pathedStepToWalk(currentStep);

			for(Edge<N> edge : this.graph.getTraversableEdges(currentStep.node))
			{
				for(N neighborNode : edge.getNodes())
				{
					if(neighborNode.equals(currentStep.node))
						continue;

					PathedStep neighborStep;
					if(nodeStepMapping.containsKey(neighborNode))
						neighborStep = nodeStepMapping.get(neighborNode);
					else
					{
						neighborStep = new PathedStep(neighborNode);
						nodeStepMapping.put(neighborNode, neighborStep);
					}

					if(!closedSteps.contains(neighborStep))
						candidateSteps.add(neighborStep);

					if(!neighborNode.equals(begin))
						neighborStep.updateParent(currentStep, edge);
				}
			}

			closedSteps.add(currentStep);
		}

		return null;
	}

	public boolean isReachable(N begin, N end)
	{
		if( this.getBestPath(begin, end) != null )
			return true;
		return false;
	}

	public boolean isConnected(N begin, N end)
	{
		if(this.getBestPath(begin, end) != null)
			return true;
		return false;
	}

	private WeightedWalk pathedStepToWalk(PathedStep endPathedStep)
	{
		List<Edge<N>> edges = new ArrayList<Edge<N>>();
		List<PathedStep> steps = new ArrayList<PathedStep>();
		N lastNode = endPathedStep.getNode();
		N firstNode = endPathedStep.getNode();

		PathedStep currentStep = endPathedStep;
		while(currentStep != null)
		{
			firstNode = currentStep.getNode();
			edges.add(0, currentStep.getParentEdge());
			steps.add(currentStep);
			currentStep = currentStep.getParent();
		}

		return new DijkstraWeightedWalk(firstNode, lastNode, edges, steps);
	}
}
