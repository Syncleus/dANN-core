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

import com.syncleus.dann.dataprocessing.language.*;
import java.util.*;

public class StemmingWordParser extends BasicWordParser implements Stemmer
{
	private Stemmer stemmer;

	public StemmingWordParser()
	{
		this.stemmer = new PorterStemmer();
	}

	public StemmingWordParser(Stemmer stemmer)
	{
		this.stemmer = stemmer;
	}

	public String stemWord(String word)
	{
		return this.stemmer.stemWord(word.toLowerCase());
	}

	private List<String> stemList(Collection<String> unstemmed)
	{
		List<String> stemmedWords = new ArrayList<String>(unstemmed.size());
		for(String word:unstemmed)
			stemmedWords.add(stemmer.stemWord(word.toLowerCase()));
		return Collections.unmodifiableList(stemmedWords);
	}

	private Set<String> stemSet(Collection<String> unstemmed)
	{
		Set<String> stemmedWords = new HashSet<String>(unstemmed.size());
		for(String word : unstemmed)
			stemmedWords.add(stemmer.stemWord(word.toLowerCase()));
		return Collections.unmodifiableSet(stemmedWords);
	}

	@Override
	public List<String> getWords(String text)
	{
		return stemList(super.getWords(text));

	}

	@Override
	public Set<String> getUniqueWords(String text)
	{
		return stemSet(super.getUniqueWords(text));
	}
}
