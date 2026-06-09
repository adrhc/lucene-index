# Copilot instructions for `lucene-index`

## Build, test, and lint commands

- Prefer Maven Wrapper (`./mvnw`) from repository root.
- Environment is pinned in `.sdkmanrc` (`java=25.0.3-tem`, `maven=3.9.10`); `env.sh` can bootstrap SDKMAN and `MVN`.

```bash
# Fast compile (main + test sources)
./mvnw -e -Dmaven.javadoc.skip=true clean compile test-compile

# Full build used by CI scripts
./mvnw -e -Dmaven.javadoc.skip=true clean install
# (equivalent wrapper script used in Jenkins: ./clean.install.sh)

# Run full unit test suite
./mvnw -e test

# Run a single test class
./mvnw -e -Dtest=PersonCrudTest test

# Run a single test method
./mvnw -e -Dtest=PersonCrudTest#crudTest test
```

- No dedicated lint goal/plugin is configured in `pom.xml` (no Checkstyle/PMD/SpotBugs/Spotless).

## High-level architecture

- Public API is `LuceneIndex<I, T>` (`src/main/java/ro/go/adrhc/persistence/lucene/LuceneIndex.java`), implemented by `LuceneIndexImpl`.
- `LuceneIndexImpl.of(...)` is assembled from `IndexOperationsParams<T>` and composes:
  - `ReadIndexOperations` (query, retrieval, counting, streaming)
  - `WriteIndexOperations` (add/upsert/remove/reset/merge/shallow-update/backup)
- `IndexOperationsParamsBuilder` is the central wiring point:
  - Builds analyzers (including `PerFieldAnalyzerWrapper` for `WORD` fields)
  - Chooses FS or RAM index (RAM when `indexPath` is omitted)
  - Creates reader pool + optional writer (read-only mode supported)

- The model is “typed object over Lucene documents”:
  - `TypedToDocumentConverter` stores both:
    - `raw` field (serialized full object via `RawFieldValueSerdes`)
    - per-field Lucene fields declared in schema
  - `DocumentToTypedConverter` rebuilds objects from `raw`.

- Search pipeline (`service/search`) is split by concern:
  - best-match search
  - reduce-based matching with custom `BestMatchingStrategy`
  - paged/multi-hit search (`ScoreDoc` + `Sort` based APIs)

- Write-side services include:
  - merge (`service/merge`) using entity `merge(...)`
  - shallow synchronization with an external source (`service/shallow`)
  - snapshot backup via Lucene `SnapshotDeletionPolicy` (`service/backup`)

## Key codebase conventions

- Domain entities indexed here should implement `Indexable<ID, T>` (extends `Identifiable` + `Mergeable`).
- Index schema is an enum implementing `LuceneFieldSpec<T>`:
  - Must expose an ID field (default detection: field named `id`, case-insensitive)
  - Field mapping is usually created via `PropertyFieldMapperFactory`
  - Sort behavior is schema-driven via `isSortable()`

- Use `IndexOperationsParamsBuilder.of(entityClass, schemaEnumClass, indexPath?)` to construct indexes; tests use helper wrappers in `IndexOperationsParamsGenerator`.
- Read/write construction follows factory pattern (`ReadIndexOperationsFactory`, `WriteIndexOperationsFactory`)—prefer extending these paths instead of manual wiring.
- `LuceneIndexImpl` auto-commits after each write operation (`executeWrite`), and blocks mutation in read-only mode.
- Test structure convention:
  - shared setup in abstract base tests (`AbstractPersonRAMIndexTest`, `AbstractAlbumsIndexTest`)
  - schema + generator classes colocated with scenario tests (`person/*`, `album/*`)
