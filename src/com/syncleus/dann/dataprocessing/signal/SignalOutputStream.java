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
package com.syncleus.dann.dataprocessing.signal;

import java.io.*;

/**
 * A SignalOutputStream is an OutputStream that writes signals.
 * @see java.io.OutputStream
 */
public class SignalOutputStream extends OutputStream
{
	private final ObjectOutputStream destStream;

	/**
	 * Creates a new SignalOutputStream around the provided OutputStream. If this stream is not
	 * an ObjectOutputStream, it wraps the underlying stream in one.
	 * @param outputStream The OutputStream to use
	 * @throws IOException If an IOException occurs in the underlying stream
	 */
	public SignalOutputStream(final OutputStream outputStream) throws IOException
	{
		if( outputStream instanceof ObjectOutputStream )
			this.destStream = (ObjectOutputStream) outputStream;
		else
			this.destStream = new ObjectOutputStream(outputStream);
	}

	/**
	 * Writes the provided signal array using an offset and length.
	 * @param signals The signals to write
	 * @param off The offset into the signal array to use
	 * @param len The length of the array
	 * @throws IOException If a write error occurs
	 */
	public void writeSignal(final double[] signals, final int off, final int len) throws IOException
	{
		for(int signalsIndex = off; signalsIndex < (off + len); signalsIndex++)
			this.destStream.writeDouble(signals[signalsIndex]);
	}

	/**
	 * Writes the entire provided signal array.
	 * @param signals The signal to write.
	 * @throws IOException If a write error occurs
	 */
	public void writeSignal(final double[] signals) throws IOException
	{
		for(final double signal : signals)
			this.destStream.writeDouble(signal);
	}

	/**
	 * Writes a single signal.
	 * @param signal The signal to write
	 * @throws IOException If a write error occurs
	 */
	public void writeSignal(final double signal) throws IOException
	{
		this.destStream.writeDouble(signal);
	}

	/**
	 * Writes an int.
	 * @param inData The int to write.
	 * @throws IOException If a write error occurs
	 */
	@Override
	public void write(final int inData) throws IOException
	{
		this.destStream.write(inData);
	}

	/**
	 * Writes an array of bytes.
	 * @param b The bytes to write
	 * @throws IOException If a write error occurs
	 */
	@Override
	public void write(final byte[] b) throws IOException
	{
		this.destStream.write(b);
	}

	/**
	 * Writes an array of bytes, with a provided offset and length.
	 * @param b The bytes to write
	 * @param off The offset
	 * @param len The length
	 * @throws IOException If a write error occurs
	 */
	@Override
	public void write(final byte[] b, final int off, final int len) throws IOException
	{
		this.destStream.write(b, off, len);
	}

	/**
	 * Flushes the IOStream's buffer.
	 * @throws IOException If an error occurs
	 */
	@Override
	public void flush() throws IOException
	{
		this.destStream.flush();
	}

	/**
	 * Closes the stream.
	 * @throws IOException If the stream is already closed, or another error occurs
	 */
	@Override
	public void close() throws IOException
	{
		this.destStream.close();
	}
}
