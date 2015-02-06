(ns clojure.tools.usb
  (:import [javax.usb UsbHostManager UsbConst]))

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

(defprotocol Describable
  (describe [o]))

(extend-protocol Describable
  javax.usb.UsbDevice
  (describe [o] (.getUsbDeviceDescriptor o))

  javax.usb.UsbConfiguration
  (describe [o] (.getUsbConfigurationDescriptor o))

  javax.usb.UsbInterface
  (describe [o] (.getUsbInterfaceDescriptor o))

  javax.usb.UsbEndpoint
  (describe [o] (.getUsbEndpointDescriptor o)))

(defn configuration
  [device]
  (.getActiveUsbConfiguration device))

(defn interface
  [device num]
  (let [config (configuration device)]
    (.getUsbInterface config (byte num))))

(defn dump
  []
  (doseq [child (devices)]
    (println (describe child))))


