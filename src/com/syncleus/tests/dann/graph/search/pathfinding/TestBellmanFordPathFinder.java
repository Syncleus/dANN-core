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
package com.syncleus.tests.dann.graph.search.pathfinding;

import com.syncleus.dann.graph.BidirectedEdge;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.WeightedBidirectedWalk;
import com.syncleus.dann.graph.search.pathfinding.BellmanFordPathFinder;
import org.junit.Assert;
import org.junit.Test;

public class TestBellmanFordPathFinder
{
	private static final double INF = Double.POSITIVE_INFINITY;
	private static final double[][] HARD_GRID =
	{
		{1.0,	1.0,	1000.0,	1.0,	1.0,	1.0,	1.0,	1.0},
		{1.0,	1.0,	1.0,	1.0,	10.0,	10.0,	1.0,	1.0},
		{INF,	INF,	INF,	INF,	INF,	1.0,	1.0,	1.0},
		{11.0,	1.0,	1.0,	1.0,	1.0,	10.0,	2.0,	1.0},
		{1.0,	10.0,	INF,	1.0,	INF,	INF,	INF,	INF},
		{1.0,	INF,	INF,	INF,	INF,	1.0,	1.0,	1.0},
		{1.0,	INF,	1.0,	1.0,	1.0,	1.0,	INF,	1.0},
		{1.0,	1.0,	1.0,	INF,	INF,	10.0,	1.0,	1.0}
	};
	private static final int[] HARD_GRID_START = {1,0};
	private static final int[] HARD_GRID_END = {7,7};
	private static final int[][] HARD_GRID_SOLUTION =
	{
		{1,0},{1,1},{2,1},{3,1},{3,0},{4,0},{5,0},{6,0},{6,1},{6,2},{5,2},
		{5,3},{4,3},{3,3},{2,3},{1,3},{1,4},{0,4},{0,5},{0,6},{0,7},{1,7},
		{2,7},{2,6},{3,6},{4,6},{5,6},{5,5},{6,5},{7,5},{7,6},{7,7}
	};

	private static final double[][] EASY_GRID =
	{
		{INF,	INF,	INF,	1.0,	1.0,	1.0,	1.0,	INF},
		{INF,	1.0,	1.0,	1.0,	INF,	INF,	1.0,	INF},
		{INF,	INF,	INF,	INF,	INF,	1.0,	1.0,	INF},
		{INF,	1.0,	1.0,	1.0,	1.0,	10.0,	INF,	INF},
		{1.0,	10.0,	INF,	INF,	INF,	INF,	INF,	INF},
		{1.0,	INF,	INF,	INF,	INF,	1.0,	1.0,	1.0},
		{1.0,	INF,	1.0,	1.0,	1.0,	1.0,	INF,	1.0},
		{1.0,	1.0,	1.0,	INF,	INF,	INF,	INF,	1.0}
	};
	private static final int[] EASY_GRID_START = {1,0};
	private static final int[] EASY_GRID_END = {7,7};
	private static final int[][] EASY_GRID_SOLUTION =
	{
		{1,0},{1,1},{2,1},{3,1},{3,0},{4,0},{5,0},{6,0},{6,1},{6,2},{5,2},
		{5,3},{4,3},{3,3},{2,3},{1,3},{1,4},{0,4},{0,5},{0,6},{0,7},{1,7},
		{2,7},{2,6},{3,6},{4,6},{5,6},{5,5},{6,5},{7,5},{7,6},{7,7}
	};

	private static boolean checkNode(GridNode node, int[] coords)
	{
		return ( (node.getX() == coords[0])&&(node.getY() == coords[1]) );
	}

	private static boolean checkSolution(WeightedBidirectedWalk<GridNode, DirectedEdge<GridNode>> path, int[][] solution)
	{
		int solutionIndex = 0;
		GridNode lastNode = path.getFirstNode();
		if(!checkNode(lastNode, solution[solutionIndex]))
			return false;

		for(BidirectedEdge<GridNode> edge : path.getSteps())
		{
			solutionIndex++;

			GridNode currentNode = (edge.getLeftNode().equals(lastNode) ? edge.getRightNode() : edge.getLeftNode());
			if(!checkNode(currentNode, solution[solutionIndex]))
				return false;
			lastNode = currentNode;
		}

		return true;
	}

	@Test
	public void testHardGrid()
	{
		DirectedGrid hardGrid = new DirectedGrid(HARD_GRID);
		BellmanFordPathFinder<DirectedGrid, GridNode, DirectedEdge<GridNode>> pathFinder = new BellmanFordPathFinder<DirectedGrid, GridNode, DirectedEdge<GridNode>>(hardGrid);

		GridNode startNode = hardGrid.getNode(HARD_GRID_START[0], HARD_GRID_START[1]);
		GridNode endNode = hardGrid.getNode(HARD_GRID_END[0], HARD_GRID_END[1]);

		WeightedBidirectedWalk<GridNode, DirectedEdge<GridNode>> path = pathFinder.getBestPath(startNode, endNode);

		Assert.assertTrue("incorrect path found!", checkSolution(path, HARD_GRID_SOLUTION));
	}

	@Test
	public void testInfinityGrid()
	{
		DirectedGrid infinityGrid = new DirectedGrid(EASY_GRID);
		BellmanFordPathFinder<DirectedGrid, GridNode, DirectedEdge<GridNode>> pathFinder = new BellmanFordPathFinder<DirectedGrid, GridNode, DirectedEdge<GridNode>>(infinityGrid);

		GridNode startNode = infinityGrid.getNode(EASY_GRID_START[0], EASY_GRID_START[1]);
		GridNode endNode = infinityGrid.getNode(EASY_GRID_END[0], EASY_GRID_END[1]);

		WeightedBidirectedWalk<GridNode, DirectedEdge<GridNode>> path = pathFinder.getBestPath(startNode, endNode);

		Assert.assertTrue("incorrect path found!", checkSolution(path, EASY_GRID_SOLUTION));
	}
}
