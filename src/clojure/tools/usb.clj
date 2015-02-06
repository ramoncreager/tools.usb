(ns clojure.tools.usb
  (:import [javax.usb UsbHostManager]))

(defn services
  []
  (UsbHostManager/getUsbServices))

(defn root
  []
  (.getRootUsbHub (services)))

(defn devices
  ([] (devices (.getAttachedUsbDevices (root))))
  ([attached-usb-devices]
   (lazy-seq
     (when-let [device (first attached-usb-devices)]
       (if (.isUsbHub device)
         (devices (lazy-cat (rest attached-usb-devices)
                            (.getAttachedUsbDevices device)))
         (cons device (devices (rest attached-usb-devices))))))))

(defn describe
  [device]
  (.getUsbDeviceDescriptor device))

(defn dump
  []
  (doseq [child (devices)]
    (println (describe child))))
