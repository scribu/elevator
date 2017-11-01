(ns elevator.core-test
  (:require [clojure.test :refer :all]
            [elevator.core :refer :all]))

(deftest idling
  (is (= elevator (step elevator))))

(deftest request-priority
  (let [el (assoc elevator :floor 3)]
    (is (= [4 5] (-> el (request-floor 5) (request-floor 4) (get-in [:requests :up]))))
    (is (= [2 1 0] (-> el (request-floor 1) (request-floor 0) (request-floor 2) (get-in [:requests :down]))))))

(deftest single-request
  (let [el (-> elevator (request-floor 2))]
    (is (= 1 (-> el (step) (:floor))))
    (is (= 2 (-> el (step) (step) (:floor))))
    (is (= 2 (-> el (step) (step) (step) (:floor))))))

(deftest multiple-requests
  (let [el (-> elevator
               (request-floor 2)
               (step)
               (request-floor 0))]
    (is (= 1 (-> el (:floor))))
    (is (= 2 (-> el (step) (:floor))))
    (is (= 1 (-> el (step) (step) (:floor))))
    (is (= 0 (-> el (step) (step) (step) (:floor))))))
