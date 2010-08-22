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

public abstract class AbstractContextGraphElement<N, E extends Edge<N>, G extends Graph<N,E>> implements ContextGraphElement<N,E,G>
{
	private final boolean allowJoiningMultipleGraphs;
	private final Set<G> joinedGraphs = new HashSet<G>();

	protected AbstractContextGraphElement(final boolean allowJoiningMultipleGraphs)
	{
		this.allowJoiningMultipleGraphs = allowJoiningMultipleGraphs;
	}

	@Override
	public boolean isGraphMember()
	{
		return (!this.joinedGraphs.isEmpty());
	}

	@Override
	public Set<G> getJoinedGraphs()
	{
		return Collections.unmodifiableSet(this.joinedGraphs);
	}

	@Override
	public boolean joiningGraph(G graph)
	{
		if( graph == null )
			throw new IllegalArgumentException("graph can not be null");

		if( !this.allowJoiningMultipleGraphs && (joinedGraphs.size() > 0) )
			return false;

		this.joinedGraphs.add(graph);
		return true;
	}

	@Override
	public boolean leavingGraph(G graph)
	{
		if( graph == null )
			throw new IllegalArgumentException("graph can not be null");
		if( !this.joinedGraphs.contains(graph) )
			throw new IllegalArgumentException("graph was never joined");

		this.joinedGraphs.remove(graph);
		return true;
	}

	@Override
	public boolean isAllowingMultipleGraphs()
	{
		return allowJoiningMultipleGraphs;
	}
}
