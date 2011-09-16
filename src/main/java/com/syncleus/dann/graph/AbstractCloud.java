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

public abstract class AbstractCloud<
	  	T,
	  	EP extends Cloud.Endpoint<? extends T>
	  > implements Cloud<T,EP>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractCloud.class);

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
	public Set<T> getTargets()
	{
		final Set<T> nodes = new HashSet<T>();
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
	public Set<T> getNeighbors(final Object source)
	{
		final Set<T> nodes = new HashSet<T>();
		for( final EP sourceEndpoint : this.getEndpoints(source) )
			for( final Cloud.Endpoint<? extends T> fromEndpoint : sourceEndpoint.getNeighbors())
				nodes.add(fromEndpoint.getTarget());

		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public Set<T> getTraversableFrom(Object source)
	{
		final Set<T> nodes = new HashSet<T>();
		for( final EP sourceEndpoint : this.getEndpoints(source) )
			for( final Cloud.Endpoint<? extends T> fromEndpoint : sourceEndpoint.getTraversableNeighborsFrom())
				nodes.add(fromEndpoint.getTarget());

		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public Set<T> getTraversableTo(Object destination)
	{
		final Set<T> nodes = new HashSet<T>();
		for( final EP destinationEndpoint : this.getEndpoints(destination) )
			for( final Cloud.Endpoint<? extends T> fromEndpoint : destinationEndpoint.getTraversableNeighborsTo())
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
		for(final T node : this.getTargets())
		{
			outString.append(':').append(node);
		}
		return outString.toString();
	}

	@Override
	protected AbstractCloud<T,EP> clone()
	{
		try
		{
			return (AbstractCloud<T,EP>) super.clone();
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("Cloud was unexpectidly not cloneable", caught);
			throw new Error("Cloud was unexpectidly not cloneable", caught);
		}
	}

	@Override
	public EdgeXml toXml()
	{
		final Namer namer = new Namer();
		final EdgeElementXml xml = new EdgeElementXml();

		xml.setNodeInstances(new EdgeElementXml.NodeInstances());
		final Set<T> writtenNodes = new HashSet<T>();
		for (T node : this.getTargets())
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
		for (T node : this.getTargets())
		{
			final NameXml connection = new NameXml();
			connection.setName(nodeNames.getNameOrCreate(node));
			jaxbObject.getConnections().getNodes().add(connection);
		}
	}

	protected abstract class AbstractEndpoint<T> implements Cloud.Endpoint<T>
	{
		protected AbstractEndpoint()
		{
		}

		@Override
		public Set<Cloud.Endpoint<? extends T>> getNeighbors()
		{
			return new NeighborSet();
		}

		@Override
		public Set<Cloud.Endpoint<? extends T>> getTraversableNeighborsTo()
		{
			final Set<Cloud.Endpoint<? extends T>> traversables = new HashSet<Cloud.Endpoint<? extends T>>();
			for(Cloud.Endpoint<? extends T> neighbor : this.getNeighbors())
				if( AbstractCloud.this.isTraversable(this.getTarget(),neighbor.getTarget()) )
					traversables.add(neighbor);
			return Collections.unmodifiableSet(traversables);
		}

		@Override
		public Set<Cloud.Endpoint<? extends T>> getTraversableNeighborsFrom()
		{
			final Set<Cloud.Endpoint<? extends T>> traversables = new HashSet<Cloud.Endpoint<? extends T>>();
			for(Cloud.Endpoint<? extends T> neighbor : this.getNeighbors())
				if( AbstractCloud.this.isTraversable(this.getTarget(),neighbor.getTarget()) )
					traversables.add(neighbor);
			return Collections.unmodifiableSet(traversables);
		}

		@Override
		public boolean isTraversable()
		{
			return (this.getTraversableNeighborsTo().size() > 0);
		}

		@Override
		public boolean isTraversable(Cloud.Endpoint<?> destination)
		{
			return AbstractCloud.this.isTraversable(this.getTarget(), destination.getTarget());
		}

		private class NeighborSet implements Set<Cloud.Endpoint<? extends T>>
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
			public Iterator<Cloud.Endpoint<? extends T>> iterator()
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
			public boolean add(Cloud.Endpoint<? extends T> nEndpoint)
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
			public boolean addAll(Collection<? extends Cloud.Endpoint<? extends T>> c)
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

			private class NeighborIterator implements Iterator<Cloud.Endpoint<? extends T>>
			{
				private int nextLeft = (getEndpoints().size()-1);
				final private Iterator<? extends Endpoint<?>> iterator;

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
				public Cloud.Endpoint<T> next()
				{
					Cloud.Endpoint<?> nextEndpoint = this.iterator.next();
					this.nextLeft--;

					if( !AbstractEndpoint.this.equals(nextEndpoint) )
						return (Cloud.Endpoint<T>) nextEndpoint;
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
