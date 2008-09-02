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
package com.syncleus.dann.associativemap;

import java.io.Serializable;
import java.util.List;


public class Point implements Serializable
{
    private double[] coordinates;
    
    public Point(int dimensions)
    {
        if(dimensions <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        
        this.coordinates = new double[dimensions];
    }
    
    public Point(double[] coordinates)
    {
        if(coordinates == null)
            throw new NullPointerException("coordinates can not be null!");
        
        if(coordinates.length <= 0)
            throw new IllegalArgumentException("coordinates must have atleast one member, 0 dimentions isnt valid!");
        
        this.coordinates = (double[]) coordinates.clone();
    }
    
    public Point(List<Double> coordinates)
    {
        if(coordinates == null)
            throw new NullPointerException("coordinates can not be null!");
        
        if(coordinates.size() <= 0)
            throw new IllegalArgumentException("coordinates must have atleast one member, 0 dimentions isnt valid!");
        
        this.coordinates = new double[coordinates.size()];
        int coordinatesIndex = 0;
        for(Double coordinate : coordinates)
        {
            this.coordinates[coordinatesIndex++] = coordinate.doubleValue();
        }
    }
    
    public Point(Point copy)
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
        if(dimension >= this.coordinates.length)
            throw new IllegalArgumentException("dimentions is larger than the dimentionality of this point");
        
        this.coordinates[dimension-1] = coordinate;
    }
    
    public double getCoordinate(int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if(dimension > this.coordinates.length)
            throw new IllegalArgumentException("dimentions is larger than the dimentionality of this point");
        
        return this.coordinates[dimension-1];
    }
    
    public void setDistance(double distance)
    {        
        double[] newCoords = (double[]) this.coordinates.clone();
        for(int coordinateIndex = 0; coordinateIndex < this.getDimensions(); coordinateIndex++)
        {
            double sphericalProducts = distance;
            
            for(int angleDimention = 1; angleDimention - 1 < (coordinateIndex + 1);angleDimention++)
            {
                if( angleDimention < (coordinateIndex + 1))
                    sphericalProducts *= Math.sin(this.getAngularComponent(angleDimention));
                else
                {
                    if((coordinateIndex + 1) == this.getDimensions())
                        sphericalProducts *= Math.sin(this.getAngularComponent(angleDimention));
                    else
                        sphericalProducts *= Math.cos(this.getAngularComponent(angleDimention));
                }
            }
            newCoords[coordinateIndex] = sphericalProducts;
        }
        
        this.coordinates = newCoords;
    }
    
    public void setAngularComponent(double angle, int dimention)
    {
        if(dimention <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if((dimention-2) >= this.coordinates.length)
            throw new IllegalArgumentException("dimentions is larger than the dimentionality (minus 1) o this point");
        
        double[] newCoords = (double[]) this.coordinates.clone();
        for(int coordinateIndex = dimention-1; coordinateIndex < this.getDimensions(); coordinateIndex++)
        {
            double sphericalProducts = this.getDistance();
            
            for(int angleDimention = 1; angleDimention - 1 < (coordinateIndex + 1);angleDimention++)
            {
                if( angleDimention < (coordinateIndex + 1))
                    sphericalProducts *= Math.sin(this.getAngularComponent(angleDimention));
                else
                {
                    if((coordinateIndex + 1) == this.getDimensions())
                        sphericalProducts *= Math.sin(this.getAngularComponent(angleDimention));
                    else
                        sphericalProducts *= Math.cos(this.getAngularComponent(angleDimention));
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
    
    public double getAngularComponent(int dimention)
    {
        if(dimention <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if((dimention-2) >= this.coordinates.length)
            throw new IllegalArgumentException("dimentions is larger than the dimentionality (minus 1) o this point");
        
        double squaredSum = 0.0;
        for(int coordinateIndex = this.coordinates.length-1; coordinateIndex >= (dimention); coordinateIndex--)
            squaredSum += Math.pow(this.coordinates[coordinateIndex], 2.0);
        
        return Math.atan2(Math.sqrt(squaredSum), this.coordinates[dimention-1]);
    }
    
    public Point calculateRelativeTo(Point absolutePoint)
    {
        if(absolutePoint == null)
            throw new NullPointerException("absolutePoint can not be null!");
        
        if(absolutePoint.getDimensions() != this.getDimensions())
            throw new IllegalArgumentException("absolutePoint must have the same dimentions as this point");
        
        double[] relativeCoords = new double[this.coordinates.length];
        for(int coordIndex = 0; coordIndex < this.coordinates.length; coordIndex++)
        {
            relativeCoords[coordIndex] = this.coordinates[coordIndex] - absolutePoint.getCoordinate(coordIndex+1);
        }
        
        return new Point(relativeCoords);
    }
    
    public Point add(Point pointToAdd)
    {
        if(pointToAdd == null)
            throw new NullPointerException("absolutePoint can not be null!");
        
        if(pointToAdd.getDimensions() != this.getDimensions())
            throw new IllegalArgumentException("absolutePoint must have the same dimentions as this point");
        
        double[] relativeCoords = new double[this.coordinates.length];
        for(int coordIndex = 0; coordIndex < this.coordinates.length; coordIndex++)
        {
            relativeCoords[coordIndex] = this.coordinates[coordIndex] + pointToAdd.getCoordinate(coordIndex+1);
        }
        
        return new Point(relativeCoords);
    }
}
