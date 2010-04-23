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
	/**
	 * Get a set of all Nodes in the graph.
	 * @return An unmodifiable set of all nodes in the graph.
	 */
	Set<N> getNodes();

	/**
	 * Get a set of all edges in the graph. Two edges in the set, and in the
	 * graph map have the same end points unless equals of the edges being used
	 * contradicts this.
	 *
	 * @return An unmodifiable set of a all edges in the graph.
	 */
	Set<E> getEdges();

	/**
	 * Get a set of all edges which have node as an end point. You may not be
	 * able to traverse from node across all of these edges. If you only want
	 * edges you can traverse then see getTraversableEdges. Throws an
	 * IllegalArgumentException if node is not in the graph.
	 *
	 * @see Graph#getTraversableEdges
	 * @param node the end point for all edges to retrieve.
	 * @return An unmodifiable set of all edges that has node as an end point.
	 */
	Set<E> getEdges(N node);

	/**
	 * Get a set of all edges which you can traverse from node. Of course node
	 * will always be an end point for each edge returned. Throws an
	 * IllegalArgumentException if node is not in the graph.
	 *
	 * @param node edges returned will be traversable from this node.
	 * @return An unmodifiable set of all edges that can be traversed from node.
	 */
	Set<E> getTraversableEdges(N node);

	/**
	 * The total degree of the specified node. This is essentially the number
	 * of edges which has node as an end point. This will always be equal to
	 * getEdges().size(). Throws an IllegalArgumentException if node is not
	 * in the graph.
	 *
	 * @see Graph#getEdges()
	 * @param node A node in the graph
	 * @return the degree of the node.
	 */
	int getDegree(N node);

	/**
	 * Determines if there is a path from the firstNode to the lastNode. Graphs
	 * do not need to be directed or undirected, another words there may
	 * be a path from firstNode to lastNode even if there is no path from
	 * lastNode to firstNode. Both nodes must be present in the graph or else
	 * an InvalidArgumentException will be thrown.
	 *
	 * @param firstNode begining node to find a path from.
	 * @param lastNode eding node to find a path to.
	 * @return true if a path exists, false otherwise.
	 */
	boolean isConnected(N firstNode, N lastNode);

	/**
	 * Get a list of all nodes adjacent to node. All edges connected to this
	 * node has its other end points added to the list returned. node itself
	 * will appear in the list once for every loop. If there are multiple edges
	 * connecting node with a particular end point it will appear multiple
	 * times in the list, once for each hop to the end point.
	 *
	 * @param node A node currently in the graph.
	 * @return A list of all nodes adjacent to node, empty set if the node has
	 * no edges.
	 */
	List<N> getNeighbors(N node);

	/**
	 * Get a list of all reachable nodes adjacent to node. All edges connected
	 * to node and is traversable from node will have its destination node(s)
	 * added to the returned list. node itself will appear in the list once for
	 * every loop. If there are multiple edges connecting node with a particular
	 * end point then the end point will appear multiple times in the list, once
	 * for each hop to the end point.
	 *
	 * @param node A node curretly in the graph.
	 * @return A list of all nodes adjacent to node and traversable from node,
	 * empty set if the node has no edges.
	 */
	List<N> getTraversableNeighbors(N node);

	/**
	 * If there is atleast one path from every Node in the graph to any other
	 * node in the graph then true, false otherwise. This is analogus with
	 * Strongly connected.
	 *
	 * @return true if the graph is connected, false otherwise.
	 */
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
	boolean isSpanning(TreeGraph<N,?> graph);
	boolean isTree();
	boolean isSubGraph(Graph<N,E> graph);
	int getMinimumDegree();
	boolean isMultigraph(boolean includeLoops);
	boolean isIsomorphic(Graph<N,E> isomorphicGraph);
	boolean isHomomorphic(Graph<N,E> homomorphicGraph);
	boolean isRegular();
	int getRegularDegree();
	boolean isSimple();
}
