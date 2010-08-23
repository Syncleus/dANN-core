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
package com.syncleus.dann.dataprocessing.language.parsing;

import java.util.*;
import java.util.regex.*;

/**
 * A BasicWordParser splits words into spaces.
 * @author Jeffrey Phillips Freeman
 */
public class BasicWordParser implements WordParser
{
	private static final Pattern SPACE_PATTERN = Pattern.compile("\\w++");
	private final Locale locale;

	/**
	 * Creates a new WordParser with the default locale.
	 */
	public BasicWordParser()
	{
		this.locale = Locale.getDefault();
	}

	/**
	 * Creates a BasicWordParser with the given locale.
	 * @param ourLocale The locale to use
	 */
	public BasicWordParser(final Locale ourLocale)
	{
		this.locale = ourLocale;
	}

	/**
	 * Gets all the words from a given string of text.
	 * @param text The string to parse
	 * @return All words in the string
	 */
	@Override
	public List<String> getWords(final String text)
	{
		final List<String> words = new ArrayList<String>();
		final String textLowerCase = text.toLowerCase(Locale.ENGLISH);
		final Matcher matches = SPACE_PATTERN.matcher(textLowerCase);
		while( matches.find() )
		{
			final String word = matches.group();
			words.add(word.toLowerCase(this.locale));
		}
		return Collections.unmodifiableList(words);
	}

	/**
	 * Gets an unmodifiable set of unique words from the given text.
	 * @param text The text to process
	 * @return The words in the text
	 */
	@Override
	public Set<String> getUniqueWords(final String text)
	{
		return Collections.unmodifiableSet(new HashSet<String>(this.getWords(text)));
	}

	/**
	 * Gets the currently used locale.
	 * @return The current locale.
	 */
	public Locale getLocale()
	{
		return this.locale;
	}
}
