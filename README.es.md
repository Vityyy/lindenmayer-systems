<p align="right">
  <a href="README.md">English</a> | <strong>Español</strong>
</p>

# Sistemas de Lindenmayer en Clojure

Un pequeño intérprete de L-Systems para línea de comandos escrito en Clojure.
Lee una gramática formal, aplica sus reglas de producción, interpreta los
símbolos resultantes como comandos de gráficos de tortuga y escribe el dibujo
como SVG.

![Copo de Koch generado por este proyecto](doc/snowflake.svg)

La imagen de arriba se genera desde `resources/nieve.sl` con cuatro iteraciones
de reescritura.

## Cómo funciona

El programa usa un pipeline compacto y determinístico:

1. Parsea un ángulo, un axioma y reglas de producción desde un recurso `.sl`.
2. Reescribe cada símbolo durante la cantidad de iteraciones solicitada.
3. Interpreta la expresión expandida como transiciones inmutables de una tortuga.
4. Usa una pila para guardar y restaurar el estado en estructuras ramificadas.
5. Calcula los límites del dibujo y serializa sus segmentos como SVG.

Comandos de dibujo soportados:

| Símbolo | Acción |
| --- | --- |
| `F`, `G` | Avanzar dibujando |
| `f`, `g` | Avanzar sin dibujar |
| `+` | Girar a la derecha según el ángulo configurado |
| `-` | Girar a la izquierda según el ángulo configurado |
| `\|` | Invertir la dirección 180 grados |
| `[` | Apilar el estado actual de la tortuga |
| `]` | Restaurar el estado anterior de la tortuga |

## Ejecución

Requisitos:

- Java JDK 8 o posterior
- [Leiningen](https://leiningen.org/)

Cloná el repositorio y ejecutá una de las definiciones incluidas:

```bash
git clone https://github.com/Vityyy/lindenmayer-systems.git
cd lindenmayer-systems
lein run koch1.sl 4 output.svg
```

Los argumentos son:

```text
lein run <archivo-en-resources> <iteraciones> <svg-de-salida>
```

Los archivos de entrada se buscan dentro de `resources/`. Usá una ruta de
salida nueva o eliminá el archivo anterior: la implementación académica
original escribe en modo append.

## Formato de entrada

Cada definición contiene el ángulo de giro, el axioma y una regla de producción
por cada línea restante:

```text
90
F
F F-F+F+F-F
```

El repositorio incluye sistemas de Koch, Sierpinski, Dragon, Levy, Peano,
árboles, grillas y otros ejemplos dentro de `resources/`.

## Tests

```bash
lein test
```

Los tests cubren reescritura de expresiones, parsing de reglas y recursos,
movimiento, rotación, comportamiento inmutable de la pila, ramificación,
límites del dibujo y salida SVG.

## Estructura del proyecto

```text
project.clj                         Definición del proyecto Leiningen
src/algo3_tp2_sistemasl/core.clj   Motor de L-System, intérprete y CLI
test/algo3_tp2_sistemasl/          Tests unitarios
resources/                         Definiciones de ejemplo
doc/snowflake.svg                  Salida generada que se muestra arriba
```

La única dependencia de runtime es Clojure 1.11.1 y su biblioteca estándar.

## Contexto académico

Este proyecto fue desarrollado en 2024 como trabajo universitario. Se conserva
como una muestra honesta de aquella implementación: el código fuente no fue
retocado para simular una aplicación comercial más reciente. Mi trabajo backend
posterior es considerablemente más avanzado, por lo que este repositorio no
debe interpretarse como una representación completa de mis prácticas actuales
en producción.

## Licencia

Distribuido bajo la [Eclipse Public License 2.0](LICENSE).
