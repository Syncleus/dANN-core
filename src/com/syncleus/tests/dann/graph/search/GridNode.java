/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann.graph.search;

import com.syncleus.dann.graph.Weighted;
import com.syncleus.dann.math.Vector;

public class GridNode extends Vector implements Weighted
{
	private double weight;

	public GridNode(int x, int y, double weight)
	{
		super((double)x, (double)y);
		this.weight = weight;
	}

	public double getWeight()
	{
		return this.weight;
	}

	public int getX()
	{
		return (int) this.getCoordinate(1);
	}

	public int getY()
	{
		return (int) this.getCoordinate(2);
	}

	@Override
	public int hashCode()
	{
		return (this.getX()*this.getY()) + this.getY();
	}

	@Override
	public boolean equals(Object compareToObj)
	{
		if(!(compareToObj instanceof GridNode))
			return false;

		GridNode compareTo = (GridNode) compareToObj;
		return ((compareTo.getX() == this.getX())&&(compareTo.getY() == this.getY()));
	}
}
