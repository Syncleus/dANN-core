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

public interface Graph
{
	Set<? extends Node> getNodes();
	List<? extends Edge> getEdges();
	boolean isConnected(Node begin, Node end);
	boolean isConnected();
	Set<? extends Graph> getConnectedComponents();
	boolean isMaximalConnected();
	boolean isCut(Graph subGraph);
	boolean isCut(Graph subGraph, Node begin, Node end);
	int getNodeConnectivity();
	int getEdgeConnectivity();
	int getNodeConnectivity(Node begin, Node end);
	int getEdgeConnectivity(Node begin, Node end);
	boolean isCompleteGraph();
	boolean isReachable(Node begin, Node end);
	Walk getShortestPath(Node begin, Node end);
	int getOrder();
	int getCycleCount();
	boolean isPancyclic();
	int getGirth();
	int getCircumference();
	boolean isTraceable();
	boolean isSpanning(Walk walk);
	boolean isSpanning(TreeGraph graph);
	boolean isTraversable();
	boolean isEularian(Walk walk);
	boolean isTree();
	boolean isSubGraph(Graph graph);
	boolean isKnot(Graph subGraph);
	int getTotalDegree();
	boolean isMultigraph();
	boolean isIsomorphic(Graph isomorphicGraph);
	boolean isHomomorphic(Graph homomorphicGraph);
	boolean isRegular();
}
