(ns todo.core
  (:require [reagent.core :as reagent]
            [todo.data :as data]
            [todo.components :as components]))

(defn ^:export run []
  (do
    (data/init-watch)
    (reagent/render [components/todo-app data/todo-items]
              (js/document.getElementById "app"))))

(run)