# Refactoring Opportunities 
| Local | Code Smell | Refabricação | Nº Aluno | Nome |
|---|---|---|---|
| Move::processEnemyFire | Long Method | Extract Method — getOutsideShots | 111245 | Martim Reis
| Move::processEnemyFire | Long Method | Extract Method — printVerboseResult | 111245 | Martim Reis
| Move::extracted | Bad Name | Rename — extracted to printVerboseResult | 111245 | Martim Reis
| Fleet::colisionRisk | Bad Name | Rename — colisionRisk to collisionRisk | 111245 | Martim Reis
| LLMService::RequestBody | Magic Number | Introduce Constant — TEMPERATURE | 111245 | Martim Reis
| LLMService::cleanJSON | Long Method | Extract Method — removeMarkdownFences | 111245 | Martim Reis
| Caravel::constructor | Duplicated Code | Extract Method — fillVertical | 111519 | Vicente Viela
| Caravel::constructor | Duplicated Code | Extract Method — fillHorizontal | 111519 | Vicente Viela
| Carrack::constructor | Duplicated Code | Extract Method — fillVertical | 111519 | Vicente Viela
| Carrack::constructor | Duplicated Code | Extract Method — fillHorizontal | 111519 | Vicente Viela
| Frigate::constructor | Duplicated Code | Extract Method — fillVertical | 111519 | Vicente Viela
| Frigate::constructor | Duplicated Code | Extract Method — fillHorizontal | 111519 | Vicente Viela
| Position::adjacentPositions | Long Method | Introduce Constant — DIRECTIONS | 111519 | Vicente Viela
| Position::adjacentPositions | Long Method | Extract Method — collectValidNeighbours | 111519 | Vicente Viela
