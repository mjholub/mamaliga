(ns mamaliga.http.media 
  (:require [clojure.string :as s]))

(defn mime-groups
 "Returns a map of mime types grouped by class and type."
  [mimeset]
  (reduce
   (fn [acc mime]
     (let [class- (first (s/split mime #"/"))
           type- (second (s/split mime #"/"))]
       (update acc class- (fnil conj #{}) type-)))
   {} mimeset))
