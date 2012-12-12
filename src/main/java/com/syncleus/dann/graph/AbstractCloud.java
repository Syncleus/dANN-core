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
import org.apache.log4j.Logger;

public abstract class AbstractCloud<
	  	E extends Cloud.Endpoint<?>
	  > implements Cloud<E>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractCloud.class);

	@Override
	public final boolean isFinite()
	{
		return true;
	}

	@Override
	public boolean containsTarget(final Object node)
	{
		for( E endpoint : this.getEndpoints() )
			if( endpoint.getTarget().equals(node))
				return true;
		return false;
	}

	@Override
	public boolean containsAllTargets(final Collection<?> nodes)
	{
		for( Object node : nodes )
			if( !this.containsTarget(node) )
				return false;
		return true;
	}

	@Override
	public boolean containsAnyTargets(final Collection<?> nodes)
	{
		for( Object node : nodes )
			if( this.containsTarget(node) )
				return true;
		return false;
	}

	@Override
	public boolean contains(final Cloud.Endpoint<?> endpoint)
	{
		for( E otherEndpoint : this.getEndpoints() )
			if( otherEndpoint.equals(endpoint))
				return true;
		return false;
	}

	@Override
	public boolean containsAll(final Collection<? extends Cloud.Endpoint<?>> endpoints)
	{
		for( Endpoint<?> endpoint : endpoints )
			if( !this.contains(endpoint) )
				return false;
		return true;
	}

	@Override
	public boolean containsAny(final Collection<? extends Cloud.Endpoint<?>> endpoints)
	{
		for( Endpoint<?> endpoint : endpoints )
			if( this.contains(endpoint) )
				return true;
		return false;
	}

	@Override
	public Set<E> getEndpoints(Object node)
	{
		final Set<E> nodesEndpoints = new HashSet<E>();
		for( E endpoint : this.getEndpoints() )
			if( endpoint.getTarget().equals(node))
				nodesEndpoints.add(endpoint);

		return Collections.unmodifiableSet(nodesEndpoints);
	}

	@Override
	public boolean areNeighbors(Cloud.Endpoint<?> neighbor, Cloud.Endpoint<?> otherNeighbor)
	{
		return this.getNeighbors(neighbor).contains(otherNeighbor);
	}

	@Override
	public int getDegree()
	{
		return this.getEndpoints().size();
	}

	@Override
	public String toString()
	{
		final StringBuilder outString = new StringBuilder(this.getDegree() * 10);
		for(final E endpoint : this.getEndpoints())
		{
			outString.append(':').append(endpoint);
		}
		return outString.toString();
	}

	@Override
	protected AbstractCloud<E> clone()
	{
		try
		{
			return (AbstractCloud<E>) super.clone();
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("Cloud was unexpectidly not cloneable", caught);
			throw new Error("Cloud was unexpectidly not cloneable", caught);
		}
	}

	// TODO : Clean this
/*
	@Override
	public EdgeXml toXml()
	{
		final Namer<Object> namer = new Namer();
		final EdgeElementXml xml = new EdgeElementXml();

		xml.setNodeInstances(new EdgeElementXml.NodeInstances());
		final Set<E> writtenNodes = new HashSet<E>();
		for (E endpoint : this.getEndpoints())
		{
			if (writtenNodes.add(endpoint))
			{
				final NamedValueXml named = new NamedValueXml();
				named.setName(namer.getNameOrCreate(endpoint));
				if (endpoint instanceof XmlSerializable)
				{
					named.setValue(((XmlSerializable) endpoint).toXml(namer));
				}
				else
				{
					named.setValue(endpoint);
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
*/

	private class NeighborSet implements Set<E>
	{
		private final E target;

		public NeighborSet(E target)
		{
			this.target = target;
		}

		@Override
		public int size()
		{
			return getDegree() - 1;
		}

		@Override
		public boolean isEmpty()
		{
			assert !getEndpoints().isEmpty();
			return ( getDegree() <= 1 ? true : false );
		}

		@Override
		public boolean contains(Object o)
		{
			if( this.target.equals(o) )
				return false;
			return getEndpoints().contains(o);
		}

		@Override
		public Iterator<E> iterator()
		{
			return new NeighborIterator();
		}

		@Override
		public Object[] toArray()
		{
			final Set<E> copiedNodes = new HashSet<E>(getEndpoints());
			copiedNodes.remove(this.target);
			return copiedNodes.toArray();
		}

		@Override
		public <PA> PA[] toArray(PA[] a)
		{
			final Set<E> copiedNodes = new HashSet<E>(getEndpoints());
			copiedNodes.remove(this.target);
			return copiedNodes.toArray(a);
		}

		@Override
		public boolean add(E nEndpoint)
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
			if( c.contains(this.target) )
				return false;
			return getEndpoints().containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends E> c)
		{
			throw new UnsupportedOperationException("This set is read-only!");
		}

		@Override
		public boolean retainAll(Collection<?> c)
		{
			if( this.containsAll(c) )
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
			if( this.size() <= 0 )
				return;
			throw new UnsupportedOperationException("This set is read-only!");
		}

		private class NeighborIterator implements Iterator<E>
		{
			private int nextLeft = (getEndpoints().size()-1);
			final private Iterator<E> iterator;

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
			public E next()
			{
				E nextEndpoint = this.iterator.next();
				this.nextLeft--;

				if( !target.equals(nextEndpoint) )
					return nextEndpoint;
				return this.iterator.next();
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("This iterator is read-only!");
			}
		};
	};

	protected abstract class AbstractEndpoint<T> implements Cloud.Endpoint<T>
	{
		private T target = null;

		protected AbstractEndpoint()
		{
		}

		protected AbstractEndpoint(T target)
		{
			this.target = target;
		}

		protected abstract boolean isTargetEquals();

		@Override
		public T getTarget()
		{
			return this.target;
		}

		protected void setTarget(final T target) throws InvalidGraphException
		{
			this.target = target;
		}

		/**
		 * By default this relies on the target to define equals, this means there can be only one instance of any
		 * endpoint. This should be overridden to allow for an edge to have the same element more than once.
		 */
		@Override
		public int hashCode()
		{
			if(!this.isTargetEquals())
				return super.hashCode();
			else if(this.getTarget() == null)
				return 0;
			else
				return this.getTarget().hashCode();
		}

		/**
		 * By default this relies on the target to define equals, this means there can be only one instance of any
		 * endpoint. This should be overridden to allow for an edge to have the same element more than once.
		 */
		@Override
		public boolean equals(Object obj)
		{
			if(!this.isTargetEquals())
				return super.equals(obj);
			else if( obj == null )
				if( this.getTarget() == null )
					return true;
				else
					return false;
			else if( this.getTarget() == null)
				return false;
			else if( obj instanceof Cloud.Endpoint )
				return ((Cloud.Endpoint<?>)obj).equals(this.getTarget());
			else
				return this.getTarget().equals(obj);
		}
	};
}
