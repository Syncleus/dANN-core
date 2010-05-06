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
package com.syncleus.tests.dann.graph.cycle;

import com.syncleus.dann.graph.*;
import com.syncleus.dann.graph.cycle.*;
import java.util.*;
import org.junit.*;

public class TestColoredDepthFirstSearchDetector
{
	@Test
	public void testDirectedNoCycles()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object centerNode = new Object();
		nodes.add(centerNode);
		Object topNode = new Object();
		nodes.add(topNode);
		Object leftNode = new Object();
		nodes.add(leftNode);
		Object rightNode = new Object();
		nodes.add(rightNode);

		Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		DirectedEdge<Object> centerTopEdge = new ImmutableDirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		DirectedEdge<Object> centerLeftEdge = new ImmutableDirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		DirectedEdge<Object> centerRightEdge = new ImmutableDirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);

		BidirectedGraph<Object, DirectedEdge<Object>> graph = new ImmutableDirectedGraph<Object, DirectedEdge<Object>>(nodes, edges);

		CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertFalse("cycle detected when there should be none.", detector.hasCycle(graph));
	}

	@Test
	public void testDirectedWithCycles()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object centerNode = new Object();
		nodes.add(centerNode);
		Object topNode = new Object();
		nodes.add(topNode);
		Object leftNode = new Object();
		nodes.add(leftNode);
		Object rightNode = new Object();
		nodes.add(rightNode);

		Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		DirectedEdge<Object> centerTopEdge = new ImmutableDirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		DirectedEdge<Object> centerLeftEdge = new ImmutableDirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		DirectedEdge<Object> centerRightEdge = new ImmutableDirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		DirectedEdge<Object> topRightEdge = new ImmutableDirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		DirectedEdge<Object> rightLeftEdge = new ImmutableDirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		DirectedEdge<Object> leftTopEdge = new ImmutableDirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);

		BidirectedGraph<Object, DirectedEdge<Object>> graph = new ImmutableDirectedGraph<Object, DirectedEdge<Object>>(nodes, edges);

		CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertTrue("cycle not detected when there should be one.", detector.hasCycle(graph));
	}

	@Test
	public void testUndirectedNoCycles()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object centerNode = new Object();
		nodes.add(centerNode);
		Object topNode = new Object();
		nodes.add(topNode);
		Object leftNode = new Object();
		nodes.add(leftNode);
		Object rightNode = new Object();
		nodes.add(rightNode);

		Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		BidirectedEdge<Object> centerTopEdge = new ImmutableUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		BidirectedEdge<Object> centerLeftEdge = new ImmutableUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		BidirectedEdge<Object> centerRightEdge = new ImmutableUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);

		Graph<Object, BidirectedEdge<Object>> graph = new ImmutableGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertFalse("cycle detected when there should be none.", detector.hasCycle(graph));
	}

	@Test
	public void testUndirectedWithCycles()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object centerNode = new Object();
		nodes.add(centerNode);
		Object topNode = new Object();
		nodes.add(topNode);
		Object leftNode = new Object();
		nodes.add(leftNode);
		Object rightNode = new Object();
		nodes.add(rightNode);

		Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		BidirectedEdge<Object> centerTopEdge = new ImmutableUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		BidirectedEdge<Object> centerLeftEdge = new ImmutableUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		BidirectedEdge<Object> centerRightEdge = new ImmutableUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		BidirectedEdge<Object> topRightEdge = new ImmutableUndirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		BidirectedEdge<Object> rightLeftEdge = new ImmutableUndirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		BidirectedEdge<Object> leftTopEdge = new ImmutableUndirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);

		Graph<Object, BidirectedEdge<Object>> graph = new ImmutableGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertTrue("cycle not detected when there should be one.", detector.hasCycle(graph));
	}

	@Test
	public void testUndirectedWithDoubleEdgeCycles()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object centerNode = new Object();
		nodes.add(centerNode);
		Object topNode = new Object();
		nodes.add(topNode);
		Object leftNode = new Object();
		nodes.add(leftNode);
		Object rightNode = new Object();
		nodes.add(rightNode);

		Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		BidirectedEdge<Object> centerTopEdge = new ImmutableUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		BidirectedEdge<Object> centerLeftEdge = new ImmutableUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		BidirectedEdge<Object> centerRightEdge = new ImmutableUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		BidirectedEdge<Object> centerRightEdge2 = new ImmutableUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge2);

		Graph<Object, BidirectedEdge<Object>> graph = new ImmutableGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertTrue("cycle not detected when there should be one.", detector.hasCycle(graph));
	}
}
