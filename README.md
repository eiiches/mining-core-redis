mining-core-redis
=================

A collection of redis-backed data structures for mining-core.

WARNING: This library is still in alpha, and the APIs can be drastically changed.


Usage
-----

If you use maven, add the following snippet for your pom.

```xml
<repositories>
    <repository>
        <id>thisptr.net</id>
        <name>thisptr.net</name>
        <url>http://nexus.thisptr.net/content/groups/public</url>
    </repository>
</repositories>
<dependency>
    <groupId>net.thisptr</groupId>
    <artifactId>mining-core-redis</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Implemented structures
----------------------

Currently, SequentialRedisBackedIdMapper is the only implementation in this project.
