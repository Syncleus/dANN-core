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
package com.syncleus.dann.dataprocessing.signal.transform;

import java.util.*;
import com.syncleus.dann.math.*;

/**
 * A DiscreteFourierTransform is a fourier transform that operates on a discrete input function.
 * @author Jeffrey Phillips Freeman
 */
public class DiscreteFourierTransform
{
	private final ComplexNumber[] transform;
	private final NavigableMap<Double, ComplexNumber> frequencies;

	/**
	 * Creates a new DiscreteFourierTransform with the given frequencies and the given bitrate.
	 * @param ourFrequencies The frequencies to use
	 * @param bitrate The bitrate to use
	 */
	public DiscreteFourierTransform(final ComplexNumber[] ourFrequencies, final int bitrate)
	{
		final double frequencySize = ((double) ourFrequencies.length) / 2.0;
		final double frequencyStep = frequencyResolution(ourFrequencies.length, bitrate);
		final NavigableMap<Double, ComplexNumber> newFrequencies = new TreeMap<Double, ComplexNumber>();
		for(int index = 0; index <= (int) frequencySize; index++)
		{
			final Double currentFrequency = ((double) index) * frequencyStep;
			newFrequencies.put(currentFrequency, ourFrequencies[index]);
		}
		this.frequencies = newFrequencies;
		this.transform = ourFrequencies.clone();
	}

	/**
	 * Gets the largest frequency possible for the given bitrate.
	 * @param bitrate The bitrate to calculate the frequency for
	 * @return The upper frequency for the given bitrate
	 */
	public static double upperFrequency(final int bitrate)
	{
		return ((double) bitrate) / 2.0;
	}

	/**
	 * Calculates the frequency resolution for the given bitrate with the given block size.
	 * @param blockSize The block size to use
	 * @param bitrate The bit rate to use
	 * @return How accurate the frequency sampling is
	 */
	public static double frequencyResolution(final int blockSize, final int bitrate)
	{
		return upperFrequency(bitrate) / (((double) blockSize) / 2.0);
	}

	/**
	 * Gets the closest discrete frequency to the supplied frequency.
	 * @param frequency The input frequency
	 * @return The closest output frequency
	 */
	public double getClosestFrequency(final double frequency)
	{
		return this.frequencies.ceilingEntry(frequency).getKey();
	}

	public ComplexNumber getClosestPhasor(final double frequency)
	{
		return this.frequencies.ceilingEntry(frequency).getValue();
	}

	public ComplexNumber getPhasor(final double frequency)
	{
		return this.frequencies.get(frequency);
	}

	public double getClosestAmplitude(final double frequency)
	{
		return this.frequencies.ceilingEntry(frequency).getValue().absScalar();
	}

	public double getAmplitude(final double frequency)
	{
		return this.frequencies.get(frequency).absScalar();
	}

	public double getClosestPhase(final double frequency)
	{
		return this.frequencies.ceilingEntry(frequency).getValue().phase();
	}

	public double getPhase(final double frequency)
	{
		return this.frequencies.get(frequency).phase();
	}

	private ComplexNumber[] amplitudes(final double startFrequency, final double endFrequency)
	{
		final NavigableMap<Double, ComplexNumber> subFrequencies = this.frequencies.subMap(startFrequency, true, endFrequency, true);
		final ComplexNumber[] amplitudes = new ComplexNumber[subFrequencies.size()];
		subFrequencies.values().toArray(amplitudes);
		return amplitudes;
	}

	public double getBandSum(final double startFrequency, final double endFrequency)
	{
		return ComplexNumber.sum(amplitudes(startFrequency, endFrequency)).absScalar();
	}

	public double getBandRms(final double startFrequency, final double endFrequency)
	{
		return Averages.rms(amplitudes(startFrequency, endFrequency)).absScalar();
	}

	public double getBandMean(final double startFrequency, final double endFrequency)
	{
		return Averages.mean(amplitudes(startFrequency, endFrequency)).absScalar();
	}

	public double getBandGeometricMean(final double startFrequency, final double endFrequency)
	{
		return Averages.geometricMean(amplitudes(startFrequency, endFrequency)).absScalar();
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
		return this.frequencies.firstEntry().getKey();
	}

	public double getMaximumFrequency()
	{
		return this.frequencies.lastEntry().getKey();
	}
}
