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

public abstract class AbstractMixableUndirectedEdge<
	    E extends MixableBidirectedEdge.Endpoint<?>,
	  	LE extends E,
	  	RE extends E
	  > extends AbstractMixableBidirectedEdge<E,LE,RE> implements MixableBidirectedEdge<E,LE,RE>
{
	private static final long serialVersionUID = 20943589023542L;

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
		return false;
	}

	@Override
	public final boolean isHalfEdge()
	{
		return false;
	}

	@Override
	public final boolean isLooseEdge()
	{
		return true;
	}

	@Override
	public final boolean isOrdinaryEdge()
	{
		return false;
	}

	@Override
	protected AbstractMixableUndirectedEdge<E,LE,RE> clone()
	{
		return (AbstractMixableUndirectedEdge<E,LE,RE>) super.clone();
	}
}

