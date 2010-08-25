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
package com.syncleus.dann.neural.som.brain;

import java.util.Map;
import java.util.Set;
import com.syncleus.dann.math.Vector;
import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.som.*;

public interface SomBrain<IN extends SomInputNeuron, ON extends SomOutputNeuron, N extends SomNeuron, S extends Synapse<N>> extends Brain<IN,ON,N,S>
{
	void createOutput(final Vector position);
	Set<Vector> getPositions();
	double getOutput(final Vector position);
	Vector getBestMatchingUnit();
	Vector getBestMatchingUnit(final boolean train);
	int getIterationsTrained();
	int getInputCount();
	void setInput(final int inputIndex, final double inputValue);
	double getInput(final int index);
	Map<Vector, double[]> getOutputWeightVectors();
}
