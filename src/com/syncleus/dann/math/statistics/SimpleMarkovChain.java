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
package com.syncleus.dann.math.statistics;

import com.syncleus.dann.math.linear.RealMatrix;
import com.syncleus.dann.math.linear.SimpleRealMatrix;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class SimpleMarkovChain<S> extends AbstractMarkovChain<S>
{
	private RealMatrix transitionProbabilityMatrix;
	private ArrayList<List<S>> rowMapping;
	private ArrayList<S> columnMapping;
	private Set<S> states;
	private int order;

	private final ArrayDeque<S> history;

	private static final double MAXIMUM_ROW_ERROR = 0.00001;
	private static final Random RANDOM = new Random();

	public SimpleMarkovChain(Map<List<S>, Map<S, Double>> transitionProbabilities, int order, Set<S> states)
	{
		this.history = new ArrayDeque<S>(order);
		this.order = order;
		this.states = Collections.unmodifiableSet(states);
		this.columnMapping = new ArrayList<S>(this.states);
		this.rowMapping = new ArrayList<List<S>>();

		int rows = transitionProbabilities.size();
		int columns = states.size();
		double[][] matrixValues = new double[rows][columns];

		//iterate through all the new rows
		int row = 0;
		for(Entry<List<S>, Map<S, Double>> transitionProbability : transitionProbabilities.entrySet())
		{
			List<S> rowHeader = transitionProbability.getKey();
			Map<S, Double> rowTransition = transitionProbability.getValue();

			assert !rowMapping.contains(rowHeader);

			this.rowMapping.add(rowHeader);

			double rowSum = 0.0;
			for(Entry<S,Double> stateTransition : rowTransition.entrySet())
			{
				int column = this.columnMapping.indexOf(stateTransition.getKey());
				matrixValues[row][column] = stateTransition.getValue();
				rowSum += matrixValues[row][column];
			}

			if( Math.abs(rowSum - 1.0) > MAXIMUM_ROW_ERROR )
				throw new IllegalArgumentException("One of the rows does not sum to 1");

			row++;
		}

		this.transitionProbabilityMatrix = new SimpleRealMatrix(matrixValues);
	}

	public SimpleMarkovChain(Map<S, Map<S, Double>> transitionProbabilities, Set<S> states)
	{
		this.history = new ArrayDeque<S>(1);
		this.order = 1;
		this.states = Collections.unmodifiableSet(states);
		this.columnMapping = new ArrayList<S>(this.states);
		this.rowMapping = new ArrayList<List<S>>();

		int rows = transitionProbabilities.size();
		int columns = states.size();
		double[][] matrixValues = new double[rows][columns];

		//iterate through all the new rows
		int row = 0;
		for(Entry<S, Map<S, Double>> transitionProbability : transitionProbabilities.entrySet())
		{
			List<S> rowHeader = Collections.singletonList(transitionProbability.getKey());
			Map<S, Double> rowTransition = transitionProbability.getValue();

			assert !rowMapping.contains(rowHeader);

			this.rowMapping.add(rowHeader);

			double rowSum = 0.0;
			for(Entry<S,Double> stateTransition : rowTransition.entrySet())
			{
				int column = this.columnMapping.indexOf(stateTransition.getKey());
				matrixValues[row][column] = stateTransition.getValue();
				rowSum += matrixValues[row][column];
			}

			if( Math.abs(rowSum - 1.0) > MAXIMUM_ROW_ERROR )
				throw new IllegalArgumentException("One of the rows does not sum to 1");

			row++;
		}

		this.transitionProbabilityMatrix = new SimpleRealMatrix(matrixValues);
	}

	public int getOrder()
	{
		return this.order;
	}

	public Set<S> getStates()
	{
		return this.states;
	}

	public void transition(S nextState)
	{
		this.history.add(nextState);
		while(this.history.size() > this.order)
			this.history.poll();
	}

	public S generateTransition(boolean step)
	{
		List<S> currentState = this.getStateHistory();
		int row = this.rowMapping.indexOf(currentState);

		final double randomValue = RANDOM.nextDouble();
		double probabilityOffset = 0.0;
		S nextStep = this.columnMapping.get(this.columnMapping.size() - 1);
		for(int columnIndex = 0; columnIndex < this.transitionProbabilityMatrix.getWidth(); columnIndex++)
		{
			double transitionProbability = this.transitionProbabilityMatrix.getDouble(row, columnIndex);
			if( randomValue <  transitionProbability + probabilityOffset)
			{
				nextStep = this.columnMapping.get(columnIndex);
				break;
			}
			probabilityOffset += transitionProbability;
		}

		if(step)
			this.transition(nextStep);
		return nextStep;
	}

	public List<S> getStateHistory()
	{
		return Collections.unmodifiableList(new ArrayList<S>(this.history));
	}

	public void reset()
	{
		this.history.clear();
	}

	public Map<S,Double> getProbability(int steps)
	{
		List<S> currentState = this.getStateHistory();

		if((currentState == null)||(currentState.size() <= 0))
			throw new IllegalStateException("probability can not be calculated without atleast one transition");

		RealMatrix futureMatrix = this.transitionProbabilityMatrix;
		for(int currentStep = 0; currentStep < steps - 1; currentStep++)
			futureMatrix = futureMatrix.multiply(this.transitionProbabilityMatrix);

		int row = this.rowMapping.indexOf(currentState);

		Map<S,Double> probability = new HashMap<S,Double>();
		for(int columnIndex = 0; columnIndex < this.transitionProbabilityMatrix.getWidth(); columnIndex++)
		{
			double transitionProbability = futureMatrix.getDouble(row, columnIndex);
			S transitionState = this.columnMapping.get(columnIndex);
			probability.put(transitionState, transitionProbability);
		}

		return Collections.unmodifiableMap(probability);
	}

	public Map<S,Double> getSteadyStateProbability()
	{
//		double[][] tmpValues = {{-0.1, 0.5},{0.1,-0.5},{1.0,1.0}};
		RealMatrix steadyStateMatrix = this.transitionProbabilityMatrix.subtract(SimpleRealMatrix.identity(this.transitionProbabilityMatrix.getHeight(), this.transitionProbabilityMatrix.getWidth())).flip();
		double[][] simultaniousValues = new double[steadyStateMatrix.getHeight()+1][steadyStateMatrix.getWidth()];
		for(int rowIndex = 0; rowIndex < simultaniousValues.length; rowIndex++)
			for(int columnIndex = 0; columnIndex < simultaniousValues[0].length; columnIndex++)
			{
				if(rowIndex >= simultaniousValues.length - 1)
					simultaniousValues[rowIndex][columnIndex] = 1.0;
				else
					simultaniousValues[rowIndex][columnIndex] = steadyStateMatrix.get(rowIndex, columnIndex).doubleValue();
			}
		RealMatrix simultaniousMatrix = new SimpleRealMatrix(simultaniousValues);

		double[][] solutionValues = new double[simultaniousValues.length][1];
		solutionValues[simultaniousValues.length - 1][0] = 1.0;
		RealMatrix solutionMatrix = new SimpleRealMatrix(solutionValues);

//		System.out.println("transitionProbabilityMatrix: " + this.transitionProbabilityMatrix);
//		System.out.println("identity: " + SimpleRealMatrix.identity(this.transitionProbabilityMatrix.getHeight(), this.transitionProbabilityMatrix.getWidth()));
//		System.out.println("simultaniousMatrix: " + simultaniousMatrix);
//		System.out.println("solutionMatrix: " + solutionMatrix);
		RealMatrix SimultaniousSolved = simultaniousMatrix.solve(solutionMatrix);
		Map<S,Double> stateProbabilities = new HashMap<S,Double>();
		for(int stateIndex = 0; stateIndex < SimultaniousSolved.getHeight(); stateIndex++)
		{
			S currentState = this.columnMapping.get(stateIndex);
			double currentProbability = SimultaniousSolved.get(stateIndex, 0).doubleValue();

			stateProbabilities.put(currentState, currentProbability);
		}

		return Collections.unmodifiableMap(stateProbabilities);

//		RealMatrix tmpTest = new SimpleRealMatrix(tmpValues);
//		RealMatrix solution = tmpTest.solve(new SimpleRealMatrix(new double[][]{{0.0}, {0.0}, {1.0}}));
//		System.out.println("tmp solution: " + solution);
	}
}
