(ns mamaliga.refs)

(defn safe-deref 
  "Derefs v only if it is a reference type (atom, ref, agent, var). 
  Otherwise returns v. This prevents the `ClassCastException` that would occur
  if we tried to dereference a non-reference type."
  [v]
  (if (some #(instance? % v) [clojure.lang.IDeref])
    @v v))
