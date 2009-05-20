/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.syncleus.tests.dann.associativemap;

import com.syncleus.dann.associativemap.*;

public class LayeredAssociativeMap extends AssociativeMap
{
    private AssociativeNode layeredNodes[][];
    private static final int NODES_PER_LAYER = 16;
    
    LayeredAssociativeMap(int layers)
    {
        this.layeredNodes = new AssociativeNode[layers][NODES_PER_LAYER];
        
        //create the nodes
        for(int layerIndex = 0; layerIndex < layers; layerIndex++)
            for(int nodeIndex = 0; nodeIndex < NODES_PER_LAYER; nodeIndex++)
            {
                this.layeredNodes[layerIndex][nodeIndex] = new AssociativeNode(this, AssociativeNode.randomCoordinates(3));
                this.nodes.add(this.layeredNodes[layerIndex][nodeIndex]);
            }
        
        //connect the nodes
        for(int layerIndex = 0; layerIndex < layers; layerIndex++)
        {
            for(int nodeIndex = 0; nodeIndex < NODES_PER_LAYER; nodeIndex++)
            {
                AssociativeNode currentNode = this.layeredNodes[layerIndex][nodeIndex];
                for(int toNodeIndex = 0; toNodeIndex < NODES_PER_LAYER; toNodeIndex++)
                {
                    if(layerIndex < (layers-1))
                    {
                        currentNode.associate(this.layeredNodes[layerIndex+1][toNodeIndex], 1.0);
                        this.layeredNodes[layerIndex+1][toNodeIndex].associate(currentNode, 1.0);
                    }
                    else
                    {
                        currentNode.associate(this.layeredNodes[0][toNodeIndex], 1.0);
                        this.layeredNodes[0][toNodeIndex].associate(currentNode, 1.0);
                    }
                }
            }
        }
        
        //connect the last layer to itself
    }


	public AssociativeNode[][] getLayers()
	{
		return (AssociativeNode[][]) this.layeredNodes.clone();
	}
}
