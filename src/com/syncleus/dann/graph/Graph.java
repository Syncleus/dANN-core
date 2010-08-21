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

import com.syncleus.dann.graph.xml.GraphXml;
import com.syncleus.dann.xml.XmlSerializable;
import java.io.Serializable;
import java.util.*;

// TODO consider making all nodes extend from a connectable interface so you can embed other graphs as nodes if they too are connectable.

/**
 * Represents a graph as a collection of nodes connected by edges. A graph does
 * not need to contain any nodes or edges however if there is atleast one edge
 * then there must be atleast one node. There can, however, be one or more nodes
 * with no edges present. Each edge must have 2 or more nodes it connects,
 * however they do not need to be different nodes. The implementation defines if
 * and how a graph can be traversed across nodes and edges.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public interface Graph<N, E extends Edge<N>> extends Serializable, Cloneable, XmlSerializable<GraphXml, Object>
{
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
	List<N> getAdjacentNodes(N node);
	/**
	 * Get a set of all edges which is connected to node (adjacent). You may not be
	 * able to traverse from the specified node to all of these edges returned. If
	 * you only want edges you can traverse then see getTraversableEdges.
	 *
	 * @param node the end point for all edges to retrieve.
	 * @return An unmodifiable set of all edges that has node as an end point.
	 * @throws IllegalArgumentException if specified node is not in the graph.
	 * @see Graph#getTraversableEdges
	 * @since 2.0
	 */
	Set<E> getAdjacentEdges(N node);
	/**
	 * Get a list of all reachable nodes adjacent to node. All edges connected to
	 * node and is traversable from node will have its destination node(s) added to
	 * the returned list. node itself will appear in the list once for every loop.
	 * If there are multiple edges connecting node with a particular end point then
	 * the end point will appear multiple times in the list, once for each hop to
	 * the end point.
	 *
	 * @param node The whose traversable neighbors are to be returned.
	 * @return A list of all nodes adjacent to the specified node and traversable
	 *         from the spevified node, empty set if the node has no edges.
	 * @since 2.0
	 */
	List<N> getTraversableNodes(N node);
	/**
	 * Get a set of all edges which you can traverse from node. Of course node will
	 * always be an end point for each edge returned. Throws an
	 * IllegalArgumentException if node is not in the graph.
	 *
	 * @param node edges returned will be traversable from this node.
	 * @return An unmodifiable set of all edges that can be traversed from node.
	 * @since 2.0
	 */
	Set<E> getTraversableEdges(N node);
	/**
	 * Determines if the graph has nodeContext enabled. If node context is enabled
	 * then all nodes which implement the ContextNode interface will be notified
	 * on context events (which graphs it is added or removed to as well as
	 * which edges a node is connected to). This also allows nodes to refuse
	 * to join networks or edges. When this is disabled the ContextNode interface
	 * is ignored and nodes will not be notified and they will have no control
	 * over their context.
	 *
	 * @return True if ContextNode is currently being honored on all nodes, false
	 * otherwise.
	 * @since 2.0
	 */
	boolean isContextEnabled();
	/**
	 * Adds the specified edge to a clone of this class.
	 *
	 * @param newEdge the edge to add to the cloned graph.
	 * @return a clone of this graph with the specified edge added to it. null if
	 *         the edge already exists.
	 * @since 2.0
	 */
	Graph<N, E> cloneAdd(E newEdge);
	/**
	 * Adds the specified node to a clone of this class.
	 *
	 * @param newNode the node to add to the cloned graph.
	 * @return a clone of this graph with the specified node added to it.
	 * @since 2.0
	 */
	Graph<N, E> cloneAdd(N newNode);
	/**
	 * Adds the specified nodes and edges to a clone of this class.
	 *
	 * @param newNodes the nodes to add to the cloned graph.
	 * @param newEdges the edges to add to the cloned graph.
	 * @return a clone of this graph with the specified nodes and edges added to
	 *         it.
	 * @since 2.0
	 */
	Graph<N, E> cloneAdd(Set<N> newNodes, Set<E> newEdges);
	/**
	 * Removed the specified edge from a clone of this class.
	 *
	 * @param edgeToRemove the edge to remove from the cloned graph.
	 * @return a clone of this graph with the specified edge removed to it.
	 * @since 2.0
	 */
	Graph<N, E> cloneRemove(E edgeToRemove);
	/**
	 * Removed the specified edge to a clone of this class.
	 *
	 * @param nodeToRemove the edge to remove from the cloned graph.
	 * @return a clone of this graph with the specified edge removed from it.
	 * @since 2.0
	 */
	Graph<N, E> cloneRemove(N nodeToRemove);
	/**
	 * Removed the specified nodes and edges from a clone of this class.
	 *
	 * @param deleteNodes the nodes to remove from the cloned graph.
	 * @param deleteEdges the edges to remove from the cloned graph.
	 * @return a clone of this graph with the specified nodes and edges removed
	 *         from it.
	 * @since 2.0
	 */
	Graph<N, E> cloneRemove(Set<N> deleteNodes, Set<E> deleteEdges);
	Graph<N, E> clone();
}
