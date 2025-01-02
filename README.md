# mamaliga

Another Clojure cross-project personal helper library.

## Installation

Download from [Clojars](https://clojars.org/clj-http).

## Usage

I personally don't mind splitting this library into smaller modules
instead of the fast, easy import that `mamaliga.core` could offer, especially
since I've got auto-import mapped to just a few keystrokes.  
For me, maintainability and clearer responsibility boundaries are more important  
This library is divided into the following namespaces. For top level :
- `collections` - general collection manipulation functions
- `error` - error handling, mostly try-catch wrappers
- `maps` - map manipulation functions
- `predicates` - predicate functions for validation and testing
- `refs` - ref utilities. Might be moved elsewhere in the future.
- `strings` - string manipulation

See Cljdoc for more details.


## Examples

Basic usage examples are provided in docstrings for most functions.

## License

Copyright © 2024 Marcelina J. Hołub

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7

If you modify this Program, or any covered work, by linking or combining
it with Clojure (or a modified version of that library), containing parts
covered under the same terms as Clojure (currently, the Eclipse Public
License version 1.0), the licensors of this Program grant you additional
permission to convey the resulting work. Corresponding Source for a
non-source form of such a combination shall include the source code for
the parts of Clojure used as well as that of the covered work.
