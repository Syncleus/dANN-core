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

import com.syncleus.dann.graph.context.ContextReporter;
import java.io.Serializable;
import java.util.Set;

/**
 * Represents a graph as a collection of nodes connected by edges. A graph does
 * not need to contain any nodes or edges however if there is at least one edge
 * then there must be at least one node. There can, however, be one or more
 * nodes with no edges present. Each edge must have 2 or more nodes it connects,
 * however they do not need to be different nodes. The implementation defines if
 * and how a graph can be traversed across nodes and edges.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 * @param <N> The node type
 * @param <E> The type of edge for the given node type
 */
public interface Graph<
	  	N,
	  	E extends Edge<N,? extends Edge.Endpoint<N>>,
	  	NEP extends Graph.NodeEndpoint<N, E>,
	  	EEP extends Graph.EdgeEndpoint<N, E>
	  > extends Edge<Object,Graph.Endpoint<N,E,?>>, Serializable, Cloneable, ContextReporter
{
	interface Endpoint<
		ON,
	  	OE extends Edge<ON,? extends Edge.Endpoint<ON>>,
	  	T
	  > extends Edge.Endpoint<T>
	{
		Set<Graph.Endpoint<ON,OE,T>> getAdjacent();
		Set<Graph.Endpoint<ON,OE,T>> getTraversableAdjacentTo();
		Set<Graph.Endpoint<ON,OE,T>> getTraversableAdjacentFrom();

		Set<Graph.NodeEndpoint<ON, OE>> getAdjacentNodes();
		Set<Graph.NodeEndpoint<ON, OE>> getTraversableAdjacentNodesTo();
		Set<Graph.NodeEndpoint<ON, OE>> getTraversableAdjacentNodesFrom();

		Set<Graph.EdgeEndpoint<ON, OE>> getAdjacentEdges();
		Set<Graph.EdgeEndpoint<ON, OE>> getTraversableAdjacentEdgesTo();
		Set<Graph.EdgeEndpoint<ON, OE>> getTraversableAdjacentEdgesFrom();
	};

	interface NodeEndpoint<
		  ON,
		  OE extends Edge<ON,? extends Edge.Endpoint<ON>>
	  > extends Graph.Endpoint<ON,OE,ON>
	{
	};

	interface EdgeEndpoint<
		  	ON,
		  	OE extends Edge<ON,? extends Edge.Endpoint<ON>>
		> extends Graph.Endpoint<ON,OE,OE>
	{
	};

	Set<EEP> getEdgeEndpoints();
	Set<EEP> getEdgeEndpoints(Edge<?,? extends Edge.Endpoint<?>> edge);

	Set<NEP> getNodeEndpoints();
	Set<NEP> getNodeEndpoints(Object node);

	/**
	 * Get a set of all nodes in the graph.
	 *
	 * @return An unmodifiable set of all nodes in the graph.
	 * @since 2.0
	 */
	Set<N> getNodes();
	/**
	 * Get a set of all edges in the graph. Two edges in the set, and in the graph,
	 * may have the same end points unless equals in the edges used by this graph
	 * restrict that possiblity.
	 *
	 * @return An unmodifiable set of a all edges in the graph.
	 * @since 2.0
	 */
	Set<E> getEdges();
	/**
	 * Get a list of all nodes adjacent to the specified node. All edges connected
	 * to this node has its other end points added to the list returned. The
	 * specified node itself will appear in the list once for every loop. If there
	 * are multiple edges connecting node with a particular end point it will
	 * appear multiple times in the list, once for each hop to the end point.
	 *
	 * @param node The whose neighbors are to be returned.
	 * @return A list of all nodes adjacent to the specified node, empty set if the
	 *         node has no edges.
	 * @since 2.0
	 */
	Set<N> getAdjacentNodes(Object node);
	/**
	 * Get a set of all edges which is connected to node (adjacent). You may not be
	 * able to traverse from the specified node to all of these edges returned. If
	 * you only want edges you can traverse then see getTraversableAdjacentEdges.
	 *
	 * @param node the end point for all edges to retrieve.
	 * @return An unmodifiable set of all edges that has node as an end point.
	 * @throws IllegalArgumentException if specified node is not in the graph.
	 * @since 2.0
	 */
	Set<E> getAdjacentEdges(Object node);

	Set<E> getTraversableEdgesFrom(Object source);
	Set<E> getTraversableEdgesFrom(Edge<?,? extends Edge.Endpoint<?>> source);
	Set<E> getTraversableEdgesTo(Object destination);
	Set<E> getTraversableEdgesTo(Edge<?,? extends Edge.Endpoint<?>> destination);

	Set<N> getTraversableNodesFrom(Object source);
	Set<N> getTraversableNodesFrom(Edge<?,? extends Edge.Endpoint<?>> source);
	Set<N> getTraversableNodesTo(Object destination);
	Set<N> getTraversableNodesTo(Edge<?,? extends Edge.Endpoint<?>> destination);

	Set<E> getTraversableAdjacentEdgesFrom(Object source);
	Set<E> getTraversableAdjacentEdgesFrom(Edge<?,? extends Edge.Endpoint<?>> source);
	Set<E> getTraversableAdjacentEdgesTo(Object destination);
	Set<E> getTraversableAdjacentEdgesTo(Edge<?,? extends Edge.Endpoint<?>> destination);

	Set<N> getTraversableAdjacentNodesFrom(Object source);
	Set<N> getTraversableAdjacentNodesFrom(Edge<?,? extends Edge.Endpoint<?>> source);

	/**
	 * Get a list of all reachable nodes adjacent to node. All edges connected to
	 * node and is traversable from node will have its destination node(s) added to
	 * the returned list. node itself will appear in the list once for every loop.
	 * If there are multiple edges connecting node with a particular end point then
	 * the end point will appear multiple times in the list, once for each hop to
	 * the end point.
	 *
	 * @param destination The whose traversable neighbors are to be returned.
	 * @return A list of all nodes adjacent to the specified node and traversable
	 *         from the spevified node, empty set if the node has no edges.
	 * @since 2.0
	 */
	Set<N> getTraversableAdjacentNodesTo(Object destination);

	/**
	 * Get a set of all edges which you can traverse from node. Of course node will
	 * always be an end point for each edge returned. Throws an
	 * IllegalArgumentException if node is not in the graph.
	 *
	 * @param destination edges returned will be traversable from this node.
	 * @return An unmodifiable set of all edges that can be traversed from node.
	 * @since 2.0
	 */
	Set<N> getTraversableAdjacentNodesTo(Edge<?,? extends Edge.Endpoint<?>> destination);
}
