(ns todo.core
  (:require [reagent.core :as r]
            [todo.data :as d]
            [todo.components :as comp]))

(defn ^:export run []
  (do
    (d/init-watch)
    (r/render [comp/todo-app d/todo-items]
              (js/document.getElementById "app"))))

(run)