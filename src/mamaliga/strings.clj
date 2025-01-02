(ns mamaliga.strings 
  (:require
   [clojure.set :as set]))

(defn bytes-to-human 
  "Converts a byte count to a human-readable string, up to the Yottabyte scale (10^24 bytes)"
  [bytecount]
  (if (<= bytecount 0)
    "0 B"
    (let [units ["B" "KB" "MB" "GB" "TB" "PB" "EB" "ZB" "YB"]
          exp (int (Math/floor (/ (Math/log bytecount) (Math/log 1024))))
          unit (units exp)
          value (/ bytecount (Math/pow 1024 exp))]
      (format "%.2f %s" value unit))))

(defn str-except
 "Evaluates `str` for everything in `values` except for `exclusions`"
  [exclusions & values]
  (if (some (partial contains? exclusions) values)
    (first (set/intersection exclusions (set values)))
    (apply str values)))
