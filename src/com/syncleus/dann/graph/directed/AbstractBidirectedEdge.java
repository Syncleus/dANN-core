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
package com.syncleus.dann.graph.directed;

import com.syncleus.dann.graph.AbstractEdge;
import com.syncleus.dann.graph.NodePair;

public abstract class AbstractBidirectedEdge<N extends BidirectedNode> extends AbstractEdge<N> implements BidirectedEdge<N>
{
	private EndState leftEndState;
	private EndState rightEndState;

	protected AbstractBidirectedEdge(N leftNode, N rightNode, EndState leftEndState, EndState rightEndState)
	{
		super(leftNode, rightNode);
		this.leftEndState = leftEndState;
		this.rightEndState = rightEndState;
	}

	protected AbstractBidirectedEdge(NodePair<N> nodes, EndState leftEndState, EndState rightEndState)
	{
		super(nodes);
		this.leftEndState = leftEndState;
		this.rightEndState = rightEndState;
	}

	public EndState getLeftEndState()
	{
		return this.leftEndState;
	}

	public EndState getRightEndState()
	{
		return this.rightEndState;
	}

	public boolean isIntroverted()
	{
		if( (this.rightEndState == EndState.Inward) && (this.leftEndState == EndState.Inward) )
			return true;
		return false;
	}

	public boolean isExtraverted()
	{
		if( (this.rightEndState == EndState.Outward) && (this.leftEndState == EndState.Outward) )
			return true;
		return false;
	}

	public boolean isDirected()
	{
		if( (this.rightEndState == EndState.Inward) && (this.leftEndState == EndState.Outward) )
			return true;
		else if( (this.rightEndState == EndState.Outward) && (this.leftEndState == EndState.Inward) )
			return true;
		return false;
	}

	public boolean isHalfEdge()
	{
		if( (this.rightEndState == EndState.None) && (this.leftEndState != EndState.None) )
			return true;
		else if( (this.rightEndState != EndState.None) && (this.leftEndState == EndState.None) )
			return true;
		return false;
	}

	public boolean isLooseEdge()
	{
		if( (this.rightEndState == EndState.None) && (this.leftEndState == EndState.None) )
			return true;
		return false;
	}

	public boolean isOrdinaryEdge()
	{
		if( (!this.isHalfEdge()) && (!this.isLooseEdge()) )
			return true;
		return false;
	}
}
