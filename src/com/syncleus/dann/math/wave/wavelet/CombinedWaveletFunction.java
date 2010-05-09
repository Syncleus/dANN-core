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
package com.syncleus.dann.math.wave.wavelet;


import com.syncleus.dann.math.wave.*;
import com.syncleus.dann.math.*;
import java.util.*;

public class CombinedWaveletFunction extends AbstractFunction implements Cloneable
{
    private Set<String> dimensions = new TreeSet<String>();
    private List<WaveMultidimensionalFunction> waves = Collections.synchronizedList(new ArrayList<WaveMultidimensionalFunction>());

    public CombinedWaveletFunction(final String[] dimensions)
    {
        super(dimensions);
        this.dimensions.addAll(Arrays.asList(dimensions));
    }
    
    
    public int  getWaveCount()
    {
        return this.waves.size();
    }



    public TreeSet<String> getDimensions()
    {
        return new TreeSet<String>(this.dimensions);
    }



    public void setDimension(final String dimension, final double value)
    {
        this.setParameter(this.getParameterNameIndex(dimension), value);
    }



    public double getDimension(final String dimension)
    {
        return this.getParameter(this.getParameterNameIndex(dimension));
    }

    public void addWave(final WaveMultidimensionalFunction wave)
    {
        this.waves.add(wave);
    }



    public double calculate()
    {
        double waveTotal = 0.0;
        for(final WaveMultidimensionalFunction currentWave:this.waves)
        {
            for(final String dimension:this.dimensions)
            {
				currentWave.setDimension(dimension, this.getDimension(dimension));
            }

            waveTotal += currentWave.calculate();
        }

        return waveTotal;
    }



	@Override
    public CombinedWaveletFunction clone()
    {
        final CombinedWaveletFunction copy = (CombinedWaveletFunction) super.clone();
		copy.dimensions = new TreeSet<String>(this.dimensions);
		copy.waves = new ArrayList<WaveMultidimensionalFunction>(this.waves);
        return copy;
    }



    public String toString()
    {
        final StringBuilder equationBuffer = new StringBuilder();
        final WaveMultidimensionalFunction[] waveArray = new WaveMultidimensionalFunction[this.waves.size()];
        this.waves.toArray(waveArray);
        for(int waveArrayIndex = 0; waveArrayIndex < waveArray.length; waveArrayIndex++)
        {
            final WaveMultidimensionalFunction currentWave = waveArray[waveArrayIndex];
            if( waveArrayIndex > 0 )
                equationBuffer.append(" + ");
            equationBuffer.append(currentWave.toString());
        }

        return equationBuffer.toString();
    }
}
