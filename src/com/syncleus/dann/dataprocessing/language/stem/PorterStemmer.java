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

import java.util.Arrays;

/** gets the stem of a word. */
public class PorterStemmer implements Stemmer
{
	private char[] buffer;
	private int bufferSize;
	private int stemEndIndex;
	private int wordEndIndex;
	private int stemStartIndex;
	private boolean dirtyBuffer;
	private final int growSize;

	public PorterStemmer()
	{
		this.growSize = 50;
		this.buffer = new char[growSize];
		this.dirtyBuffer = false;
	}

	public PorterStemmer(int growSize)
	{
		this.growSize = growSize;
		this.buffer = new char[growSize];
		this.dirtyBuffer = false;
	}

	public String stemWord(String originalWord)
	{
		this.dirtyBuffer = false;
		this.bufferSize = originalWord.toCharArray().length;
		this.buffer = Arrays.copyOf(originalWord.toCharArray(), this.bufferSize);

		stemStartIndex = 0;
		wordEndIndex = bufferSize - 1;
		if(wordEndIndex > stemStartIndex + 1)
		{
			step1();
			step2();
			step3();
			step4();
			step5();
			step6();
		}

		if(bufferSize != wordEndIndex + 1)
			dirtyBuffer = true;
		bufferSize = wordEndIndex + 1;

		if( dirtyBuffer )
			return new String(this.buffer, 0, this.bufferSize);
		else
			return originalWord;
	}

	private boolean isConsonant(int i)
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

	private int countConsonantsInStem()
	{
		int n = 0;
		int i = this.stemStartIndex;
		while(true)
		{
			if(i > this.stemEndIndex)
				return n;
			if(!this.isConsonant(i))
				break;
			i++;
		}
		i++;
		while(true)
		{
			while(true)
			{
				if(i > this.stemEndIndex)
					return n;
				if(this.isConsonant(i))
					break;
				i++;
			}
			i++;
			n++;
			while(true)
			{
				if(i > this.stemEndIndex)
					return n;
				if(!this.isConsonant(i))
					break;
				i++;
			}
			i++;
		}
	}

	private boolean isVowelInStem()
	{
		for(int bufferIndex = this.stemStartIndex; bufferIndex <= this.stemEndIndex; bufferIndex++)
			if(!this.isConsonant(bufferIndex))
				return true;
		return false;
	}

	private boolean isRepeatedConsonant(int testCharacterIndex)
	{
		return testCharacterIndex >= this.stemStartIndex + 1 && this.buffer[testCharacterIndex] == this.buffer[testCharacterIndex - 1] && this.isConsonant(testCharacterIndex);
	}

	private boolean isConsonantVowelConsonant(int i)
	{
		if(i < this.stemStartIndex + 2 || !this.isConsonant(i) || this.isConsonant(i - 1) || !this.isConsonant(i - 2))
			return false;
		else
		{
			int ch = this.buffer[i];
			if(ch == 'w' || ch == 'x' || ch == 'y')
				return false;
		}
		return true;
	}

	private boolean ends(String possibleEnding)
	{
		int possibleEndingLength = possibleEnding.length();
		int o = this.wordEndIndex - possibleEndingLength + 1;
		if(o < this.stemStartIndex)
			return false;
		for(int i = 0; i < possibleEndingLength; i++)
			if(buffer[o + i] != possibleEnding.charAt(i))
				return false;
		this.stemEndIndex = this.wordEndIndex - possibleEndingLength;
		return true;
	}

	private void setTo(String setString)
	{
		for(int i = 0; i < setString.length(); i++)
			this.buffer[this.stemEndIndex + 1 + i] = setString.charAt(i);
		this.wordEndIndex = this.stemEndIndex + setString.length();
		dirtyBuffer = true;
	}

	private void setToConsonantStem(String setString)
	{
		if(this.countConsonantsInStem() > 0)
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
		if(buffer[wordEndIndex] == 's')
			if(ends("sses"))
				wordEndIndex -= 2;
			else if(ends("ies"))
				setTo("i");
			else if(buffer[wordEndIndex - 1] != 's')
				wordEndIndex--;
		if(ends("eed"))
		{
			if(countConsonantsInStem() > 0)
				wordEndIndex--;
		}
		else if((ends("ed") || ends("ing")) && isVowelInStem())
		{
			wordEndIndex = stemEndIndex;
			if(ends("at"))
				setTo("ate");
			else if(ends("bl"))
				setTo("ble");
			else if(ends("iz"))
				setTo("ize");
			else if(isRepeatedConsonant(wordEndIndex))
			{
				int ch = buffer[wordEndIndex--];
				if(ch == 'l' || ch == 's' || ch == 'z')
					wordEndIndex++;
			}
			else if(countConsonantsInStem() == 1 && isConsonantVowelConsonant(wordEndIndex))
				setTo("e");
		}
	}

	/* step2() turns terminal y to bufferIndex when there is another vowel in the stem. */
	private void step2()
	{
		if(ends("y") && isVowelInStem())
		{
			buffer[wordEndIndex] = 'i';
			dirtyBuffer = true;
		}
	}

	/* step3() maps double suffices to single ones. so -ization ( = -ize plus
	-ation) maps to -ize etc. note that the string before the suffix must give
	countConsonantsInStem() > 0. */
	private void step3()
	{
		if(wordEndIndex == stemStartIndex)
			return;
		switch(buffer[wordEndIndex - 1])
		{
		case 'a':
			if(ends("ational"))
			{
				setToConsonantStem("ate");
				break;
			}
			if(ends("tional"))
			{
				setToConsonantStem("tion");
				break;
			}
			break;
		case 'c':
			if(ends("enci"))
			{
				setToConsonantStem("ence");
				break;
			}
			if(ends("anci"))
			{
				setToConsonantStem("ance");
				break;
			}
			break;
		case 'e':
			if(ends("izer"))
			{
				setToConsonantStem("ize");
				break;
			}
			break;
		case 'l':
			if(ends("bli"))
			{
				setToConsonantStem("ble");
				break;
			}
			if(ends("alli"))
			{
				setToConsonantStem("al");
				break;
			}
			if(ends("entli"))
			{
				setToConsonantStem("ent");
				break;
			}
			if(ends("eli"))
			{
				setToConsonantStem("e");
				break;
			}
			if(ends("ousli"))
			{
				setToConsonantStem("ous");
				break;
			}
			break;
		case 'o':
			if(ends("ization"))
			{
				setToConsonantStem("ize");
				break;
			}
			if(ends("ation"))
			{
				setToConsonantStem("ate");
				break;
			}
			if(ends("ator"))
			{
				setToConsonantStem("ate");
				break;
			}
			break;
		case 's':
			if(ends("alism"))
			{
				setToConsonantStem("al");
				break;
			}
			if(ends("iveness"))
			{
				setToConsonantStem("ive");
				break;
			}
			if(ends("fulness"))
			{
				setToConsonantStem("ful");
				break;
			}
			if(ends("ousness"))
			{
				setToConsonantStem("ous");
				break;
			}
			break;
		case 't':
			if(ends("aliti"))
			{
				setToConsonantStem("al");
				break;
			}
			if(ends("iviti"))
			{
				setToConsonantStem("ive");
				break;
			}
			if(ends("biliti"))
			{
				setToConsonantStem("ble");
				break;
			}
			break;
		case 'g':
			if(ends("logi"))
			{
				setToConsonantStem("log");
				break;
			}
		}
	}

	/* step4() deals with -ic-, -full, -ness etc. similar strategy to step3. */
	private void step4()
	{
		switch(buffer[wordEndIndex])
		{
		case 'e':
			if(ends("icate"))
			{
				setToConsonantStem("ic");
				break;
			}
			if(ends("ative"))
			{
				setToConsonantStem("");
				break;
			}
			if(ends("alize"))
			{
				setToConsonantStem("al");
				break;
			}
			break;
		case 'i':
			if(ends("iciti"))
			{
				setToConsonantStem("ic");
				break;
			}
			break;
		case 'l':
			if(ends("ical"))
			{
				setToConsonantStem("ic");
				break;
			}
			if(ends("ful"))
			{
				setToConsonantStem("");
				break;
			}
			break;
		case 's':
			if(ends("ness"))
			{
				setToConsonantStem("");
				break;
			}
			break;
		}
	}

	/* step5() takes off -ant, -ence etc., in context <c>vcvc<v>. */
	private void step5()
	{
		if(wordEndIndex == stemStartIndex)
			return;
		switch(buffer[wordEndIndex - 1])
		{
		case 'a':
			if(ends("al"))
				break;
			return;
		case 'c':
			if(ends("ance"))
				break;
			if(ends("ence"))
				break;
			return;
		case 'e':
			if(ends("er"))
				break;
			return;
		case 'i':
			if(ends("ic"))
				break;
			return;
		case 'l':
			if(ends("able"))
				break;
			if(ends("ible"))
				break;
			return;
		case 'n':
			if(ends("ant"))
				break;
			if(ends("ement"))
				break;
			if(ends("ment"))
				break;
			/* element etc. not stripped before the countConsonantsInStem */
			if(ends("ent"))
				break;
			return;
		case 'o':
			if(ends("ion") && stemEndIndex >= 0 && (buffer[stemEndIndex] == 's' || buffer[stemEndIndex] == 't'))
				break;
			/* stemEndIndex >= 0 fixes Bug 2 */
			if(ends("ou"))
				break;
			return;
		/* takes care of -ous */
		case 's':
			if(ends("ism"))
				break;
			return;
		case 't':
			if(ends("ate"))
				break;
			if(ends("iti"))
				break;
			return;
		case 'u':
			if(ends("ous"))
				break;
			return;
		case 'v':
			if(ends("ive"))
				break;
			return;
		case 'z':
			if(ends("ize"))
				break;
			return;
		default:
			return;
		}
		if(countConsonantsInStem() > 1)
			wordEndIndex = stemEndIndex;
	}

	/* step6() removes a final -e if countConsonantsInStem() > 1. */
	private void step6()
	{
		stemEndIndex = wordEndIndex;
		if(buffer[wordEndIndex] == 'e')
		{
			int a = countConsonantsInStem();
			if(a > 1 || a == 1 && !isConsonantVowelConsonant(wordEndIndex - 1))
				wordEndIndex--;
		}
		if(buffer[wordEndIndex] == 'l' && isRepeatedConsonant(wordEndIndex) && countConsonantsInStem() > 1)
			wordEndIndex--;
	}
}
