(ns catamorph.tree)

(def leaf :leaf)
(defn node2 [value left right] [:node2 value left right])
(defn node3 [value left middle right] [:node3 value left middle right])
(defn leaf? [t] (= :leaf t))
(defn node2? [t] (and (vector? t) (= 4 (count t)) (= :node2 (first t))))
(defn node3? [t] (and (vector? t) (= 5 (count t)) (= :node3 (first t))))
(defn node2-value [t] (get t 1))
(defn node2-left [t] (get t 2))
(defn node2-right [t] (get t 3))
(defn node3-value [t] (get t 1))
(defn node3-left [t] (get t 2))
(defn node3-middle [t] (get t 3))
(defn node3-right [t] (get t 4))

(defn tree-reduce [t node2f node3f leafv]
  (cond (leaf? t) leafv
        (node2? t) (node2f (node2-value t)
                           (tree-reduce (node2-left t) node2f node3f leafv)
                           (tree-reduce (node2-right t) node2f node3f leafv))
        (node3? t) (node3f (node3-value t)
                           (tree-reduce (node3-left t) node2f node3f leafv)
                           (tree-reduce (node3-middle t) node2f node3f leafv)
                           (tree-reduce (node3-right t) node2f node3f leafv))
        true "error"))

(defn tree-map [t f]
  (tree-reduce t (fn [v l r] (node2 (f v) l r))
               (fn [v l m r] (node3 (f v) l m r)) leaf))

