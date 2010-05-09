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
package com.syncleus.tests.dann.graph.search.optimization;

import com.syncleus.tests.dann.graph.search.Grid;
import com.syncleus.tests.dann.graph.search.GridNode;
import com.syncleus.dann.graph.search.optimization.HillClimbingLocalSearch;
import org.junit.Assert;
import org.junit.Test;

public class TestHillClimbingLocalSearch
{
	private static final double[][] EASY_GRID =
			{
					{93.0, 94.0, 95.0, 96.0, 97.0, 98.9, 97.0, 96.0},
					{94.0, 95.0, 96.0, 97.0, 98.0, 99.0, 98.0, 97.0},
					{95.0, 96.0, 97.0, 98.0, 99.0, 100.0, 99.0, 98.0},
					{96.0, 97.0, 98.0, 99.0, 100.0, 101.0, 100.0, 99.9},
					{95.0, 96.0, 97.0, 98.0, 99.0, 100.0, 99.0, 98.0},
					{94.0, 95.0, 96.0, 97.0, 98.0, 99.0, 98.0, 97.0},
					{93.0, 94.0, 95.0, 96.0, 97.0, 98.0, 97.0, 96.0},
					{92.0, 93.0, 94.0, 95.0, 96.0, 97.0, 96.0, 95.0}
			};
	private static final int[] EASY_GRID_START = {1, 0};
	private static final int[] EASY_GRID_SOLUTION = {5, 3};

	@Test
	public void testEasyHill()
	{
		final Grid easyHillGrid = new Grid(EASY_GRID);
		final HillClimbingLocalSearch<Grid, GridNode> searcher = new HillClimbingLocalSearch<Grid, GridNode>(easyHillGrid);
		final GridNode startNode = easyHillGrid.getNode(EASY_GRID_START[0], EASY_GRID_START[1]);
		final GridNode expectedSolutionNode = easyHillGrid.getNode(EASY_GRID_SOLUTION[0], EASY_GRID_SOLUTION[1]);
		final GridNode obtainedSolutionNode = searcher.search(startNode);
		Assert.assertTrue("incorrect solution found!", obtainedSolutionNode.equals(expectedSolutionNode));
	}
}
