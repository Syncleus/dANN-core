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

public abstract class AbstractHyperEdge extends AbstractEdge implements HyperEdge
{
	private final List<HyperNode> hyperNodes;

	protected AbstractHyperEdge(List<HyperNode> nodes)
	{
		super(new ArrayList<Node>(nodes));
		this.hyperNodes = Collections.unmodifiableList(new ArrayList<HyperNode>(nodes));
	}

	protected AbstractHyperEdge(HyperNode... nodes)
	{
		super(nodes);
		List<HyperNode> newNodes = new ArrayList<HyperNode>();
		for(HyperNode node : nodes)
			newNodes.add(node);
		this.hyperNodes = Collections.unmodifiableList(newNodes);
	}

	public List<HyperNode> getHyperNodes()
	{
		return this.hyperNodes;
	}

	public int getDegree()
	{
		return 0;
	}

	public boolean isSymmetric(HyperEdge symmetricEdge)
	{
		return false;
	}
}
