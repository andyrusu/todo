(ns todo.manage)

(defn add-item
  "Add a new item to the list."
  [items item]
  (conj items item))

(defn delete-item
  "Deletes an item from the vector."
  [items item]
  (remove #(= % item) items))

(defn move-all-to-inactive
  "Move items from :active to :inactiv"
  [all-items]
  {:active []
   :inactive (into (:inactive all-items) (:active all-items))})