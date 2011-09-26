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
package com.syncleus.dann.graph.cycle;

import java.util.*;
import com.syncleus.dann.graph.*;
import org.junit.*;

public class TestColoredDepthFirstSearchDetector
{
	@Test
	public void testDirectedNoCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object centerNode = new Object();
		nodes.add(centerNode);
		final Object topNode = new Object();
		nodes.add(topNode);
		final Object leftNode = new Object();
		nodes.add(leftNode);
		final Object rightNode = new Object();
		nodes.add(rightNode);

		final Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		final DirectedEdge<Object> centerTopEdge = new SimpleDirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		final DirectedEdge<Object> centerLeftEdge = new SimpleDirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		final DirectedEdge<Object> centerRightEdge = new SimpleDirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);

		final BidirectedGraph<Object, DirectedEdge<Object>> graph = new ImmutableDirectedAdjacencyGraph<Object, DirectedEdge<Object>>(nodes, edges);

		final CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertFalse("cycle detected when there should be none.", detector.hasCycle(graph));
	}

	@Test
	public void testDirectedWithCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object centerNode = new Object();
		nodes.add(centerNode);
		final Object topNode = new Object();
		nodes.add(topNode);
		final Object leftNode = new Object();
		nodes.add(leftNode);
		final Object rightNode = new Object();
		nodes.add(rightNode);

		final Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		final DirectedEdge<Object> centerTopEdge = new SimpleDirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		final DirectedEdge<Object> centerLeftEdge = new SimpleDirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		final DirectedEdge<Object> centerRightEdge = new SimpleDirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		final DirectedEdge<Object> topRightEdge = new SimpleDirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		final DirectedEdge<Object> rightLeftEdge = new SimpleDirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		final DirectedEdge<Object> leftTopEdge = new SimpleDirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);

		final BidirectedGraph<Object, DirectedEdge<Object>> graph = new ImmutableDirectedAdjacencyGraph<Object, DirectedEdge<Object>>(nodes, edges);

		final CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertTrue("cycle not detected when there should be one.", detector.hasCycle(graph));
	}

	@Test
	public void testUndirectedNoCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object centerNode = new Object();
		nodes.add(centerNode);
		final Object topNode = new Object();
		nodes.add(topNode);
		final Object leftNode = new Object();
		nodes.add(leftNode);
		final Object rightNode = new Object();
		nodes.add(rightNode);

		final Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		final BidirectedEdge<Object> centerTopEdge = new SimpleUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		final BidirectedEdge<Object> centerLeftEdge = new SimpleUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		final BidirectedEdge<Object> centerRightEdge = new SimpleUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);

		final CloudGraph<Object, BidirectedEdge<Object>> graph = new ImmutableAdjacencyGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		final CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertFalse("cycle detected when there should be none.", detector.hasCycle(graph));
	}

	@Test
	public void testUndirectedWithCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object centerNode = new Object();
		nodes.add(centerNode);
		final Object topNode = new Object();
		nodes.add(topNode);
		final Object leftNode = new Object();
		nodes.add(leftNode);
		final Object rightNode = new Object();
		nodes.add(rightNode);

		final Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		final BidirectedEdge<Object> centerTopEdge = new SimpleUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		final BidirectedEdge<Object> centerLeftEdge = new SimpleUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		final BidirectedEdge<Object> centerRightEdge = new SimpleUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		final BidirectedEdge<Object> topRightEdge = new SimpleUndirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		final BidirectedEdge<Object> rightLeftEdge = new SimpleUndirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		final BidirectedEdge<Object> leftTopEdge = new SimpleUndirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);

		final CloudGraph<Object, BidirectedEdge<Object>> graph = new ImmutableAdjacencyGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		final CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertTrue("cycle not detected when there should be one.", detector.hasCycle(graph));
	}

	@Test
	public void testUndirectedWithDoubleEdgeCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object centerNode = new Object();
		nodes.add(centerNode);
		final Object topNode = new Object();
		nodes.add(topNode);
		final Object leftNode = new Object();
		nodes.add(leftNode);
		final Object rightNode = new Object();
		nodes.add(rightNode);

		final Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		final BidirectedEdge<Object> centerTopEdge = new SimpleUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		final BidirectedEdge<Object> centerLeftEdge = new SimpleUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		final BidirectedEdge<Object> centerRightEdge = new SimpleUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		final BidirectedEdge<Object> centerRightEdge2 = new SimpleUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge2);

		final CloudGraph<Object, BidirectedEdge<Object>> graph = new ImmutableAdjacencyGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		final CycleDetector detector = new ColoredDepthFirstSearchDetector();
		Assert.assertTrue("cycle not detected when there should be one.", detector.hasCycle(graph));
	}
}
