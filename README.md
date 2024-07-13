# Objective

Offer a simple interface for using a lucene index as a
classical [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) repository.

# API

- ro.go.adrhc.persistence.lucene.typedindex.FSIndexRepository
- ro.go.adrhc.persistence.lucene.typedindex.IndexOperations

# Examples

- ro.go.adrhc.persistence.lucene.index.album.AlbumsCrudTest
- ro.go.adrhc.persistence.lucene.index.person.SearchPerformanceTest

### Detailed development flow

AbstractAlbumsIndexTest and AbstractPersonsIndexTest use:

- TestIndexContext.createTypedIndexSpec to create TypedIndexContext
    - uses TypedIndexFactoriesParamsFactory.create
- IndexRepositoryFactory.create(typedIndexContext) to create IndexRepository
- IndexRepository is then used to access the lucene index

# Articles

[Lucene Fields and Term Vectors](https://northcoder.com/post/lucene-fields-and-term-vectors/)  
[Apache Lucene - Query Parser Syntax](https://lucene.apache.org/core/2_9_4/queryparsersyntax.html)  
