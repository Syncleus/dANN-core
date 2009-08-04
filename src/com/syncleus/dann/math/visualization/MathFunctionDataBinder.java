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


import com.syncleus.dann.math.AbstractMathFunction;
import org.freehep.j3d.plot.*;
import javax.vecmath.*;
import java.awt.Color;
import java.security.InvalidParameterException;

public final class MathFunctionDataBinder implements Binned2DData
{
    private final AbstractMathFunction function;
    private final int functionXIndex;
    private final int functionYIndex;
    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;
    private final float minZ;
    private final float maxZ;
    private final int resolution;



    public MathFunctionDataBinder(AbstractMathFunction function,
                                     String functionXParam,
                                     String functionYParam,
                                     float xMin,
                                     float xMax,
                                     float yMin,
                                     float yMax,
                                     int resolution)
    {
        if( resolution <= 0 )
            throw new InvalidParameterException("resolution must be greater than 0");
        
        this.function = function;
        this.functionXIndex = this.function.getParameterNameIndex(functionXParam);
        this.functionYIndex = this.function.getParameterNameIndex(functionYParam);
        this.minX = xMin;
        this.maxX = xMax;
        this.minY = yMin;
        this.maxY = yMax;
        this.resolution = resolution;

        boolean zMaxSet = false;
        boolean zMinSet = false;
		float newZMax = 1.0f;
		float newZMin = -1.0f;
        for(int xIndex = 0;xIndex < this.xBins();xIndex++)
        {
            this.setX(this.convertFromXIndex(xIndex));
            for(int yIndex = 0;yIndex < this.yBins();yIndex++)
            {
                this.setY(this.convertFromYIndex(yIndex));
                final float currentZ = (float)this.calculateZ();

                if(!Float.isNaN(currentZ))
                {

                    if((this.maxZ < currentZ) || (!zMaxSet))
                    {
                        newZMax = currentZ;
                        zMaxSet = true;
                    }

                    if((this.minZ > currentZ) || (!zMinSet))
                    {
                        newZMin = currentZ;
                        zMinSet = true;
                    }
                }
            }
        }
        
        if(newZMax == newZMin)
        {
            newZMax += 1.0;
            newZMin += -1.0;
        }

		this.maxZ = newZMax;
		this.minZ = newZMin;
        
        if( Float.isNaN(maxZ) || Float.isNaN(minZ))
            throw new InvalidParameterException("z does not deviate, nothing to plot!");
    }



	private float convertFromXIndex(final int xCoord)
    {
        final float xSize = this.maxX - this.minX;

        return (((float)xCoord) / ((float)this.xBins())) * xSize + this.minX;
    }



    private float convertFromYIndex(final int yCoord)
    {
        final float ySize = this.maxY - this.minY;
        return (((float)(this.yBins() - yCoord)) / ((float)this.yBins())) * ySize + this.minY;
    }



    public AbstractMathFunction getFunction()
    {
        return this.function;
    }

	public int getXIndex()
	{
		return this.functionXIndex;
	}

	public int getYIndex()
	{
		return this.functionYIndex;
	}

	public int getResolution()
	{
		return this.resolution;
	}

    private void setX(final double xCoord)
    {
        this.function.setParameter(this.functionXIndex, xCoord);
    }



    private void setY(final double yCoord)
    {
        this.function.setParameter(this.functionYIndex, yCoord);
    }



    private double calculateZ()
    {
        return this.function.calculate();
    }



    public Color3b colorAt(final int xIndex, final int yIndex)
    {
        final float xCoord = this.convertFromXIndex(xIndex);
        final float yCoord = this.convertFromYIndex(yIndex);

        this.setX(xCoord);
        this.setY(yCoord);
        final double zCoord = this.calculateZ();


        if(zCoord > this.maxZ)
            return new Color3b(new Color(0.0f, 0.0f, 0.0f));
        else if(zCoord < this.minZ)
            return new Color3b(new Color(0.0f, 0.0f, 0.0f));
        else
        {
            final float redValue = (float)(zCoord - this.minZ) / (this.maxZ - this.minZ);
            final float blueValue = 1.0f - redValue;
            final float greenValue = 0.0f;

            return new Color3b(new Color(redValue, greenValue, blueValue));
        }
    }



    public int xBins()
    {
        return this.resolution;
    }



    public float xMax()
    {
        return this.maxX;
    }



    public float xMin()
    {
        return this.minX;
    }



    public int yBins()
    {
        return this.resolution;
    }



    public float yMax()
    {
        return this.maxY;
    }



    public float yMin()
    {
        return this.minY;
    }



    public float zAt(final int xIndex, final int yIndex)
    {
        final float xCoord = this.convertFromXIndex(xIndex);
        final float yCoord = this.convertFromYIndex(yIndex);

        this.setX(xCoord);
        this.setY(yCoord);
        final float zCoord = (float)this.calculateZ();


        if(zCoord < this.minZ)
            return this.minZ;
        else if(zCoord > this.maxZ)
            return this.maxZ;
        else if(Float.isNaN(zCoord))
            return 0.0f;
        else
            return zCoord;
    }



    public float zMax()
    {
        return this.maxZ;
    }



    public float zMin()
    {
        return this.minZ;
    }
}
