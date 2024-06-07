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

  )

(defn desapilar-tortuga [pila])

(defn dibujar [pila expresion]
  (let [
        tortuga (first pila)
        paso (str(first expresion))
        tortuga_nueva (cond
                  (or (= paso "F") (= paso "G")) (adelante tortuga 1)
                  (or (= paso "f") (= paso "g")) (-> tortuga (pluma-arriba) (adelante 1) (pluma-abajo))
                  (= paso "+") (derecha tortuga)
                  (= paso "-") (izquierda tortuga)
                  (= paso "|") (invertir tortuga))

        nueva_pila (cond
                 (= paso "[") (apilar-tortuga tortuga pila)
                 (= paso "]") (desapilar-tortuga pila)
                 (not(nil? tortuga_nueva)) (apilar-tortuga tortuga_nueva (desapilar-tortuga pila)))
      ]
    (if (not-empty expresion) (dibujar nueva_pila (rest expresion)))
  ))

(defn wrap-tortuga [angulo expresion]
  (let [tortuga (nueva-tortuga 0 0 angulo true)
       pila '(tortuga)]

    (dibujar pila expresion)
    )
  )

(defn hallar-expresion [expresion-actual reglas]
  (if (empty? expresion-actual)
    ""
    (let [elemento-actual (str (first expresion-actual))
          regla (get reglas (keyword elemento-actual) elemento-actual)]

      (str regla (hallar-expresion (rest expresion-actual) reglas)))))

(defn convertir-reglas-a-diccionario [reglas]
  (into {}
        (map (fn [r]
               (let [parts (str/split r #" ")]
                 [(keyword (first parts)) (second parts)]))
             reglas)))

(defn -main [nombre-archivo]
  (let [entrada (procesar-entrada nombre-archivo)
        angulo (first entrada)
        axioma (entrada 1)
        reglas (convertir-reglas-a-diccionario (subvec entrada 2))]

    (wrap-tortuga angulo (hallar-expresion axioma reglas))
  ))