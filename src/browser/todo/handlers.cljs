(ns todo.handlers
  (:require [todo.data :as data]
            [todo.manage :as m]
            [goog.dom.forms :as f]
            [goog.dom.dataset :as d])
  (:import [goog.events KeyCodes]))

(defn new-item
  "Handler for the onKeyPress event to add a new item."
  [event]
  (when (= (.-charCode event) KeyCodes.ENTER)
    (let [elem (.-currentTarget event)
          item (f/getValue elem)]
      (do
        (swap! data/todo-items
               (fn [list item]
                 {:active (m/add-item (:active list) item)
                  :inactive (:inactive list)})
               item)
        (f/setValue elem "")))))

(defn mark-item-done
  "Handler for the onClick event, moves one item to :inactive."
  [event]
  (let [elem (.-currentTarget event)
        val (f/getValue elem)]
    (swap! data/todo-items
           (fn [list item]
             {:active (m/delete-item (:active list) item)
              :inactive (m/add-item (:inactive list) item)})
           val)))

(defn mark-all-done
  "Handler for the onClick event, moves all items to :inactive."
  [event]
  (swap! data/todo-items m/move-all-to-inactive))

(defn delete-item
  "Handler for the onClick event, deletes an item from :inactive"
  [event]
  (let [elem (.-currentTarget event)
        val (d/get elem "val")]
    (swap! data/todo-items
           (fn [list item]
             (assoc list
                    :inactive
                    (m/delete-item (:inactive list)
                                   (js->clj item))))
           val)))