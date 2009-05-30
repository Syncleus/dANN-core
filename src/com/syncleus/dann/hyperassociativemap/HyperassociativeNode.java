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
import java.util.*;


public class HyperassociativeNode implements Serializable
{
	// <editor-fold defaultstate="collapsed" desc="Attributes">
    private HyperassociativeMap network;
    private Hashtable<HyperassociativeNode, Double> weightedNeighbors = new Hashtable<HyperassociativeNode, Double>();
    private Hyperpoint location;
    private double equilibriumDistance = 1.0;
    private double learningRate = 0.004;
    private double maximumDistance = 50.0;
    private static Random random = new Random();
	// </editor-fold>


	// <editor-fold defaultstate="collapsed" desc="Constructors">
	public HyperassociativeNode(HyperassociativeMap network, int dimensions, double learningRate, double maximumDistance, double equilibriumDistance)
	{
        this(network, dimensions, learningRate, maximumDistance);
		this.equilibriumDistance = equilibriumDistance;
	}

	public HyperassociativeNode(HyperassociativeMap network, int dimensions, double learningRate, double maximumDistance)
	{
		this(network, dimensions, learningRate);
		this.maximumDistance = maximumDistance;
	}

	public HyperassociativeNode(HyperassociativeMap network, int dimensions, double learningRate)
	{
		this(network, dimensions);
		this.learningRate = learningRate;
	}

    public HyperassociativeNode(HyperassociativeMap network, int dimensions)
    {
        if (network == null)
            throw new NullPointerException("network can not be null!");

        this.location = new Hyperpoint(dimensions);
        this.network = network;
    }

	public HyperassociativeNode(HyperassociativeMap network, Hyperpoint location, double learningRate, double maximumDistance, double equilibriumDistance)
	{
		this(network, location, learningRate, maximumDistance);
		this.equilibriumDistance = equilibriumDistance;
	}

	public HyperassociativeNode(HyperassociativeMap network, Hyperpoint location, double learningRate, double maximumDistance)
	{
		this(network, location, learningRate);
		this.maximumDistance = maximumDistance;
	}

	public HyperassociativeNode(HyperassociativeMap network, Hyperpoint location, double learningRate)
	{
		this(network, location);
		this.learningRate = learningRate;
	}



    public HyperassociativeNode(HyperassociativeMap network, Hyperpoint location)
    {
        if (location == null)
            throw new NullPointerException("location can not be null!");
        if (network == null)
            throw new NullPointerException("network can not be null!");

        this.location = location;
        this.network = network;
    }
	// </editor-fold>



    public void associate(HyperassociativeNode newNeighbor, double newWeight)
    {
        if (newNeighbor == null)
            throw new NullPointerException("newNeighbor can not be null!");
        if (this == newNeighbor)
            throw new IllegalArgumentException("an AssociativeNode can not associate with itself");

        this.weightedNeighbors.put(newNeighbor, newWeight);
    }



    public void dissociate(HyperassociativeNode neighbor)
    {
        this.weightedNeighbors.remove(neighbor);
    }



    public void dissociateAll()
    {
        this.weightedNeighbors.clear();
    }



    public Set<HyperassociativeNode> getNeighbors()
    {
        return Collections.unmodifiableSet(this.weightedNeighbors.keySet());
    }



    public double getNeighborsWeight(HyperassociativeNode neighbor) throws NeighborNotFoundException
    {
        if (neighbor == null)
            throw new NullPointerException("neighbor can not be null!");

        Double weight = this.weightedNeighbors.get(neighbor);

        if (weight == null)
            throw new NeighborNotFoundException("neighbor is not recognized as a current neighbor");

        return weight.doubleValue();
    }



    public static Hyperpoint randomCoordinates(int dimensions)
    {
        double[] randomCoords = new double[dimensions];
        for (int randomCoordsIndex = 0; randomCoordsIndex < dimensions; randomCoordsIndex++)
            randomCoords[randomCoordsIndex] = (random.nextDouble() * 2.0) - 1.0;

        return new Hyperpoint(randomCoords);
    }



    public void align()
    {
        //calculate equilibrium with neighbors
        Set<HyperassociativeNode> neighbors = this.weightedNeighbors.keySet();

        Hyperpoint compositeVector = new Hyperpoint(this.location.getDimensions());
        for (HyperassociativeNode neighbor : neighbors)
        {
            Hyperpoint neighborVector = neighbor.location.calculateRelativeTo(this.location);
            double neighborEquilibrium = (equilibriumDistance / this.weightedNeighbors.get(neighbor).doubleValue());
            if (neighborVector.getDistance() > neighborEquilibrium)
			{
				double newDistance = Math.pow(neighborVector.getDistance() - neighborEquilibrium, 2.0);
				if(Math.abs(newDistance) > Math.abs(neighborVector.getDistance() - neighborEquilibrium))
					newDistance = neighborVector.getDistance() - neighborEquilibrium;
                neighborVector.setDistance(newDistance);
			}
            else
			{
				double newDistance = -1.0 * atanh((neighborEquilibrium - neighborVector.getDistance()) / neighborEquilibrium);
				if( Math.abs(newDistance) > Math.abs(neighborEquilibrium - neighborVector.getDistance()))
					newDistance = -1.0 * (neighborEquilibrium - neighborVector.getDistance());
                neighborVector.setDistance(newDistance);
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
		//this line is a hack that makes it work, not sure why.
		compositeVector.setCoordinate(0.0d, compositeVector.getDimensions());

		
//        if( Math.abs(compositeVector.getDistance()) > (maximumDistance/10.0) )
//		{
//            compositeVector.setDistance(Math.signum(compositeVector.getDistance())*(maximumDistance/10.0));
//			compositeVector.setCoordinate(0.0d, compositeVector.getDimensions());
//		}

        this.location = this.location.add(compositeVector);

		//if this node is outside of the maximum distance allowed then readjust its distance
//        if( Math.abs(this.location.getDistance()) > (maximumDistance) )
//		{
//			 if( Math.abs(this.location.getDistance()) >= maximumDistance )
//			 {
//				this.location.setDistance(Math.signum(this.location.getDistance())*(maximumDistance*0.9d));
//				this.location.setCoordinate(0.0d, this.location.getDimensions());
//			 }
//		}
    }

	void recenter(Hyperpoint center)
	{
		this.location = this.location.calculateRelativeTo(center);
	}



    private static double atanh(double value)
    {
        return 0.5 * Math.log(Math.abs((value + 1.0) / (1.0 - value)));
    }



    protected HyperassociativeMap getNetwork()
    {
        return network;
    }



    public Hyperpoint getLocation()
    {
        return new Hyperpoint(this.location);
    }
}
