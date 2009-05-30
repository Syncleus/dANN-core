/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                    *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by syncleus at http://www.syncleus.com.  *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact syncleus at the information below if you cannot find   *
 *  a license:                                                                 *
 *                                                                             *
 *  Syncleus                                                                   *
 *  1116 McClellan St.                                                         *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.hyperassociativemap;

import java.io.Serializable;
import java.util.List;


public class Hyperpoint implements Serializable
{
    private double[] coordinates;
    
    public Hyperpoint(int dimensions)
    {
        if(dimensions <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        
        this.coordinates = new double[dimensions];
    }
    
    public Hyperpoint(double[] coordinates)
    {
        if(coordinates == null)
            throw new NullPointerException("coordinates can not be null!");
        
        if(coordinates.length <= 0)
            throw new IllegalArgumentException("coordinates must have atleast one member, 0 dimensions isnt valid!");
        
        this.coordinates = (double[]) coordinates.clone();
    }
    
    public Hyperpoint(List<Double> coordinates)
    {
        if(coordinates == null)
            throw new NullPointerException("coordinates can not be null!");
        
        if(coordinates.size() <= 0)
            throw new IllegalArgumentException("coordinates must have atleast one member, 0 dimensions isnt valid!");
        
        this.coordinates = new double[coordinates.size()];
        int coordinatesIndex = 0;
        for(Double coordinate : coordinates)
        {
            this.coordinates[coordinatesIndex++] = coordinate.doubleValue();
        }
    }
    
    public Hyperpoint(Hyperpoint copy)
    {
        this.coordinates = (double[]) copy.coordinates.clone();
    }
    
    public int getDimensions()
    {
        return this.coordinates.length;
    }
    
    public void setCoordinate(double coordinate, int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if(dimension > this.coordinates.length)
            throw new IllegalArgumentException("dimensions is larger than the dimensionality of this point");
        
        this.coordinates[dimension-1] = coordinate;
    }
    
    public double getCoordinate(int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if(dimension > this.coordinates.length)
            throw new IllegalArgumentException("dimensions is larger than the dimensionality of this point");
        
        return this.coordinates[dimension-1];
    }
    
    public void setDistance(double distance)
    {        
        double[] newCoords = (double[]) this.coordinates.clone();
        for(int coordinateIndex = 0; coordinateIndex < this.getDimensions(); coordinateIndex++)
        {
            double sphericalProducts = distance;

			for(int angleDimension = 1; angleDimension - 1 < ((coordinateIndex+1 < this.getDimensions() ? coordinateIndex : coordinateIndex - 1) + 1);angleDimension++)
            {
                if( angleDimension < (coordinateIndex + 1))
                    sphericalProducts *= Math.sin(this.getAngularComponent(angleDimension));
                else
                {
                    if((coordinateIndex + 1) == this.getDimensions())
                        sphericalProducts *= Math.sin(this.getAngularComponent(angleDimension));
                    else
                        sphericalProducts *= Math.cos(this.getAngularComponent(angleDimension));
                }
            }
            newCoords[coordinateIndex] = sphericalProducts;
        }
        
        this.coordinates = newCoords;
    }
    
    public void setAngularComponent(double angle, int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if((dimension-1) > this.coordinates.length)
            throw new IllegalArgumentException("dimensions is larger than the dimensionality (minus 1) of this point");
        
        double[] newCoords = (double[]) this.coordinates.clone();
        for(int coordinateIndex = dimension-1; coordinateIndex < this.getDimensions(); coordinateIndex++)
        {
            double sphericalProducts = this.getDistance();
            
            for(int angleDimension = 1; angleDimension - 1 < ((coordinateIndex+1 < this.getDimensions() ? coordinateIndex : coordinateIndex - 1) + 1);angleDimension++)
            {
                if( angleDimension < (coordinateIndex + 1))
				{
                    sphericalProducts *= Math.sin((angleDimension == dimension ? angle : this.getAngularComponent(angleDimension)));
				}
                else
                {
                    if((coordinateIndex + 1) == this.getDimensions())
					{
                        sphericalProducts *= Math.sin((angleDimension == dimension ? angle : this.getAngularComponent(angleDimension)));
					}
                    else
					{
                        sphericalProducts *= Math.cos((angleDimension == dimension ? angle : this.getAngularComponent(angleDimension)));
					}
                }
            }
            newCoords[coordinateIndex] = sphericalProducts;
        }
        
        this.coordinates = newCoords;
    }
    
    public double getDistance()
    {
        double squaredSum = 0.0;
        for(double coordinate : this.coordinates)
            squaredSum += Math.pow(coordinate,2);
        return Math.sqrt(squaredSum);
    }
    
    public double getAngularComponent(int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if((dimension-1) > this.coordinates.length)
            throw new IllegalArgumentException("dimensions is larger than the dimensionality (minus 1) of this point");
        
        double squaredSum = 0.0;
        for(int coordinateIndex = this.coordinates.length-1; coordinateIndex >= (dimension); coordinateIndex--)
            squaredSum += Math.pow(this.coordinates[coordinateIndex], 2.0);
        
        return Math.atan2(Math.sqrt(squaredSum), this.coordinates[dimension-1]);
    }
    
    public Hyperpoint calculateRelativeTo(Hyperpoint absolutePoint)
    {
        if(absolutePoint == null)
            throw new NullPointerException("absolutePoint can not be null!");
        
        if(absolutePoint.getDimensions() != this.getDimensions())
            throw new IllegalArgumentException("absolutePoint must have the same dimensions as this point");
        
        double[] relativeCoords = new double[this.coordinates.length];
        for(int coordIndex = 0; coordIndex < this.coordinates.length; coordIndex++)
        {
            relativeCoords[coordIndex] = this.coordinates[coordIndex] - absolutePoint.getCoordinate(coordIndex+1);
        }
        
        return new Hyperpoint(relativeCoords);
    }
    
    public Hyperpoint add(Hyperpoint pointToAdd)
    {
        if(pointToAdd == null)
            throw new NullPointerException("pointToAdd can not be null!");
        
        if(pointToAdd.getDimensions() != this.getDimensions())
            throw new IllegalArgumentException("pointToAdd must have the same dimensions as this point");
        
        double[] relativeCoords = new double[this.coordinates.length];
        for(int coordIndex = 0; coordIndex < this.coordinates.length; coordIndex++)
        {
            relativeCoords[coordIndex] = this.coordinates[coordIndex] + pointToAdd.getCoordinate(coordIndex+1);
        }
        
        return new Hyperpoint(relativeCoords);
    }


	@Override
	public String toString()
	{
		String stringValue = "{";
		for(int dimension = 1; dimension <= this.getDimensions(); dimension++)
		{
			stringValue += this.getCoordinate(dimension);
			if(dimension < this.getDimensions())
				stringValue += ",";
		}

		return stringValue + "}";
	}
}
