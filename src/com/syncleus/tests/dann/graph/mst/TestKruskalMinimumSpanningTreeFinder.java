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
package com.syncleus.tests.dann.graph.mst;

import com.syncleus.dann.graph.*;
import com.syncleus.dann.graph.cycle.*;
import com.syncleus.dann.graph.mst.*;
import java.util.*;
import org.junit.*;
import org.apache.log4j.Logger;

public class TestKruskalMinimumSpanningTreeFinder
{
	private final static Logger LOGGER = Logger.getLogger(TestKruskalMinimumSpanningTreeFinder.class);

	@Test
	public void testUndirected()
	{
		Set<Object> nodes = new HashSet<Object>();
		Object centerNode = "centerNode";
		nodes.add(centerNode);
		Object topNode = "topNode";
		nodes.add(topNode);
		Object leftNode = "leftNode";
		nodes.add(leftNode);
		Object rightNode = "rightNode";
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

		MinimumSpanningTreeFinder<Object, BidirectedEdge<Object>> finder = new KruskalMinimumSpanningTreeFinder<Object, BidirectedEdge<Object>>();
		Set<BidirectedEdge<Object>> mstEdges = finder.findMinimumSpanningTree(graph);
		TreeGraph<Object, BidirectedEdge<Object>> mst = new ImmutableTreeGraph<Object, BidirectedEdge<Object>>(graph.getNodes(), mstEdges);

		LOGGER.info("mst edges:");
		for(Edge edge : mst.getEdges())
			LOGGER.info(edge);

		CycleDetector detector = new ColoredDepthFirstSearchDetector();
		LOGGER.info("mst is cyclic: " + detector.hasCycle(mst));
		LOGGER.info("mst is connected: " + mst.isStronglyConnected());
		LOGGER.info("mst is contains all nodes: " + mst.getNodes().containsAll(graph.getNodes()));

		Assert.assertTrue("mst was not acyclic", !detector.hasCycle(mst));
		Assert.assertTrue("mst was not connected", mst.isStronglyConnected());
		Assert.assertTrue("mst did not contain all the nodes of the paret graph", mst.getNodes().containsAll(graph.getNodes()));
	}

	@Test
	public void testLinkedUndirected()
	{
		Set<Object> nodes = new LinkedHashSet<Object>();
		Object centerNode = "centerNode";
		nodes.add(centerNode);
		Object leftNode = "leftNode";
		nodes.add(leftNode);
		Object topNode = "topNode";
		nodes.add(topNode);
		Object rightNode = "rightNode";
		nodes.add(rightNode);

		Set<BidirectedEdge<Object>> edges = new LinkedHashSet<BidirectedEdge<Object>>();
		BidirectedEdge<Object> centerRightEdge = new ImmutableUndirectedEdge<Object>(centerNode, rightNode);
		edges.add(centerRightEdge);
		BidirectedEdge<Object> rightLeftEdge = new ImmutableUndirectedEdge<Object>(rightNode, leftNode);
		edges.add(rightLeftEdge);
		BidirectedEdge<Object> topRightEdge = new ImmutableUndirectedEdge<Object>(topNode, rightNode);
		edges.add(topRightEdge);
		BidirectedEdge<Object> centerTopEdge = new ImmutableUndirectedEdge<Object>(centerNode, topNode);
		edges.add(centerTopEdge);
		BidirectedEdge<Object> centerLeftEdge = new ImmutableUndirectedEdge<Object>(centerNode, leftNode);
		edges.add(centerLeftEdge);
		BidirectedEdge<Object> leftTopEdge = new ImmutableUndirectedEdge<Object>(leftNode, topNode);
		edges.add(leftTopEdge);

		Graph<Object, BidirectedEdge<Object>> graph = new LinkedGraph<Object, BidirectedEdge<Object>>(nodes, edges);

		MinimumSpanningTreeFinder<Object, BidirectedEdge<Object>> finder = new KruskalMinimumSpanningTreeFinder<Object, BidirectedEdge<Object>>();
		Set<BidirectedEdge<Object>> mstEdges = finder.findMinimumSpanningTree(graph);
		TreeGraph<Object, BidirectedEdge<Object>> mst = new ImmutableTreeGraph<Object, BidirectedEdge<Object>>(graph.getNodes(), mstEdges);

		LOGGER.info("Linkedgraph objects:");
		for(Object node : nodes)
			LOGGER.info(node);
		LOGGER.info("Linkedgraph edges:");
		for(Edge edge : edges)
			LOGGER.info(edge);

		LOGGER.info("Linked mst edges:");
		for(Edge edge : mst.getEdges())
			LOGGER.info(edge);

		CycleDetector detector = new ColoredDepthFirstSearchDetector();
		LOGGER.info("Linked mst is cyclic: " + detector.hasCycle(mst));
		LOGGER.info("Linked mst is connected: " + mst.isStronglyConnected());
		LOGGER.info("Linked mst is contains all nodes: " + mst.getNodes().containsAll(graph.getNodes()));

		Assert.assertTrue("Linked mst was not acyclic", !detector.hasCycle(mst));
		Assert.assertTrue("Linked mst was not connected", mst.isStronglyConnected());
		Assert.assertTrue("Linked mst did not contain all the nodes of the paret graph", mst.getNodes().containsAll(graph.getNodes()));
	}
}
