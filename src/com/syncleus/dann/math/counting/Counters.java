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
package com.syncleus.dann.math.counting;

import java.math.BigInteger;
import java.util.*;

public final class Counters
{
	private Counters()
	{
		//this class can not be instantiated
	}

	public static <O> BigInteger everyCombinationCount(final Collection<O> superCollection)
	{
		if (superCollection == null)
			throw new IllegalArgumentException("superCollection can not be null");
		final BigInteger combinations = BigInteger.ZERO;
		for(int currentSequenceLength = 1; currentSequenceLength <= superCollection.size(); currentSequenceLength++)
			combinations.add(fixedLengthCombinationCount(superCollection, currentSequenceLength));
		return combinations;
	}

	public static <O> BigInteger fixedLengthCombinationCount(final Collection<O> superCollection, final int length)
	{
		if (superCollection == null)
			throw new IllegalArgumentException("superCollection can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superCollection.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		final Counter generator = new CombinationCounter(superCollection.size(), length);
		return generator.getTotal();
	}

	public static <O> Set<List<O>> everyCombination(final List<O> superList)
	{
		if (superList == null)
			throw new IllegalArgumentException("superList can not be null");
		final Set<List<O>> combinations = new HashSet<List<O>>();
		for(int currentSequenceLength = 1; currentSequenceLength <= superList.size(); currentSequenceLength++)
			combinations.addAll(fixedLengthCombinations(superList, currentSequenceLength));
		return Collections.unmodifiableSet(combinations);
	}

	public static <O> Set<Set<O>> everyCombination(final Set<O> superSet)
	{
		if (superSet == null)
			throw new IllegalArgumentException("superSet can not be null");
		final Set<Set<O>> combinations = new HashSet<Set<O>>();
		for(int currentSequenceLength = 1; currentSequenceLength <= superSet.size(); currentSequenceLength++)
			combinations.addAll(fixedLengthCombinations(superSet, currentSequenceLength));
		return Collections.unmodifiableSet(combinations);
	}

	public static <O> Set<List<O>> fixedLengthCombinations(final List<O> superList, final int length)
	{
		if (superList == null)
			throw new IllegalArgumentException("superList can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superList.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		final Set<List<O>> combinations = new HashSet<List<O>>();
		final Counter generator = new CombinationCounter(superList.size(), length);
		while (generator.hasMore())
		{
			final List<O> combination = new ArrayList<O>();
			final int[] combinationIndexes = generator.getNext();
			for(final int combinationIndex : combinationIndexes)
				combination.add(superList.get(combinationIndex));
			combinations.add(Collections.unmodifiableList(combination));
		}
		return Collections.unmodifiableSet(combinations);
	}

	public static <O> Set<Set<O>> fixedLengthCombinations(final Set<O> superSet, final int length)
	{
		if (superSet == null)
			throw new IllegalArgumentException("superSet can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superSet.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		final List<O> superSetList = new ArrayList<O>(superSet);
		final Set<Set<O>> combinations = new HashSet<Set<O>>();
		final Counter generator = new CombinationCounter(superSet.size(), length);
		while (generator.hasMore())
		{
			final Set<O> combination = new HashSet<O>();
			final int[] combinationIndexes = generator.getNext();
			for(final int combinationIndex : combinationIndexes)
				combination.add(superSetList.get(combinationIndex));
			combinations.add(Collections.unmodifiableSet(combination));
		}
		return Collections.unmodifiableSet(combinations);
	}

	private static <O> Set<List<O>> sameLengthPermutations(final List<O> superList)
	{
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Counter generator = new PermutationCounter(superList.size());
		while (generator.hasMore())
		{
			final List<O> permutation = new ArrayList<O>();
			final int[] permutationIndexes = generator.getNext();
			for(final int permutationIndex : permutationIndexes)
				permutation.add(superList.get(permutationIndex));
			permutations.add(Collections.unmodifiableList(permutation));
		}
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> BigInteger everyPermutationCount(final Collection<O> superCollection)
	{
		if (superCollection == null)
			throw new IllegalArgumentException("superCollection can not be null");
		final BigInteger combinations = BigInteger.ZERO;
		for(int currentSequenceLength = 1; currentSequenceLength <= superCollection.size(); currentSequenceLength++)
			combinations.add(fixedLengthPermutationCount(superCollection, currentSequenceLength));
		return combinations;
	}

	public static <O> BigInteger fixedLengthPermutationCount(final Collection<O> superCollection, final int length)
	{
		if (superCollection == null)
			throw new IllegalArgumentException("superCollection can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superCollection.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		final Counter permutator = new PermutationCounter(length);
		final Counter combinator = new CombinationCounter(superCollection.size(), length);
		final BigInteger combinationCount = combinator.getTotal();
		final BigInteger permutationsPerCount = permutator.getTotal();
		return combinationCount.multiply(permutationsPerCount);
	}

	public static <O> Set<List<O>> everyPermutation(final List<O> superList)
	{
		if (superList == null)
			throw new IllegalArgumentException("superList can not be null");
		//get every combination then permutate it
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Set<List<O>> combinations = everyCombination(superList);
		for(final List<O> combination : combinations)
			permutations.addAll(sameLengthPermutations(combination));
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> Set<List<O>> everyPermutation(final Set<O> superSet)
	{
		if (superSet == null)
			throw new IllegalArgumentException("superSet can not be null");
		return everyPermutation(new ArrayList<O>(superSet));
	}

	public static <O> Set<List<O>> fixedLengthPermutations(final List<O> superList, final int length)
	{
		if (superList == null)
			throw new IllegalArgumentException("superList can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superList.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		//get every combination then permutate it
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Set<List<O>> combinations = fixedLengthCombinations(superList, length);
		for(final List<O> combination : combinations)
			permutations.addAll(sameLengthPermutations(combination));
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> Set<List<O>> fixedLengthPermutations(final Set<O> superSet, final int length)
	{
		if (superSet == null)
			throw new IllegalArgumentException("superSet can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superSet.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		return fixedLengthPermutations(new ArrayList<O>(superSet), length);
	}

	private static <O> Set<List<O>> sameLengthLexicographicPermutations(final List<O> superList)
	{
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Counter generator = new LexicographicPermutationCounter(superList.size());
		while (generator.hasMore())
		{
			final List<O> permutation = new ArrayList<O>();
			final int[] permutationIndexes = generator.getNext();
			for(final int permutationIndex : permutationIndexes)
				permutation.add(superList.get(permutationIndex));
			permutations.add(Collections.unmodifiableList(permutation));
		}
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> BigInteger everyLexicographicPermutationCount(final Collection<O> superCollection)
	{
		if (superCollection == null)
			throw new IllegalArgumentException("superCollection can not be null");
		final BigInteger combinations = BigInteger.ZERO;
		for(int currentSequenceLength = 1; currentSequenceLength <= superCollection.size(); currentSequenceLength++)
			combinations.add(fixedLengthLexicographicPermutationCount(superCollection, currentSequenceLength));
		return combinations;
	}

	public static <O> BigInteger fixedLengthLexicographicPermutationCount(final Collection<O> superCollection, final int length)
	{
		if (superCollection == null)
			throw new IllegalArgumentException("superCollection can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superCollection.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		final Counter permutator = new LexicographicPermutationCounter(length);
		final Counter combinator = new CombinationCounter(superCollection.size(), length);
		final BigInteger combinationCount = combinator.getTotal();
		final BigInteger permutationsPerCount = permutator.getTotal();
		return combinationCount.multiply(permutationsPerCount);
	}

	public static <O> Set<List<O>> everyLexicographicPermutation(final List<O> superList)
	{
		if (superList == null)
			throw new IllegalArgumentException("superList can not be null");
		//get every combination then permutate it
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Set<List<O>> combinations = everyCombination(superList);
		for(final List<O> combination : combinations)
			permutations.addAll(sameLengthLexicographicPermutations(combination));
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> Set<List<O>> everyLexicographicPermutation(final Set<O> superSet)
	{
		if (superSet == null)
			throw new IllegalArgumentException("superSet can not be null");
		return everyLexicographicPermutation(new ArrayList<O>(superSet));
	}

	public static <O> Set<List<O>> fixedLengthLexicographicPermutations(final List<O> superList, final int length)
	{
		if (superList == null)
			throw new IllegalArgumentException("superList can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superList.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		//get every combination then permutate it
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Set<List<O>> combinations = fixedLengthCombinations(superList, length);
		for(final List<O> combination : combinations)
			permutations.addAll(sameLengthLexicographicPermutations(combination));
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> Set<List<O>> fixedLengthLexicographicPermutations(final Set<O> superSet, final int length)
	{
		if (superSet == null)
			throw new IllegalArgumentException("superSet can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superSet.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		return fixedLengthLexicographicPermutations(new ArrayList<O>(superSet), length);
	}

	private static <O> Set<List<O>> sameLengthJohnsonTrotterPermutations(final List<O> superList)
	{
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Counter generator = new JohnsonTrotterPermutationCounter(superList.size());
		while (generator.hasMore())
		{
			final List<O> permutation = new ArrayList<O>();
			final int[] permutationIndexes = generator.getNext();
			for(final int permutationIndex : permutationIndexes)
				permutation.add(superList.get(permutationIndex));
			permutations.add(Collections.unmodifiableList(permutation));
		}
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> BigInteger everyJohnsonTrotterPermutationCount(final Collection<O> superCollection)
	{
		if (superCollection == null)
			throw new IllegalArgumentException("superCollection can not be null");
		final BigInteger combinations = BigInteger.ZERO;
		for(int currentSequenceLength = 1; currentSequenceLength <= superCollection.size(); currentSequenceLength++)
			combinations.add(fixedLengthJohnsonTrotterPermutationCount(superCollection, currentSequenceLength));
		return combinations;
	}

	public static <O> BigInteger fixedLengthJohnsonTrotterPermutationCount(final Collection<O> superCollection, final int length)
	{
		if (superCollection == null)
			throw new IllegalArgumentException("superCollection can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superCollection.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		final Counter permutator = new JohnsonTrotterPermutationCounter(length);
		final Counter combinator = new CombinationCounter(superCollection.size(), length);
		final BigInteger combinationCount = combinator.getTotal();
		final BigInteger permutationsPerCount = permutator.getTotal();
		return combinationCount.multiply(permutationsPerCount);
	}

	public static <O> Set<List<O>> everyJohnsonTrotterPermutation(final List<O> superList)
	{
		if (superList == null)
			throw new IllegalArgumentException("superList can not be null");
		//get every combination then permutate it
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Set<List<O>> combinations = everyCombination(superList);
		for(final List<O> combination : combinations)
			permutations.addAll(sameLengthJohnsonTrotterPermutations(combination));
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> Set<List<O>> everyJohnsonTrotterPermutation(final Set<O> superSet)
	{
		if (superSet == null)
			throw new IllegalArgumentException("superSet can not be null");
		return everyJohnsonTrotterPermutation(new ArrayList<O>(superSet));
	}

	public static <O> Set<List<O>> fixedLengthJohnsonTrotterPermutations(final List<O> superList, final int length)
	{
		if (superList == null)
			throw new IllegalArgumentException("superList can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superList.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		//get every combination then permutate it
		final Set<List<O>> permutations = new HashSet<List<O>>();
		final Set<List<O>> combinations = fixedLengthCombinations(superList, length);
		for(final List<O> combination : combinations)
			permutations.addAll(sameLengthJohnsonTrotterPermutations(combination));
		return Collections.unmodifiableSet(permutations);
	}

	public static <O> Set<List<O>> fixedLengthJohnsonTrotterPermutations(final Set<O> superSet, final int length)
	{
		if (superSet == null)
			throw new IllegalArgumentException("superSet can not be null");
		if (length < 0)
			throw new IllegalArgumentException("length must be >= 0");
		if (length > superSet.size())
			throw new IllegalArgumentException("length can not be larger than the collection size");
		return fixedLengthJohnsonTrotterPermutations(new ArrayList<O>(superSet), length);
	}
}
