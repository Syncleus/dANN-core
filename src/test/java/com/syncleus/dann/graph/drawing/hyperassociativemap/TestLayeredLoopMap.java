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
package com.syncleus.dann.graph.drawing.hyperassociativemap;

import java.util.concurrent.*;
import org.junit.*;

public class TestLayeredLoopMap
{
	@Test
	public void testLayeredLoopAverage()
	{
		final int cores = Runtime.getRuntime().availableProcessors();
		final ThreadPoolExecutor executor = new ThreadPoolExecutor(cores + 1, cores * 2, 20, TimeUnit.SECONDS, new LinkedBlockingQueue());

		try
		{
			final LayeredHyperassociativeMap testMap = new LayeredHyperassociativeMap(10, executor);

			//align the testMap
			for(int alignCount = 0; alignCount < 10; alignCount++)
				testMap.align();

			final SimpleNode[][] nodes = testMap.getGraph().getNodeInLayers();

			//find the farthest nodes in layer 1 and 2
			double adjacentTotal = 0.0;
			double adjacentComponents = 0.0;
			double seperatedTotal = 0.0;
			double seperatedComponents = 0.0;
			for(int primaryLayerIndex = 0; primaryLayerIndex < nodes[0].length; primaryLayerIndex++)
			{
				final SimpleNode currentPrimaryLayerNode = nodes[0][primaryLayerIndex];

				for(int adjacentLayerIndex = 0; adjacentLayerIndex < nodes[1].length; adjacentLayerIndex++)
				{
					final SimpleNode currentAdjacentLayerNode = nodes[1][adjacentLayerIndex];
					final double currentDistance = testMap.getCoordinates().get(currentPrimaryLayerNode).calculateRelativeTo(testMap.getCoordinates().get(currentAdjacentLayerNode)).getDistance();

					adjacentTotal += currentDistance;
					adjacentComponents++;
				}


				for(int seperatedLayerIndex = 0; seperatedLayerIndex < nodes[nodes.length - 1].length; seperatedLayerIndex++)
				{
					final SimpleNode currentSeperatedLayerNode = nodes[nodes.length - 1][seperatedLayerIndex];
					final double currentDistance = testMap.getCoordinates().get(currentPrimaryLayerNode).calculateRelativeTo(testMap.getCoordinates().get(currentSeperatedLayerNode)).getDistance();

					seperatedTotal += currentDistance;
					seperatedComponents++;
				}
			}

			final double averageSeperated = seperatedTotal / seperatedComponents;
			final double averageAdjacent = adjacentTotal / adjacentComponents;
			Assert.assertTrue("Associative Map did not properly align: averageAdjacent:" + averageAdjacent + " averageSeperated:" + averageSeperated, averageAdjacent < averageSeperated);
		}
		finally
		{
			executor.shutdown();
		}
	}
}
