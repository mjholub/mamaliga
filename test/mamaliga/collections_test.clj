(ns mamaliga.collections-test 
  (:require
   [clojure.test :refer [deftest is testing]]
   [mamaliga.collections :refer [conj-when flattenv removev sort-maps-by-key
                                 variadic]]))

(deftest test-conj-when
  (testing "simple vector, with a truthy predicate"
    (is (= [1 2 3] (conj-when [] true 1 true 2 true 3)))
    (is (= [:a 6] (conj-when [] (= 2 2) :a (pos? 5) 6))))
  (testing "simple vector, with a falsy predicate"
    (is (= [1 3] (conj-when [] true 1 false 2 true 3)))
    (is (= [:b 3] (conj-when [] true :b (pos? -1) 2 true 3))))
  (testing "unconditional conjoining (just the value with no predicate preceding)"
    (is (= [1 3] (conj-when [] 1 false 2 3))
        "Should conjoin 1 and 3 when no predicate is provided")
    (is (= ["bob" "alice"] (conj-when [] "bob" "alice"))
        "Should conjoin strings when no predicate is provided"))
  (testing "empty vector"
    (is (= [] (conj-when [])))
    (is (= [] (conj-when [] false 1)))))

(deftest test-removev
  (testing "Basic removal functionality"
    (is (= [2 4 6] (removev odd? [1 2 3 4 5 6]))
        "Should remove odd numbers")
    (is (= [] (removev number? [1 2 3 4]))
        "Should return empty vector when all elements match predicate"))
  
  (testing "Edge cases"
    (is (= [] (removev odd? []))
        "Should handle empty collections")
    (is (= [1 2 3] (removev nil? [1 2 3]))
        "Should work with nil predicate matches")
    (is (= [] (removev (constantly true) [1 2 3]))
        "Should handle always-true predicate")
    (is (= [1 2 3] (removev (constantly false) [1 2 3]))
        "Should handle always-false predicate")))

(deftest test-flattenv
  (testing "Basic flatten functionality"
    (is (= [1 2 3 4] (flattenv [1 [2 [3] 4]]))
        "Should flatten nested vectors")
    (is (= [1 2 3] (flattenv '(1 (2 (3)))))
        "Should flatten nested lists"))
  
  (testing "Edge cases"
    (is (= [] (flattenv []))
        "Should handle empty collections")
    (is (= [] (flattenv [[]]))
        "Should handle nested empty collections")
    (is (= [1] (flattenv 1))
        "Should handle non-sequential inputs")
    (is (= [1 2 3] (flattenv [1 [2] [[3]]]))
        "Should handle deeply nested structures")
    (is (= ["a" "b"] (flattenv ["a" ["b"]]))
        "Should work with strings")
    (is (= [] (flattenv nil))
        "Should handle nil input")))

(deftest test-sort-maps-by-key
  (testing "Basic sorting"
    (let [maps [{:a 3} {:a 1} {:a 2}]]
      (is (= [{:a 1} {:a 2} {:a 3}]
             (sort-maps-by-key maps :a))
          "Should sort ascending by default")
      (is (= [{:a 3} {:a 2} {:a 1}]
             (sort-maps-by-key maps :a :desc))
          "Should sort descending when specified")))
  
  (testing "Edge cases"
    (is (= [] (sort-maps-by-key [] :any-key))
        "Should handle empty collections")
    (is (= [{:a nil} {:a 1}]
           (sort-maps-by-key [{:a 1} {:a nil}] :a))
        "Should handle nil values")
    (let [maps [{:a 1} {:b 2}]]
      (is (= [{:b 2} {:a 1}]
             (sort-maps-by-key maps :missing :asc))
          "Should handle missing keys"))
    (is (= [{:a "a"} {:a "b"} {:a "c"}]
           (sort-maps-by-key [{:a "c"} {:a "a"} {:a "b"}] :a))
        "Should handle string values")
    (is (thrown? IllegalArgumentException
                 (sort-maps-by-key [{:a 1}] :a :invalid-order))
        "Should throw on invalid order parameter")))

(deftest test-variadic
  (testing "Basic functionality"
    (is (= 1 (variadic '(1)))
        "Should return single element for single-item sequence")
    (is (= '(1 2) (variadic '(1 2)))
        "Should return sequence for multi-item sequence"))
  
  (testing "Edge cases"
    (is (= nil (variadic '()))
        "Should handle empty sequences")
    (is (= nil (variadic nil))
        "Should handle nil input")
    (is (= [1] (variadic [1]))
        "Should work with vectors")
    (is (= 1 (variadic '(1)))
        "Should return first element for single-item list")
    (is (= [1 2 3] (variadic [1 2 3]))
        "Should preserve original collection type for multi-item collections")))
