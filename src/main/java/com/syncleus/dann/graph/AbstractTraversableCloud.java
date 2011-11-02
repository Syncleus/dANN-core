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

public abstract class AbstractTraversableCloud<
	  	E extends TraversableCloud.Endpoint<?>
	  > extends AbstractCloud<E> implements TraversableCloud<E>
{
	@Override
	public Set<E> getTraversableFrom(Cloud.Endpoint<?> source)
	{
		final Set<E> traversableEndpoints = new HashSet<E>();
		for( final E toEndpoint : getNeighbors(source))
			if( this.isTraversable(source, toEndpoint) )
				traversableEndpoints.add(toEndpoint);

		return Collections.unmodifiableSet(traversableEndpoints);
	}

	@Override
	public Set<E> getTraversableTo(Cloud.Endpoint<?> destination)
	{
		final Set<E> traversableEndpoints = new HashSet<E>();
		for( final E fromEndpoint : getNeighbors(destination) )
			if( this.isTraversable(fromEndpoint, destination))
				traversableEndpoints.add(fromEndpoint);

		return Collections.unmodifiableSet(traversableEndpoints);
	}

	@Override
	public boolean isTraversableFrom(Cloud.Endpoint<?> source)
	{
		return !this.getTraversableFrom(source).isEmpty();
	}

	@Override
	public boolean isTraversableTo(Cloud.Endpoint<?> destination)
	{
		return !this.getTraversableTo(destination).isEmpty();
	}

	protected abstract class AbstractEndpoint<T> extends AbstractCloud<E>.AbstractEndpoint<T> implements TraversableCloud.Endpoint<T>
	{
		protected AbstractEndpoint()
		{
			super();
		}

/*
		@Override
		public Set<TraversableCloud.Endpoint<T>> TraversableNeighborsTo()
		{
			final Set<TraversableCloud.Endpoint<P,P>> traversables = new HashSet<TraversableCloud.Endpoint<P,P>>();
			for(E neighbor : AbstractTraversableCloud.this.getEndpoints())
				if( AbstractTraversableCloud.this.isTraversable(this.getTarget(),neighbor.getTarget()) )
					traversables.add(neighbor);
			return null;
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
*/

		@Override
		public boolean isTraversableFrom(Cloud.Endpoint<?> target)
		{
			return isTraversable(target, this);
		}

		@Override
		public boolean isTraversableTo(Cloud.Endpoint<?> target)
		{
			return isTraversable(this, target);
		}

		@Override
		public boolean isTraversableFrom()
		{
			return isTraversableFrom(this);
		}

		@Override
		public boolean isTraversableTo()
		{
			return isTraversableTo(this);
		}
	}
}
