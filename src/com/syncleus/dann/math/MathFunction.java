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
import java.util.Hashtable;

public abstract class MathFunction
{
    private double[] parameters;
    private String[] parameterNames;
    private Hashtable<String,Integer> indexNames = new Hashtable<String,Integer>();
    
    protected MathFunction(String[] parameterNames)
    {
        if(parameterNames.length <= 0)
            return;
        
        this.parameters = new double[parameterNames.length];
        this.parameterNames = parameterNames.clone();
        
        for(int index = 0; index < this.parameterNames.length; index++)
        {
            this.indexNames.put(this.parameterNames[index], Integer.valueOf(index));
        }
    }
    
    public String[] getParameterNames()
    {
        return this.parameterNames.clone();
    }
    
    protected static String[] combineLabels(String[] first, String[] second)
    {
        String[] result = new String[first.length + second.length];
        int resultIndex = 0;
        
        for(int firstIndex = 0; firstIndex < first.length; firstIndex++)
        {
            result[resultIndex] = first[firstIndex];
            resultIndex++;
        }
        for(int secondIndex = 0; secondIndex < second.length; secondIndex++)
        {
            result[resultIndex] = second[secondIndex];
            resultIndex++;
        }
        
        return result;
    }
    
    public void setParameter(int parameterIndex, double value)
    {
        if(parameterIndex >= parameters.length || parameterIndex < 0)
            throw new InvalidParameterException("parameterIndex of " + parameterIndex + " is out of range");
        
        this.parameters[parameterIndex] = value;
    }

	public void setParameter(String parameterName, double value)
	{
		this.setParameter(this.getParameterNameIndex(parameterName), value);
	}
    
    public double getParameter(int parameterIndex)
    {
        if(parameterIndex >= parameters.length || parameterIndex < 0)
            throw new InvalidParameterException("parameterIndex out of range");
        
        return this.parameters[parameterIndex];
    }

	public double getParameter(String parameterName)
	{
		return this.getParameter(this.getParameterNameIndex(parameterName));
	}
    
    public String getParameterName(int parameterIndex)
    {
        if( parameterIndex >= this.parameterNames.length || parameterIndex < 0 )
            throw new InvalidParameterException("parameterIndex is not within range");
        
        return this.parameterNames[parameterIndex];
    }
    
    public int getParameterNameIndex(String parameterName)
    {
        if( this.indexNames.containsKey(parameterName) == false)
            throw new InvalidParameterException("parameterName: " + parameterName + " does not exist");
        
        return this.indexNames.get(parameterName).intValue();
    }
    
    public int getParameterCount()
    {
        return this.parameters.length;
    }
    
    public abstract MathFunction clone();
    public abstract double calculate();
    public abstract String toString();
}
