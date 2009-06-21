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

import com.syncleus.dann.math.*;
import com.syncleus.dann.util.UniqueId;
import java.util.*;

public class SignalProcessingWavelet implements SignalMutable, Comparable<SignalProcessingWavelet>, Cloneable
{
    private UniqueId id = null;
    private SignalConcentration output;
    private double currentOutput = 0.0;
    private static Random random = new Random();
    TreeSet<SignalConcentration> signals = new TreeSet<SignalConcentration>();
    ArrayList<WaveMultidimensionalMathFunction> waves = new ArrayList<WaveMultidimensionalMathFunction>();
    WaveletMathFunction wavelet;
//    private Cell cell = null;



    public SignalProcessingWavelet(/*Cell cell,*/ SignalConcentration initialInput, SignalConcentration initialOutput)
    {
        this.output = initialOutput;

        this.signals.add(initialInput);

        WaveMultidimensionalMathFunction initialWave = generateNewWave();
        this.waves.add(initialWave);

//        this.cell = cell;

        this.id = new UniqueId(64);
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
        return compareWith.id.compareTo(this.id);
    }



    public WaveletMathFunction getWavelet()
    {
        this.reconstructWavelet();
        return this.wavelet.clone();
    }



    public void preTick()
    {
        this.reconstructWavelet();

        for(SignalConcentration signal:this.signals)
        {
            this.wavelet.setParameter(this.wavelet.getParameterNameIndex(signal.getId().toString()), signal.getValue());
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



    public SignalProcessingWavelet clone()
    {
        SignalProcessingWavelet copy = new SignalProcessingWavelet();
        copy.signals = new TreeSet<SignalConcentration>(this.signals);
        copy.waves = new ArrayList<WaveMultidimensionalMathFunction>(this.waves);

        copy.output = this.output;
        copy.wavelet = (this.wavelet == null ? null : this.wavelet.clone());
        copy.id = this.id;
        return copy;
    }


/*
    public SignalProcessingWavelet clone(Cell cell)
    {
        SignalProcessingWavelet newWavelet = this.clone();
        newWavelet.setCell(cell);

        return newWavelet;
    }
*/

/*
    void setCell(Cell cell)
    {
        this.cell = cell;

        this.output = this.cell.updateSignal(this.output);
        TreeSet<Signal> newSignals = new TreeSet<Signal>();
        for(Signal dimension:this.signals)
        {
            newSignals.add(this.cell.updateSignal(dimension));
        }
        this.signals = newSignals;
    }*/



    private void reconstructWavelet()
    {
        String[] signalNames = new String[this.signals.size()];
        //System.out.println("dimensions size: " + this.dimensions.size());
        int signalNamesIndex = 0;
        for(SignalConcentration dimension:this.signals)
        {
            signalNames[signalNamesIndex++] = dimension.getId().toString();
        }

        this.wavelet = new WaveletMathFunction(signalNames);

        for(WaveMultidimensionalMathFunction wave:this.waves)
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
    public SignalProcessingWavelet mutate(double deviation)
    {
        SignalProcessingWavelet copy = this.clone();

        while(random.nextFloat() < 0.1)
        {

            //add a mutated copy of an existing wave
            if(random.nextDouble() < 0.1)
            {
                //Signal newSignal = this.getRandomSignal();
                //return this.mutate(newSignal);
                copy.waves.add(this.generateRandomWave());
                copy.id = new UniqueId(64);
            }
            //make a random new wave
            if(random.nextDouble() < 0.1)
            {
                copy.waves.add(this.generateNewWave());
                copy.id = new UniqueId(64);
            }
            //delete a random wave
            if(random.nextDouble() < 0.1)
            {
                //only delete if there will be atleast one wave left
                if(this.waves.size() > 1)
                {
                    WaveMultidimensionalMathFunction deleteWave = copy.waves.get(this.random.nextInt(copy.waves.size()));
                    copy.waves.remove(deleteWave);
                }
            }
            //delete a signal
            if(random.nextDouble() < 0.1)
            {
                //only delet eif there will be atleast one signal left
                if(this.signals.size() > 1)
                {
                    SignalConcentration[] signals = new SignalConcentration[copy.signals.size()];
                    copy.signals.toArray(signals);

                    SignalConcentration deleteSignal = signals[random.nextInt(signals.length)];
                    copy.signals.remove(deleteSignal);

                    SignalConcentration[] copySignals = new SignalConcentration[copy.signals.size()];
                    copy.signals.toArray(copySignals);

                    String[] dimensionNames = new String[copySignals.length];
                    int dimensionNamesIndex = 0;
                    for(SignalConcentration copySignal:copySignals)
                    {
                        dimensionNames[dimensionNamesIndex++] = copySignal.getId().toString();
                    }

                    copy.waves.clear();
                    for(WaveMultidimensionalMathFunction wave:this.waves)
                    {
                        WaveMultidimensionalMathFunction newWave = new WaveMultidimensionalMathFunction(dimensionNames);
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
    public SignalProcessingWavelet mutate(double deviation, SignalConcentration newSignal)
    {
        SignalProcessingWavelet copy = this.clone();
        copy.signals.add(newSignal);
        if(copy.signals.size() > this.signals.size())
        {
            copy.waves.clear();
            for(WaveMultidimensionalMathFunction wave:this.waves)
            {
                String[] names = new String[wave.getDimensionNames().length + 1];
                int index = 0;
                for(String dimensionName:wave.getDimensionNames())
                {
                    names[index++] = dimensionName;
                }
                names[index++] = newSignal.getId().toString();

                WaveMultidimensionalMathFunction newWave = new WaveMultidimensionalMathFunction(names);
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

        copy.id = new UniqueId(64);
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
    public SignalProcessingWavelet mutate(WaveMultidimensionalMathFunction wave)
    {
        String[] dimensionNames = new String[this.signals.size()];
        int index = 0;
        for(Signal dimension:this.signals)
        {
            dimensionNames[index++] = dimension.getId().toString();
        }

        WaveMultidimensionalMathFunction newWave = new WaveMultidimensionalMathFunction(dimensionNames);
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
        SignalConcentration randomSignal = signalsArray[random.nextInt(signalsArray.length)];

        return randomSignal;
    }



    private WaveMultidimensionalMathFunction generateNewWave()
    {
        String[] dimensionNames = new String[this.signals.size()];
        int index = 0;
        for(SignalConcentration dimension:this.signals)
        {
            dimensionNames[index++] = dimension.getId().toString();
        }
        WaveMultidimensionalMathFunction newWave = new WaveMultidimensionalMathFunction(dimensionNames);

        newWave.setFrequency(random.nextGaussian() * 0.001);
        newWave.setPhase(random.nextGaussian() * 10);
        newWave.setAmplitude(random.nextGaussian());
        newWave.setForm(Math.abs(random.nextGaussian()));
        if(newWave.getForm() <= 0.0)
        {
            newWave.setForm(newWave.getForm() + ((1 + random.nextGaussian()) * 10));
        }

        for(String dimensionName:dimensionNames)
        {
            newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((random.nextFloat() * 2 - 1) * 100));
        }
        newWave.setDistribution(random.nextFloat() * 100);

        return newWave;
    }



    private WaveMultidimensionalMathFunction generateRandomWave()
    {
        if(this.waves.size() > 0)
        {
            WaveMultidimensionalMathFunction[] wavesArray = new WaveMultidimensionalMathFunction[this.waves.size()];
            this.waves.toArray(wavesArray);
            WaveMultidimensionalMathFunction randomWave = wavesArray[random.nextInt(wavesArray.length)];
            WaveMultidimensionalMathFunction newWave = randomWave.clone();

            if(random.nextDouble() <= 1.0)
            {
                newWave.setFrequency(newWave.getFrequency() + ((random.nextFloat() * 2 - 1) * 0.01));
            }
            if(random.nextDouble() <= 1.0)
            {
                newWave.setPhase(newWave.getPhase() + ((random.nextFloat() * 2 - 1) * 10));
            }
            if(random.nextDouble() <= 1.0)
            {
                newWave.setAmplitude(newWave.getAmplitude() + ((random.nextFloat() * 2 - 1) * 10));
            }
            if(random.nextDouble() <= 1.0)
            {
                newWave.setForm(newWave.getForm() + (random.nextFloat() * 0.01));
            }
            if(random.nextDouble() <= 1.0)
            {
                newWave.setDistribution(newWave.getDistribution() + ((random.nextFloat() * 2 - 1) * 100));
            }
            if(random.nextDouble() <= 1.0)
            {
                String[] dimensionNames = newWave.getDimensionNames();
                for(String dimensionName:dimensionNames)
                {
                    newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((random.nextFloat() * 2 - 1) * 100));
                }
            }

            return newWave;
        }

        return null;
    }



    public String toString()
    {
        this.reconstructWavelet();

        return this.wavelet.toString();
    }
}
