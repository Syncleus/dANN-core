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
package com.syncleus.tests.dann.classify.naive.bayes.fisher;

import com.syncleus.dann.classify.naive.bayes.fisher.*;
import org.junit.*;

public class TestStemmingLanguageFisherClassifier
{
	@Test
	public void testClassify()
	{
		final TrainableLanguageFisherClassifier<Integer> classifier = new StemmingLanguageFisherClassifier<Integer>();

		//train
		classifier.train("Money is the root of all evil!", 1);
		classifier.train("Money destroys the soul", 1);
		classifier.train("Money kills!", 1);
		classifier.train("The quick brown fox.", 1);
		classifier.train("Money should be here once", 2);
		classifier.train("some nonsense to take up space", 2);
		classifier.train("Even more nonsense cause we can", 2);
		classifier.train("nonsense was the root of all good", 2);
		classifier.train("just a filler to waste space", 2);

		//test
		Assert.assertTrue("Feature had incorrect category!", classifier.featureClassification("Money") == 1);
		Assert.assertTrue("Feature had incorrect category!", classifier.featureClassification("Fox") == 1);
		Assert.assertTrue("Feature had incorrect category!", classifier.featureClassification("Nonsense") == 2);
		Assert.assertTrue("Feature had incorrect category!", classifier.featureClassification("Waste") == 2);
		Assert.assertTrue("Feature had incorrect category!", classifier.featureClassification("Evil") == 1);
		Assert.assertTrue("Feature had incorrect category!", classifier.featureClassification("Good") == 2);

		Assert.assertTrue("Item had incorrect category!", classifier.classification("Money was here once") == 2);
		Assert.assertTrue("Item had incorrect category!", classifier.classification("Money destroys the quick brown fox!") == 1);
		Assert.assertTrue("Item had incorrect category!", classifier.classification("kills the soul") == 1);
		Assert.assertTrue("Item had incorrect category!", classifier.classification("nonsense is the root of good") == 2);
	}
}
