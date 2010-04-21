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

import com.syncleus.dann.graph.*;
import java.util.*;

public class JohnsonGraphTransformer<N> implements GraphTransformer<BidirectedGraph<N, WeightedDirectedEdge<N>, WeightedBidirectedWalk<N, WeightedDirectedEdge<N>>>>
{
	private class SimpleGraph extends AbstractBidirectedGraph<N, WeightedDirectedEdge<N>, WeightedBidirectedWalk<N, WeightedDirectedEdge<N>>>
	{
		private Set<N> nodes = new HashSet<N>();

		private final Set<WeightedDirectedEdge<N>> edges = new HashSet<WeightedDirectedEdge<N>>();
		private final Map<N,Set<WeightedDirectedEdge<N>>> outEdges = new HashMap<N,Set<WeightedDirectedEdge<N>>>();
		private final Map<N,Set<WeightedDirectedEdge<N>>> inEdges = new HashMap<N,Set<WeightedDirectedEdge<N>>>();

		public SimpleGraph(BidirectedGraph<N, WeightedDirectedEdge<N>, WeightedBidirectedWalk<N, WeightedDirectedEdge<N>>> graphToCopy)
		{
			this.nodes.addAll(graphToCopy.getNodes());
			this.edges.addAll(graphToCopy.getEdges());

			for(WeightedDirectedEdge<N> edge : this.edges)
			{
				N outNode = edge.getSourceNode();
				N inNode = edge.getDestinationNode();

				Set<WeightedDirectedEdge<N>> outNodeEdges = this.outEdges.get(outNode);
				if(outNodeEdges == null)
				{
					outNodeEdges = new HashSet<WeightedDirectedEdge<N>>();
					this.outEdges.put(outNode, outNodeEdges);
				}
				Set<WeightedDirectedEdge<N>> inNodeEdges = this.inEdges.get(inNode);
				if(inNodeEdges == null)
				{
					inNodeEdges = new HashSet<WeightedDirectedEdge<N>>();
					this.inEdges.put(inNode, inNodeEdges);
				}

				outNodeEdges.add(edge);
				inNodeEdges.add(edge);
			}
		}

		public boolean add(WeightedDirectedEdge<N> newEdge)
		{
			if( newEdge == null )
				throw new IllegalArgumentException("newEdge can not be null");
			if( ! this.nodes.containsAll(newEdge.getNodes()) )
				throw new IllegalArgumentException("newEdge has a node as an end point that is not part of the graph");

			if( this.edges.add(newEdge) )
			{
				this.outEdges.get(newEdge.getSourceNode()).add(newEdge);
				this.inEdges.get(newEdge.getDestinationNode()).add(newEdge);
				return true;
			}

			return false;
		}


		public boolean add(N newNode)
		{
			if(newNode == null)
				throw new IllegalArgumentException("newNode can not be null");

			if( this.nodes.add(newNode) )
			{
				this.outEdges.put(newNode, new HashSet<WeightedDirectedEdge<N>>());
				this.inEdges.put(newNode, new HashSet<WeightedDirectedEdge<N>>());
				return true;
			}
			return false;
		}

		public boolean connect(N source, N destination, double weight)
		{
			if( source == null )
				throw new IllegalArgumentException("source can not be null");
			if( destination == null )
				throw new IllegalArgumentException("destination can not be null");
			if( ! this.nodes.contains( source ) )
				throw new IllegalArgumentException("source is not a member of this graph");
			if( ! this.nodes.contains( destination ) )
				throw new IllegalArgumentException("destination is not a member of this graph");

			return this.add(new SimpleWeightedDirectedEdge<N>(source, destination, weight));
		}

		public boolean disconnect(N source, N destination)
		{
			if( source == null )
				throw new IllegalArgumentException("source can not be null");
			if( destination == null )
				throw new IllegalArgumentException("destination can not be null");
			if( ! this.nodes.contains( source ) )
				throw new IllegalArgumentException("source is not a member of this graph");
			if( ! this.nodes.contains( destination ) )
				throw new IllegalArgumentException("destination is not a member of this graph");

			return this.remove(new SimpleWeightedDirectedEdge<N>(source, destination, 0.0));
		}

		public boolean remove(WeightedDirectedEdge<N> edgeToRemove)
		{
			if( edgeToRemove == null )
				throw new IllegalArgumentException("removeSynapse can not be null");

			if( this.edges.remove(edgeToRemove) )
			{
				if( this.outEdges.containsKey(edgeToRemove.getSourceNode()) )
					this.outEdges.get(edgeToRemove.getSourceNode()).remove(edgeToRemove);
				if( this.inEdges.containsKey(edgeToRemove.getDestinationNode()) )
					this.inEdges.get(edgeToRemove.getDestinationNode()).remove(edgeToRemove);
				return true;
			}
			return false;
		}

		public boolean remove(N nodeToRemove)
		{
			if( nodeToRemove == null )
				throw new IllegalArgumentException("node can not be null");

			if( this.nodes.remove(nodeToRemove) )
			{
				Set<WeightedDirectedEdge<N>> removeEdges = new HashSet<WeightedDirectedEdge<N>>();
				if( this.outEdges.containsKey(nodeToRemove) )
					removeEdges.addAll(this.outEdges.remove(nodeToRemove));
				if( this.inEdges.containsKey(nodeToRemove) )
					removeEdges.addAll(this.inEdges.remove(nodeToRemove));
				this.edges.removeAll(removeEdges);

				return true;
			}
			return false;
		}

		public Set<N> getNodes()
		{
			return Collections.unmodifiableSet(this.nodes);
		}

		@Override
		public Set<WeightedDirectedEdge<N>> getEdges()
		{
			return Collections.unmodifiableSet(this.edges);
		}

		public Set<WeightedDirectedEdge<N>> getEdges(N node)
		{
			Set<WeightedDirectedEdge<N>> nodeSynapses = new HashSet<WeightedDirectedEdge<N>>();
			if( this.outEdges.containsKey(node) )
				nodeSynapses.addAll( this.outEdges.get(node) );
			if( this.inEdges.containsKey(node) )
				nodeSynapses.addAll( this.inEdges.get(node) );
			return Collections.unmodifiableSet(nodeSynapses);
		}

		public Set<WeightedDirectedEdge<N>> getTraversableEdges(N node)
		{
			return this.getOutEdges(node);
		}

		public Set<WeightedDirectedEdge<N>> getOutEdges(N node)
		{
			if( this.outEdges.containsKey(node) )
				 return Collections.unmodifiableSet( this.outEdges.get(node) );
			return Collections.emptySet();
		}

		public Set<WeightedDirectedEdge<N>> getInEdges(N node)
		{
			if( this.inEdges.containsKey(node) )
				 return Collections.unmodifiableSet( this.inEdges.get(node) );
			return Collections.emptySet();
		}

		public int getIndegree(N node)
		{
			return this.inEdges.get(node).size();
		}

		public int getOutdegree(N node)
		{
			return this.outEdges.get(node).size();
		}


		public boolean isConnected(N leftNode, N rightNode)
		{
			Set<WeightedDirectedEdge<N>> outSet = this.getOutEdges(leftNode);
			Set<WeightedDirectedEdge<N>> inSet = this.getInEdges(rightNode);
			Set<WeightedDirectedEdge<N>> jointSet = new HashSet<WeightedDirectedEdge<N>>(outSet);
			jointSet.retainAll(inSet);

			return ( !jointSet.isEmpty() );
		}

		public List<N> getNeighbors(N node)
		{
			Set<WeightedDirectedEdge<N>> nodeEdges = this.getEdges(node);
			List<N> neighbors = new ArrayList<N>();
			for(WeightedDirectedEdge<N> nodeEdge : nodeEdges)
				neighbors.add( (nodeEdge.getLeftNode().equals(node) ? nodeEdge.getRightNode() : nodeEdge.getLeftNode() ) );
			return Collections.unmodifiableList(neighbors);
		}

		public List<N> getTraversableNeighbors(N node)
		{
			Set<WeightedDirectedEdge<N>> nodeEdges = this.getOutEdges(node);
			List<N> neighbors = new ArrayList<N>();
			for(WeightedDirectedEdge<N> nodeEdge : nodeEdges)
				neighbors.add( (nodeEdge.getLeftNode().equals(node) ? nodeEdge.getRightNode() : nodeEdge.getLeftNode() ) );
			return Collections.unmodifiableList(neighbors);
		}
	}
	
	private N blankNode;

	public JohnsonGraphTransformer(N blankNode)
	{
		this.blankNode = blankNode;
	}

	private boolean containsInfinite(BidirectedGraph<N, WeightedDirectedEdge<N>, WeightedBidirectedWalk<N, WeightedDirectedEdge<N>>> original)
	{
		for(WeightedDirectedEdge<N> edge : original.getEdges())
			if(Double.isInfinite(edge.getWeight()))
				return true;
		return false;
	}

	public BidirectedGraph<N, WeightedDirectedEdge<N>, WeightedBidirectedWalk<N, WeightedDirectedEdge<N>>> transform(BidirectedGraph<N, WeightedDirectedEdge<N>, WeightedBidirectedWalk<N, WeightedDirectedEdge<N>>> original)
	{
		if(original == null)
			throw new IllegalArgumentException("original can not be null");
		if(original.getNodes().contains(this.blankNode))
			throw new IllegalArgumentException("original can not contain blankNode");
		if(containsInfinite(original))
			throw new IllegalArgumentException("original cannot contain infinite weights");

		SimpleGraph copyGraph = new SimpleGraph(original);
		Set<N> originalNodes = copyGraph.getNodes();
		copyGraph.add(blankNode);
		for(N originalNode : originalNodes)
			copyGraph.connect(blankNode, originalNode, 0.0);

		BellmanFordPathFinder<SimpleGraph, N, WeightedDirectedEdge<N>> pathFinder = new BellmanFordPathFinder<SimpleGraph, N, WeightedDirectedEdge<N>>(copyGraph);

		SimpleGraph johnsonGraph = new SimpleGraph(original);
		List<WeightedDirectedEdge<N>> edges = new ArrayList<WeightedDirectedEdge<N>>(johnsonGraph.getEdges());
		for(WeightedDirectedEdge<N> edge : edges)
		{
			double newWeight = edge.getWeight() + pathFinder.getBestPath(blankNode, edge.getSourceNode(), false).getWeight() - pathFinder.getBestPath(blankNode, edge.getDestinationNode(), false).getWeight();
			johnsonGraph.remove(edge);
			johnsonGraph.add(new SimpleWeightedDirectedEdge<N>(edge.getSourceNode(), edge.getDestinationNode(), newWeight));
		}

		return johnsonGraph;
	}
}
