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
package com.syncleus.dann.dataprocessing.language.parsing.stem;

import java.util.*;

/**
 * This is an implementation of <a href="http://tartarus.org/~martin/PorterStemmer/">Martin Porter's stemming algorithm</a>.
 * This algorithm efficiently separates word stems from words.
 * @author Jeffrey Phillips Freeman
 */
public class PorterStemmer implements Stemmer
{
	private static final int GROW_SIZE = 50;

	private char[] buffer;
	private int stemEndIndex;
	private int wordEndIndex;
	private int stemStartIndex;
	private boolean dirtyBuffer;
	private final int growSize;
	private final Locale locale;

	/**
	 * Creates a new PorterStemmer with the default grow size and default locale.
	 */
	public PorterStemmer()
	{
		this(GROW_SIZE);
	}

	/**
	 * Creates a new PorterStemmer with the supplied grow size and default locale.
	 * @param ourGrowSize The grow size to use.
	 */
	public PorterStemmer(final int ourGrowSize)
	{
		this(Locale.getDefault(), ourGrowSize);
	}

	/**
	 * Creates a new PorterStemmer with the default grow size and the supplied locale.
	 * @param ourLocale The locale to use
	 */
	public PorterStemmer(final Locale ourLocale)
	{
		this(ourLocale, GROW_SIZE);
	}

	/**
	 * Creates a new PorterStemmer with the supplied locale and the supplied grow size.
	 * @param ourLocale The locale to use
	 * @param ourGrowSize The grow size to use
	 */
	public PorterStemmer(final Locale ourLocale, final int ourGrowSize)
	{
		this.growSize = ourGrowSize;
		this.buffer = new char[ourGrowSize];
		this.dirtyBuffer = false;
		this.locale = ourLocale;
	}

	/**
	 * Gets the word stem from the specified word.
	 * @param originalWord The word to extract the stems from
	 * @return The stem of the word
	 */
	@Override
	public String stemWord(final String originalWord)
	{
		final String originalWordLowerCase = originalWord.toLowerCase(this.locale);
		this.dirtyBuffer = false;
		int bufferSize = originalWordLowerCase.toCharArray().length;
		this.buffer = Arrays.copyOf(originalWordLowerCase.toCharArray(), bufferSize);
		this.stemStartIndex = 0;
		this.wordEndIndex = bufferSize - 1;
		if( this.wordEndIndex > this.stemStartIndex + 1 )
		{
			step1();
			step2();
			step3();
			step4();
			step5();
			step6();
		}
		if( bufferSize != this.wordEndIndex + 1 )
			this.dirtyBuffer = true;
		bufferSize = this.wordEndIndex + 1;
		if( this.dirtyBuffer )
			return new String(this.buffer, 0, bufferSize);
		else
			return originalWordLowerCase;
	}

	/**
	 * Returns whether the character at a given index is a consonant, by exclusion.
	 * A, e, i, o, u are not consonants. y is a consonant when it's at the start of the stem,
	 * or when it immediately follows a vowel. All other characters are consonants.
	 * @param i The index to check
	 * @return Whether it is a consonant
	 */
	private boolean isConsonant(final int i)
	{
		switch(this.buffer[i])
		{
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			return false;
		case 'y':
			return (i == this.stemStartIndex) || !this.isConsonant(i - 1);
		default:
			return true;
		}
	}

	/**
	 * Returns the number of consonants in the stem via a complex linear
	 * count.
	 * @return The number of consonants in the stem.
	 * @see com.syncleus.dann.dataprocessing.language.parsing.stem.PorterStemmer#isConsonant(int)
	 */
	private int countConsonantsInStem()
	{
		int n = 0;
		int i = this.stemStartIndex;
		while( true )
		{
			if( i > this.stemEndIndex )
				return n;
			if( !this.isConsonant(i) )
				break;
			i++;
		}
		i++;
		while( true )
		{
			while( true )
			{
				if( i > this.stemEndIndex )
					return n;
				if( this.isConsonant(i) )
					break;
				i++;
			}
			i++;
			n++;
			while( true )
			{
				if( i > this.stemEndIndex )
					return n;
				if( !this.isConsonant(i) )
					break;
				i++;
			}
			i++;
		}
	}

	/**
	 * Returns whether there is a vowel in the stem by a simple linear search.
	 * @return Whether there is a vowel in the word stem.
	 * @see com.syncleus.dann.dataprocessing.language.parsing.stem.PorterStemmer#isConsonant(int)
	 */
	private boolean isVowelInStem()
	{
		for(int bufferIndex = this.stemStartIndex; bufferIndex <= this.stemEndIndex; bufferIndex++)
			if( !this.isConsonant(bufferIndex) )
				return true;
		return false;
	}

	/**
	 * Determines whether the given index is a consonant that appears twice in succession, once at the given index
	 * and once before.
	 * 'tall' has the repeated consonant 'l' at index 3. The theoretical word stem 'lawl' does not have such
	 * a repeated consonant.
	 * @param testCharacterIndex The index of the character to check
	 * @return Whether there is a repeated consonant at the given index
	 */
	private boolean isRepeatedConsonant(final int testCharacterIndex)
	{
		return testCharacterIndex >= this.stemStartIndex + 1 && this.buffer[testCharacterIndex] == this.buffer[testCharacterIndex - 1] && this.isConsonant(testCharacterIndex);
	}

	/**
	 * Determines whether the given index is the third character in a consonant-vowel-consonant sequence.
	 * 'com' is an example of such a sequence.
	 * @param i The index to check
	 * @return Whether the index is a cvc sequence
	 */
	private boolean isConsonantVowelConsonant(final int i)
	{
		if( i < this.stemStartIndex + 2 || !this.isConsonant(i) || this.isConsonant(i - 1) || !this.isConsonant(i - 2) )
			return false;
		else
		{
			final int ch = this.buffer[i];
			if( ch == 'w' || ch == 'x' || ch == 'y' )
				return false;
		}
		return true;
	}

	/**
	 * Determines whether the String ends with the provided String.
	 * @param possibleEnding The possible ending to the String
	 * @return Whether the word ends with the given string
	 */
	private boolean ends(final String possibleEnding)
	{
		final int possibleEndingLength = possibleEnding.length();
		final int o = this.wordEndIndex - possibleEndingLength + 1;
		if( o < this.stemStartIndex )
			return false;
		for(int i = 0; i < possibleEndingLength; i++)
			if( this.buffer[o + i] != possibleEnding.charAt(i) )
				return false;
		this.stemEndIndex = this.wordEndIndex - possibleEndingLength;
		return true;
	}

	/**
	 * Sets the internal buffer to the given string.
	 * @param setString The string to use as the buffer
	 */
	private void setTo(final String setString)
	{
		for(int i = 0; i < setString.length(); i++)
			this.buffer[this.stemEndIndex + 1 + i] = setString.charAt(i);
		this.wordEndIndex = this.stemEndIndex + setString.length();
		this.dirtyBuffer = true;
	}

	/**
	 * Sets the internal buffer to the given string if there are consonants in the stem.
	 * @param setString The stream to use as the buffer
	 */
	private void setToConsonantStem(final String setString)
	{
		if( this.countConsonantsInStem() > 0 )
			this.setTo(setString);
	}

	/* step1() gets rid of plurals and -ed or -ing. e.g.
	caresses  ->  caress
	ponies    ->  poni
	ties      ->  ti
	caress    ->  caress
	cats      ->  cat

	feed      ->  feed
	agreed    ->  agree
	disabled  ->  disable

	matting   ->  mat
	mating    ->  mate
	meeting   ->  meet
	milling   ->  mill
	messing   ->  mess

	meetings  ->  meet
	 */

	/**
	 * Removes plurals (-s), and stems of -ed or -ing.
	 * Transforms "caresses" into "caress"-, "ponies" into "poni"-, and "meetings" into "meet"-
	 * Operates in-place.
	 */
	private void step1()
	{
		if( this.buffer[this.wordEndIndex] == 's' )
			if( ends("sses") )
				this.wordEndIndex -= 2;
			else if( ends("ies") )
				setTo("i");
			else if( this.buffer[this.wordEndIndex - 1] != 's' )
				this.wordEndIndex--;
		if( ends("eed") )
		{
			if( countConsonantsInStem() > 0 )
				this.wordEndIndex--;
		}
		else if( (ends("ed") || ends("ing")) && isVowelInStem() )
		{
			this.wordEndIndex = this.stemEndIndex;
			if( ends("at") )
				setTo("ate");
			else if( ends("bl") )
				setTo("ble");
			else if( ends("iz") )
				setTo("ize");
			else if( isRepeatedConsonant(this.wordEndIndex) )
			{
				final int ch = this.buffer[this.wordEndIndex--];
				if( ch == 'l' || ch == 's' || ch == 'z' )
					this.wordEndIndex++;
			}
			else if( countConsonantsInStem() == 1 && isConsonantVowelConsonant(this.wordEndIndex) )
				setTo("e");
		}
	}

	/* step2() turns terminal y to bufferIndex when there is another vowel in the stem. */

	/**
	 * If the word ends in a y, and it's not the only vowel, change the terminal y to an i.
	 */
	private void step2()
	{
		if( ends("y") && isVowelInStem() )
		{
			this.buffer[this.wordEndIndex] = 'i';
			this.dirtyBuffer = true;
		}
	}

	/**
	 * step3() maps double suffices to single ones. so -ization ( = -ize plus
	 * -ation) maps to -ize etc. note that the string before the suffix must give
	 * countConsonantsInStem() > 0.
	 */
	private void step3()
	{
		if( this.wordEndIndex == this.stemStartIndex )
			return;
		switch(this.buffer[this.wordEndIndex - 1])
		{
		case 'a':
			if( ends("ational") )
				setToConsonantStem("ate");
			else if( ends("tional") )
				setToConsonantStem("tion");
			break;
		case 'c':
			if( ends("enci") )
				setToConsonantStem("ence");
			else if( ends("anci") )
				setToConsonantStem("ance");
			break;
		case 'e':
			if( ends("izer") )
				setToConsonantStem("ize");
			break;
		case 'l':
			if( ends("bli") )
				setToConsonantStem("ble");
			else if( ends("alli") )
				setToConsonantStem("al");
			else if( ends("entli") )
				setToConsonantStem("ent");
			else if( ends("eli") )
				setToConsonantStem("e");
			else if( ends("ousli") )
				setToConsonantStem("ous");
			break;
		case 'o':
			if( ends("ization") )
				setToConsonantStem("ize");
			else if( ends("ation") )
				setToConsonantStem("ate");
			else if( ends("ator") )
				setToConsonantStem("ate");
			break;
		case 's':
			if( ends("alism") )
				setToConsonantStem("al");
			else if( ends("iveness") )
				setToConsonantStem("ive");
			else if( ends("fulness") )
				setToConsonantStem("ful");
			else if( ends("ousness") )
				setToConsonantStem("ous");
			break;
		case 't':
			if( ends("aliti") )
				setToConsonantStem("al");
			else if( ends("iviti") )
				setToConsonantStem("ive");
			else if( ends("biliti") )
				setToConsonantStem("ble");
			break;
		case 'g':
			if( ends("logi") )
				setToConsonantStem("log");
			break;
		default:
			break;
			//throw new UnexpectedDannError("Unknown suffix from " + buffer[wordEndIndex - 1]);
		}
	}

	/**
	 * step4() removes -ic-, -full, -ness, -icate, -ative, and -alize suffixes.
	 * @see PorterStemmer#step3()
	 */
	private void step4()
	{
		switch(this.buffer[this.wordEndIndex])
		{
		case 'e':
			if( ends("icate") )
				setToConsonantStem("ic");
			else if( ends("ative") )
				setToConsonantStem("");
			else if( ends("alize") )
				setToConsonantStem("al");
			break;
		case 'i':
			if( ends("iciti") )
				setToConsonantStem("ic");
			break;
		case 'l':
			if( ends("ical") )
				setToConsonantStem("ic");
			else if( ends("ful") )
				setToConsonantStem("");
			break;
		case 's':
			if( ends("ness") )
				setToConsonantStem("");
			break;
		default:
			break;
			//throw new UnexpectedDannError("Unknown suffix from " + buffer[wordEndIndex]);
		}
	}

	/**
	 * step5() removes -ant, -ence, -ance, -er, -ic, the -[st]ion endings, -ment, -ement,
	 * ou, -ism, -iti, -ous, -ive, and -ize.
	 */
	private void step5()
	{
		if( this.wordEndIndex == this.stemStartIndex )
			return;
		switch(this.buffer[this.wordEndIndex - 1])
		{
		case 'a':
			if( ends("al") )
				break;
			return;
		case 'c':
			if( ends("ance") )
				break;
			if( ends("ence") )
				break;
			return;
		case 'e':
			if( ends("er") )
				break;
			return;
		case 'i':
			if( ends("ic") )
				break;
			return;
		case 'l':
			if( ends("able") )
				break;
			if( ends("ible") )
				break;
			return;
		case 'n':
			if( ends("ant") )
				break;
			if( ends("ement") )
				break;
			if( ends("ment") )
				break;
			/* element etc. not stripped before the countConsonantsInStem */
			if( ends("ent") )
				break;
			return;
		case 'o':
			if( ends("ion") && this.stemEndIndex >= 0 && (this.buffer[this.stemEndIndex] == 's' || this.buffer[this.stemEndIndex] == 't') )
				break;
			/* stemEndIndex >= 0 fixes Bug 2 */
			if( ends("ou") )
				break;
			return;
		/* takes care of -ous */
		case 's':
			if( ends("ism") )
				break;
			return;
		case 't':
			if( ends("ate") )
				break;
			if( ends("iti") )
				break;
			return;
		case 'u':
			if( ends("ous") )
				break;
			return;
		case 'v':
			if( ends("ive") )
				break;
			return;
		case 'z':
			if( ends("ize") )
				break;
			return;
		default:
			return;
		}
		if( countConsonantsInStem() > 1 )
			this.wordEndIndex = this.stemEndIndex;
	}

	/**
	 * step6() removes a final -e if countConsonantsInStem() > 1.
	 */
	private void step6()
	{
		this.stemEndIndex = this.wordEndIndex;
		if( this.buffer[this.wordEndIndex] == 'e' )
		{
			final int a = countConsonantsInStem();
			if( a > 1 || a == 1 && !isConsonantVowelConsonant(this.wordEndIndex - 1) )
				this.wordEndIndex--;
		}
		if( this.buffer[this.wordEndIndex] == 'l' && isRepeatedConsonant(this.wordEndIndex) && countConsonantsInStem() > 1 )
			this.wordEndIndex--;
	}

	/**
	 * Gets the current grow size.
	 * @return The current grow size.
	 */
	public int getGrowSize()
	{
		return this.growSize;
	}

	/**
	 * Gets the current locale.
	 * @return The locale in current use.
	 */
	public Locale getLocale()
	{
		return this.locale;
	}
}
