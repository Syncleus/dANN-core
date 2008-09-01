/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.syncleus.dann.activation;

public interface ActivationFunction
{
    public double activate(double activity);
    public double activateDerivative(double activity);
}
