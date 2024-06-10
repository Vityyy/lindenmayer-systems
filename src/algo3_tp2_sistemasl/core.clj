(ns algo3-tp2-sistemasl.core
  (:require
    [clojure.string :as str]))
(defn procesar-entrada [nombre-archivo]
  (str/split (slurp (str "resources/" nombre-archivo)) #"\n") )

(defn nueva-tortuga [coord-x coord-y angulo pluma-abajo]
  {:x coord-x :y coord-y :angulo-cons angulo :angulo-act 90 :pluma pluma-abajo})

(defn adelante [tortuga n]
  (let [angulo-radianes (Math/toRadians (get tortuga :angulo-act))
        dx (* n (Math/cos angulo-radianes))
        dy (* n (Math/sin angulo-radianes))]
    (-> tortuga
        (update :x + dx)
        (update :y + dy))))

(defn invertir [tortuga]
  (update tortuga :angulo-act + 180))

(defn derecha [tortuga]
  (update tortuga :angulo-act - (tortuga :angulo-cons)))

(defn izquierda [tortuga]
  (update tortuga :angulo-act + (tortuga :angulo-cons)))

(defn pluma-arriba [tortuga]
  (update tortuga :pluma false))

(defn pluma-abajo [tortuga]
  (update tortuga :pluma true))

(defn apilar-tortuga [tortuga pila]
  (conj pila (into {} tortuga)))

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
        maximo-y (reduce max (concat (map :y1 historial) (map :y2 historial)))
        ancho (- maximo-x minimo-x)
        alto (- maximo-y minimo-y)
        ]
    (vector minimo-x maximo-y ancho alto))
  )

(defn escribir-svg [historial nombre-archivo]
  (let [
        mov (first historial)
        linea (str "  <line x1=\"" (:x1 mov) "\" y1=\"" (-(:y1 mov)) "\" x2=\"" (:x2 mov) "\" y2=\"" (-(:y2 mov)) "\" stroke-width=\"1\" stroke=\"black\" />")
        rest (rest historial)
        ]
    (spit nombre-archivo linea :append true)
    (if (not-empty rest)
      (recur rest nombre-archivo))))

(defn escribir-svg-wrapper [historial nombre-archivo]
  (let [limites(calcular-limites historial)
        viewbox (str (- (limites 0) 100) " " (- (- (limites 1)) 100) " " (+ (limites 2) 100) " " (+ (limites 3) 100))]
    (spit nombre-archivo (str "<svg viewBox=\"" viewbox "\" xmlns=\"http://www.w3.org/2000/svg\">") :append true)
  (escribir-svg historial nombre-archivo)
  (spit nombre-archivo "</svg>" :append true)
  ))
(defn dibujar [pila expresion historial]
  (if (empty? expresion)
    historial
    (let [
          tortuga (first pila)
          paso (str (first expresion))
          tortuga-nueva (cond
                          (or (= paso "F") (= paso "G")) (adelante tortuga 15)
                          (or (= paso "f") (= paso "g")) (-> tortuga (pluma-arriba) (adelante 15) (pluma-abajo))
                          (= paso "+") (derecha tortuga)
                          (= paso "-") (izquierda tortuga)
                          (= paso "|") (invertir tortuga))

          nueva_pila (cond
                       (= paso "[") (apilar-tortuga tortuga pila)
                       (= paso "]") (desapilar-tortuga pila)
                       (not (nil? tortuga-nueva)) (apilar-tortuga tortuga-nueva (desapilar-tortuga pila))
                       :else pila)

          historial-actualizado (detectar-movimiento paso tortuga tortuga-nueva historial)]

      (recur nueva_pila (rest expresion) historial-actualizado))))

(defn wrap-tortuga [angulo expresion]
  (let [tortuga (nueva-tortuga 0 0 angulo true)
        pila (list tortuga)
        historial '()]

    (dibujar pila expresion historial)))

(defn hallar-expr-aux [expresion-actual reglas resultado]
  (if (empty? expresion-actual)
    resultado
    (let [elemento-actual (str (first expresion-actual))
          regla (get reglas (keyword elemento-actual) elemento-actual)]

      (recur (rest expresion-actual) reglas (str resultado regla)))))

(defn hallar-expresion [expresion reglas iteraciones]
  (if (zero? iteraciones)
    expresion
    (let [nueva-expresion (hallar-expr-aux expresion reglas "")]
      (recur nueva-expresion reglas (dec iteraciones))))
  )

(defn convertir-reglas-a-diccionario [reglas]
  (into {}
        (map (fn [r]
               (let [parts (str/split r #" ")]
                 [(keyword (first parts)) (second parts)]))
             reglas)))

(defn -main [nombre-entrada iteraciones nombre-svg]
  (let [entrada (procesar-entrada nombre-entrada)
        angulo (read-string (str/trim (first entrada)))
        axioma (entrada 1)
        reglas (convertir-reglas-a-diccionario (subvec entrada 2))
        ]
    (escribir-svg-wrapper (wrap-tortuga angulo (hallar-expresion axioma reglas iteraciones)) nombre-svg)
  ))