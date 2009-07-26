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

import com.syncleus.dann.math.Hyperpoint;
import com.syncleus.dann.neural.som.ExponentialDecaySomBrain;
import java.util.Random;
import org.junit.*;

public class TestColor
{
	@Test
	public void testLoop()
	{
		for(int i = 0; i < 200;i++)
			this.testColor();
	}

	
	public void testColor()
	{
		final int iterations = 10000;
		final Random random = new Random();

		//initialize brain with 3d input and 2d output
		ExponentialDecaySomBrain brain = new ExponentialDecaySomBrain(3, 2, iterations, 0.1);
		
		//create the output latice
		for(double x = 0; x < 10; x++)
			for(double y = 0; y < 10; y++)
				brain.createOutput(new Hyperpoint(new double[]{x, y}));

		//run through random training data
		for(int iteration = 0; iteration < iterations; iteration++)
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
		for(int iteration = 0; iteration < 100; iteration++)
		{
			String outText = "";
			//find a mutual offset in the color space (leaving room for the
			//block)
			double redOffset = random.nextDouble() * maxOffset;
			double greenOffset =  random.nextDouble() * maxOffset;
			double blueOffset =  random.nextDouble() * maxOffset;
			outText += "close color offsets... red: " + redOffset + ", green: " + greenOffset + ", blue: " + blueOffset + "\n";

			//get the location of a color within the block
			brain.setInput(0, redOffset + (random.nextDouble() * blockSize));
			brain.setInput(1, greenOffset + (random.nextDouble() * blockSize));
			brain.setInput(2, blueOffset + (random.nextDouble() * blockSize));
			outText += "close color1... red:" + brain.getInput(0) + ", green: " +brain.getInput(1) + ", blue" + brain.getInput(2) + "\n";
			Hyperpoint color1 = brain.getBestMatchingUnit(true);

			//get the location of the other color within the block
			brain.setInput(0, redOffset + (random.nextDouble() * blockSize));
			brain.setInput(1, greenOffset + (random.nextDouble() * blockSize));
			brain.setInput(2, blueOffset + (random.nextDouble() * blockSize));
			outText += "close color2... red:" + brain.getInput(0) + ", green: " +brain.getInput(1) + ", blue" + brain.getInput(2) + "\n";
			Hyperpoint color2 = brain.getBestMatchingUnit(true);

			//calculate the distance between these two points
			outText += "close color1 point: " + color1 + "\n";
			outText += "close color2 point: " + color2 + "\n";
			double distance = color1.calculateRelativeTo(color2).getDistance();
			outText += "close color distance: " + distance + "\n";
			//store the distance if its greater than the current max
			if( farthestDistanceClose < distance )
			{
				farthestDistanceClose = distance;
				closeOutText = outText;
			}
		}

		//test the maximum distance of far colors in the color space
		final double maxDrift = maxOffset/400.0;
		double closestDistanceFar = Double.POSITIVE_INFINITY;
		String farOutText = "";
		for(int iteration = 0; iteration < 100; iteration++)
		{
			String outText= "";
			//get the location of a color within the block
			boolean isRed1Positive = random.nextBoolean();
			boolean isGreen1Positive = random.nextBoolean();
			boolean isBlue1Positive = random.nextBoolean();
			brain.setInput(0, ( isRed1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
			brain.setInput(1, ( isGreen1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
			brain.setInput(2, ( isBlue1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
			outText += "far color1... red:" + brain.getInput(0) + ", green: " +brain.getInput(1) + ", blue" + brain.getInput(2) + "\n";
			Hyperpoint color1 = brain.getBestMatchingUnit(true);

			//get the location of the other color within the block
			brain.setInput(0, ( !isRed1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
			brain.setInput(1, ( !isGreen1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
			brain.setInput(2, ( !isBlue1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)));
			outText += "far color2... red:" + brain.getInput(0) + ", green: " +brain.getInput(1) + ", blue" + brain.getInput(2) + "\n";
			Hyperpoint color2 = brain.getBestMatchingUnit(true);

			//calculate the distance between these two points
			outText += "far color1 point: " + color1 + "\n";
			outText += "far color2 point: " + color2 + "\n";
			double distance = color1.calculateRelativeTo(color2).getDistance();
			outText += "far color distance: " + distance + "\n";
			//store the distance if its greater than the current max
			if( closestDistanceFar > distance )
			{
				closestDistanceFar = distance;
				farOutText = outText;
			}
		}

		//check that the farthest close is closer than the farthest far,
		//essentially make sure similar colors are always close and
		//dissimilar colors are always far away.
		Assert.assertTrue("colors did not map properly: far: " + closestDistanceFar + " -> close: " + farthestDistanceClose + "\n" + closeOutText + "\n" + farOutText + "\n", closestDistanceFar > farthestDistanceClose);
	}
}
