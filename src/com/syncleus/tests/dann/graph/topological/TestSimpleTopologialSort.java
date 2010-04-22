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
package com.syncleus.tests.dann.graph.topological;

import com.syncleus.dann.graph.*;
import com.syncleus.dann.graph.topological.*;
import java.util.*;
import org.junit.*;

public class TestSimpleTopologialSort
{
	@Test
	public void testTopologicalSort()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object centerNode = "centerNode";
		nodes.add(centerNode);
		Object topNode = "topNode";
		nodes.add(topNode);
		Object leftNode = "leftNode";
		nodes.add(leftNode);
		Object leftiestNode = "leftiestNode";
		nodes.add(leftiestNode);
		Object rightNode = "rightNode";
		nodes.add(rightNode);

		Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		DirectedEdge<Object> centerTopEdge = new SimpleDirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		DirectedEdge<Object> centerLeftEdge = new SimpleDirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		DirectedEdge<Object> leftLeftiestEdge = new SimpleDirectedEdge<Object>(leftNode, leftiestNode);
		edges.add(leftLeftiestEdge);
		DirectedEdge<Object> centerRightEdge = new SimpleDirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);

		BidirectedGraph<Object, DirectedEdge<Object>> graph = new SimpleDirectedGraph<Object, DirectedEdge<Object>>(nodes, edges);

		TopologicalSorter<Object> sorter = new SimpleTopologicalSorter<Object>();
		List<Object> sortedNodes = sorter.sort(graph);

		Assert.assertTrue("center node is not the first node!", sortedNodes.get(0) == centerNode);
		Assert.assertTrue("left node is not before leftiest node!", sortedNodes.indexOf(leftNode) < sortedNodes.indexOf(leftiestNode));
	}
}
