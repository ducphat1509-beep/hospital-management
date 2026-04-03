Coding Rules:

- Use MVC architecture
- Entity → DAO → Service → Controller

DAO:
- Only handle database operations
- Use Hibernate Session

Service:
- Handle business logic
- Validate before DAO

Controller:
- Only call Service

Mapping:
- Use correct Hibernate annotations
- Use mappedBy for relationships