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

public abstract class AbstractBidirectedEdge implements BidirectedEdge
{
	protected static <N extends BidirectedNode> List<N> pairToList(NodePair<N> nodes)
	{
		List<N> pairList = new ArrayList<N>();
		pairList.add(nodes.getLeftNode());
		pairList.add(nodes.getRightNode());
		return pairList;
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
