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
package com.syncleus.dann.neural.som.brain;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import com.syncleus.dann.*;
import com.syncleus.dann.math.Vector;
import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.som.SimpleSomNeuron;
import org.apache.log4j.Logger;

/**
 * A SomBrain acts as the parent class for all brains that use traditional SOM
 * agorithms. It implements a standard SOM leaving only the methods handling the
 * neighborhood and learning rate as abstract. These methods can be implemented
 * in several ways to alow for different types of SOM networks.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public abstract class AbstractSomBrain extends AbstractLocalBrain
{
	private int iterationsTrained;
	private Vector upperBounds;
	private Vector lowerBounds;
	private final List<InputNeuron> inputs;
	private final Map<Vector, SimpleSomNeuron> outputs = new HashMap<Vector, SimpleSomNeuron>();
	private static final Logger LOGGER = Logger.getLogger(AbstractSomBrain.class);

	private static class PropagateOutput implements Callable<Double>
	{
		private final SimpleSomNeuron neuron;
		private static final Logger LOGGER = Logger.getLogger(PropagateOutput.class);

		public PropagateOutput(final SimpleSomNeuron neuron)
		{
			this.neuron = neuron;
		}

		public Double call()
		{
			this.neuron.propagate();
			return this.neuron.getOutput();
		}
	}

	private class TrainNeuron implements Runnable
	{
		private final SimpleSomNeuron neuron;
		private final Vector neuronPoint;
		private final Vector bestMatchPoint;
		private final double neighborhoodRadius;
		private final double learningRate;

		public TrainNeuron(final SimpleSomNeuron neuron, final Vector neuronPoint, final Vector bestMatchPoint, final double neighborhoodRadius, final double learningRate)
		{
			this.neuron = neuron;
			this.neuronPoint = neuronPoint;
			this.bestMatchPoint = bestMatchPoint;
			this.neighborhoodRadius = neighborhoodRadius;
			this.learningRate = learningRate;
		}

		public void run()
		{
			final double currentDistance = this.neuronPoint.calculateRelativeTo(this.bestMatchPoint).getDistance();
			if( currentDistance < this.neighborhoodRadius )
			{
				final double neighborhoodAdjustment = neighborhoodFunction(currentDistance);
				this.neuron.train(this.learningRate, neighborhoodAdjustment);
			}
		}
	}

	/**
	 * Called by chidren classes to instantiate a basic SomBrain with the given
	 * number of inputs and with an output lattice of the given number of
	 * dimensions.
	 *
	 * @param inputCount The number of inputs
	 * @param dimentionality The number of dimensions of the output lattice
	 * @since 2.0
	 */
	protected AbstractSomBrain(final int inputCount, final int dimentionality)
	{
		this(inputCount, dimentionality, null);
	}

	/**
	 * Called by chidren classes to instantiate a basic SomBrain with the given
	 * number of inputs and with an output lattice of the given number of
	 * dimensions.
	 *
	 * @param inputCount The number of inputs
	 * @param dimentionality The number of dimensions of the output lattice
	 * @param executor ThreadPoolExecutor to use when executing parallel
	 * functionality.
	 * @since 2.0
	 */
	protected AbstractSomBrain(final int inputCount, final int dimentionality, final ExecutorService executor)
	{
		super(executor);

		if( inputCount <= 0 )
			throw new IllegalArgumentException("input count must be greater than 0");
		if( dimentionality <= 0 )
			throw new IllegalArgumentException("dimentionality must be greater than 0");

		this.upperBounds = new Vector(dimentionality);
		this.lowerBounds = new Vector(dimentionality);
		final List<InputNeuron> newInputs = new ArrayList<InputNeuron>();
		for(int inputIndex = 0; inputIndex < inputCount; inputIndex++)
		{
			final InputNeuron newNeuron = new SimpleInputNeuron(this);
			newInputs.add(newNeuron);
			super.add(newNeuron);
		}
		this.inputs = Collections.unmodifiableList(newInputs);
	}

	private void updateBounds(final Vector position)
	{
		//make sure we have the proper dimentionality
		if( position.getDimensions() != this.upperBounds.getDimensions() )
			throw new IllegalArgumentException("Dimentionality mismatch");

		for(int dimensionIndex = 1; dimensionIndex <= position.getDimensions(); dimensionIndex++)
		{
			if( this.upperBounds.getCoordinate(dimensionIndex) < position.getCoordinate(dimensionIndex) )
				this.upperBounds = this.upperBounds.setCoordinate(position.getCoordinate(dimensionIndex), dimensionIndex);
			if( this.lowerBounds.getCoordinate(dimensionIndex) > position.getCoordinate(dimensionIndex) )
				this.lowerBounds = this.lowerBounds.setCoordinate(position.getCoordinate(dimensionIndex), dimensionIndex);
		}
	}

	/**
	 * Creates a new point in the output lattice at the given position. This will
	 * automatically have all inputs connected to it.
	 *
	 * @param position The position of the new output in the latice.
	 * @since 2.0
	 */
	public void createOutput(final Vector position)
	{
		//make sure we have the proper dimentionality
		if( position.getDimensions() != this.upperBounds.getDimensions() )
			throw new IllegalArgumentException("Dimentionality mismatch");

		//increase the upper bounds if needed
		this.updateBounds(position);

		//create and add the new output neuron
		final SimpleSomNeuron outputNeuron = new SimpleSomNeuron(this);
		this.outputs.put(position, outputNeuron);
		this.add(outputNeuron);

		//connect all inputs to the new neuron
		for(final InputNeuron input : this.inputs)
			this.connect(input, outputNeuron);
	}

	/**
	 * Gets the positions of all the outputs in the output lattice.
	 *
	 * @return the positions of all the outputs in the output lattice.
	 * @since 2.0
	 */
	public final Set<Vector> getPositions()
	{
		final HashSet<Vector> positions = new HashSet<Vector>();
		for(final Vector position : this.outputs.keySet())
			positions.add(new Vector(position));
		return Collections.unmodifiableSet(positions);
	}

	/**
	 * Gets the current output at the specified position in the output lattice if
	 * the position doesnt have a SimpleSomNeuron associated with it then it throws
	 * an exception.
	 *
	 * @param position position in the output latice of the output you wish to
	 * retreive.
	 * @return The value of the specified SimpleSomNeuron, or null if there is no
	 *         SimpleSomNeuron associated with the given position.
	 * @throws IllegalArgumentException if position doesnt exist.
	 * @since 2.0
	 */
	public final double getOutput(final Vector position)
	{
		final SimpleSomNeuron outputNeuron = this.outputs.get(position);
		if( outputNeuron == null )
			throw new IllegalArgumentException("position does not exist");

		outputNeuron.propagate();
		return outputNeuron.getOutput();
	}

	/**
	 * Obtains the BMU (Best Matching Unit) for the current input set. This will
	 * also train against the current input.
	 *
	 * @return the BMU for the current input set.
	 */
	public final Vector getBestMatchingUnit()
	{
		return this.getBestMatchingUnit(true);
	}

	/**
	 * Obtains the BMU (Best Matching Unit) for the current input set. This will
	 * also train against the current input when specified.
	 *
	 * @param train true to train against the input set, false if no training
	 * occurs.
	 * @return the BMU for the current input set.
	 * @since 2.0
	 */
	public final Vector getBestMatchingUnit(final boolean train)
	{
		//make sure we have atleast one output
		if( this.outputs.size() <= 0 )
			throw new IllegalStateException("Must have atleast one output");

		Vector bestMatchingUnit = null;
		double bestMatch = Double.POSITIVE_INFINITY;
		if( this.getThreadExecutor() != null )
		{
			//stick all the neurons in the queue to propogate
			final Map<Vector, Future<Double>> futureOutput = new HashMap<Vector, Future<Double>>();
			for(final Entry<Vector, SimpleSomNeuron> entry : this.outputs.entrySet())
			{
				final PropagateOutput callable = new PropagateOutput(entry.getValue());
				futureOutput.put(entry.getKey(), this.getThreadExecutor().submit(callable));
			}

			//find the best matching unit
			for(final Entry<Vector, SimpleSomNeuron> entry : this.outputs.entrySet())
			{
				final double output;
				try
				{
					output = futureOutput.get(entry.getKey()).get();
				}
				catch(InterruptedException caught)
				{
					LOGGER.warn("PropagateOutput was unexpectidy interupted", caught);
					throw new UnexpectedInterruptedException("Unexpected interuption. Get should block indefinately", caught);
				}
				catch(ExecutionException caught)
				{
					LOGGER.error("PropagateOutput was had an unexcepted problem executing.", caught);
					throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
				}

				if( bestMatchingUnit == null )
					bestMatchingUnit = entry.getKey();
				else if( output < bestMatch )
				{
					bestMatchingUnit = entry.getKey();
					bestMatch = output;
				}
			}
		}
		else
		{
			for(final Entry<Vector, SimpleSomNeuron> entry : this.outputs.entrySet())
			{
				final SimpleSomNeuron neuron = entry.getValue();
				neuron.propagate();
				final double output = neuron.getOutput();

				if( bestMatchingUnit == null )
					bestMatchingUnit = entry.getKey();
				else if( output < bestMatch )
				{
					bestMatchingUnit = entry.getKey();
					bestMatch = output;
				}
			}
		}

		if( train )
			this.train(bestMatchingUnit);

		return bestMatchingUnit;
	}

	private void train(final Vector bestMatchingUnit)
	{
		final double neighborhoodRadius = this.neighborhoodRadiusFunction();
		final double learningRate = this.learningRateFunction();

		if( this.getThreadExecutor() != null )
		{
			//add all the neuron trainingevents to the thread queue
			final ArrayList<Future> futures = new ArrayList<Future>();
			for(final Entry<Vector, SimpleSomNeuron> entry : this.outputs.entrySet())
			{
				final TrainNeuron runnable = new TrainNeuron(entry.getValue(), entry.getKey(), bestMatchingUnit, neighborhoodRadius, learningRate);
				futures.add(this.getThreadExecutor().submit(runnable));
			}

			//wait until all neurons are trained
			try
			{
				for(final Future future : futures)
					future.get();
			}
			catch(InterruptedException caught)
			{
				LOGGER.warn("PropagateOutput was unexpectidy interupted", caught);
				throw new UnexpectedInterruptedException("Unexpected interuption. Get should block indefinately", caught);
			}
			catch(ExecutionException caught)
			{
				LOGGER.error("PropagateOutput had an unexpected problem executing.", caught);
				throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
			}
		}
		else
		{
			for(final Entry<Vector, SimpleSomNeuron> entry : this.outputs.entrySet())
			{
				final TrainNeuron runnable = new TrainNeuron(entry.getValue(), entry.getKey(), bestMatchingUnit, neighborhoodRadius, learningRate);
				runnable.run();
			}
		}

		this.iterationsTrained++;
	}

	/**
	 * The number of iterations trained so far.
	 *
	 * @return the iterationsTrained so far.
	 * @since 2.0
	 */
	public final int getIterationsTrained()
	{
		return this.iterationsTrained;
	}

	/**
	 * The upper bounds of the positions of the output neurons.
	 *
	 * @return the upperBounds
	 * @since 2.0
	 */
	protected final Vector getUpperBounds()
	{
		return this.upperBounds;
	}

	/**
	 * The lower bounds of the positions of the output neurons.
	 *
	 * @return the lowerBounds
	 * @since 2.0
	 */
	protected final Vector getLowerBounds()
	{
		return this.lowerBounds;
	}

	/**
	 * Gets the number of inputs.
	 *
	 * @return The number of inputs.
	 * @since 2.0
	 */
	public final int getInputCount()
	{
		return this.inputs.size();
	}

	/**
	 * Sets the current input.
	 *
	 * @since 2.0
	 */
	public final void setInput(final int inputIndex, final double inputValue)
	{
		if( inputIndex >= this.getInputCount() )
			throw new IllegalArgumentException("inputIndex is out of bounds");

		final InputNeuron currentInput = this.inputs.get(inputIndex);
		currentInput.setInput(inputValue);
		currentInput.propagate();
	}

	/**
	 * Gets the current input value at the specied index.
	 *
	 * @param index Index of the input to get.
	 * @return The current value for the specified input.
	 * @since 2.0
	 */
	public final double getInput(final int index)
	{
		return this.inputs.get(index).getInput();
	}

	/**
	 * Obtains the weight vectors of the outputs
	 *
	 * @return the weight vectors of each output in the output lattice
	 * @since 2.0
	 */
	public final Map<Vector, double[]> getOutputWeightVectors()
	{
		//iterate through the output lattice
		final HashMap<Vector, double[]> weightVectors = new HashMap<Vector, double[]>();
		for(final Entry<Vector, SimpleSomNeuron> output : this.outputs.entrySet())
		{
			final double[] weightVector = new double[this.inputs.size()];
			final SimpleSomNeuron currentNeuron = output.getValue();
			final Vector currentPoint = output.getKey();
			//iterate through the weight vectors of the current neuron
			for(final Synapse source : this.getInEdges(currentNeuron))
			{
				assert (source.getSourceNode() instanceof InputNeuron);
				final int sourceIndex = this.inputs.indexOf((InputNeuron) source.getSourceNode());
				weightVector[sourceIndex] = source.getWeight();
			}
			//add the current weight vector to the map
			weightVectors.put(currentPoint, weightVector);
		}
		return Collections.unmodifiableMap(weightVectors);
	}

	/**
	 * Determines the neighboorhood function based on the neurons distance from the
	 * Best Matching Unit (BMU).
	 *
	 * @param distanceFromBest The neuron's distance from the BMU.
	 * @return the decay effecting the learning of the specified neuron due to its
	 *         distance from the BMU.
	 * @since 2.0
	 */
	protected abstract double neighborhoodFunction(double distanceFromBest);

	/**
	 * Determine the current radius of the neighborhood which will be centered
	 * around the Best Matching Unit (BMU).
	 *
	 * @return the current radius of the neighborhood.
	 * @since 2.0
	 */
	protected abstract double neighborhoodRadiusFunction();

	/**
	 * Determines the current learning rate for the network.
	 *
	 * @return the current learning rate for the network.
	 * @since 2.0
	 */
	protected abstract double learningRateFunction();
}
