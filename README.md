# SynthCity - Sprint 2

Proyecto Java/Maven de SynthCity integrado para el Sprint 2.

## Flujo del sistema

```text
Ciudad -> ResultadoSimulacion -> MetricaCiudad -> ResultadoEvaluacion + PredictionResult -> GUI + Persistencia
```

## Modulos

- `modulo_1`: modelo de ciudad, tablero, bloques funcionales, densidad y expansion.
- `modulo_2`: motor de simulacion urbana basica sin `switch` funcional sobre tipos de bloque.
- `modulo_3`: evaluacion, score de viabilidad, alertas, comparacion y prediccion heuristica inicial.
- `modulo_4`: salida textual, GUI JavaFX, resumen visual y persistencia JDBC.

## Validacion

El proyecto se ha validado con:

```powershell
mvn test
```

Para ejecutar la GUI con JavaFX:

```powershell
mvn javafx:run
```

La configuracion JDBC espera la base de datos MySQL definida en `Docker/mysql_jdbc_docker_pack`.
