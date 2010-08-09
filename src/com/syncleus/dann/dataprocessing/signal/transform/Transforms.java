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

/**
 * A utility class that provides the StreamPair class, to convert from a FastFourierTransformerInputStream to
 * a SignalOutputStream, and a method to create a StreamPair from a FastFourierTransformer and a given
 * sampling rate.
 * @author Jeffrey Phillips Freeman
 */
public final class Transforms
{
	/**
	 * A StreamPair encapsulates a FastFourierTransformerInputStream and a SignalOutputStream.
	 */
	public static final class StreamPair
	{
		private final FastFourierTransformerInputStream inStream;
		private final SignalOutputStream outStream;

		/**
		 * Creates a new StreamPair around the given FastFourierTransformerInputStream and the given SignalOutputStream.
		 * @param inputStream The FastFourierTransformerInputStream to use
		 * @param outputStream The SignalOutputStream to use
		 */
		public StreamPair(final FastFourierTransformerInputStream inputStream, final SignalOutputStream outputStream)
		{
			if( inputStream == null )
				throw new IllegalArgumentException("inputStream can not be null");
			if( outputStream == null )
				throw new IllegalArgumentException("outputStream can not be null");

			this.inStream = inputStream;
			this.outStream = outputStream;
		}

		/**
		 * Gets the FastFourierTransformerInputStream provided.
		 * @return The FastFourierTransformerInputStream provided
		 */
		public FastFourierTransformerInputStream getInStream()
		{
			return this.inStream;
		}

		/**
		 * Gets the SignalOutputStream provided.
		 * @return The SignalOutputStream provided
		 */
		public SignalOutputStream getOutStream()
		{
			return this.outStream;
		}
	}

	/**
	 * Private constructor to prevent initialization.
	 */
	private Transforms()
	{
	}

	/**
	 * Creates a new StreamPair from the provided FastFourierTransformer and the given interval.
	 * Signals are provided to the FastFourierTransformerInputStream in the StreamPair, and are then
	 * made available from the StreamPair's SignalOutputStream after the FastFourierTransformer is applied.
	 * @param transformer The FastFourierTransformer to use
	 * @param interval The interval to sample over
	 * @return A StreamPair usable to transform data
	 */
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
