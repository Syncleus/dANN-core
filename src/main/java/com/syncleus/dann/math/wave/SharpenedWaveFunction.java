/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.math.wave;

public class SharpenedWaveFunction extends WaveFunction
{
	public SharpenedWaveFunction(final SharpenedWaveFunction copy)
	{
		super(copy);
	}

	public SharpenedWaveFunction()
	{
		super(new String[]{"form"});
	}

	public SharpenedWaveFunction(final String[] additionalParameters)
	{
		super(combineLabels(new String[]{"form"}, additionalParameters));
	}

	public void setForm(final double form)
	{
		this.setParameter(this.getParameterNameIndex("form"), form);
	}

	public double getForm()
	{
		return this.getParameter(this.getParameterNameIndex("form"));
	}

	@Override
	public double calculate()
	{
		if( super.calculate() == 0.0 )
			return 0.0;
		if( this.getAmplitude() == 0.0 )
			return 0.0;

		return (super.calculate() / Math.abs(super.calculate())) * Math.abs(Math.pow(Math.abs(super.calculate() / this.getAmplitude()), this.getForm()) * this.getAmplitude());
	}

	@Override
	public SharpenedWaveFunction clone()
	{
		return (SharpenedWaveFunction) super.clone();
	}

	@Override
	public String toString()
	{
		return this.toString("x");
	}

	@Override
	public String toString(final String xName)
	{
		return '(' + super.toString(xName) + "/|" + super.toString(xName) + "|) * |(|" + super.toString(xName) + ")/amplitude|^form| * amplitude";
	}
}
