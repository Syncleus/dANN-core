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

import com.syncleus.dann.graph.context.ContextReporter;
import com.syncleus.dann.graph.xml.EdgeXml;
import com.syncleus.dann.xml.XmlSerializable;
import java.io.Serializable;
import java.util.Set;

public interface Edge<
	  	T,
	  	EP extends Edge.Endpoint<T>
	  > extends Serializable, Cloneable, XmlSerializable<EdgeXml, Object>, ContextReporter
{
	interface Endpoint<
		  	T
		  >
	{
		Set<Edge.Endpoint<T>> getNeighbors();
		Set<Edge.Endpoint<T>> getTraversableNeighborsTo();
		Set<Edge.Endpoint<T>> getTraversableNeighborsFrom();
		boolean isTraversable();
		boolean isTraversable(final Edge.Endpoint<T> destination);
		boolean isTraversable(final T destination);
		T getTarget();
	};

	Set<EP> getEndPoints();
	Set<EP> getEndPoints(final T node);
	boolean contains(final T node);
	Set<T> getTargets();
	Set<T> getNeighbors(final T source);
	Set<T> getTraversableFrom(final T source);
	Set<T> getTraversableTo(final T destination);
	boolean isTraversable(final T source, final T destination);
	int getDegree();
}
