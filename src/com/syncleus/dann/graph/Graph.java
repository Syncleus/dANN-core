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

public interface Graph<N, E extends Edge<? extends N>, W extends Walk<? extends N, ? extends E>>
{
	//connectivity
	Set<N> getNodes();
	List<E> getEdges();
	List<E> getEdges(N node);
	List<E> getTraversableEdges(N node);
	int getDegree(N node);
	boolean isConnected(N leftNode, N rightNode);
	List<N> getNeighbors(N node);
	List<N> getTraversableNeighbors(N node);

	//attributes/properties
	boolean isConnected();
	Set<Graph<N,E,W>> getConnectedComponents();
	boolean isMaximalSubgraph(Graph<? extends N, ? extends E, ? extends W> subgraph);
	boolean isCut(Set<? extends N> nodes, List<? extends E> edges);
	boolean isCut(Set<? extends N> nodes);
	boolean isCut(List<? extends E> edges);
	boolean isCut(N nodes);
	boolean isCut(E edges);
	boolean isCut(Set<? extends N> nodes, List<? extends E> edges, N begin, N end);
	boolean isCut(Set<? extends N> nodes, N begin, N end);
	boolean isCut(List<? extends E> edges, N begin, N end);
	boolean isCut(N nodes, N begin, N end);
	boolean isCut(E edges, N begin, N end);
	int getNodeConnectivity();
	int getEdgeConnectivity();
	int getNodeConnectivity(N begin, N end);
	int getEdgeConnectivity(N begin, N end);
	boolean isComplete();
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
	boolean isSubGraph(Graph<? extends N, ? extends E, ? extends W> graph);
	boolean isKnot(Graph<? extends N, ? extends E, ? extends W> subGraph);
	int getTotalDegree();
	boolean isMultigraph();
	boolean isIsomorphic(Graph<? extends N, ? extends E, ? extends W> isomorphicGraph);
	boolean isHomomorphic(Graph<? extends N, ? extends E, ? extends W> homomorphicGraph);
	boolean isRegular();
}
