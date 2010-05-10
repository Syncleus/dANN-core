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
package com.syncleus.dann.dataprocessing.signal.transform;

import java.io.*;
import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.dataprocessing.signal.SignalOutputStream;

public final class Transforms
{
	public static final class StreamPair
	{
		private final FastFourierTransformerInputStream inStream;
		private final SignalOutputStream outStream;

		public StreamPair(final FastFourierTransformerInputStream inStream, final SignalOutputStream outStream)
		{
			if( inStream == null )
				throw new IllegalArgumentException("inStream can not be null");
			if( outStream == null )
				throw new IllegalArgumentException("outStream can not be null");

			this.inStream = inStream;
			this.outStream = outStream;
		}

		public FastFourierTransformerInputStream getInStream()
		{
			return this.inStream;
		}

		public SignalOutputStream getOutStream()
		{
			return this.outStream;
		}
	}

	private Transforms()
	{
	}

	public static StreamPair streamedTransformer(final FastFourierTransformer transformer, final int interval)
	{
		try
		{
			final PipedInputStream inPipe = new PipedInputStream();
			final PipedOutputStream outPipe = new PipedOutputStream(inPipe);
			inPipe.connect(outPipe);

			final FastFourierTransformerInputStream fftInStream = new FastFourierTransformerInputStream(inPipe, transformer, interval);
			final SignalOutputStream signalOutStream = new SignalOutputStream(outPipe);

			return new StreamPair(fftInStream, signalOutStream);
		}
		catch(IOException caughtException)
		{
			throw new UnexpectedDannError(caughtException);
		}
	}
}
