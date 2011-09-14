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

public abstract class AbstractDirectedEdge<N, SN extends N, DN extends N> extends AbstractBidirectedEdge<N, SN,DN> implements MixableDirectedEdge<N, SN,DN>
{
	private static final long serialVersionUID = -7589242369886611386L;

	@Override
	public final AbstractSourceEndpoint getLeftEndPoint()
	{
		return this.getSourceEndPoint();
	}

	@Override
	public final AbstractDestinationEndpoint getRightEndPoint()
	{
		return this.getDestinationEndPoint();
	}

	@Override
	public abstract AbstractSourceEndpoint getSourceEndPoint();

	@Override
	public abstract AbstractDestinationEndpoint getDestinationEndPoint();

	@Override
	public final boolean isIntroverted()
	{
		return false;
	}

	@Override
	public final boolean isExtroverted()
	{
		return false;
	}

	@Override
	public final boolean isDirected()
	{
		return true;
	}

	@Override
	public final boolean isHalfEdge()
	{
		return false;
	}

	@Override
	public final boolean isLooseEdge()
	{
		return false;
	}

	@Override
	public final boolean isOrdinaryEdge()
	{
		return true;
	}

	@Override
	public String toString()
	{
		return this.getSourceEndPoint().getTarget() + "->" + this.getDestinationEndPoint().getTarget();
	}

	@Override
	protected AbstractDirectedEdge<N, SN,DN> clone()
	{
		return (AbstractDirectedEdge<N, SN,DN>) super.clone();
	}



	protected abstract class AbstractSourceEndpoint extends AbstractEndpoint<SN,DN> implements MixableDirectedEdge.Endpoint<N, SN,DN>
	{
		public AbstractSourceEndpoint()
		{
			super(Direction.INWARD);
		}

		public AbstractSourceEndpoint(SN node)
		{
			super(node, Direction.INWARD);
		}

		@Override
		public final MixableBidirectedEdge.Endpoint<N, DN,SN> getNeighbor()
		{
			return getDestinationEndPoint();
		}

		@Override
		public final Set<Endpoint<N, N>> getTraversableNeighborsTo()
		{
			return Collections.<Endpoint<N, N>>singleton(getDestinationEndPoint());
		}

		@Override
		public final Set<Endpoint<N, N>> getTraversableNeighborsFrom()
		{
			return Collections.emptySet();
		}

		@Override
		public final boolean isTraversable()
		{
			return true;
		}

		@Override
		public final boolean isTraversable(Endpoint<N, N> destination)
		{
			if( destination == null )
				throw new IllegalArgumentException("destination can not be null");

			return destination.equals(this.getNeighbor());
		}

		@Override
		public final boolean isTraversable(N destination)
		{
			if(destination == null)
			{
				if(this.getNeighbor().getTarget() == null)
					return true;
				else
					return false;
			}
			else if(this.getNeighbor().getTarget() == null)
				return false;

			return destination.equals(this.getNeighbor().getTarget());
		}
	};

	protected abstract class AbstractDestinationEndpoint extends AbstractEndpoint<DN,SN> implements MixableDirectedEdge.Endpoint<N, DN,SN>
	{
		public AbstractDestinationEndpoint()
		{
			super(Direction.OUTWARD);
		}

		public AbstractDestinationEndpoint(DN node)
		{
			super(node, Direction.OUTWARD);
		}

		@Override
		public final MixableBidirectedEdge.Endpoint<N, SN,DN> getNeighbor()
		{
			return getDestinationEndPoint();
		}

		@Override
		public final Set<Endpoint<N, N>> getTraversableNeighborsTo()
		{
			return Collections.emptySet();
		}

		@Override
		public final Set<Endpoint<N, N>> getTraversableNeighborsFrom()
		{
			return Collections.<Endpoint<N, N>>singleton(getSourceEndPoint());
		}

		@Override
		public final boolean isTraversable()
		{
			return false;
		}

		@Override
		public final boolean isTraversable(Endpoint<N, N> destination)
		{
			return false;
		}

		@Override
		public final boolean isTraversable(N destination)
		{
			return false;
		}
	};
}
