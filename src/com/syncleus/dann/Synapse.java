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
package com.syncleus.dann;

/**
 * The synapse acts as a bridge between connected neurons. It is also where the
 * connection weights are stores and manipulated.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.ProcessingUnit
 */
public class Synapse implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * The outgoing neuron connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private NeuronProcessingUnit destination;
    /**
     * The incomming neuron connection.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private NeuronProcessingUnit source;
    /**
     * The current weight of the synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double weight = 0.0;
    /**
     * The current output of the synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double output = 0.0;
    /**
     * The current input from the synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double input = 0.0;
    /**
     * The current synapse's deltaTrain<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double differential = 0.0;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new instance of Synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param sourceToSet The incomming neuron connection.
     * @param destinationToSet The outgoing neuron connection.
     * @param initialWeight The initial weight of the synapse
     */
    public Synapse(NeuronProcessingUnit sourceToSet, NeuronProcessingUnit destinationToSet, double initialWeight)
    {
        this.destination = destinationToSet;
        this.source = sourceToSet;
        this.weight = initialWeight;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Link Traversal">
    /**
     * Obtains the incomming neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return The source neuron.
     */
    public NeuronProcessingUnit getSource()
    {
        return this.source;
    }



    /**
     * Obtains the outgoing neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @return The destination neuron.
     */
    public NeuronProcessingUnit getDestination()
    {
        return this.destination;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">
    /**
     * learns the new weight based on the current training set<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NeuronProcessingUnit#calculateDeltaTrain
     */
    public void learnWeight(double deltaTrainToSet, double learningRate)
    {
        this.weight += learningRate * this.input * deltaTrainToSet;
        
        this.source.differentialActivity -= this.differential;
        this.differential = deltaTrainToSet * this.weight;
        this.source.differentialActivity += this.differential;
    }



    /**
     * Set the current input for the synapse<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.NeuronProcessingUnit#propogate
     * @param newInput The new input value to set.
     */
    public void setInput(double newInput)
    {
        this.input = newInput;
        double newOutput = this.input * this.weight;
        double deltaOutput = newOutput - this.output;
        this.output = newOutput;
        
        this.destination.activity += deltaOutput;
    }



    // </editor-fold>
}
