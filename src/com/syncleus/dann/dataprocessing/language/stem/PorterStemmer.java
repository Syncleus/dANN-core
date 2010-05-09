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

public class PorterStemmer implements Stemmer
{
	private char[] buffer;
	private int stemEndIndex;
	private int wordEndIndex;
	private int stemStartIndex;
	private boolean dirtyBuffer;
	private final int growSize;
	private final Locale locale;

	public PorterStemmer()
	{
		this(50);
	}

	public PorterStemmer(final int growSize)
	{
		this(Locale.getDefault(), growSize);
	}

	public PorterStemmer(final Locale locale)
	{
		this(locale, 50);
	}

	public PorterStemmer(final Locale locale, final int growSize)
	{
		this.growSize = growSize;
		this.buffer = new char[growSize];
		this.dirtyBuffer = false;
		this.locale = locale;
	}

	public String stemWord(final String originalWord)
	{
		final String originalWordLowerCase = originalWord.toLowerCase(this.locale);
		this.dirtyBuffer = false;
		int bufferSize = originalWordLowerCase.toCharArray().length;
		this.buffer = Arrays.copyOf(originalWordLowerCase.toCharArray(), bufferSize);
		this.stemStartIndex = 0;
		this.wordEndIndex = bufferSize - 1;
		if (this.wordEndIndex > this.stemStartIndex + 1)
		{
			step1();
			step2();
			step3();
			step4();
			step5();
			step6();
		}
		if (bufferSize != this.wordEndIndex + 1)
			this.dirtyBuffer = true;
		bufferSize = this.wordEndIndex + 1;
		if (this.dirtyBuffer)
			return new String(this.buffer, 0, bufferSize);
		else
			return originalWordLowerCase;
	}

	private boolean isConsonant(final int i)
	{
		switch (this.buffer[i])
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

	private int countConsonantsInStem()
	{
		int n = 0;
		int i = this.stemStartIndex;
		while (true)
		{
			if (i > this.stemEndIndex)
				return n;
			if (!this.isConsonant(i))
				break;
			i++;
		}
		i++;
		while (true)
		{
			while (true)
			{
				if (i > this.stemEndIndex)
					return n;
				if (this.isConsonant(i))
					break;
				i++;
			}
			i++;
			n++;
			while (true)
			{
				if (i > this.stemEndIndex)
					return n;
				if (!this.isConsonant(i))
					break;
				i++;
			}
			i++;
		}
	}

	private boolean isVowelInStem()
	{
		for(int bufferIndex = this.stemStartIndex; bufferIndex <= this.stemEndIndex; bufferIndex++)
			if (!this.isConsonant(bufferIndex))
				return true;
		return false;
	}

	private boolean isRepeatedConsonant(final int testCharacterIndex)
	{
		return testCharacterIndex >= this.stemStartIndex + 1 && this.buffer[testCharacterIndex] == this.buffer[testCharacterIndex - 1] && this.isConsonant(testCharacterIndex);
	}

	private boolean isConsonantVowelConsonant(final int i)
	{
		if (i < this.stemStartIndex + 2 || !this.isConsonant(i) || this.isConsonant(i - 1) || !this.isConsonant(i - 2))
			return false;
		else
		{
			final int ch = this.buffer[i];
			if (ch == 'w' || ch == 'x' || ch == 'y')
				return false;
		}
		return true;
	}

	private boolean ends(final String possibleEnding)
	{
		final int possibleEndingLength = possibleEnding.length();
		final int o = this.wordEndIndex - possibleEndingLength + 1;
		if (o < this.stemStartIndex)
			return false;
		for(int i = 0; i < possibleEndingLength; i++)
			if (this.buffer[o + i] != possibleEnding.charAt(i))
				return false;
		this.stemEndIndex = this.wordEndIndex - possibleEndingLength;
		return true;
	}

	private void setTo(final String setString)
	{
		for(int i = 0; i < setString.length(); i++)
			this.buffer[this.stemEndIndex + 1 + i] = setString.charAt(i);
		this.wordEndIndex = this.stemEndIndex + setString.length();
		this.dirtyBuffer = true;
	}

	private void setToConsonantStem(final String setString)
	{
		if (this.countConsonantsInStem() > 0)
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
	private void step1()
	{
		if (this.buffer[this.wordEndIndex] == 's')
			if (ends("sses"))
				this.wordEndIndex -= 2;
			else if (ends("ies"))
				setTo("i");
			else if (this.buffer[this.wordEndIndex - 1] != 's')
				this.wordEndIndex--;
		if (ends("eed"))
		{
			if (countConsonantsInStem() > 0)
				this.wordEndIndex--;
		}
		else if ((ends("ed") || ends("ing")) && isVowelInStem())
		{
			this.wordEndIndex = this.stemEndIndex;
			if (ends("at"))
				setTo("ate");
			else if (ends("bl"))
				setTo("ble");
			else if (ends("iz"))
				setTo("ize");
			else if (isRepeatedConsonant(this.wordEndIndex))
			{
				final int ch = this.buffer[this.wordEndIndex--];
				if (ch == 'l' || ch == 's' || ch == 'z')
					this.wordEndIndex++;
			}
			else if (countConsonantsInStem() == 1 && isConsonantVowelConsonant(this.wordEndIndex))
				setTo("e");
		}
	}

	/* step2() turns terminal y to bufferIndex when there is another vowel in the stem. */
	private void step2()
	{
		if (ends("y") && isVowelInStem())
		{
			this.buffer[this.wordEndIndex] = 'i';
			this.dirtyBuffer = true;
		}
	}

	/* step3() maps double suffices to single ones. so -ization ( = -ize plus
	-ation) maps to -ize etc. note that the string before the suffix must give
	countConsonantsInStem() > 0. */
	private void step3()
	{
		if (this.wordEndIndex == this.stemStartIndex)
			return;
		switch (this.buffer[this.wordEndIndex - 1])
		{
		case 'a':
			if (ends("ational"))
				setToConsonantStem("ate");
			else if (ends("tional"))
				setToConsonantStem("tion");
			break;
		case 'c':
			if (ends("enci"))
				setToConsonantStem("ence");
			else if (ends("anci"))
				setToConsonantStem("ance");
			break;
		case 'e':
			if (ends("izer"))
				setToConsonantStem("ize");
			break;
		case 'l':
			if (ends("bli"))
				setToConsonantStem("ble");
			else if (ends("alli"))
				setToConsonantStem("al");
			else if (ends("entli"))
				setToConsonantStem("ent");
			else if (ends("eli"))
				setToConsonantStem("e");
			else if (ends("ousli"))
				setToConsonantStem("ous");
			break;
		case 'o':
			if (ends("ization"))
				setToConsonantStem("ize");
			else if (ends("ation"))
				setToConsonantStem("ate");
			else if (ends("ator"))
				setToConsonantStem("ate");
			break;
		case 's':
			if (ends("alism"))
				setToConsonantStem("al");
			else if (ends("iveness"))
				setToConsonantStem("ive");
			else if (ends("fulness"))
				setToConsonantStem("ful");
			else if (ends("ousness"))
				setToConsonantStem("ous");
			break;
		case 't':
			if (ends("aliti"))
				setToConsonantStem("al");
			else if (ends("iviti"))
				setToConsonantStem("ive");
			else if (ends("biliti"))
				setToConsonantStem("ble");
			break;
		case 'g':
			if (ends("logi"))
				setToConsonantStem("log");
			break;
		}
	}

	/* step4() deals with -ic-, -full, -ness etc. similar strategy to step3. */
	private void step4()
	{
		switch (this.buffer[this.wordEndIndex])
		{
		case 'e':
			if (ends("icate"))
				setToConsonantStem("ic");
			else if (ends("ative"))
				setToConsonantStem("");
			else if (ends("alize"))
				setToConsonantStem("al");
			break;
		case 'i':
			if (ends("iciti"))
				setToConsonantStem("ic");
			break;
		case 'l':
			if (ends("ical"))
				setToConsonantStem("ic");
			else if (ends("ful"))
				setToConsonantStem("");
			break;
		case 's':
			if (ends("ness"))
				setToConsonantStem("");
			break;
		}
	}

	/* step5() takes off -ant, -ence etc., in context <c>vcvc<v>. */
	private void step5()
	{
		if (this.wordEndIndex == this.stemStartIndex)
			return;
		switch (this.buffer[this.wordEndIndex - 1])
		{
		case 'a':
			if (ends("al"))
				break;
			return;
		case 'c':
			if (ends("ance"))
				break;
			if (ends("ence"))
				break;
			return;
		case 'e':
			if (ends("er"))
				break;
			return;
		case 'i':
			if (ends("ic"))
				break;
			return;
		case 'l':
			if (ends("able"))
				break;
			if (ends("ible"))
				break;
			return;
		case 'n':
			if (ends("ant"))
				break;
			if (ends("ement"))
				break;
			if (ends("ment"))
				break;
			/* element etc. not stripped before the countConsonantsInStem */
			if (ends("ent"))
				break;
			return;
		case 'o':
			if (ends("ion") && this.stemEndIndex >= 0 && (this.buffer[this.stemEndIndex] == 's' || this.buffer[this.stemEndIndex] == 't'))
				break;
			/* stemEndIndex >= 0 fixes Bug 2 */
			if (ends("ou"))
				break;
			return;
		/* takes care of -ous */
		case 's':
			if (ends("ism"))
				break;
			return;
		case 't':
			if (ends("ate"))
				break;
			if (ends("iti"))
				break;
			return;
		case 'u':
			if (ends("ous"))
				break;
			return;
		case 'v':
			if (ends("ive"))
				break;
			return;
		case 'z':
			if (ends("ize"))
				break;
			return;
		default:
			return;
		}
		if (countConsonantsInStem() > 1)
			this.wordEndIndex = this.stemEndIndex;
	}

	/* step6() removes a final -e if countConsonantsInStem() > 1. */
	private void step6()
	{
		this.stemEndIndex = this.wordEndIndex;
		if (this.buffer[this.wordEndIndex] == 'e')
		{
			final int a = countConsonantsInStem();
			if (a > 1 || a == 1 && !isConsonantVowelConsonant(this.wordEndIndex - 1))
				this.wordEndIndex--;
		}
		if (this.buffer[this.wordEndIndex] == 'l' && isRepeatedConsonant(this.wordEndIndex) && countConsonantsInStem() > 1)
			this.wordEndIndex--;
	}

	public int getGrowSize()
	{
		return growSize;
	}

	public Locale getLocale()
	{
		return locale;
	}
}
