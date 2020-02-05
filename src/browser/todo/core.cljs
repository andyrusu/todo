(ns todo.core
  (:require [reagent.core :as r]
            [todo.manage :as m]
            [goog.dom.forms :as f]
            [goog.dom.dataset :as d])
  (:import [goog.events KeyCodes]))

(def todo-items (r/atom {:active ["Take out the trash" "Buy bread" "Teach penguins to fly"]
                         :inactive ["Some item"]}))

(defn handle-new-item
  "Handler for the onKeyPress event to add a new item."
  [event]
  (when (= (.-charCode event) KeyCodes.ENTER)
    (let [elem (.-currentTarget event)
          item (f/getValue elem)]
      (do
        (swap! todo-items 
               (fn [list item]
                 {:active (m/add-item (:active list) item)
                  :inactive (:inactive list)})
               item)
        (f/setValue elem "")))))

(defn handle-mark-item-done
  "Handler for the onClick event, moves one item to :inactive."
  [event]
  (let [elem (.-currentTarget event)
        val (d/get elem "val")]
    (swap! todo-items 
           (fn [list item]
             {:active (m/delete-item (:active list) item)
              :inactive (m/add-item (:inactive list) item)}) 
           val)))

(defn handle-mark-all-done 
  "Handler for the onClick event, moves all items to :inactive."
  [event]
  (swap! todo-items m/move-all-to-inactive))

(defn handle-delete-item
  "Handler for the onClick event, deletes an item from :inactive"
  [event]
  (let [elem (.-currentTarget event)
        val (d/get elem "val")]
    (swap! todo-items 
           (fn [list item]
             (assoc list 
                    :inactive 
                    (m/delete-item (:inactive list) 
                                   (js->clj item)))) 
           val)))

(defn new-item-form []
  [:div
   [:input {:id "new-item" :class "form-control add-todo" :type "text" :placeholder "Add todo" :on-key-press handle-new-item}]
   [:button {:id "check-all" :class "btn btn-success" :on-click handle-mark-all-done} "Mark all as done"]
   [:hr]])

(defn list-active-item
  "Generates the template for active list items."
  [[id label]]
  ^{:key id} [:li.ui-state-default
              [:div.checkbox {:data-val label :on-click handle-mark-item-done}
               [:label 
                [:input {:type "checkbox" :value "" :checked false}]
                label]]])

(defn list-inactive-item [[id label]]
  ^{:key id} [:li
   label
   [:button {:class "remove-item btn btn-default btn-xs pull-right" :data-val label :on-click handle-delete-item}
    [:span {:class "glyphicon glyphicon-remove"}]]])

(defn list-footer [no-of-items]
  [:div.todo-footer
   [:strong
    [:span.count-todos]]
   "Items left " no-of-items])

(defn list-body [items is-active?]
  (let [indexed (map-indexed vector items)
        list-item (if is-active? list-active-item list-inactive-item)]
    [:div.col-md-6
     [:div {:class (str "todolist" (when-not is-active? " not-done"))}
      [:h1 
       (if is-active? 
         "Active" 
         "Done")]
      (when is-active? 
        (new-item-form))
      [:ul {:id (if is-active? "sortable" "done-items") 
            :class "list-unstyled"}
       (for [item indexed]
         (list-item item))]
      (when is-active?
        (list-footer (count items)))]]))

(defn todo-app []
  [:div.container
   [:div.row
    (list-body (:active @todo-items) true)
    (list-body (:inactive @todo-items) false)]])

(def click-count (r/atom 0))
(defn counting-component []
  [:div
   "The atom " [:code "click-count"] " has value: "
   @click-count ". "
   [:input {:type "button" :value "Click me!"
            :on-click #(swap! click-count inc)}]])

(defn ^:export run []
  (r/render [todo-app]
            (js/document.getElementById "app")))

(run)