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
package com.syncleus.dann.math.visualization;

import com.syncleus.dann.math.AbstractFunction;
import org.freehep.j3d.plot.*;

public class MathFunctionCanvas extends SurfacePlot
{
	private AbstractFunction function;
	private int xIndex;
	private int yIndex;
	private float xMin;
	private float xMax;
	private float yMin;
	private float yMax;
	private int resolution;

	public MathFunctionCanvas(AbstractFunction function, String xParameter, String yParameter, float xMin, float xMax, float yMin, float yMax, int resolution)
	{
		super();

		this.function = function;
		this.xIndex = this.function.getParameterNameIndex(xParameter);
		this.yIndex = this.function.getParameterNameIndex(yParameter);
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.resolution = resolution;

		this.setLogZscaling(false);

		this.refresh();
	}

	private void refresh()
	{
        MathFunctionDataBinder dataBinder = new MathFunctionDataBinder(
            this.function,
            this.function.getParameterName(xIndex),
            this.function.getParameterName(yIndex),
            this.xMin,
            this.xMax,
            this.yMin,
            this.yMax,
            this.resolution);

		super.setData(dataBinder);
	}

	public void setFunction(AbstractFunction newFunction, String xParameter, String yParameter)
	{
		this.function = newFunction;
		this.xIndex = this.function.getParameterNameIndex(xParameter);
		this.yIndex = this.function.getParameterNameIndex(yParameter);

		this.refresh();
	}

    public AbstractFunction getFunction()
    {
        return this.function;
    }

	public int getXIndex()
	{
		return this.xIndex;
	}

	public int getYIndex()
	{
		return this.yIndex;
	}

	public int getResolution()
	{
		return this.resolution;
	}

    public float getXMax()
    {
        return this.xMax;
    }



    public float getXMin()
    {
        return this.xMin;
    }



    public float getYMax()
    {
        return this.yMax;
    }



    public float getYMin()
    {
        return this.yMin;
    }

	public void setData(Binned2DData data)
	{
		if(data instanceof MathFunctionDataBinder)
		{
			MathFunctionDataBinder mathFunctionData = (MathFunctionDataBinder)data;

			this.function = mathFunctionData.getFunction();
			this.xIndex = mathFunctionData.getXIndex();
			this.yIndex = mathFunctionData.getYIndex();
			this.setXMin(mathFunctionData.xMin());
			this.setXMax(mathFunctionData.xMax());
			this.setYMin(mathFunctionData.yMin());
			this.setYMax(mathFunctionData.yMax());
			this.setResolution(mathFunctionData.getResolution());

			this.refresh();
		}
		else
			throw new IllegalArgumentException("data must be a MathFunction3dDataBinder");
	}



	public void setXMin(float xMin)
	{
		this.xMin = xMin;
		this.refresh();
	}



	public void setXMax(float xMax)
	{
		this.xMax = xMax;
		this.refresh();
	}



	public void setYMin(float yMin)
	{
		this.yMin = yMin;
		this.refresh();
	}



	public void setYMax(float yMax)
	{
		this.yMax = yMax;
		this.refresh();
	}



	public void setResolution(int resolution)
	{
		this.resolution = resolution;
		this.refresh();
	}
}
