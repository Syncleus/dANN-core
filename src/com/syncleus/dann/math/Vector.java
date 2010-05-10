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
 * coordinate systems and hyperspherical coordinate systems. This class is
 * thread safe.
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public class Vector implements Serializable
{
	private static final long serialVersionUID = -1488734312355605257L;
	private final double[] coordinates;
	private static final String DIMENSIONS_BELOW_ONE = "dimensions can not be less than or equal to zero";
	private Double distanceCache = null;

	/**
	 * Creates a Vector at the origin (all coordinates are 0) in the specified
	 * number of dimensions
	 *
	 * @param dimensions number of dimensions of the point
	 * @since 1.0
	 */
	public Vector(final int dimensions)
	{
		if (dimensions <= 0)
			throw new IllegalArgumentException(DIMENSIONS_BELOW_ONE);
		this.coordinates = new double[dimensions];
	}

	/**
	 * Creates a hyperpoint with the specified coordinates. The number of
	 * dimensions will be equal to the number of coordinates.
	 *
	 * @param coordinates The initial coordinates for this point.
	 * @since 1.0
	 */
	public Vector(final double... coordinates)
	{
		if (coordinates == null)
			throw new IllegalArgumentException("coordinates can not be null!");
		if (coordinates.length <= 0)
			throw new IllegalArgumentException("coordinates must have atleast one member, 0 dimensions isnt valid!");
		this.coordinates = coordinates.clone();
	}

	/**
	 * Creates a hyperpoint with the specified coordinates. The number of
	 * dimensions will be equal to the number of coordinates.
	 *
	 * @param coordinates The initial coordinates for this point.
	 * @since 1.0
	 */
	public Vector(final List<Double> coordinates)
	{
		if (coordinates == null)
			throw new IllegalArgumentException("coordinates can not be null!");
		if (coordinates.size() <= 0)
			throw new IllegalArgumentException("coordinates must have atleast one member, 0 dimensions isnt valid!");
		this.coordinates = new double[coordinates.size()];
		int coordinatesIndex = 0;
		for(final Double coordinate : coordinates)
		{
			this.coordinates[coordinatesIndex++] = coordinate;
		}
	}

	/**
	 * Initializes a new hyperpoint that is a copy of the specified hyperpoint.
	 *
	 * @param copy the Vector to copy.
	 * @since 1.0
	 */
	public Vector(final Vector copy)
	{
		this.coordinates = copy.coordinates.clone();
	}

	/**
	 * Gets the number of dimensions of this point.
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
	 * @param coordinate The new value to set for the coordinate.
	 * @param dimension The dimension of the coordinate to set.
	 * @throws IllegalArgumentException Thrown if the coordinate is less than or
	 * equal to 0 or more than the number of dimensions.
	 * @since 1.0
	 */
	public Vector setCoordinate(final double coordinate, final int dimension)
	{
		if (dimension <= 0)
			throw new IllegalArgumentException(DIMENSIONS_BELOW_ONE);
		if (dimension > this.coordinates.length)
			throw new IllegalArgumentException("dimensions is larger than the dimensionality of this point");
		final double[] coords = this.coordinates.clone();
		coords[dimension - 1] = coordinate;
		return new Vector(coords);
	}

	/**
	 * Gets the current value of the specified coordinate.
	 *
	 * @param dimension The dimension of the coordinate to get.
	 * @return The value for the requested coordinate.
	 * @throws IllegalArgumentException Thrown if the coordinate is less than or
	 * equal to 0 or more than the number of dimensions.
	 * @since 1.0
	 */
	public double getCoordinate(final int dimension)
	{
		if (dimension <= 0)
			throw new IllegalArgumentException(DIMENSIONS_BELOW_ONE);
		if (dimension > this.coordinates.length)
			throw new IllegalArgumentException("dimensions is larger than the dimensionality of this point");
		return this.coordinates[dimension - 1];
	}

	/**
	 * Sets the distance component of the hyperspherical representation of this
	 * point. It will leave all the angular components close to what they were
	 * before this method was called if the distance argument is positive. If
	 * the distance argument is negative it will invert the vector as well.
	 *
	 * @param distance The new distance for this vector.
	 * @since 1.0
	 */
	public Vector setDistance(final double distance)
	{
		final Vector newVector = new Vector(this.coordinates);
		final double[] newCoords = newVector.coordinates;
		final double oldDistance = this.getDistance();
		final double scalar = distance / oldDistance;
		for(int newCoordsIndex = 0; newCoordsIndex < newCoords.length; newCoordsIndex++)
			newCoords[newCoordsIndex] *= scalar;
		return newVector;
	}

	/**
	 * Sets the one of the angular components of the hyperspherical
	 * representation of this point. It will keep the other angles and distance
	 * component close to the same.
	 *
	 * @param angle New angle to set.
	 * @param dimension Dimension of the angle you want to set.
	 * @throws IllegalArgumentException Thrown if dimension is less than or
	 * equal to 0 or if dimension is greater than or equal to the number of
	 * dimensions.
	 * @since 1.0
	 */
	public Vector setAngularComponent(final double angle, final int dimension)
	{
		if (dimension <= 0)
			throw new IllegalArgumentException(DIMENSIONS_BELOW_ONE);
		if ((dimension - 1) > this.coordinates.length)
			throw new IllegalArgumentException("dimensions is larger than the dimensionality (minus 1) of this point");
		final Vector newVector = new Vector(this);
		final double[] newCoords = newVector.coordinates;
		for(int cartesianDimension = 1; cartesianDimension <= this.getDimensions(); cartesianDimension++)
		{
			double sphericalProducts = this.getDistance();
			for(int angleDimension = 1; angleDimension <= (cartesianDimension >= this.getDimensions() ? this.getDimensions() - 1 : cartesianDimension); angleDimension++)
			{
				if (angleDimension < cartesianDimension)
				{
					if (angleDimension == dimension)
						sphericalProducts *= Math.sin(angle);
					else
						sphericalProducts *= Math.sin(this.getAngularComponent(angleDimension));
				}
				else
				{
					if (angleDimension == dimension)
						sphericalProducts *= Math.cos(angle);
					else
						sphericalProducts *= Math.cos(this.getAngularComponent(angleDimension));
				}
			}
			newCoords[cartesianDimension - 1] = sphericalProducts;
		}
		return newVector;
	}

	/**
	 * Gets the distance component of the hyperspherical representation of this
	 * point.
	 *
	 * @return The distance component of this point using hyperspherical
	 *         coordinates.
	 * @since 1.0
	 */
	public double getDistance()
	{
		if (this.distanceCache == null)
		{
			final double currentCoords[] = this.coordinates.clone();
			double squaredSum = 0.0;
			for(final double coordinate : currentCoords)
				squaredSum += Math.pow(coordinate, 2);
			this.distanceCache = Math.sqrt(squaredSum);
		}
		return this.distanceCache;
	}

	/**
	 * Obtain the angle of a particular dimension.
	 *
	 * @param dimension The dimension you want the angle of. the first dimension
	 * is 1. the last is one less than the total number of dimensions.
	 * @return returns a value representing the angle between Pi/2 and -Pi/2
	 * @since 1.0
	 */
	public double getAngularComponent(final int dimension)
	{
		if (dimension <= 0)
			throw new IllegalArgumentException(DIMENSIONS_BELOW_ONE);
		if ((dimension - 1) > this.coordinates.length)
			throw new IllegalArgumentException("dimensions is larger than the dimensionality (minus 1) of this point");
		final double currentCoords[] = this.coordinates.clone();
		double squaredSum = 0.0;
		for(int coordinateIndex = currentCoords.length - 1; coordinateIndex >= dimension; coordinateIndex--)
			squaredSum += Math.pow(currentCoords[coordinateIndex], 2.0);
		if (dimension == (this.getDimensions() - 1))
			return Math.atan2(Math.sqrt(squaredSum), currentCoords[dimension - 1]);
		else
		{
			if (currentCoords[dimension - 1] == 0.0d)
				return Math.PI / 2.0d;
			return Math.atan(Math.sqrt(squaredSum) / currentCoords[dimension - 1]);
		}
	}

	public double getNorm(final int order)
	{
		double poweredSum = 0.0;
		for(final double coordinate : this.coordinates)
			poweredSum += Math.pow(Math.abs(coordinate), order);
		return Math.pow(poweredSum, 1.0 / ((double) order));
	}

	public double getNorm()
	{
		return this.getNorm(2);
	}

	public double getNormInfinity()
	{
		double maximum = 0.0;
		for(final double coordinate : this.coordinates)
			if (maximum < coordinate)
				maximum = coordinate;
		return maximum;
	}

	public Vector normalize()
	{
		if (this.isOrigin())
			throw new ArithmeticException("cant normalize a 0 vector");
		final double norm = this.getNorm();
		return this.multiply(1.0 / norm);
	}

	public boolean isOrigin()
	{
		for(final double coordinate : this.coordinates)
			if (coordinate != 0.0)
				return false;
		return true;
	}

	/**
	 * Recalculates this point using the specified point as its origin.
	 *
	 * @param absolutePoint The origin to calculate relative to.
	 * @return The new Vector resulting from the new origin.
	 * @since 1.0
	 */
	public Vector calculateRelativeTo(final Vector absolutePoint)
	{
		if (absolutePoint == null)
			throw new IllegalArgumentException("absolutePoint can not be null!");
		final double currentCoords[] = this.coordinates.clone();
		final double absoluteCoords[] = absolutePoint.coordinates.clone();
		if (absoluteCoords.length != currentCoords.length)
			throw new IllegalArgumentException("absolutePoint must have the same dimensions as this point");
		final double[] relativeCoords = new double[currentCoords.length];
		for(int coordIndex = 0; coordIndex < currentCoords.length; coordIndex++)
			relativeCoords[coordIndex] = currentCoords[coordIndex] - absoluteCoords[coordIndex];
		return new Vector(relativeCoords);
	}

	/**
	 * Adds the specified Vector to this Vector.
	 *
	 * @param pointToAdd Vector to add with this one.
	 * @return The resulting Vector after addition.
	 * @since 1.0
	 */
	public Vector add(final Vector pointToAdd)
	{
		if (pointToAdd == null)
			throw new IllegalArgumentException("pointToAdd can not be null!");
		final double currentCoords[] = this.coordinates.clone();
		final double addCoords[] = pointToAdd.coordinates.clone();
		if (addCoords.length != currentCoords.length)
			throw new IllegalArgumentException("pointToAdd must have the same dimensions as this point");
		final double[] relativeCoords = new double[currentCoords.length];
		for(int coordIndex = 0; coordIndex < currentCoords.length; coordIndex++)
			relativeCoords[coordIndex] = currentCoords[coordIndex] + addCoords[coordIndex];
		return new Vector(relativeCoords);
	}

	public Vector subtract(final Vector pointToAdd)
	{
		if (pointToAdd == null)
			throw new IllegalArgumentException("pointToAdd can not be null!");
		final double currentCoords[] = this.coordinates.clone();
		final double addCoords[] = pointToAdd.coordinates.clone();
		if (addCoords.length != currentCoords.length)
			throw new IllegalArgumentException("pointToAdd must have the same dimensions as this point");
		final double[] relativeCoords = new double[currentCoords.length];
		for(int coordIndex = 0; coordIndex < currentCoords.length; coordIndex++)
			relativeCoords[coordIndex] = currentCoords[coordIndex] - addCoords[coordIndex];
		return new Vector(relativeCoords);
	}

	public Vector multiply(final double scalar)
	{
		return this.setDistance(this.getDistance() * scalar);
	}

	public Vector divide(final double scalar)
	{
		return this.setDistance(this.getDistance() / scalar);
	}

	public Vector negate()
	{
		return this.multiply(-1.0);
	}

	public double dotProduct(final Vector operand)
	{
		if (this.coordinates.length != operand.coordinates.length)
			throw new IllegalArgumentException("operand must have the same number of dimensions as this vector.");
		double result = 0.0;
		for(int coordIndex = 0; coordIndex < this.coordinates.length; coordIndex++)
			result += this.coordinates[coordIndex] * operand.coordinates[coordIndex];
		return result;
	}

	public boolean isNaN()
	{
		for(final double coordinate : this.coordinates)
			if (Double.isNaN(coordinate))
				return true;
		return false;
	}

	public boolean isInfinite()
	{
		for(final double coordinate : this.coordinates)
			if (Double.isInfinite(coordinate))
				return true;
		return false;
	}

	/**
	 * A string representation of this Vector in cartesian coordinates.
	 *
	 * @return String representation of this point in cartesian coordinates.
	 * @since 1.0
	 */
	@Override
	public String toString()
	{
		final double currentCoords[] = this.coordinates.clone();
		final StringBuilder stringValue = new StringBuilder(currentCoords.length*5 + 2);
		stringValue.append('{');
		for(int dimension = 0; dimension < currentCoords.length; dimension++)
		{
			stringValue.append(currentCoords[dimension]);
			if (dimension < (currentCoords.length - 1))
				stringValue.append(',');
		}
		stringValue.append('}');
		return stringValue.toString();
	}

	/**
	 * A string representation of this Vector in Hyperspherical coordinates.
	 *
	 * @return String representation of this Vector in Hyperspherical coordinates.
	 * @since 1.0
	 */
	public String toStringHypersphere()
	{
		synchronized (this)
		{
			final StringBuilder retString = new StringBuilder(this.getDimensions() * 6);
			retString.append(this.getDistance());
			retString.append('@');
			for(int angleDimension = 1; angleDimension < this.getDimensions(); angleDimension++)
			{
				retString.append(this.getAngularComponent(angleDimension));
				if (angleDimension < (this.getDimensions() - 1))
					retString.append(',');
			}
			return retString.toString();
		}
	}

	/**
	 * Generates a hash code based on thie coordinate values.
	 *
	 * @return the hashcode representing this object.
	 * @since 2.0
	 */
	@Override
	public int hashCode()
	{
		final double[] currentCoords = this.coordinates.clone();
		int hashcode = 0;
		for(final double coordinate : currentCoords)
			hashcode += hashcode ^ Double.valueOf(coordinate).hashCode();
		return hashcode;
	}

	/**
	 * checks if another point is equals to this one.
	 *
	 * @return true if equals, false if not.
	 * @since 2.0
	 */
	@Override
	public boolean equals(final Object compareWithObject)
	{
		if (!(compareWithObject instanceof Vector))
			return false;
		final Vector compareWith = (Vector) compareWithObject;
		final double currentCoords[] = this.coordinates;
		final double otherCoords[] = compareWith.coordinates;
		if (currentCoords.length != otherCoords.length)
			return false;
		for(int dimension = 0; dimension <= currentCoords.length; dimension++)
			if (currentCoords[dimension] != otherCoords[dimension])
				return false;
		return true;
	}
}
