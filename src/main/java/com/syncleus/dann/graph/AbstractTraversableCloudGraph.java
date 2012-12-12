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

import java.util.HashSet;
import java.util.Set;
import com.syncleus.dann.graph.search.pathfinding.*;

public abstract class AbstractTraversableCloudGraph<
        NE extends TraversableCloudGraph.NodeEndpoint<?>,
        EE extends TraversableCloudGraph.EdgeEndpoint<? extends Cloud<?>>
        > extends AbstractCloudGraph<NE,EE> implements TraversableCloudGraph<NE, EE>
{
	private final CloudTraverser<? super Cloud<?>> traverser;

	protected AbstractTraversableCloudGraph()
	{
		this.traverser = null;
	}

	protected AbstractTraversableCloudGraph(final CloudTraverser<? super Cloud<?>> traverser)
	{
		this.traverser = traverser;
	}

	public CloudTraverser<? super Cloud<?>> getCloudTraverser()
	{
		return this.traverser;
	}

	@Override
	public CloudGraph.Endpoints<NE, EE> getGraphTraversableFrom(Cloud.Endpoint<?> source)
	{
		if( !this.contains(source) )
			throw new IllegalArgumentException("target does not belong to this graph as a node");

		this.getAdjacent(source);

		final WalkFinder<?> pathFinder = this.getWalkFinder();

		final Set<NE> traversables = new HashSet<NE>();
		for(NE neighbor : this.getNodes())
			if( this.isTraversable(source,neighbor,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public CloudGraph.Endpoints<NE, EE> getGraphTraversableTo(Cloud.Endpoint<?> destination)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public CloudGraph.Endpoints<NE, EE> getTraversableAdjacentTo(Cloud.Endpoint<?> destination)
	{
		if( !this.contains(destination) )
			throw new IllegalArgumentException("target does not belong to this graph as a node");

		CloudGraph.Endpoints<NE,EE> adjacentEndpoints = this.getAdjacent(destination);

		Set<EE> traversableAdjacentEdges = new HashSet<EE>();
		for( EE adjacentEdgeEndpoint : adjacentEndpoints.getEdgeEndpoints() )
		{
traversableAdjacentEdges.add(adjacentEdgeEndpoint);
		}

		final Set<NE> traversables = new HashSet<NE>();
		for(N neighbor : this.getNodes())
			if( this.isTraversable(source,neighbor,pathFinder) )
				traversables.add(neighbor);
		return Collections.unmodifiableSet(traversables);
	}

	@Override
	public CloudGraph.Endpoints<NE, EE> getTraversableAdjacentFrom(Cloud.Endpoint<?> destination)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Set<NE> getTraversableFrom(Cloud.Endpoint<?> source)
	{
		return this.getGraphTraversableFrom(source).getNodeEndpoints();
	}

	@Override
	public Set<NE> getTraversableTo(Cloud.Endpoint<?> destination)
	{
		return this.getGraphTraversableTo(destination).getNodeEndpoints();
	}

	@Override
	public boolean isTraversable(Cloud.Endpoint<?> sourceTarget, Cloud.Endpoint<?> destinationTarget)
	{
			if(this.getCloudTraverser() != null)
				if(this.getCloudTraverser().isTraversable(this,sourceTarget,destinationTarget))
					return true;
				else
					return false
			else
				if()
	}

	@Override
	public boolean isTraversableFrom(Cloud.Endpoint<?> source)
	{
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isTraversableTo(Cloud.Endpoint<?> destination)
	{
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}


	protected abstract class AbstractNodeEndpoint<T> extends AbstractCloudGraph<NE,EE>.AbstractNodeEndpoint<T> implements TraversableCloudGraph.NodeEndpoint<T>
	{

		protected AbstractNodeEndpoint()
		{
		}

		protected AbstractNodeEndpoint(T target)
		{
			super(target);
		}
	};

	protected abstract class AbstractEdgeEndpoint<T extends Cloud<?>> extends AbstractCloudGraph<NE,EE>.AbstractEdgeEndpoint<T> implements TraversableCloudGraph.EdgeEndpoint<T>
	{
		protected AbstractEdgeEndpoint()
		{
		}

		protected AbstractEdgeEndpoint(T target)
		{
			super(target);
		}
	};
}
