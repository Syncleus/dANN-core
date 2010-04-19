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

import com.syncleus.dann.UnexpectedDannError;
import org.apache.log4j.Logger;
import java.util.*;

public abstract class AbstractFunction implements Cloneable, Function
{
    private double[] parameters;
    private final String[] parameterNames;
    private final Map<String,Integer> indexNames;
	private final static Logger LOGGER = Logger.getLogger(AbstractFunction.class);

	protected AbstractFunction(AbstractFunction copy)
	{
		this.parameters = copy.parameters.clone();
		this.parameterNames = copy.parameterNames;
		this.indexNames = copy.indexNames;
	}

    protected AbstractFunction(String[] parameterNames)
    {
        if(parameterNames.length <= 0)
		{
			this.indexNames = new Hashtable<String,Integer>();
			this.parameters = null;
			this.parameterNames = null;
            return;
		}
        
        this.parameters = new double[parameterNames.length];
        this.parameterNames = parameterNames.clone();

		Map<String,Integer> newIndexNames = new HashMap<String,Integer>();
        for(int index = 0; index < this.parameterNames.length; index++)
            newIndexNames.put(this.parameterNames[index], Integer.valueOf(index));
		this.indexNames = Collections.unmodifiableMap(newIndexNames);
    }
    
    protected static String[] combineLabels(final String[] first, final String[] second)
    {
        final String[] result = new String[first.length + second.length];
        int resultIndex = 0;

		System.arraycopy(first, 0, result, resultIndex, first.length);
        resultIndex += first.length;
		System.arraycopy(second, 0, result, resultIndex, second.length);
        
        return result;
    }
    
    public final void setParameter(final int parameterIndex, final double value)
    {
        if(parameterIndex >= parameters.length || parameterIndex < 0)
            throw new IllegalArgumentException("parameterIndex of " + parameterIndex + " is out of range");
        
        this.parameters[parameterIndex] = value;
    }

	public final void setParameter(final String parameterName, final double value)
	{
		this.setParameter(this.getParameterNameIndex(parameterName), value);
	}
    
    public final double getParameter(final int parameterIndex)
    {
        if(parameterIndex >= parameters.length || parameterIndex < 0)
            throw new IllegalArgumentException("parameterIndex out of range");
        
        return this.parameters[parameterIndex];
    }

	public final double getParameter(final String parameterName)
	{
		return this.getParameter(this.getParameterNameIndex(parameterName));
	}
    
    public final String getParameterName(final int parameterIndex)
    {
        if( parameterIndex >= this.parameterNames.length || parameterIndex < 0 )
            throw new IllegalArgumentException("parameterIndex is not within range");
        
        return this.parameterNames[parameterIndex];
    }
    
    public final int getParameterNameIndex(final String parameterName)
    {
        if( !this.indexNames.containsKey(parameterName))
            throw new IllegalArgumentException("parameterName: " + parameterName + " does not exist");
        
        return this.indexNames.get(parameterName).intValue();
    }
    
    public final int getParameterCount()
    {
        return this.parameters.length;
    }

	@Override
	@SuppressWarnings("unchecked")
    public AbstractFunction clone()
	{
		try
		{
			AbstractFunction copy = (AbstractFunction) super.clone();
			copy.parameters = this.parameters.clone();
			return copy;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

    public abstract double calculate();
	@Override
    public abstract String toString();
}
