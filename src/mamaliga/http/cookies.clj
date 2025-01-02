(ns mamaliga.http.cookies)

(defn- cookie-map->header-string
  [{:keys [value max-age domain same-site http-only]
    :or {max-age (* 3600 24 90)
         same-site "Lax"}}]
  (str value
       "; Max-Age=" max-age
       "; Domain=" domain
       "; SameSite=" same-site
       (when http-only "; HttpOnly")))

(defn set-cookie-header
 "Returns a string suitable for a Set-Cookie header."
  [cookie-name cookie-map]
  (let [cookie-string (cookie-map->header-string cookie-map)]
    (str cookie-name "=" cookie-string)))
