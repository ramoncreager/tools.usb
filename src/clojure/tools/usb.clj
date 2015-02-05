(ns clojure.tools.usb
  (:import [javax.usb UsbHostManager]))

(defn services
  []
  (UsbHostManager/getUsbServices))

(defn root
  []
  (.getRootUsbHub (services)))

(defn dump
  ([] (dump (root)))
  ([device]
   (when (.isUsbHub device)
     (doseq [child (.getAttachedUsbDevices device)]
       (println child)))))
