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

import java.util.Set;

public abstract class AbstractGraph<G extends Graph<? extends G, ? extends N, ? extends E, ? extends W>, N extends Node<? extends E>, E extends Edge<? extends N>, W extends Walk<? extends N, ? extends E>> implements Graph<G, N, E, W>
{
	public boolean isConnected(N begin, N end)
	{
		return false;
	}

	public boolean isConnected()
	{
		return false;
	}

	public Set<G> getConnectedComponents()
	{
		return null;
	}

	public boolean isMaximalConnected()
	{
		return false;
	}

	public boolean isCut(G subGraph)
	{
		return false;
	}

	public boolean isCut(G subGraph, N begin, N end)
	{
		return false;
	}

	public int getNodeConnectivity()
	{
		return 0;
	}

	public int getEdgeConnectivity()
	{
		return 0;
	}

	public int getNodeConnectivity(N begin, N end)
	{
		return 0;
	}

	public int getEdgeConnectivity(N begin, N end)
	{
		return 0;
	}

	public boolean isCompleteGraph()
	{
		return false;
	}

	public boolean isReachable(N begin, N end)
	{
		return false;
	}

	public W getShortestPath(N begin, N end)
	{
		return null;
	}

	public int getOrder()
	{
		return 0;
	}

	public int getCycleCount()
	{
		return 0;
	}

	public boolean isPancyclic()
	{
		return false;
	}

	public int getGirth()
	{
		return 0;
	}

	public int getCircumference()
	{
		return 0;
	}

	public boolean isTraceable()
	{
		return false;
	}

	public boolean isSpanning(W walk)
	{
		return false;
	}

	public boolean isSpanning(TreeGraph graph)
	{
		return false;
	}

	public boolean isTraversable()
	{
		return false;
	}

	public boolean isEularian(W walk)
	{
		return false;
	}

	public boolean isTree()
	{
		return false;
	}

	public boolean isSubGraph(G graph)
	{
		return false;
	}

	public boolean isKnot(G subGraph)
	{
		return false;
	}

	public int getTotalDegree()
	{
		return 0;
	}

	public boolean isMultigraph()
	{
		return false;
	}

	public boolean isIsomorphic(G isomorphicGraph)
	{
		return false;
	}

	public boolean isHomomorphic(G homomorphicGraph)
	{
		return false;
	}

	public boolean isRegular()
	{
		return false;
	}
}
