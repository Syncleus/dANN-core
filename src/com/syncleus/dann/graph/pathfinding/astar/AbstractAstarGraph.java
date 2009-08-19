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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class AbstractAstarGraph extends AbstractBidirectedGraph implements WeightedGraph, DirectedGraph
{
	private static class PathedStep implements Comparable<PathedStep>
	{
		private AstarNode node;
		private PathedStep parent;
		private AstarEdge parentEdge;
		private double cachedPathWeight;

		public PathedStep(AstarNode node)
		{
			if( node == null )
				throw new IllegalArgumentException("node can not be null");

			this.node = node;
		}

		public boolean updateParent(PathedStep newParent, AstarEdge newParentEdge)
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
			for(AstarEdge edge : this.getNode().getTraversableEdges())
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

		public AstarNode getNode()
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

		public AstarEdge getParentEdge()
		{
			return parentEdge;
		}
	}

	@Override
	public List<? extends AstarEdge> getEdges()
	{
		List<AstarEdge> allEdges = new ArrayList<AstarEdge>();
		Set<? extends AstarNode> allNodes = this.getNodes();
		Set<AstarNode> traversedNodes = new HashSet<AstarNode>();
		for(AstarNode node : allNodes)
		{
			List<? extends AstarEdge> currentEdges = node.getEdges();

			for(AstarEdge edge : currentEdges)
			{
				NodePair<? extends AstarNode> currentNodePair = edge.getNodePair();
				if((!traversedNodes.contains(currentNodePair.getLeftNode()))&&(!traversedNodes.contains(currentNodePair.getRightNode())))
					allEdges.add(edge);
			}

			traversedNodes.add(node);
		}

		return Collections.unmodifiableList(allEdges);
	}

	@Override
	public AstarWalk getShortestPath(Node beginNode, Node endNode)
	{
		if(beginNode == null)
			throw new IllegalArgumentException("begin can not be null");
		if(endNode == null)
			throw new IllegalArgumentException("end can not be null");
		if(beginNode.equals(endNode))
			throw new IllegalArgumentException("begin can not be equal to end");
		if( (!(beginNode instanceof AstarNode)) || (!(endNode instanceof AstarNode)) )
			return null;

		AstarNode begin = (AstarNode) beginNode;
		AstarNode end = (AstarNode) endNode;

		//initalize candidate nodes queue containing potential steps as a
		//solution
		Map<AstarNode, PathedStep> nodeStepMapping = new HashMap<AstarNode, PathedStep>();
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

			for(AstarEdge edge : currentStep.node.getTraversableEdges())
			{
				AstarNode neighborNode = (edge.getSourceNode().equals(currentStep.getNode()) ? edge.getDestinationNode() : edge.getSourceNode());
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

			closedSteps.add(currentStep);
		}

		return null;
	}

	private static AstarWalk pathedStepToWalk(PathedStep endPathedStep)
	{
		List<AstarEdge> steps = new ArrayList<AstarEdge>();
		AstarNode lastNode = endPathedStep.getNode();
		AstarNode firstNode = endPathedStep.getNode();

		PathedStep currentStep = endPathedStep;
		while(currentStep != null)
		{
			firstNode = currentStep.getNode();
			steps.add(0, currentStep.getParentEdge());
			currentStep = currentStep.getParent();
		}

		return new AstarWalk(firstNode, lastNode, steps);
	}

	public Set<? extends AstarNode> getNodes()
	{
		return null;
	}
}
