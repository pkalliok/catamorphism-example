(ns catamorph.number)

(def zero :zero)
(defn succ [n] [:succ n])
(defn zero? [n] (= n :zero))
(defn succ? [n] (and (vector? n) (= 2 (count n)) (= :succ (first n))))
(defn pred [n] (second n))

(defn num-reduce [n succf zero]
  (cond (succ? n) (succf (num-reduce (pred n) succf zero))
        (zero? n) zero
        true "error"))

(def one (succ zero))
(def two (succ one))
(def three (succ two))

