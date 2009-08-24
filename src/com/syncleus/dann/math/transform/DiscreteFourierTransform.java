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
package com.syncleus.dann.math.transform;

import com.syncleus.dann.math.*;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class DiscreteFourierTransform
{
	private final ComplexNumber[] transform;
	private final NavigableMap<Double, ComplexNumber> frequencies;

	public DiscreteFourierTransform(final ComplexNumber[] frequencies, final int bitrate)
	{
		final double frequencySize = ((double)frequencies.length)/2.0;
		final NavigableMap<Double, ComplexNumber> newFrequencies = new TreeMap<Double, ComplexNumber>();
		for(int index = 0; index < frequencySize; index++)
		{
			// frequency = (index/size)/(size/bitrate)
			Double currentFrequency = Double.valueOf((((double)(index+1))/frequencySize)/(((double)frequencies.length)/((double)bitrate)));
			newFrequencies.put(currentFrequency, frequencies[index]);
		}
		this.frequencies = newFrequencies;

		this.transform = frequencies.clone();
	}

	public double getClosestFrequency(double frequency)
	{
		return this.frequencies.ceilingEntry(frequency).getKey().doubleValue();
	}

	public ComplexNumber getClosestPhasor(double frequency)
	{
		return this.frequencies.ceilingEntry(frequency).getValue();
	}

	public ComplexNumber getPhasor(double frequency)
	{
		return this.frequencies.get(Double.valueOf(frequency));
	}

	public double getClosestAmplitude(double frequency)
	{
		return this.frequencies.ceilingEntry(frequency).getValue().abs();
	}

	public double getAmplitude(double frequency)
	{
		return this.frequencies.get(Double.valueOf(frequency)).abs();
	}

	public double getClosestPhase(double frequency)
	{
		return this.frequencies.ceilingEntry(frequency).getValue().phase();
	}

	public double getPhase(double frequency)
	{
		return this.frequencies.get(Double.valueOf(frequency)).phase();
	}

	private ComplexNumber[] amplitudes(double startFrequency, double endFrequency)
	{
		final NavigableMap<Double, ComplexNumber> subFrequencies = this.frequencies.subMap(startFrequency, true, endFrequency, true);
		ComplexNumber[] amplitudes = new ComplexNumber[subFrequencies.size()];
		subFrequencies.values().toArray(amplitudes);
		return amplitudes;
	}

	public double getBandSum(double startFrequency, double endFrequency)
	{
		return ComplexNumber.sum(amplitudes(startFrequency, endFrequency)).abs();
	}

	public double getBandRms(double startFrequency, double endFrequency)
	{
		return ComplexNumber.rms(amplitudes(startFrequency, endFrequency)).abs();
	}

	public double getBandMean(double startFrequency, double endFrequency)
	{
		return ComplexNumber.mean(amplitudes(startFrequency, endFrequency)).abs();
	}

	public double getBandGeometricMean(double startFrequency, double endFrequency)
	{
		return ComplexNumber.geometricMean(amplitudes(startFrequency, endFrequency)).abs();
	}

	public ComplexNumber[] getTransform()
	{
		return this.transform.clone();
	}

	public SortedMap<Double, ComplexNumber> getFrequencyPhasors()
	{
		return Collections.unmodifiableSortedMap(this.frequencies);
	}

	public double getMinimumFrequency()
	{
		return this.frequencies.firstEntry().getKey().doubleValue();
	}

	public double getMaximumFrequency()
	{
		return this.frequencies.lastEntry().getKey().doubleValue();
	}
}
