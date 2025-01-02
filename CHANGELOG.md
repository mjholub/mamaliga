# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## 0.1.0 - 2025-01-02
Initial commit
# Added
collections: General collection manipulation functions

`removev, conj-when, flattenv, sort-maps-by-key`


predicates: Predicate functions for validation and testing

`anyeq, is-uuid?, nil-or-empty?, if-let-for, present-in?, absent-in?`


strings: String manipulation and formatting

`bytes-to-human, str-except`


maps: Map-specific operations

`assoc-optional-values, assoc-when, create-map, either-in, equal-keys, incremental-update, map->nsmap, project`


http.params: Web parameter handling

`checkbox->bool, bool->checkbox`


http.cookies: Cookie-related functionality

`cookie-map->header-string (private), set-cookie-header`

http.media: media handling

`mime-groups`

- core.refs: Reference type utilities

`safe-deref`
