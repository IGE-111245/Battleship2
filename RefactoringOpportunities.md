# Refactoring Opportunities

| Local | Code Smell | Refabricação | Nº Aluno |
|---|---|---|---|
| Move::processEnemyFire | Long Method | Extract Method — getOutsideShots | 111245 |
| Move::processEnemyFire | Long Method | Extract Method — printVerboseResult | 111245 |
| Move::extracted | Bad Name | Rename — extracted to printVerboseResult | 111245 |
| Fleet::colisionRisk | Bad Name | Rename — colisionRisk to collisionRisk | 111245 |
| LLMService::RequestBody | Magic Number | Introduce Constant — TEMPERATURE | 111245 |
| LLMService::cleanJSON | Long Method | Extract Method — removeMarkdownFences | 111245 |
| Caravel::constructor | Duplicated Code | Extract Method — fillVertical | 111519 |
| Caravel::constructor | Duplicated Code | Extract Method — fillHorizontal | 111519 |
| Carrack::constructor | Duplicated Code | Extract Method — fillVertical | 111519 |
| Carrack::constructor | Duplicated Code | Extract Method — fillHorizontal | 111519 |
| Frigate::constructor | Duplicated Code | Extract Method — fillVertical | 111519 |
| Frigate::constructor | Duplicated Code | Extract Method — fillHorizontal | 111519 |
| Position::adjacentPositions | Long Method | Introduce Constant — DIRECTIONS | 111519 |
| Position::adjacentPositions | Long Method | Extract Method — collectValidNeighbours | 111519 |
