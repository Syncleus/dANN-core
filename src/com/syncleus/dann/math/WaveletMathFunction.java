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
package com.syncleus.dann.math;


import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class WaveletMathFunction extends AbstractMathFunction implements Cloneable
{
    private TreeSet<String> dimensions = new TreeSet<String>();
    private List<WaveMultidimensionalMathFunction> waves = Collections.synchronizedList(new ArrayList<WaveMultidimensionalMathFunction>());
    //private Hashtable<DistributedFormedWaveMathFunction, String[]> waves = new Hashtable<DistributedFormedWaveMathFunction, String>();
    //private Hashtable<String, WaveMultidimensionalMathFunction[]> waves = new Hashtable<String, WaveMultidimensionalMathFunction[]>();

    public WaveletMathFunction(String[] dimensions)
    {
        super(dimensions);
        for(String dimension:dimensions)
        {
            this.dimensions.add(dimension);
        }
    }
    
    
    public int  getWaveCount()
    {
        return this.waves.size();
    }



    public TreeSet<String> getDimensions()
    {
        return new TreeSet<String>(this.dimensions);
    }



    public void setDimension(String dimension, double value)
    {
        this.setParameter(this.getParameterNameIndex(dimension), value);
    }



    public double getDimension(String dimension)
    {
        return this.getParameter(this.getParameterNameIndex(dimension));
    }

    /*
    private boolean checkWave(WaveMultidimensionalMathFunction wave)
    {
    String[] waveDimensions = wave.getDimensionNames();
    for(String waveDimension : waveDimensions)
    if( this.dimensions.contains(waveDimension) == false )
    return false;
    return true;
    }*/

    /*
    private void addToWaveArray(WaveDimension newWave)
    {
    WaveMultidimensionalMathFunction[] currentWaves = this.waves.get(newWave.getDimension());
    WaveMultidimensionalMathFunction[] newWaves;
    if( currentWaves != null)
    {
    newWaves = new WaveMultidimensionalMathFunction[currentWaves.length + 1];
    System.arraycopy(newWaves, 0, currentWaves, 0, currentWaves.length);
    }
    else
    newWaves = new WaveMultidimensionalMathFunction[1];
    newWaves[newWaves.length - 1] = newWave.getWave();
    this.waves.put(newWave.getDimension(), newWaves);
    }*/

    public void addWave(WaveMultidimensionalMathFunction wave)
    {
        //if( checkWave(wave) == false )
        //    throw new InvalidParameterException("dimensions dont match");

        this.waves.add(wave);
    }



    public double calculate()
    {
        double waveTotal = 0.0;
        for(WaveMultidimensionalMathFunction currentWave:this.waves)
        {
            for(String dimension:this.dimensions)
            {
                try
                {
                    currentWave.setDimension(dimension, this.getDimension(dimension));
                }
                catch(InvalidParameterException caughtException)
                {
                }
            }

            waveTotal += currentWave.calculate();
        }

        return waveTotal;
    /*
    double result = 0.0;
    for(int index = 0; index < this.waves.size(); index++)
    {
    double waveProduct = 1.0;
    double waveTotal = 0.0;
    for(String dimension : this.dimensions)
    {
    WaveMultidimensionalMathFunction wave = this.waves.get(dimension)[index];
    //System.out.println("wave: " + wave);
    if( wave != null )
    {
    wave.setX(this.getParameter(this.getParameterNameIndex(dimension)));
    waveProduct *= wave.calculate();
    waveTotal += wave.calculate();
    }
    }
    if(waveTotal != 0.0 )
    result += waveProduct/waveTotal;
    }
    return result;*/

    /*
    double waveTotal = 0.0;
    for( DistributedFormedWaveMathFunction wave : this.waves.keySet())
    {
    String currentWaveDimension = this.waves.get(wave);
    int currentWaveDimensionIndex = this.getParameterNameIndex(currentWaveDimension);
    double dimensionValue = this.getParameter(currentWaveDimensionIndex);
    wave.setX(dimensionValue);
    waveTotal += wave.calculate();
    }
    return waveTotal;
     */
    }



	@Override
    public WaveletMathFunction clone() throws CloneNotSupportedException
    {
        WaveletMathFunction copy = (WaveletMathFunction) super.clone();

        copy.waves.addAll(this.waves);
		copy.dimensions.addAll(this.dimensions);

        return copy;
    }



    public String toString()
    {
		StringBuffer equationBuffer =new StringBuffer();
        WaveMultidimensionalMathFunction[] waveArray = new WaveMultidimensionalMathFunction[this.waves.size()];
        this.waves.toArray(waveArray);
        for(int waveArrayIndex = 0; waveArrayIndex < waveArray.length; waveArrayIndex++)
        //for(WaveMultidimensionalMathFunction currentWave : this.waves)
        {
            WaveMultidimensionalMathFunction currentWave = waveArray[waveArrayIndex];
            if( waveArrayIndex > 0 )
                equationBuffer.append(" + ");
            equationBuffer.append(currentWave.toString());
        }

        return equationBuffer.toString();
    }
}
