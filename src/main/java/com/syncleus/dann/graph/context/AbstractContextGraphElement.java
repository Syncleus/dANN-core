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
package com.syncleus.dann.graph.context;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;
import com.syncleus.dann.graph.Graph;

public abstract class AbstractContextGraphElement<G extends Graph<?, ?>> implements ContextGraphElement<G>, Serializable
{
	private final boolean allowJoiningMultipleGraphs;
	private final Set<G> joinedGraphs = new HashSet<G>();

	protected AbstractContextGraphElement(final boolean allowJoiningMultipleGraphs)
	{
		this.allowJoiningMultipleGraphs = allowJoiningMultipleGraphs;
	}

	protected boolean isGraphMember()
	{
		return (!this.joinedGraphs.isEmpty());
	}

	protected Set<G> getJoinedGraphs()
	{
		return Collections.unmodifiableSet(this.joinedGraphs);
	}

	public void changingJoinedGraphs(Set<G> joiningGraphs, Set<G> leavingGraphs) throws RejectedContextException
	{
		if( ((joiningGraphs == null)||(joiningGraphs.isEmpty())) && ((leavingGraphs == null)||(leavingGraphs.isEmpty())) )
			throw new IllegalArgumentException("No graphs specified for joining or leaving!");

		if( (joiningGraphs != null) && (!joiningGraphs.isEmpty()) )
		{
			if( joiningGraphs.contains(null) )
				throw new IllegalArgumentException("joiningGraphs can not have a null element!");

			if( !this.allowJoiningMultipleGraphs && ((!this.joinedGraphs.isEmpty()) || (joiningGraphs.size() - leavingGraphs.size() > 1) ) )
				throw new RejectedContextException("Can not join multiple graphs at the same time!");
		}
		if( (leavingGraphs != null) && (!leavingGraphs.isEmpty()) )
		{
			if( leavingGraphs.contains(null) )
				throw new IllegalArgumentException("leavingGraphs can not have a null element!");

			if( !this.joinedGraphs.containsAll(leavingGraphs) )
				throw new IllegalArgumentException("One of the graphs being left has not been joined");
		}
	}

	public void changedJoinedGraphs(Set<G> newJoinedGraphs, Set<G> newLeftGraphs)
	{
		if( ((newJoinedGraphs == null)||(newJoinedGraphs.isEmpty())) && ((newLeftGraphs == null)||(newLeftGraphs.isEmpty())) )
			throw new IllegalArgumentException("No graphs specified for joining or leaving!");

		if( (newJoinedGraphs != null) && (!newJoinedGraphs.isEmpty()) )
		{
			if( newJoinedGraphs.contains(null) )
				throw new IllegalArgumentException("newJoinedGraphs can not have a null element!");

			this.joinedGraphs.addAll(newJoinedGraphs);
		}

		if( (newLeftGraphs != null) && (!newLeftGraphs.isEmpty()) )
		{
			if( newLeftGraphs.contains(null) )
				throw new IllegalArgumentException("newLeftGraphs can not have a null element!");

			this.joinedGraphs.removeAll(newLeftGraphs);
		}
	}

	protected boolean isAllowingMultipleGraphs()
	{
		return allowJoiningMultipleGraphs;
	}
}
