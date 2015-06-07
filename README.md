# dANN 2.x
[![Dependency Status](https://www.versioneye.com/user/projects/5574beea336334002000012b/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5574beea336334002000012b)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.syncleus.dann/dann-core/badge.png?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.syncleus.dann/dann-core/)

dANN is an Artificial Intelligence and Artificial Genetics library targeted at employing conventional algorithms as well
as acting as a platform for research & development of novel algorithms. As new algorithms are developed and proven to be
effective they will be integrated into the core library. It is currently written in Java and is being actively developed
by a small team.

Our intentions are two fold. First, to provide a powerful interface for programs to include conventional machine
learning technology into their code. Second, To act as a testing ground for research and development of new AI concepts.
We provide new AI technology we have developed, and the latest algorithms already on the market. In the spirit of
modular programming the library also provides access to the primitive components giving you greater control over
implementing your own unique algorithms. You can either let our library do all the work, or you can override any step
along the way.

dANN 2.x was completely rewritten for dANN 3.x as such the latest version of dANN will vary significantly from this
version.

For more information check out the [main dANN site](http://wiki.syncleus.com/index.php/dANN/v2.x).

dANN currently implements a large collection of various algorithms. The following is a partial list:

* Graph Theory
    * Search
        * Path Finding
            * A*
            * Dijkstra
            * Bellman-Ford
            * Johnson's
            * Floyd-Warshall
        * Optimization
            * Hill Climbing Local Search
    * Graph Drawing
        * Hyperassociative Map
        * 3D Hyperassociative Map Visualization
    * Cycle Detection
        * Colored Depth-first Search
        * Exhaustive Depth First Search
    * Minimal Spanning Tree Detection (MST)
        * Kruskal
        * Prim
    * Topological Sort Algorithm
* Evolutionary Algorithms
    * Genetic Algorithms
    * Genetic Wavelets
* Naive Classifier
    * Naive Bayes Classifier
    * Naive Fisher Classifier
* Data Processing
    * Signal Processing
        * Cooley Tukey Fast Fourier Transform
    * Language Processing
        * Word Parsing
        * Word Stemming
            * Porter Stemming Algorithm
    * Data Interrelational Graph
* Graphical Models
    * Markov Random Fields
        * Dynamic Markov Random Field
    * Bayesian Networks
        * Dynamic Bayesian Networks
    * Dynamic Graphical Models
        * Hidden Markov Models
            * Baum–Welch Algorithm
            * Layered Hidden Markov Models
            * Hierarchical Hidden Markov Models
* Artificial Neural Networks
    * Activation Function Collection
    * Backpropagation Networks
        * Feedforward Networks
    * Self Organizing Maps
    * Realtime Neural Networks
        * Spiking Neural Networks
            * Izhikevich Algorithm
    * 3D Network Visualization
* Mathematics
    * Statistics
        * Markov Chains
            * Markov Chain Monte Carlo (Parameter Estimation)
    * Counting
        * Combinations
        * Permutations
            * Lexicographic
            * Johnson-Trotter Algorithm
    * Complex Numbers
    * N-Dimensional Vectors
    * Greatest Common Denominator
        * Binary Algorithm
        * Euclidean Algorithm
        * Extended Euclidean Algorithm
    * Linear Algebra
        * Cholesky Decomposition
        * Hessenberg Decomposition
        * Eigenvalue Decomposition
        * LU Decomposition
        * QR Decomposition
        * Singular Value Decomposition

## Maven Dependency

In order to use dANN 2.x add the following dependency to your maven pom.

    <dependency>
        <groupId>com.syncleus.dann</groupId>
        <artifactId>dann-core</artifactId>
        <version>2.1</version>
    </dependency>

## Getting Started

There are several excellent examples listed on the [dANN main site](http://wiki.syncleus.com/index.php/dANN/v2.x).
There are many thing's dANN can do so it would be impossible to come up with any singular example which demonstrates
the full power of the dANN library. So instead we will focus on a simple naive classifier example. Naive classifiers
are powerful, yet simple, tools used to classify data. They are the most common tool used in spam filters for example.
The following example shows how to use a simple naive classifier, though it could be easily modified to work with
dANN's bayes and fisher classifier implementations.

The first step is to create a new classifier we can work with.

    TrainableLanguageNaiveClassifier<Integer> classifier =
         new SimpleLanguageNaiveClassifier<Integer>();

This will create a new classifier where items are classified into categories represented by Integer types. Another
classifier to consider using is StemmingLanguageNaiveClassifier. This classifier is used in the exact same way however
it applies Porter Stemming Algorithm to each word. This will cause words like running and run to be seen as the same
feature. If you want to use this classifier instead you could do the following.

    TrainableLanguageNaiveClassifier<Integer> classifier =
        new StemmingLanguageNaiveClassifier<Integer>();

Next the real magic happens, we train the classifier. In this example there are only 2 categories we train for, 1 and 2.

    classifier.train("Money is the root of all evil!", 1);
    classifier.train("Money destroys the soul", 1);
    classifier.train("Money kills!", 1);
    classifier.train("The quick brown fox.", 1);
    classifier.train("Money should be here once", 2);
    classifier.train("some nonsense to take up space", 2);
    classifier.train("Even more nonsense cause we can", 2);
    classifier.train("nonsense is the root of all good", 2);
    classifier.train("just a filler to waste space", 2);

You'll notice we intentionally trained the classifier with several obvious patterns. Money appears most often in
category 1, and nonsense and space prefers category 2. This will show up when we ask for some classifications in the
next step.

    assert (classifier.featureClassification("Money") == 1);
    assert (classifier.featureClassification("Fox") == 1);
    assert (classifier.featureClassification("Nonsense") == 2);
    assert (classifier.featureClassification("Waste") == 2);
    assert (classifier.featureClassification("Evil") == 1);
    assert (classifier.featureClassification("Good") == 2);

As you can see this simple class will classify the features (words) of a phrase into categories it has previously
learned. Not only can it classify the features within an item but also items themselves.

    assert (classifier.classification("Money was here once") == 2);
    assert (classifier.classification("Money destroys the quick brown fox!") == 1);
    assert (classifier.classification("kills the soul") == 1);
    assert (classifier.classification("nonsense is the root of good") == 1);

## Obtaining the Source

The official source repository for dANN is located on the Syncleus Gerrit instance and can be cloned using the
following command.

```
git clone http://gerrit.syncleus.com/dANN-core
```

Remember this README file is from dANN 2.x which is no longer on the git master branch and has instead moved to the v2.x
branch of the git repository.

We also maintain a GitHub clone of the official repository which can be found
[here](https://github.com/Syncleus/dANN-core). Finally Syncleus also hosts an instance of GitLab which has a
clone of the repository which can be found [here](http://gitlab.syncleus.com/syncleus/dANN-core).
