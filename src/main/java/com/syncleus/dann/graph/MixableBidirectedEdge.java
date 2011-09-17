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

public interface MixableBidirectedEdge<
	  	T,
	  	LT extends T,
	  	RT extends T,
	  	EP extends MixableBidirectedEdge.Endpoint<? extends T, ? extends LT, ? extends RT>
	  > extends Edge<T, EP>
{
	interface Endpoint<
			  NN,
			  EN extends NN,
			  ON extends NN
		  > extends Edge.Endpoint<NN>
	{
		enum Direction
		{
			OUTWARD, INWARD, NONE
		}

		Direction getDirection();
		Endpoint<NN, ON,EN> getNeighbor();
	};

	EP getLeftEndPoint();
	EP getRightEndPoint();
	boolean isIntroverted();
	boolean isExtroverted();
	boolean isDirected();
	boolean isHalfEdge();
	boolean isLooseEdge();
	boolean isOrdinaryEdge();
	boolean isLoop();
}
