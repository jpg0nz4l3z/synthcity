# Proyecto SynthCity — Sprint 1

## 📌 Descripción

Este repositorio contiene el desarrollo del sistema **SynthCity** correspondiente al Sprint 1.
El objetivo es construir un sistema modular que permita generar, simular, evaluar y presentar el estado de una ciudad.

El flujo del sistema es:

Ciudad → ResultadoSimulacion → MetricaCiudad → ResultadoEvaluacion → SalidaTexto

---

## 🌿 Estructura de ramas

Se define la siguiente estructura obligatoria:

* `main` → versión estable del sistema
* `develop` → integración del sprint
* `feature/modulo1`
* `feature/modulo2`
* `feature/modulo3`
* `feature/modulo4`

---

## 🔒 Reglas de ramas protegidas

Las ramas `main` y `develop` están protegidas:

* ❌ No se permite hacer push directo
* ❌ No se trabaja directamente sobre ellas
* ✅ Solo se actualizan mediante **Pull Request**
* ✅ Requieren **al menos 1 revisión obligatoria** antes de hacer merge

---

## 🔄 Flujo de trabajo

1. Cada equipo trabaja en su rama (`feature/moduloX`)
2. Se realizan commits frecuentes y con sentido
3. Se suben los cambios a la rama correspondiente
4. Se crea un Pull Request hacia `develop`
5. Se revisa el código
6. Se hace merge si todo es correcto

Flujo:

```
feature → develop → main
```

---

## 📏 Normas de trabajo

* 🚫 No trabajar directamente sobre `main`
* 🔄 Actualizar la rama antes de empezar a trabajar
* 💬 Usar mensajes de commit claros
* 🧩 Mantener el proyecto siempre compilable
* 🔀 Integrar cambios de forma frecuente (no al final del sprint)
* ⚠️ Resolver conflictos entendiendo el código

---

## 🌱 Uso de ramas

* Cada rama debe tener una única responsabilidad
* Las subramas deben ser cortas (no durar todo el sprint)
* Integración mínima: **al menos una vez por sesión**

---

## 🧪 Integración

Se deben realizar integraciones durante el sprint:

* Inicial → estructura del proyecto
* Intermedia → validación entre módulos
* Final → sistema completo funcionando

---

## 🎯 Objetivo del repositorio

Garantizar que el desarrollo del sistema sea:

* ✔️ Coherente
* ✔️ Integrable
* ✔️ Trazable
* ✔️ Mantenible

El repositorio es parte clave de la infraestructura del sistema, no un elemento opcional.