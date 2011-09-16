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

import com.syncleus.dann.graph.search.pathfinding.DijkstraPathFinder;
import com.syncleus.dann.graph.search.pathfinding.PathFinder;
import com.syncleus.dann.graph.xml.*;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;
import org.apache.log4j.Logger;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * An AbstractAdjacencyGraph is a Graph implemented using adjacency lists.
 * @param <N> The node type
 * @param <E> The type of edge for the given node type
 */
@XmlJavaTypeAdapter( com.syncleus.dann.xml.XmlSerializableAdapter.class )
public abstract class AbstractAdjacencyGraph<
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N>>,
	  	NE extends Graph.NodeEndpoint<N, E>,
	  	EE extends Graph.EdgeEndpoint<N, E>
	  > extends AbstractCloud<Object,Graph.Endpoint<?, N,E>> implements Graph<N, E, NE, EE>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractAdjacencyGraph.class);
	protected abstract Set<EdgeEndpoint<N,E>> getAdjacentEdgeEndpoints(Graph.NodeEndpoint<?, ?> nodeEndpoint);

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

	@Override
	public Set<EE> getEdgeEndpoints(Cloud<?,? extends Cloud.Endpoint<?>> cloud)
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
	public Set<NE> getNodeEndpoints(Object node)
	{
		Set<NE> matchingEndpoints  = new HashSet<NE>();
		for(NE endpoint : this.getNodeEndpoints() )
		{
			if( endpoint.getTarget().equals(node))
				matchingEndpoints.add(endpoint);
		}

		return Collections.unmodifiableSet(matchingEndpoints);
	}

	@Override
	public Set<N> getAdjacentNodes(Object node)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(NE destinationEndpoint : this.getNodeEndpoints(node) )
			for(Graph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getAdjacentEdges())
				for(Graph.NodeEndpoint<? extends N, ? extends E> nodeEndpoint : sourceEndpoint.getAdjacentNodes())
					sourceNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}

	@Override
	public Set<E> getAdjacentEdges(Object node)
	{
		final Set<E> sourceEdges = new HashSet<E>();

		for(NE destinationEndpoint : this.getNodeEndpoints(node) )
			for(Graph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getAdjacentEdges())
				sourceEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceEdges);
	}

	@Override
	public Set<Graph.Endpoint<?, N, E>> getEndpoints()
	{
		final Set<Graph.Endpoint<?, N,E>> endpoints = new HashSet<Graph.Endpoint<?, N,E>>();
		endpoints.addAll(this.getNodeEndpoints());
		endpoints.addAll(this.getEdgeEndpoints());
		return Collections.<Graph.Endpoint<?, N, E>>unmodifiableSet(endpoints);
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
			for(Graph.EdgeEndpoint<? extends N, ? extends E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}

	@Override
	public Set<E> getTraversableAdjacentEdgesFrom(Cloud<?,? extends Cloud.Endpoint<?>> source)
	{
		final Set<E> destinationEdges = new HashSet<E>();

		for(EE sourceEndpoint : this.getEdgeEndpoints(source) )
			for(Graph.EdgeEndpoint<? extends N, ? extends E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}


	@Override
	public Set<E> getTraversableAdjacentEdgesTo(Object destination)
	{
		final Set<E> sourceEdges = new HashSet<E>();

		for(NE destinationEndpoint : this.getNodeEndpoints(destination) )
			for(Graph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesTo())
				sourceEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceEdges);
	}

	@Override
	public Set<E> getTraversableAdjacentEdgesTo(Cloud<?,? extends Cloud.Endpoint<?>> destination)
	{
		final Set<E> destinationEdges = new HashSet<E>();

		for(EE destinationEndpoint : this.getEdgeEndpoints(destination) )
			for(Graph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesFrom())
				destinationEdges.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationEdges);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesFrom(Object source)
	{
		final Set<N> destinationNodes = new HashSet<N>();

		for(NE sourceEndpoint : this.getNodeEndpoints(source) )
			for(Graph.EdgeEndpoint<? extends N, ? extends E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentEdgesTo())
				for(Graph.NodeEndpoint<? extends N, ? extends E> nodeEndpoint : destinationEndpoint.getTraversableAdjacentNodesTo())
					destinationNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesFrom(Cloud<?,? extends Cloud.Endpoint<?>> source)
	{
		final Set<N> destinationNodes = new HashSet<N>();

		for(EE sourceEndpoint : this.getEdgeEndpoints(source) )
			for(Graph.NodeEndpoint<? extends N, ? extends E> destinationEndpoint : sourceEndpoint.getTraversableAdjacentNodesTo())
					destinationNodes.add(destinationEndpoint.getTarget());

		return Collections.unmodifiableSet(destinationNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesTo(Object destination)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(NE destinationEndpoint : this.getNodeEndpoints(destination) )
			for(Graph.EdgeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentEdgesFrom())
				for(Graph.NodeEndpoint<? extends N, ? extends E> nodeEndpoint : sourceEndpoint.getTraversableAdjacentNodesFrom())
					sourceNodes.add(nodeEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}

	@Override
	public Set<N> getTraversableAdjacentNodesTo(Cloud<?,? extends Cloud.Endpoint<?>> destination)
	{
		final Set<N> sourceNodes = new HashSet<N>();

		for(EE destinationEndpoint : this.getEdgeEndpoints(destination) )
			for(Graph.NodeEndpoint<? extends N, ? extends E> sourceEndpoint : destinationEndpoint.getTraversableAdjacentNodesFrom())
					sourceNodes.add(sourceEndpoint.getTarget());

		return Collections.unmodifiableSet(sourceNodes);
	}

	/**
	 * Clones the current object.
	 * @return A clone of the current object, with no changes
	 */
	@Override
	protected AbstractAdjacencyGraph<N, E, NE, EE> clone()
	{
		return (AbstractAdjacencyGraph<N, E, NE, EE>) super.clone();
	}

	/**
	 * Converts the current AbstractAdjacencyGraph to a GraphXML.
	 * @return The GraphXML representation of this AbstractAdjacencyGraph
	 */
	@Override
	public GraphXml toXml()
	{
		final GraphElementXml xml = new GraphElementXml();
		final Namer<Object> namer = new Namer<Object>();

		xml.setNodeInstances(new GraphElementXml.NodeInstances());
		for(N node : this.getNodes())
		{
			final String nodeName = namer.getNameOrCreate(node);

			final Object nodeXml;
			if(node instanceof XmlSerializable)
				nodeXml = ((XmlSerializable)node).toXml(namer);
			else
				//if the object isnt XmlSerializable lets try to just serialize it as a regular JAXB object
				nodeXml = node;

			final NamedValueXml encapsulation = new NamedValueXml();
			encapsulation.setName(nodeName);
			encapsulation.setValue(nodeXml);

			xml.getNodeInstances().getNodes().add(encapsulation);
		}

		this.toXml(xml, namer);
		return xml;
	}

	/**
	 * Converts a given Namer to its GraphXML representation.
	 * @param namer The namer to convert
	 * @return The GraphXML representation of this namer
	 */
	@Override
	public GraphXml toXml(final Namer<Object> namer)
	{
		if(namer == null)
			throw new IllegalArgumentException("namer can not be null");

		final GraphXml xml = new GraphXml();
		this.toXml(xml, namer);
		return xml;
	}
/*
	/**
	 * Adds a current Namer to the given GraphXML object.
	 * @param jaxbObject The graph to add the object to
	 * @param namer THe namer to add to the GraphXML
	 */
/*
	@Override
	public void toXml(final GraphXml jaxbObject, final Namer<Object> namer)
	{
		if(namer == null)
			throw new IllegalArgumentException("nodeNames can not be null");
		if(jaxbObject == null)
			throw new IllegalArgumentException("jaxbObject can not be null");

		for(N node : this.getNodes())
		{
			final String nodeName = namer.getNameOrCreate(node);

			final Object nodeXml;
			if(node instanceof XmlSerializable)
				nodeXml = ((XmlSerializable)node).toXml(namer);
			else
				// if the object isnt XmlSerializable lets try to just serialize
				// it as a regular JAXB object
				nodeXml = node;

			final NameXml encapsulation = new NameXml();
			encapsulation.setName(nodeName);

			if( jaxbObject.getNodes() == null )
				jaxbObject.setNodes(new GraphXml.Nodes());
			jaxbObject.getNodes().getNodes().add(encapsulation);
		}

		for(E edge : this.getEdges())
		{
			final EdgeXml edgeXml = edge.toXml(namer);

			if( jaxbObject.getEdges() == null )
				jaxbObject.setEdges(new GraphXml.Edges());
			jaxbObject.getEdges().getEdges().add(edgeXml);
		}
	}
*/

	protected abstract class AbstractNodeEndpoint extends AbstractCloud<? super N,Graph.Endpoint<? super N, N,E>>.AbstractEndpoint<N> implements Graph.NodeEndpoint<N,E>
	{

		@Override
		public Set<Graph.EdgeEndpoint<N, E>> getAdjacentEdges()
		{
			return getAdjacentEdgeEndpoints(this);
		}

		@Override
		public Set<? extends Graph.EdgeEndpoint<N, E>> getAdjacent()
		{
			return getAdjacentEdges();
		}

		@Override
		public Set<? extends Graph.EdgeEndpoint<N, E>> getTraversableAdjacentTo()
		{
			return this.getTraversableAdjacentEdgesTo();
		}

		@Override
		public Set<? extends Graph.EdgeEndpoint<N, E>> getTraversableAdjacentFrom()
		{
			return this.getTraversableAdjacentEdgesFrom();
		}

		@Override
		public Set<Graph.NodeEndpoint<N, E>> getAdjacentNodes()
		{
			final Set<Graph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<N, E>>();

			for(Graph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					for( Cloud.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getNeighbors() )
						adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.<Graph.NodeEndpoint<N, E>>unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.NodeEndpoint<N, E>> getTraversableAdjacentNodesTo()
		{
			final Set<Graph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<N, E>>();

			for(Graph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					for( Cloud.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getTraversableNeighborsTo() )
						adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.NodeEndpoint<N, E>> getTraversableAdjacentNodesFrom()
		{
			final Set<Graph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<N, E>>();

			for(Graph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					for( Cloud.Endpoint<? extends N> adjacentNodeEndpoint : nodeEndpoint.getTraversableNeighborsFrom() )
						adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(adjacentNodeEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.EdgeEndpoint<N, E>> getTraversableAdjacentEdgesTo()
		{
			final Set<Graph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<N, E>>();

			for(Graph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					if( nodeEndpoint.isTraversable() )
						adjacentEdges.add(adjacentEndpoint);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<Graph.EdgeEndpoint<N, E>> getTraversableAdjacentEdgesFrom()
		{
			final Set<Graph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<N, E>>();

			for(Graph.EdgeEndpoint<N, E> adjacentEndpoint : this.getAdjacentEdges() )
				for( Cloud.Endpoint<? extends N> nodeEndpoint : adjacentEndpoint.getTarget().getEndpoints(this.getTarget()) )
					if( nodeEndpoint.isTraversable() )
						adjacentEdges.add(adjacentEndpoint);

			return Collections.unmodifiableSet(adjacentEdges);
		}
	};

	protected abstract class AbstractEdgeEndpoint extends AbstractCloud<? super E,Graph.Endpoint<? super E, N,E>>.AbstractEndpoint<E> implements Graph.EdgeEndpoint<N,E>
	{
		@Override
		public Set<Graph.NodeEndpoint<N, E>> getAdjacent()
		{
			return this.getAdjacentNodes();
		}

		@Override
		public Set<Graph.NodeEndpoint<N, E>> getTraversableAdjacentTo()
		{
			return this.getTraversableAdjacentNodesTo();
		}

		@Override
		public Set<Graph.NodeEndpoint<N, E>> getTraversableAdjacentFrom()
		{
			return this.getTraversableAdjacentNodesFrom();
		}

		@Override
		public Set<Graph.NodeEndpoint<N, E>> getAdjacentNodes()
		{
			final Set<Graph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<N, E>>();

			for(Endpoint<? extends N> adjacentEndpoint : this.getTarget().getEndpoints())
				adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(adjacentEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.EdgeEndpoint<N, E>> getAdjacentEdges()
		{
			final Set<Graph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				for( NE neighborNode : AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()) )
					adjacentEdges.addAll(AbstractAdjacencyGraph.this.getAdjacentEdgeEndpoints(neighborNode));

			adjacentEdges.remove(this);
			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<Graph.EdgeEndpoint<N, E>> getTraversableAdjacentEdgesTo()
		{
			final Set<Graph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				if( sourceEndpoint.getTraversableNeighborsFrom().size() > 0 )
					for( NE adjacentNode : AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()))
						for( Graph.EdgeEndpoint<N, E> adjacentEdge : AbstractAdjacencyGraph.this.getAdjacentEdgeEndpoints(adjacentNode) )
						 	if( adjacentEdge.getTarget().getTraversableFrom(adjacentNode.getTarget()).size() > 0 )
								 adjacentEdges.add(adjacentEdge);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<Graph.EdgeEndpoint<N, E>> getTraversableAdjacentEdgesFrom()
		{
			final Set<Graph.EdgeEndpoint<N, E>> adjacentEdges = new HashSet<Graph.EdgeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				if( sourceEndpoint.getTraversableNeighborsTo().size() > 0 )
					for( NE adjacentNode : AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()))
						for( Graph.EdgeEndpoint<N, E> adjacentEdge : AbstractAdjacencyGraph.this.getAdjacentEdgeEndpoints(adjacentNode) )
						 	if( adjacentEdge.getTarget().getTraversableTo(adjacentNode.getTarget()).size() > 0 )
								 adjacentEdges.add(adjacentEdge);

			return Collections.unmodifiableSet(adjacentEdges);
		}

		@Override
		public Set<Graph.NodeEndpoint<N, E>> getTraversableAdjacentNodesTo()
		{
			final Set<Graph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				if( sourceEndpoint.getTraversableNeighborsFrom().size() > 0 )
					adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}

		@Override
		public Set<Graph.NodeEndpoint<N, E>> getTraversableAdjacentNodesFrom()
		{
			final Set<Graph.NodeEndpoint<N, E>> adjacentNodes = new HashSet<Graph.NodeEndpoint<N, E>>();

			for(Endpoint<? extends N> sourceEndpoint : this.getTarget().getEndpoints())
				if( sourceEndpoint.getTraversableNeighborsTo().size() > 0 )
					adjacentNodes.addAll(AbstractAdjacencyGraph.this.getNodeEndpoints(sourceEndpoint.getTarget()));

			return Collections.unmodifiableSet(adjacentNodes);
		}
	};
}
