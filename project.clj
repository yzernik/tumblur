(defproject tumblur "0.1.0-SNAPSHOT"
  :description "A Tumblr reader"
  :url "http://example.com/FIXME"
  :license {:name "MIT" :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot tumblur.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
