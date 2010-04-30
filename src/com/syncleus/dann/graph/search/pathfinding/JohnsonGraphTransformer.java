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

public class JohnsonGraphTransformer<N> implements GraphTransformer<BidirectedGraph<N, ? extends WeightedDirectedEdge<N>>>
{
	private static class SimpleGraph<N> extends AbstractBidirectedGraph<N,WeightedDirectedEdge<N>>
	{
		private static final long serialVersionUID = 6277394757261733780L;
		
		private Set<N> nodes;

		private final Set<WeightedDirectedEdge<N>> edges;
		private final Map<N,Set<WeightedDirectedEdge<N>>> outEdges = new HashMap<N,Set<WeightedDirectedEdge<N>>>();
		private final Map<N,Set<WeightedDirectedEdge<N>>> inEdges = new HashMap<N,Set<WeightedDirectedEdge<N>>>();

		public SimpleGraph(Set<N> nodes, Set<WeightedDirectedEdge<N>> edges)
		{
			this.nodes  = nodes;
			this.edges = edges;

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

		public Set<WeightedDirectedEdge<N>> getAdjacentEdges(N node)
		{
			Set<WeightedDirectedEdge<N>> nodeSynapses = new HashSet<WeightedDirectedEdge<N>>();
			if( this.outEdges.containsKey(node) )
				nodeSynapses.addAll( this.outEdges.get(node) );
			if( this.inEdges.containsKey(node) )
				nodeSynapses.addAll( this.inEdges.get(node) );
			return Collections.unmodifiableSet(nodeSynapses);
		}

		@Override
		public Set<WeightedDirectedEdge<N>> getTraversableEdges(N node)
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

		public List<N> getAdjacentNodes(N node)
		{
			Set<WeightedDirectedEdge<N>> nodeEdges = this.getAdjacentEdges(node);
			List<N> neighbors = new ArrayList<N>();
			for(WeightedDirectedEdge<N> nodeEdge : nodeEdges)
				neighbors.add( (nodeEdge.getLeftNode().equals(node) ? nodeEdge.getRightNode() : nodeEdge.getLeftNode() ) );
			return Collections.unmodifiableList(neighbors);
		}

		@Override
		public List<N> getTraversableNodes(N node)
		{
			Set<WeightedDirectedEdge<N>> adjacentOutEdges = this.getTraversableEdges(node);
			List<N> neighbors = new ArrayList<N>();
			for(WeightedDirectedEdge<N> outEdge : adjacentOutEdges)
				neighbors.add( outEdge.getDestinationNode() );
			return Collections.unmodifiableList(neighbors);
		}
	}

	private static final Object blankNode = new Object();

	private boolean containsInfinite(Graph<N,?> original)
	{
		for(Edge edge : original.getEdges())
			if(edge instanceof Weighted)
				if(Double.isInfinite(((Weighted)edge).getWeight()))
					return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public BidirectedGraph<N, WeightedDirectedEdge<N>> transform(BidirectedGraph<N,? extends WeightedDirectedEdge<N>> original)
	{
		if(original == null)
			throw new IllegalArgumentException("original can not be null");
		if(containsInfinite(original))
			throw new IllegalArgumentException("original cannot contain infinite weights");

		Set<WeightedDirectedEdge<Object>> originalEdges = new HashSet<WeightedDirectedEdge<Object>>();
		for(WeightedDirectedEdge<N> originalEdge : original.getEdges())
			originalEdges.add((WeightedDirectedEdge<Object>)originalEdge);
		SimpleGraph<Object> copyGraph = new SimpleGraph<Object>(new HashSet<Object>(original.getNodes()), originalEdges );

		Set<Object> originalNodes = copyGraph.getNodes();
		copyGraph.add(blankNode);
		for(Object originalNode : originalNodes)
			copyGraph.connect(blankNode, originalNode, 0.0);

		BellmanFordPathFinder<Object, WeightedDirectedEdge<Object>> pathFinder = new BellmanFordPathFinder<Object, WeightedDirectedEdge<Object>>(copyGraph);

		SimpleGraph johnsonGraph = new SimpleGraph(original.getNodes(), new HashSet<WeightedDirectedEdge<N>>(original.getEdges()));
		List<WeightedDirectedEdge<N>> edges = new ArrayList<WeightedDirectedEdge<N>>(johnsonGraph.getEdges());
		for(WeightedDirectedEdge<N> edge : edges)
		{
			double newWeight = edge.getWeight() + this.getPathWeight(pathFinder.getBestPath(blankNode, edge.getSourceNode(), false)) - this.getPathWeight(pathFinder.getBestPath(blankNode, edge.getDestinationNode(), false));
			johnsonGraph.remove(edge);
			johnsonGraph.add(new SimpleWeightedDirectedEdge<N>(edge.getSourceNode(), edge.getDestinationNode(), newWeight));
		}

		return johnsonGraph;
	}

	private double getPathWeight(List<WeightedDirectedEdge<Object>> path)
	{
		double weight = 0.0;
		for(WeightedDirectedEdge<Object> node : path)
			weight += node.getWeight();
		return weight;
	}
}
