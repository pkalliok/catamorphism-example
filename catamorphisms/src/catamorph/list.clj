(ns catamorph.list)

(def my-nil :nil)
(defn my-cons [head tail] [:cons head tail])
(defn my-nil? [l] (= l :nil))
(defn my-cons? [l] (and (vector? l) (= 3 (count l)) (= :cons (first l))))
(defn head [l] (get l 1))
(defn tail [l] (get l 2))

(defn list-reduce [l consf null]
  (cond (my-nil? l) null
        (my-cons? l) (consf (head l) (list-reduce (tail l) consf null))
        true "error"))

