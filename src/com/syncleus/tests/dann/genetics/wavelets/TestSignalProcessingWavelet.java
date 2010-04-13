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
import com.syncleus.dann.genetics.wavelets.SignalProcessingWavelet.GlobalSignalConcentration;

public class TestSignalProcessingWavelet
{
	@Test
	public void testMutations()
	{
		for(int testCount = 0; testCount < 100; testCount++)
			testMutationOnce();
	}

	@Test
	public void testMutationOnce()
	{
        GlobalSignalConcentration signalX = new GlobalSignalConcentration();
        GlobalSignalConcentration signalY = new GlobalSignalConcentration();
        GlobalSignalConcentration signalZ = new GlobalSignalConcentration();
        SignalProcessingWavelet processor = new SignalProcessingWavelet(/*new Cell(),*/ signalX, signalZ);
        for(int index = 0;index < 500 ;index++)
        {
			try
			{
				processor = processor.mutate(1.0, signalX);
				processor = processor.mutate(1.0, signalY);
				processor = processor.mutate(1.0);
			}
			catch(CloneNotSupportedException caught)
			{
				caught.printStackTrace();
				System.exit(0);
			}
        }

//        System.out.println("The current equation contains " + processor.getWaveCount() + " waves:");
//        System.out.println(processor.toString());

        processor.preTick();
        processor.tick();
	}
}
