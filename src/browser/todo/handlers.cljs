(ns todo.handlers
  (:require [todo.data :as data]
            [todo.manage :as manage]
            [goog.dom.forms :as gform]
            [goog.dom.dataset :as gdata])
  (:import [goog.events KeyCodes]))

(defn new-item
  "Handler for the onKeyPress event to add a new item."
  [event]
  (when (= (.-charCode event) KeyCodes.ENTER)
    (let [elem (.-currentTarget event)
          item (gform/getValue elem)]
      (do
        (swap! data/todo-items
               (fn [list item]
                 {:active (manage/add-item (:active list) item)
                  :inactive (:inactive list)})
               item)
        (gform/setValue elem "")))))

(defn mark-item-done
  "Handler for the onClick event, moves one item to :inactive."
  [event]
  (let [elem (.-currentTarget event)
        val (gform/getValue elem)]
    (swap! data/todo-items
           (fn [list item]
             {:active (manage/delete-item (:active list) item)
              :inactive (manage/add-item (:inactive list) item)})
           val)))

(defn mark-all-done
  "Handler for the onClick event, moves all items to :inactive."
  [event]
  (swap! data/todo-items manage/move-all-to-inactive))

(defn delete-item
  "Handler for the onClick event, deletes an item from :inactive"
  [event]
  (let [elem (.-currentTarget event)
        val (gdata/get elem "val")]
    (swap! data/todo-items
           (fn [list item]
             (assoc list
                    :inactive
                    (manage/delete-item (:inactive list)
                                   (js->clj item))))
           val)))