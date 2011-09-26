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
import com.syncleus.dann.graph.adjacency.AdjacencyMapping;
import com.syncleus.dann.graph.adjacency.HashAdjacencyMapping;

public abstract class AbstractMutableAdjacencyGraph<
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<N>>,
	  	NE extends MutableCloudGraph.NodeEndpoint<N, E>,
	  	EE extends MutableCloudGraph.EdgeEndpoint<N, E>
	  >
	  extends AbstractAdjacencyGraph<N, E, NE, EE>
	  implements MutableCloudGraph<N, E, NE, EE>
{
	private static final long serialVersionUID = -4613327727609060678L;
	private final AdjacencyMapping<MutableCloudGraph.NodeEndpoint<N, E>,MutableCloudGraph.EdgeEndpoint<N, E>,Endpoint<N>> adjacency = new HashAdjacencyMapping<MutableCloudGraph.NodeEndpoint<N, E>,MutableCloudGraph.EdgeEndpoint<N, E>,Endpoint<N>>();

	protected AbstractMutableAdjacencyGraph()
	{
	}

	protected void internalJoinNode(final MutableCloudGraph.NodeEndpoint<N, E> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));
		else if( this.contains(endpoint) )
			throw new IllegalArgumentException("endpoint is already a member of this graph!");

		this.adjacency.putLeftKey(endpoint);
	}

	protected void internalLeaveNode(MutableCloudGraph.NodeEndpoint<?, ?> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));
		else if( !this.adjacency.getLeftKeys().contains(endpoint) )
			throw new IllegalArgumentException("endpoint is not an endpoint in this graph");
		else if( this.getAdjacentEdgeEndpoints(endpoint).size() > 0)
			throw new InvalidGraphException("Node must be an orphan in the graph to remove it");

		this.adjacency.removeLeftKey(endpoint);
	}

	protected void internalJoinEdge(final MutableCloudGraph.EdgeEndpoint<N, E> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));
		//make sure all of the edge's node already belong to this graph
		else if( !this.containsAllTargets(endpoint.getTarget().getTargets()) )
			throw new InvalidGraphException("All of the edge's targets must already exist in this graph");
		//we cant do anything if the endpoint already exists in the key, they are unique representations of
		//a single membership to the graph
		else if(this.contains(endpoint))
			throw new IllegalArgumentException("endpoint is already a member of this graph!");

		for( final Cloud.Endpoint<N> targetEndpoint : endpoint.getTarget().getEndpoints() )
			for( final NE nodeEndpoint : this.getNodeEndpoints(targetEndpoint.getTarget()))
				this.adjacency.put(nodeEndpoint,endpoint,targetEndpoint);
	}


	protected void internalLeaveEdge(MutableCloudGraph.EdgeEndpoint<?, ?> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));
		else if( !this.adjacency.getRightKeys().contains(endpoint) )
			throw new IllegalArgumentException("endpoint is not an endpoint in this graph");

		this.adjacency.removeRightKey(endpoint);
	}

	@Override
	protected Set<CloudGraph.EdgeEndpoint<N,E>> getAdjacentEdgeEndpoints(CloudGraph.NodeEndpoint<?, ?> nodeEndpoint)
	{
		if( !this.adjacency.getLeftKeys().contains(nodeEndpoint) )
			throw new IllegalArgumentException("nodeEndpoint is not an endpoint for this graph");
		return Collections.<EdgeEndpoint<N,E>>unmodifiableSet(this.adjacency.getLeftAdjacency(nodeEndpoint));
	}

	@Override
	public void leaveNode(MutableCloudGraph.NodeEndpoint<?, ?> endpoint) throws InvalidGraphException
	{
		this.internalLeaveNode(endpoint);
	}

	@Override
	public void leaveEdge(MutableCloudGraph.EdgeEndpoint<?, ?> endpoint) throws InvalidGraphException
	{
		this.internalLeaveEdge(endpoint);
	}

	@Override
	public Map<N, NE> joinNodes(Set<? extends N> nodes) throws InvalidGraphException
	{
		final Map<N, NE> endpoints = new HashMap<N,NE>();
		for(N node : nodes)
			endpoints.put(node, this.joinNode(node));
		return Collections.unmodifiableMap(endpoints);
	}

	@Override
	public Map<N, Set<NE>> joinNodes(Map<? extends N, ? extends Integer> nodes) throws InvalidGraphException
	{
		final Map<N,Set<NE>> joinMapping = new HashMap<N, Set<NE>>(nodes.size());
		for(final Map.Entry<? extends N,? extends Integer> nodeEntry : nodes.entrySet())
		{
			final Set<NE> newEndpoints = new HashSet<NE>(nodeEntry.getValue());
			for(int count = 0; count < nodeEntry.getValue(); count++)
				newEndpoints.add(this.joinNode(nodeEntry.getKey()));
			joinMapping.put(nodeEntry.getKey(),newEndpoints);
		}
		return Collections.unmodifiableMap(joinMapping);
	}

	@Override
	public void leaveNodes(Set<? extends MutableCloudGraph.NodeEndpoint<?, ?>> nodeEndpoints) throws InvalidGraphException
	{
		for(MutableCloudGraph.NodeEndpoint<?, ? extends Cloud<?,? extends Endpoint<?>>> node : nodeEndpoints)
			this.leaveNode(node);
	}

	@Override
	public Map<E, EE> joinEdges(Set<? extends E> edges) throws InvalidGraphException
	{
		final Map<E, EE> endpoints = new HashMap<E,EE>();
		for(E edge : edges)
			endpoints.put(edge, this.joinEdge(edge));
		return Collections.unmodifiableMap(endpoints);
	}

	@Override
	public Map<E, Set<EE>> joinEdges(Map<? extends E, ? extends Integer> edges) throws InvalidGraphException
	{
		final Map<E,Set<EE>> joinMapping = new HashMap<E, Set<EE>>(edges.size());
		for(final Map.Entry<? extends E,? extends Integer> edgeEntry : edges.entrySet())
		{
			final Set<EE> newEndpoints = new HashSet<EE>(edgeEntry.getValue());
			for(int count = 0; count < edgeEntry.getValue(); count++)
				newEndpoints.add(this.joinEdge(edgeEntry.getKey()));
			joinMapping.put(edgeEntry.getKey(),newEndpoints);
		}
		return Collections.unmodifiableMap(joinMapping);
	}

	@Override
	public void leaveEdges(Set<? extends MutableCloudGraph.EdgeEndpoint<?, ?>> edgeEndpoints) throws InvalidGraphException
	{
		for(final MutableCloudGraph.EdgeEndpoint<?, ? extends Cloud<?,? extends Endpoint<?>>> edgeEndpoint : edgeEndpoints)
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
	public Map<?, CloudGraph.Endpoint<?, N, E>> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, Set<? extends CloudGraph.Endpoint<?, ?, ?>> disconnectEndpoints) throws InvalidGraphException
	{
		if( disconnectEndpoints != null )
		{
			for(final CloudGraph.Endpoint<?, ?,?> disconnectEndpoint : disconnectEndpoints)
				this.adjacency.remove(disconnectEndpoint.getTarget());
		}

		Map<Object, CloudGraph.Endpoint<?, N,E>> newEndpoints = new HashMap<Object, CloudGraph.Endpoint<?, N, E>>();
		if( addNodes != null )
			newEndpoints.putAll(this.joinNodes(addNodes));
		if( addEdges != null )
			newEndpoints.putAll(this.joinEdges(addEdges));
		return newEndpoints;
	}

	@Override
	public Set<EE> getEdgeEndpoints()
	{
		return Collections.unmodifiableSet(this.adjacency.getRightKeys());
	}

	@Override
	public Set<NE> getNodeEndpoints()
	{
		return Collections.unmodifiableSet(this.adjacency.getLeftKeys());
	}

	@Override
	public boolean isContextEnabled()
	{
		return false;
	}


	protected abstract class AbstractNodeEndpoint extends AbstractAdjacencyGraph<N,E,NE,EE>.AbstractNodeEndpoint implements MutableCloudGraph.NodeEndpoint<N, E>
	{
		private N target;

		protected AbstractNodeEndpoint(final N target)
		{
			this.target = target;
		}

		@Override
		public void setTarget(N newTarget) throws InvalidGraphException
		{
			if(this.target != null && this.target.equals(newTarget) )
			{
				this.target = newTarget;
				return;
			}

			internalLeaveNode(this);
			this.target = newTarget;
			internalJoinNode(this);
		}

		@Override
		public N getTarget()
		{
			return this.target;
		}
	};

	protected abstract class AbstractEdgeEndpoint extends AbstractAdjacencyGraph<N,E,NE,EE>.AbstractEdgeEndpoint implements MutableCloudGraph.EdgeEndpoint<N, E>
	{
		private E target;

		protected AbstractEdgeEndpoint(final E target)
		{
			this.target = target;
		}

		@Override
		public void setTarget(E newTarget) throws InvalidGraphException
		{
			if(this.target != null && this.target.equals(newTarget) )
			{
				this.target = newTarget;
				return;
			}

			internalLeaveEdge(this);
			this.target = newTarget;
			internalJoinEdge(this);
		}

		@Override
		public E getTarget()
		{
			return this.target;
		}
	};
}