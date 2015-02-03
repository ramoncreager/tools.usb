(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]))

(def system nil)

(defn init
  [])

(defn start
  [])

(defn stop
  [])

(defn go
  []
  (init)
  (start)
  :ready)

(defn reset
  []
  (stop)
  (refresh :after 'user/go))
