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

/**
 * A type of graph where every edge has exactly 2 end points and may have a
 * directionality at either end point. The two end points at each end do not
 * need to be unique, allowing for loops. Each end point of every edge can have
 * one of three states, no directionality, outward, or inward. Both undirected
 * and directed graphs (as well as a mix of the two) extend from this type of
 * graph. With undirected edges neither end point has directionality. With
 * directed edges both ends have opposite orientation, outward on one end and
 * inward on the other, such that both ends point in the same direction.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public interface BidirectedGraph<
	  	NE extends BidirectedGraph.NodeEndpoint<?>,
	  	EE extends BidirectedGraph.EdgeEndpoint<?>
	  > extends TraversableGraph<NE, EE>, BidirectedCloud<NE>
{
	interface Endpoint<
		  	T
		  >
		  extends TraversableGraph.Endpoint<T>, BidirectedCloud.Endpoint<T>
	{
	};

	interface NodeEndpoint<
		  	T
	  > extends TraversableGraph.NodeEndpoint<T>, Endpoint<T>
	{
	};

	interface EdgeEndpoint<
		  	T extends BidirectedEdge<?>
		> extends TraversableGraph.EdgeEndpoint<T>, Endpoint<T>
	{
	};
}
