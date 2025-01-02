(ns mamaliga.error
  (:require
   [clojure.tools.logging :as log]))

(defmacro with-http-error-handling
  "wraps the body in a try/catch block that logs the error and returns a 500 response
    with a message. The operation-name is used in the log message.
    Not very fine-grained, use with-error-handling for more control."
  [operation-name & body]
  `(try
     ~@body
     (catch Exception e#
       (log/errorf "Error %s: %s" ~operation-name (.getMessage e#))
       {:status 500
        :body (str "Error " ~operation-name ".")})))

(defmacro with-log-and-nil-on-error [operation-name & body]
  `(try
     ~@body
     (catch Exception e#
       (log/errorf "Error %s: %s" ~operation-name (.getMessage e#))
       nil)))

(defmacro with-fallback
  "wraps the body in a try/catch block that logs the error and returns a 500 response
    with a message. The operation-name is used in the log message.
    If the body throws an exception, the fallback is returned."
  [operation-name fallback & body]
  `(try
     ~@body
     (catch Exception e#
       (log/errorf "Error %s: %s" ~operation-name (.getMessage e#))
       ~fallback)))

(def http-getter-error
  {:status 500
   :body "Error retrieving data from the server. See log for details or try again later."
   :headers {"Content-Type" "application/json"}})

(defmacro catch-into-500
  "Wraps with-fallback, passing http-getter-error as the fallback.
  Use it for REST API handlers, not for HTML responses."
  [operation-name & body]
  `(with-fallback ~operation-name ~http-getter-error ~@body))

(defn- rand-range [min max]
  (+ min (* (rand) (- max min))))

(defn retry
  "Retries a function within a randomized jitter window.
  Wrapping fn must be invoked in a separate thread to avoid blocking with random sleep"
  [retries start-min start-max f & args]
  (let [res (try {:value (apply f args)}
                 (catch Exception e
                   (if (zero? retries)
                     (throw e)
                     {:exception e})))]

    (if (:exception res)
      (do
        (let [sleeptime (int (rand-range start-min start-max))]
          (Thread/sleep sleeptime))
        (apply retry (dec retries) (+ (rand-int start-min) start-min) (* 2 start-max) f args))
      (:value res))))

