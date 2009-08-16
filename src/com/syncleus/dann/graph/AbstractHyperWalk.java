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
public abstract class AbstractHyperWalk<N extends HyperNode<? extends E>, E extends HyperEdge<? extends N>> extends AbstractWalk<N, E> implements HyperWalk<N, E>
{
	private List<N> nodeSteps;

	protected AbstractHyperWalk(List<N> nodeSteps, List<E> edgeSteps)
	{
		super(nodeSteps.get(0), nodeSteps.get(nodeSteps.size() - 1), edgeSteps);

		if(nodeSteps == null)
			throw new IllegalArgumentException("nodeSteps can not be null");
		if(edgeSteps == null)
			throw new IllegalArgumentException("edgeSteps can not be null");
		if(nodeSteps.size() < 2)
			throw new IllegalArgumentException("nodeSteps must have atleast 2 elements");
		if(edgeSteps.size() != (nodeSteps.size()-1) )
			throw new IllegalArgumentException("edgeSteps must be exactly one less in size than nodeSteps");

		this.nodeSteps = new ArrayList<N>(nodeSteps);
	}

	public List<N> getNodeSteps()
	{
		return Collections.unmodifiableList(nodeSteps);
	}
}
