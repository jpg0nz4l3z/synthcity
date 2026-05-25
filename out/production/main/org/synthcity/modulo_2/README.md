# Revision simple de integracion Sprint 3 - Modulo 2

## Donde esta esta revision

- Rama base del equipo: `feature/modulo2-simulacion-iterativa`
- Rama de revision: `feature/modulo2-revision-integracion-sm`
- Archivo: `src/main/java/org/synthcity/modulo_2/README.md`

## Equipo Modulo 2

- Nicolas Pardo: Scrum Master y simulacion iterativa.
- Alfa Umaro Jalo: efectos espaciales.
- Juan Pablo Gonzalez: contrato de `ResultadoSimulacion`.

## Estado general

Desde el Modulo 2 ya tenemos preparadas nuestras ramas principales para el
Sprint 3. Por ahora no vemos necesario tocar mas codigo del Modulo 2.

Lo que tenemos que hacer ahora es coordinarnos con los otros modulos antes del
merge general. Si algun modulo cambia firmas, paquetes o imports, entonces si
haremos un ajuste pequeno en nuestra rama.

## Que necesitamos de cada modulo

### Modulo 1

Necesitamos confirmar cual sera la API final de ciudad y bloques.

Puntos importantes para Modulo 2:

- `getRadioInfluencia()`
- `esGeneradorDemanda()`
- posiciones de bloques activos
- expansion de ciudad

Con eso podemos asegurar que los efectos espaciales y la simulacion iterativa
funcionen bien.

### Modulo 3

Necesitamos que Modulo 3 use los datos que ya entrega `ResultadoSimulacion`.

Lo importante es evitar que se recalculen por separado datos como energia,
servicios, contaminacion, bienestar o estabilidad si ya vienen desde Modulo 2.
Asi todos trabajamos con la misma informacion.

### Modulo 4

Necesitamos que Modulo 4 valide el flujo completo de ejecucion:

`Modulo 1 -> Modulo 2 -> Modulo 3 -> Modulo 4`

Puntos a revisar:

- imports finales
- paquetes finales de evaluacion y prediccion
- GUI
- JDBC
- ejecucion del proyecto completo

## Decision actual del Modulo 2

Por el momento no modificamos mas codigo.

Solo nos adaptaremos si:

- Modulo 1 cambia la API final que consume el simulador.
- Modulo 3 necesita algun dato extra en `ResultadoSimulacion`.
- Modulo 4 detecta algun problema real al ejecutar el flujo completo.

## Mensaje general para la integracion

Desde el Modulo 2 dejamos la parte preparada con las ramas actuales. De momento
no vemos necesario modificar mas codigo. Solo necesitamos confirmar la API final
de Modulo 1, que Modulo 3 consuma directamente nuestros resultados, y que Modulo
4 valide el flujo completo de ejecucion. Si alguno cambia firmas, paquetes o
imports, nos adaptamos antes del merge general.
