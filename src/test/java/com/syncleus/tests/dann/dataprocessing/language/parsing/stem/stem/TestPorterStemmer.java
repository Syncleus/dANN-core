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
package com.syncleus.tests.dann.dataprocessing.language.parsing.stem.stem;

import com.syncleus.dann.dataprocessing.language.parsing.stem.*;
import org.junit.*;

public class TestPorterStemmer
{
	@Test
	public void testWords()
	{
		final Stemmer stemmer = new PorterStemmer();

		Assert.assertTrue("word stem incorrect!", stemmer.stemWord("bowling").compareToIgnoreCase("bowl") == 0);
		Assert.assertTrue("word stem incorrect!", stemmer.stemWord("happiness").compareToIgnoreCase("happi") == 0);
		Assert.assertTrue("word stem incorrect!", stemmer.stemWord("jeffrey").compareToIgnoreCase("jeffrei") == 0);
		Assert.assertTrue("word stem incorrect!", stemmer.stemWord("running").compareToIgnoreCase("run") == 0);
		Assert.assertTrue("word stem incorrect!", stemmer.stemWord("napping").compareToIgnoreCase("nap") == 0);
		Assert.assertTrue("word stem incorrect!", stemmer.stemWord("runner").compareToIgnoreCase("runner") == 0);
		Assert.assertTrue("word stem incorrect!", stemmer.stemWord("hiker").compareToIgnoreCase("hiker") == 0);
		Assert.assertTrue("word stem incorrect!", stemmer.stemWord("Nonsense").compareToIgnoreCase("Nonsens") == 0);
	}
}
