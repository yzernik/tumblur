(ns tumblure.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [cemerick.friend        :as friend]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util     :refer [format-config-uri]]
            [environ.core :refer [env]]))

(def client-config
  {:client-id     (env :tumblure-oauth2-client-id)
   :client-secret (env :tumblure-oauth2-client-secret)
   :callback      {:domain "http://localhost:5000" ;; replace this for production with the appropriate site URL
                   :path "/oauth2callback"}})

(defn credential-fn
  "Upon successful authentication with the third party, Friend calls
  this function with the user's token. This function is responsible for
  translating that into a Friend identity map with at least the :identity
  and :roles keys. How you decide what roles to grant users is up to you;
  you could e.g. look them up in a database.
  
  You can also return nil here if you decide that the token provided
  is invalid. This could be used to implement e.g. banning users.
  
  This example code just automatically assigns anyone who has
  authenticated with the third party the nominal role of ::user."
  [token]
  {:identity token
   :roles #{::user}})

(def uri-config
  {:authentication-uri {:url "http://www.tumblr.com/oauth/authorize"
                        :query {:client_id (:client-id client-config)
                                :response_type "code"
                                :redirect_uri (format-config-uri client-config)
                                :scope "email"}}
   
   :access-token-uri {:url "http://www.tumblr.com/oauth/access_token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri (format-config-uri client-config)}}})

(def friend-config
  {:allow-anon? true
   :workflows   [(oauth2/workflow
                  {:client-config client-config
                   :uri-config uri-config
                   :credential-fn credential-fn})
                 ;; Optionally add other workflows here...
                 ]})

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (pr-str ["Hello" :from 'Heroku])})

(defroutes app
  (GET "/" []
       (splash))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
