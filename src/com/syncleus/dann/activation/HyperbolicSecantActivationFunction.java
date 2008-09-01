package com.syncleus.dann.activation;

public class HyperbolicSecantActivationFunction implements ActivationFunction
{
    public double activate(double activity)
    {
        return 1.0/Math.cosh(activity);
    }
    
    public double activateDerivative(double activity)
    {
        return -1.0 * Math.tanh(activity) * this.activate(activity);
    }
}
