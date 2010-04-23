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

public interface Graph<N, E extends Edge<N>>
{
	//connectivity
	Set<N> getNodes();
	Set<E> getEdges();
	Set<E> getEdges(N node);
	Set<E> getTraversableEdges(N node);
	int getDegree(N node);
	boolean isConnected(N leftNode, N rightNode);
	List<N> getNeighbors(N node);
	List<N> getTraversableNeighbors(N node);

	//attributes/properties
	boolean isConnected();
	Set<Graph<N,E>> getConnectedComponents();
	boolean isMaximalSubgraph(Graph<N,E> subgraph);
	boolean isCut(Set<N> nodes, Set<E> edges);
	boolean isCut(Set<E> edges);
	boolean isCut(N nodes);
	boolean isCut(E edges);
	boolean isCut(Set<N> nodes, Set<E> edges, N begin, N end);
	boolean isCut(Set<E> edges, N begin, N end);
	boolean isCut(N node, N begin, N end);
	boolean isCut(E edge, N begin, N end);
	int getNodeConnectivity();
	int getEdgeConnectivity();
	int getNodeConnectivity(N begin, N end);
	int getEdgeConnectivity(N begin, N end);
	boolean isComplete();
	int getOrder();
	int getCycleCount();
	boolean isPancyclic();
	boolean isUnicyclic();
	int getGirth();
	int getCircumference();
	boolean isTraceable();
	boolean isSpanning(TreeGraph graph);
	boolean isTraversable();
	boolean isTree();
	boolean isSubGraph(Graph<N,E> graph);
	boolean isKnot(Graph<N,E> subGraph);
	int getMinimumDegree();
	boolean isMultigraph(boolean includeLoops);
	boolean isIsomorphic(Graph<N,E> isomorphicGraph);
	boolean isHomomorphic(Graph<N,E> homomorphicGraph);
	boolean isRegular();
	int getRegularDegree();
	boolean isSimple();
}
