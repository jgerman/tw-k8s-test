(defproject tw-k8s-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring "1.9.4"]
                 [com.cognitect.aws/api "0.8.505" :exclusions [org.eclipse.jetty/jetty-util org.eclipse.jetty/jetty-http]]
                 [com.cognitect.aws/endpoints "1.1.11.976"]
                 [com.cognitect.aws/s3 "811.2.865.0"]
                 [com.cognitect.aws/states "810.2.801.0"]
                 [com.cognitect.aws/lambda "810.2.817.0"]
                 [com.cognitect.aws/secretsmanager "811.2.858.0"]
                 [cheshire "5.10.0"]
                 [jarohen/chime "0.3.3"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler tw-k8s-test.handler/app}
  :main ^:skip-aot tw-k8s-test.core
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}
   :uberjar {:aot :all}})
