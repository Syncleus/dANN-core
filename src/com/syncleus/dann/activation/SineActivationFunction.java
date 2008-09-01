package com.syncleus.dann.activation;


public class SineActivationFunction implements ActivationFunction
{
    public double activate(double activity)
    {
        return Math.sin(activity);
    }
    
    public double activateDerivative(double activity)
    {
        return Math.cos(activity);
    }
}