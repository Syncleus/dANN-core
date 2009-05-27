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
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;


public class HyperassociativeNode implements Serializable
{
    private HyperassociativeMap network;
    private Hashtable<HyperassociativeNode, Double> weightedNeighbors = new Hashtable<HyperassociativeNode, Double>();
    private Hyperpoint location;
    private static final double EQUILIBRIUM_DISTANCE = 1.0;
    private static final double LEARNING_RATE = 0.004;
    private static final double MAXIMUM_DISTANCE = 10.0;
    private static Random random = new Random();



    public HyperassociativeNode(HyperassociativeMap network, int dimensions)
    {
        if (network == null)
            throw new NullPointerException("network can not be null!");

        this.location = new Hyperpoint(dimensions);
        this.network = network;
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



    public void associate(HyperassociativeNode newNeighbor, double newWeight)
    {
        if (newNeighbor == null)
            throw new NullPointerException("newNeighbor can not be null!");
        if (this == newNeighbor)
            throw new IllegalArgumentException("an AssociativeNode can not associate with itself");

//        System.out.println("adding new neighbor");
        this.weightedNeighbors.put(newNeighbor, newWeight);
    }



    public void dissociate(HyperassociativeNode neighbor)
    {
//        System.out.println("warning, dissasociating");
        this.weightedNeighbors.remove(neighbor);
    }



    public void dissociateAll()
    {
//        System.out.println("warning, dissasociating all");
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
//        if( this.random.nextInt(10000) == 0)
//            this.location = randomCoordinates(this.location.getDimensions());

        //calculate equilibrium with neighbors
        Set<HyperassociativeNode> neighbors = this.weightedNeighbors.keySet();
//        if(neighbors.size() > 0 )
//            System.out.println("has " + neighbors.size() + " neighbors");
        Hyperpoint compositeVector = new Hyperpoint(this.location.getDimensions());
        for (HyperassociativeNode neighbor : neighbors)
        {
            Hyperpoint neighborVector = neighbor.location.calculateRelativeTo(this.location);
            double neighborEquilibrium = (EQUILIBRIUM_DISTANCE / this.weightedNeighbors.get(neighbor).doubleValue());
            if (neighborVector.getDistance() > neighborEquilibrium)
                neighborVector.setDistance(Math.pow(neighborVector.getDistance() - neighborEquilibrium, 2.0));
            //neighborVector.setDistance(neighborVector.getDistance() - neighborEquilibrium);
            else
                neighborVector.setDistance(-1.0 * atanh((neighborEquilibrium - neighborVector.getDistance()) / neighborEquilibrium));
            //neighborVector.setDistance(neighborVector.getDistance() * (-1 * Math.pow(neighborEquilibrium,2)));

            compositeVector = compositeVector.add(neighborVector);
        }

        //calculate repulsion with all non-neighbors
        for (HyperassociativeNode node : this.network.getNodes())
            if ((neighbors.contains(node) == false)&&(node != this)&&(node.weightedNeighbors.keySet().contains(this) == false) )
            {
                Hyperpoint nodeVector = node.location.calculateRelativeTo(this.location);
//                if (nodeVector.getDistance() < EQUILIBRIUM_DISTANCE)
                    nodeVector.setDistance(-1.0/Math.pow(nodeVector.getDistance(), 2.0));
                    //nodeVector.setDistance(-1.0 * atanh((EQUILIBRIUM_DISTANCE - nodeVector.getDistance()) / EQUILIBRIUM_DISTANCE));
                
                compositeVector = compositeVector.add(nodeVector);
            }

        compositeVector.setDistance(compositeVector.getDistance() * LEARNING_RATE);
        
        if( Math.abs(compositeVector.getDistance()) > (MAXIMUM_DISTANCE/4.0) )
            compositeVector.setDistance(Math.signum(compositeVector.getDistance())*(MAXIMUM_DISTANCE/4.0));

        for (int dimension = 1; dimension <= compositeVector.getDimensions(); dimension++)
            if (compositeVector.getCoordinate(dimension) >= MAXIMUM_DISTANCE)
                compositeVector.setCoordinate(MAXIMUM_DISTANCE, dimension);
            else if (compositeVector.getCoordinate(dimension) <= (-1.0 * MAXIMUM_DISTANCE))
                compositeVector.setCoordinate(-1.0 * MAXIMUM_DISTANCE, dimension);

        this.location = this.location.add(compositeVector);
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
