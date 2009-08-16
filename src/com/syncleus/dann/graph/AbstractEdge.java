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

public abstract class AbstractEdge<N extends Node> implements Edge<N>
{
	private NodePair<N> nodes;

	protected AbstractEdge(N leftNode, N rightNode)
	{
		if((leftNode == null)||(rightNode == null))
			throw new IllegalArgumentException("Neither leftNode nor rightNode can be null.");
		
		this.nodes = new NodePair<N>(leftNode, rightNode);
	}

	protected AbstractEdge(NodePair<N> nodes)
	{
		if(nodes == null)
			throw new IllegalArgumentException("nodes can not be null.");

		this.nodes = nodes;
	}

	public NodePair<N> getNodes()
	{
		return this.nodes;
	}

	public boolean isLoop()
	{
		if(this.nodes.getLeftNode().equals(this.nodes.getRightNode()))
			return true;
		return false;
	}
}
