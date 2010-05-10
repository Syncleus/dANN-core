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

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import com.syncleus.dann.*;
import com.syncleus.dann.graph.*;
import com.syncleus.dann.graph.drawing.GraphDrawer;
import com.syncleus.dann.math.Vector;
import org.apache.log4j.Logger;

public class HyperassociativeMap<G extends Graph<N, ?>, N> implements GraphDrawer<G, N>
{
	private final G graph;
	private final int dimensions;
	private final ExecutorService threadExecutor;
	private static final Logger LOGGER = Logger.getLogger(HyperassociativeMap.class);
	private Map<N, Vector> coordinates = Collections.synchronizedMap(new HashMap<N, Vector>());
	private static final Random RANDOM = new Random();
	private final boolean useWeights;
	private double equilibriumDistance;
	private double learningRate = 0.4;
	private double maxMovement = 0.0;
	private double totalMovement = 0.0;
	private double acceptableDistanceFactor = 0.75;
	private static final double REPULSIVE_WEAKNESS = 2.0;
	private static final double ATTRACTION_STRENGTH = 4.0;

	private class Align implements Callable<Vector>
	{
		private final N node;

		public Align(final N node)
		{
			this.node = node;
		}

		public Vector call()
		{
			return align(this.node);
		}
	}

	public HyperassociativeMap(final G graph, final int dimensions, final double equilibriumDistance, final boolean useWeights, final ExecutorService threadExecutor)
	{
		if( graph == null )
			throw new IllegalArgumentException("Graph can not be null");
		if( dimensions <= 0 )
			throw new IllegalArgumentException("dimensions must be 1 or more");

		this.graph = graph;
		this.dimensions = dimensions;
		this.threadExecutor = threadExecutor;
		this.equilibriumDistance = equilibriumDistance;
		this.useWeights = useWeights;

		//refresh all nodes
		for(final N node : this.graph.getNodes())
			this.coordinates.put(node, randomCoordinates(this.dimensions));
	}

	public HyperassociativeMap(final G graph, final int dimensions, final ExecutorService threadExecutor)
	{
		this(graph, dimensions, 1.0, true, threadExecutor);
	}

	public HyperassociativeMap(final G graph, final int dimensions, final double equilibriumDistance, final boolean useWeights)
	{
		this(graph, dimensions, equilibriumDistance, useWeights, null);
	}

	public HyperassociativeMap(final G graph, final int dimensions)
	{
		this(graph, dimensions, 1.0, true, null);
	}

	public G getGraph()
	{
		return this.graph;
	}

	public double getEquilibriumDistance()
	{
		return this.equilibriumDistance;
	}

	public void setEquilibriumDistance(final double newEquilbirumDistance)
	{
		this.equilibriumDistance = newEquilbirumDistance;
	}

	public void resetLearning()
	{
		this.learningRate = 0.4;
		this.maxMovement = 0.0;
		this.totalMovement = 0.0;
		this.acceptableDistanceFactor = 0.75;
	}

	public void reset()
	{
		this.resetLearning();
		//randomize all nodes
		for(final N node : this.coordinates.keySet())
			this.coordinates.put(node, randomCoordinates(this.dimensions));
	}

	public boolean isAlignable()
	{
		return true;
	}

	public boolean isAligned()
	{
		if( this.isAlignable() )
			return ((this.maxMovement < 0.005 * this.equilibriumDistance) && (this.maxMovement > 0.0));
		else
			return false;
	}

	private double getAverageMovement()
	{
		return this.totalMovement / ((double) this.graph.getOrder());
	}

	public void align()
	{
		//refresh all nodes
		if( !this.coordinates.keySet().equals(this.graph.getNodes()) )
		{
			final Map<N, Vector> newCoordinates = new HashMap<N, Vector>();
			for(final N node : this.graph.getNodes())
				if( this.coordinates.containsKey(node) )
					newCoordinates.put(node, this.coordinates.get(node));
				else
					newCoordinates.put(node, randomCoordinates(this.dimensions));
			this.coordinates = Collections.synchronizedMap(newCoordinates);
		}

		this.totalMovement = 0.0;
		this.maxMovement = 0.0;
		Vector center;
		if( this.threadExecutor != null )
		{
			//align all nodes in parallel
			final List<Future<Vector>> futures = this.submitFutureAligns();

			//wait for all nodes to finish aligning and calculate new sum of all the points
			try
			{
				center = this.waitAndProcessFutures(futures);
			}
			catch(InterruptedException caught)
			{
				LOGGER.warn("waitAndProcessFutures was unexpectidy interupted", caught);
				throw new UnexpectedInterruptedException("Unexpected interuption. Get should block indefinately", caught);
			}
		}
		else
			center = this.processLocally();

		LOGGER.debug("maxMove: " + this.maxMovement + ", Average Move: " + this.getAverageMovement());

		//divide each coordinate of the sum of all the points by the number of
		//nodes in order to calulate the average point, or center of all the
		//points
		for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
			center = center.setCoordinate(center.getCoordinate(dimensionIndex) / ((double) this.graph.getNodes().size()), dimensionIndex);

		this.recenterNodes(center);
	}

	public int getDimensions()
	{
		return this.dimensions;
	}

	public Map<N, Vector> getCoordinates()
	{
		return Collections.unmodifiableMap(this.coordinates);
	}

	private void recenterNodes(final Vector center)
	{
		for(final N node : this.graph.getNodes())
			this.coordinates.put(node, this.coordinates.get(node).calculateRelativeTo(center));
	}

	public boolean isUsingWeights()
	{
		return this.useWeights;
	}

	Map<N, Double> getNeighbors(final N nodeToQuery)
	{
		final Map<N, Double> neighbors = new HashMap<N, Double>();
		for(final Edge<N> neighborEdge : this.graph.getAdjacentEdges(nodeToQuery))
		{
			final Double currentWeight = ((neighborEdge instanceof Weighted) && this.useWeights ? ((Weighted) neighborEdge).getWeight() : this.equilibriumDistance);
			for(final N neighbor : neighborEdge.getNodes())
				if( !neighbor.equals(nodeToQuery) )
					neighbors.put(neighbor, currentWeight);
		}
		return neighbors;
	}

	private Vector align(final N nodeToAlign)
	{
		//calculate equilibrium with neighbors
		final Vector location = this.coordinates.get(nodeToAlign);
		final Map<N, Double> neighbors = this.getNeighbors(nodeToAlign);

		Vector compositeVector = new Vector(location.getDimensions());
		for(final Entry<N, Double> neighborEntry : neighbors.entrySet())
		{
			final N neighbor = neighborEntry.getKey();
			final double associationEquilibriumDistance = neighborEntry.getValue();

			Vector neighborVector = this.coordinates.get(neighbor).calculateRelativeTo(location);
			if( Math.abs(neighborVector.getDistance()) > associationEquilibriumDistance )
			{
				double newDistance = Math.pow(Math.abs(neighborVector.getDistance()) - associationEquilibriumDistance, ATTRACTION_STRENGTH);
				if( Math.abs(newDistance) > Math.abs(Math.abs(neighborVector.getDistance()) - associationEquilibriumDistance) )
					newDistance = Math.copySign(Math.abs(Math.abs(neighborVector.getDistance()) - associationEquilibriumDistance), newDistance);
				newDistance *= this.learningRate;
				neighborVector = neighborVector.setDistance(Math.signum(neighborVector.getDistance()) * newDistance);
			}
			else
			{
				double newDistance = -1.0 * atanh((associationEquilibriumDistance - Math.abs(neighborVector.getDistance())) / associationEquilibriumDistance);
				if( Math.abs(newDistance) > Math.abs(associationEquilibriumDistance - Math.abs(neighborVector.getDistance())) )
					newDistance = -1.0 * (associationEquilibriumDistance - Math.abs(neighborVector.getDistance()));
				newDistance *= this.learningRate;
				neighborVector = neighborVector.setDistance(Math.signum(neighborVector.getDistance()) * newDistance);
			}
			compositeVector = compositeVector.add(neighborVector);
		}
		//calculate repulsion with all non-neighbors
		for(final N node : this.graph.getNodes())
			if( (!neighbors.containsKey(node)) && (node != nodeToAlign) && (!this.graph.getAdjacentNodes(node).contains(nodeToAlign)) )
			{
				Vector nodeVector = this.coordinates.get(node).calculateRelativeTo(location);
				double newDistance = -1.0 / Math.pow(nodeVector.getDistance(), REPULSIVE_WEAKNESS);
				if( Math.abs(newDistance) > Math.abs(this.equilibriumDistance) )
					newDistance = Math.copySign(this.equilibriumDistance, newDistance);
				newDistance *= this.learningRate;
				nodeVector = nodeVector.setDistance(newDistance);
				compositeVector = compositeVector.add(nodeVector);
			}
		Vector newLocation = location.add(compositeVector);
		final Vector oldLocation = this.coordinates.get(nodeToAlign);
		double moveDistance = Math.abs(newLocation.calculateRelativeTo(oldLocation).getDistance());
		if( moveDistance > this.equilibriumDistance * this.acceptableDistanceFactor )
		{
			final double newLearningRate = ((this.equilibriumDistance * this.acceptableDistanceFactor) / moveDistance);
			if( newLearningRate < this.learningRate )
			{
				this.learningRate = newLearningRate;
				LOGGER.debug("learning rate: " + this.learningRate);
			}
			else
			{
				this.learningRate *= 0.9;
				LOGGER.debug("learning rate: " + this.learningRate);
			}

			newLocation = oldLocation;
			moveDistance = 0.0;
		}

		if( moveDistance > this.maxMovement )
			this.maxMovement = moveDistance;
		this.totalMovement += moveDistance;

		this.coordinates.put(nodeToAlign, newLocation);
		return newLocation;
	}

	/**
	 * Obtains a Vector with RANDOM coordinates for the specified number of
	 * dimensions.
	 *
	 * @param dimensions Number of dimensions for the RANDOM Vector
	 * @return New RANDOM Vector
	 * @since 1.0
	 */
	public static Vector randomCoordinates(final int dimensions)
	{
		final double[] randomCoords = new double[dimensions];
		for(int randomCoordsIndex = 0; randomCoordsIndex < dimensions; randomCoordsIndex++)
			randomCoords[randomCoordsIndex] = (RANDOM.nextDouble() * 2.0) - 1.0;

		return new Vector(randomCoords);
	}

	private static double atanh(final double value)
	{
		return 0.5 * Math.log(Math.abs((value + 1.0) / (1.0 - value)));
	}

	private List<Future<Vector>> submitFutureAligns()
	{
		final ArrayList<Future<Vector>> futures = new ArrayList<Future<Vector>>();
		for(final N node : this.graph.getNodes())
			futures.add(this.threadExecutor.submit(new Align(node)));
		return futures;
	}

	private Vector processLocally()
	{
		Vector pointSum = new Vector(this.dimensions);
		for(final N node : this.graph.getNodes())
		{
			final Vector newPoint = this.align(node);
			for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
				pointSum = pointSum.setCoordinate(pointSum.getCoordinate(dimensionIndex) + newPoint.getCoordinate(dimensionIndex), dimensionIndex);
		}
		if( this.learningRate * 1.01 < 0.4 )
		{
			if( this.getAverageMovement() < (this.equilibriumDistance * this.acceptableDistanceFactor * 0.1) )
				this.acceptableDistanceFactor *= 0.9;
			this.learningRate *= 1.01;
			LOGGER.debug("learning rate: " + this.learningRate + ", acceptableDistanceFactor: " + this.acceptableDistanceFactor);
		}
		return pointSum;
	}

	private Vector waitAndProcessFutures(final List<Future<Vector>> futures) throws InterruptedException
	{
		//wait for all nodes to finish aligning and calculate new center point
		Vector pointSum = new Vector(this.dimensions);
		try
		{
			for(final Future<Vector> future : futures)
			{
				final Vector newPoint = future.get();
				for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
					pointSum = pointSum.setCoordinate(pointSum.getCoordinate(dimensionIndex) + newPoint.getCoordinate(dimensionIndex), dimensionIndex);
			}
		}
		catch(ExecutionException caught)
		{
			LOGGER.error("Align had an unexcepted problem executing.", caught);
			throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
		}
		if( this.learningRate * 1.01 < 0.4 )
		{
			if( this.getAverageMovement() < (this.equilibriumDistance * this.acceptableDistanceFactor * 0.1) )
				this.acceptableDistanceFactor = this.maxMovement * 2.0;
			this.learningRate *= 1.01;
			LOGGER.debug("learning rate: " + this.learningRate + ", acceptableDistanceFactor: " + this.acceptableDistanceFactor);
		}
		return pointSum;
	}
}
