(ns algo3-tp2-sistemasl.core-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [algo3-tp2-sistemasl.core :as l-system]))

(defn approximately=
  ([expected actual]
   (approximately= expected actual 1.0e-9))
  ([expected actual tolerance]
   (< (Math/abs (- expected actual)) tolerance)))

(deftest expression-rewriting-test
  (testing "zero iterations preserve the axiom"
    (is (= "FX" (l-system/hallar-expresion "FX" {:F "F+F"} 0))))
  (testing "rules are applied to every symbol on each iteration"
    (is (= "F+F+F+F"
           (l-system/hallar-expresion "F" {:F "F+F"} 2))))
  (testing "symbols without a rule are preserved"
    (is (= "FX+Y"
           (l-system/hallar-expresion "AX+Y" {:A "F"} 1)))))

(deftest rule-parsing-test
  (is (= {:F "FF" :X "F+[[X]-X]"}
         (l-system/convertir-reglas-a-diccionario
           ["F FF" "X F+[[X]-X]"]))))

(deftest resource-input-test
  (let [[angle axiom & rules] (l-system/procesar-entrada "koch1.sl")]
    (is (= "90" angle))
    (is (= "F" axiom))
    (is (= {:F "F-F+F+F-F"}
           (l-system/convertir-reglas-a-diccionario rules)))))

(deftest turtle-rotation-test
  (let [turtle (l-system/nueva-tortuga 0 0 90 true)]
    (is (= 0 (:angulo-act (l-system/derecha turtle))))
    (is (= 180 (:angulo-act (l-system/izquierda turtle))))
    (is (= 270 (:angulo-act (l-system/invertir turtle))))
    (is (= 90 (:angulo-act turtle)) "operations leave the original value unchanged")))

(deftest turtle-movement-test
  (let [moved (l-system/adelante
                (l-system/nueva-tortuga 0 0 90 true)
                15)]
    (is (approximately= 0 (:x moved)))
    (is (approximately= 15 (:y moved)))))

(deftest turtle-stack-test
  (let [trunk (l-system/nueva-tortuga 0 0 90 true)
        branch (l-system/derecha trunk)
        stack (->> (list trunk)
                   (l-system/apilar-tortuga branch))]
    (is (= branch (first stack)))
    (is (= trunk (first (l-system/desapilar-tortuga stack))))))

(deftest branching-drawing-test
  (let [segments (reverse (l-system/wrap-tortuga 90 "F[+F]F"))
        [trunk branch continuation] segments]
    (is (= 3 (count segments)))
    (is (approximately= 0 (:x1 trunk)))
    (is (approximately= 15 (:y2 trunk)))
    (is (approximately= 15 (:x2 branch)))
    (is (approximately= 15 (:y2 branch)))
    (is (approximately= 0 (:x1 continuation))
        "closing the branch restores the previous X position")
    (is (approximately= 15 (:y1 continuation))
        "closing the branch restores the previous Y position")
    (is (approximately= 30 (:y2 continuation)))))

(deftest drawing-bounds-test
  (let [segments [{:x1 -2 :y1 3 :x2 4 :y2 -5}
                  {:x1 4 :y1 -5 :x2 10 :y2 8}]]
    (is (= [-2 8 12 13]
           (l-system/calcular-limites segments)))))

(deftest svg-output-test
  (let [output (java.io.File/createTempFile "l-system-" ".svg")
        segments [{:x1 0 :y1 0 :x2 15 :y2 0}]]
    (try
      (l-system/escribir-svg-wrapper segments (.getAbsolutePath output))
      (let [svg (slurp output)]
        (is (str/starts-with? svg "<svg viewBox="))
        (is (str/includes? svg "<line x1=\"0\" y1=\"0\" x2=\"15\" y2=\"0\""))
        (is (str/ends-with? svg "</svg>")))
      (finally
        (.delete output)))))
