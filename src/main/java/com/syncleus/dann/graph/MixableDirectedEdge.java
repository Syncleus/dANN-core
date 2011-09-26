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

public interface MixableDirectedEdge<
	  	T,
	  	ST extends T,
	  	DT extends T,
	  	E extends MixableBidirectedEdge.Endpoint<T, ? extends T, ? extends T>,
	  	SE extends MixableBidirectedEdge.Endpoint<T, ST, DT>,
	  	DE extends MixableBidirectedEdge.Endpoint<T, DT, ST>
	  > extends MixableBidirectedEdge<T, ST, DT, E, SE, DE>
{
	interface Endpoint<P, T extends P, N extends P> extends MixableBidirectedEdge.Endpoint<P, T, N>
	{
	};

	SE getSourceEndPoint();
	DE getDestinationEndPoint();
}
