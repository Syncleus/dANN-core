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
package com.syncleus.tests.dann.graphicalmodel.bayesian;

import com.syncleus.dann.graphicalmodel.bayesian.StateEvidence;
import java.util.Map;
import org.junit.*;

public class TestStateEvidence
{
	private static enum TestEnum
	{
		TOP,BOTTOM;
	}

	@Test
	public void testPercentage()
	{
		StateEvidence evidence = new StateEvidence();
		Map<Enum,Integer> evidenceMap = evidence;
		evidenceMap.put(TestEnum.TOP, 700);
		evidenceMap.put(TestEnum.BOTTOM, 300);
		Assert.assertTrue("top percentage: " + evidence.getPercentage(TestEnum.TOP), Math.abs(evidence.getPercentage(TestEnum.TOP) - 0.7) < 0.0001);
		Assert.assertTrue("bottom percentage: " + evidence.getPercentage(TestEnum.BOTTOM), Math.abs(evidence.getPercentage(TestEnum.BOTTOM) - 0.3) < 0.0001);
	}
}
