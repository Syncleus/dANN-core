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


public class HyperassociativeNode implements Serializable
{
	// <editor-fold defaultstate="collapsed" desc="Attributes">

    private AbstractHyperassociativeMap network;
    private Hashtable<HyperassociativeNode, Double> weightedNeighbors = new Hashtable<HyperassociativeNode, Double>();
    private Hyperpoint location;
    private double equilibriumDistance = 1.0;
    private double learningRate = 0.004;
    private static Random random = new Random();

	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Constructors">

	/**
	 * Initializes an Hyperassociative node with the specified properties.
	 *
	 *
	 * @param network The network this node will belong to.
	 * @param learningRate The learning rate for this node.
	 * @param equilibriumDistance The equilibrium distance between this node
	 * and any nodes it associates with.
	 * @since 1.0
	 */
	public HyperassociativeNode(AbstractHyperassociativeMap network, double learningRate, double equilibriumDistance)
	{
        this(network, learningRate);
		this.equilibriumDistance = equilibriumDistance;
	}

	/**
	 * Initializes an Hyperassociative node with the specified properties.
	 *
	 *
	 * @param network The network this node will belong to.
	 * @param learningRate The learning rate for this node.
	 * @since 1.0
	 */
	public HyperassociativeNode(AbstractHyperassociativeMap network, double learningRate)
	{
		this(network);
		this.learningRate = learningRate;
	}

	/**
	 * Initializes an Hyperassociative node with the specified properties.
	 *
	 *
	 * @param network The network this node will belong to.
	 * @since 1.0
	 */
    public HyperassociativeNode(AbstractHyperassociativeMap network)
    {
        if (network == null)
            throw new NullPointerException("network can not be null!");

        this.location = new Hyperpoint(network.getDimensions());
        this.network = network;
    }

	/**
	 * Initializes an Hyperassociative node with the specified properties.
	 *
	 *
	 * @param network The network this node will belong to.
	 * @param location The initial location of this node.
	 * @param learningRate The learning rate for this node.
	 * @param equilibriumDistance The equilibrium distance between this node
	 * and any nodes it associates with.
	 * @since 1.0
	 */
	public HyperassociativeNode(AbstractHyperassociativeMap network, Hyperpoint location, double learningRate, double equilibriumDistance)
	{
		this(network, location, learningRate);
		this.equilibriumDistance = equilibriumDistance;
	}

	/**
	 * Initializes an Hyperassociative node with the specified properties.
	 *
	 *
	 * @param network The network this node will belong to.
	 * @param location The initial location of this node.
	 * @param learningRate The learning rate for this node.
	 * @since 1.0
	 */
	public HyperassociativeNode(AbstractHyperassociativeMap network, Hyperpoint location, double learningRate)
	{
		this(network, location);
		this.learningRate = learningRate;
	}


	/**
	 * Initializes an Hyperassociative node with the specified properties.
	 *
	 *
	 * @param network The network this node will belong to.
	 * @param location The initial location of this node.
	 * @since 1.0
	 */
    public HyperassociativeNode(AbstractHyperassociativeMap network, Hyperpoint location)
    {
        if (location == null)
            throw new NullPointerException("location can not be null!");
        if (network == null)
            throw new NullPointerException("network can not be null!");

        this.location = location;
        this.network = network;
    }
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Accessors">

	/**
	 * Gets the Map this node belongs to.
	 *
	 *
	 * @return The map this node belongs to.
	 * @since 1.0
	 */
    protected AbstractHyperassociativeMap getNetwork()
    {
        return network;
    }

	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Association">

	/**
	 * Associates this node with the specified node at the specified weight.
	 *
	 *
	 * @param newNeighbor Node to associate to.
	 * @param newWeight Weight of association.
	 * @since 1.0
	 */
    public void associate(HyperassociativeNode newNeighbor, double newWeight)
    {
        if (newNeighbor == null)
            throw new NullPointerException("newNeighbor can not be null!");
        if (this == newNeighbor)
            throw new IllegalArgumentException("an AssociativeNode can not associate with itself");

        this.weightedNeighbors.put(newNeighbor, newWeight);
    }



	/**
	 * Dissociates from the specified node.
	 *
	 *
	 * @param neighbor Node to dissociate from.
	 * @since 1.0
	 */
    public void dissociate(HyperassociativeNode neighbor)
    {
        this.weightedNeighbors.remove(neighbor);
    }



	/**
	 * Dissociates from all nodes.
	 *
	 *
	 * @since 1.0
	 */
    public void dissociateAll()
    {
        this.weightedNeighbors.clear();
    }



	/**
	 * Gets all nodes currently associated to.
	 *
	 *
	 * @return An unmodifiable set of associated nodes.
	 * @since 1.0
	 */
    public Set<HyperassociativeNode> getNeighbors()
    {
        return Collections.unmodifiableSet(this.weightedNeighbors.keySet());
    }



	/**
	 * Gets the weight of the specified associated node.
	 *
	 *
	 * @param neighbor Node to get the weight for.
	 * @return The weight of the specified node.
	 * @throws com.syncleus.dann.hyperassociativemap.NeighborNotFoundException
	 * thrown if neighbor is null.
	 * @since 1.0
	 */
    public double getNeighborsWeight(HyperassociativeNode neighbor) throws NeighborNotFoundException
    {
        if (neighbor == null)
            throw new NullPointerException("neighbor can not be null!");

        Double weight = this.weightedNeighbors.get(neighbor);

        if (weight == null)
            throw new NeighborNotFoundException("neighbor is not recognized as a current neighbor");

        return weight.doubleValue();
    }



	/**
	 * Obtains a Hyperpoint with random coordinates for the specified number of
	 * dimensions.
	 *
	 *
	 * @param dimensions Number of dimensions for the random Hyperpoint
	 * @return New random Hyperpoint
	 * @since 1.0
	 */
    public static Hyperpoint randomCoordinates(int dimensions)
    {
        double[] randomCoords = new double[dimensions];
        for (int randomCoordsIndex = 0; randomCoordsIndex < dimensions; randomCoordsIndex++)
            randomCoords[randomCoordsIndex] = (random.nextDouble() * 2.0) - 1.0;

        return new Hyperpoint(randomCoords);
    }

	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Alignment">

	/**
	 * Aligns this node by one step against all nodes associated or not.
	 *
	 *
	 * @since 1.0
	 */
    public void align()
    {
        //calculate equilibrium with neighbors
        Set<HyperassociativeNode> neighbors = this.weightedNeighbors.keySet();

        Hyperpoint compositeVector = new Hyperpoint(this.location.getDimensions());
        for (HyperassociativeNode neighbor : neighbors)
        {
            Hyperpoint neighborVector = neighbor.location.calculateRelativeTo(this.location);
            double neighborEquilibrium = (equilibriumDistance / this.weightedNeighbors.get(neighbor).doubleValue());
            if (Math.abs(neighborVector.getDistance()) > neighborEquilibrium)
			{
				double newDistance = Math.pow(Math.abs(neighborVector.getDistance()) - neighborEquilibrium, 2.0);
				if(Math.abs(newDistance) > Math.abs(Math.abs(neighborVector.getDistance()) - neighborEquilibrium))
					newDistance = Math.abs(Math.abs(neighborVector.getDistance()) - neighborEquilibrium);
                neighborVector.setDistance(Math.signum(neighborVector.getDistance()) * newDistance);
			}
            else
			{
				double newDistance = -1.0 * atanh((neighborEquilibrium - Math.abs(neighborVector.getDistance())) / neighborEquilibrium);
				if( Math.abs(newDistance) > Math.abs(neighborEquilibrium - Math.abs(neighborVector.getDistance())))
					newDistance = -1.0 * (neighborEquilibrium - Math.abs(neighborVector.getDistance()));
                neighborVector.setDistance(Math.signum(neighborVector.getDistance()) * newDistance);
			}

            compositeVector = compositeVector.add(neighborVector);
        }

        //calculate repulsion with all non-neighbors
        for (HyperassociativeNode node : this.network.getNodes())
            if ((neighbors.contains(node) == false)&&(node != this)&&(node.weightedNeighbors.keySet().contains(this) == false) )
            {
                Hyperpoint nodeVector = node.location.calculateRelativeTo(this.location);
				double newDistance = -1.0/Math.pow(nodeVector.getDistance(), 2.0);
				if(Math.abs(newDistance) > Math.abs(this.equilibriumDistance))
					newDistance = -1.0 * this.equilibriumDistance;
                nodeVector.setDistance(newDistance);
                
                compositeVector = compositeVector.add(nodeVector);
            }

        compositeVector.setDistance(compositeVector.getDistance() * learningRate);

        this.location = this.location.add(compositeVector);
    }

	/**
	 * Centers this node to a new center point (origin).
	 *
	 *
	 * @param center The new origin for this node.
	 * @since 1.0
	 */
	void recenter(Hyperpoint center)
	{
		this.location = this.location.calculateRelativeTo(center);
	}


    private static double atanh(double value)
    {
        return 0.5 * Math.log(Math.abs((value + 1.0) / (1.0 - value)));
    }



	/**
	 * Gets the current Hyperpoint location for this node.
	 *
	 *
	 * @return The current Hyperpoint location for this node.
	 * @since 1.0
	 */
    public Hyperpoint getLocation()
    {
        return new Hyperpoint(this.location);
    }

	// </editor-fold>
}
