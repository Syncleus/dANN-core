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

public class DistributedFormedWaveMathFunction extends FormedWaveMathFunction
{
    private boolean constantMode = false;
    private double constantValue;



    public DistributedFormedWaveMathFunction(double constantValue)
    {
        this();

        this.constantMode = true;
        this.constantValue = constantValue;
    }


	public DistributedFormedWaveMathFunction(DistributedFormedWaveMathFunction copy)
	{
		super(copy);
		this.constantMode = copy.constantMode;
		this.constantValue = copy.constantValue;
	}



    public DistributedFormedWaveMathFunction()
    {
        super(new String[]{"center", "distribution"});
        this.setDistribution(1.0);
    }



    protected DistributedFormedWaveMathFunction(String[] additionalParameters)
    {
        super(combineLabels(new String[]{"center", "distribution"}, additionalParameters));
        this.setDistribution(1.0);
    }



    public void setCenter(double center)
    {
        this.setParameter(this.getParameterNameIndex("center"), center);
    }



    public double getCenter()
    {
        return this.getParameter(this.getParameterNameIndex("center"));
    }



    public void setDistribution(double distribution)
    {
        if(distribution == 0.0)
        {
            throw new InvalidParameterException("distribution can't be 0");
        }

        this.setParameter(this.getParameterNameIndex("distribution"), distribution);
    }



    public double getDistribution()
    {
        return this.getParameter(this.getParameterNameIndex("distribution"));
    }



    private double calculateDistribution()
    {
        return (1 / (this.getDistribution() * Math.sqrt(2 * Math.PI))) * Math.exp(-1 * (Math.pow((this.getX() - this.getCenter()), 2)) / (2 * Math.pow(this.getDistribution(), 2)));
    //return Math.pow(1/Math.cosh(this.getX() - this.getCenter()), this.getDistribution());
    }



    public double calculate()
    {
        if(this.constantMode)
        {
            return this.constantValue;
        }

        return super.calculate() * this.calculateDistribution();
    }



	@Override
    public DistributedFormedWaveMathFunction clone() throws CloneNotSupportedException
    {
        DistributedFormedWaveMathFunction copy = (DistributedFormedWaveMathFunction)super.clone();
        copy.setX(this.getX());
        copy.setFrequency(this.getFrequency());
        copy.setPhase(this.getPhase());
        copy.setAmplitude(this.getAmplitude());
        copy.setForm(this.getForm());
        copy.setCenter(this.getCenter());
        copy.setDistribution(this.getDistribution());
        copy.constantMode = this.constantMode;
        copy.constantValue = this.constantValue;
        return copy;
    }



    public String toString()
    {
        return this.toString("x", "center");
    }
    
    String toString(String xName, String centerName)
    {
        return "(1 / (distribution * Math.sqrt(2 * pi))) * e^(-1 * ( (" + xName + " - " + centerName + ")^2 ) / (2 * distribution^2))" + " * " + super.toString(xName);
    }
}
