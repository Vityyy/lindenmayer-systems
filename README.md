# 🌿 L-Systems in Clojure

<div align="center">
  <img src="https://img.shields.io/badge/Clojure-5881D8?style=for-the-badge&logo=clojure&logoColor=white" />
  <img src="https://img.shields.io/badge/Functional_Programming-663399?style=for-the-badge&logo=clojure&logoColor=white" />
  <img src="https://img.shields.io/badge/Fractals-FF6B35?style=for-the-badge&logo=mathworks&logoColor=white" />
  <img src="https://img.shields.io/badge/Programming_Paradigms-2E8B57?style=for-the-badge&logo=academia&logoColor=white" />
</div>

<div align="center">
  <img src="https://user-images.githubusercontent.com/73097560/115834477-dbab4500-a447-11eb-908a-139a6edaec5c.gif">
</div>

> **📚 Academic Project Notice**  
> This project was developed during our **third year** of the Computer Engineering program at the University of Buenos Aires. _This repository **does not** reflect our current programming level or professional skills. It is kept here as an academic and knowledge record._

---

## 🤖 About

**L-Systems** is a functional implementation of **Lindenmayer Systems** (L-Systems) coded entirely in **Clojure**. This project explores parallel rewriting systems and formal grammars, demonstrating how simple rules can generate complex fractal patterns and simulate natural growth processes like plant development.

### 🎯 ¿What are L-Systems?
L-Systems are **parallel rewriting systems** that use:
- **🌱 Simple rules** to generate complex structures
- **🔄 Iterative expansion** of symbolic strings
- **🎨 Geometric interpretation** to create visual patterns
- **📐 Mathematical precision** in fractal generation

### 🔧 Technical Features
- **Pure Functional Programming** leveraging Clojure's immutable data structures
- **Parallel String Rewriting** for efficient L-System generation
- **Rule-Based Grammar** system for pattern definition
- **Recursive Pattern Generation** through functional composition

---

## 📋 Prerequisites

- **Java JDK 8+** (required for Clojure runtime)
- **Clojure CLI** tools installed
- **Leiningen** or **deps.edn** for dependency management

---

## 🛠️ Quick Start

### Installation
```bash
# Clone the repository
git clone https://github.com/Vityyy/L-Systems.git
cd L-Systems
```

### Using Leiningen
```bash
# Install dependencies
lein deps

# Run REPL
lein repl

# Run examples
lein run
```

### Using Clojure CLI
```bash
# Run with deps.edn
clj -M:dev

# Start REPL
clj
```

---

## 🌟 Example L-Systems

### 🌿 Classic Patterns
- **Koch Curve** — Fractal snowflake generation
- **Sierpinski Triangle** — Classic recursive triangle
- **Dragon Curve** — Space-filling dragon fractal
- **Plant Growth** — Biological growth simulation

### 🔧 Custom Rules
Define your own L-System rules:
```clojure
;; Example: Simple branching pattern
{:axiom "F"
 :rules {"F" "F[+F]F[-F]F"}}
```

---

## 🧪 Running Examples

### Interactive REPL Session
```clojure
;; Load the L-Systems namespace
(require '[l-systems.core :as ls])

;; Generate a simple L-System
(ls/generate-l-system axiom rules iterations)

;; Visualize patterns
(ls/draw-l-system pattern)
```

### Example Generations
```clojure
;; Koch curve after 3 iterations
(ls/koch-curve 3)

;; Plant growth simulation
(ls/plant-growth 5)
```

---

## 💡 Key Concepts
- **Functional Programming** paradigms and principles
- **Recursive Data Structures** and their manipulation
- **Lazy Sequences** for efficient memory usage
- **Pattern Matching** and rule-based systems
- **Mathematical Modeling and precision** through code in fractal generation
- **Immutable Data Structures** for safe parallel processing

---

## 🔧 Project Structure

```
src/
├── l_systems/
│   ├── core.clj           # Main L-System engine
│   ├── rules.clj          # Rule definitions
│   ├── patterns.clj       # Common L-System patterns
│   └── visualization.clj  # Rendering and display
test/
├── l_systems/
│   └── core_test.clj      # Unit tests
```

---


## 🧪 Testing

Run the test suite:
```bash
lein test
```

---

<div align="center">
  <img src="https://user-images.githubusercontent.com/73097560/115834477-dbab4500-a447-11eb-908a-139a6edaec5c.gif">
  
  **Built with 🌿 Clojure & 📐 Mathematics at Universidad de Buenos Aires**
</div>
