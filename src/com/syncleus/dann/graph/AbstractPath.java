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

public abstract class AbstractPath<N, E extends Edge<N>> extends AbstractWalk<N,E> implements Path<N,E>
{
	@Override
	protected boolean verify(List<N> nodeSteps, List<E> edgeSteps)
	{
		if( (super.verify(nodeSteps, edgeSteps)) && (verifyUtility(nodeSteps, edgeSteps)) )
			return true;
		return false;
	}

	static <N, E extends Edge<N>> boolean verifyUtility(List<N> nodeSteps, List<E> edgeSteps)
	{
		if(nodeSteps.size()<2)
			throw new IllegalArgumentException("Wrong number of nodes or steps");
		if(nodeSteps.get(0).equals(nodeSteps.get(nodeSteps.size()-1)))
			return false;

		return true;
	}

	public boolean isChain()
	{
		return isChain(this);
	}

	protected static <N, E extends Edge<N>> boolean isChain(Path<N,E> path)
	{
		Set<N> uniqueNodes = new HashSet<N>(path.getNodeSteps());
		Set<E> uniqueEdges = new HashSet<E>(path.getSteps());
		if( uniqueNodes.size() < path.getNodeSteps().size())
			return false;
		if( uniqueEdges.size() < path.getSteps().size())
			return false;
		return true;
	}

	public boolean isIndependent(Path<N,E> path)
	{
		return AbstractPath.isIndependentUtility(this, path);
	}

	static <N, E extends Edge<N>> boolean isIndependentUtility(Path<N,E> firstPath, Path<N,E> secondPath)
	{
		if( !firstPath.getFirstNode().equals(secondPath.getFirstNode()) )
			return false;
		if( !firstPath.getLastNode().equals(secondPath.getLastNode()) )
			return false;

		List<N> exclusiveFirstNodes = new ArrayList<N>(firstPath.getNodeSteps());
		exclusiveFirstNodes.remove(exclusiveFirstNodes.size()-1);
		exclusiveFirstNodes.remove(0);

		List<N> secondNodes = new ArrayList<N>(secondPath.getNodeSteps());
		secondNodes.remove(secondNodes.size()-1);
		secondNodes.remove(0);

		exclusiveFirstNodes.removeAll(secondNodes);

		if( exclusiveFirstNodes.size() < firstPath.getNodeSteps().size())
			return false;
		return true;
	}

	@Override
	public boolean isCycle()
	{
		return false;
	}
	
	static int hashCodeUtility(Path path)
	{
		return (path.getNodeSteps().hashCode() + path.getSteps().hashCode()) * path.getSteps().hashCode();
	}
	
	static boolean equalsUtility(Path path, Object object)
	{
		Path secondPath = (Path)object;
		if(! (secondPath.getNodeSteps().equals(path.getNodeSteps())) )
			return false;
		if(! (secondPath.getSteps().equals(path.getSteps())) )
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		return AbstractPath.hashCodeUtility(this);
	}

	@Override
	public boolean equals(Object object)
	{
		if(!(object instanceof Path))
			return false;
		return AbstractPath.equalsUtility(this, object);
	}
}
