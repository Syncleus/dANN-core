package com.syncleus.tests.dann.associativemap;

import com.syncleus.dann.associativemap.AssociativeNode;
import org.junit.*;

public class TestLayeredLoopMap
{
	private static LayeredAssociativeMap testMap = null;

	@BeforeClass public static void alignMap()
	{
		testMap = new LayeredAssociativeMap(32);

		//align the testMap
		for(int alignCount = 0; alignCount<100; alignCount++)
			testMap.align();
	}

	@AfterClass public static void cleanMap()
	{
		testMap = null;
	}

	@Test
	public void testLayeredLoopIndividual()
	{

		AssociativeNode[][] nodes = testMap.getLayers();

		//find the farthest nodes in layer 1 and 2
		double farthestAdjacent = 0.0;
		double closestSeperated = Double.MAX_VALUE;
		for(int primaryLayerIndex = 0; primaryLayerIndex < nodes[0].length; primaryLayerIndex++)
		{
			AssociativeNode currentPrimaryLayerNode = nodes[0][primaryLayerIndex];

			for(int adjacentLayerIndex = 0; adjacentLayerIndex < nodes[1].length; adjacentLayerIndex++)
			{
				AssociativeNode currentAdjacentLayerNode = nodes[1][adjacentLayerIndex];
				double currentDistance = currentPrimaryLayerNode.getLocation().calculateRelativeTo(currentAdjacentLayerNode.getLocation()).getDistance();

				if(farthestAdjacent < currentDistance)
					farthestAdjacent = currentDistance;
			}


			for(int seperatedLayerIndex = 0; seperatedLayerIndex < nodes[16].length; seperatedLayerIndex++)
			{
				AssociativeNode currentSeperatedLayerNode = nodes[16][seperatedLayerIndex];
				double currentDistance = currentPrimaryLayerNode.getLocation().calculateRelativeTo(currentSeperatedLayerNode.getLocation()).getDistance();

				if(closestSeperated > currentDistance)
					closestSeperated = currentDistance;
			}
		}

		Assert.assertTrue("Associative Map did not properly align: farthestAdjacent:" + farthestAdjacent + " closestSeperated:" + closestSeperated, farthestAdjacent < closestSeperated);
	}

	@Test
	public void testLayeredLoopAverage()
	{
		AssociativeNode[][] nodes = testMap.getLayers();

		//find the farthest nodes in layer 1 and 2
		double adjacentTotal = 0.0;
		double adjacentComponents = 0.0;
		double seperatedTotal = 0.0;
		double seperatedComponents = 0.0;
		for(int primaryLayerIndex = 0; primaryLayerIndex < nodes[0].length; primaryLayerIndex++)
		{
			AssociativeNode currentPrimaryLayerNode = nodes[0][primaryLayerIndex];

			for(int adjacentLayerIndex = 0; adjacentLayerIndex < nodes[1].length; adjacentLayerIndex++)
			{
				AssociativeNode currentAdjacentLayerNode = nodes[1][adjacentLayerIndex];
				double currentDistance = currentPrimaryLayerNode.getLocation().calculateRelativeTo(currentAdjacentLayerNode.getLocation()).getDistance();

				adjacentTotal += currentDistance;
				adjacentComponents++;
			}


			for(int seperatedLayerIndex = 0; seperatedLayerIndex < nodes[16].length; seperatedLayerIndex++)
			{
				AssociativeNode currentSeperatedLayerNode = nodes[16][seperatedLayerIndex];
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
