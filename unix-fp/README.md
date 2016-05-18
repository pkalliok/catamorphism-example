# Funktionaalinen ohjelmointi Unix-työkaluilla

## Valmistelu

Käynnistä clojure-tulkki ja kirjoita:

```clojure
(require 'clojure.java.io)
(defn lines [filename] (line-seq (clojure.java.io/reader filename)))
```

## Mistä on kyse?

Jotkin Unixin komentotulkin ja funktionaalisten kielten lausekkeet
muistuttavat toisiaan:

```bash
cat foo | grep 'hie.*no' | wc -l
```

```clojure
(->> (lines "foo") (filter #(re-find #"hie.*no" %)) (count))
```

1. komentotulkki ei muuta olemassa olevaa tietoa, vaan tuottaa uutta,
   kuten funktionaaliset kielet
2. komentotulkki rakentaa monimutkaisia muunnoksia yksinkertaisista,
   kuten funktionaalisten kielten kompositio (yllä threading-makro ->>)
3. komentotulkissa ei ole korkeamman asteen funktioita, mutta lähes
   jokaisessa komennossa on implisiittinen iteraattori (filter, map,
   sort-by)

=> Komentotulkin tietojenkäsittely muistuttaa kovasti modernia
sekvenssin käsittelyä funktionaalisessa ohjelmointikielessä!

```bash
cat foo | rev | sed 's/^/upponalle /'
```

```clojure
(->> (lines "foo")
     (map #(clojure.string/join (reverse (seq %))))
     (map #(str "upponalle " %)))
```

## Peruskauraa



## Joukkoalgebra

## Relaatioalgebra

## Sort-muunnokset
