(with-monad count-m (run-count-m (m-result 13)))
((check-count 15 15) 0 vector)
((check-count 15 15) 15 vector)
((check-count 15 15) 17 vector)

(require '[clojure.algo.monads :refer :all])
(let [x 1 y 3] (+ x y))
(domonad sequence-m [x (m-result 1) y (m-result 3)] (+ x y))
(with-monad sequence-m (m-result 1))
(domonad sequence-m [x [1 2 3] y [4 5 6]] (+ x y))
(for [x [1 2 3] y [4 5 6]] (+ x y))
(first (domonad sequence-m [x (m-result 1) y (m-result 3)] (+ x y)))

(domonad maybe-m [x (m-result 1) y (m-result 3)] (+ x y))
(domonad maybe-m [x (m-result 1) y m-zero] (+ x y))

(print-tree example-tree)
(print-tree (tree-tag example-tree 'foo))
(print-tree (tree-tag-running-impure example-tree 'foo))
(print-tree (tree-tag-running-threaded example-tree 'foo))

(domonad state-m [x (m-result 3) y (fetch-state)] (+ x y))
((domonad state-m [x (m-result 3) y (fetch-state)] (+ x y)) 0)
((domonad state-m [x (m-result 3) y (fetch-state)] (+ x y)) 5)
((domonad state-m [x (m-result 3) y (update-state inc)] (+ x y)) 5)
(clojure.pprint/pprint (macroexpand '(domonad state-m [x (m-result 3) y (update-state inc)] (+ x y))))

(print-tree (tree-tag-running example-tree 'foo))
(print-tree (tree-tag-running-maybe example-tree 'foo 'jotain))
(print-tree (tree-tag-running-maybe example-tree 'foo 'right-left))

