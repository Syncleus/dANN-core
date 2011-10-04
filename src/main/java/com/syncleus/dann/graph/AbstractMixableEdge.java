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

public abstract class AbstractMixableEdge<
	  	E extends MixableEdge.Endpoint<?>,
	  	LE extends E,
	  	RE extends E
	  > extends AbstractHyperedge<E> implements MixableEdge<E,LE,RE>
{
	@Override
	public Set<E> getEndpoints()
	{
		return new PairSet();
	}

	@Override
	public Set<E> getNeighbors(Endpoint<?> endpoint)
	{
		if(this.getLeftEndpoint().equals(endpoint))
			return Collections.singleton(this.getRightEndpoint());
		else if(this.getRightEndpoint().equals(endpoint))
			return Collections.singleton(this.getLeftEndpoint());
		else
			return Collections.emptySet();
	}

	private class PairSet extends AbstractSet<E>
	{
		@Override
		public Iterator<E> iterator()
		{
			return new PairIterator();
		}

		@Override
		public int size()
		{
			return 2;
		}

		@Override
		public boolean contains(Object o)
		{
			if( (getLeftEndpoint().equals(o)) || (getRightEndpoint().equals(o)) )
				return true;
			return false;
		}

		@Override
		public Endpoint<?>[] toArray()
		{
			return new Endpoint<?>[]{getLeftEndpoint(),getRightEndpoint()};
		}

		@Override
		public <T> T[] toArray(T[] a)
		{
			a[0] = (T) getLeftEndpoint();
			a[1] = (T) getRightEndpoint();
			return a;
		}

		@Override
		public boolean add(E nnEndpoint)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public boolean remove(Object o)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public boolean containsAll(Collection<?> c)
		{
			for(Object object : c)
				if( !this.contains(object) )
					return false;
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends E> c)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public boolean retainAll(Collection<?> c)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public boolean removeAll(Collection<?> c)
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		@Override
		public void clear()
		{
			throw new UnsupportedOperationException("This Set is read-only!");
		}

		private class PairIterator implements Iterator<E>
		{
			private Boolean beforeRight = false;

			@Override
			public boolean hasNext()
			{
				return (beforeRight != null);
			}

			@Override
			public E next()
			{
				if(beforeRight == null)
					throw new NoSuchElementException("no elements left");
				else if(!beforeRight)
				{
					beforeRight = true;
					return getLeftEndpoint();
				}
				else
				{
					beforeRight = null;
					return getRightEndpoint();
				}
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("This is a read-only iterator!");
			}
		}
	}

	protected abstract class AbstractEndpoint<T> extends AbstractCloud<E>.AbstractEndpoint<T>
	{
		protected AbstractEndpoint()
		{
			super();
		}

		protected AbstractEndpoint(T target)
		{
			super(target);
		}
	}
}