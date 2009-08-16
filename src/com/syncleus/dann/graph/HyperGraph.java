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

public interface HyperGraph<G extends HyperGraph<? extends G, ? extends N, ? extends E, ? extends W>, N extends HyperNode<? extends E>, E extends HyperEdge<? extends N>, W extends HyperWalk<? extends N, ? extends E>> extends Graph<G, N, E, W>
{
	boolean isPartial(G partialGraph);
	boolean isDual(G dualGraph);
	boolean isHost(G hostGraph);
	boolean isPrimal(Graph primalGraph);
	boolean isUniform();
	boolean isSymmetric();
	boolean isVertexSymmetric();
	boolean isEdgeSymmetric();
}
