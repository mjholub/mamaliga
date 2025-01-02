(ns mamaliga.predicates)

(defmacro anyeq 
  "Checks collection x if any of its elements is equal to ys.
  More readable than long (or (= x y1) (= x y2) ...)
  Examples:
  (anyeq [1 2 3] 2 3) => true
  (anyeq [1 2 3] 4 5) => false"
  [x & ys]
  ;; no need for explicit right-hand side for or since we want it to return nil anyway
  `(or ~@(map (fn [y] `(= ~x ~y)) ys))) 

(defmacro if-let-for [[sym coll] body else-expr]
  `(if-let [results# (for [~sym ~coll] ~body)]
     (if (seq? results#)
       results#
       ~else-expr)
     ~else-expr))

(defn is-uuid? [s]
  (re-matches #"^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$" s))

(defn nil-or-empty?
 "Mostly useful for UUIDs. UUIDs can only be checked with nil?, but not with empty? or seq?
 (it would throw a ClassCastException for clojure.lang.ISeq). On the other hand, (nil? \"\") => false."
  [s]
  (or (nil? s) 
      (and (sequential? s) (empty? s))
      (and (string? s) (empty? s))))

(defn present-in? [coll elem]
  (some #(= elem %) coll))

(defn absent-in? [coll elem]
  (not (present-in? coll elem)))
