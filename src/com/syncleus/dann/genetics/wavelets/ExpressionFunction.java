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
package com.syncleus.dann.genetics.wavelets;

import java.util.*;
import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.math.wave.WaveMultidimensionalFunction;
import com.syncleus.dann.math.wave.wavelet.CombinedWaveletFunction;
import org.apache.log4j.Logger;

public class ExpressionFunction implements Cloneable
{
	private static final Random RANDOM = Mutations.getRandom();
	private Set<ReceptorKey> receptors;
	private List<WaveMultidimensionalFunction> waves;
	private CombinedWaveletFunction wavelet;
	private static final Logger LOGGER = Logger.getLogger(ExpressionFunction.class);

	public ExpressionFunction(final ExpressionFunction copy)
	{
		this.receptors = new HashSet<ReceptorKey>(copy.receptors);
		this.waves = new ArrayList<WaveMultidimensionalFunction>(copy.waves);
		this.wavelet = copy.wavelet;
	}

	public ExpressionFunction(final ReceptorKey initialInput)
	{
		this.waves = new ArrayList<WaveMultidimensionalFunction>();
		this.receptors = new HashSet<ReceptorKey>();
		this.receptors.add(initialInput);
		final WaveMultidimensionalFunction initialWave = generateNewWave();
		this.waves.add(initialWave);
	}

	private ExpressionFunction()
	{
	}

	public CombinedWaveletFunction getWaveletMathFunction()
	{
		return this.wavelet;
	}

	public int getWaveCount()
	{
		this.reconstructWavelet();
		return this.wavelet.getWaveCount();
	}

	public Set<ReceptorKey> getReceptors()
	{
		return Collections.unmodifiableSet(this.receptors);
	}

	public CombinedWaveletFunction getWavelet()
	{
		this.reconstructWavelet();
		return this.wavelet.clone();
	}

	public boolean receives(final SignalKey signal)
	{
		for(final ReceptorKey receptor : this.receptors)
		{
			if (receptor.binds(signal))
				return true;
		}
		return false;
	}

	public double calculate(final Set<SignalKeyConcentration> signalConcentrations)
	{
		this.reconstructWavelet();
		for(final ReceptorKey receptor : this.receptors)
		{
			double concentration = 0.0;
			//calculate concentration for the current receptor
			for(final SignalKeyConcentration signalConcentration : signalConcentrations)
			{
				if (receptor.binds(signalConcentration.getSignal()))
					concentration += signalConcentration.getConcentration();
			}
			this.wavelet.setParameter(String.valueOf(receptor.hashCode()), concentration);
		}
		return this.wavelet.calculate();
	}

	@Override
	public ExpressionFunction clone()
	{
		try
		{
			final ExpressionFunction copy = (ExpressionFunction) super.clone();
			copy.receptors = new HashSet<ReceptorKey>(this.receptors);
			final List<WaveMultidimensionalFunction> newWaves = new ArrayList<WaveMultidimensionalFunction>();
			for(final WaveMultidimensionalFunction wave : this.waves)
				newWaves.add(wave.clone());
			copy.waves = newWaves;
			copy.wavelet = this.wavelet.clone();
			return copy;
		}
		catch (CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

	private void reconstructWavelet()
	{
		final String[] receptorNames = new String[this.receptors.size()];
		int receptorNamesIndex = 0;
		for(final ReceptorKey receptor : this.receptors)
			receptorNames[receptorNamesIndex++] = String.valueOf(receptor.hashCode());
		this.wavelet = new CombinedWaveletFunction(receptorNames);
		for(final WaveMultidimensionalFunction wave : this.waves)
		{
			this.wavelet.addWave(wave);
		}
	}

	/**
	 * Internally mutates.<br/> <br/> may change in any of the following
	 * ways:<br/> <ul> <li>add a new bound wave</li> <li>copy an existing wave
	 * and mutates it adding the new mutated wave</li> <li>delete an existing
	 * wave</li> <li>removing a signal</li> <li>Do nothing</li> </ul>
	 *
	 * @return New mutated wavelet
	 */
	public ExpressionFunction mutate(final double deviation)
	{
		final ExpressionFunction copy = this.clone();
		while (RANDOM.nextFloat() < (float) 0.1)
		{
			//add a mutated copy of an existing wave
			if (RANDOM.nextDouble() < 0.1)
			{
				//Signal newSignal = this.getRandomSignal();
				//return this.mutate(newSignal);
				copy.waves.add(this.generateRandomWave());
			}
			//make a RANDOM new wave
			if (RANDOM.nextDouble() < 0.1)
			{
				copy.waves.add(this.generateNewWave());
			}
			//delete a RANDOM wave
			if ((this.waves.size() > 1) && (RANDOM.nextDouble() < 0.1))
			{
				final WaveMultidimensionalFunction deleteWave = copy.waves.get(RANDOM.nextInt(copy.waves.size()));
				copy.waves.remove(deleteWave);
			}
			//only delete if there will be atleast one signal left
			if ((this.receptors.size() > 1) && (RANDOM.nextDouble() < 0.1))
			{
				final ReceptorKey[] receptorArray = new ReceptorKey[copy.receptors.size()];
				copy.receptors.toArray(receptorArray);
				final ReceptorKey deleteReceptor = receptorArray[RANDOM.nextInt(receptorArray.length)];
				copy.receptors.remove(deleteReceptor);
				final ReceptorKey[] copyReceptors = new ReceptorKey[copy.receptors.size()];
				copy.receptors.toArray(copyReceptors);
				final String[] dimensionNames = new String[copyReceptors.length];
				int dimensionNamesIndex = 0;
				for(final ReceptorKey copyReceptor : copyReceptors)
					dimensionNames[dimensionNamesIndex++] = String.valueOf(copyReceptor.hashCode());
				copy.waves.clear();
				for(final WaveMultidimensionalFunction wave : this.waves)
				{
					final WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(dimensionNames);
					newWave.setAmplitude(wave.getAmplitude());
					newWave.setDistribution(wave.getDistribution());
					newWave.setForm(wave.getForm());
					newWave.setFrequency(wave.getFrequency());
					newWave.setPhase(wave.getPhase());
					for(final String dimension : dimensionNames)
					{
						newWave.setCenter(dimension, wave.getCenter(dimension));
						newWave.setDimension(dimension, wave.getDimension(dimension));
					}
					copy.waves.add(newWave);
				}
			}
		}
		return copy;
	}

	/**
	 * Mutates by incorperating a new signal into the mutated result.<br/> <br/>
	 * May mutate by:<br/> <ul> <li>adding the new signal</li> <ul>
	 *
	 * @param newReceptor The new receptor to possibly incorperate into
	 * mutation
	 * @param deviation RANDOM deviation for mutation.
	 * @return New mutated wavelet
	 */
	public ExpressionFunction mutate(final double deviation, final ReceptorKey newReceptor)
	{
		final ExpressionFunction copy = this.clone();
		copy.receptors.add(newReceptor);
		if (copy.receptors.size() > this.receptors.size())
		{
			copy.waves.clear();
			for(final WaveMultidimensionalFunction wave : this.waves)
			{
				final String[] names = new String[wave.getDimensionNames().length + 1];
				int index = 0;
				for(final String dimensionName : wave.getDimensionNames())
					names[index++] = dimensionName;
				names[index] = String.valueOf(newReceptor.hashCode());
				final WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(names);
				newWave.setAmplitude(wave.getAmplitude());
				newWave.setDistribution(wave.getDistribution());
				newWave.setForm(wave.getForm());
				newWave.setFrequency(wave.getFrequency());
				newWave.setPhase(wave.getPhase());
				for(final String dimension : wave.getDimensionNames())
				{
					newWave.setCenter(dimension, wave.getCenter(dimension));
					newWave.setDimension(dimension, wave.getDimension(dimension));
				}
				copy.waves.add(newWave);
			}
		}
		return copy.mutate(1.0);
	}

	private WaveMultidimensionalFunction generateNewWave()
	{
		final String[] dimensionNames = new String[this.receptors.size()];
		int index = 0;
		for(final ReceptorKey receptor : this.receptors)
			dimensionNames[index++] = String.valueOf(receptor.hashCode());
		final WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(dimensionNames);
		newWave.setFrequency(RANDOM.nextGaussian() * 0.001);
		newWave.setPhase(RANDOM.nextGaussian() * 10);
		newWave.setAmplitude(RANDOM.nextGaussian());
		newWave.setForm(Math.abs(RANDOM.nextGaussian()));
		if (newWave.getForm() <= 0.0)
			newWave.setForm(newWave.getForm() + ((1 + RANDOM.nextGaussian()) * 10));
		for(final String dimensionName : dimensionNames)
			newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((RANDOM.nextFloat() * 2 - 1) * 100));
		newWave.setDistribution(RANDOM.nextFloat() * 100);
		return newWave;
	}

	private WaveMultidimensionalFunction generateRandomWave()
	{
		if (!this.waves.isEmpty())
		{
			final WaveMultidimensionalFunction[] wavesArray = new WaveMultidimensionalFunction[this.waves.size()];
			this.waves.toArray(wavesArray);
			final WaveMultidimensionalFunction randomWave = wavesArray[RANDOM.nextInt(wavesArray.length)];
			final WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(randomWave);
			if (RANDOM.nextDouble() <= 1.0)
				newWave.setFrequency(newWave.getFrequency() + ((RANDOM.nextFloat() * 2 - 1) * 0.01));
			if (RANDOM.nextDouble() <= 1.0)
				newWave.setPhase(newWave.getPhase() + ((RANDOM.nextFloat() * 2 - 1) * 10));
			if (RANDOM.nextDouble() <= 1.0)
				newWave.setAmplitude(newWave.getAmplitude() + ((RANDOM.nextFloat() * 2 - 1) * 10));
			if (RANDOM.nextDouble() <= 1.0)
				newWave.setForm(newWave.getForm() + (RANDOM.nextFloat() * 0.01));
			if (RANDOM.nextDouble() <= 1.0)
				newWave.setDistribution(newWave.getDistribution() + ((RANDOM.nextFloat() * 2 - 1) * 100));
			if (RANDOM.nextDouble() <= 1.0)
			{
				final String[] dimensionNames = newWave.getDimensionNames();
				for(final String dimensionName : dimensionNames)
					newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((RANDOM.nextFloat() * 2 - 1) * 100));
			}
			return newWave;
		}
		return null;
	}

	@Override
	public String toString()
	{
		this.reconstructWavelet();
		return this.wavelet.toString();
	}
}
