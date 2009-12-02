/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.dataprocessing.signal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SignalOutputStream extends OutputStream
{
	private ObjectOutputStream destStream;

	public SignalOutputStream(OutputStream destStream) throws IOException
	{
		if(destStream instanceof ObjectOutputStream)
			this.destStream = (ObjectOutputStream) destStream;
		else
			this.destStream = new ObjectOutputStream(destStream);
	}

	public void writeSignal(double[] signals, int off, int len) throws IOException
	{
		for(int signalsIndex = off; signalsIndex < (off+len); signalsIndex++)
			this.destStream.writeDouble(signals[signalsIndex]);
	}

	public void writeSignal(double[] signals) throws IOException
	{
		for(double signal : signals)
			this.destStream.writeDouble(signal);
	}

	public void writeSignal(double signal) throws IOException
	{
		this.destStream.writeDouble(signal);
	}

	public void write(int inData) throws IOException
	{
		this.destStream.write(inData);
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		this.destStream.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		this.destStream.write(b, off, len);
	}

	@Override
	public void flush() throws IOException
	{
		this.destStream.flush();
	}

	@Override
	public void close() throws IOException
	{
		this.destStream.close();
	}
}
