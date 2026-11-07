---
name: room-data-layer
description: Room entity/DAO/database/mapper/repository conventions and testing rules.
---

## Conventions
- `Entity`: `@Entity(tableName = "...")`, each daily record keyed by `date: String` (ISO-8601, `UNIQUE`).
- `Dao`: all queries return `Flow<T>` (read) or `suspend` (write); use `@Upsert` for insert-or-update.
- `Database`: `@Database(entities=[...], version=N)`, singleton via `companion object`.
- `Mapper`: pure functions `Entity.toDomain()` and `DomainModel.toEntity()` in a separate `Mappers.kt`.
- `Repository`: interface in `domain/`; implementation in `data/` accepts Dao + maps to domain models.

## UNIQUE date rule
Only one record per calendar day. Always upsert, never plain insert.

## Testing
- DAO tests: use `Room.inMemoryDatabaseBuilder` with `InstantTaskExecutorRule`.
- Repository tests: use in-memory DB, assert `Flow` emissions with `turbine`.
- Never mock the DB; test against real Room.
