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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDirectedNode<E extends DirectedEdge> extends AbstractBidirectedNode<E> implements DirectedNode<E>
{
	@Override
	public List<E> getTraversableEdges()
	{
		return this.getOutEdges();
	}

	@Override
	public List<E> getOutEdges()
	{
		List<E> allEdges = this.getEdges();
		List<E> outEdges = new ArrayList<E>();
		for(E edge : allEdges)
			if(edge.getSourceNode().equals(this))
				outEdges.add(edge);

		return Collections.unmodifiableList(outEdges);
	}

	@Override
	public List<E> getInEdges()
	{
		List<E> allEdges = this.getEdges();
		List<E> outEdges = new ArrayList<E>();
		for(E edge : allEdges)
			if(edge.getDestinationNode().equals(this))
				outEdges.add(edge);

		return Collections.unmodifiableList(outEdges);
	}

	@Override
	public int getIndegree()
	{
		return this.getInEdges().size();
	}

	@Override
	public int getOutdegree()
	{
		return this.getOutEdges().size();
	}
}
