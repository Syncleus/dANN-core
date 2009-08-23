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

public abstract class AbstractHyperGraph<N extends HyperNode<E>, E extends HyperEdge, W extends HyperWalk<N, E>> extends AbstractGraph<N,E,W> implements HyperGraph<N,E,W>
{
	public boolean isPartial(HyperGraph partialGraph)
	{
		return false;
	}

	public boolean isDual(HyperGraph dualGraph)
	{
		return false;
	}

	public boolean isHost(HyperGraph hostGraph)
	{
		return false;
	}

	public boolean isPrimal(Graph primalGraph)
	{
		return false;
	}

	public boolean isUniform()
	{
		return false;
	}

	public boolean isSymmetric()
	{
		return false;
	}

	public boolean isVertexSymmetric()
	{
		return false;
	}

	public boolean isEdgeSymmetric()
	{
		return false;
	}
	
}
