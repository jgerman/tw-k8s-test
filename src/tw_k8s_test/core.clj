(ns tw-k8s-test.core
  (:require [tw-k8s-test.handler :as handler]
            [ring.adapter.jetty :as jetty]
            [cognitect.aws.client.api :as cog-aws]
            [net.jgerman.grab-bag-clj.aws :as aws]
            [chime.core :as chime])
  (:import [java.time Instant Duration])

  (:gen-class))

(def sf-arn "arn:aws:states:us-east-1:744417803430:stateMachine:amazon-ads-data-service-prod")

(defn list-execution [time]
  (let [sfs (aws/build-client :states)
        result (cog-aws/invoke sfs {:op :ListExecutions
                                    :request {:stateMachineArn sf-arn
                                              :maxResults 1}})]
    (println "Top Execution at: " (str time) " :: " (-> result
                             :executions
                             first
                             :name))))

(defn repeatedly-get-execution []
  (chime/chime-at
   (chime/periodic-seq (Instant/now) (Duration/ofMinutes 1))
   list-execution))

(defn -main
  [& args]
  (println "Starting up")
  (repeatedly-get-execution)
  (jetty/run-jetty handler/app {:port 3000}))
