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
package com.syncleus.dann.graph.search.pathfinding;

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.SimpleWalk;
import com.syncleus.dann.graph.Weighted;
import com.syncleus.dann.graph.WeightedWalk;
import com.syncleus.dann.graph.search.pathfinding.HeuristicPathCost;
import com.syncleus.dann.graph.search.pathfinding.PathFinder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AstarPathFinder<G extends Graph<N, E, ?>, N, E extends Edge<N>> implements PathFinder<N,E,WeightedWalk<N,E>>
{
	private final class AstarWeightedWalk extends SimpleWalk<N,E> implements WeightedWalk<N,E>
	{
		private final double totalWeight;

		public AstarWeightedWalk(N firstNode, N lastNode, List<E> edges, double weight)
		{
			super(firstNode,lastNode,edges);

			this.totalWeight = weight;
		}

		public double getWeight()
		{
			return this.totalWeight;
		}
	}

	private final class PathedStep implements Comparable<PathedStep>
	{
		private N node;
		private N goalNode;
		private PathedStep parent;
		private E parentEdge;
		private double cachedPathWeight;

		public PathedStep(N node, N goalNode)
		{
			if( node == null )
				throw new IllegalArgumentException("node can not be null");
			if( goalNode == null )
				throw new IllegalArgumentException("goalNode can not be null");

			this.goalNode = goalNode;
			this.node = node;
		}

		public boolean updateParent(PathedStep newParent, E newParentEdge)
		{
			if( newParent == null )
				throw new IllegalArgumentException("newParent can not be null");
			if( newParent.equals(this))
				throw new IllegalArgumentException("newParent can not be this node");
			if( newParentEdge == null)
				throw new IllegalArgumentException("newParentEdge can not be null");
			if( ! newParentEdge.getNodes().contains(newParent.getNode()))
				throw new IllegalArgumentException("newParentEdge must connect to new Parent");
			if( (newParentEdge instanceof Weighted)&&( ((Weighted)newParentEdge).getWeight() < 0.0) )
				throw new IllegalArgumentException("edge weight can not be negative");
			if( (this.node instanceof Weighted)&&( ((Weighted)this.node).getWeight() < 0.0) )
				throw new IllegalArgumentException("this.node weight can not be negative");

			boolean parentHasEdge = false;
			for( Edge<N> edge : graph.getTraversableEdges(this.node) )
				if(edge.getNodes().contains(newParent.getNode()))
				{
					parentHasEdge = true;
					break;
				}

			if(!parentHasEdge)
				throw new IllegalArgumentException("newParent is not connected to this node");

			double newWeight = (newParentEdge instanceof Weighted ? ((Weighted)newParentEdge).getWeight() : 0.0);
			if(this.node instanceof Weighted)
				newWeight += ((Weighted)this.node).getWeight();
			else
				newWeight += 1.0;
			if((this.parent == null) || ( newParent.getCachedPathWeight() + newWeight < this.getCachedPathWeight()))
			{
				this.parent = newParent;
				this.parentEdge = newParentEdge;
				this.cachedPathWeight = newParent.getCachedPathWeight() + newWeight;
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

		public double getHeuristicCostToGoal()
		{
			return heuristicPathCost.getHeuristicPathCost(this.node, this.goalNode);
		}

		public double getHeuristicOverallCost()
		{
			return this.getCachedPathWeight() + this.getHeuristicCostToGoal();
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
			if(this.getHeuristicOverallCost() < compareWith.getHeuristicOverallCost())
				return -1;
			else if(this.getHeuristicOverallCost() > compareWith.getHeuristicOverallCost())
				return 1;
			else
				return 0;
		}

		public E getParentEdge()
		{
			return parentEdge;
		}
	}



	private G graph;
	private HeuristicPathCost<N> heuristicPathCost;

	public AstarPathFinder(G graph, HeuristicPathCost<N> heuristicPathCost)
	{
		if( graph == null )
			throw new IllegalArgumentException("graph can not be null");
		if( heuristicPathCost  == null )
			throw new IllegalArgumentException("heuristicPathCost can not be null");
		if( !heuristicPathCost.isOptimistic() )
			throw new IllegalArgumentException("heuristicPathCost must be admissible");
//		Does the heuristic need to be consistent?
//		if( !heuristicPathCost.isConsistent() )
//			throw new IllegalArgumentException("This implementation requires a consistent heuristic");

		this.graph = graph;
		this.heuristicPathCost = heuristicPathCost;
	}

	public WeightedWalk<N,E> getBestPath(N begin, N end)
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
		PathedStep beginStep = new PathedStep(begin, end);
		nodeStepMapping.put(begin, beginStep);
		candidateSteps.add(beginStep);

		//all nodes that have been closed can no longer be traversed
		Set<PathedStep> closedSteps = new HashSet<PathedStep>();

		//lets iterate through each step from the begining
		while(!candidateSteps.isEmpty())
		{
			PathedStep currentStep = candidateSteps.poll();
			if( currentStep.getNode().equals(end) )
				return pathedStepToWalk(currentStep);

			for(E edge : this.graph.getTraversableEdges(currentStep.node))
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
						neighborStep = new PathedStep(neighborNode, end);
						nodeStepMapping.put(neighborNode, neighborStep);
					}

					if(!neighborNode.equals(begin))
						neighborStep.updateParent(currentStep, edge);

					if(!closedSteps.contains(neighborStep))
					{
						candidateSteps.remove(neighborStep);
						candidateSteps.add(neighborStep);
					}
				}
			}

			closedSteps.add(currentStep);
		}

		return null;
	}

	public boolean isReachable(N begin, N end)
	{
		return (this.getBestPath(begin, end) != null);
	}

	public boolean isConnected(N begin, N end)
	{
		return (this.getBestPath(begin, end) != null);
	}

	private WeightedWalk<N,E> pathedStepToWalk(PathedStep endPathedStep)
	{
		List<E> edges = new ArrayList<E>();
		List<PathedStep> steps = new ArrayList<PathedStep>();
		N lastNode = endPathedStep.getNode();
		N firstNode = endPathedStep.getNode();

		PathedStep currentStep = endPathedStep;
		while(currentStep != null)
		{
			firstNode = currentStep.getNode();
			if(currentStep.getParentEdge() != null)
				edges.add(0, currentStep.getParentEdge());
			steps.add(currentStep);
			currentStep = currentStep.getParent();
		}

		return new AstarWeightedWalk(firstNode, lastNode, edges, endPathedStep.getCachedPathWeight());
	}
}
