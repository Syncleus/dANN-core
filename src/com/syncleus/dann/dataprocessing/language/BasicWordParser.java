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
package com.syncleus.dann.dataprocessing.language;

import java.util.*;
import java.util.regex.*;

public class BasicWordParser implements WordParser
{
	public static final Pattern spacePattern = Pattern.compile("\\w++");

	public List<String> getWords(final String text)
	{
		final List<String> words = new ArrayList<String>();

		final String textLowerCase = text.toLowerCase();
		final Matcher matches = spacePattern.matcher(textLowerCase);
		while(matches.find())
		{
			final String word = matches.group();
			words.add(word.toLowerCase());
		}

		return Collections.unmodifiableList(words);
	}

	public Set<String> getUniqueWords(final String text)
	{
		return Collections.unmodifiableSet(new HashSet<String>(this.getWords(text)));
	}
}
