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

import java.util.Set;

public interface TraversableCloud<E extends TraversableCloud.Endpoint<?>> extends Cloud<E>
{
    interface Endpoint<T> extends Cloud.Endpoint<T>
    {
    }

	boolean isTraversable(E endpoint);
    Set<E> getTraversableEndpoints(E endpoint);

	/**
	 * Returns an edge with the specified node disconnected.
	 *
	 *
     * @param endpoint node to remove from the returned edge.
     * @return an edge with the specified node disconnected,
	 *   <tt>null</tt> if the entire edge should be deleted as a result of
	 *   removing the specified node.
	 * @since 2.0
	 */
	TraversableCloud<E> disconnect(E endpoint);

	/**
	 * Returns an edge with the specified nodes disconnected.
	 *
	 *
     * @param endpoints node to remove from the returned edge.
     * @return an edge with the specified nodes disconnected,
	 *   <tt>null</tt> if the entire edge should be deleted as a result of
	 *   removing the specified nodes.
	 */
	TraversableCloud<E> disconnect(Set<E> endpoints);

}
