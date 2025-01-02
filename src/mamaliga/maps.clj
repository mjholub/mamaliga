(ns mamaliga.maps)

(defmacro create-map
 "Creates a map where each sym's value
 is assigned to a keyword with the same name.
 Exafmple:
 (let [a 1 b 2] (create-map a b)) => {:a 1 :b 2}"
  [& syms]
  (zipmap (mapv keyword (vec syms)) (vec syms)))

(defmacro assoc-when 
  "Associates a value to a key in a map if `pred` is true
  Examples:
  (assoc-when {:a 1} true :b 2) => {:a 1 :b 2}
  (assoc-when {:a 1 :b \"foo\"} (< 3 2) :c 3) => {:a 1 :b \"foo\"}"
  [m pred key val]
  `(if ~pred (assoc ~m ~key ~val) ~m))

(defmacro either-in 
  "Returns true if k is in any of the maps
  Examples:
  (either-in :a {:a 1} {:b 2}) => true
  (either-in :a {:b 2} {:c 3}) => false"
  [k & maps]
  `(or ~@(map (fn [m] `(~k ~m)) maps)))

(defmacro equal-keys?
  "Returns true if all **keys** (not necessarily their values) in maps are equal
  Example:
  (equal-keys? {:a 1 :b 2} {:a 3 :b 4}) => true"
  [m k1 k2]
   `(= (~k1 ~m) (~k2 ~m)))

(defn assoc-when-some 
  "Associates a value to a key in a map if `val` is not nil.
  Useful for preventing accidental overwrites."
 [m key val]
 (assoc-when m (some? val) key val))

(defn assoc-optional-values 
  "Merges targeet-map with other-map, but only for keys in optkeys.
  Basically a wrapper for `(merge m (select-keys...))`"
  [target-map other-map optkeys]
  (merge target-map (select-keys other-map optkeys)))

(defn map->nsmap
 "Namespaces unqualified keyword keys in map m with n.
 N should be a string. If the keywords are already qualified, they are left as is.
 Example:
 (map->nsmap {:a 1 :b 2} \"foo\") => {:foo/a 1 :foo/b 2}"
  [m n]
  (reduce-kv (fn [acc k v]
               (let [new-kw (if (and (keyword? k)
                                    (not (qualified-keyword? k)))
                             (keyword (str n) (name k))
                             k)]
                 (assoc acc new-kw v)))
             {} m))

(defn project
 "Assigns v for each k in ks
 Example:
 (project [:a :b] 1) => {:a 1 :b 1}"
  [ks v]
  (into {} (map #(vector % v) ks)))
