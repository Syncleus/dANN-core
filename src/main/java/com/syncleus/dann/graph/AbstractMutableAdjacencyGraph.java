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
package com.syncleus.dann.graph;

import java.util.*;

public abstract class AbstractMutableAdjacencyGraph<
	  	N,
	  	E extends Edge<N,? extends Edge.Endpoint<N>>,
	  	NEP extends MutableGraph.NodeEndpoint<N, E>,
	  	EEP extends MutableGraph.EdgeEndpoint<N, E>
	  >
	  extends AbstractAdjacencyGraph<N, E, NEP, EEP>
	  implements MutableGraph<N, E, NEP, EEP>
{
	private static final long serialVersionUID = -4613327727609060678L;
	private final AdjacencyMapping<NEP,EEP> adjacency = new HashAdjacencyMapping<NEP, EEP>();
	private final boolean uniqueNodes;
	private final boolean uniqueEdges;

	protected AbstractMutableAdjacencyGraph()
	{
		this(false);
	}

	protected AbstractMutableAdjacencyGraph(final boolean uniqueEdges)
	{
		this(uniqueEdges,true);
	}

	protected AbstractMutableAdjacencyGraph(final boolean uniqueEdges, final boolean uniqueNodes)
	{
		this.uniqueEdges = uniqueEdges;
		this.uniqueNodes = uniqueNodes;
	}

	protected final boolean isUniqueEdges()
	{
		return this.uniqueEdges;
	}

	protected final boolean isUniqueNodes()
	{
		return this.uniqueNodes;
	}

	@Override
	protected Set<Graph.EdgeEndpoint<N,E>> getAdjacentEdgeEndpoints(Graph.NodeEndpoint<? extends N, ? extends E> nodeEndpoint)
	{
		if( !this.adjacency.getLeftKeys().contains(nodeEndpoint) )
			throw new IllegalArgumentException("nodeEndpoint is not an endpoint for this graph");
		return Collections.<EdgeEndpoint<N,E>>unmodifiableSet(this.adjacency.getLeftAdjacency(nodeEndpoint));
	}

	protected abstract NEP createNodeEndpoint(N node) throws InvalidGraphException;
	protected abstract EEP createEdgeEndpoint(E node) throws InvalidGraphException;

	@Override
	public NEP joinNode(N node) throws InvalidGraphException
	{
		if(this.uniqueNodes && this.contains(node))
			return this.getNodeEndpoints(node).iterator().next();

		final NEP endpoint = this.createNodeEndpoint(node);
		assert !this.adjacency.getLeftKeys().contains(endpoint);

		this.adjacency.putLeftKey(endpoint);

		return endpoint;
	}

	@Override
	public Map<N, NEP> joinNodes(Set<? extends N> nodes) throws InvalidGraphException
	{
		final Map<N, NEP> endpoints = new HashMap<N,NEP>();
		for(N node : nodes)
			endpoints.put(node, this.joinNode(node));
		return Collections.unmodifiableMap(endpoints);
	}

	@Override
	public Map<N, Set<NEP>> joinNodes(Map<? extends N, ? extends Integer> nodes) throws InvalidGraphException
	{
		final Map<N,Set<NEP>> joinMapping = new HashMap<N, Set<NEP>>(nodes.size());
		for(final Map.Entry<? extends N,? extends Integer> nodeEntry : nodes.entrySet())
		{
			final Set<NEP> newEndpoints = new HashSet<NEP>(nodeEntry.getValue());
			for(int count = 0; count < nodeEntry.getValue(); count++)
				newEndpoints.add(this.joinNode(nodeEntry.getKey()));
			joinMapping.put(nodeEntry.getKey(),newEndpoints);
		}
		return Collections.unmodifiableMap(joinMapping);
	}

	@Override
	public Set<EEP> leaveNode(MutableGraph.NodeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>> endpoint) throws InvalidGraphException
	{
		if( !this.adjacency.getLeftKeys().contains(endpoint) )
			throw new IllegalArgumentException("endpoint is not an enpoint in this graph");

		//first we need to remove all associated edges
		final Set<EEP> orphanEdges = this.adjacency.getLeftAdjacency(endpoint);
		this.adjacency.getRightKeys().removeAll(orphanEdges);
		this.adjacency.removeLeftKey(endpoint);

		return Collections.unmodifiableSet(orphanEdges);
	}

	@Override
	public Set<EEP> leaveNodes(Set<? extends MutableGraph.NodeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>>> nodeEndpoints) throws InvalidGraphException
	{
		final Set<EEP> edgeEndpoints = new HashSet<EEP>();
		for(MutableGraph.NodeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>> node : nodeEndpoints)
			edgeEndpoints.addAll(this.leaveNode(node));
		return Collections.unmodifiableSet(edgeEndpoints);
	}

	@Override
	public EEP joinEdge(E edge) throws InvalidGraphException
	{
		//make sure all of the edge's node already belong to this graph
		if( !this.containsAll(edge.getTargets()) )
			throw new IllegalArgumentException("All of the edge's targets must already exist in this graph");

		if(this.uniqueEdges && this.contains(edge))
			return this.getEdgeEndpoints(edge).iterator().next();

		final EEP edgeEndpoint = this.createEdgeEndpoint(edge);
		for( final N node : edge.getTargets() )
			this.adjacency.put(this.getNodeEndpoints(node).iterator().next(),edgeEndpoint);

		return edgeEndpoint;
	}

	@Override
	public Map<E, EEP> joinEdges(Set<? extends E> edges) throws InvalidGraphException
	{
		final Map<E, EEP> endpoints = new HashMap<E,EEP>();
		for(E edge : edges)
			endpoints.put(edge, this.joinEdge(edge));
		return Collections.unmodifiableMap(endpoints);
	}

	@Override
	public Map<E, Set<EEP>> joinEdges(Map<? extends E, ? extends Integer> edges) throws InvalidGraphException
	{
		final Map<E,Set<EEP>> joinMapping = new HashMap<E, Set<EEP>>(edges.size());
		for(final Map.Entry<? extends E,? extends Integer> edgeEntry : edges.entrySet())
		{
			final Set<EEP> newEndpoints = new HashSet<EEP>(edgeEntry.getValue());
			for(int count = 0; count < edgeEntry.getValue(); count++)
				newEndpoints.add(this.joinEdge(edgeEntry.getKey()));
			joinMapping.put(edgeEntry.getKey(),newEndpoints);
		}
		return Collections.unmodifiableMap(joinMapping);
	}

	@Override
	public void leaveEdge(MutableGraph.EdgeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>> edgeEndpoint) throws InvalidGraphException
	{
		if( !this.adjacency.getRightKeys().contains(edgeEndpoint) )
			throw new IllegalArgumentException("endpoint is not an enpoint in this graph");

		this.adjacency.removeRightKey(edgeEndpoint);
	}

	@Override
	public void leaveEdges(Set<? extends MutableGraph.EdgeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>>> edgeEndpoints) throws InvalidGraphException
	{
		for(final MutableGraph.EdgeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>> edgeEndpoint : edgeEndpoints)
			this.leaveEdge(edgeEndpoint);
	}

	@Override
	public void clear() throws InvalidGraphException
	{
		this.adjacency.clear();
	}

	@Override
	public void clearEdges() throws InvalidGraphException
	{
		this.adjacency.getRightKeys().clear();
	}

	@Override
	public Map<?, Graph.Endpoint<N, E, ?>> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, Set<? extends Graph.Endpoint<?, ?, ?>> disconnectEndpoints) throws InvalidGraphException
	{
		for(final Graph.Endpoint<?,?,?> disconnectEndpoint : disconnectEndpoints)
			this.adjacency.remove(disconnectEndpoint.getTarget());

		Map<Object, Graph.Endpoint<N,E,?>> newEndpoints = new HashMap<Object, Graph.Endpoint<N, E, ?>>();
		newEndpoints.putAll(this.joinNodes(addNodes));
		newEndpoints.putAll(this.joinEdges(addEdges));
		return newEndpoints;
	}

	@Override
	public Set<EEP> getEdgeEndpoints()
	{
		return Collections.unmodifiableSet(this.adjacency.getRightKeys());
	}

	@Override
	public Set<NEP> getNodeEndpoints()
	{
		return Collections.unmodifiableSet(this.adjacency.getLeftKeys());
	}

/*
		private class NodeTargetSet extends AbstractTargetSet<N>
		{
			@Override
			public int size()
			{
				return nodeAdjacency.size();
			}

			@Override
			public boolean isEmpty()
			{
				return nodeAdjacency.isEmpty();
			}

			@Override
			public boolean contains(N o)
			{
				return nodeAdjacency.containsKey(o);
			}

			@Override
			public Iterator<N> iterator()
			{
				return Collections.unmodifiableSet(nodeAdjacency.keySet()).iterator();
			}

			@Override
			public Object[] toArray()
			{
				Object[] array = new Object[this.size()];
				int arrayIndex = 0;
				for(NEP nodeEndpoint : nodeAdjacency.keySet())
				{
					array[arrayIndex] = nodeEndpoint.getTarget();
					arrayIndex++;
				}
				return array;
			}

			@Override
			public <T> T[] toArray(T[] a)
			{
				return nodeAdjacency.keySet().toArray(a);
			}

			@Override
			public boolean add(N n)
			{
				throw new UnsupportedOperationException("This set is read-only!");
			}

			@Override
			public boolean remove(Object o)
			{
				throw new UnsupportedOperationException("This set is read-only!");
			}

			@Override
			public boolean containsAll(Collection<?> c)
			{
				return nodeAdjacency.keySet()
			}

			@Override
			public boolean addAll(Collection<? extends N> c)
			{
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public boolean retainAll(Collection<?> c)
			{
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public boolean removeAll(Collection<?> c)
			{
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public void clear()
			{
				//To change body of implemented methods use File | Settings | File Templates.
			}
		};
*/

	protected abstract class AbstractNodeEndpoint extends AbstractAdjacencyGraph<N,E,NEP,EEP>.AbstractNodeEndpoint implements MutableGraph.NodeEndpoint<N, E>
	{
	}

	protected abstract class AbstractEdgeEndpoint extends AbstractAdjacencyGraph<N,E,NEP,EEP>.AbstractEdgeEndpoint implements MutableGraph.EdgeEndpoint<N, E>
	{
	}
}