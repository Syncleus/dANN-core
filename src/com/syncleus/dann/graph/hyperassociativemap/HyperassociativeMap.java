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
package com.syncleus.dann.graph.hyperassociativemap;
import com.syncleus.dann.math.Hyperpoint;
import java.io.Serializable;
import java.util.*;


/**
 * Represents a collection of interconnected hyperassociative map nodes.
 *
 *
 * @author Syncleus, Inc.
 * @since 1.0
 *
 */
public abstract class HyperassociativeMap implements Serializable
{
	/**
	 * HashSet of all the nodes in this map.
	 *
	 * @since 1.0
	 */
    protected HashSet<HyperassociativeNode> nodes = new HashSet<HyperassociativeNode>();
	private int dimensions;

	/**
	 * Initializes a HyperassociativeMap of the specified dimensions.
	 *
	 *
	 * @param dimensions The number of dimensions for this map.
	 * @since 1.0
	 */
	public HyperassociativeMap(int dimensions)
	{
		this.dimensions = dimensions;
	}

	/**
	 * Gets the number of dimensions for this map.
	 *
	 *
	 * @return The number of dimensions for this map.
	 * @since 1.0
	 */
	public int getDimensions()
	{
		return this.dimensions;
	}

	/**
	 * Gets all the nodes contained within this map.
	 *
	 *
	 * @return An unmodifiable Set of all the nodes in this map.
	 * @since 1.0
	 */
    public Set<HyperassociativeNode> getNodes()
    {
        return Collections.unmodifiableSet(this.nodes);
    }

	/**
	 * Aligns all the nodes in this map by a single step.
	 *
	 *
	 * @since 1.0
	 */
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