(defproject tumblure "1.0.0-SNAPSHOT"
  :description "A Tumblr reader"
  :url "https://github.com/yzernik/tumblure"
  :license {:name "MIT" :url "http://opensource.org/licenses/MIT"}
  :main ^:skip-aot tumblure.web
  :target-path "target/%s"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [environ "0.5.0"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "tumblure-standalone.jar"
  :profiles {:production {:env {:production true}}})
