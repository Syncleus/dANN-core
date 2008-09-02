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
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;


public class AssociativeNode implements Serializable
{
    private AssociativeMap network;
    private Hashtable<AssociativeNode,Double> weightedNeighbors = new Hashtable<AssociativeNode,Double>();
    private Point location;
    private static final double EQUILIBRIUM_DISTANCE = 10.0;
    private static final double LEARNING_RATE = 0.0001;
    private static final double MAXIMUM_DISTANCE = 100.0;
    
    public AssociativeNode(AssociativeMap network, int dimentions)
    {
        if(network == null)
            throw new NullPointerException("network can not be null!");
        
        this.location = new Point(dimentions);
        this.network = network;
    }
    
    public AssociativeNode(AssociativeMap network, Point location)
    {
        if(location == null)
            throw new NullPointerException("location can not be null!");
        if(network == null)
            throw new NullPointerException("network can not be null!");
        
        this.location = location;
        this.network = network;
    }
    
    public void associate(AssociativeNode newNeighbor, double newWeight)
    {
        if(newNeighbor == null)
            throw new NullPointerException("newNeighbor can not be null!");
        
        this.weightedNeighbors.put(newNeighbor, newWeight);
    }
    
    public void dissociate(AssociativeNode neighbor)
    {
        this.weightedNeighbors.remove(neighbor);
    }
    
    public void dissociateAll()
    {
        this.weightedNeighbors.clear();
    }
    
    public Set<AssociativeNode> getNeighbors()
    {
        return Collections.unmodifiableSet(this.weightedNeighbors.keySet());
    }
    
    public double getNeighborsWeight(AssociativeNode neighbor) throws NeighborNotFoundException
    {
        if(neighbor == null)
            throw new NullPointerException("neighbor can not be null!");
        
        Double weight = this.weightedNeighbors.get(neighbor);
        
        if(weight == null)
            throw new NeighborNotFoundException("neighbor is not recognized as a current neighbor");
        
        return weight.doubleValue();
    }
    
    public void align()
    {
        Set<AssociativeNode> neighbors = this.weightedNeighbors.keySet();
        Point compositeVector = new Point(this.location.getDimensions());
        for(AssociativeNode neighbor : neighbors)
        {
            Point neighborVector = neighbor.location.calculateRelativeTo(this.location);
            if(neighborVector.getDistance() > EQUILIBRIUM_DISTANCE)
                neighborVector.setDistance(neighborVector.getDistance() - EQUILIBRIUM_DISTANCE);
            else
                neighborVector.setDistance(neighborVector.getDistance() * (-1 * Math.pow(EQUILIBRIUM_DISTANCE,2)));
            
            compositeVector = compositeVector.add(neighborVector);
        }
        compositeVector.setDistance(compositeVector.getDistance() * LEARNING_RATE);
        
        for(int dimention = 1; dimention <= compositeVector.getDimensions(); dimention++)
        {
            if(compositeVector.getCoordinate(dimention) >= MAXIMUM_DISTANCE)
                compositeVector.setCoordinate(MAXIMUM_DISTANCE, dimention);
            else if (compositeVector.getCoordinate(dimention) <= (-1.0*MAXIMUM_DISTANCE))
                compositeVector.setCoordinate(-1.0*MAXIMUM_DISTANCE, dimention);
        }
        
        this.location = this.location.add(compositeVector);
    }



    protected AssociativeMap getNetwork()
    {
        return network;
    }



    public Point getLocation()
    {
        return new Point(this.location);
    }
}
