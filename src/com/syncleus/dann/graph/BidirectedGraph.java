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

import java.util.List;
import java.util.Set;

public interface BidirectedGraph<N extends BidirectedNode<? extends E>, E extends BidirectedEdge, W extends BidirectedWalk<? extends N, ? extends E>> extends Graph<N,E,W>
{
	Set<BidirectedGraph> getStrongComponents();
	boolean isStronglyConnected();
	boolean isPolytree();
	//Parent methods
	Set<N> getNodes();
	List<E> getEdges();
	boolean isConnected();
	Set<Graph> getConnectedComponents();
	boolean isMaximalConnected();
	boolean isCut(Graph subGraph);
	boolean isCut(Graph subGraph, N begin, N end);
	int getNodeConnectivity();
	int getEdgeConnectivity();
	int getNodeConnectivity(N begin, N end);
	int getEdgeConnectivity(N begin, N end);
	boolean isCompleteGraph();
	int getOrder();
	int getCycleCount();
	boolean isPancyclic();
	int getGirth();
	int getCircumference();
	boolean isTraceable();
	boolean isSpanning(W walk);
	boolean isSpanning(TreeGraph graph);
	boolean isTraversable();
	boolean isEularian(W walk);
	boolean isTree();
	boolean isSubGraph(Graph graph);
	boolean isKnot(Graph subGraph);
	int getTotalDegree();
	boolean isMultigraph();
	boolean isIsomorphic(Graph isomorphicGraph);
	boolean isHomomorphic(Graph homomorphicGraph);
	boolean isRegular();
}
