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

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public interface ConnectionismOptimizedGraph<N, E extends Edge<N>> extends Graph<N, E>
{
	/**
	 * The total degree of the specified node. This is essentially the number of
	 * edges which has node as an end point. This will always be equal to
	 * getAdjacentEdges().size(). Throws an IllegalArgumentException if node is not
	 * in the graph.
	 *
	 * @param node The node whose degree is to be returned
	 * @return the degree of the specified node.
	 * @see com.syncleus.dann.graph.Graph#getAdjacentEdges
	 * @since 2.0
	 */
	int getDegree(N node);
	/**
	 * Gets the order of the graph, this is the same as the number of nodes in the
	 * graph.
	 *
	 * @return The order of the graph.
	 * @since 2.0
	 */
	int getOrder();
	/**
	 * Determines the smallest degree of all the nodes present in the graph. The
	 * graph must contain atleast one node.
	 *
	 * @return the smallest degree of all the nodes present in the graph.
	 * @throws IllegalStateException thrown if there are no nodes in the graph.
	 * @since 2.0
	 */
	int getMinimumDegree();
	/**
	 * Determines the regular degree if there is one, if all nodes are the same
	 * degree return their degree, if they have varying degrees then return -1.
	 * There must be atleast one node in the graph.
	 *
	 * @return the regular degree of the graph if there is one, -1 otherwise.
	 * @throws IllegalStateException thrown if there are no nodes in the graph.
	 * @since 2.0
	 */
	int getRegularDegree();
	/**
	 * Determines if the edge is the only edge with its particular set of end point
	 * nodes, false if unique, true if not. If there is another edge in the graph
	 * with the exact same set of nodes, no more and no less, then returns true,
	 * otherwise false.
	 *
	 * @param edge the edge to check if it is multiple.
	 * @return true if there is another edge in the graph with the exact same set
	 *         of nodes.
	 * @throws IllegalArgumentException if the specified edge is not present in the
	 * graph.
	 * @since 2.0
	 */
	boolean isMultiple(E edge);
	/**
	 * Calculates the number of edges in the graph with the exact set of end nodes
	 * as the specified edge, not including the specified edge itself.
	 *
	 * @param edge the edge of which the multiplicity is to be calculated.
	 * @return the number of edges in the graph with the exact set of end nodes as
	 *         the specified edge, not including the specified edge itself.
	 * @throws IllegalArgumentException if the specified edge is not present in the
	 * graph.
	 * @since 2.0
	 */
	int getMultiplicity(E edge);
	/**
	 * Determined the largest multiplicty of any node in the graph and return it.
	 * Returns 0 if there are no edges.
	 *
	 * @return the largest multiplicty of any node in the graph and return it.
	 * @since 2.0
	 */
	int getMultiplicity();
	/**
	 * Determins if this graph has multiplicity greater than 0, but no loops. If
	 * there are no edges it returns false.
	 *
	 * @return true if this graph has multiplicity greater than 0, but no loops.
	 * @since 2.0
	 */
	boolean isMultigraph();
	/**
	 * Determins if every node in this graph has the same degree. If there are no
	 * nodes in the graph this will return true.
	 *
	 * @return true if every node in this graph has the same degree or there are no
	 *         nodes, false otherwise.
	 * @since 2.0
	 */
	boolean isRegular();
	/**
	 * Determines if graph has no loops, and all edges have a multiplicity of 0.
	 * Simple graphs can not have two nodes connected by two edges differing only
	 * by its direction/navigability.
	 *
	 * @return true if graph has no loops, and all edges have a multiplicity of 0.
	 * @since 2.0
	 */
	boolean isSimple();
}
