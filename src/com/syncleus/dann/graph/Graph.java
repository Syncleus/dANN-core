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

/**
 * Represents a graph as a collection of nodes connected by edges. A graph does
 * not need to contain any nodes or edges however if there is atleast one edge
 * then there must be atleast one node. There can, however, be one or more nodes
 * with no edges present. Each edge must have 2 or more nodes it connects,
 * however they do not need to be different nodes. The implementation defines
 * if and how a graph can be traversed across nodes and edges.
 *
 * @author Jeffrey Phillips Freeman
 * @param <N> The class represenging a node.
 * @param <E> The edge used to connect nodes.
 * @since 2.0
 */
public interface Graph<N, E extends Edge<N>>
{
	/**
	 * Get a set of all Nodes in the graph.
	 *
	 * @return An unmodifiable set of all nodes in the graph.
	 * @since 2.0
	 */
	Set<N> getNodes();

	/**
	 * Get a set of all edges in the graph. Two edges in the set, and in the
	 * graph, may have the same end points unless equals in the edges used by
	 * this graph restrict that possiblity.
	 *
	 * @return An unmodifiable set of a all edges in the graph.
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
	 */
	List<N> getTraversableNodes(N node);

	/**
	 * Get a set of all edges which you can traverse from node. Of course node
	 * will always be an end point for each edge returned. Throws an
	 * IllegalArgumentException if node is not in the graph.
	 *
	 * @param node edges returned will be traversable from this node.
	 * @return An unmodifiable set of all edges that can be traversed from node.
	 * @since 2.0
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
	 * @since 2.0
	 */
	int getDegree(N node);

	/**
	 * Determines if there is a path from the firstNode to the lastNode. There
	 * may be a path from firstNode to lastNode even if there is no path from
	 * lastNode to firstNode. This is because it only checks if there is a
	 * traversable path. Both nodes must be present in the graph or else
	 * an InvalidArgumentException will be thrown.
	 *
	 * @param firstNode begining node to find a path from.
	 * @param lastNode eding node to find a path to.
	 * @return true if a path exists, false otherwise.
	 * @since 2.0
	 */
	boolean isStronglyConnected(N firstNode, N lastNode);

	/**
	 * If there is atleast one path from every node in the graph to any other
	 * node in the graph then true, false otherwise. There must be a traversable
	 * path, not just a series of adjacency.
	 *
	 * @return true if the graph is connected, false otherwise.
	 * @since 2.0
	 */
	boolean isStronglyConnected();

	/**
	 * Determines if there is a path between the firstNode and the lastNode. If
	 * this returns true then reversing the arguments will also return true.
	 * This is because you only need to form a path between the specified nodes
	 * as if the graph were undirected.
	 *
	 * @throws IllegalArgumentException if either of the specified nodes is not
	 * in the graph.
	 * @param firstNode begining node to find a path from.
	 * @param lastNode eding node to find a path to.
	 * @return true if a path exists, false otherwise.
	 * @since 2.0
	 */
	boolean isWeaklyConnected(N firstNode, N lastNode);

	/**
	 * If there is atleast one path from every node in the graph to any other
	 * node in the graph, treating all edges as undirected edges, then true,
	 * false otherwise.
	 *
	 * @see Graph#isStronglyConnected
	 * @return true if the graph is connected, false otherwise.
	 * @since 2.0
	 */
	boolean isWeaklyConnected();

	/**
	 * Obtains a set of all Maximally connected componets in the graph. A
	 * subgraph is a maximally connected component if it is a weakly connected
	 * subgraph of this graph and to which no vertex can be added from this
	 * graph and still be weakly connected. 
	 *
	 * @return a unmodifiable set of all the maximally connected components.
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
	 */
	boolean isCut(Set<N> nodes, Set<E> edges);

	/**
	 * Determines if the set of edges form a cut. They are a cut
	 * if by removing all the specified edges the number of maximally
	 * connected components is increased.
	 *
	 * @param edges set of edges to remove when checking for a cut.
	 * @return true if the edges form a cut, false otherwise.
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
	 */
	int getEdgeConnectivity(N begin, N end);

	/**
	 * Check if graph is complete, another words a simple graph where every node
	 * is connected by an edge to every node (adjacent). Not every node needs to
	 * have an edge that can be traversed to every other node so long as an edge
	 * exists.
	 *
	 * @return true if this is a simple graph where every node is connected by
	 * an edge to every other node(adjacent).
	 * @since 2.0
	 */
	boolean isComplete();

	/**
	 * Gets the order of the graph, this is the same as the number of nodes in
	 * the graph.
	 *
	 * @return The order of the graph.
	 * @since 2.0
	 */
	int getOrder();

	/**
	 * Calculates the number of cylces int he graph. A cycle is any walk that
	 * starts and ends on the same node and does not repeat any nodes or edges.
	 * Two cycles are considered the same if they contain the sequence of nodes
	 * and edges even if their starting point is different. In fact cycles do
	 * not have a non-arbitrary starting and ending point. If this returns 0
	 * then isAcyclic will be true. If this returns 1 then isUnicyclic will be
	 * true.
	 *
	 * @return The number of cycles in the graph, 0 if the graph is acyclic, 1
	 * if unicyclic.
	 * @since 2.0
	 */
	int getCycleCount();

	/**
	 * Determines if there is a cycle of every possible length in the graph, not
	 * including lengths less than 3, and that the graph is simple. Therefore
	 * the graph can not have any loops. Returns true if the order of the graph
	 * is less than 3. Returns false if the graph is not simple.
	 *
	 * @return true there is a cycle of every possible length in the graph, not
	 * including lengths less than 3, and that the graph is simple.
	 * @since 2.0
	 */
	boolean isPancyclic();

	/**
	 * Determines if this is a simple graph with exactly one cycle. Since the
	 * graph must be simple it can not have any loops.
	 *
	 * @return true there is exactly one cycle and the graph is simple, false
	 * otherwise.
	 * @since 2.0
	 */
	boolean isUnicyclic();

	/**
	 * Determines if the graph contains no cycles or loops. Since the graph need
	 * not be a simple graph the graph can have multiple edges between nodes
	 * however there can not be loops if it is to qualify as acyclic.
	 *
	 * @return true if there are no cycles or loops, false otherwise.
	 * @since 2.0
	 */
	boolean isAcyclic();

	/**
	 * Finds the length of the shortest cycle in the graph, -1 if the graph is
	 * acyclic. If the graph contains any loops this should return 0.
	 *
	 * @return The length of the shortest cycle in the graph, -1 if the graph is
	 * acyclic.
	 * @since 2.0
	 */
	int getGirth();

	/**
	 * Finds the length of the longest cycle in the graph, -1 if the graph is
	 * acyclic.
	 *
	 * @return The length of the longest cycle in the graph, -1 if the graph is
	 * acyclic.
	 * @since 2.0
	 */
	int getCircumference();

	/**
	 * Determines if the specified graph is a spanning tree of this graph. This
	 * will return true if the specified graph contains all the nodes from this
	 * graph, is weakly connected, and is acyclic, otherwise it returns false.
	 *
	 * @param graph graph to check if it is a spanning tree of this.
	 * @return true if the specified graph contains all the nodes from this
	 * graph, is weakly connected, and is acyclic, otherwise it returns false.
	 * @since 2.0
	 */
	boolean isSpanningTree(Graph<N,E> graph);

	/**
	 * Determines if this graph is weakly connected, acyclic, and simple,
	 * another words a Tree. Some rooted tree's are directed trees, however
	 * not all directed tree's are rooted.
	 * 
	 * @return true if this graph is weakly connected, acyclic, and simple,
	 * another words a Tree.
	 * @since 2.0
	 */
	boolean isTree();

	/**
	 * Determined if this graph contains maximally connected components which
	 * are all Trees. If this grap is itself a tree this returns true.
	 *
	 * @return true if this graph contains maximally connected components which
	 * are all Trees.
	 * @since 2.0
	 */
	boolean isForest();

	/**
	 * Determines if the specified graph only contains nodes and edges that are
	 * also present in this graph. If graph is empty then this will always
	 * return true.
	 *
	 * @param graph
	 * @return true if the specified graph only contains nodes and edges that are
	 * also present in this graph.
	 * @since 2.0
	 */
	boolean isSubGraph(Graph<N,E> graph);

	/**
	 * Determines the smallest degree of all the nodes present in the graph. The
	 * graph must contain atleast one node.
	 *
	 * @throws IllegalStateException thrown if there are no nodes in the graph.
	 * @return the smallest degree of all the nodes present in the graph.
	 * @since 2.0
	 */
	int getMinimumDegree();

	/**
	 * Determines the regular degree if there is one, if all nodes are the same
	 * degree return their degree, if they have varying degrees then return -1.
	 * There must be atleast one node in the graph.
	 *
	 * @throws IllegalStateException thrown if there are no nodes in the graph.
	 * @return the regular degree of the graph if there is one, -1 otherwise.
	 * @since 2.0
	 */
	int getRegularDegree();

	/**
	 * Determines if the edge is the only edge with its particular set of end
	 * point nodes, false if unique, true if not. If there is another edge in
	 * the graph with the exact same set of nodes, no more and no less, then
	 * returns true, otherwise false.
	 *
	 * @throws IllegalArgumentException if the specified edge is not present in
	 * the graph.
	 * @param edge the edge to check if it is multiple.
	 * @return true if there is another edge in the graph with the exact same
	 * set of nodes.
	 * @since 2.0
	 */
	boolean isMultiple(E edge);

	/**
	 * Calculates the number of edges in the graph with the exact set of end
	 * nodes as the specified edge, not including the specified edge itself.
	 *
	 * @throws IllegalArgumentException if the specified edge is not present in
	 * the graph.
	 * @param edge the edge of which the multiplicity is to be calculated.
	 * @return the number of edges in the graph with the exact set of end
	 * nodes as the specified edge, not including the specified edge itself.
	 * @since 2.0
	 */
	int getMultiplicity(E edge);

	/**
	 * Determined the largest multiplicty of any node in the graph and return
	 * it. Returns 0 if there are no edges.
	 *
	 * @return the largest multiplicty of any node in the graph and return
	 * it.
	 * @since 2.0
	 */
	int getMultiplicity();

	/**
	 * Determins if this graph has multiplicity greater than 0, but no loops.
	 * If there are no edges it returns false.
	 *
	 * @return true if this graph has multiplicity greater than 0, but no loops.
	 * @since 2.0
	 */
	boolean isMultigraph();

	/**
	 * Determines if all the nodes in the specified graph contains all the same
	 * nodes as this graph and every pair of nodes that are adjacent in this
	 * graph are adjacent in the specified graph, and vice versa. For
	 * bidirected edges this means both graphs have equal nodes and the same number of edges each eith the same end points
	 * however the traversability of the edges may differ between graphs. In
	 * Hypergraphs there can be an entirely different number of edges and still
	 * be isomorphic. If there are no edges and/or there are no nodes in both
	 * graphs then they are isomorphic.
	 *
	 * @param isomorphicGraph graph to check if it is isomorphic with this
	 * graph.
	 * @return true if this graph and the specified graph have all the same
	 * nodes and all the same pairs of adjacecy betwee them. False otherwise.
	 * @since 2.0
	 */
	boolean isIsomorphic(Graph<N,E> isomorphicGraph);

	/**
	 * Determines if all the nodes in the specified graph contains all the same
	 * nodes as this graph and every pair of nodes that are adjacent in this
	 * graph are adjacent in the specified graph. Unlike isIsomorphic this is a
	 * one way comparison, so while every adjacent pair of nodes in this graph
	 * must be present int he specified graph, there may be adjacenct nodes in
	 * the specified graph that are not adjacent in this graph. That also means
	 * the specified graph may have nodes that dont exist in this graph, but
	 * every node in this graph must exist int he specified graph. If there are
	 * no edges and/or there are no nodes in both graphs then they are
	 * homomorphic.
	 *
	 * @param homomorphicGraph graph to check if it is homomorphic with this
	 * graph.
	 * @return true if every pair of adjacent nodes in this graph are also
	 * present and adjacent in the specified graph.
	 * @since 2.0
	 */
	boolean isHomomorphic(Graph<N,E> homomorphicGraph);

	/**
	 * Determins if every node in this graph has the same degree. If there are
	 * no nodes in the graph this will return true.
	 *
	 * @return true if every node in this graph has the same degree or there are
	 * no nodes, false otherwise.
	 * @since 2.0
	 */
	boolean isRegular();

	/**
	 * Determines if graph has no loops, and all edges have a multiplicity of
	 * 0. Simple graphs can not have two nodes connected by two edges differing
	 * only by its direction/navigability.
	 *
	 * @return true if graph has no loops, and all edges have a multiplicity of
	 * 0.
	 * @since 2.0
	 */
	boolean isSimple();
}
