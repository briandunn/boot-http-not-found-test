(set-env!
  :source-paths #{"src/clj" "src/cljs"}
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/boot-cljs "1.7.48-6" :scope "test"]
                  [adzerk/boot-cljs-repl "0.2.0" :scope "test"]
                  [adzerk/boot-reload "0.4.1" :scope "test"]
                  [pandeiro/boot-http        "0.7.3"      :scope "test"]
                  [org.clojure/clojurescript "1.7.122"]
                  [reagent "0.5.0"]
                  [ring "1.4.0"]])

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
  '[notfound.core])

(deftask build []
  (comp (speak)

    (cljs)
    ))

(deftask run []
  (comp (serve :port 7777 :not-found 'notfound.core/not-found-handler)
    (watch)
    (cljs-repl)
    (reload)
    (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none :source-map true}
    reload {:on-jsload 'test-not-found.app/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
    (run)))


