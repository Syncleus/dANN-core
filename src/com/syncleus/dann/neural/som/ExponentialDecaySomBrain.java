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
package com.syncleus.dann.neural.som;

public class ExponentialDecaySomBrain extends SomBrain
{
	int iterationsToConverge;
	double initialLearningRate;

	public ExponentialDecaySomBrain(int inputCount, int dimentionality, int iterationsToConverge, double initialLearningRate)
	{
		super(inputCount, dimentionality);
		this.iterationsToConverge = iterationsToConverge;
		this.initialLearningRate = initialLearningRate;
	}

	private double getIntialRadius()
	{
		double maxCrossSection = 0.0;
		for(int dimensionIndex = 1; dimensionIndex <= this.getUpperBounds().getDimensions(); dimensionIndex++)
		{
			double crossSection = this.getUpperBounds().getCoordinate(dimensionIndex) - this.getLowerBounds().getCoordinate(dimensionIndex);
			if( crossSection > maxCrossSection)
				maxCrossSection = crossSection;
		}

		return maxCrossSection/2.0;
	}

	private double getTimeConstant()
	{
		return ((double)this.iterationsToConverge) / Math.log(this.getIntialRadius());
	}

	protected double neighborhoodFunction(double distanceFromBest)
	{
		return Math.exp(-1.0*(Math.pow(distanceFromBest, 2.0))/(2.0*Math.pow(this.neighborhoodRadiusFunction(), 2.0)));
	}

	protected double neighborhoodRadiusFunction()
	{
		return this.getIntialRadius() * Math.exp(-1.0 * this.getIterationsTrained() / this.getTimeConstant());
	}

	protected double learningRateFunction()
	{
		return this.initialLearningRate * Math.exp(-1.0 * this.getIterationsTrained() / this.getTimeConstant());
	}
}
