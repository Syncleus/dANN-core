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
import com.syncleus.dann.math.*;
public class WaveFunction extends AbstractFunction implements Cloneable
{
    public WaveFunction()
    {
        super(new String[]{"x", "frequency", "amplitude", "phase"});
    }

	public WaveFunction(WaveFunction copy)
	{
		super(copy);
	}
    
    protected WaveFunction(String[] parameterNames)
    {
        super(
                combineLabels(new String[]{"x", "frequency", "amplitude", "phase"}, parameterNames)
        );
    }
    
    protected void setX(double x)
    {
        this.setParameter(this.getParameterNameIndex("x"), x);
    }
    
    protected double getX()
    {
        return this.getParameter(this.getParameterNameIndex("x"));
    }
    
    public void setFrequency(double frequency)
    {
        this.setParameter(this.getParameterNameIndex("frequency"), frequency);
    }
    
    public double getFrequency()
    {
        return this.getParameter(this.getParameterNameIndex("frequency"));
    }
    
    public void setAmplitude(double amplitude)
    {
        this.setParameter(this.getParameterNameIndex("amplitude"), amplitude);
    }
    
    public double getAmplitude()
    {
        return this.getParameter(this.getParameterNameIndex("amplitude"));
    }
    
    public void setPhase(double phase)
    {
        this.setParameter(this.getParameterNameIndex("phase"), phase);
    }
    
    public double getPhase()
    {
        return this.getParameter(this.getParameterNameIndex("phase"));
    }
    
    public double calculate()
    {
        return Math.sin( (this.getX()+(this.getPhase()/360)) * 2 * Math.PI * this.getFrequency()) * this.getAmplitude();
    }

	@Override
    public WaveFunction clone()
    {
		return (WaveFunction) super.clone();
    }
    
    public String toString()
    {
        return this.toString("x");
    }
    
    String toString(String xName)
    {
        return "sin( (" + xName + "+(phase/360)) * 2pi * frequency) * amplitude";
    }
}
