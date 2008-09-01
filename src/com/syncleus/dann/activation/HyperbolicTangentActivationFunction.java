package com.syncleus.dann.activation;

public class HyperbolicTangentActivationFunction implements ActivationFunction
{
    public double activate(double activity)
    {
        return Math.tanh(activity);
    }
    
    public double activateDerivative(double activity)
    {
        return 1.0 - Math.pow(this.activate(activity), 2.0);
    }
}
