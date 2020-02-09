(ns todo.data
  (:require [reagent.core :as reagent]
            [todo.localstorage :as ls]
            [cljs.reader :as reader]))

(defn create-list
  [active inactive]
  {:active active
   :inactive inactive})

(defn init-data
  []
  (let [list (ls/get-item "list")]
    (if (nil? list)
      (create-list [] [])
      (reader/read-string list))))

(def todo-items (reagent/atom (init-data)))

(defn init-watch
  []
  (add-watch todo-items :save-local (fn [key ref old new]
                                      (ls/set-item! "list" (pr-str new)))))