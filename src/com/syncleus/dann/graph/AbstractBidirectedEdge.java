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
import java.util.List;
public abstract class AbstractBidirectedEdge<N> extends AbstractEdge<N> implements BidirectedEdge<N>
{
	private final N leftNode;
	private final N rightNode;
	private final EndState leftEndState;
	private final EndState rightEndState;

	protected AbstractBidirectedEdge(N leftNode, EndState leftEndState, N rightNode, EndState rightEndState)
	{
		super(packNodes(leftNode, rightNode));
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.leftEndState = leftEndState;
		this.rightEndState = rightEndState;
	}

	private static <N> List<N> packNodes(N leftNode, N rightNode)
	{
		List<N> pack = new ArrayList<N>();
		pack.add(leftNode);
		pack.add(rightNode);
		return pack;
	}

	public final N getLeftNode()
	{
		return this.leftNode;
	}

	public final N getRightNode()
	{
		return this.rightNode;
	}

	public final EndState getLeftEndState()
	{
		return this.leftEndState;
	}

	public final EndState getRightEndState()
	{
		return this.rightEndState;
	}

	public boolean isIntroverted()
	{
		if( (this.getRightEndState() == EndState.Inward) && (this.getLeftEndState() == EndState.Inward) )
			return true;
		return false;
	}

	public boolean isExtraverted()
	{
		if( (this.getRightEndState() == EndState.Outward) && (this.getLeftEndState() == EndState.Outward) )
			return true;
		return false;
	}

	public boolean isDirected()
	{
		if( (this.getRightEndState() == EndState.Inward) && (this.getLeftEndState() == EndState.Outward) )
			return true;
		else if( (this.getRightEndState() == EndState.Outward) && (this.getLeftEndState() == EndState.Inward) )
			return true;
		return false;
	}

	public boolean isHalfEdge()
	{
		if( (this.getRightEndState() == EndState.None) && (this.getLeftEndState() != EndState.None) )
			return true;
		else if( (this.getRightEndState() != EndState.None) && (this.getLeftEndState() == EndState.None) )
			return true;
		return false;
	}

	public boolean isLooseEdge()
	{
		if( (this.getRightEndState() == EndState.None) && (this.getLeftEndState() == EndState.None) )
			return true;
		return false;
	}

	public boolean isOrdinaryEdge()
	{
		if( (!this.isHalfEdge()) && (!this.isLooseEdge()) )
			return true;
		return false;
	}

	public boolean isLoop()
	{
		if(this.getLeftEndState().equals(this.getRightEndState()))
			return true;
		return false;
	}
}
