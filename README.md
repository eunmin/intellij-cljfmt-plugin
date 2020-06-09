# intellij-cljfmt-plugin

cljfmt(https://github.com/weavejester/cljfmt) plugin for IntelliJ.

## Usage

### Build plugin

```
./gradlew buildPlugin
```

### Install plugin

* Go to `Preferences > Plugins > Settings > Install Plugin from Disk ...`
* Select plugin zip file(like this cljfmt-plugin-0.1.0-SNAPSHOT.zip) in build/distributions directory.

### Configuration

* Add the cljfmt dependency and repl port option to your Leiningen project map:
  ```clojure
  (defproject webapp "0.1.0-SNAPSHOT"
    :description "FIXME: write description"
    :url "http://example.com/FIXME"
    :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
              :url "https://www.eclipse.org/legal/epl-2.0/"}
    :dependencies [[org.clojure/clojure "1.10.1"]]
    :repl-options {:init-ns webapp.core :port 30000}
    :profiles {:dev {:dependencies [[cljfmt "0.6.7"]]}})
  ```

* Check your nREPL host and port in `Preferences > Tools > cljfmt`

### Reformat

* `Tools > Reformat Code(cljfmt)`

