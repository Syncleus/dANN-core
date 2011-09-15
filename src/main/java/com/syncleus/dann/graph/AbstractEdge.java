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
import com.syncleus.dann.graph.xml.EdgeElementXml;
import com.syncleus.dann.graph.xml.EdgeXml;
import com.syncleus.dann.xml.NameXml;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;
import org.apache.log4j.Logger;

public abstract class AbstractEdge<
	  	PA,
	  	EP extends Edge.Endpoint<? extends PA>
	  > implements Edge<PA,EP>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractEdge.class);

	@Override
	public boolean contains(final Object node)
	{
		for( EP endpoint : this.getEndpoints() )
			if( endpoint.getTarget().equals(node))
				return true;
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> nodes)
	{
		for( Object node : nodes )
			if( !this.contains(node) )
				return false;
		return true;
	}

	@Override
	public boolean containsAny(final Collection<?> nodes)
	{
		for( Object node : nodes )
			if( this.contains(node) )
				return true;
		return false;
	}

	@Override
	public Set<PA> getTargets()
	{
		final Set<PA> nodes = new HashSet<PA>();
		for( EP endpoint : this.getEndpoints() )
			nodes.add(endpoint.getTarget());

		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public Set<EP> getEndpoints(Object node)
	{
		final Set<EP> nodesEndpoints = new HashSet<EP>();
		for( EP endpoint : this.getEndpoints() )
			if( endpoint.getTarget().equals(node))
				nodesEndpoints.add(endpoint);

		return Collections.unmodifiableSet(nodesEndpoints);
	}

	@Override
	public Set<PA> getNeighbors(final Object source)
	{
		final Set<PA> nodes = new HashSet<PA>();
		for( final EP sourceEndpoint : this.getEndpoints(source) )
			for( final Edge.Endpoint<? extends PA> fromEndpoint : sourceEndpoint.getNeighbors())
				nodes.add(fromEndpoint.getTarget());

		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public Set<PA> getTraversableFrom(Object source)
	{
		final Set<PA> nodes = new HashSet<PA>();
		for( final EP sourceEndpoint : this.getEndpoints(source) )
			for( final Edge.Endpoint<? extends PA> fromEndpoint : sourceEndpoint.getTraversableNeighborsFrom())
				nodes.add(fromEndpoint.getTarget());

		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public Set<PA> getTraversableTo(Object destination)
	{
		final Set<PA> nodes = new HashSet<PA>();
		for( final EP destinationEndpoint : this.getEndpoints(destination) )
			for( final Edge.Endpoint<? extends PA> fromEndpoint : destinationEndpoint.getTraversableNeighborsTo())
				nodes.add(fromEndpoint.getTarget());

		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public int getDegree()
	{
		return this.getTargets().size();
	}

	@Override
	public String toString()
	{
		final StringBuilder outString = new StringBuilder(this.getTargets().size() * 10);
		for(final PA node : this.getTargets())
		{
			outString.append(':').append(node);
		}
		return outString.toString();
	}

	@Override
	protected AbstractEdge<PA,EP> clone()
	{
		try
		{
			return (AbstractEdge<PA,EP>) super.clone();
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("Edge was unexpectidly not cloneable", caught);
			throw new Error("Edge was unexpectidly not cloneable", caught);
		}
	}

	@Override
	public EdgeXml toXml()
	{
		final Namer namer = new Namer();
		final EdgeElementXml xml = new EdgeElementXml();

		xml.setNodeInstances(new EdgeElementXml.NodeInstances());
		final Set<PA> writtenNodes = new HashSet<PA>();
		for (PA node : this.getTargets())
		{
			if (writtenNodes.add(node))
			{
				final NamedValueXml named = new NamedValueXml();
				named.setName(namer.getNameOrCreate(node));
				if (node instanceof XmlSerializable)
				{
					named.setValue(((XmlSerializable) node).toXml(namer));
				}
				else
				{
					named.setValue(node);
				}
				xml.getNodeInstances().getNodes().add(named);
			}
		}
		this.toXml(xml, namer);

		return xml;
	}

	@Override
	public EdgeXml toXml(final Namer<Object> nodeNames)
	{
		if (nodeNames == null)
		{
			throw new IllegalArgumentException("nodeNames can not be null");
		}

		final EdgeXml xml = new EdgeXml();
		this.toXml(xml, nodeNames);
		return xml;
	}

	@Override
	public void toXml(final EdgeXml jaxbObject, final Namer<Object> nodeNames)
	{
		if (nodeNames == null)
		{
			throw new IllegalArgumentException("nodeNames can not be null");
		}
		if (jaxbObject == null)
		{
			throw new IllegalArgumentException("jaxbObject can not be null");
		}

		if (jaxbObject.getConnections() == null)
		{
			jaxbObject.setConnections(new EdgeXml.Connections());
		}
		for (PA node : this.getTargets())
		{
			final NameXml connection = new NameXml();
			connection.setName(nodeNames.getNameOrCreate(node));
			jaxbObject.getConnections().getNodes().add(connection);
		}
	}

	protected abstract class AbstractEndpoint implements Edge.Endpoint<PA>
	{
		protected AbstractEndpoint()
		{
		}

		@Override
		public Set<Edge.Endpoint<PA>> getNeighbors()
		{
			return new NeighborSet();
		}

		@Override
		public Set<Edge.Endpoint<PA>> getTraversableNeighborsTo()
		{
			final Set<Edge.Endpoint<PA>> traversables = new HashSet<Edge.Endpoint<PA>>();
			for(Edge.Endpoint<PA> neighbor : this.getNeighbors())
				if( AbstractEdge.this.isTraversable(this.getTarget(),neighbor.getTarget()) )
					traversables.add(neighbor);
			return Collections.unmodifiableSet(traversables);
		}

		@Override
		public Set<Edge.Endpoint<PA>> getTraversableNeighborsFrom()
		{
			final Set<Edge.Endpoint<PA>> traversables = new HashSet<Edge.Endpoint<PA>>();
			for(Edge.Endpoint<PA> neighbor : this.getNeighbors())
				if( AbstractEdge.this.isTraversable(this.getTarget(),neighbor.getTarget()) )
					traversables.add(neighbor);
			return Collections.unmodifiableSet(traversables);
		}

		@Override
		public boolean isTraversable()
		{
			return (this.getTraversableNeighborsTo().size() > 0);
		}

		@Override
		public boolean isTraversable(Edge.Endpoint<?> destination)
		{
			return AbstractEdge.this.isTraversable(this.getTarget(), destination.getTarget());
		}

		@Override
		public boolean isTraversable(Object destination)
		{
			return AbstractEdge.this.isTraversable(this.getTarget(), destination);
		}

		private class NeighborSet implements Set<Edge.Endpoint<PA>>
		{
			@Override
			public int size()
			{
				return getTargets().size() - 1;
			}

			@Override
			public boolean isEmpty()
			{
				assert !getTargets().isEmpty();
				return ( getEndpoints().size() <= 1 ? true : false );
			}

			@Override
			public boolean contains(Object o)
			{
				if( getTarget().equals(o) )
					return false;
				return getEndpoints().contains(o);
			}

			@Override
			public Iterator<Edge.Endpoint<PA>> iterator()
			{
				return new NeighborIterator();
			}

			@Override
			public Object[] toArray()
			{
				final Set<EP> copiedNodes = new HashSet<EP>(getEndpoints());
				copiedNodes.remove(getTarget());
				return copiedNodes.toArray();
			}

			@Override
			public <PA> PA[] toArray(PA[] a)
			{
				final Set<EP> copiedNodes = new HashSet<EP>(getEndpoints());
				copiedNodes.remove(getTarget());
				return copiedNodes.toArray(a);
			}

			@Override
			public boolean add(Edge.Endpoint<PA> nEndpoint)
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
				if( c.contains(AbstractEndpoint.this) )
					return false;
				return getEndpoints().containsAll(c);
			}

			@Override
			public boolean addAll(Collection<? extends Edge.Endpoint<PA>> c)
			{
				throw new UnsupportedOperationException("This set is read-only!");
			}

			@Override
			public boolean retainAll(Collection<?> c)
			{
				if( c.containsAll(this) )
					return false;
				throw new UnsupportedOperationException("This set is read-only!");
			}

			@Override
			public boolean removeAll(Collection<?> c)
			{
				if( Collections.disjoint(this, c) )
					return false;
				throw new UnsupportedOperationException("This set is read-only!");
			}

			@Override
			public void clear()
			{
				if( getEndpoints().size() <= 1 )
					return;
				throw new UnsupportedOperationException("This set is read-only!");
			}

			private class NeighborIterator implements Iterator<Edge.Endpoint<PA>>
			{
				private int nextLeft = (getEndpoints().size()-1);
				final private Iterator<EP> iterator;

				public NeighborIterator()
				{
					this.iterator = getEndpoints().iterator();
				}

				@Override
				public boolean hasNext()
				{
					return (nextLeft > 0 ? true : false);
				}

				@Override
				public Edge.Endpoint<PA> next()
				{
					Edge.Endpoint<? extends PA> nextEndpoint = this.iterator.next();
					this.nextLeft--;

					if( !AbstractEndpoint.this.equals(nextEndpoint) )
						return (Edge.Endpoint<PA>) nextEndpoint;
					return next();
				}

				@Override
				public void remove()
				{
					throw new UnsupportedOperationException("This iterator is read-only!");
				}
			};
		};
	};
}
