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
package com.syncleus.dann.math.wave;
import com.syncleus.dann.math.wave.wavelet.ShapenedWaveletMathFunction;
import com.syncleus.dann.math.*;
public class WaveMultidimensionalMathFunction extends AbstractMathFunction implements Cloneable
{
    private boolean constantMode = false;
    private double constantValue;
    private String[] dimensionNames = null;
    private ShapenedWaveletMathFunction wave = new ShapenedWaveletMathFunction();

	public WaveMultidimensionalMathFunction(WaveMultidimensionalMathFunction copy)
	{
		super(copy);

		copy.dimensionNames = this.dimensionNames.clone();
        copy.constantMode = this.constantMode;
        copy.constantValue = this.constantValue;

		
        this.setFrequency(copy.getFrequency());
        this.setPhase(copy.getPhase());
        this.setAmplitude(copy.getAmplitude());
        this.setForm(copy.getForm());
        this.setDistribution(copy.getDistribution());
        for(String dimensionName : copy.dimensionNames )
        {
            final double dimensionValue = copy.getDimension(dimensionName);
            final double centerValue = copy.getCenter(dimensionName);
            this.setDimension(dimensionName, dimensionValue);
            this.setCenter(dimensionName, centerValue);
        }
        this.constantMode = copy.constantMode;
        this.constantValue = copy.constantValue;
	}
    
    public WaveMultidimensionalMathFunction(double constantValue)
    {
        super(new String[]{});
        
        this.constantMode = true;
        this.constantValue = constantValue;
    }
    
    public WaveMultidimensionalMathFunction(String[] dimensions)
    {
        super(combineLabels(appendStrings(dimensions, "center-"), combineLabels(dimensions, new String[]{"distribution", "form", "frequency", "amplitude", "phase"})));
        this.setDistribution(1.0);
        this.dimensionNames = dimensions.clone();
    }
    
    public String[] getDimensions()
    {
        return this.dimensionNames.clone();
    }

    private static String[] appendStrings(final String[] original, final String append)
    {
        String[] copy = new String[original.length];
        for(int index = 0; index < copy.length; index++)
        {
            copy[index] = append + original[index];
        }
        return copy;
    }
    
    public final void setDimension(final String dimension, final double value)
    {
        this.setParameter(this.getParameterNameIndex(dimension), value);
    }
    
    
    public final double getDimension(final String dimension)
    {
        return this.getParameter(this.getParameterNameIndex(dimension));
    }
    
    
    public final void setCenter(final String dimension, final double value)
    {
        this.setParameter(this.getParameterNameIndex("center-" + dimension), value);
        this.wave.setCenter(0.0);
    }
    
    
    public final double getCenter(final String dimension)
    {
        return this.getParameter(this.getParameterNameIndex("center-" + dimension));
    }



    public final void setDistribution(final double distribution)
    {
        if(distribution == 0.0)
            throw new IllegalArgumentException("distribution can't be 0");
        
        this.setParameter(this.getParameterNameIndex("distribution"), distribution);
        this.wave.setDistribution(distribution);
    }



    public final double getDistribution()
    {
        return this.getParameter(this.getParameterNameIndex("distribution"));
    }
    
    
    public final void setFrequency(final double frequency)
    {
        this.setParameter(this.getParameterNameIndex("frequency"), frequency);
        this.wave.setFrequency(frequency);
    }
    
    public final double getFrequency()
    {
        return this.getParameter(this.getParameterNameIndex("frequency"));
    }
    
    public final void setAmplitude(final double amplitude)
    {
        this.setParameter(this.getParameterNameIndex("amplitude"), amplitude);
        this.wave.setAmplitude(amplitude);
    }
    
    public final double getAmplitude()
    {
        return this.getParameter(this.getParameterNameIndex("amplitude"));
    }
    
    public final void setPhase(final double phase)
    {
        this.setParameter(this.getParameterNameIndex("phase"), phase);
        this.wave.setPhase(phase);
    }
    
    public final double getPhase()
    {
        return this.getParameter(this.getParameterNameIndex("phase"));
    }
    
    public final void setForm(final double form)
    {
        this.setParameter(this.getParameterNameIndex("form"), form);
        this.wave.setForm(form);
    }
    
    public final double getForm()
    {
        return this.getParameter(this.getParameterNameIndex("form"));
    }



    public double calculate()
    {
        if( this.constantMode )
            return this.constantValue;
        
        //step through each dimension value and center value
        double squaredSum = 0.0;
        for(String dimensionName : this.dimensionNames )
        {
            final double dimensionValue = this.getDimension(dimensionName);
            final double centerValue = this.getCenter(dimensionName);
            final double relativeValue = dimensionValue - centerValue;
            
            squaredSum += Math.pow(relativeValue, 2.0);
        }
        final double distanceFromCenter = Math.sqrt(squaredSum);
        
        this.wave.setX(distanceFromCenter);
        
        return this.wave.calculate();
    }



	@Override
    public WaveMultidimensionalMathFunction clone()
    {
        final WaveMultidimensionalMathFunction copy = (WaveMultidimensionalMathFunction)super.clone();

		copy.wave = this.wave.clone();
		copy.dimensionNames = this.dimensionNames.clone();
        copy.constantMode = this.constantMode;
        copy.constantValue = this.constantValue;

        return copy;
    }



    String toString(final String centerName)
    {
        final StringBuffer equationBuffer = new StringBuffer(32);
        for(int squaredSumsIndex = 0; squaredSumsIndex < this.dimensionNames.length; squaredSumsIndex++)
        {
            if(squaredSumsIndex > 0)
                equationBuffer.append(" + ");
            equationBuffer.append("(" + this.dimensionNames[squaredSumsIndex] + " - center-" + this.dimensionNames[squaredSumsIndex] + ")^2");
        }

        final String equation = "sqrt( " + equationBuffer.toString() + " )";
        
        return this.wave.toString(equation, centerName);
    }
    
    
    public String toString()
    {
        return this.toString("center");
    }



    public String[] getDimensionNames()
    {
        return dimensionNames.clone();
    }
}
