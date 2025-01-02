(ns mamaliga.http.params)

(defn checkbox->bool
 "Converts a checkbox value to a boolean.
 Normally a checkbox in a HTML form will have a value of 'on' 
 if checked and no value if unchecked."
  [checkbox-value]
  (= checkbox-value "on"))

(defn bool->checkbox 
  "Converts a boolean to a checkbox value. (true to \"on\", false to \"\")"
  [bool-value]
  (if bool-value "on" ""))
