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
package com.syncleus.dann.dataprocessing.language.stem;

import java.util.*;
import com.syncleus.dann.dataprocessing.language.BasicWordParser;

/**
 * A StemmingWordParser parses Strings into their stems.
 * @author Jeffrey Phillips Freeman
 */
public class StemmingWordParser extends BasicWordParser implements Stemmer
{
	private final Stemmer stemmer;

	/**
	 * Creates a StemmingWordParser with the default Stemmer, a PorterStemmer.
	 * @see com.syncleus.dann.dataprocessing.language.stem.PorterStemmer
	 */
	public StemmingWordParser()
	{
		this(new PorterStemmer());
	}

	/**
	 * Creates a StemmingWordParser with the supplied Stemmer.
	 * @param ourStemmer The Stemmer to use.
	 */
	public StemmingWordParser(final Stemmer ourStemmer)
	{
		this.stemmer = ourStemmer;
	}

	/**
	 * Gets the word stem for the given word.
	 * @param word The word to get the stem from
	 * @return The word stem.
	 */
	@Override
	public String stemWord(final String word)
	{
		return this.stemmer.stemWord(word.toLowerCase(this.getLocale()));
	}

	/**
	 * Converts a collection of Strings to a list of its stems - one stem per word.
	 * Convenience method for stemWord().
	 * @param unstemmed The collection of unstemmed words
	 * @return An unmodifiable list of the word stems from the given collection.
	 * @see com.syncleus.dann.dataprocessing.language.stem.StemmingWordParser#stemWord(String)
	 */
	private List<String> stemList(final Collection<String> unstemmed)
	{
		final List<String> stemmedWords = new ArrayList<String>(unstemmed.size());
		for(final String word : unstemmed)
			stemmedWords.add(stemmer.stemWord(word.toLowerCase(this.getLocale())));
		return Collections.unmodifiableList(stemmedWords);
	}

	/**
	 * Converts a collection of Strings to a set of its stems. A maximum of one stem per word,
	 * but duplicates are removed from output.
	 * @param unstemmed A collection of unstemmed words
	 * @return An unmodifiable set of word stems from the given collection
	 */
	private Set<String> stemSet(final Collection<String> unstemmed)
	{
		final Set<String> stemmedWords = new HashSet<String>(unstemmed.size());
		for(final String word : unstemmed)
			stemmedWords.add(stemmer.stemWord(word.toLowerCase(this.getLocale())));
		return Collections.unmodifiableSet(stemmedWords);
	}

	/**
	 * Gets a collection of stems from the given text.
	 * @param text The string to parse
	 * @return The stems from the given text
	 */
	@Override
	public List<String> getWords(final String text)
	{
		return stemList(super.getWords(text));
	}

	/**
	 * Gets the unique stems from the string of text.
	 * @param text The text to process
	 * @return The unique set of stems
	 */
	@Override
	public Set<String> getUniqueWords(final String text)
	{
		return stemSet(super.getUniqueWords(text));
	}
}
