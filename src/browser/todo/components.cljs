(ns todo.components
  (:require [todo.handlers :as handlers]))

(defn new-item-form []
  [:div
   [:input {:id "new-item" :class "form-control add-todo" :type "text" :placeholder "Add todo" :on-key-press handlers/new-item}]
   [:button {:id "check-all" :class "btn btn-success" :on-click handlers/mark-all-done} "Mark all as done"]
   [:hr]])

(defn list-active-item
  "Generates the template for active list items."
  [[id label]]
  ^{:key id} [:li.ui-state-default
              [:div.checkbox 
               [:label 
                [:input {:type "checkbox" 
                         :checked false
                         :value label 
                         :on-change handlers/mark-item-done}]
                label]]])

(defn list-inactive-item [[id label]]
  ^{:key id} [:li
              label
              [:button {:class "remove-item btn btn-default btn-xs pull-right" :data-val label :on-click handlers/delete-item}
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

(defn todo-app [todo-items]
  [:div.container
   [:div.row
    (list-body (:active @todo-items) true)
    (list-body (:inactive @todo-items) false)]])