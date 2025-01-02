(defproject mamaliga "0.1.0"
  :description "Yet another collection of Clojure utilities I use in my projects"
  :url "https://github.com/mjholub/mamaliga"
  :license {:name "GNU GPL v3+, with Clojure exception"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.12.0"]]
  :main ^:skip-aot mamaliga.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
