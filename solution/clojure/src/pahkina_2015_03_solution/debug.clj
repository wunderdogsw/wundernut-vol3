(ns pahkina-2015-03-solution.debug)

; ----- Drawing the puzzle -----
(defn piece-as-str [step state]
  (if (or (empty? state) (> step (dec (count state))))
    "------------"
    (let [p (state step)]
      (if (nil? p)
        "------------"
      (str (p :name) (p :edges))))))

(defn draw-this-or-empty [this n step]
  (if (= n step)
    this
    " "))

(defn draw-piece [state step n]
  (str
    (draw-this-or-empty "*" n step)
    (piece-as-str n state)
    (if-let [turns (get-in state [n :turns])]
      (if (neg? turns)
        turns
        (str " " turns))
      "  ")
    " "))

(defn print-state [state step]
  (do
    (println (str
      "                                "
      (draw-piece state step 0)))
    (println (str
      "                "
      (draw-piece state step 2)
      (draw-piece state step 1)
      (draw-piece state step 6)))
    (println (str
      (draw-piece state step 4)
      (draw-piece state step 3)
      (draw-piece state step 5)
      (draw-piece state step 7)
      (draw-piece state step 8)))))

(defn preturn [v]
  (do
    (println v)
    v))
