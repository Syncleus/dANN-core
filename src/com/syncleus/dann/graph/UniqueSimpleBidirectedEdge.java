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

public class UniqueSimpleBidirectedEdge<N> extends SimpleBidirectedEdge<N>
{
	public UniqueSimpleBidirectedEdge(N leftNode, EndState leftEndState, N rightNode, EndState rightEndState)
	{
		super(leftNode, leftEndState, rightNode, rightEndState);
	}

	@Override
	public boolean equals(Object compareToObj)
	{
		if(!(compareToObj instanceof BidirectedEdge))
			return false;
		BidirectedEdge compareTo = (BidirectedEdge) compareToObj;
		return
			(
				(compareTo.getLeftNode().equals(this.getLeftNode()))&&
				(compareTo.getRightNode().equals(this.getRightNode()))&&
				(compareTo.getLeftEndState().equals(this.getLeftEndState()))&&
				(compareTo.getRightEndState().equals(this.getRightEndState()))
			)||
			(
				(compareTo.getLeftNode().equals(this.getRightNode()))&&
				(compareTo.getRightNode().equals(this.getLeftNode()))&&
				(compareTo.getLeftEndState().equals(this.getRightEndState()))&&
				(compareTo.getRightEndState().equals(this.getLeftEndState()))
			);
	}

	@Override
	public int hashCode()
	{
		int leftNodeHash = this.getLeftNode().hashCode();
		int rightNodeHash = this.getRightNode().hashCode();
		int leftStateHash = this.getLeftEndState().hashCode();
		int rightStateHash = this.getRightEndState().hashCode();
		return
			leftNodeHash +
			(leftNodeHash * leftStateHash) +
			rightNodeHash +
			(rightNodeHash * rightStateHash);
	}
}
