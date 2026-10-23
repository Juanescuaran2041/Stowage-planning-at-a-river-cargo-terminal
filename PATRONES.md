# PATRONES.md: planificación de estiba en una terminal fluvial

Cinco patrones, cada uno resuelve un problema concreto del caso.

## 1. Factory Method: creación de unidades desde el manifiesto

- **Dónde:** `factory.ManifestRegister` (creador), `ContainerRegister`, `BulkRegister`, `LiquidRegister` (creadores concretos), `models.ChargeUnity` (producto) con `ContainerUnit`, `BulkUnit`, `LiquidUnit`.
- **Problema:** cada tipo de carga llega en un formato de línea distinto y calcula su peso de otra manera. Un solo método con condicionales era inmanejable.
- **Cómo:** `process(lines)` es el método plantilla: recorre las líneas y llama a `createUnit(line)`, el método fábrica, que cada registrador implementa con su parseo. Una línea inválida lanza `IllegalArgumentException`; `process` la anota como rechazada y continúa.

## 2. Abstract Factory: familias por tipo de terminal

- **Dónde:** `terminal.TerminalFabric` con `ContainersFabric`, `BulksFabric`, `LiquidsFabric`. Productos: `ChargeEquipment`, `StabilityValidator`, `ShippingDocument`.
- **Problema:** cada terminal define de forma inseparable su equipo de cargue, su validador de estabilidad y su documento de embarque. Mezclar la grúa con el validador de granel sería un error.
- **Cómo:** cada fábrica crea los tres productos de su familia. `StowagePlanner` recibe una `TerminalFabric` y solo usa las interfaces de producto, así que no sabe en qué terminal trabaja.

## 3. Adapter: instrumentos de nivel del río

- **Dónde:** `river.RiverLevelMeter` (interfaz), `LimnimeterAdapter`, `RadarAdapter`. Clases ajenas en `thirdparty`.
- **Problema:** dos instrumentos de terceros, que no se pueden modificar, exponen interfaces distintas (`getLevelMeters()` y un texto `NIVEL_CM=145;ESTADO=OK`).
- **Cómo:** cada adaptador implementa `RiverLevelMeter`. El del radar interpreta el texto, convierte centímetros a metros y lanza `IllegalStateException` si el estado es `FALLA`. Solo los adaptadores conocen `thirdparty`.

## 4. Bridge: régimen del río x remolcador

- **Dónde:** `voyage.Voyage` (abstracción) con `NormalWatersVoyage`, `LowWatersVoyage`, `RisingVoyage`; `Tugboat` (implementador) con `Rio1800Tugboat`, `Rio3200Tugboat`.
- **Problema:** 3 regímenes x 2 remolcadores serían 6 clases, y un tercer remolcador agregaría 3 más.
- **Cómo:** `Voyage` guarda un `Tugboat` (el puente) y `plan(totalWeight, departure)` combina el factor y las horas por día del régimen con la velocidad y el peso máximo del remolcador. Un remolcador nuevo es una sola clase y no toca ningún régimen.

## 5. Facade: central de la terminal

- **Dónde:** `facade.CentralTerminal` con los `enum` `TerminalType`, `RiverInstrument`, `TugboatType`.
- **Problema:** el cliente tendría que conocer registradores, fábricas, planeador, adaptadores, regímenes y remolcadores, y el orden para usarlos.
- **Cómo:** ofrece `processManifest`, `generatePlan`, `printPlan`, `evaluateRiverLevel` y `planVoyage`. Crea y coordina los objetos internos. `app.Main` solo importa `facade`.

## Algoritmo de estiba

`plan.StowagePlanner` aplica First Fit Decreasing sobre cuatro `plan.Hold`, y calcula balance proa/popa, calado, tiempo de cargue y, con el calado efectivo del río, las toneladas por trasladar o descargar.
