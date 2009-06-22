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

import java.io.Serializable;
import java.util.List;

/**
 * Representation of a point in n-dimensions. Works with both Cartesian
 * coordinate systems and hyperspherical coordinate systems.
 *
 *
 * @author Syncleus, Inc.
 * @since 1.0
 *
 */
public class Hyperpoint implements Serializable
{
    private double[] coordinates;

	/**
	 * Creates a Hyperpoint at the origin (all coordinates are 0) in the
	 * specified number of dimensions
	 *
	 *
	 * @param dimensions number of dimensions of the point
	 * @since 1.0
	 */
    public Hyperpoint(int dimensions)
    {
        if(dimensions <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        
        this.coordinates = new double[dimensions];
    }

	/**
	 * Creates a hyperpoint with the specified coordinates. The number of
	 * dimensions will be equal to the number of coordinates.
	 *
	 *
	 * @param coordinates The initial coordinates for this point.
	 * @since 1.0
	 */
    public Hyperpoint(double[] coordinates)
    {
        if(coordinates == null)
            throw new NullPointerException("coordinates can not be null!");
        
        if(coordinates.length <= 0)
            throw new IllegalArgumentException("coordinates must have atleast one member, 0 dimensions isnt valid!");
        
        this.coordinates = (double[]) coordinates.clone();
    }

	/**
	 * Creates a hyperpoint with the specified coordinates. The number of
	 * dimensions will be equal to the number of coordinates.
	 *
	 *
	 * @param coordinates The initial coordinates for this point.
	 * @since 1.0
	 */
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

	/**
	 * Initializes a new hyperpoint that is a copy of the specified hyperpoint.
	 *
	 *
	 * @param copy the Hyperpoint to copy.
	 * @since 1.0
	 */
    public Hyperpoint(Hyperpoint copy)
    {
        this.coordinates = (double[]) copy.coordinates.clone();
    }

	/**
	 * Gets the number of dimensions of this point.
	 *
	 *
	 * @return The number of dimensions of this point.
	 */
    public int getDimensions()
    {
        return this.coordinates.length;
    }

	/**
	 * Sets the specified coordinate.
	 *
	 *
	 * @param coordinate The new value to set for the coordinate.
	 * @param dimension The dimension of the coordinate to set.
	 * @throws IllegalArgumentException Thrown if the coordinate is less than or
	 * equal to 0 or more than the number of dimensions.
	 * @since 1.0
	 */
    public void setCoordinate(double coordinate, int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if(dimension > this.coordinates.length)
            throw new IllegalArgumentException("dimensions is larger than the dimensionality of this point");
        
        this.coordinates[dimension-1] = coordinate;
    }

	/**
	 * Gets the current value of the specified coordinate.
	 *
	 *
	 * @param dimension The dimension of the coordinate to get.
	 * @return The value for the requested coordinate.
	 * @throws IllegalArgumentException Thrown if the coordinate is less than or
	 * equal to 0 or more than the number of dimensions.
	 * @since 1.0
	 */
    public double getCoordinate(int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if(dimension > this.coordinates.length)
            throw new IllegalArgumentException("dimensions is larger than the dimensionality of this point");
        
        return this.coordinates[dimension-1];
    }

	/**
	 * Sets the distance component of the hyperspherical representation of this
	 * point. It will leave all the angular components close to what they were
	 * before this method was called if the distance argument is positive. If
	 * the distance argument is negative it will invert the vector as well.
	 *
	 *
	 * @param distance The new distance for this vector.
	 * @since 1.0
	 */
    public void setDistance(double distance)
    {
		double[] newCoords = (double[]) this.coordinates.clone();
		
		double oldDistance = this.getDistance();
		double scalar = distance/oldDistance;
		
		for(int newCoordsIndex = 0; newCoordsIndex < newCoords.length; newCoordsIndex++)
			newCoords[newCoordsIndex] *= scalar;
        
        this.coordinates = newCoords;
    }

	/**
	 * Sets the one of the angular components of the hyperspherical
	 * representation of this point. It will keep the other angles and distance
	 * component close to the same.
	 *
	 *
	 * @param angle New angle to set.
	 * @param dimension Dimension of the angle you want to set.
	 * @throws IllegalArgumentException Thrown if dimension is less than or
	 * equal to 0 or if dimension is greater than or equal to the number of
	 * dimensions.
	 * @since 1.0
	 */
    public void setAngularComponent(double angle, int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if((dimension-1) > this.coordinates.length)
            throw new IllegalArgumentException("dimensions is larger than the dimensionality (minus 1) of this point");
        
        double[] newCoords = (double[]) this.coordinates.clone();
		for(int cartesianDimension = 1; cartesianDimension <= this.getDimensions(); cartesianDimension++)
		{
			double sphericalProducts = this.getDistance();
			for(int angleDimension = 1; angleDimension <= ( cartesianDimension >= this.getDimensions() ? this.getDimensions() - 1 : cartesianDimension); angleDimension++)
			{
				if(angleDimension < cartesianDimension)
				{
					if(angleDimension != dimension)
						sphericalProducts *= Math.sin(this.getAngularComponent(angleDimension));
					else
						sphericalProducts *= Math.sin(angle);
				}
				else
				{
					if(angleDimension != dimension)
						sphericalProducts *= Math.cos(this.getAngularComponent(angleDimension));
					else
						sphericalProducts *= Math.cos(angle);
				}
			}
			newCoords[cartesianDimension-1] = sphericalProducts;
		}
        
        this.coordinates = newCoords;
    }

	/**
	 * Gets the distance component of the hyperspherical representation of this
	 * point.
	 *
	 *
	 * @return The distance component of this point using hyperspherical
	 * coordinates.
	 * @since 1.0
	 */
    public double getDistance()
    {
        double squaredSum = 0.0;
        for(double coordinate : this.coordinates)
            squaredSum += Math.pow(coordinate,2);
        return Math.sqrt(squaredSum);
    }

	/**
	 * Obtain the angle of a particular dimension.
	 *
	 *
	 * @param dimension The dimension you want the angle of. the first
	 * dimension is 1. the last is one less than the total number of dimensions.
	 * @return returns a value representing the angle between Pi/2 and -Pi/2
	 * @since 1.0
	 */
    public double getAngularComponent(int dimension)
    {
        if(dimension <= 0)
            throw new IllegalArgumentException("dimensions can not be less than or equal to zero");
        if((dimension-1) > this.coordinates.length)
            throw new IllegalArgumentException("dimensions is larger than the dimensionality (minus 1) of this point");

        double squaredSum = 0.0;
        for(int coordinateIndex = this.coordinates.length-1; coordinateIndex >= (dimension); coordinateIndex--)
            squaredSum += Math.pow(this.coordinates[coordinateIndex], 2.0);

		if( dimension != this.getDimensions() - 1)
		{
			if(this.coordinates[dimension-1] == 0.0d)
				return Math.PI/2.0d;

			return Math.atan(Math.sqrt(squaredSum) / this.coordinates[dimension-1]);
		}
		else
			return Math.atan2(Math.sqrt(squaredSum), this.coordinates[dimension-1]);
    }

	/**
	 * Recalculates this point using the specified point as its origin.
	 *
	 *
	 * @param absolutePoint The origin to calculate relative to.
	 * @return The new Hyperpoint resulting from the new origin.
	 * @since 1.0
	 */
    public Hyperpoint calculateRelativeTo(Hyperpoint absolutePoint)
    {
        if(absolutePoint == null)
            throw new NullPointerException("absolutePoint can not be null!");
        
        if(absolutePoint.getDimensions() != this.getDimensions())
            throw new IllegalArgumentException("absolutePoint must have the same dimensions as this point");
        
        double[] relativeCoords = new double[this.coordinates.length];
        for(int coordIndex = 0; coordIndex < this.coordinates.length; coordIndex++)
            relativeCoords[coordIndex] = this.coordinates[coordIndex] - absolutePoint.getCoordinate(coordIndex+1);
        
        return new Hyperpoint(relativeCoords);
    }

	/**
	 * Adds the specified Hyperpoint to this Hyperpoint.
	 *
	 *
	 * @param pointToAdd Hyperpoint to add with this one.
	 * @return The resulting Hyperpoint after addition.
	 * @since 1.0
	 */
    public Hyperpoint add(Hyperpoint pointToAdd)
    {
        if(pointToAdd == null)
            throw new NullPointerException("pointToAdd can not be null!");
        
        if(pointToAdd.getDimensions() != this.getDimensions())
            throw new IllegalArgumentException("pointToAdd must have the same dimensions as this point");
        
        double[] relativeCoords = new double[this.coordinates.length];
        for(int coordIndex = 0; coordIndex < this.coordinates.length; coordIndex++)
            relativeCoords[coordIndex] = this.coordinates[coordIndex] + pointToAdd.getCoordinate(coordIndex+1);
        
        return new Hyperpoint(relativeCoords);
    }


	/**
	 * A string representation of this Hyperpoint in cartesian coordinates.
	 *
	 *
	 * @return String representation of this point in cartesian coordinates.
	 * @since 1.0
	 */
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

	/**
	 * A string representation of this Hyperpoint in Hyperspherical coordinates.
	 *
	 *
	 * @return String representation of this Hyperpoint in Hyperspherical
	 * coordinates.
	 * @since 1.0
	 */
	public String toStringHypersphere()
	{
		String retString = this.getDistance() + "@";
		for(int angleDimension = 1; angleDimension < this.getDimensions(); angleDimension++)
		{
			retString += this.getAngularComponent(angleDimension);
			if(angleDimension < this.getDimensions() - 1)
				retString += ",";
		}
		return retString;
	}
}
