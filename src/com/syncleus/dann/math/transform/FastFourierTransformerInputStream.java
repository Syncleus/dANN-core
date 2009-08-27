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
package com.syncleus.dann.math.transform;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class FastFourierTransformerInputStream extends InputStream
{
	private ObjectInputStream srcStream;
	private FastFourierTransformer transformer;
	public int interval;
	public double[] buffer;

	public FastFourierTransformerInputStream(InputStream srcStream, FastFourierTransformer transformer, int interval) throws IOException
	{
		if(srcStream == null)
			throw new IllegalArgumentException("srcStream can not be null");
		if(transformer == null)
			throw new IllegalArgumentException("transformer can not be null");
		if(interval <= 0)
			throw new IllegalArgumentException("interval must be greater than 0");
		
		if(srcStream instanceof ObjectInputStream)
			this.srcStream = (ObjectInputStream) srcStream;
		else
			this.srcStream = new ObjectInputStream(srcStream);

		this.transformer = transformer;
		this.interval = interval;
	}

	public int transformsAvailable() throws IOException
	{
		int doublesAvailible = ( this.buffer != null ? (this.available()/8)+this.buffer.length : this.available()/8 );
		return doublesAvailible/this.transformer.getBlockSize();
	}
	
	public DiscreteFourierTransform readTransform() throws IOException
	{

		//make sure the buffer contains atleast one block, if not fill it. copy
		//block to signal
		final double[] signal = Arrays.copyOf(this.buffer, this.transformer.getBlockSize());
		if((this.buffer == null)||(this.buffer.length <= this.transformer.getBlockSize()))
		{
			//signal = Arrays.copyOf(this.buffer, this.transformer.getBlockSize());
			for(int signalIndex = this.buffer.length; signalIndex < signal.length; signalIndex++)
				signal[signalIndex] = this.srcStream.readDouble();
			this.buffer = signal;
		}

		//remove the time interval from the begining of the buffer and stream.
		int signalsToRemove = this.interval;
		if(this.buffer != null)
		{
			if(this.buffer.length <= signalsToRemove)
			{
				signalsToRemove -= this.buffer.length;
				this.buffer = null;
			}
			else
			{
				this.buffer = Arrays.copyOfRange(this.buffer, signalsToRemove, this.buffer.length);
				signalsToRemove = 0;
			}
		}

		if(signalsToRemove > 0)
			this.srcStream.skip(signalsToRemove);

		return this.transformer.transform(signal);
	}

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	@Override
	public int available() throws IOException
	{
		return this.srcStream.available();
	}

	@Override
	public void mark(int readlimit)
	{
		this.srcStream.mark(readlimit);
	}

	@Override
	public boolean markSupported()
	{
		return this.srcStream.markSupported();
	}

	public int read() throws IOException
	{
		return this.srcStream.read();
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		return this.srcStream.read(b);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		return this.srcStream.read(b, off, len);
	}

	@Override
	public void close() throws IOException
	{
		this.srcStream.close();
	}

	@Override
	public void reset() throws IOException
	{
		this.srcStream.reset();
	}

	@Override
	public long skip(long n) throws IOException
	{
		return this.srcStream.skip(n);
	}
}
