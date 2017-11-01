(ns elevator.core
  (:gen-class))

(def elevator {:floor 0
               :direction :up
               :requests {:up ()
                          :down ()}})

(defn- get-direction
  [cur-floor new-floor]
  (if (> new-floor cur-floor) :up :down))

(defn request-floor
  "Make a new request from inside the elevator."
  [state requested-floor]
  (cond
    (= requested-floor (:floor state)) state
    :else (let [request-dir (get-direction (:floor state) requested-floor)
                prioritize (if (= request-dir :up) sort (comp reverse sort))]
            (update-in state [:requests request-dir] (comp prioritize conj) requested-floor))))

(defn- opposite
  [dir]
  (if (= dir :up) :down :up))

(defn- update-floor
  [state dir]
  (let [func (if (= :up dir) inc dec)]
    (update state :floor func)))

(defn- check-if-request-fulfilled
  [state requested-floor requested-dir]
  (if (= (:floor state) requested-floor)
    (update-in state [:requests requested-dir] rest)
    state))

(defn step
  "Figure out where to go and move in that direction, by one floor."
  [state]
  (let [cur-dir (:direction state)
        candidate-dirs [cur-dir (opposite cur-dir)]]
    (if-let [requested-floor (some #(first (get-in state [:requests %])) candidate-dirs)]
      ; found a request
      (let [requested-dir (get-direction (:floor state) requested-floor)]
        (-> state
            (assoc :direction requested-dir)
            (update-floor requested-dir)
            (check-if-request-fulfilled requested-floor requested-dir)))
      ; no request in either direction
      state)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
