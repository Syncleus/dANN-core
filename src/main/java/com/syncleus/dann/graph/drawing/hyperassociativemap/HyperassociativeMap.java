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

import java.util.Map.Entry;
import java.util.concurrent.*;
import com.syncleus.dann.*;
import com.syncleus.dann.graph.*;
import com.syncleus.dann.graph.drawing.GraphDrawer;
import com.syncleus.dann.graph.topological.Topography;
import com.syncleus.dann.math.Vector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * A Hyperassociative Map is a new type of algorithm that organizes an arbitrary
 * graph of interconnected nodes according to its associations to other nodes.
 * Once a new Hyperassociative Map has been associated and aligned, nodes that
 * are most closely associated will be closest to each other.
 * For more info, please see the
 * <a href ="http://wiki.syncleus.com/index.php/dANN:Hyperassociative_Map">
 * Hyperassociative-Map dANN Wiki page</a>.
 * @author Jeffrey Phillips Freeman
 * @param <G> The graph type
 * @param <N> The node type
 */
public class HyperassociativeMap<G extends Graph<N, ?>, N> implements GraphDrawer<G, N>
{
	private static final double REPULSIVE_WEAKNESS = 2.0;
	private static final double ATTRACTION_STRENGTH = 4.0;
	private static final double DEFAULT_LEARNING_RATE = 0.4;
	private static final double DEFAULT_MAX_MOVEMENT = 0.0;
	private static final double DEFAULT_TOTAL_MOVEMENT = 0.0;
	private static final double DEFAULT_ACCEPTABLE_DISTANCE_FACTOR = 0.75;
	private static final double EQUILIBRIUM_DISTANCE = 1.0;
	private static final double EQUILIBRIUM_ALIGNMENT_FACTOR = 0.005;
	private static final double LEARNING_RATE_INCREASE_FACTOR = 0.9;
	private static final double LEARNING_RATE_PROCESSING_ADJUSTMENT = 1.01;

	private final G graph;
	private final int dimensions;
	private final ExecutorService threadExecutor;
	private static final Logger LOGGER = Logger.getLogger(HyperassociativeMap.class);
	private Map<N, Vector> coordinates = Collections.synchronizedMap(new HashMap<N, Vector>());
	private static final Random RANDOM = new Random();
	private final boolean useWeights;
	private double equilibriumDistance;
	private double learningRate = DEFAULT_LEARNING_RATE;
	private double maxMovement = DEFAULT_MAX_MOVEMENT;
	private double totalMovement = DEFAULT_TOTAL_MOVEMENT;
	private double acceptableDistanceFactor = DEFAULT_ACCEPTABLE_DISTANCE_FACTOR;

	private class Align implements Callable<Vector>
	{
		private final N node;

		public Align(final N node)
		{
			this.node = node;
		}

		@Override
		public Vector call()
		{
			return align(node);
		}
	}

	public HyperassociativeMap(final G graph, final int dimensions, final double equilibriumDistance, final boolean useWeights, final ExecutorService threadExecutor)
	{
		if (graph == null)
			throw new IllegalArgumentException("Graph can not be null");
		if (dimensions <= 0)
			throw new IllegalArgumentException("dimensions must be 1 or more");

		this.graph = graph;
		this.dimensions = dimensions;
		this.threadExecutor = threadExecutor;
		this.equilibriumDistance = equilibriumDistance;
		this.useWeights = useWeights;

		// refresh all nodes
		for (final N node : this.graph.getTargets())
		{
			this.coordinates.put(node, randomCoordinates(this.dimensions));
		}
	}

	public HyperassociativeMap(final G graph, final int dimensions, final ExecutorService threadExecutor)
	{
		this(graph, dimensions, EQUILIBRIUM_DISTANCE, true, threadExecutor);
	}

	public HyperassociativeMap(final G graph, final int dimensions, final double equilibriumDistance, final boolean useWeights)
	{
		this(graph, dimensions, equilibriumDistance, useWeights, null);
	}

	public HyperassociativeMap(final G graph, final int dimensions)
	{
		this(graph, dimensions, EQUILIBRIUM_DISTANCE, true, null);
	}

	@Override
	public G getGraph()
	{
		return graph;
	}

	public double getEquilibriumDistance()
	{
		return equilibriumDistance;
	}

	public void setEquilibriumDistance(final double equilibriumDistance)
	{
		this.equilibriumDistance = equilibriumDistance;
	}

	public void resetLearning()
	{
		learningRate = DEFAULT_LEARNING_RATE;
		maxMovement = DEFAULT_TOTAL_MOVEMENT;
		totalMovement = DEFAULT_TOTAL_MOVEMENT;
		acceptableDistanceFactor = DEFAULT_ACCEPTABLE_DISTANCE_FACTOR;
	}

	@Override
	public void reset()
	{
		resetLearning();
		// randomize all nodes
		for (final N node : coordinates.keySet())
		{
			coordinates.put(node, randomCoordinates(dimensions));
		}
	}

	@Override
	public boolean isAlignable()
	{
		return true;
	}

	@Override
	public boolean isAligned()
	{
		return isAlignable()
				&& (maxMovement < (EQUILIBRIUM_ALIGNMENT_FACTOR * equilibriumDistance))
				&& (maxMovement > DEFAULT_MAX_MOVEMENT);
	}

	private double getAverageMovement()
	{
		return totalMovement / Topography.getOrder((Graph<N, ?>) graph);
	}

	@Override
	public void align()
	{
		// refresh all nodes
		if (!coordinates.keySet().equals(graph.getTargets()))
		{
			final Map<N, Vector> newCoordinates = new HashMap<N, Vector>();
			for (final N node : graph.getTargets())
			{
				if (coordinates.containsKey(node))
				{
					newCoordinates.put(node, coordinates.get(node));
				}
				else
				{
					newCoordinates.put(node, randomCoordinates(dimensions));
				}
			}
			coordinates = Collections.synchronizedMap(newCoordinates);
		}

		totalMovement = DEFAULT_TOTAL_MOVEMENT;
		maxMovement = DEFAULT_MAX_MOVEMENT;
		Vector center;
		if (threadExecutor == null)
		{
			center = processLocally();
		}
		else
		{
			// align all nodes in parallel
			final List<Future<Vector>> futures = submitFutureAligns();

			// wait for all nodes to finish aligning and calculate new sum of
			// all the points
			try
			{
				center = waitAndProcessFutures(futures);
			}
			catch (InterruptedException caught)
			{
				LOGGER.warn("waitAndProcessFutures was unexpectedly interrupted", caught);
				throw new UnexpectedInterruptedException("Unexpected interruption. Get should block indefinitely", caught);
			}
		}

		LOGGER.debug("maxMove: " + maxMovement + ", Average Move: " + getAverageMovement());

		// divide each coordinate of the sum of all the points by the number of
		// nodes in order to calculate the average point, or center of all the
		// points
		for (int dimensionIndex = 1; dimensionIndex <= dimensions; dimensionIndex++)
		{
			center = center.setCoordinate(center.getCoordinate(dimensionIndex) / graph.getTargets().size(), dimensionIndex);
		}

		recenterNodes(center);
	}

	@Override
	public int getDimensions()
	{
		return dimensions;
	}

	@Override
	public Map<N, Vector> getCoordinates()
	{
		return Collections.unmodifiableMap(coordinates);
	}

	private void recenterNodes(final Vector center)
	{
		for (final N node : graph.getTargets())
		{
			coordinates.put(node, coordinates.get(node).calculateRelativeTo(center));
		}
	}

	public boolean isUsingWeights()
	{
		return useWeights;
	}

	Map<N, Double> getNeighbors(final N nodeToQuery)
	{
		final Map<N, Double> neighbors = new HashMap<N, Double>();
		for (final Edge<N> neighborEdge : graph.getAdjacentEdges(nodeToQuery))
		{
			final Double currentWeight = (((neighborEdge instanceof Weighted) && useWeights) ? ((Weighted) neighborEdge).getWeight() : equilibriumDistance);
			for (final N neighbor : neighborEdge.getTargets())
			{
				if (!neighbor.equals(nodeToQuery))
				{
					neighbors.put(neighbor, currentWeight);
				}
			}
		}
		return neighbors;
	}

	private Vector align(final N nodeToAlign)
	{
		// calculate equilibrium with neighbors
		final Vector location = coordinates.get(nodeToAlign);
		final Map<N, Double> neighbors = getNeighbors(nodeToAlign);

		Vector compositeVector = new Vector(location.getDimensions());
		for (final Entry<N, Double> neighborEntry : neighbors.entrySet())
		{
			final N neighbor = neighborEntry.getKey();
			final double associationEquilibriumDistance = neighborEntry.getValue();

			Vector neighborVector = coordinates.get(neighbor).calculateRelativeTo(location);
			if (Math.abs(neighborVector.getDistance()) > associationEquilibriumDistance)
			{
				double newDistance = Math.pow(Math.abs(neighborVector.getDistance()) - associationEquilibriumDistance, ATTRACTION_STRENGTH);
				if (Math.abs(newDistance) > Math.abs(Math.abs(neighborVector.getDistance()) - associationEquilibriumDistance))
				{
					newDistance = Math.copySign(Math.abs(Math.abs(neighborVector.getDistance()) - associationEquilibriumDistance), newDistance);
				}
				newDistance *= learningRate;
				neighborVector = neighborVector.setDistance(Math.signum(neighborVector.getDistance()) * newDistance);
			}
			else
			{
				double newDistance = -EQUILIBRIUM_DISTANCE * atanh((associationEquilibriumDistance - Math.abs(neighborVector.getDistance())) / associationEquilibriumDistance);
				if (Math.abs(newDistance) > (Math.abs(associationEquilibriumDistance - Math.abs(neighborVector.getDistance()))))
				{
					newDistance = -EQUILIBRIUM_DISTANCE * (associationEquilibriumDistance - Math.abs(neighborVector.getDistance()));
				}
				newDistance *= learningRate;
				neighborVector = neighborVector.setDistance(Math.signum(neighborVector.getDistance()) * newDistance);
			}
			compositeVector = compositeVector.add(neighborVector);
		}
		// calculate repulsion with all non-neighbors
		for (final N node : graph.getTargets())
		{
			if ((!neighbors.containsKey(node)) && (node != nodeToAlign)
					&& (!graph.getAdjacentNodes(node).contains(nodeToAlign)))
			{
				Vector nodeVector = coordinates.get(node).calculateRelativeTo(location);
				double newDistance = -EQUILIBRIUM_DISTANCE / Math.pow(nodeVector.getDistance(), REPULSIVE_WEAKNESS);
				if (Math.abs(newDistance) > Math.abs(equilibriumDistance))
				{
					newDistance = Math.copySign(equilibriumDistance, newDistance);
				}
				newDistance *= learningRate;
				nodeVector = nodeVector.setDistance(newDistance);
				compositeVector = compositeVector.add(nodeVector);
			}
		}
		Vector newLocation = location.add(compositeVector);
		final Vector oldLocation = coordinates.get(nodeToAlign);
		double moveDistance = Math.abs(newLocation.calculateRelativeTo(oldLocation).getDistance());
		if (moveDistance > equilibriumDistance * acceptableDistanceFactor)
		{
			final double newLearningRate = ((equilibriumDistance * acceptableDistanceFactor) / moveDistance);
			if (newLearningRate < learningRate)
			{
				learningRate = newLearningRate;
				LOGGER.debug("learning rate: " + learningRate);
			}
			else
			{
				learningRate *= LEARNING_RATE_INCREASE_FACTOR;
				LOGGER.debug("learning rate: " + learningRate);
			}

			newLocation = oldLocation;
			moveDistance = DEFAULT_TOTAL_MOVEMENT;
		}

		if (moveDistance > maxMovement)
		{
			maxMovement = moveDistance;
		}
		totalMovement += moveDistance;

		coordinates.put(nodeToAlign, newLocation);
		return newLocation;
	}

	/**
	 * Obtains a Vector with RANDOM coordinates for the specified number of
	 * dimensions.
	 * The coordinates will be in range [-1.0, 1.0].
	 *
	 * @param dimensions Number of dimensions for the RANDOM Vector
	 * @return New RANDOM Vector
	 * @since 1.0
	 */
	public static Vector randomCoordinates(final int dimensions)
	{
		final double[] randomCoordinates = new double[dimensions];
		for (int randomCoordinatesIndex = 0; randomCoordinatesIndex < dimensions; randomCoordinatesIndex++)
		{
			randomCoordinates[randomCoordinatesIndex] = (RANDOM.nextDouble() * 2.0) - 1.0;
		}

		return new Vector(randomCoordinates);
	}

	/**
	 * Returns the inverse hyperbolic tangent of a value.
	 * You may see
	 * <a href="http://www.mathworks.com/help/techdoc/ref/atanh.html">
	 * MathWorks atanh page</a> for more info.
	 * @param value the input.
	 * @return the inverse hyperbolic tangent of value.
	 */
	private static double atanh(final double value)
	{
		return Math.log(Math.abs((value + 1.0) / (1.0 - value))) / 2;
	}

	private List<Future<Vector>> submitFutureAligns()
	{
		final ArrayList<Future<Vector>> futures = new ArrayList<Future<Vector>>();
		for (final N node : graph.getTargets())
		{
			futures.add(threadExecutor.submit(new Align(node)));
		}
		return futures;
	}

	private Vector processLocally()
	{
		Vector pointSum = new Vector(dimensions);
		for (final N node : graph.getTargets())
		{
			final Vector newPoint = align(node);
			for (int dimensionIndex = 1; dimensionIndex <= dimensions; dimensionIndex++)
			{
				pointSum = pointSum.setCoordinate(pointSum.getCoordinate(dimensionIndex) + newPoint.getCoordinate(dimensionIndex), dimensionIndex);
			}
		}
		if ((learningRate * LEARNING_RATE_PROCESSING_ADJUSTMENT) < DEFAULT_LEARNING_RATE)
		{
			final double acceptableDistanceAdjustment = 0.1;
			if (getAverageMovement() < (equilibriumDistance * acceptableDistanceFactor * acceptableDistanceAdjustment))
			{
				acceptableDistanceFactor *= LEARNING_RATE_INCREASE_FACTOR;
			}
			learningRate *= LEARNING_RATE_PROCESSING_ADJUSTMENT;
			LOGGER.debug("learning rate: " + learningRate + ", acceptableDistanceFactor: " + acceptableDistanceFactor);
		}
		return pointSum;
	}

	private Vector waitAndProcessFutures(final List<Future<Vector>> futures) throws InterruptedException
	{
		// wait for all nodes to finish aligning and calculate the new center point
		Vector pointSum = new Vector(dimensions);
		try
		{
			for (final Future<Vector> future : futures)
			{
				final Vector newPoint = future.get();
				for (int dimensionIndex = 1; dimensionIndex <= dimensions; dimensionIndex++)
				{
					pointSum = pointSum.setCoordinate(pointSum.getCoordinate(dimensionIndex) + newPoint.getCoordinate(dimensionIndex), dimensionIndex);
				}
			}
		}
		catch (ExecutionException caught)
		{
			LOGGER.error("Align had an unexpected problem executing.", caught);
			throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinitely", caught);
		}
		if (learningRate * LEARNING_RATE_PROCESSING_ADJUSTMENT < DEFAULT_LEARNING_RATE)
		{
			final double acceptableDistanceAdjustment = 0.1;
			if (getAverageMovement() < (equilibriumDistance * acceptableDistanceFactor * acceptableDistanceAdjustment))
			{
				acceptableDistanceFactor = maxMovement * 2.0;
			}
			learningRate *= LEARNING_RATE_PROCESSING_ADJUSTMENT;
			LOGGER.debug("learning rate: " + learningRate + ", acceptableDistanceFactor: " + acceptableDistanceFactor);
		}
		return pointSum;
	}
}
