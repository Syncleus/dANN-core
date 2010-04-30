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

import java.util.List;

public abstract class AbstractUniqueEdge<N> extends AbstractEdge<N>
{
	protected AbstractUniqueEdge(final List<N> nodes)
	{
		super(nodes);
	}

	protected AbstractUniqueEdge(final N... nodes)
	{
		super(nodes);
	}

	@Override
	public boolean equals(final Object compareToObj)
	{
		if(compareToObj == null)
			return false;

		if(!(compareToObj instanceof Edge))
			return false;

		final Edge compareTo = (Edge) compareToObj;
		return (compareTo.getNodes().equals(this.getNodes()))&&
			(this.getNodes().equals(compareTo.getNodes()));
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		for(N node : this.getNodes())
			hash += node.hashCode();
		return hash;
	}
}
