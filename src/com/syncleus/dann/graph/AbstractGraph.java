/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph;

import java.util.Set;

public abstract class AbstractGraph<N, E extends Edge<? extends N>, W extends Walk<? extends N, ? extends E>> implements Graph<N,E,W>
{
	public int getDegree(N node)
	{
		return 0;
	}
	
	public boolean isConnected()
	{
		return false;
	}

	public Set<Graph<N,E,W>> getConnectedComponents()
	{
		return null;
	}

	public boolean isMaximalConnected()
	{
		return false;
	}

	public boolean isCut(Graph<? extends N, ? extends E, ? extends W> subGraph)
	{
		return false;
	}

	public boolean isCut(Graph<? extends N, ? extends E, ? extends W> subGraph, N begin, N end)
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

	public boolean isSubGraph(Graph<? extends N, ? extends E, ? extends W> graph)
	{
		return false;
	}

	public boolean isKnot(Graph<? extends N, ? extends E, ? extends W> subGraph)
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

	public boolean isIsomorphic(Graph<? extends N, ? extends E, ? extends W> isomorphicGraph)
	{
		return false;
	}

	public boolean isHomomorphic(Graph<? extends N, ? extends E, ? extends W> homomorphicGraph)
	{
		return false;
	}

	public boolean isRegular()
	{
		return false;
	}
}
