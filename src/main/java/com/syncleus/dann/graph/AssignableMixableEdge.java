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

public interface AssignableMixableEdge<
	  	T,
	  	LT extends T,
	  	RT extends T,
	  	E extends AssignableMixableEdge.Endpoint<T, ? extends T, ? extends T>,
	  	LE extends AssignableMixableEdge.Endpoint<T, LT, RT>,
	  	RE extends AssignableMixableEdge.Endpoint<T, RT, LT>
	  > extends MixableEdge<T, LT, RT, E, LE, RE>, AssignableCloud<T, E>
{
	interface Endpoint<P, T extends P, N extends P> extends MixableEdge.Endpoint<P, T, N>, AssignableCloud.Endpoint<P, T>
	{
	};

	void reassign(LT newLeftNode, RT newRightNode) throws InvalidEdgeException;
}