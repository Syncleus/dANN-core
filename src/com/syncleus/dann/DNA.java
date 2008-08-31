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

import java.util.Random;


/**
 * Largly under developed this class will contain or generate a basic list of
 * properties used by the individual neurons, and layers. This will be the Class
 * genetic algorithms should inherit from.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class DNA implements java.io.Serializable
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * The learning rate is used by neurons to determine how influential each
     * training set will be on its current state.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Synapse#learnWeight
     */
    public double learningRate = 0.001;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new instance of DNA with default values.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public DNA()
    {
    }    // </editor-fold>
}
