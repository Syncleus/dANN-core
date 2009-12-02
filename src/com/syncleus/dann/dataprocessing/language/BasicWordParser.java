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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicWordParser implements WordParser
{
	public static final Pattern spacePattern = Pattern.compile("\\w++");

	public List<String> getWords(String text)
	{
		List<String> words = new ArrayList<String>();

		text = text.toLowerCase();
		Matcher matches = spacePattern.matcher(text);
		while(matches.find())
		{
			String word = matches.group();
			words.add(word.toLowerCase());
		}

		return Collections.unmodifiableList(words);
	}

	public Set<String> getUniqueWords(String text)
	{
		return Collections.unmodifiableSet(new HashSet<String>(this.getWords(text)));
	}
}
