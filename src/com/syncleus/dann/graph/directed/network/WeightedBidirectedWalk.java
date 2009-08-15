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
package com.syncleus.dann.graph.directed.network;

import com.syncleus.dann.graph.WeightedWalk;
import com.syncleus.dann.graph.directed.BidirectedWalk;
import java.util.List;

public interface WeightedBidirectedWalk<N extends WeightedBidirectedNode<? extends E>, E extends WeightedBidirectedEdge<? extends N>> extends BidirectedWalk<N,E>, WeightedWalk<N,E>
{
	List<? extends E> getSteps();
	N getFirstNode();
	N getLastNode();
}
