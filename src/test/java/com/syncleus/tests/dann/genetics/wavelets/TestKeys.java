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
package com.syncleus.tests.dann.genetics.wavelets;

import com.syncleus.dann.genetics.wavelets.*;
import org.junit.*;

public class TestKeys
{
	private static final String[] SUCCESS_SIGNALS =
			{
					"0",
					"1",
					"101x11x0xxx0110",
					"1100001xxx1xx1101xxx0"
			};
	private static final String[] SUCCESS_RECEPTORS =
			{
					"0",
					"1",
					"11x0",
					"1xx1"
			};
	private static final String[] FAILURE_SIGNALS =
			{
					"1",
					"0",
					"101x11x0xxx0110",
					"1001xxxx10xx11xx01",
					"1111xxxx11xx11xx11"
			};
	private static final String[] FAILURE_RECEPTORS =
			{
					"0",
					"1",
					"111x01",
					"111xx1",
					"111xx1"
			};

	@Test
	public void testSuccessfulKeyPairs()
	{
		for(int index = 0; index < SUCCESS_SIGNALS.length; index++)
		{
			final SignalKey signal = new SignalKey(SUCCESS_SIGNALS[index]);
			final ReceptorKey receptor = new ReceptorKey(SUCCESS_RECEPTORS[index]);

			Assert.assertTrue("signal at index " + index + " does not bind.  Signal: " + signal + ", Receptor: " + receptor, receptor.binds(signal));
		}
	}

	@Test
	public void testFailureKeyPairs()
	{
		for(int index = 0; index < FAILURE_SIGNALS.length; index++)
		{
			final SignalKey signal = new SignalKey(FAILURE_SIGNALS[index]);
			final ReceptorKey receptor = new ReceptorKey(FAILURE_RECEPTORS[index]);

			Assert.assertTrue("signal at index " + index + " was incorrectly able to bind. Signal: " + signal + ", Receptor: " + receptor, !receptor.binds(signal));
		}
	}
}
