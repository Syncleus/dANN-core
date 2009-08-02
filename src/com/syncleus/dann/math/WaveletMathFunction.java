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

    public void addWave(WaveMultidimensionalMathFunction wave)
    {
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
    }



	@Override
    public WaveletMathFunction clone()
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
        {
            WaveMultidimensionalMathFunction currentWave = waveArray[waveArrayIndex];
            if( waveArrayIndex > 0 )
                equationBuffer.append(" + ");
            equationBuffer.append(currentWave.toString());
        }

        return equationBuffer.toString();
    }
}
