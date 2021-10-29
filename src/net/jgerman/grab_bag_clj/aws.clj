;; I'm stealing this from my own codebase because I haven't pushed to clojars yet
;; and I need it now...
(ns net.jgerman.grab-bag-clj.aws
    (:require [cognitect.aws.client.api :as aws]
            [cheshire.core :as json]))

;; collection of useful utils using the congnitect aws api as a based

(defn get-ops-names [client]
  (let [ops (aws/ops client)]
    (map #(first %) ops)))

(defn get-op-desc [client name]
  (let [ops (aws/ops client)]
    (filter #(= name (first %)) ops)))


(defn build-client
  "Build a simple clients: :lambda :s3 :secretsmanager :states."
  [aws-api]
  (aws/client {:api aws-api}))

(defn list-functions [lambda]
  (aws/invoke lambda {:op :ListFunctions}))

;; I might like to write a version of this that provides a lazy list of the
;; items in a bucket

(defn get-objects
  ([s3 bucket]
   (get-objects s3 bucket nil))
  ([s3 bucket continuation-token]
   (aws/invoke s3 {:op :ListObjectsV2
                   :request {:Bucket bucket
                             :ContinuationToken continuation-token}})))

(defn process-bucket
  "Process a bucket by applying objects-fn to the keys in each request. Each
  object in the list of objects has the following keys:
  :Key
  :LastModified
  :ETag
  :Size
  :StorageClass"
  [s3 bucket objects-fn]
  (let [objects-processed (atom 0)
        start (System/currentTimeMillis)]
      (loop [continuation-token nil]
        (let [result (get-objects s3 bucket continuation-token)]
          (objects-fn (:Contents result))
          (swap! objects-processed (fn [x] (+ x (count (:Contents result)))))
          (when (:IsTruncated result)
            (recur (:NextContinuationToken result)))))
      {:objects-processed @objects-processed
       :elapsed-time (float (/ (- (System/currentTimeMillis)
                                  start)
                               1000))}))
