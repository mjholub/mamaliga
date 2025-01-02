(ns mamaliga.error-test 
  (:require
   [clojure.test :refer [deftest is testing]]
   [clojure.tools.logging :as log]
   [mamaliga.error :refer [catch-into-500 http-getter-error retry
                           with-fallback with-http-error-handling
                           with-log-and-nil-on-error]]))

(defn- capture-log-output [f]
  (let [log-messages (atom [])]
    (with-redefs [log/errorf (fn [& args] 
                              (swap! log-messages conj (apply format args)))]
      [log-messages (f)])))

(deftest test-with-http-error-handling
  (testing "successful execution returns body result"
    (let [result (with-http-error-handling "test-op"
                  {:status 200 :body "success"})]
      (is (= {:status 200 :body "success"} result))))
  
  (testing "exception results in 500 response and logs error"
    (let [[logs result] (capture-log-output
                        #(with-http-error-handling "test-op"
                           (throw (Exception. "test error"))))]
      (is (= {:status 500 :body "Error test-op."} result))
      (is (= ["Error test-op: test error"] @logs)))))

(deftest test-with-log-and-nil-on-error
  (testing "successful execution returns result"
    (let [result (with-log-and-nil-on-error "test-op" 42)]
      (is (= 42 result))))
  
  (testing "exception results in nil and logs error"
    (let [[logs result] (capture-log-output
                        #(with-log-and-nil-on-error "test-op"
                           (throw (Exception. "test error"))))]
      (is (nil? result))
      (is (= ["Error test-op: test error"] @logs)))))

(deftest test-with-fallback
  (testing "successful execution returns result"
    (let [result (with-fallback "test-op" :fallback 42)]
      (is (= 42 result))))
  
  (testing "exception results in fallback value and logs error"
    (let [[logs result] (capture-log-output
                        #(with-fallback "test-op" :fallback
                           (throw (Exception. "test error"))))]
      (is (= :fallback result))
      (is (= ["Error test-op: test error"] @logs)))))

(deftest test-catch-into-500
  (testing "successful execution returns result"
    (let [result (catch-into-500 "test-op"
                  {:status 200 :body "success"})]
      (is (= {:status 200 :body "success"} result))))
  
  (testing "exception results in standard error response"
    (let [[logs result] (capture-log-output
                        #(catch-into-500 "test-op"
                           (throw (Exception. "test error"))))]
      (is (= http-getter-error result))
      (is (= ["Error test-op: test error"] @logs)))))

(deftest test-retry
  (testing "successful execution returns result immediately"
    (let [counter (atom 0)
          f (fn [] (swap! counter inc) 42)
          result (retry 3 10 20 f)]
      (is (= 42 result))
      (is (= 1 @counter))))
  
  (testing "retries on failure until success"
    (let [counter (atom 0)
          f (fn [] 
              (swap! counter inc)
              (if (< @counter 3)
                (throw (Exception. "test error"))
                42))
          result (retry 5 10 20 f)]
      (is (= 42 result))
      (is (= 3 @counter))))
  
  (testing "exhausts retries and throws final exception"
    (let [counter (atom 0)]
      (is (thrown? Exception
                   (retry 2 10 20
                          (fn []
                            (swap! counter inc)
                            (throw (Exception. "test error"))))))
      (is (= 3 @counter))))
  
  (testing "handles multiple arguments"
    (let [f (fn [x y] (+ x y))
          result (retry 3 10 20 f 1 2)]
      (is (= 3 result))))
  
  (testing "validates retry timing parameters"
    (let [counter (atom 0)
          start-time (System/currentTimeMillis)
          f (fn [] 
              (swap! counter inc)
              (if (= @counter 1)
                (throw (Exception. "test error"))
                42))
          result (retry 3 100 200 f)
          elapsed (- (System/currentTimeMillis) start-time)]
      (is (= 42 result))
      (is (>= elapsed 100)))))
