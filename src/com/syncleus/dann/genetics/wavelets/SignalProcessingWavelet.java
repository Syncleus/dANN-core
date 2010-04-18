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

import com.syncleus.dann.math.wave.WaveMultidimensionalFunction;
import com.syncleus.dann.math.wave.wavelet.CombinedWaveletFunction;
import java.io.Serializable;
import java.util.*;

public class SignalProcessingWavelet implements Comparable<SignalProcessingWavelet>, Cloneable
{
	// <editor-fold defaultstate="collapsed" desc="internal class">
	public static abstract class SignalConcentration implements Comparable<SignalConcentration>
	{
		private double value = 0.0;
		private long id = RANDOM.nextLong();

		public SignalConcentration()
		{
		}

		protected SignalConcentration(SignalConcentration originalSignal)
		{
			value = originalSignal.value;
			id = originalSignal.id;
		}

		public double add(double addValue)
		{
			this.value += addValue;
			return this.value;
		}

		public double getValue()
		{
			return this.value;
		}

		public void setValue(double newValue)
		{
			this.value = newValue;
		}

		public long getId()
		{
			return this.id;
		}

		public int compareTo(SignalConcentration compareWith)
		{
			return (this.id < compareWith.id ? -1 : (this.id > compareWith.id ? 1 : 0));
		}

		@Override
		public int hashCode()
		{
			return (int) this.id;
		}

		@Override
		public boolean equals(Object compareWith)
		{
			if(compareWith instanceof SignalConcentration)
			{
				SignalConcentration signalCompare = (SignalConcentration) compareWith;
				if(signalCompare.id == this.id)
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

		protected GlobalSignalConcentration(GlobalSignalConcentration originalSignal)
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
    private ArrayList<WaveMultidimensionalFunction> waves = new ArrayList<WaveMultidimensionalFunction>();
    private CombinedWaveletFunction wavelet;



    public SignalProcessingWavelet(SignalConcentration initialInput, SignalConcentration initialOutput)
    {
        this.output = initialOutput;

        this.signals.add(initialInput);

        WaveMultidimensionalFunction initialWave = generateNewWave();
        this.waves.add(initialWave);

//        this.cell = cell;

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
        TreeSet<SignalConcentration> copy = new TreeSet<SignalConcentration>(this.signals);
        copy.add(this.output);
        return copy;
    }



    public int compareTo(SignalProcessingWavelet compareWith)
    {
//        return compareWith.id.compareTo(this.id);
		if(this.id < compareWith.id)
			return -1;
		else if(this.id > compareWith.id)
			return 1;
		return 0;
    }

	@Override
	public boolean equals(Object compareWithObject)
	{
//		return this.id.equals(compareWithObject);
		if( compareWithObject instanceof SignalProcessingWavelet)
			return (this.id == ((SignalProcessingWavelet)compareWithObject).id);
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

        for(SignalConcentration signal:this.signals)
        {
            this.wavelet.setParameter(this.wavelet.getParameterNameIndex(signal.getId() + ""), signal.getValue());
        }

        double newOutput = this.wavelet.calculate();

        //this.output.setValue(this.output.getValue() + newOutput);
        // this.output.setValue(this.output.getValue() + (-1 * this.currentOutput) + newOutput);
        this.currentOutput = newOutput;
    }



    public void tick()
    {
        this.output.setValue(this.output.getValue() + currentOutput);
    }



	@Override
    public SignalProcessingWavelet clone() throws CloneNotSupportedException
    {
        SignalProcessingWavelet copy = (SignalProcessingWavelet) super.clone();
        copy.signals = new TreeSet<SignalConcentration>(this.signals);
        copy.waves = new ArrayList<WaveMultidimensionalFunction>(this.waves);

        copy.output = this.output;
        copy.wavelet = (this.wavelet == null ? null : this.wavelet.clone());
        copy.id = this.id;
        return copy;
    }



    private void reconstructWavelet()
    {
        String[] signalNames = new String[this.signals.size()];
        //System.out.println("dimensions size: " + this.dimensions.size());
        int signalNamesIndex = 0;
        for(SignalConcentration dimension:this.signals)
        {
            signalNames[signalNamesIndex++] = dimension.getId() + "";
        }

        this.wavelet = new CombinedWaveletFunction(signalNames);

        for(WaveMultidimensionalFunction wave:this.waves)
        {
            this.wavelet.addWave(wave);
        }
    }



    /**
     * Internally mutates.<br/>
     * <br/>
     * may change in any of the following ways:<br/>
     * <ul>
     * <li>add a new bound wave</li>
     * <li>copy an existing wave and mutates it adding the new mutated wave</li>
     * <li>delete an existing wave</li>
     * <li>removing a signal</li>
     * <li>Do nothing</li>
     * </ul>
     * 
     * @return New mutated wavelet
     */
    public SignalProcessingWavelet mutate(double deviation) throws CloneNotSupportedException
    {
        SignalProcessingWavelet copy = this.clone();

        while(RANDOM.nextFloat() < 0.1)
        {

            //add a mutated copy of an existing wave
            if(RANDOM.nextDouble() < 0.1)
            {
                //Signal newSignal = this.getRandomSignal();
                //return this.mutate(newSignal);
                copy.waves.add(this.generateRandomWave());
                copy.id = RANDOM.nextLong();
            }
            //make a RANDOM new wave
            if(RANDOM.nextDouble() < 0.1)
            {
                copy.waves.add(this.generateNewWave());
                copy.id = RANDOM.nextLong();
            }
            //delete a RANDOM wave
            if(RANDOM.nextDouble() < 0.1)
            {
                //only delete if there will be atleast one wave left
                if(copy.waves.size() > 1)
                {
                    WaveMultidimensionalFunction deleteWave = copy.waves.get(this.RANDOM.nextInt(copy.waves.size()));
                    copy.waves.remove(deleteWave);
                }
            }
            //delete a signal
            if(RANDOM.nextDouble() < 0.1)
            {
                //only delet eif there will be atleast one signal left
                if(copy.signals.size() > 1)
                {
                    SignalConcentration[] signals = new SignalConcentration[copy.signals.size()];
                    copy.signals.toArray(signals);

                    SignalConcentration deleteSignal = signals[RANDOM.nextInt(signals.length)];
                    copy.signals.remove(deleteSignal);

                    SignalConcentration[] copySignals = new SignalConcentration[copy.signals.size()];
                    copy.signals.toArray(copySignals);

                    String[] dimensionNames = new String[copySignals.length];
                    int dimensionNamesIndex = 0;
                    for(SignalConcentration copySignal:copySignals)
                    {
                        dimensionNames[dimensionNamesIndex++] = copySignal.getId() + "";
                    }

                    copy.waves.clear();
                    for(WaveMultidimensionalFunction wave:this.waves)
                    {
                        WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(dimensionNames);
                        newWave.setAmplitude(wave.getAmplitude());
                        newWave.setDistribution(wave.getDistribution());
                        newWave.setForm(wave.getForm());
                        newWave.setFrequency(wave.getFrequency());
                        newWave.setPhase(wave.getPhase());
                        for(String dimension:dimensionNames)
                        {
                            newWave.setCenter(dimension, wave.getCenter(dimension));
                            newWave.setDimension(dimension, wave.getDimension(dimension));
                        }

                        copy.waves.add(newWave);
                    }
                }
            }
        }

        return copy;
    }



    /**
     * Mutates by incorperating a new signal into the mutated result.<br/>
     * <br/>
     * May mutate by:<br/>
     * <ul>
     * <li>adding the new signal</li>
     * <ul>
     * @param newSignal The new signal to incorperate.
     * @return New mutated wavelet
     */
    public SignalProcessingWavelet mutate(double deviation, SignalConcentration newSignal) throws CloneNotSupportedException
    {
        SignalProcessingWavelet copy = this.clone();
        copy.signals.add(newSignal);
        if(copy.signals.size() > this.signals.size())
        {
            copy.waves.clear();
            for(WaveMultidimensionalFunction wave:this.waves)
            {
                String[] names = new String[wave.getDimensionNames().length + 1];
                int index = 0;
                for(String dimensionName:wave.getDimensionNames())
                    names[index++] = dimensionName;
                names[index++] = newSignal.getId() + "";

                WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(names);
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
     * <li>add the wave with the current signals as its dimensions.</li>
     * </ul>
     * 
     * @param wave wave to incorperate
     * @return New mutated wavelet
     */
    /*
    public SignalProcessingWavelet mutate(WaveMultidimensionalFunction wave)
    {
        String[] dimensionNames = new String[this.signals.size()];
        int index = 0;
        for(Signal dimension:this.signals)
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

        SignalConcentration[] signalsArray = new SignalConcentration[this.signals.size()];
        this.signals.toArray(signalsArray);
        SignalConcentration randomSignal = signalsArray[RANDOM.nextInt(signalsArray.length)];

        return randomSignal;
    }



    private WaveMultidimensionalFunction generateNewWave()
    {
        String[] dimensionNames = new String[this.signals.size()];
        int index = 0;
        for(SignalConcentration dimension:this.signals)
        {
            dimensionNames[index++] = dimension.getId() + "";
        }
        WaveMultidimensionalFunction newWave = new WaveMultidimensionalFunction(dimensionNames);

        newWave.setFrequency(RANDOM.nextGaussian() * 0.001);
        newWave.setPhase(RANDOM.nextGaussian() * 10);
        newWave.setAmplitude(RANDOM.nextGaussian());
        newWave.setForm(Math.abs(RANDOM.nextGaussian()));
        if(newWave.getForm() <= 0.0)
        {
            newWave.setForm(newWave.getForm() + ((1 + RANDOM.nextGaussian()) * 10));
        }

        for(String dimensionName:dimensionNames)
        {
            newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((RANDOM.nextFloat() * 2 - 1) * 100));
        }
        newWave.setDistribution((RANDOM.nextDouble() * 100.0) + Double.MIN_VALUE);

        return newWave;
    }



    private WaveMultidimensionalFunction generateRandomWave()
    {
        if(this.waves.size() > 0)
        {
            WaveMultidimensionalFunction[] wavesArray = new WaveMultidimensionalFunction[this.waves.size()];
            this.waves.toArray(wavesArray);
            WaveMultidimensionalFunction randomWave = wavesArray[RANDOM.nextInt(wavesArray.length)];
			WaveMultidimensionalFunction newWave = randomWave.clone();

            if(RANDOM.nextDouble() <= 1.0)
            {
                newWave.setFrequency(newWave.getFrequency() + ((RANDOM.nextFloat() * 2 - 1) * 0.01));
            }
            if(RANDOM.nextDouble() <= 1.0)
            {
                newWave.setPhase(newWave.getPhase() + ((RANDOM.nextFloat() * 2 - 1) * 10));
            }
            if(RANDOM.nextDouble() <= 1.0)
            {
                newWave.setAmplitude(newWave.getAmplitude() + ((RANDOM.nextFloat() * 2 - 1) * 10));
            }
            if(RANDOM.nextDouble() <= 1.0)
            {
                newWave.setForm(newWave.getForm() + (RANDOM.nextFloat() * 0.01));
            }
            if(RANDOM.nextDouble() <= 1.0)
            {
                newWave.setDistribution(newWave.getDistribution() + ((RANDOM.nextFloat() * 2 - 1) * 100));
            }
            if(RANDOM.nextDouble() <= 1.0)
            {
                String[] dimensionNames = newWave.getDimensionNames();
                for(String dimensionName:dimensionNames)
                {
                    newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((RANDOM.nextFloat() * 2 - 1) * 100));
                }
            }

            return newWave;
        }

        return this.generateNewWave();
    }



    public String toString()
    {
        this.reconstructWavelet();

        return this.wavelet.toString();
    }
}
