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
package com.syncleus.tests.dann.neural.som;

import com.syncleus.dann.math.Vector;
import com.syncleus.dann.neural.som.brain.ExponentialDecaySomBrain;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.*;

public class TestColor
{
	private static final int TEST_ITERATIONS = 100;
	private static final int TRAIN_ITERATIONS = 10000;
	private static final double DRIFT_FACTOR = 400.0;
	private static final int OUTPUT_WIDTH = 10;
	private static final int OUTPUT_HEIGHT = 10;
	private static final int OUTPUT_DIMENSIONS = 2;
	private static final double LEARNING_RATE = 0.1;
	private static final int INPUT_DIMENSIONS = 3;

	@Test
	public void testColor()
	{
		final Random random = new Random();

		final int cores = Runtime.getRuntime().availableProcessors();
		final ThreadPoolExecutor executor = new ThreadPoolExecutor(cores+1, cores*2, 20, TimeUnit.SECONDS, new LinkedBlockingQueue());
		try
		{
			//initialize brain with 3d input and 2d output
			final ExponentialDecaySomBrain brain = new ExponentialDecaySomBrain(INPUT_DIMENSIONS, OUTPUT_DIMENSIONS, TRAIN_ITERATIONS, LEARNING_RATE, executor);

			//create the output latice
			for(double x = 0; x < OUTPUT_WIDTH; x++)
				for(double y = 0; y < OUTPUT_HEIGHT; y++)
					brain.createOutput(new Vector(new double[]{x, y}));

			//run through random training data
			for(int iteration = 0; iteration < TRAIN_ITERATIONS; iteration++)
			{
				brain.setInput(0, random.nextDouble());
				brain.setInput(1, random.nextDouble());
				brain.setInput(2, random.nextDouble());

				brain.getBestMatchingUnit(true);
			}

			//some static varibles for the blocksize
			final double blockSize = 0.0025;
			final double maxOffset = 1.0 - blockSize;
			//test the maximum distance of close colors in the color space
			double farthestDistanceClose = 0.0;
			String closeOutText = "";
			for(int iteration = 0; iteration < TEST_ITERATIONS; iteration++)
			{
				final StringBuffer outText = new StringBuffer(64);
				//find a mutual offset in the color space (leaving room for the
				//block)
				final double redOffset = random.nextDouble() * maxOffset;
				final double greenOffset =  random.nextDouble() * maxOffset;
				final double blueOffset =  random.nextDouble() * maxOffset;
				outText.append("close color offsets... red: " + redOffset + ", green: " + greenOffset + ", blue: " + blueOffset + "\n");

				//get the location of a color within the block
				brain.setInput(0, redOffset + (random.nextDouble() * blockSize));
				brain.setInput(1, greenOffset + (random.nextDouble() * blockSize));
				brain.setInput(2, blueOffset + (random.nextDouble() * blockSize));
				outText.append("close color1... red:" + brain.getInput(0) + ", green: " +brain.getInput(1) + ", blue" + brain.getInput(2) + "\n");
				final Vector color1 = brain.getBestMatchingUnit(true);

				//get the location of the other color within the block
				brain.setInput(0, redOffset + (random.nextDouble() * blockSize));
				brain.setInput(1, greenOffset + (random.nextDouble() * blockSize));
				brain.setInput(2, blueOffset + (random.nextDouble() * blockSize));
				outText.append("close color2... red:" + brain.getInput(0) + ", green: " +brain.getInput(1) + ", blue" + brain.getInput(2) + "\n");
				final Vector color2 = brain.getBestMatchingUnit(true);

				//calculate the distance between these two points
				outText.append("close color1 point: " + color1 + "\n");
				outText.append("close color2 point: " + color2 + "\n");
				final double distance = color1.calculateRelativeTo(color2).getDistance();
				outText.append("close color distance: " + distance + "\n");
				//store the distance if its greater than the current max
				if( farthestDistanceClose < distance )
				{
					farthestDistanceClose = distance;
					closeOutText = outText.toString();
				}
			}

			//test the maximum distance of far colors in the color space
			final double maxDrift = maxOffset/DRIFT_FACTOR;
			double closestDistanceFar = Double.POSITIVE_INFINITY;
			String farOutText = "";
			for(int iteration = 0; iteration < TEST_ITERATIONS; iteration++)
			{
				final StringBuffer outText = new StringBuffer(64);
				//get the location of a color within the block
				final boolean isRed1Positive = random.nextBoolean();
				final boolean isGreen1Positive = random.nextBoolean();
				final boolean isBlue1Positive = random.nextBoolean();
				brain.setInput(0, ( isRed1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
				brain.setInput(1, ( isGreen1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
				brain.setInput(2, ( isBlue1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
				outText.append("far color1... red:" + brain.getInput(0) + ", green: " +brain.getInput(1) + ", blue" + brain.getInput(2) + "\n");
				final Vector color1 = brain.getBestMatchingUnit(true);

				//get the location of the other color within the block
				brain.setInput(0, ( isRed1Positive ? 1.0 - (random.nextDouble() * maxDrift) : random.nextDouble() * maxDrift));
				brain.setInput(1, ( isGreen1Positive ? 1.0 - (random.nextDouble() * maxDrift) : random.nextDouble() * maxDrift));
				brain.setInput(2, ( isBlue1Positive ? 1.0 - (random.nextDouble() * maxDrift) : random.nextDouble() * maxDrift));
				outText.append("far color2... red:" + brain.getInput(0) + ", green: " +brain.getInput(1) + ", blue" + brain.getInput(2) + "\n");
				final Vector color2 = brain.getBestMatchingUnit(true);

				//calculate the distance between these two points
				outText.append("far color1 point: " + color1 + "\n");
				outText.append("far color2 point: " + color2 + "\n");
				final double distance = color1.calculateRelativeTo(color2).getDistance();
				outText.append("far color distance: " + distance + "\n");
				//store the distance if its greater than the current max
				if( closestDistanceFar > distance )
				{
					closestDistanceFar = distance;
					farOutText = outText.toString();
				}
			}

			//check that the farthest close is closer than the farthest far,
			//essentially make sure similar colors are always close and
			//dissimilar colors are always far away.
			Assert.assertTrue("colors did not map properly: far: " + closestDistanceFar + " -> close: " + farthestDistanceClose + "\n" + closeOutText + "\n" + farOutText + "\n", closestDistanceFar > farthestDistanceClose);
		}
		finally
		{
			executor.shutdown();
		}
	}
}