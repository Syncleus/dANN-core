package com.syncleus.dann.activation;

public class GausianActivationFunction implements ActivationFunction
{
    public double activate(double activity)
    {
        return Math.pow(Math.E, (-1.0 * Math.pow(activity,2) ));
    }
    
    public double activateDerivative(double activity)
    {
        return (-2.0 * Math.log10(Math.E) * activity) / Math.pow(Math.E, Math.pow(activity,2));
    }
}
