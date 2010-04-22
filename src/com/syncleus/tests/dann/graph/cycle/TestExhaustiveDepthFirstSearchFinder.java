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
 *  findCycles a license:                                                            *
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

public class TestExhaustiveDepthFirstSearchFinder
{
	@Test
	public void testDirectedNoCycles()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object topNode = new Object();
		nodes.add(topNode);
		Object leftNode = new Object();
		nodes.add(leftNode);
		Object rightNode = new Object();
		nodes.add(rightNode);

		Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		DirectedEdge<Object> topRightEdge = new SimpleDirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		DirectedEdge<Object> rightLeftEdge = new SimpleDirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		DirectedEdge<Object> topLeftEdge = new SimpleDirectedEdge<Object>(topNode, leftNode);
		edges.add(topLeftEdge);

		BidirectedGraph<Object, DirectedEdge<Object>> graph = new SimpleDirectedGraph<Object, DirectedEdge<Object>>(nodes, edges);

		CycleFinder counter = new ExhaustiveDepthFirstSearchCycleFinder();
		Assert.assertTrue("Cycles detected when there should be none: " + counter.cycleCount(graph), counter.cycleCount(graph) == 0);
	}

	@Test
	public void testDirectedWithCycles()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object tippyTopNode = new Object();
		nodes.add(tippyTopNode);
		Object topNode = new Object();
		nodes.add(topNode);
		Object leftNode = new Object();
		nodes.add(leftNode);
		Object rightNode = new Object();
		nodes.add(rightNode);
		Object bottomNode = new Object();
		nodes.add(bottomNode);

		Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		DirectedEdge<Object> bottomLeftEdge = new SimpleDirectedEdge<Object>(bottomNode, leftNode);
		edges.add(bottomLeftEdge);
		DirectedEdge<Object> letRightEdge = new SimpleDirectedEdge<Object>(leftNode, rightNode);
		edges.add(letRightEdge);
		DirectedEdge<Object> rightBottomEdge = new SimpleDirectedEdge<Object>(rightNode, bottomNode);
		edges.add(rightBottomEdge);
		DirectedEdge<Object> leftTopEdge = new SimpleDirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);
		DirectedEdge<Object> topRightEdge = new SimpleDirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		DirectedEdge<Object> leftTippyTopEdge = new SimpleDirectedEdge<Object>(leftNode, tippyTopNode);
		edges.add(leftTippyTopEdge);
		DirectedEdge<Object> tippyTopRightEdge = new SimpleDirectedEdge<Object>(tippyTopNode, rightNode);
		edges.add(tippyTopRightEdge);

		BidirectedGraph<Object, DirectedEdge<Object>> graph = new SimpleDirectedGraph<Object, DirectedEdge<Object>>(nodes, edges);

		CycleFinder counter = new ExhaustiveDepthFirstSearchCycleFinder();
		Assert.assertTrue("incorrect number of cycles detected. Expected 3, got: " + counter.cycleCount(graph), counter.cycleCount(graph) == 3);
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
		BidirectedEdge<Object> centerTopEdge = new SimpleUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		BidirectedEdge<Object> centerLeftEdge = new SimpleUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		BidirectedEdge<Object> centerRightEdge = new SimpleUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);

		Graph<Object, BidirectedEdge<Object>> graph = new SimpleGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		CycleFinder counter = new ExhaustiveDepthFirstSearchCycleFinder();
		Assert.assertTrue("Cycles detected when there should be none: " + counter.cycleCount(graph), counter.cycleCount(graph) == 0);
	}

	@Test
	public void testUndirectedWithCycles()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object bottomNode = new Object();
		nodes.add(bottomNode);
		Object topNode = new Object();
		nodes.add(topNode);
		Object leftNode = new Object();
		nodes.add(leftNode);
		Object rightNode = new Object();
		nodes.add(rightNode);

		Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		BidirectedEdge<Object> rightBottomEdge = new SimpleUndirectedEdge<Object>(rightNode, bottomNode);
		edges.add(rightBottomEdge);
		BidirectedEdge<Object> bottomLeftEdge = new SimpleUndirectedEdge<Object>(bottomNode, leftNode);
		edges.add(bottomLeftEdge);
		BidirectedEdge<Object> topRightEdge = new SimpleUndirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		BidirectedEdge<Object> rightLeftEdge = new SimpleUndirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		BidirectedEdge<Object> leftTopEdge = new SimpleUndirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);

		Graph<Object, BidirectedEdge<Object>> graph = new SimpleGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		CycleFinder counter = new ExhaustiveDepthFirstSearchCycleFinder();
		Assert.assertTrue("incorrect number of cycles detected. Expected 3, got: " + counter.cycleCount(graph), counter.cycleCount(graph) == 3);
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
		BidirectedEdge<Object> centerTopEdge = new SimpleUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		BidirectedEdge<Object> centerLeftEdge = new SimpleUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		BidirectedEdge<Object> centerRightEdge = new SimpleUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		BidirectedEdge<Object> centerRightEdge2 = new SimpleUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge2);

		Graph<Object, BidirectedEdge<Object>> graph = new SimpleGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		CycleFinder counter = new ExhaustiveDepthFirstSearchCycleFinder();
		Assert.assertTrue("incorrect number of cycles detected. Expected 1, got: " + counter.cycleCount(graph), counter.cycleCount(graph) == 1);
	}
}
