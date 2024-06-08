(ns algo3-tp2-sistemasl.core
  (:require
    [clojure.string :as str]))

(defn procesar-entrada [nombre-archivo]
  (str/split (slurp (str "resources/" nombre-archivo)) #"\n") )

(defn nueva-tortuga [coord-x coord-y angulo pluma-abajo]
  {:x coord-x :y coord-y :angulo-cons angulo :angulo-act angulo :pluma pluma-abajo})

(defn adelante [tortuga n]
  (let [angulo-radianes (Math/toRadians (:angulo tortuga))
        dx (* n (Math/cos angulo-radianes))
        dy (* n (Math/sin angulo-radianes))]
    (-> tortuga
        (update :x + dx)
        (update :y + dy))))

(defn invertir [tortuga]
  (update tortuga :angulo-actual + 180))

(defn derecha [tortuga]
  (update tortuga :angulo-actual - :angulo-cons))

(defn izquierda [tortuga]
  (update tortuga :angulo-actual + :angulo-cons))

(defn pluma-arriba [tortuga]
  (update tortuga :pluma false))

(defn pluma-abajo [tortuga]
  (update tortuga :pluma true))

(defn apilar-tortuga [tortuga pila]
  (conj pila tortuga))
(defn desapilar-tortuga [pila]
  (rest pila))

(defn detectar-movimiento [paso tortuga-vieja tortuga-nueva historial]
  (if (or (= paso "F") (= paso "G"))
    (let [x1 (:x tortuga-vieja)
          y1 (:y tortuga-vieja)
          x2 (:x tortuga-nueva)
          y2 (:y tortuga-nueva)
          mov {:x1 x1 :y1 y1 :x2 x2 :y2 y2}]
      (conj historial mov))

    historial
    ))

(defn calcular-limites [historial]
  (let [minimo-x (reduce min (concat (map :x1 historial) (map :x2 historial)))
        minimo-y (reduce min (concat (map :y1 historial) (map :y2 historial)))
        maximo-x (reduce max (concat (map :x1 historial) (map :x2 historial)))
        maximo-y (reduce max (concat (map :y1 historial) (map :y2 historial)))]
    ([minimo-x minimo-y maximo-x maximo-y]))
  )

(defn escribir-svg [historial nombre-archivo]
  (let [
        mov (last historial)
        linea (str "<line x1=\"" (:x1 mov) "\"" " y1=\"" (:y1 mov) "\"" " x2=\"" (:x2 mov) "\"" " y2=\"" (:y2 mov) "\"" " stroke-width=\"1\" stroke=\"black\" />")
        rest (rest historial)
        ]
    (spit nombre-archivo linea)
    (if (not-empty rest)
      (escribir-svg rest nombre-archivo))))

(defn escribir-svg-wrapper [historial nombre-archivo]
  (let [limites(calcular-limites historial)]
    (spit nombre-archivo (str "<svg viewBox=\"" (limites 1) "\"" (limites 2) "\"" (limites 3) "\"" (limites 4) "\"" "xmlns=\"http://www.w3.org/2000/svg\" />")))

  (escribir-svg historial nombre-archivo)
  (spit nombre-archivo "</svg>")
  )
(defn dibujar [pila expresion historial]
  (let [
        tortuga (first pila)
        paso (str (first expresion))
        tortuga-nueva (cond
                        (or (= paso "F") (= paso "G")) (adelante tortuga 1)
                        (or (= paso "f") (= paso "g")) (-> tortuga (pluma-arriba) (adelante 1) (pluma-abajo))
                        (= paso "+") (derecha tortuga)
                        (= paso "-") (izquierda tortuga)
                        (= paso "|") (invertir tortuga))

        nueva_pila (cond
                     (= paso "[") (apilar-tortuga tortuga pila)
                     (= paso "]") (desapilar-tortuga pila)
                     (not (nil? tortuga-nueva)) (apilar-tortuga tortuga-nueva (desapilar-tortuga pila)))

        historial-actualizado (detectar-movimiento paso tortuga tortuga-nueva historial)]
    (if (not-empty expresion)
      (dibujar nueva_pila (rest expresion) historial-actualizado)
      historial-actualizado)
  ))

(defn wrap-tortuga [angulo expresion]
  (let [tortuga (nueva-tortuga 0 0 angulo true)
        pila '(tortuga)
        historial '()]

    (dibujar pila expresion historial)))

(defn hallar-expresion [expresion-actual reglas]
  (if (empty? expresion-actual)
    ""
    (let [elemento-actual (str (first expresion-actual))
          regla (get reglas (keyword elemento-actual) elemento-actual)]

      (str regla (hallar-expresion (rest expresion-actual) reglas)))))

(defn wrapper-hallar-expresion [axioma reglas iteraciones]
  (if (zero? iteraciones) ())
  )

(defn convertir-reglas-a-diccionario [reglas]
  (into {}
        (map (fn [r]
               (let [parts (str/split r #" ")]
                 [(keyword (first parts)) (second parts)]))
             reglas)))

(defn -main [nombre-entrada iteraciones nombre-svg]
  (let [entrada (procesar-entrada nombre-entrada)
        angulo (first entrada)
        axioma (entrada 1)
        reglas (convertir-reglas-a-diccionario (subvec entrada 2))
        ]

    (escribir-svg-wrapper (wrap-tortuga angulo (hallar-expresion axioma reglas)) nombre-svg)
  ))