(ns pahkina-2015-03-solution.fast
  (:require [pahkina-2015-03-solution.debug :as d]))

; Just run (find-solutions) to run the algorithm.
; Removed the brute force part by aping hasse's algorithm.


; traversal order (step) | in a list (position)
;     0                  |     0
;   2 1 6                |   1 2 3
; 4 3 5 7 8              | 4 5 6 7 8

(def pieces '{P7 [GL GH TH],
              P1 [GH YH GL],
              P8 [TH TL YH],
              P3 [YH GH GL],
              P9 [GL YH YL],
              P6 [TL TH GL],
              P5 [TL YH YL],
              P4 [YL YH GL],
              P2 [YH TL GL]})

(def piece-names (keys pieces))

;a c -> b a
; b      c
(defn turn-upright [[a b c]]
  [b c a])

; b  ->  a
;a c    c b
(defn turn-downright [[a b c]]
  [c a b])

(defn turn-at [step coll]
  (if (some #{0 2 4 5 6 8} [step])
    (turn-upright coll)
    (turn-downright coll)))

(def step-to-point {0 0, 1 2, 2 1, 3 5, 4 4, 5 6, 6 3, 7 7, 8 8})

(defn step->point [step]
  (step-to-point step))

(def point-to-step (into {} (map (comp vec reverse) step-to-point)))

(defn point->step [point]
  (point-to-step point))

; Places where the edge pairs are
(def pair-positions [[[0 1] [1 1]]
                     [[1 0] [2 2]]
		     [[2 1] [3 1]]
		     [[3 0] [4 2]]
		     [[3 2] [5 0]]
		     [[1 2] [6 0]]
		     [[6 1] [7 1]]
		     [[7 2] [8 0]]
		     [[7 0] [5 2]]])

; Piece orientation
(defn up-or-down [step [a b c]]
  (if (some #{0 2 4 5 6 8} [step])
    [a b c]
    [c b a]))

(defn create-piece-at [name step]
  {:name name
   :edges (up-or-down step (pieces name))
   :turns 0})

(defn is-a-pair? [a b]
  (let [a-type (first (str a))
        a-part (second (str a))
        b-type (first (str b))
        b-part (second (str b))]
    (and (= a-type b-type)
         (not= a-part b-part))))

(defn check-pair-position [state pos]
      (let [[[p1 e1] [p2 e2]] (pair-positions pos)
            piece1 (get state p1)
            piece2 (get state p2)]
       (if (or (nil? piece1) (nil? piece2))
         true
         (is-a-pair? (get-in piece1 [:edges e1])
                     (get-in piece2 [:edges e2])))))  

(defn is-valid? [state]
  (let [step (count state)
        pos (- step 2)]
    (cond
      (neg? pos)
        true
    (= step 8) ;two points to check
      (and (check-pair-position state 8) (check-pair-position state pos))
    :else
      (check-pair-position state pos))))

(defn turn-piece [piece step]
  (-> piece
      (update-in [:edges] (partial turn-at step))
      (update-in [:turns] inc)))

(defn create-rotations [piece step]
  (take 3 (iterate #(turn-piece % step) piece)))

(defn prune [available used result]
  (if (empty? available)
    (conj result used)
    (let [step (count used)
          pieces (map #(create-piece-at % step) available)
          rotations (mapcat #(create-rotations % step) pieces)
          candidates (map (partial conj used) rotations)
          pos-candidates (filter is-valid? candidates)]
      (mapcat #(prune (vec (remove #{((last %) :name)} available))
                      %
                      result)
              pos-candidates))))


;----------------------------
; Running and printing stuff

(defn state->permutation [state]
  (map (comp :name state point->step)
       (range (count state))))

(defn duplicate-state? [this that]
  (let [get-name (fn [state pos] (get-in state [pos :name]))]
    (and
      (or (= (get-name this 0) (get-name that 4)) (= (get-name this 0) (get-name that 8)))
      (or (= (get-name this 1) (get-name that 3)) (= (get-name this 1) (get-name that 7)))
      (or (= (get-name this 2) (get-name that 5)) (= (get-name this 2) (get-name that 6)))
      (or (= (get-name this 3) (get-name that 7)) (= (get-name this 3) (get-name that 1)))
      (or (= (get-name this 4) (get-name that 8)) (= (get-name this 4) (get-name that 0)))
      (or (= (get-name this 5) (get-name that 6)) (= (get-name this 5) (get-name that 2)))
      (or (= (get-name this 6) (get-name that 2)) (= (get-name this 6) (get-name that 5)))
      (or (= (get-name this 7) (get-name that 1)) (= (get-name this 7) (get-name that 3)))
      (or (= (get-name this 8) (get-name that 0)) (= (get-name this 8) (get-name that 4))))))

(defn get-unique-states [states]
  (reduce (fn [acc state] (if (some #{true}
                                    (map #(duplicate-state? % state)
                                    acc))
                            acc
                            (conj acc state)))
          []
          states))

(defn print-out [solutions]
  (doseq [s solutions]
    (println s)))

(defn find-solutions []
  (->> (prune piece-names [] [])
       get-unique-states
       (map state->permutation)
       (map vec)
       print-out))

