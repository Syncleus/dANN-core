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
	  	NE extends MutableCloudGraph.NodeEndpoint<? super N>,
	  	E extends Cloud<? extends Cloud.Endpoint<? extends N>>,
	  	EE extends MutableCloudGraph.EdgeEndpoint<E>
	  >
	  extends AbstractCloudGraph<NE, EE>
	  implements MutableCloudGraph<N, NE, E, EE>
{
	private static final long serialVersionUID = -4613327727609060678L;
	private final AdjacencyMapping<NE,EE,Cloud.Endpoint<? extends N>> adjacency = new HashAdjacencyMapping<NE,EE,Cloud.Endpoint<? extends N>>();

	protected AbstractMutableAdjacencyGraph()
	{
	}

	protected void internalJoin(final NE endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));
		else if( this.contains(endpoint) )
			throw new IllegalArgumentException("endpoint is already a member of this graph!");

		this.adjacency.putLeftKey(endpoint);
	}

	protected void internalLeave(Cloud.Endpoint<?> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));
		else if( !this.adjacency.getLeftKeys().contains(endpoint) )
			throw new IllegalArgumentException("endpoint is not an endpoint in this graph");
		else if( this.getAdjacentEdgeEndpoints(endpoint).size() > 0)
			throw new InvalidGraphException("Node must be an orphan in the graph to remove it");

		this.adjacency.removeLeftKey(endpoint);
	}

	protected void internalJoinEdge(final EE endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));
		//make sure all of the edge's node already belong to this graph
        for(Endpoint<? extends N> targetEndpoint : endpoint.getTarget().getEndpoints())
            if( !this.containsTarget(targetEndpoint.getTarget()) )
                throw new InvalidGraphException("All of the edge's targets must already exist in this graph");
		//we cant do anything if the endpoint already exists in the key, they are unique representations of
		//a single membership to the graph
		if(this.contains(endpoint))
			throw new IllegalArgumentException("endpoint is already a member of this graph!");

		for( final Cloud.Endpoint<? extends N> targetEndpoint : endpoint.getTarget().getEndpoints() )
			for( final NE nodeEndpoint : this.getEndpoints(targetEndpoint.getTarget()))
				this.adjacency.put(nodeEndpoint,endpoint,targetEndpoint);
	}


	protected void internalLeaveEdge(Cloud.Endpoint<?> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));
		else if( !this.adjacency.getRightKeys().contains(endpoint) )
			throw new IllegalArgumentException("endpoint is not an endpoint in this graph");

		this.adjacency.removeRightKey(endpoint);
	}

    protected void internalAssignEdge(Cloud.Endpoint<?> endpoint, N newTarget) throws InvalidGraphException
    {
        if(endpoint == null)
            throw new IllegalArgumentException(("endpoint can not be null"));
        else if( !this.adjacency.getRightKeys().contains(endpoint) )
            throw new IllegalArgumentException("endpoint is not an endpoint in this graph");

        this.adjacency.

        this.adjacency.removeRightKey(endpoint);
    }

//	@Override
	protected Set<EE> getAdjacentEdgeEndpoints(Cloud.Endpoint<?> nodeEndpoint)
	{
		if( !this.adjacency.getLeftKeys().contains(nodeEndpoint) )
			throw new IllegalArgumentException("nodeEndpoint is not an endpoint for this graph");
		return Collections.<EE>unmodifiableSet(this.adjacency.getLeftAdjacency(nodeEndpoint));
	}

	@Override
	public void leave(Cloud.Endpoint<?> endpoint) throws InvalidGraphException
	{
		this.internalLeave(endpoint);
	}

	@Override
	public void leaveEdge(Cloud.Endpoint<?> endpoint) throws InvalidGraphException
	{
		this.internalLeaveEdge(endpoint);
	}

	@Override
	public Set<NE> joins(Set<? extends N> nodes) throws InvalidGraphException
	{
		final Set<NE> endpoints = new HashSet<NE>();
		for(N node : nodes)
			endpoints.add(this.join(node));
		return Collections.unmodifiableSet(endpoints);
	}

	@Override
	public Set<NE> joins(Map<? extends N, ? extends Integer> nodes) throws InvalidGraphException
	{
		final Set<NE> joinSet = new HashSet<NE>(nodes.size());
		for(final Map.Entry<? extends N,? extends Integer> nodeEntry : nodes.entrySet())
		{
			final Set<NE> newEndpoints = new HashSet<NE>(nodeEntry.getValue());
			for(int count = 0; count < nodeEntry.getValue(); count++)
				newEndpoints.add(this.join(nodeEntry.getKey()));
			joinSet.addAll(newEndpoints);
		}
		return Collections.unmodifiableSet(joinSet);
	}

	@Override
	public void leave(Set<? extends Cloud.Endpoint<?>> nodeEndpoints) throws InvalidGraphException
	{
		for(Cloud.Endpoint<?> node : nodeEndpoints)
			this.leave(node);
	}

	@Override
	public Set<EE> joinEdges(Set<? extends E> edges) throws InvalidGraphException
	{
		final Set<EE> endpoints = new HashSet<EE>();
		for(E edge : edges)
			endpoints.add(this.joinEdge(edge));
		return Collections.unmodifiableSet(endpoints);
	}

	@Override
	public Set<EE> joinEdges(Map<? extends E, ? extends Integer> edges) throws InvalidGraphException
	{
		final Set<EE> joinSet = new HashSet<EE>(edges.size());
		for(final Map.Entry<? extends E,? extends Integer> edgeEntry : edges.entrySet())
		{
			final Set<EE> newEndpoints = new HashSet<EE>(edgeEntry.getValue());
			for(int count = 0; count < edgeEntry.getValue(); count++)
				newEndpoints.add(this.joinEdge(edgeEntry.getKey()));
			joinSet.addAll(newEndpoints);
		}
		return Collections.unmodifiableSet(joinSet);
	}

	@Override
	public void leaveEdges(Set<? extends Cloud.Endpoint<?>> edgeEndpoints) throws InvalidGraphException
	{
		for(final Cloud.Endpoint<?> edgeEndpoint : edgeEndpoints)
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
    public CloudGraph.Endpoints<NE,EE> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, Set<? extends Cloud.Endpoint<?>> disconnectEndpoints) throws InvalidGraphException
	{
		if( disconnectEndpoints != null )
		{
			for(final Cloud.Endpoint<?> disconnectEndpoint : disconnectEndpoints)
				this.adjacency.remove(disconnectEndpoint.getTarget());
		}

		//Map<Object, CloudGraph.Endpoint<?, N,E>> newEndpoints = new HashMap<Object, CloudGraph.Endpoint<?, N, E>>();
        Set<NE> newNodeEndpoints = new HashSet<NE>();
		if( addNodes != null )
			newNodeEndpoints.addAll(this.joins(addNodes));

        Set<EE> newEdgeEndpoints = new HashSet<EE>();
		if( addEdges != null )
			newEdgeEndpoints.addAll(this.joinEdges(addEdges));

		return new Endpoints(Collections.unmodifiableSet(newNodeEndpoints), Collections.unmodifiableSet(newEdgeEndpoints) );
	}

    @Override
    public CloudGraph.Endpoints<NE,EE> reconfigure(Map<? extends N,? extends Integer> addNodes, Map<? extends E,? extends Integer> addEdges, Set<? extends Cloud.Endpoint<?>> disconnectEndpoints) throws InvalidGraphException
	{
		if( disconnectEndpoints != null )
		{
			for(final Cloud.Endpoint<?> disconnectEndpoint : disconnectEndpoints)
				this.adjacency.remove(disconnectEndpoint.getTarget());
		}

		//Map<Object, CloudGraph.Endpoint<?, N,E>> newEndpoints = new HashMap<Object, CloudGraph.Endpoint<?, N, E>>();
        Set<NE> newNodeEndpoints = new HashSet<NE>();
		if( addNodes != null )
			newNodeEndpoints.addAll(this.joins(addNodes));

        Set<EE> newEdgeEndpoints = new HashSet<EE>();
		if( addEdges != null )
			newEdgeEndpoints.addAll(this.joinEdges(addEdges));

		return new Endpoints(Collections.unmodifiableSet(newNodeEndpoints), Collections.unmodifiableSet(newEdgeEndpoints) );
	}

	@Override
	public Set<EE> getEdgeEndpoints()
	{
		return Collections.unmodifiableSet(this.adjacency.getRightKeys());
	}

	@Override
	public Set<NE> getEndpoints()
	{
		return Collections.unmodifiableSet(this.adjacency.getLeftKeys());
	}

	@Override
	public boolean isContextEnabled()
	{
		return false;
	}


	protected abstract class AbstractNodeEndpoint<N> extends AbstractCloudGraph<NE,EE>.AbstractNodeEndpoint<N> implements MutableCloudGraph.NodeEndpoint<N>
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

			internalLeave(this);
			this.target = newTarget;
			internalJoin(this);
		}

		@Override
		public N getTarget()
		{
			return this.target;
		}
	};

	protected abstract class AbstractEdgeEndpoint extends AbstractCloudGraph<NE,EE>.AbstractEdgeEndpoint implements MutableCloudGraph.EdgeEndpoint<E>
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