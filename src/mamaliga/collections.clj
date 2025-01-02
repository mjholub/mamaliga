(ns mamaliga.collections)

;; FIXME: does not work properly for unconditional conjoining
(defmacro conj-when 
  "Conditionally conjoins elements to a collection based on predicates.
   Takes a collection followed by pairs of pred-expr and value-expr.
   If pred-expr is true or not provided (unconditional), conjoins value-expr.
   All predicates are evaluated and all matching values are conjoined.
   
   Examples:
   (conj-when [] 
     true 1        ;; unconditional
     (pos? 5) 2    ;; conditional
     false 3)      ;; skipped
   => [1 2]
   
   (conj-when [] 
     1             ;; unconditional
     (pos? -1) 2   ;; skipped
     3)            ;; unconditional
   => [1 3]"
  [coll & clauses]
  (let [result (gensym "result")]
    `(let [~result ~coll]
       ~(reduce
         (fn [acc [pred? val]]
           (if pred?
             `(if ~pred?
                (conj ~acc ~val)
                ~acc)
             `(conj ~acc ~val)))
         result
         (partition-all 2 clauses)))))

(defn removev 
  "Like remove, but returns a vector directly instead"
  [pred coll] 
  (filterv (complement pred) coll))

(defn flattenv
 "Flatten returning a vector without the need for additional conversion"
  [x]
  (filterv (complement sequential?) (rest (tree-seq sequential? seq x))))

(defn sort-maps-by-key
  "Sorts a collection of maps by a specified key"
  ([coll key]
   (sort-maps-by-key coll key :asc))
  ([coll key order]
   (let [comparator (if (= order :desc)
                     #(compare %2 %1)
                     compare)]
     (vec (sort-by key comparator coll)))))

(defn variadic
  "Returns first arg if v has one element
  Examples:
  (variadic '(1)) => 1
  (variadic [1 2]) => [1 2]"
  [v]
  (if (and (seq? v) (< 1 (count v))) v (first v)))
