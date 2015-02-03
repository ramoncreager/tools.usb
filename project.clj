(defproject usb "0.1.0-SNAPSHOT"
  :description "javax-usb (JSR-80) compliant USB library for Clojure"
  :url "http://www.github.com/aamedina/usb"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]
                 [org.usb4java/usb4java-javax "1.2.0"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.9"]]
                   :source-paths ["dev"]}}
  :jvm-opts ["-Xmx512m" "-server"])
