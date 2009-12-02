/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann.graph.drawing.hyperassociativemap;

import com.syncleus.dann.graph.drawing.hyperassociativemap.HyperassociativeNode;
import org.junit.*;

public class TestLayeredLoopMap
{
	private static LayeredHyperassociativeMap testMap = null;

	@BeforeClass public static void alignMap()
	{
		testMap = new LayeredHyperassociativeMap(32);

		//align the testMap
		for(int alignCount = 0; alignCount<100; alignCount++)
			testMap.align();
	}

	@AfterClass public static void cleanMap()
	{
		testMap = null;
	}

	@Test
	public void testLayeredLoopAverage()
	{
		HyperassociativeNode[][] nodes = testMap.getLayers();

		//find the farthest nodes in layer 1 and 2
		double adjacentTotal = 0.0;
		double adjacentComponents = 0.0;
		double seperatedTotal = 0.0;
		double seperatedComponents = 0.0;
		for(int primaryLayerIndex = 0; primaryLayerIndex < nodes[0].length; primaryLayerIndex++)
		{
			HyperassociativeNode currentPrimaryLayerNode = nodes[0][primaryLayerIndex];

			for(int adjacentLayerIndex = 0; adjacentLayerIndex < nodes[1].length; adjacentLayerIndex++)
			{
				HyperassociativeNode currentAdjacentLayerNode = nodes[1][adjacentLayerIndex];
				double currentDistance = currentPrimaryLayerNode.getLocation().calculateRelativeTo(currentAdjacentLayerNode.getLocation()).getDistance();

				adjacentTotal += currentDistance;
				adjacentComponents++;
			}


			for(int seperatedLayerIndex = 0; seperatedLayerIndex < nodes[16].length; seperatedLayerIndex++)
			{
				HyperassociativeNode currentSeperatedLayerNode = nodes[16][seperatedLayerIndex];
				double currentDistance = currentPrimaryLayerNode.getLocation().calculateRelativeTo(currentSeperatedLayerNode.getLocation()).getDistance();

				seperatedTotal += currentDistance;
				seperatedComponents++;
			}
		}

		double averageSeperated = seperatedTotal / seperatedComponents;
		double averageAdjacent = adjacentTotal / adjacentComponents;
		Assert.assertTrue("Associative Map did not properly align: averageAdjacent:" + averageAdjacent + " averageSeperated:" + averageSeperated, averageAdjacent < averageSeperated);
	}
}
