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

import java.util.List;
import java.util.Set;

/**
 * A WordParser splits a String into individual words.
 * @author Jeffrey Phillips Freeman
 */
public interface WordParser
{
	/**
	 * Gets all words from the given String.
	 * @param text The string to use
	 * @return The list of all words
	 */
	List<String> getWords(String text);

	/**
	 * Gets the unique words from the given STring.
	 * @param text The string to use
	 * @return The set of all unique words
	 * @see WordParser#getWords(String)
	 */
	Set<String> getUniqueWords(String text);
}
