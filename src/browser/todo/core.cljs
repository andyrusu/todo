(ns todo.core
  (:require [reagent.core :as r]))

(def click-count (r/atom 0))

(defn counting-component []
  [:div
   "The atom " [:code "click-count"] " has value: "
   @click-count ". "
   [:input {:type "button" :value "Click me!"
            :on-click #(swap! click-count inc)}]])

(defn ^:export run []
  (r/render [counting-component]
            (js/document.getElementById "app")))