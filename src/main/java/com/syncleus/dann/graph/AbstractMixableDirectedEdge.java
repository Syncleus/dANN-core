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

public abstract class AbstractMixableDirectedEdge<
	    E extends MixableDirectedEdge.Endpoint<?>,
	  	SE extends E,
	  	DE extends E
	  > extends AbstractMixableBidirectedEdge<E,SE,DE> implements MixableDirectedEdge<E,SE,DE>
{
	private static final long serialVersionUID = -5892401329886611386L;

	@Override
	public final SE getLeftEndpoint()
	{
		return this.getSourceEndpoint();
	}

	@Override
	public final DE getRightEndpoint()
	{
		return this.getDestinationEndpoint();
	}

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
		return this.getSourceEndpoint().getTarget() + "->" + this.getDestinationEndpoint().getTarget();
	}

	@Override
	protected AbstractMixableDirectedEdge<E,SE,DE> clone()
	{
		return (AbstractMixableDirectedEdge<E,SE,DE>) super.clone();
	}

	protected abstract class AbstractEndpoint<T> extends AbstractMixableBidirectedEdge<E,SE,DE>.AbstractEndpoint<T> implements MixableDirectedEdge.Endpoint<T>
	{
		protected AbstractEndpoint(Direction direction)
		{
			super(direction);
		}

		protected AbstractEndpoint(T target, Direction direction)
		{
			super(target,direction);
		}
	};
}