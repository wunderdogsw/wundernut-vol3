(ns pahkina-2015-03-solution.core
  (:require [clojure.math.combinatorics :refer [permutations]]))

; Just run (find-solutions) to run the algorithm.
; It takes about 30 seconds on my machine.


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

(def position-permutations (permutations piece-names))

; For debugging
;(def correct-per '[P1 P3 P7 P6 P5 P9 P4 P8 P2])
;(def incorrect-per '[P1 P3 P2 P7 P5 P4 P6 P8 P9])

; ----- Drawing the puzzle -----
(defn piece-as-str [step state]
  (let [p (state step)]
    (str (p :name) (p :edges))))

(defn draw-this-or-empty [this n step]
  (if (= n step)
    this
    " "))

(defn draw-piece [state step n]
  (str
    (draw-this-or-empty "*" n step)
    (piece-as-str n state)
    (get-in state [n :turns])
    " "))

(defn print-state [state step]
  (do
    (println (str
      "                              "
      (draw-piece state step 0)))
    (println (str
      "               "
      (draw-piece state step 2)
      (draw-piece state step 1)
      (draw-piece state step 6)))
    (println (str
      (draw-piece state step 4)
      (draw-piece state step 3)
      (draw-piece state step 5)
      (draw-piece state step 7)
      (draw-piece state step 8)))))
; ---------------------------------

(defn preturn [v]
  (do
    (println v)
    v))
; --------------------------------
; --------------------------------

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

;           P0
;           0
;      P2 1 P1 5 P6
;      2         6
; P4 3 P3 4 P5 8 P7 7 P8

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

(defn get-initial-state [pos-per]
  (vec (map (fn [pos]
              (let [point (step->point pos)
                    name (pos-per point)]
                {:name name
                 :edges (up-or-down pos (pieces name))
                 :turns 0}))
            (range (count pos-per)))))

(defn get-facing-pair-at [state n]
   (let [[[p1 e1] [p2 e2]] (pair-positions n)]
    [(get-in state [p1 :edges e1])
     (get-in state [p2 :edges e2])]))

(defn is-a-pair? [[a b]]
  (let [a-type (first (str a))
        a-part (second (str a))
        b-type (first (str b))
        b-part (second (str b))]
    (and (= a-type b-type)
         (not= a-part b-part))))

(defn turn-piece-at [state step]
  {:pre [(not (neg? step)) (< step (count state))]}
  (-> state
      (update-in [step :edges] (partial turn-at step))
      (update-in [step :turns] inc)))

(defn reset-piece-at [state step]
  (-> state
      (update-in [step :edges]
                 (constantly
                   (up-or-down step (pieces (get-in state [step :name])))))
      (update-in [step :turns]
                 (constantly 0))))

(defn step-back [state step]
  {:pre [(pos? step) (< step (count state))]}
  (-> state
      (reset-piece-at step)
      (turn-piece-at (dec step))))

(defn can-turn-this? [state step]
  (< (get-in state [step :turns]) 2))

(defn traversal [state step]
  (let [final-step (dec (count state))]
    (cond
      (= step final-step)
        (cond
          ;correct solution
          (and (is-a-pair? (get-facing-pair-at state (dec step)))
               (is-a-pair? (get-facing-pair-at state step)))
            state
          ;turn this
          (can-turn-this? state step)
            (recur (turn-piece-at state step) step)
          ;turns used -> go back one step
          :else
            (recur (step-back state step) (dec step)))
      ;zero turned too many times -> not a solution
      (and (zero? step) (> (get-in state [step :turns]) 2))
        false
      ;zero turned -> continue
      (zero? step)
        (recur state (inc step))
      ;pair found -> next step
      (is-a-pair? (get-facing-pair-at state (dec step)))
        (recur state (inc step))
      ;turn this
      (can-turn-this? state step)
        (recur (turn-piece-at state step) step)
      ;go to prev step
      :else
       (recur (step-back state step) (dec step))
)))

(defn solution-or-false [initial-state]
  (traversal initial-state 1))

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
  (->> position-permutations
       (map (comp solution-or-false get-initial-state vec))
       (filter identity)
       get-unique-states
       (map state->permutation)
       (map vec)
       print-out))

