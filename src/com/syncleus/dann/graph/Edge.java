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

import com.syncleus.dann.graph.xml.EdgeElementXml;
import com.syncleus.dann.graph.xml.EdgeXml;
import com.syncleus.dann.xml.XmlSerializable;

import java.io.Serializable;
import java.util.List;

public interface Edge<N> extends Serializable, Cloneable, XmlSerializable<EdgeXml>
{
	List<N> getNodes();
	List<N> getTraversableNodes(N node);
	boolean isTraversable(N node);
	/**
	 * returns an edge with the specified node disconnected, null if the entire
	 * edge should be deleted as a result of removing the specified node.
	 *
	 * @param node node to remove from the returned edge.
	 * @return an edge with the specified node disconnected, null if the entire
	 *         edge should be deleted as a result of removing the specified node.
	 * @since 2.0
	 */
	Edge<N> disconnect(N node);
	Edge<N> disconnect(List<N> node);
	Edge<N> clone();
}
