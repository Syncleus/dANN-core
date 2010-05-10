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

public class SignalProcessingWavelet implements Comparable<SignalProcessingWavelet>, Cloneable
{
	// <editor-fold defaultstate="collapsed" desc="internal class">

	public abstract static class SignalConcentration implements Comparable<SignalConcentration>
	{
		private double value = 0.0;
		private long id = RANDOM.nextLong();

		public SignalConcentration()
		{
		}

		protected SignalConcentration(final SignalConcentration originalSignal)
		{
			this.value = originalSignal.value;
			this.id = originalSignal.id;
		}

		public double add(final double addValue)
		{
			this.value += addValue;
			return this.value;
		}

		public double getValue()
		{
			return this.value;
		}

		public void setValue(final double newValue)
		{
			this.value = newValue;
		}

		public long getId()
		{
			return this.id;
		}

		public int compareTo(final SignalConcentration compareWith)
		{
			return (this.id < compareWith.id ? -1 : (this.id > compareWith.id ? 1 : 0));
		}

		@Override
		public int hashCode()
		{
			return (int) this.id;
		}

		@Override
		public boolean equals(final Object compareWith)
		{
			if( compareWith instanceof SignalConcentration )
			{
				final SignalConcentration signalCompare = (SignalConcentration) compareWith;
				if( signalCompare.id == this.id )
					return true;
			}
			return false;
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="internal class">

	public static class GlobalSignalConcentration extends SignalConcentration
	{
		public GlobalSignalConcentration()
		{
		}

		protected GlobalSignalConcentration(final GlobalSignalConcentration originalSignal)
		{
			super(originalSignal);
		}
	}
	// </editor-fold>
	private long id;
	private SignalConcentration output;
	private double currentOutput = 0.0;
	private static final Random RANDOM = new Random();
	private Set<SignalConcentration> signals = new HashSet<SignalConcentration>();
	private List<WaveMultidimensionalFunction> waves = new ArrayList<WaveMultidimensionalFunction>();
	private CombinedWaveletFunction wavelet;
	private static final Logger LOGGER = Logger.getLogger(SignalProcessingWavelet.class);

	public SignalProcessingWavelet(final SignalConcentration initialInput, final SignalConcentration initialOutput)
	{
		this.output = initialOutput;
		this.signals.add(initialInput);
		final WaveMultidimensionalFunction initialWave = generateNewWave();
		this.waves.add(initialWave);
		this.id = RANDOM.nextLong();
	}

	private SignalProcessingWavelet()
	{
	}

	public int getWaveCount()
	{
		this.reconstructWavelet();
		return this.wavelet.getWaveCount();
	}

	public TreeSet<SignalConcentration> getSignals()
	{
		final TreeSet<SignalConcentration> copy = new TreeSet<SignalConcentration>(this.signals);
		copy.add(this.output);
		return copy;
	}

	public int compareTo(final SignalProcessingWavelet compareWith)
	{
		if( this.id < compareWith.id )
			return -1;
		else if( this.id > compareWith.id )
			return 1;
		return 0;
	}

	@Override
	public boolean equals(final Object compareWithObject)
	{
		if( compareWithObject instanceof SignalProcessingWavelet )
			return (this.id == ((SignalProcessingWavelet) compareWithObject).id);
		return false;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public CombinedWaveletFunction getWavelet()
	{
		this.reconstructWavelet();
		return this.wavelet.clone();
	}

	public void preTick()
	{
		this.reconstructWavelet();
		for(final SignalConcentration signal : this.signals)
		{
			this.wavelet.setParameter(this.wavelet.getParameterNameIndex(String.valueOf(signal.getId())), signal.getValue());
		}
		this.currentOutput = this.wavelet.calculate();
	}

	public void tick()
	{
		this.output.setValue(this.output.getValue() + this.currentOutput);
	}

	@Override
	public SignalProcessingWavelet clone()
	{
		try
		{
			final SignalProcessingWavelet copy = (SignalProcessingWavelet) super.clone();
			copy.signals = new TreeSet<SignalConcentration>(this.signals);
			final List<WaveMultidimensionalFunction> newWaves = new ArrayList<WaveMultidimensionalFunction>();
			for(final WaveMultidimensionalFunction wave : this.waves)
				newWaves.add(wave.clone());
			copy.waves = newWaves;
			copy.wavelet = (this.wavelet == null ? null : this.wavelet.clone());
			return copy;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

	private void reconstructWavelet()
	{
		final String[] signalNames = new String[this.signals.size()];
		//System.out.println("dimensions size: " + this.dimensions.size());
		int signalNamesIndex = 0;
		for(final SignalConcentration dimension : this.signals)
		{
			signalNames[signalNamesIndex++] = String.valueOf(dimension.getId());
		}
		this.wavelet = new CombinedWaveletFunction(signalNames);
		for(final WaveMultidimensionalFunction wave : this.waves)
		{
			this.wavelet.addWave(wave);
		}
	}

	/**
	 * Internally mutates.<br/> <br/> may change in any of the following ways:<br/>
	 * <ul> <li>add a new bound wave</li> <li>copy an existing wave and mutates it
	 * adding the new mutated wave</li> <li>delete an existing wave</li>
	 * <li>removing a signal</li> <li>Do nothing</li> </ul>
	 *
	 * @return New mutated wavelet
	 */
	public SignalProcessingWavelet mutate(final double deviation)
	{
		final SignalProcessingWavelet copy = this.clone();
		copy.id = RANDOM.nextLong();
		do
		{
			final float roll = RANDOM.nextFloat();
			//60% chance to add a mutated copy of an existing wave
			if( roll < (float) 0.2 )
			{
				//Signal newSignal = this.getRandomSignal();
				//return this.mutate(newSignal);
				copy.waves.add(this.generateRandomWave(deviation));
				copy.id = RANDOM.nextLong();
			}
			//20% to make a RANDOM new wave
			else if( roll < (float) 0.8 )
			{
				copy.waves.add(this.generateNewWave());
				copy.id = RANDOM.nextLong();
			}
			//10% to delete a RANDOM wave
			else if( roll < (float) 0.9 )
			{
				//only delete if there will be atleast one wave left
				if( copy.waves.size() > 1 )
				{
					final WaveMultidimensionalFunction deleteWave = copy.waves.get(RANDOM.nextInt(copy.waves.size()));
					copy.waves.remove(deleteWave);
				}
			}
			//10% to delete a signal
			else
			{
				//only delete if there will be atleast one signal left
				if( copy.signals.size() > 1 )
				{
					final SignalConcentration[] newSignals = new SignalConcentration[copy.signals.size()];
					copy.signals.toArray(newSignals);
					final SignalConcentration deleteSignal = newSignals[RANDOM.nextInt(newSignals.length)];
					copy.signals.remove(deleteSignal);
					final SignalConcentration[] copySignals = new SignalConcentration[copy.signals.size()];
					copy.signals.toArray(copySignals);
					final String[] dimensionNames = new String[copySignals.length];
					int dimensionNamesIndex = 0;
					for(final SignalConcentration copySignal : copySignals)
					{
						dimensionNames[dimensionNamesIndex++] = String.valueOf(copySignal.getId());
					}
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
		} while( RANDOM.nextFloat() < (float) 0.1 );
		return copy;
	}

	/**
	 * Mutates by incorperating a new signal into the mutated result.<br/> <br/>
	 * May mutate by:<br/> <ul> <li>adding the new signal</li> <ul>
	 *
	 * @param newSignal The new signal to incorperate.
	 * @return New mutated wavelet
	 */
	public SignalProcessingWavelet mutate(final double deviation, final SignalConcentration newSignal)
	{
		if( this.signals.contains(newSignal) )
			return this.mutate(deviation);
		final SignalProcessingWavelet copy = this.clone();
		copy.signals.add(newSignal);
		if( copy.signals.size() > this.signals.size() )
		{
			copy.waves.clear();
			for(final WaveMultidimensionalFunction wave : this.waves)
			{
				final String[] names = new String[wave.getDimensionNames().length + 1];
				int index = 0;
				for(final String dimensionName : wave.getDimensionNames())
					names[index++] = dimensionName;
				names[index] = String.valueOf(newSignal.getId());
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
		copy.id = RANDOM.nextLong();
		return copy.mutate(1.0);
	}
	/*
		 * Mutates by incorperating the wave.<br/>
		 * <br/>
		 * May mutate by:<br/>
		 * <ul>
		 * <li>add the wave with the current newSignals as its dimensions.</li>
		 * </ul>
		 *
		 * @param wave wave to incorperate
		 * @return New mutated wavelet
		 */
	/*
		public SignalProcessingWavelet mutate(WaveMultidimensionalFunction wave)
		{
			String[] dimensionNames = new String[this.newSignals.size()];
			int index = 0;
			for(Signal dimension:this.newSignals)
			{
				dimensionNames[index++] = dimension.getId().toString();
			}

			WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(dimensionNames);
			newWave.setAmplitude(wave.getAmplitude());
			newWave.setDistribution(wave.getDistribution());
			newWave.setForm(wave.getForm());
			newWave.setFrequency(wave.getFrequency());
			newWave.setPhase(wave.getPhase());
			for(String dimension:wave.getDimensionNames())
			{
				newWave.setCenter(dimension, wave.getCenter(dimension));
				newWave.setDimension(dimension, wave.getDimension(dimension));
			}

			SignalProcessingWavelet copy = this.clone();
			copy.waves.add(newWave);
			return copy.mutate();
		}*/

	private SignalConcentration getRandomSignal()
	{
		final SignalConcentration[] signalsArray = new SignalConcentration[this.signals.size()];
		this.signals.toArray(signalsArray);
		return signalsArray[RANDOM.nextInt(signalsArray.length)];
	}

	private WaveMultidimensionalFunction generateNewWave()
	{
		final String[] dimensionNames = new String[this.signals.size()];
		int index = 0;
		for(final SignalConcentration dimension : this.signals)
		{
			dimensionNames[index++] = String.valueOf(dimension.getId());
		}
		final WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(dimensionNames);
		newWave.setFrequency(RANDOM.nextGaussian() * 0.001);
		newWave.setPhase(RANDOM.nextGaussian() * 10);
		newWave.setAmplitude(RANDOM.nextGaussian());
		newWave.setForm(Math.abs(RANDOM.nextGaussian()));
		if( newWave.getForm() <= 0.0 )
		{
			newWave.setForm(newWave.getForm() + ((1 + RANDOM.nextGaussian()) * 10));
		}
		for(final String dimensionName : dimensionNames)
		{
			newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((RANDOM.nextFloat() * 2 - 1) * 100));
		}
		newWave.setDistribution((RANDOM.nextDouble() * 100.0) + Double.MIN_VALUE);
		return newWave;
	}

	private WaveMultidimensionalFunction generateRandomWave(final double deviation)
	{
		if( !this.waves.isEmpty() )
		{
			final WaveMultidimensionalFunction[] wavesArray = new WaveMultidimensionalFunction[this.waves.size()];
			this.waves.toArray(wavesArray);
			final WaveMultidimensionalFunction randomWave = wavesArray[RANDOM.nextInt(wavesArray.length)];
			final WaveMultidimensionalFunction newWave = randomWave.clone();
			// TODO clean this up
			if( RANDOM.nextDouble() <= 1.0 )
			{
				newWave.setFrequency(newWave.getFrequency() + ((RANDOM.nextFloat() * 2.0 - 1.0) * deviation * 0.01));
			}
			if( RANDOM.nextDouble() <= 1.0 )
			{
				newWave.setPhase(newWave.getPhase() + ((RANDOM.nextFloat() * 2.0 - 1.0) * deviation * 10.0));
			}
			if( RANDOM.nextDouble() <= 1.0 )
			{
				newWave.setAmplitude(newWave.getAmplitude() + ((RANDOM.nextFloat() * 2.0 - 1.0) * deviation * 10.0));
			}
			if( RANDOM.nextDouble() <= 1.0 )
			{
				newWave.setForm(newWave.getForm() + (RANDOM.nextFloat() * deviation * 0.01));
			}
			if( RANDOM.nextDouble() <= 1.0 )
			{
				newWave.setDistribution(newWave.getDistribution() + ((RANDOM.nextFloat() * 2.0 - 1.0) * deviation * 100.0));
			}
			if( RANDOM.nextDouble() <= 1.0 )
			{
				final String[] dimensionNames = newWave.getDimensionNames();
				for(final String dimensionName : dimensionNames)
				{
					newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((RANDOM.nextFloat() * 2.0 - 1.0) * deviation * 100.0));
				}
			}
			return newWave;
		}
		return this.generateNewWave();
	}

	@Override
	public String toString()
	{
		this.reconstructWavelet();
		return this.wavelet.toString();
	}
}
