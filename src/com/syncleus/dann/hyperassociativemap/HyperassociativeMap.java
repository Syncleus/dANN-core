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
package com.syncleus.dann.hyperassociativemap;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public abstract class HyperassociativeMap implements Serializable
{
    protected HashSet<HyperassociativeNode> nodes = new HashSet<HyperassociativeNode>();
	private int dimensions;

	public HyperassociativeMap(int dimensions)
	{
		this.dimensions = dimensions;
	}

	public int getDimensions()
	{
		return this.dimensions;
	}
    
    public Set<HyperassociativeNode> getNodes()
    {
        return Collections.unmodifiableSet(this.nodes);
    }
    
    public void align()
    {
		Hyperpoint center = new Hyperpoint(this.dimensions);
        for(HyperassociativeNode node : nodes)
		{
            node.align();
			for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
				center.setCoordinate(center.getCoordinate(dimensionIndex) + node.getLocation().getCoordinate(dimensionIndex), dimensionIndex);
		}

		for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
			center.setCoordinate(center.getCoordinate(dimensionIndex)/((double)this.nodes.size()),dimensionIndex);

		for(HyperassociativeNode node : nodes)
		{
			node.recenter(center);
		}
    }
}
