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

public abstract class AbstractEdge implements Edge
{
	private final List<Node> nodes;

	protected AbstractEdge(List<Node> nodes)
	{
		this.nodes = Collections.unmodifiableList(new ArrayList<Node>(nodes));
	}

	protected AbstractEdge(Node... nodes)
	{
		List<Node> newNodes = new ArrayList<Node>();
		for(Node node : nodes)
			newNodes.add(node);
		this.nodes = Collections.unmodifiableList(newNodes);
	}

	public final List<Node> getNodes()
	{
		return this.nodes;
	}
}
