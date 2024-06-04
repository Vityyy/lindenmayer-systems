(ns algo3-tp2-sistemasl.core
  (:require
    [clojure.string :as str]))

(defn procesar_entrada [nombre-archivo]
  (str/split (slurp (str "resources/" nombre-archivo)) #"\n") )

;(defn nueva_tortuga [coord-x coord-y angulo pluma-abajo]
;  {:x coord-x :y coord-y :angulo angulo :pluma pluma-abajo})
;
;(defn adelante [tortuga n]
;  (let [angulo-radianes (Math/toRadians (:angulo tortuga))
;        dx (* n (Math/cos angulo-radianes))
;        dy (* n (Math/sin angulo-radianes))]
;    (-> tortuga
;        (update :x + dx)
;        (update :y + dy))))
;
;
;
;(defn derecha [tortuga angulo]
;  (update tortuga :angulo - angulo))
;
;(defn izquierda [tortuga angulo]
;  (update tortuga :angulo + angulo))
;
;(defn pluma_arriba [tortuga]
;  (update tortuga :pluma false))
;
;(defn pluma_abajo [tortuga]
;  (update tortuga :pluma true))
;
;(defn wrap [angulo axioma reglas]
;  (let [tortuga (nueva_tortuga 0 0 angulo true)
;       pila '(tortuga)
;       actual (first pila)]
;
;    )
;  )
;(defn dibujar [simbolo]
;
;  )
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
  (let [entrada (procesar_entrada nombre-archivo)
        angulo (first entrada)
        axioma (nth entrada 1)
        reglas (convertir-reglas-a-diccionario (subvec entrada 2))]

    (hallar-expresion axioma reglas))
  )