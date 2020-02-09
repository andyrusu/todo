(ns todo.main
  (:require [electron :as el]))

(def app (.-app el))
;(def browser-window (.-BrowserWindow el))

(defn create-window []
  (let [window (new (.-BrowserWindow el) #js {:width 800
                                              :height 600
                                              :webPreferences {:nodeIntegration true}})]
    (.loadFile window "public/index.html")))

(defn ^:export main []
  (.then (.whenReady app) create-window))