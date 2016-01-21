(ns clojure.tools.usb
  (:import [javax.usb UsbHostManager UsbConst UsbPipe UsbInterface]))

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
  ([device]
   (.getActiveUsbConfiguration device))
  ([device number]
   (.get (.getUsbConfigurations device) number)))


(defn interface
  [config number]
  (.getUsbInterface config (byte number)))

(defn endpoint
  [iface ep]
  (.get (.getUsbEndpoints iface) ep))

(defn pipe
  [ep]
  (.getUsbPipe ep))

(defn dump
  []
  (doseq [child (devices)]
    (println (describe child))))

(defn find-device
  "Reads all the devices (using 'devices' above), and returns a list
  of all devices that match vendor-id and product-id"
  [vendor-id product-id]
  (filter (fn [d] (let [desc (describe d)]
                    (and (= product-id (.idProduct desc))
                         (= vendor-id (.idVendor desc)))))
          (devices)))
