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
	  	T,
	  	EP extends TraversableCloud.Endpoint<T,? extends T>
	  > extends AbstractCloud<T, EP> implements TraversableCloud<T,EP>
{
	@Override
	public Set<T> getTraversableFrom(Object source)
	{
		final Set<T> nodes = new HashSet<T>();
		for( final EP sourceEndpoint : this.getEndpoints(source) )
			for( final TraversableCloud.Endpoint<T,? extends T> fromEndpoint : sourceEndpoint.getTraversableNeighborsFrom())
				nodes.add(fromEndpoint.getTarget());

		return Collections.unmodifiableSet(nodes);
	}

	@Override
	public Set<T> getTraversableTo(Object destination)
	{
		final Set<T> nodes = new HashSet<T>();
		for( final EP destinationEndpoint : this.getEndpoints(destination) )
			for( final TraversableCloud.Endpoint<T,? extends T> fromEndpoint : destinationEndpoint.getTraversableNeighborsTo())
				nodes.add(fromEndpoint.getTarget());

		return Collections.unmodifiableSet(nodes);
	}

	protected abstract class AbstractEndpoint<P,T> extends AbstractEndpoint<P,T> implements TraversableCloud.Endpoint<P,T>
	{
		@Override
		public Set<TraversableCloud.Endpoint<P,P>> getTraversableNeighborsTo()
		{
			final Set<TraversableCloud.Endpoint<P,P>> traversables = new HashSet<TraversableCloud.Endpoint<P,P>>();
			for(EP neighbor : AbstractTraversableCloud.this.getEndpoints())
				if( AbstractTraversableCloud.this.isTraversable(this.getTarget(),neighbor.getTarget()) )
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
	}
}
