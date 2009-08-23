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

import com.syncleus.dann.graph.AbstractBidirectedGraph;
import com.syncleus.dann.graph.DirectedGraph;
import com.syncleus.dann.graph.WeightedGraph;
import com.syncleus.dann.graph.Node;
import com.syncleus.dann.graph.NodePair;
import com.syncleus.dann.graph.pathfinding.PathFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class AbstractAstarGraph<L> extends AbstractBidirectedGraph<AstarNode<L>,AstarEdge<L>,AstarWalk<L>> implements WeightedGraph<AstarNode<L>,AstarEdge<L>,AstarWalk<L>>, DirectedGraph<AstarNode<L>,AstarEdge<L>,AstarWalk<L>>, PathFinder<AstarNode<L>,AstarWalk<L>>
{
	private static class PathedStep<L> implements Comparable<PathedStep<L>>
	{
		private AstarNode<L> node;
		private PathedStep<L> parent;
		private AstarEdge<L> parentEdge;
		private double cachedPathWeight;

		public PathedStep(AstarNode<L> node)
		{
			if( node == null )
				throw new IllegalArgumentException("node can not be null");

			this.node = node;
		}

		public boolean updateParent(PathedStep<L> newParent, AstarEdge<L> newParentEdge)
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
			for(AstarEdge<L> edge : this.getNode().getTraversableEdges())
				if(edge.getNodes().contains(newParent.getNode()))
				{
					parentHasEdge = true;
					break;
				}

			if(!parentHasEdge)
				throw new IllegalArgumentException("newParent is not connected to this node");

			if((this.parent == null) || ( newParent.getCachedPathWeight() + newParentEdge.getAstarWeight() < this.getCachedPathWeight()))
			{
				this.parent = newParent;
				this.parentEdge = newParentEdge;
				this.cachedPathWeight = newParent.getCachedPathWeight() + newParentEdge.getAstarWeight();
				return true;
			}
			else
				return false;
		}

		public AstarNode<L> getNode()
		{
			return node;
		}

		public PathedStep<L> getParent()
		{
			return parent;
		}

		public double getCachedPathWeight()
		{
			return cachedPathWeight;
		}

		@Override
		public boolean equals(Object compareTo)
		{
			if(!(compareTo instanceof PathedStep))
				return false;
			
			return this.node.equals(compareTo);
		}

		@Override
		public int hashCode()
		{
			return this.node.hashCode();
		}

		public int compareTo(PathedStep<L> compareWith)
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

		public AstarEdge<L> getParentEdge()
		{
			return parentEdge;
		}
	}

	@Override
	public List<AstarEdge<L>> getEdges()
	{
		List<AstarEdge<L>> allEdges = new ArrayList<AstarEdge<L>>();
		Set<AstarNode<L>> allNodes = this.getNodes();
		Set<AstarNode<L>> traversedNodes = new HashSet<AstarNode<L>>();
		for(AstarNode<L> node : allNodes)
		{
			List<AstarEdge<L>> currentEdges = node.getEdges();

			for(AstarEdge<L> edge : currentEdges)
			{
				NodePair<AstarNode<L>> currentNodePair = edge.getAstarNodePair();
				if((!traversedNodes.contains(currentNodePair.getLeftNode()))&&(!traversedNodes.contains(currentNodePair.getRightNode())))
					allEdges.add(edge);
			}

			traversedNodes.add(node);
		}

		return Collections.unmodifiableList(allEdges);
	}

	public boolean isReachable(AstarNode<L> begin, AstarNode<L> end)
	{
		if( this.getBestPath(begin, end) != null )
			return true;
		return false;
	}

	public boolean isConnected(AstarNode<L> begin, AstarNode<L> end)
	{
		if(begin == null)
			throw new IllegalArgumentException("begin can not be null");
		if(end == null)
			throw new IllegalArgumentException("end can not be null");
		if(begin.equals(end))
			throw new IllegalArgumentException("begin can not be equal to end");

		//initalize candidate nodes queue containing potential steps as a
		//solution
		Map<AstarNode<L>, PathedStep<L>> nodeStepMapping = new HashMap<AstarNode<L>, PathedStep<L>>();
		PriorityQueue<PathedStep<L>> candidateSteps = new PriorityQueue<PathedStep<L>>();
		PathedStep<L> beginStep = new PathedStep<L>(begin);
		nodeStepMapping.put(begin, beginStep);
		candidateSteps.add(beginStep);

		//all nodes that have been closed cannolonger be traversed
		Set<PathedStep<L>> closedSteps = new HashSet<PathedStep<L>>();

		//lets iterate through each step from the begining
		while(!candidateSteps.isEmpty())
		{
			PathedStep<L> currentStep = candidateSteps.poll();
			if( currentStep.getNode().equals(end) )
				return true;

			for(AstarEdge<L> edge : currentStep.node.getEdges())
			{
				AstarNode<L> neighborNode = (edge.getAstarNodePair().getSourceNode().equals(currentStep.getNode()) ? edge.getAstarNodePair().getDestinationNode() : edge.getAstarNodePair().getSourceNode());
				PathedStep<L> neighborStep;
				if(nodeStepMapping.containsKey(neighborNode))
					neighborStep = nodeStepMapping.get(neighborNode);
				else
				{
					neighborStep = new PathedStep<L>(neighborNode);
					nodeStepMapping.put(neighborNode, neighborStep);
				}

				if(!closedSteps.contains(neighborStep))
					candidateSteps.add(neighborStep);

				if(!neighborNode.equals(begin))
					neighborStep.updateParent(currentStep, edge);
			}

			closedSteps.add(currentStep);
		}

		return false;
	}

	@Override
	public AstarWalk<L> getBestPath(AstarNode<L> begin, AstarNode<L> end)
	{
		if(begin == null)
			throw new IllegalArgumentException("begin can not be null");
		if(end == null)
			throw new IllegalArgumentException("end can not be null");
		if(begin.equals(end))
			throw new IllegalArgumentException("begin can not be equal to end");

		//initalize candidate nodes queue containing potential steps as a
		//solution
		Map<AstarNode<L>, PathedStep<L>> nodeStepMapping = new HashMap<AstarNode<L>, PathedStep<L>>();
		PriorityQueue<PathedStep<L>> candidateSteps = new PriorityQueue<PathedStep<L>>();
		PathedStep<L> beginStep = new PathedStep<L>(begin);
		nodeStepMapping.put(begin, beginStep);
		candidateSteps.add(beginStep);

		//all nodes that have been closed cannolonger be traversed
		Set<PathedStep<L>> closedSteps = new HashSet<PathedStep<L>>();

		//lets iterate through each step from the begining
		while(!candidateSteps.isEmpty())
		{
			PathedStep<L> currentStep = candidateSteps.poll();
			if( currentStep.getNode().equals(end) )
				return pathedStepToWalk(currentStep);

			for(AstarEdge<L> edge : currentStep.node.getTraversableEdges())
			{
				AstarNode<L> neighborNode = (edge.getAstarNodePair().getSourceNode().equals(currentStep.getNode()) ? edge.getAstarNodePair().getDestinationNode() : edge.getAstarNodePair().getSourceNode());
				PathedStep<L> neighborStep;
				if(nodeStepMapping.containsKey(neighborNode))
					neighborStep = nodeStepMapping.get(neighborNode);
				else
				{
					neighborStep = new PathedStep<L>(neighborNode);
					nodeStepMapping.put(neighborNode, neighborStep);
				}

				if(!closedSteps.contains(neighborStep))
					candidateSteps.add(neighborStep);

				if(!neighborNode.equals(begin))
					neighborStep.updateParent(currentStep, edge);
			}

			closedSteps.add(currentStep);
		}

		return null;
	}

	private static <L> AstarWalk<L> pathedStepToWalk(PathedStep<L> endPathedStep)
	{
		List<AstarEdge<L>> steps = new ArrayList<AstarEdge<L>>();
		AstarNode<L> lastNode = endPathedStep.getNode();
		AstarNode<L> firstNode = endPathedStep.getNode();

		PathedStep<L> currentStep = endPathedStep;
		while(currentStep != null)
		{
			firstNode = currentStep.getNode();
			steps.add(0, currentStep.getParentEdge());
			currentStep = currentStep.getParent();
		}

		return new AstarWalk<L>(firstNode, lastNode, steps);
	}
}
