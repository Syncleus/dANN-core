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
package com.syncleus.dann.graph.topological;

import java.util.Set;
import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public interface WeakConnectivityOptimizedGraph<N, E extends Edge<N>> extends Graph<N, E>
{
	/**
	 * Determines if there is a path between the firstNode and the lastNode. If
	 * this returns true then reversing the arguments will also return true. This
	 * is because you only need to form a path between the specified nodes as if
	 * the graph were undirected.
	 *
	 * @param firstNode begining node to find a path from.
	 * @param lastNode eding node to find a path to.
	 * @return true if a path exists, false otherwise.
	 * @throws IllegalArgumentException if either of the specified nodes is not in
	 * the graph.
	 * @since 2.0
	 */
	boolean isWeaklyConnected(N firstNode, N lastNode);
	/**
	 * If there is atleast one path from every node in the graph to any other node
	 * in the graph, treating all edges as undirected edges, then true, false
	 * otherwise.
	 *
	 * @return true if the graph is connected, false otherwise.
	 * @see StrongConnectivityOptimizedGraph#isStronglyConnected
	 * @since 2.0
	 */
	boolean isWeaklyConnected();

	/**
	 * Obtains a set of all Maximally connected componets in the graph. A subgraph
	 * is a maximally connected component if it is a weakly connected subgraph of
	 * this graph and to which no vertex can be added from this graph and still be
	 * weakly connected. Assuming of course all related edges are also copied to
	 * to the subgraph.
	 *
	 * @return a unmodifiable set of all the maximally connected components.
	 * @since 2.0
	 */
	Set<Graph<N, E>> getMaximallyConnectedComponents();
	/**
	 * Determines if the subgraph is a maximally connected component. A subgraph is
	 * maximally connected if every node in the subgraph has every edge from the
	 * parent graph that is connected to one of the nodes in the subgraph along
	 * with every node that is an end point of one of these edges.
	 *
	 * @param subgraph A subgraph of this graph to be checked if maximally
	 * connected.
	 * @return true if the subgraph is a maximally connected component of this
	 *         graph. False otherwise.
	 * @since 2.0
	 */
	boolean isMaximalSubgraph(Graph<N, E> subgraph);
	/**
	 * Determines if the set of nodes and edges form a cut. They are a cut if by
	 * removing all the specified nodes and edges the number of maximally connected
	 * components is increased. Its important to note that when removing a node
	 * that node will also be removed as an end point from all edges. If as a
	 * result a edge is left with a single end point then that edge will also be
	 * removed even if it isnt speciically listed in edges.
	 *
	 * @param nodes set of nodes to remove when checking for a cut.
	 * @param edges set of edges to remove when checking for a cut.
	 * @return true if the nodes and edges form a cut, false otherwise.
	 * @since 2.0
	 */
	boolean isCut(Set<N> nodes, Set<E> edges);
	/**
	 * Determines if the set of edges form a cut. They are a cut if by removing all
	 * the specified edges the number of maximally connected components is
	 * increased.
	 *
	 * @param edges set of edges to remove when checking for a cut.
	 * @return true if the edges form a cut, false otherwise.
	 * @since 2.0
	 */
	boolean isCut(Set<E> edges);
	/**
	 * Determines if node forms a cut. It is a cut if by removing the specified
	 * node the number of maximally connected components is increased. Its
	 * important to note that when removing a node that node will also be removed
	 * as an end point from all edges. If as a result a edge is left with a single
	 * end point then that edge will also be removed.
	 *
	 * @param node node to remove when checking for a cut.
	 * @return true if the node is a cut node, False otherwise.
	 * @since 2.0
	 */
	boolean isCut(N node);
	/**
	 * Determines if edge forms a cut. It is a cut if by removing the specified
	 * edge the number of maximally connected components is increased.
	 *
	 * @param edge edge to remove when checking for a cut.
	 * @return true if the edge is a cut edge, False otherwise.
	 * @since 2.0
	 */
	boolean isCut(E edge);
	/**
	 * Determines if the set of nodes and edges form a cut between begin and end
	 * nodes. They are a cut if by removing all the specified nodes and edges there
	 * is no longer a path between begin and end. Its important to note that when
	 * removing a node that node will also be removed as an end point from all
	 * edges. If as a result a edge is left with a single end point then that edge
	 * will also be removed even if it isnt speciically listed in edges.
	 *
	 * @param nodes set of nodes to remove when checking for a cut.
	 * @param edges set of edges to remove when checking for a cut.
	 * @param begin begining node in the path to check for a cut.
	 * @param end ending node in the path to check for a cut.
	 * @return true if the nodes and edges form a cut between begin and end, false
	 *         otherwise.
	 * @throws IllegalArgumentException if any of nodes, edges, begin, or end are
	 * not in this graph or begin and end have no path between them before the
	 * cut.
	 * @since 2.0
	 */
	boolean isCut(Set<N> nodes, Set<E> edges, N begin, N end);
	/**
	 * Determines if the set of edges form a cut between begin and end nodes. They
	 * are a cut if by removing all the specified edges there is no longer a path
	 * between begin and end.
	 *
	 * @param edges set of edges to remove when checking for a cut.
	 * @param begin begining node in the path to check for a cut.
	 * @param end ending node in the path to check for a cut.
	 * @return true if the edges form a cut between begin and end, false
	 *         otherwise.
	 * @throws IllegalArgumentException if any of nodes, edges, begin, or end are
	 * not in this graph or begin and end have no path between them before the
	 * cut.
	 * @since 2.0
	 */
	boolean isCut(Set<E> edges, N begin, N end);
	/**
	 * Determines if the node is a cut node between begin and end nodes. It is a
	 * cut if by removing the specified node there is no longer a path between
	 * begin and end. Its important to note that when removing the node that node
	 * will also be removed as an end point from all edges. If as a result a edge
	 * is left with a single end point then that edge will also be removed even if
	 * it isnt speciically listed in edges.
	 *
	 * @param node node to remove when checking for a cut.
	 * @param begin begining node in the path to check for a cut.
	 * @param end ending node in the path to check for a cut.
	 * @return true if the node forms a cut between begin and end, false
	 *         otherwise.
	 * @throws IllegalArgumentException if any of nodes, edges, begin, or end are
	 * not in this graph or begin and end have no path between them before the
	 * cut.
	 * @since 2.0
	 */
	boolean isCut(N node, N begin, N end);
	/**
	 * Determines if the edge is a cut edge between begin and end nodes. It is a
	 * cut if by removing the specified edge there is no longer a path between
	 * begin and end.
	 *
	 * @param edge edge to remove when checking for a cut.
	 * @param begin begining node in the path to check for a cut.
	 * @param end ending node in the path to check for a cut.
	 * @return true if the edge forms a cut between begin and end, false
	 *         otherwise.
	 * @throws IllegalArgumentException if any of nodes, edges, begin, or end are
	 * not in this graph or begin and end have no path between them before the
	 * cut.
	 * @since 2.0
	 */
	boolean isCut(E edge, N begin, N end);
	/**
	 * Determines the minimum number of nodes that must be removed from the graph
	 * in order to form a cut. A cut is where the total number of maximally
	 * connected components is increased due to removing nodes or edges. Its
	 * important to note that when removing a node that node will also be removed
	 * as an end point from all edges. If as a result a edge is left with a single
	 * end point then that edge will also be removed.
	 *
	 * @return The minimum number of nodes that must be removed from the graph in
	 *         order to form a cut.
	 * @since 2.0
	 */
	int getNodeConnectivity();
	/**
	 * Determines the minimum number of edges that must be removed from the graph
	 * in order to form a cut. A cut is where the total number of maximally
	 * connected components is increased due to removing nodes or edges.
	 *
	 * @return The minimum number of edges that must be removed from the graph in
	 *         order to form a cut.
	 * @since 2.0
	 */
	int getEdgeConnectivity();
	/**
	 * Determines the minimum number of nodes that must be removed from the graph
	 * in order to form a cut between the specified nodes. A cut is where there is
	 * no longer a path between begin and end. If there is no path between begin
	 * and end this returns 0. Its important to note that when removing a node that
	 * node will also be removed as an end point from all edges. If as a result a
	 * edge is left with a single end point then that edge will also be removed.
	 *
	 * @param begin begining node in the path to check for connectivity.
	 * @param end ending node in the path to check for connectivity.
	 * @return The minimum number of nodes that must be removed from the graph in
	 *         order to form a cut between the specified nodes.
	 * @throws IllegalArgumentException if either begin or end is not in the
	 * graph.
	 * @since 2.0
	 */
	int getNodeConnectivity(N begin, N end);
	/**
	 * Determines the minimum number of edges that must be removed from the graph
	 * in order to form a cut between the specified nodes. A cut is where there is
	 * no longer a path between begin and end. If there is no path between begin
	 * and end this returns 0;
	 *
	 * @param begin begining node in the path to check for connectivity.
	 * @param end ending node in the path to check for connectivity.
	 * @return The minimum number of edges that must be removed from the graph in
	 *         order to form a cut between the specified nodes.
	 * @throws IllegalArgumentException if either begin or end is not in the
	 * graph.
	 * @since 2.0
	 */
	int getEdgeConnectivity(N begin, N end);
	/**
	 * Check if graph is complete, another words a simple graph where every node is
	 * connected by an edge to every node (adjacent). Not every node needs to have
	 * an edge that can be traversed to every other node so long as an edge
	 * exists.
	 *
	 * @return true if this is a simple graph where every node is connected by an
	 *         edge to every other node(adjacent).
	 * @since 2.0
	 */
	boolean isComplete();
}
