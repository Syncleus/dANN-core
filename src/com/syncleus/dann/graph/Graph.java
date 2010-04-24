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
	 * graph, may have the same end points unless equals in the edges used by
	 * this graph restrict that possiblity.
	 *
	 * @return An unmodifiable set of a all edges in the graph.
	 */
	Set<E> getEdges();

	/**
	 * Get a list of all nodes adjacent to the specified node. All edges
	 * connected to this node has its other end points added to the list
	 * returned. The specified node itself will appear in the list once for
	 * every loop. If there are multiple edges connecting node with a particular
	 * end point it will appear multiple times in the list, once for each hop to
	 * the end point.
	 *
	 * @param node The whose neighbors are to be returned.
	 * @return A list of all nodes adjacent to the specified node, empty set if
	 * the node has no edges.
	 */
	List<N> getAdjacentNodes(N node);

	/**
	 * Get a set of all edges which is connected to node (adjacent). You may not
	 * be able to traverse from the specified node to all of these edges
	 * returned. If you only want edges you can traverse then see
	 * getTraversableEdges.
	 *
	 * @throws IllegalArgumentException if specified node is not in the graph.
	 * @see Graph#getTraversableEdges
	 * @param node the end point for all edges to retrieve.
	 * @return An unmodifiable set of all edges that has node as an end point.
	 */
	Set<E> getAdjacentEdges(N node);

	/**
	 * Get a list of all reachable nodes adjacent to node. All edges connected
	 * to node and is traversable from node will have its destination node(s)
	 * added to the returned list. node itself will appear in the list once for
	 * every loop. If there are multiple edges connecting node with a particular
	 * end point then the end point will appear multiple times in the list, once
	 * for each hop to the end point.
	 *
	 * @param node The whose traversable neighbors are to be returned.
	 * @return A list of all nodes adjacent to the specified node and
	 * traversable from the spevified node, empty set if the node has no edges.
	 */
	List<N> getTraversableNeighbors(N node);

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
	 * getAdjacentEdges().size(). Throws an IllegalArgumentException if node is not
	 * in the graph.
	 *
	 * @see Graph#getAdjacentEdges
	 * @param node The node whose degree is to be returned
	 * @return the degree of the specified node.
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
	 * If there is atleast one path from every Node in the graph to any other
	 * node in the graph then true, false otherwise. This is analogus with
	 * Strongly connected.
	 *
	 * @return true if the graph is connected, false otherwise.
	 */
	boolean isConnected();

	/**
	 * Obtains a set of all Maximally connected componets in the graph. A
	 * subgraph is maximally connected if every node in the subgraph has every
	 * edge from the parent graph that is connected to one of the nodes in the
	 * subgraph along with every node that is an end point of one of these
	 * edges.
	 *
	 * @return a unmodifiable set of all the maximally connected components.
	 */
	Set<Graph<N,E>> getMaximallyConnectedComponents();

	/**
	 * Determines if the subgraph is a maximally connected component. A
	 * subgraph is maximally connected if every node in the subgraph has every
	 * edge from the parent graph that is connected to one of the nodes in the
	 * subgraph along with every node that is an end point of one of these
	 * edges.
	 *
	 * @param subgraph A subgraph of this graph to be checked if maximally
	 * connected.
	 * @return true if the subgraph is a maximally connected component of this
	 * graph. False otherwise.
	 */
	boolean isMaximalSubgraph(Graph<N,E> subgraph);

	/**
	 * Determines if the set of nodes and edges form a cut. They are a cut
	 * if by removing all the specified nodes and edges the number of maximally
	 * connected components is increased. Its important to note that when
	 * removing a node that node will also be removed as an end point from all
	 * edges. If as a result a edge is left with a single end point then that
	 * edge will also be removed even if it isnt speciically listed in edges.
	 *
	 * @param nodes set of nodes to remove when checking for a cut.
	 * @param edges set of edges to remove when checking for a cut.
	 * @return true if the nodes and edges form a cut, false otherwise.
	 */
	boolean isCut(Set<N> nodes, Set<E> edges);

	/**
	 * Determines if the set of edges form a cut. They are a cut
	 * if by removing all the specified edges the number of maximally
	 * connected components is increased.
	 *
	 * @param edges set of edges to remove when checking for a cut.
	 * @return true if the edges form a cut, false otherwise.
	 */
	boolean isCut(Set<E> edges);

	/**
	 * Determines if node forms a cut. It is a cut if by removing the specified
	 * node the number of maximally connected components is increased. Its
	 * important to note that when removing a node that node will also be
	 * removed as an end point from all edges. If as a result a edge is left
	 * with a single end point then that edge will also be removed.
	 *
	 * @param node node to remove when checking for a cut.
	 * @return true if the node is a cut node, False otherwise.
	 */
	boolean isCut(N node);

	/**
	 * Determines if edge forms a cut. It is a cut if by removing the specified
	 * edge the number of maximally connected components is increased.
	 *
	 * @param edge edge to remove when checking for a cut.
	 * @return true if the edge is a cut edge, False otherwise.
	 */
	boolean isCut(E edge);

	/**
	 * Determines if the set of nodes and edges form a cut between begin and end
	 * nodes. They are a cut if by removing all the specified nodes and edges
	 * there is no longer a path between begin and end. Its important to note
	 * that when removing a node that node will also be removed as an end point
	 * from all edges. If as a result a edge is left with a single end point
	 * then that edge will also be removed even if it isnt speciically listed in
	 * edges.
	 *
	 * @param nodes set of nodes to remove when checking for a cut.
	 * @param edges set of edges to remove when checking for a cut.
	 * @param begin begining node in the path to check for a cut.
	 * @param end ending node in the path to check for a cut.
	 * @return true if the nodes and edges form a cut between begin and end,
	 * false otherwise.
	 * @throws IllegalArgumentException if any of nodes, edges, begin, or end
	 * are not in this graph or begin and end have no path between them before
	 * the cut.
	 */
	boolean isCut(Set<N> nodes, Set<E> edges, N begin, N end);

	/**
	 * Determines if the set of edges form a cut between begin and end
	 * nodes. They are a cut if by removing all the specified edges
	 * there is no longer a path between begin and end.
	 *
	 * @param edges set of edges to remove when checking for a cut.
	 * @param begin begining node in the path to check for a cut.
	 * @param end ending node in the path to check for a cut.
	 * @return true if the edges form a cut between begin and end, false
	 * otherwise.
	 * @throws IllegalArgumentException if any of nodes, edges, begin, or end
	 * are not in this graph or begin and end have no path between them before
	 * the cut.
	 */
	boolean isCut(Set<E> edges, N begin, N end);

	/**
	 * Determines if the node is a cut node between begin and end nodes. It is
	 * a cut if by removing the specified node there is no longer a path between
	 * begin and end. Its important to note that when removing the node that
	 * node will also be removed as an end point from all edges. If as a result
	 * a edge is left with a single end point then that edge will also be
	 * removed even if it isnt speciically listed in edges.
	 *
	 * @param node node to remove when checking for a cut.
	 * @param begin begining node in the path to check for a cut.
	 * @param end ending node in the path to check for a cut.
	 * @return true if the node forms a cut between begin and end, false
	 * otherwise.
	 * @throws IllegalArgumentException if any of nodes, edges, begin, or end
	 * are not in this graph or begin and end have no path between them before
	 * the cut.
	 */
	boolean isCut(N node, N begin, N end);

	/**
	 * Determines if the edge is a cut edge between begin and end nodes. It is
	 * a cut if by removing the specified edge there is no longer a path between
	 * begin and end.
	 *
	 * @param edge edge to remove when checking for a cut.
	 * @param begin begining node in the path to check for a cut.
	 * @param end ending node in the path to check for a cut.
	 * @return true if the edge forms a cut between begin and end, false
	 * otherwise.
	 * @throws IllegalArgumentException if any of nodes, edges, begin, or end
	 * are not in this graph or begin and end have no path between them before
	 * the cut.
	 */
	boolean isCut(E edge, N begin, N end);

	/**
	 * Determines the minimum number of nodes that must be removed from the
	 * graph in order to form a cut. A cut is where the total number of
	 * maximally connected components is increased due to removing nodes or
	 * edges. Its important to note that when removing a node that
	 * node will also be removed as an end point from all edges. If as a result
	 * a edge is left with a single end point then that edge will also be
	 * removed.
	 *
	 * @return The minimum number of nodes that must be removed from the graph
	 * in order to form a cut.
	 */
	int getNodeConnectivity();

	/**
	 * Determines the minimum number of edges that must be removed from the
	 * graph in order to form a cut. A cut is where the total number of
	 * maximally connected components is increased due to removing nodes or
	 * edges.
	 *
	 * @return The minimum number of edges that must be removed from the graph
	 * in order to form a cut.
	 */
	int getEdgeConnectivity();

	/**
	 * Determines the minimum number of nodes that must be removed from the
	 * graph in order to form a cut between the specified nodes. A cut is where
	 * there is no longer a path between begin and end. If there is no path
	 * between begin and end this returns 0. Its important to note that when
	 * removing a node that node will also be removed as an end point from all
	 * edges. If as a result a edge is left with a single end point then that
	 * edge will also be removed.
	 *
	 * @param begin begining node in the path to check for connectivity.
	 * @param end ending node in the path to check for connectivity.
	 * @throws IllegalArgumentException if either begin or end is not in the
	 * graph.
	 * @return The minimum number of nodes that must be removed from the graph
	 * in order to form a cut between the specified nodes.
	 */
	int getNodeConnectivity(N begin, N end);

	/**
	 * Determines the minimum number of edges that must be removed from the
	 * graph in order to form a cut between the specified nodes. A cut is where
	 * there is no longer a path between begin and end. If there is no path
	 * between begin and end this returns 0;
	 *
	 * @param begin begining node in the path to check for connectivity.
	 * @param end ending node in the path to check for connectivity.
	 * @throws IllegalArgumentException if either begin or end is not in the
	 * graph.
	 * @return The minimum number of edges that must be removed from the graph
	 * in order to form a cut between the specified nodes.
	 */
	int getEdgeConnectivity(N begin, N end);

	/**
	 * A simple graph where every node is connected by an edge to every node
	 * (adjacent). Not every node needs to have an edge that can be traversed to
	 * every other node so long as an edge exists.
	 *
	 * @return true if this is a simple graph where every node is connected by
	 * an edge to every other node(adjacent).
	 */
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
