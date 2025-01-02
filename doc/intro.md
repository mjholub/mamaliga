# Introduction to mamaliga

## Overview

This library provides a collection of utility functions and macros organized into focused namespaces.  
It is designed to support both general-purpose Clojure development and web-specific functionality, with a focus on maintainability and clear separation of concerns.

Namespace organization is briefly explained within the README file.

I seek to do my best in making the code self-documenting, by using comprehensible naming that builds upon the existing Clojure function and macro names.

## Usage Patterns

### Working with Collections

When working with collections, prefer using the vector-returning variants (`removev`, `flattenv`) when you know you'll need a vector, as they're more efficient than converting after the fact:
```clojure
;; Preferred
(removev pred coll)

;; Less efficient
(vec (remove pred coll))
```

### Map Transformations

For map transformations, consider using the selective update functions to avoid unnecessary modifications:
```clojure
;; Updates only what changed
(incremental-update existing-map new-map)

;; Adds namespace qualification to keys
(map->nsmap {:a 1} "user")  ; => {:user/a 1}
```

## Error handling

The library generally follows these principles for error handling:

- Predicates return false rather than throwing exceptions
- Transform functions return nil or empty collections for invalid input
- Web utilities handle edge cases gracefully (e.g., missing parameters)

## Important notes

The `error` namespace assumes `clojure.tools.logging`'s log implementation usage.

### Contributing

See CONTRIBUTING.md
