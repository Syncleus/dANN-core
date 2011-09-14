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

import java.util.*;

public interface MutableHyperEdge<
	  	T,
	  	EP extends Edge.Endpoint<T>
	  > extends HyperEdge<T,EP>, MutableEdge<T,EP>
{
	interface Endpoint<NN, EN extends NN> extends HyperEdge.Endpoint<NN, EN>, MutableEdge.Endpoint<NN, EN>
	{
	};

	Endpoint<T, T> join(final T node) throws InvalidEdgeException;
	Map<T,Endpoint<T, T>> join(final Set<T> nodes) throws InvalidEdgeException;
	void leave(final Endpoint<T, T> endPoint) throws InvalidEdgeException;
	void leave(final Set<Endpoint<T, T>> endPoint) throws InvalidEdgeException;
	void clear() throws InvalidEdgeException;
	Map<T,Endpoint<T, T>> reconfigure(final Set<T> connectNodes, final Set<Endpoint<T, T>> disconnectEndPoints) throws InvalidEdgeException;
}
