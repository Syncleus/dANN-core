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

import org.apache.log4j.Logger;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

@XmlJavaTypeAdapter( com.syncleus.dann.xml.XmlSerializableAdapter.class )
public abstract class AbstractCloudGraph<
	  	NE extends CloudGraph.NodeEndpoint<?>,
	  	EE extends CloudGraph.EdgeEndpoint<? extends Cloud<?>>
	  > extends AbstractCloud<NE> implements CloudGraph<NE, EE>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractCloudGraph.class);

/*
	protected PathFinder<N,E> getPathFinder()
	{
		return new DijkstraPathFinder<N,E>(this);
	}

	@Override
	public Set<N> getNodes()
	{
		final Set<N> nodes = new HashSet<N>();
		for(NE destinationEndpoint : this.getNodeEndpoints() )
			nodes.add(destinationEndpoint.getTarget());
		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public Set<E> getEdges()
	{
		final Set<E> edges = new HashSet<E>();
		for(EE destinationEndpoint : this.getEdgeEndpoints() )
			edges.add(destinationEndpoint.getTarget());
		return Collections.unmodifiableSet(edges);
	}
*/

	@Override
	public final boolean areEdgesFinite()
	{
		return true;
	}

	@Override
	public Set<EE> getEdgeEndpoints(Cloud<?> cloud)
	{
		Set<EE> matchingEndpoints  = new HashSet<EE>();
		for(final EE endpoint : this.getEdgeEndpoints() )
		{
			if( endpoint.getTarget().equals(cloud))
				matchingEndpoints.add(endpoint);
		}

		return Collections.unmodifiableSet(matchingEndpoints);
	}

	@Override
	public boolean containsEdgeTarget(final Cloud<?> edge)
	{
		for( EE endpoint : this.getEdgeEndpoints() )
			if( endpoint.getTarget().equals(edge))
				return true;
		return false;
	}

	@Override
	public boolean containsAllEdgeTargets(final Collection<? extends Cloud<?>> edges)
	{
		for( Cloud<?> edge : edges )
			if( !this.containsEdgeTarget(edge) )
				return false;
		return true;
	}

	@Override
	public boolean containsAnyEdgeTargets(final Collection<? extends Cloud<?>> edges)
	{
		for( Cloud<?> edge : edges )
			if( this.containsTarget(edge) )
				return true;
		return false;
	}

	@Override
	public boolean containsEdge(final Cloud.Endpoint<?> endpoint)
	{
		for( EE otherEndpoint : this.getEdgeEndpoints() )
			if( otherEndpoint.equals(endpoint))
				return true;
		return false;
	}

	@Override
	public boolean containsAllEdges(final Collection<? extends Cloud.Endpoint<?>> endpoints)
	{
		for( Cloud.Endpoint<?> endpoint : endpoints )
			if( !this.containsEdge(endpoint) )
				return false;
		return true;
	}

	@Override
	public boolean containsAnyEdges(final Collection<? extends Cloud.Endpoint<?>> endpoints)
	{
		for( Cloud.Endpoint<?> endpoint : endpoints )
			if( this.containsEdge(endpoint) )
				return true;
		return false;
	}

/*
	@Override
	public Set<N> getAdjacentNodes(Object node)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(NE destinationEndpoint : this.getNodeEndpoints(node) )
			for(CloudGraph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getAdjacentEdges())
				for(CloudGraph.NodeEndpoint<? extends N, ? extends E> nodeEndpoint : sourceEndpoint.getAdjacentNodes())
					sourceNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}

	@Override
	public Set<E> getAdjacentEdges(Object node)
	{
		final Set<E> sourceEdges = new HashSet<E>();

		for(NE destinationEndpoint : this.getNodeEndpoints(node) )
			for(CloudGraph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getAdjacentEdges())
				sourceEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceEdges);
	}

	@Override
	public Set<CloudGraph.Endpoint<?, N, E>> getEndpoints()
	{
		final Set<CloudGraph.Endpoint<?, N,E>> endpoints = new HashSet<CloudGraph.Endpoint<?, N,E>>();
		endpoints.addAll(this.getNodeEndpoints());
		endpoints.addAll(this.getEdgeEndpoints());
		return Collections.<CloudGraph.Endpoint<?, N, E>>unmodifiableSet(endpoints);
	}


	@Override
	public boolean isTraversable(Object source, Object destination)
	{
		return this.isTraversable(source, destination, this.getPathFinder());
	}

	private boolean isTraversable(Object source, Object destination, final PathFinder pathFinder)
	{
		if(this.getNodes().contains(source) && this.getNodes().contains(destination))
			return this.getPathFinder().isReachable((N)source, (N)destination);
		//edges are handled recusively
		else if(this.getEdges().contains(source))
		{
			final E sourceEdge = (E) source;
			final Set<N> sourceNodes = new HashSet<N>();
			for(Endpoint<? extends N> endpoint : sourceEdge.getEndpoints())
				if((endpoint.getTraversableNeighborsFrom().size() > 0)&&(!sourceNodes.contains(endpoint.getTarget())))
				{
					if(this.isTraversable(endpoint.getTarget(), destination))
						return true;
					sourceNodes.add(endpoint.getTarget());
				}
			return false;
		}
		else if(this.getEdges().contains(destination))
		{
			final E destinationEdge = (E) destination;
			final Set<N> destinationNodes = new HashSet<N>();
			for(Endpoint<? extends N> endpoint : destinationEdge.getEndpoints())
				if((endpoint.isTraversable())&&(!destinationNodes.contains(endpoint.getTarget())))
				{
					if(this.isTraversable(source, endpoint.getTarget()))
						return true;
					destinationNodes.add(endpoint.getTarget());
				}
			return false;
		}
		else
			throw new IllegalArgumentException("both source and destination need to be contained within this graph");
	}

	@Override
	public Set<E> getTraversableEdgesFrom(Object source)
	{
		if( !this.getNodes().contains(source) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<E> traversables = new HashSet<E>();
		for(E neighbor : this.getEdges())
			if( this.isTraversable(source, neighbor, pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<E> getTraversableEdgesFrom(Cloud<?,? extends Cloud.Endpoint<?>> source)
	{
		if( !this.getEdges().contains(source) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<E> traversables = new HashSet<E>();
		for(E neighbor : this.getEdges())
			if( this.isTraversable(source,neighbor,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<E> getTraversableEdgesTo(Object destination)
	{
		if( !this.getNodes().contains(destination) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<E> traversables = new HashSet<E>();
		for(E neighbor : this.getEdges())
			if( this.isTraversable(neighbor,destination,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<E> getTraversableEdgesTo(Cloud<?,? extends Cloud.Endpoint<?>> destination)
	{
		if( !this.getEdges().contains(destination) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<E> traversables = new HashSet<E>();
		for(E neighbor : this.getEdges())
			if( this.isTraversable(neighbor,destination,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<N> getTraversableNodesFrom(Object source)
	{
		if( !this.getNodes().contains(source) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<N> traversables = new HashSet<N>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(source, neighbor, pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<N> getTraversableNodesFrom(Cloud<?,? extends Cloud.Endpoint<?>> source)
	{
		if( !this.getEdges().contains(source) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<N> traversables = new HashSet<N>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(source,neighbor,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<N> getTraversableNodesTo(Object destination)
	{
		if( !this.getNodes().contains(destination) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<N> traversables = new HashSet<N>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(neighbor, destination, pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<N> getTraversableNodesTo(Cloud<?,? extends Cloud.Endpoint<?>> destination)
	{
		if( !this.getEdges().contains(destination) )
			throw new IllegalArgumentException("source does not belong to this graph");

		final PathFinder<N,E> pathFinder = this.getPathFinder();

		final Set<N> traversables = new HashSet<N>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(neighbor, destination, pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public Set<E> getTraversableAdjacentEdgesFrom(Object source)
	{
		final Set<E> destinationEdges = new HashSet<E>();

		for(NE sourceEndpoint : this.getNodeEndpoints(source) )
			for(CloudGraph.EdgeEndpoint<? extends N, ? extends E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}

	@Override
	public Set<E> getTraversableAdjacentEdgesFrom(Cloud<?,? extends Cloud.Endpoint<?>> source)
	{
		final Set<E> destinationEdges = new HashSet<E>();

		for(EE sourceEndpoint : this.getEdgeEndpoints(source) )
			for(CloudGraph.EdgeEndpoint<? extends N, ? extends E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}


	@Override
	public Set<E> getTraversableAdjacentEdgesTo(Object destination)
	{
		final Set<E> sourceEdges = new HashSet<E>();

		for(NE destinationEndpoint : this.getNodeEndpoints(destination) )
			for(CloudGraph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesTo())
				sourceEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceEdges);
	}

	@Override
	public Set<E> getTraversableAdjacentEdgesTo(Cloud<?,? extends Cloud.Endpoint<?>> destination)
	{
		final Set<E> destinationEdges = new HashSet<E>();

		for(EE destinationEndpoint : this.getEdgeEndpoints(destination) )
			for(CloudGraph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesFrom(Object source)
	{
		final Set<N> destinationNodes = new HashSet<N>();

		for(NE sourceEndpoint : this.getNodeEndpoints(source) )
			for(CloudGraph.EdgeEndpoint<? extends N, ? extends E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesTo())
				for(CloudGraph.NodeEndpoint<? extends N, ? extends E> nodeEndpoint : destinationEndpoint.getTraversableAdjacentNodesTo())
					destinationNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesFrom(Cloud<?,? extends Cloud.Endpoint<?>> source)
	{
		final Set<N> destinationNodes = new HashSet<N>();

		for(EE sourceEndpoint : this.getEdgeEndpoints(source) )
			for(CloudGraph.NodeEndpoint<? extends N, ? extends E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentNodesTo())
					destinationNodes.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesTo(Object destination)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(NE destinationEndpoint : this.getNodeEndpoints(destination) )
			for(CloudGraph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesFrom())
				for(CloudGraph.NodeEndpoint<? extends N, ? extends E> nodeEndpoint : sourceEndpoint.getTraversableAdjacentNodesFrom())
					sourceNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesTo(Cloud<?,? extends Cloud.Endpoint<?>> destination)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(EE destinationEndpoint : this.getEdgeEndpoints(destination) )
			for(CloudGraph.NodeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentNodesFrom())
					sourceNodes.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}
*/

	/**
	 * Clones the current object.
	 * @return A clone of the current object, with no changes
	 */
	@Override
	protected AbstractCloudGraph<NE, EE> clone()
	{
		return (AbstractCloudGraph<NE, EE>) super.clone();
	}

	protected class Endpoints implements CloudGraph.Endpoints<NE,EE>
	{
		private final Set<NE> nodeEndpoints;
		private final Set<EE> edgeEndpoints;

		public Endpoints(final Set<NE> nodeEndpoints, final Set<EE> edgeEndpoints)
		{
			this.nodeEndpoints = nodeEndpoints;
			this.edgeEndpoints = edgeEndpoints;
		}

		@Override
		public Set<NE> getNodeEndpoints()
		{
			return nodeEndpoints;
		}

		@Override
		public Set<EE> getEdgeEndpoints()
		{
			return edgeEndpoints;
		}
	}

	protected abstract class AbstractNodeEndpoint<T> extends AbstractCloud<NE>.AbstractEndpoint<T> implements CloudGraph.NodeEndpoint<T>
	{

		protected AbstractNodeEndpoint()
		{
		}

		protected AbstractNodeEndpoint(T target)
		{
			super(target);
		}

		@Override
		public final boolean isNodeEndpoint()
		{
			return true;
		}

		@Override
		public boolean isEdgeEndpoint()
		{
			return false;
		}

/*
		@Override
		public Set<CloudGraph.EdgeEndpoint<N, E>> getAdjacentEdges()
		{
			return getAdjacentEdgeEndpoints(this);
		}

		@Override
		public Set<? extends CloudGraph.EdgeEndpoint<N, E>> getAdjacent()
		{
			return getAdjacentEdges();
		}

		@Override
		public Set<? extends CloudGraph.EdgeEndpoint<N, E>> getTraversableAdjacentTo()
		{
			return this.getTraversableAdjacentEdgesTo();
		}

		@Override
		public Set<? extends CloudGraph.EdgeEndpoint<N, E>> getTraversableAdjacentFrom()
		{
			return this.getTraversableAdjacentEdgesFrom();
		}

		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getAdjacentNodes()
		{
			final Set<CloudGraph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<CloudGraph.NodeEndpoint<N, E>>();

			for(CloudGraph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					for( Cloud.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getNeighbors() )
						adjacentNodes.addAll(AbstractCloudGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.<CloudGraph.NodeEndpoint<N, E>>unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getTraversableAdjacentNodesTo()
		{
			final Set<CloudGraph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<CloudGraph.NodeEndpoint<N, E>>();

			for(CloudGraph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					for( Cloud.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getTraversableNeighborsTo() )
						adjacentNodes.addAll(AbstractCloudGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getTraversableAdjacentNodesFrom()
		{
			final Set<CloudGraph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<CloudGraph.NodeEndpoint<N, E>>();

			for(CloudGraph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					for( Cloud.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getTraversableNeighborsFrom() )
						adjacentNodes.addAll(AbstractCloudGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<CloudGraph.EdgeEndpoint<N, E>> getTraversableAdjacentEdgesTo()
		{
			final Set<CloudGraph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<CloudGraph.EdgeEndpoint<N, E>>();

			for(CloudGraph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					if( nodeEndpoint.isTraversable() )
						adjacentEdges.add(adjacentEndpoint);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<CloudGraph.EdgeEndpoint<N, E>> getTraversableAdjacentEdgesFrom()
		{
			final Set<CloudGraph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<CloudGraph.EdgeEndpoint<N, E>>();

			for(CloudGraph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					if( nodeEndpoint.isTraversable() )
						adjacentEdges.add(adjacentEndpoint);

			return Collections.unmodifiableSet(adjacentEdges);
		}
*/
	};

	protected abstract class AbstractEdgeEndpoint<T extends Cloud<?>> extends AbstractCloud<NE>.AbstractEndpoint<T> implements CloudGraph.EdgeEndpoint<T>
	{
		protected AbstractEdgeEndpoint()
		{
		}

		protected AbstractEdgeEndpoint(T target)
		{
			super(target);
		}

		@Override
		public boolean isNodeEndpoint()
		{
			return false;
		}

		@Override
		public final boolean isEdgeEndpoint()
		{
			return true;
		}

/*
		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getAdjacent()
		{
			return this.getAdjacentNodes();
		}

		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getTraversableAdjacentTo()
		{
			return this.getTraversableAdjacentNodesTo();
		}

		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getTraversableAdjacentFrom()
		{
			return this.getTraversableAdjacentNodesFrom();
		}

		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getAdjacentNodes()
		{
			final Set<CloudGraph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<CloudGraph.NodeEndpoint<N, E>>();

			for(Endpoint<? extends N> adjacentEndpoint : this.getTarget().getEndpoints())
				adjacentNodes.addAll(AbstractCloudGraph.this.getNodeEndpoints(adjacentEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<CloudGraph.EdgeEndpoint<N, E>> getAdjacentEdges()
		{
			final Set<CloudGraph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<CloudGraph.EdgeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				for( NE neighborNode : AbstractCloudGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()) )
					adjacentEdges.addAll(AbstractCloudGraph.this.getAdjacentEdgeEndpoints(neighborNode));

			adjacentEdges.remove(this);
			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<CloudGraph.EdgeEndpoint<N, E>> getTraversableAdjacentEdgesTo()
		{
			final Set<CloudGraph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<CloudGraph.EdgeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				if( sourceEndpoint.getTraversableNeighborsFrom().size() > 0 )
					for( NE adjacentNode : AbstractCloudGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()))
						for( CloudGraph.EdgeEndpoint<N, E> adjacentEdge : AbstractCloudGraph.this.getAdjacentEdgeEndpoints(adjacentNode) )
						 	if( adjacentEdge.getTarget().getTraversableFrom(adjacentNode.getTarget()).size() > 0 )
								 adjacentEdges.add(adjacentEdge);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<CloudGraph.EdgeEndpoint<N, E>> getTraversableAdjacentEdgesFrom()
		{
			final Set<CloudGraph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<CloudGraph.EdgeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				if( sourceEndpoint.getTraversableNeighborsTo().size() > 0 )
					for( NE adjacentNode : AbstractCloudGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()))
						for( CloudGraph.EdgeEndpoint<N, E> adjacentEdge : AbstractCloudGraph.this.getAdjacentEdgeEndpoints(adjacentNode) )
						 	if( adjacentEdge.getTarget().getTraversableTo(adjacentNode.getTarget()).size() > 0 )
								 adjacentEdges.add(adjacentEdge);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getTraversableAdjacentNodesTo()
		{
			final Set<CloudGraph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<CloudGraph.NodeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				if( sourceEndpoint.getTraversableNeighborsFrom().size() > 0 )
					adjacentNodes.addAll(AbstractCloudGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<CloudGraph.NodeEndpoint<N, E>> getTraversableAdjacentNodesFrom()
		{
			final Set<CloudGraph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<CloudGraph.NodeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				if( sourceEndpoint.getTraversableNeighborsTo().size() > 0 )
					adjacentNodes.addAll(AbstractCloudGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}
*/
	};
}
