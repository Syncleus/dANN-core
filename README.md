# dANN
[![Build Status](http://builds.syncleus.com/buildStatus/icon?job=dANN Core&style=flat)](http://sonar.syncleus.com/dashboard/index/8265)
[![Test Coverage](https://img.shields.io/sonar/http/sonar.syncleus.com/com.syncleus.dann:dann-core/coverage.svg?style=flat)](http://sonar.syncleus.com/dashboard/index/8265)
[![Tests Passed](https://img.shields.io/jenkins/t/http/builds.syncleus.com/dANN Core.svg?style=flat)](http://sonar.syncleus.com/dashboard/index/8265)
[![Dependencies](https://www.versioneye.com/user/projects/5574bee63363340023000126/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5574bee63363340023000126)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.syncleus.dann/dann-core/badge.png?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.syncleus.dann/dann-core/)


dANN is an Artificial Neural Network library built on top of the [GRAIL](http://wiki.syncleus.com/index.php/GRAIL) and
[Ferma](http://wiki.syncleus.com/index.php/Ferma) frameworks. By leveraging GRAIL features dANN is able to back its
Neural Networks with both in-memory and on-disk representations and is compatable with all major graph databases. This
allows generic tools to be interfaced to accomplish an assortment of extensions such as: distributed processing,
visualizations, indexing, complex graph traversal, [SPARQL](http://en.wikipedia.org/wiki/SPARQL) queries, and much more.

dANN is built on GRAIL, and GRAIL is built on Ferma, which uses a native [TinkerPop](http://www.tinkerpop.com) stack,
this means it can support virtually every graph database available to you. A few examples of supported Graph Databases
are as follows.

* [Titan](http://thinkaurelius.github.io/titan/)
* [Neo4j](http://neo4j.com)
* [OrientDB](http://www.orientechnologies.com/orientdb/)
* [MongoDB](http://www.mongodb.org)
* [Oracle NoSQL](http://www.oracle.com/us/products/database/nosql/overview/index.html)
* [TinkerGraph](https://github.com/tinkerpop/blueprints/wiki/TinkerGraph)

TinkerPop also provides several tools which can be used to work with the stack including the following.

* **Furnace** - Graph analysis utilities
* **Pipes** - A data-flow framework for splitting, merging, filtering, and transforming of data
* **Gremlin** - A graph query language
* **Blueprints** - A standard graph API

Finally, depending on the choice of a graph database backend, it is possible to utilize any of the following features.

* Elastic and linear scaling capabilities
* [ACID](http://en.wikipedia.org/wiki/ACID) and [BASE](http://en.wikipedia.org/wiki/Eventual_consistency) consistency
models
* Distributed data, replication, high availability, and fault tolerance
* [Faunus](https://github.com/thinkaurelius/faunus) Gremlin-based distributed graph analytics, backed by an Hadoop
cluster.
* Direct [Apache Hadoop](http://hadoop.apache.org) integration providing graph analytics, reporting, and ETL features
* Indexing and search utilities including: [ElasticSearch](http://www.elasticsearch.org/overview/elasticsearch),
[Solr](http://lucene.apache.org/solr/), and [Lucene](http://lucene.apache.org)

A Titan backend can support all the above features.

## Obtaining the Source

The official source repository for dANN is located on the Syncleus Gerrit instance and can be cloned using the
following command.

```
git clone http://gerrit.syncleus.com/dANN-core
```

We also maintain a GitHub clone of the official repository which can be found
[here](https://github.com/Syncleus/dANN-core). Finally Syncleus also hosts an instance of GitLab which has a
clone of the repository which can be found [here](http://gitlab.syncleus.com/syncleus/dANN-core).
