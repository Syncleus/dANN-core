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
package com.syncleus.dann.graph.tree.mst;

import java.util.*;
import com.syncleus.dann.graph.*;
import com.syncleus.dann.graph.cycle.*;
import com.syncleus.dann.graph.topological.Topography;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class TestKruskalMinimumSpanningTreeFinder
{
	private static final Logger LOGGER = Logger.getLogger(TestKruskalMinimumSpanningTreeFinder.class);

	@Test
	public void testUndirected()
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

		final MinimumSpanningTreeFinder<Object, BidirectedEdge<Object>> finder = new KruskalMinimumSpanningTreeFinder<Object, BidirectedEdge<Object>>();
		final Set<BidirectedEdge<Object>> mstEdges = finder.findMinimumSpanningTree(graph);
		final TreeGraph<Object, BidirectedEdge<Object>> mst = new ImmutableTreeAdjacencyGraph<Object, BidirectedEdge<Object>>(graph.getTargets(), mstEdges);

		LOGGER.info("mst edges:");
		for(final Cloud cloud : mst.getEdges())
			LOGGER.info(cloud);

		final CycleDetector detector = new ColoredDepthFirstSearchDetector();
		LOGGER.info("mst is cyclic: " + detector.hasCycle(mst));
		LOGGER.info("mst is connected: " + Topography.isStronglyConnected(mst));
		LOGGER.info("mst is contains all nodes: " + mst.getTargets().containsAll(graph.getTargets()));

		Assert.assertTrue("mst was not acyclic", !detector.hasCycle(mst));
		Assert.assertTrue("mst was not connected", Topography.isStronglyConnected(mst));
		Assert.assertTrue("mst did not contain all the nodes of the paret graph", mst.getTargets().containsAll(graph.getTargets()));
	}

	@Test
	public void testLinkedUndirected()
	{
		final Set<Object> nodes = new LinkedHashSet<Object>();
		final Object centerNode = "centerNode";
		nodes.add(centerNode);
		final Object leftNode = "leftNode";
		nodes.add(leftNode);
		final Object topNode = "topNode";
		nodes.add(topNode);
		final Object rightNode = "rightNode";
		nodes.add(rightNode);

		final Set<BidirectedEdge<Object>> edges = new LinkedHashSet<BidirectedEdge<Object>>();
		final BidirectedEdge<Object> centerRightEdge = new SimpleUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		final BidirectedEdge<Object> rightLeftEdge = new SimpleUndirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		final BidirectedEdge<Object> topRightEdge = new SimpleUndirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		final BidirectedEdge<Object> centerTopEdge = new SimpleUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		final BidirectedEdge<Object> centerLeftEdge = new SimpleUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		final BidirectedEdge<Object> leftTopEdge = new SimpleUndirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);

		final CloudGraph<Object, BidirectedEdge<Object>> graph = new LinkedGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		final MinimumSpanningTreeFinder<Object, BidirectedEdge<Object>> finder = new KruskalMinimumSpanningTreeFinder<Object, BidirectedEdge<Object>>();
		final Set<BidirectedEdge<Object>> mstEdges = finder.findMinimumSpanningTree(graph);
		final TreeGraph<Object, BidirectedEdge<Object>> mst = new ImmutableTreeAdjacencyGraph<Object, BidirectedEdge<Object>>(graph.getTargets(), mstEdges);

		LOGGER.info("Linkedgraph objects:");
		for(final Object node : nodes)
			LOGGER.info(node);
		LOGGER.info("Linkedgraph edges:");
		for(final Cloud cloud : edges)
			LOGGER.info(cloud);

		LOGGER.info("Linked mst edges:");
		for(final Cloud cloud : mst.getEdges())
			LOGGER.info(cloud);

		final CycleDetector detector = new ColoredDepthFirstSearchDetector();
		LOGGER.info("Linked mst is cyclic: " + detector.hasCycle(mst));
		LOGGER.info("Linked mst is connected: " + Topography.isStronglyConnected(mst));
		LOGGER.info("Linked mst is contains all nodes: " + mst.getTargets().containsAll(graph.getTargets()));

		Assert.assertTrue("Linked mst was not acyclic", !detector.hasCycle(mst));
		Assert.assertTrue("Linked mst was not connected", Topography.isStronglyConnected(mst));
		Assert.assertTrue("Linked mst did not contain all the nodes of the paret graph", mst.getTargets().containsAll(graph.getTargets()));
	}
}
