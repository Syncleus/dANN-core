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

import com.syncleus.dann.neural.som.*;
import com.syncleus.dann.neural.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.Map.Entry;
import com.syncleus.dann.math.Vector;
import org.apache.log4j.Logger;
import com.syncleus.dann.InterruptedDannRuntimeException;
import com.syncleus.dann.DannRuntimeException;
import com.syncleus.dann.UnexpectedDannError;


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
	private final List<SomInputNeuron> inputs = new ArrayList<SomInputNeuron>();
	private final Hashtable<Vector, SomNeuron> outputs = new Hashtable<Vector, SomNeuron>();
	private final static Logger LOGGER = Logger.getLogger(AbstractSomBrain.class);


	private static class PropagateOutput implements Callable<Double>
	{
		private final SomNeuron neuron;
		private final static Logger LOGGER = Logger.getLogger(PropagateOutput.class);

		public PropagateOutput(SomNeuron neuron)
		{
			this.neuron = neuron;
		}

		public Double call()
		{
			try
			{
				this.neuron.propagate();
				return Double.valueOf(this.neuron.getOutput());
			}
			catch(Exception caught)
			{
				LOGGER.error("A throwable was caught!", caught);
				throw new DannRuntimeException("Throwable caught: " + caught, caught);
			}
			catch(Error caught)
			{
				LOGGER.error("A throwable was caught!", caught);
				throw new Error("Throwable caught: " + caught, caught);
			}
		}
	}

	private class TrainNeuron implements Runnable
	{
		private final SomNeuron neuron;
		private final Vector neuronPoint;
		private final Vector bestMatchPoint;
		private final double neighborhoodRadius;
		private final double learningRate;

		public TrainNeuron(SomNeuron neuron, Vector neuronPoint, Vector bestMatchPoint, double neighborhoodRadius, double learningRate)
		{
			this.neuron = neuron;
			this.neuronPoint = neuronPoint;
			this.bestMatchPoint = bestMatchPoint;
			this.neighborhoodRadius = neighborhoodRadius;
			this.learningRate = learningRate;
		}

		public void run()
		{
			try
			{
				final double currentDistance = this.neuronPoint.calculateRelativeTo(this.bestMatchPoint).getDistance();
				if( currentDistance < this.neighborhoodRadius)
				{
					final double neighborhoodAdjustment = neighborhoodFunction(currentDistance);
					this.neuron.train(this.learningRate, neighborhoodAdjustment);
				}
			}
			catch(Exception caught)
			{
				LOGGER.error("A throwable was caught in TrainNeuron!", caught);
				throw new DannRuntimeException("Throwable caught: " + caught, caught);
			}
			catch(Error caught)
			{
				LOGGER.error("A throwable was caught in TrainNeuron!", caught);
				throw new Error("Throwable caught: " + caught, caught);
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
	protected AbstractSomBrain(int inputCount, int dimentionality)
	{
		if( inputCount <= 0 )
			throw new IllegalArgumentException("input count must be greater than 0");
		if(dimentionality <= 0)
			throw new IllegalArgumentException("dimentionality must be greater than 0");
		
		this.upperBounds = new Vector(dimentionality);
		this.lowerBounds = new Vector(dimentionality);
		for(int inputIndex = 0; inputIndex < inputCount; inputIndex++)
		{
			SomInputNeuron newNeuron = new SomInputNeuron(this);
			this.inputs.add(newNeuron);
			super.add(newNeuron);
		}
	}

	private void updateBounds(final Vector position)
	{
		//make sure we have the proper dimentionality
		if(position.getDimensions() != this.upperBounds.getDimensions())
			throw new IllegalArgumentException("Dimentionality mismatch");

		for(int dimensionIndex = 1; dimensionIndex <= position.getDimensions(); dimensionIndex++)
		{
			if(this.upperBounds.getCoordinate(dimensionIndex) < position.getCoordinate(dimensionIndex))
				this.upperBounds = this.upperBounds.setCoordinate(position.getCoordinate(dimensionIndex), dimensionIndex);
			if(this.lowerBounds.getCoordinate(dimensionIndex) > position.getCoordinate(dimensionIndex))
				this.lowerBounds = this.lowerBounds.setCoordinate(position.getCoordinate(dimensionIndex), dimensionIndex);
		}
	}

	/**
	 * Creates a new point in the output lattice at the given position. This
	 * will automatically have all inputs connected to it.
	 *
	 * @param position The position of the new output in the latice.
	 * @since 2.0
	 */
	public void createOutput(final Vector position)
	{
		//make sure we have the proper dimentionality
		if(position.getDimensions() != this.getUpperBounds().getDimensions())
			throw new IllegalArgumentException("Dimentionality mismatch");

		final Vector positionCopy = new Vector(position);

		//increase the upper bounds if needed
		this.updateBounds(positionCopy);

		//create and add the new output neuron
		final SomNeuron outputNeuron = new SomNeuron(this);
		this.outputs.put(positionCopy, outputNeuron);
		this.add(outputNeuron);

		//connect all inputs to the new neuron
		try
		{
			for(SomInputNeuron input : inputs)
				this.connect(input, outputNeuron);
		}
		catch(InvalidConnectionTypeDannException caught)
		{
			LOGGER.error("An error was caught that wasnt expected", caught);
			throw new UnexpectedDannError("unexpected InvalidConnectionTypeDannException", caught);
		}
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
		for(Vector position : this.outputs.keySet())
			positions.add(new Vector(position));
		return Collections.unmodifiableSet(positions);
	}

	/**
	 * Gets the current output at the specified position in the output lattice
	 * if the position doesnt have a SomNeuron associated with it then it
	 * returns null.
	 *
	 * @param position position in the output latice of the output you wish to
	 * retreive.
	 * @return The value of the specified SomNeuron, or null if there is no
	 * SomNeuron associated with the given position.
	 * @since 2.0
	 */
	public final double getOutput(final Vector position)
	{
		final SomNeuron outputNeuron = this.outputs.get(position);
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
		if( this.outputs.size() <= 0)
			throw new IllegalStateException("Must have atleast one output");

		//stick all the neurons in the queue to propogate
		final HashMap<Vector, Future<Double>> futureOutput = new HashMap<Vector, Future<Double>>();
		for(Entry<Vector, SomNeuron> entry : this.outputs.entrySet())
		{
			final PropagateOutput callable = new PropagateOutput(entry.getValue());
			futureOutput.put(entry.getKey(), this.getThreadExecutor().submit(callable));
		}

		//find the best matching unit
		Vector bestMatchingUnit = null;
		double bestError = Double.POSITIVE_INFINITY;
		for(Entry<Vector, SomNeuron> entry : this.outputs.entrySet())
		{
			double currentError;
			try
			{
				currentError = futureOutput.get(entry.getKey()).get().doubleValue();
			}
			catch(InterruptedException caught)
			{
				LOGGER.error("PropagateOutput was unexpectidy interupted", caught);
				throw new InterruptedDannRuntimeException("Unexpected interuption. Get should block indefinately", caught);
			}
			catch(ExecutionException caught)
			{
				LOGGER.error("PropagateOutput was had an unexcepted problem executing.", caught);
				throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
			}

			if(bestMatchingUnit == null)
				bestMatchingUnit = entry.getKey();
			else if(currentError < bestError)
			{
				bestMatchingUnit = entry.getKey();
				bestError = currentError;
			}
		}

		if(train)
			this.train(bestMatchingUnit);

		return bestMatchingUnit;
	}

	private void train(final Vector bestMatchingUnit)
	{
		final double neighborhoodRadius = this.neighborhoodRadiusFunction();
		final double learningRate = this.learningRateFunction();

		//add all the neuron trainingevents to the thread queue
		final ArrayList<Future> futures = new ArrayList<Future>();
		for(Entry<Vector, SomNeuron> entry : this.outputs.entrySet())
		{
			final TrainNeuron runnable = new TrainNeuron(entry.getValue(), entry.getKey(), bestMatchingUnit, neighborhoodRadius, learningRate);
			futures.add(this.getThreadExecutor().submit(runnable));
		}

		//wait until all neurons are trained
		try
		{
			for(Future future : futures)
				future.get();
		}
		catch(InterruptedException caught)
		{
			LOGGER.error("PropagateOutput was unexpectidy interupted", caught);
			throw new InterruptedDannRuntimeException("Unexpected interuption. Get should block indefinately", caught);
		}
		catch(ExecutionException caught)
		{
			LOGGER.error("PropagateOutput had an unexpected problem executing.", caught);
			throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
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
		return iterationsTrained;
	}



	/**
	 * The upper bounds of the positions of the output neurons.
	 *
	 * @since 2.0
	 * @return the upperBounds
	 */
	protected final Vector getUpperBounds()
	{
		return upperBounds;
	}



	/**
	 * The lower bounds of the positions of the output neurons.
	 *
	 * @since 2.0
	 * @return the lowerBounds
	 */
	protected final Vector getLowerBounds()
	{
		return lowerBounds;
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
	 * @param inputIndex
	 * @param inputValue
	 */
	public final void setInput(final int inputIndex, final double inputValue)
	{
		if(inputIndex >= this.getInputCount())
			throw new IllegalArgumentException("inputIndex is out of bounds");
		
		final SomInputNeuron currentInput = this.inputs.get(inputIndex);
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
		for(Entry<Vector,SomNeuron> output : this.outputs.entrySet())
		{
			final double[] weightVector = new double[this.inputs.size()];
			final SomNeuron currentNeuron = output.getValue();
			final Vector currentPoint = output.getKey();

			//iterate through the weight vectors of the current neuron
			for(Synapse source : this.getInEdges(currentNeuron))
			{
				final int sourceIndex = this.inputs.indexOf(source.getSourceNode());
				weightVector[sourceIndex] = source.getWeight();
			}

			//add the current weight vector to the map
			weightVectors.put(currentPoint, weightVector);
		}

		return Collections.unmodifiableMap(weightVectors);
	}

	/**
	 * Determines the neighboorhood function based on the neurons distance from
	 * the Best Matching Unit (BMU).
	 *
	 * @param distanceFromBest The neuron's distance from the BMU.
	 * @return the decay effecting the learning of the specified neuron due to
	 * its distance from the BMU.
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
