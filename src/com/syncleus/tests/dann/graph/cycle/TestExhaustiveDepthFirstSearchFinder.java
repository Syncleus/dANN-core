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
import org.apache.log4j.Logger;
import org.junit.*;

public class TestExhaustiveDepthFirstSearchFinder
{
	private final static Logger LOGGER = Logger.getLogger(TestExhaustiveDepthFirstSearchFinder.class);

	@Test
	public void testDirectedNoCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object topNode = "topNode";
		nodes.add(topNode);
		final Object leftNode = "leftNode";
		nodes.add(leftNode);
		final Object rightNode = "rightNode";
		nodes.add(rightNode);

		final Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		final DirectedEdge<Object> topRightEdge = new ImmutableDirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		final DirectedEdge<Object> rightLeftEdge = new ImmutableDirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		final DirectedEdge<Object> topLeftEdge = new ImmutableDirectedEdge<Object>(topNode, leftNode);
		edges.add(topLeftEdge);

		final BidirectedGraph<Object, DirectedEdge<Object>> graph = new ImmutableDirectedAdjacencyGraph<Object, DirectedEdge<Object>>(nodes, edges);

		final CycleFinder<Object, DirectedEdge<Object>> finder = new ExhaustiveDepthFirstSearchCycleFinder<Object, DirectedEdge<Object>>();
		LOGGER.info("testDirectedNoCycles cycles: ");
		for(final Object cycle: finder.findCycles(graph))
			LOGGER.info(cycle);
		Assert.assertTrue("Cycles detected when there should be none: " + finder.cycleCount(graph), finder.cycleCount(graph) == 0);
	}

	@Test
	public void testDirectedWithCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object tippyTopNode = "tippyTopNode";
		nodes.add(tippyTopNode);
		final Object topNode = "topNode";
		nodes.add(topNode);
		final Object leftNode = "leftNode";
		nodes.add(leftNode);
		final Object rightNode = "RightNode";
		nodes.add(rightNode);
		final Object bottomNode = "bottomNode";
		nodes.add(bottomNode);

		final Set<DirectedEdge<Object>> edges = new HashSet<DirectedEdge<Object>>();
		final DirectedEdge<Object> bottomLeftEdge = new ImmutableDirectedEdge<Object>(bottomNode, leftNode);
		edges.add(bottomLeftEdge);
		final DirectedEdge<Object> letRightEdge = new ImmutableDirectedEdge<Object>(leftNode, rightNode);
		edges.add(letRightEdge);
		final DirectedEdge<Object> rightBottomEdge = new ImmutableDirectedEdge<Object>(rightNode, bottomNode);
		edges.add(rightBottomEdge);
		final DirectedEdge<Object> leftTopEdge = new ImmutableDirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);
		final DirectedEdge<Object> topRightEdge = new ImmutableDirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		final DirectedEdge<Object> leftTippyTopEdge = new ImmutableDirectedEdge<Object>(leftNode, tippyTopNode);
		edges.add(leftTippyTopEdge);
		final DirectedEdge<Object> tippyTopRightEdge = new ImmutableDirectedEdge<Object>(tippyTopNode, rightNode);
		edges.add(tippyTopRightEdge);

		final BidirectedGraph<Object, DirectedEdge<Object>> graph = new ImmutableDirectedAdjacencyGraph<Object, DirectedEdge<Object>>(nodes, edges);

		final CycleFinder<Object, DirectedEdge<Object>> finder = new ExhaustiveDepthFirstSearchCycleFinder<Object, DirectedEdge<Object>>();
		LOGGER.info("testDirectedWithCycles cycles: ");
		for(final Object cycle: finder.findCycles(graph))
			LOGGER.info(cycle);
		Assert.assertTrue("incorrect number of cycles detected. Expected 3, got: " + finder.cycleCount(graph), finder.cycleCount(graph) == 3);
	}

	@Test
	public void testUndirectedNoCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object centerNode = "centerNode";
		nodes.add(centerNode);
		final Object topNode = "topNode";
		nodes.add(topNode);
		final Object leftNode = "leftNode";
		nodes.add(leftNode);
		final Object rightNode = "rightNode";
		nodes.add(rightNode);

		final Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		final BidirectedEdge<Object> centerTopEdge = new ImmutableUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		final BidirectedEdge<Object> centerLeftEdge = new ImmutableUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		final BidirectedEdge<Object> centerRightEdge = new ImmutableUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);

		final Graph<Object, BidirectedEdge<Object>> graph = new ImmutableAdjacencyGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		final CycleFinder<Object, BidirectedEdge<Object>> finder = new ExhaustiveDepthFirstSearchCycleFinder<Object, BidirectedEdge<Object>>();
		LOGGER.info("testUndirectedNoCycles cycles: ");
		for(final Object cycle: finder.findCycles(graph))
			LOGGER.info(cycle);
		Assert.assertTrue("Cycles detected when there should be none: " + finder.cycleCount(graph), finder.cycleCount(graph) == 0);
	}

	@Test
	public void testUndirectedWithCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object bottomNode = "bottomNode";
		nodes.add(bottomNode);
		final Object topNode = "topNode";
		nodes.add(topNode);
		final Object leftNode = "leftNode";
		nodes.add(leftNode);
		final Object rightNode = "rightNode";
		nodes.add(rightNode);

		final Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		final BidirectedEdge<Object> rightBottomEdge = new ImmutableUndirectedEdge<Object>(rightNode, bottomNode);
		edges.add(rightBottomEdge);
		final BidirectedEdge<Object> bottomLeftEdge = new ImmutableUndirectedEdge<Object>(bottomNode, leftNode);
		edges.add(bottomLeftEdge);
		final BidirectedEdge<Object> topRightEdge = new ImmutableUndirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		final BidirectedEdge<Object> rightLeftEdge = new ImmutableUndirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		final BidirectedEdge<Object> leftTopEdge = new ImmutableUndirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);

		final Graph<Object, BidirectedEdge<Object>> graph = new ImmutableAdjacencyGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		final CycleFinder<Object, BidirectedEdge<Object>> finder = new ExhaustiveDepthFirstSearchCycleFinder<Object, BidirectedEdge<Object>>();
		LOGGER.info("testUndirectedWithCycles cycles: ");
		for(final Object cycle: finder.findCycles(graph))
			LOGGER.info(cycle);
		Assert.assertTrue("incorrect number of cycles detected. Expected 3, got: " + finder.cycleCount(graph), finder.cycleCount(graph) == 3);
	}

	@Test
	public void testUndirectedWithDoubleEdgeCycles()
	{
		final Set<Object> nodes = new HashSet<Object>();
		final Object centerNode = "centerNode";
		nodes.add(centerNode);
		final Object topNode = "topNode";
		nodes.add(topNode);
		final Object leftNode = "leftNode";
		nodes.add(leftNode);
		final Object rightNode = "rightNode";
		nodes.add(rightNode);

		final Set<BidirectedEdge<Object>> edges = new HashSet<BidirectedEdge<Object>>();
		final BidirectedEdge<Object> centerTopEdge = new ImmutableUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		final BidirectedEdge<Object> centerLeftEdge = new ImmutableUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		final BidirectedEdge<Object> centerRightEdge = new ImmutableUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		final BidirectedEdge<Object> centerRightEdge2 = new ImmutableUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge2);

		final Graph<Object, BidirectedEdge<Object>> graph = new ImmutableAdjacencyGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		final CycleFinder<Object, BidirectedEdge<Object>> finder = new ExhaustiveDepthFirstSearchCycleFinder<Object, BidirectedEdge<Object>>();
		LOGGER.info("testUndirectedWithDoubleEdgeCycles cycles: ");
		for(final Object cycle: finder.findCycles(graph))
			LOGGER.info(cycle);
		Assert.assertTrue("incorrect number of cycles detected. Expected 1, got: " + finder.cycleCount(graph), finder.cycleCount(graph) == 1);
	}
}
