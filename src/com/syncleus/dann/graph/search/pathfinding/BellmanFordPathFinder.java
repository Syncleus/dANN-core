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

import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.SimpleBidirectedWalk;
import com.syncleus.dann.graph.Weighted;
import com.syncleus.dann.graph.WeightedBidirectedWalk;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BellmanFordPathFinder<G extends BidirectedGraph<? extends N, ? extends E, ?>, N, E extends DirectedEdge<? extends N>> implements PathFinder<N,E,WeightedBidirectedWalk<N,E>>
{
	private final class DumbWeightedWalk extends SimpleBidirectedWalk<N,E> implements WeightedBidirectedWalk<N,E>
	{
		private final double totalWeight;

		public DumbWeightedWalk(N firstNode, N lastNode, List<E> edges, double weight)
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
		private PathedStep parent;
		private E parentEdge;
		private double cachedPathWeight;

		public PathedStep(N node, double initialWeight)
		{
			if( node == null )
				throw new IllegalArgumentException("node can not be null");

			this.node = node;
			this.cachedPathWeight = initialWeight;
		}

		private boolean updateParent(PathedStep newParent, E newParentEdge)
		{
			double newWeight = (newParentEdge instanceof Weighted ? ((Weighted)newParentEdge).getWeight() : 1.0);
			if(this.node instanceof Weighted)
				newWeight += ((Weighted)this.node).getWeight();
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
				return -1;
			else if(this.getCachedPathWeight() > compareWith.getCachedPathWeight())
				return 1;
			else
				return 0;
		}

		public E getParentEdge()
		{
			return parentEdge;
		}

		@Override
		public String toString()
		{
			return this.node.toString();
		}
	}

	private G graph;
	Map<N,PathedStep> pathedSteps;
	N begin;

	public BellmanFordPathFinder(G graph)
	{
		if( graph == null )
			throw new IllegalArgumentException("graph can not be null");
		if( graph.isMultigraph() )
			throw new IllegalArgumentException("graph can not be a multigraph");

		this.graph = graph;
	}

	public WeightedBidirectedWalk<N,E> getBestPath(N begin, N end)
	{
		return this.getBestPath(begin, end, true);
	}

	public WeightedBidirectedWalk<N,E> getBestPath(N begin, N end, boolean refresh)
	{
		if((refresh)||(this.pathedSteps == null)||(!begin.equals(this.begin)))
			this.calculateSteps(begin);

		//construct a walk from the end node
		PathedStep endPathedStep = pathedSteps.get(end);
		PathedStep beginPathedStep = pathedSteps.get(begin);
		assert ((endPathedStep != null)&&(beginPathedStep != null));

		return this.pathedStepToWalk(endPathedStep);
	}

	public void calculateSteps(N begin)
	{
		Set<? extends N> nodes = this.graph.getNodes();
		Set<? extends E> edges = this.graph.getEdges();

		pathedSteps = new HashMap<N,PathedStep>(nodes.size());

		//relax edges
		for(int lcv = 0; lcv < (nodes.size() - 1); lcv++)
		{
			for(E edge : edges)
			{
				if( edge.getDestinationNode() == begin )
					continue;

				PathedStep sourcePathedStep = pathedSteps.get(edge.getSourceNode());
				if( sourcePathedStep == null )
				{
					sourcePathedStep = new PathedStep(edge.getSourceNode(), (edge.getSourceNode().equals(begin) ? 0.0 : Double.POSITIVE_INFINITY));
					pathedSteps.put(edge.getSourceNode(), sourcePathedStep);
				}
				PathedStep destinationPathedStep = pathedSteps.get(edge.getDestinationNode());
				if( destinationPathedStep == null )
				{
					destinationPathedStep = new PathedStep(edge.getDestinationNode(), Double.POSITIVE_INFINITY);
					pathedSteps.put(edge.getDestinationNode(), destinationPathedStep);
				}

				destinationPathedStep.updateParent(sourcePathedStep, edge);
			}
		}

		//check for negative cycles
		for(E edge : edges)
		{
			if( edge.getDestinationNode() == begin )
				continue;
			
			PathedStep sourcePathedStep = pathedSteps.get(edge.getSourceNode());
			PathedStep destinationPathedStep = pathedSteps.get(edge.getDestinationNode());
			assert (( sourcePathedStep != null )&&( destinationPathedStep != null ));

			if(destinationPathedStep.updateParent(sourcePathedStep, edge))
				throw new NegativeWeightCycleException("negative-weight cycle found in graph");
		}
	}

	public boolean isReachable(N begin, N end)
	{
		return ( this.getBestPath(begin, end) != null);
	}

	public boolean isConnected(N begin, N end)
	{
		return ( this.getBestPath(begin, end) != null);
	}

	private WeightedBidirectedWalk<N,E> pathedStepToWalk(PathedStep endPathedStep)
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

		return new DumbWeightedWalk(firstNode, lastNode, edges, endPathedStep.getCachedPathWeight());
	}
}
