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
import java.util.*;

public class ExpressionFunction
{
    private static Random random = new Random();
    HashSet<ReceptorKey> receptors;
    ArrayList<WaveMultidimensionalMathFunction> waves;
    WaveletMathFunction wavelet;



	public ExpressionFunction(ExpressionFunction copy)
	{
		this.receptors = new HashSet<ReceptorKey>(copy.receptors);
		this.waves = new ArrayList<WaveMultidimensionalMathFunction>(copy.waves);
		this.wavelet = copy.wavelet;
	}

    public ExpressionFunction(ReceptorKey initialInput)
    {
		this.waves = new ArrayList<WaveMultidimensionalMathFunction>();
		this.receptors = new HashSet<ReceptorKey>();

        this.receptors.add(initialInput);

        WaveMultidimensionalMathFunction initialWave = generateNewWave();
        this.waves.add(initialWave);
    }

    private ExpressionFunction()
    {
    }

	public WaveletMathFunction getWaveletMathFunction()
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



    public WaveletMathFunction getWavelet()
    {
        this.reconstructWavelet();
        return this.wavelet.clone();
    }


	public boolean receives(SignalKey signal)
	{
		for(ReceptorKey receptor : this.receptors)
		{
			if( receptor.binds(signal) )
				return true;
		}

		return false;
	}


	public double calculate(Set<SignalKeyConcentration> signalConcentrations)
	{
        this.reconstructWavelet();

		for(ReceptorKey receptor : this.receptors)
		{
			double concentration = 0.0;
			//calculate concentration for the current receptor
			for(SignalKeyConcentration signalConcentration : signalConcentrations)
			{
				if( receptor.binds(signalConcentration.getSignal()))
					concentration += signalConcentration.getConcentration();
			}

			this.wavelet.setParameter(String.valueOf(receptor.hashCode()), concentration);
		}

        return this.wavelet.calculate();
	}



	@Override
    public ExpressionFunction clone()
    {
        return new ExpressionFunction(this);
    }



    private void reconstructWavelet()
    {
        String[] receptorNames = new String[this.receptors.size()];

        int receptorNamesIndex = 0;
        for(ReceptorKey receptor : this.receptors)
            receptorNames[receptorNamesIndex++] = String.valueOf(receptor.hashCode());

        this.wavelet = new WaveletMathFunction(receptorNames);

        for(WaveMultidimensionalMathFunction wave : this.waves)
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
    public ExpressionFunction mutate(double deviation)
    {
        ExpressionFunction copy = this.clone();

        while(random.nextFloat() < 0.1)
        {

            //add a mutated copy of an existing wave
            if(random.nextDouble() < 0.1)
            {
                //Signal newSignal = this.getRandomSignal();
                //return this.mutate(newSignal);
                copy.waves.add(this.generateRandomWave());
            }
            //make a random new wave
            if(random.nextDouble() < 0.1)
            {
                copy.waves.add(this.generateNewWave());
            }
            //delete a random wave
            if(random.nextDouble() < 0.1)
            {
                //only delete if there will be atleast one wave left
                if(this.waves.size() > 1)
                {
                    WaveMultidimensionalMathFunction deleteWave = copy.waves.get(random.nextInt(copy.waves.size()));
                    copy.waves.remove(deleteWave);
                }
            }
            //delete a signal
            if(random.nextDouble() < 0.1)
            {
                //only delet eif there will be atleast one signal left
                if(this.receptors.size() > 1)
                {
                    ReceptorKey[] receptorArray = new ReceptorKey[copy.receptors.size()];
                    copy.receptors.toArray(receptorArray);

                    ReceptorKey deleteReceptor = receptorArray[random.nextInt(receptorArray.length)];
                    copy.receptors.remove(deleteReceptor);

                    ReceptorKey[] copyReceptors = new ReceptorKey[copy.receptors.size()];
                    copy.receptors.toArray(copyReceptors);

                    String[] dimensionNames = new String[copyReceptors.length];
                    int dimensionNamesIndex = 0;
                    for(ReceptorKey copyReceptor : copyReceptors)
                    {
                        dimensionNames[dimensionNamesIndex++] = String.valueOf(copyReceptor.hashCode());
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
    public ExpressionFunction mutate(double deviation, ReceptorKey newReceptor)
    {
        ExpressionFunction copy = this.clone();

        copy.receptors.add(newReceptor);
        if(copy.receptors.size() > this.receptors.size())
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
                names[index++] = String.valueOf(newReceptor.hashCode());

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

        return copy.mutate(1.0);
    }



    private WaveMultidimensionalMathFunction generateNewWave()
    {
        String[] dimensionNames = new String[this.receptors.size()];
        int index = 0;
        for(ReceptorKey receptor : this.receptors)
            dimensionNames[index++] = String.valueOf(receptor.hashCode());
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



	@Override
    public String toString()
    {
        this.reconstructWavelet();

        return this.wavelet.toString();
    }
}
