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

Näistä pitäisi yhdistellä kaikki hienommat jutut!

```bash
head -20 vaylat.csv | tail -13
sed -ne 32p vaylat.csv
```

```clojure
(def vaylat (lines "vaylat.csv"))
(->> vaylat (take 20) (take-last 13))
(nth vaylat 32)
```

```bash
grep Oulu vaylat.csv
```

```clojure
(filter #(.contains % "Oulu") vaylat)
```

```bash
sed 's/Oulu/Koulu/g' vaylat.csv
```

```clojure
(map #(clojure.string/replace % "Oulu" "Koulu") vaylat)
```

```bash
cat vaylat.csv | tr ' ' \\012
```
```clojure
(mapcat (clojure.string/split % #" ") vaylat)
```

```bash
sort vaylat.csv
cat vaylat.csv | wc -l
```

```clojure
(sort vaylat)
(count vaylat)
```

```bash
cut -d'|' -f2 vaylat.csv
```

```clojure
(def vaylat-rel (map #(clojure.string/split % #"\|") vaylat))
(map second vaylat-rel)
```

```bash
cut -d'|' -f3 vaylat.csv | sort | uniq -c
```

```clojure
(frequencies (map #(get % 2) vaylat-rel))
```

```bash
cut -d'|' -f3 vaylat.csv | sort | uniq -d
```

```clojure
(into '() (set (map #(get % 2) vaylat-rel)))
```

## Joukkoalgebra

Joukkoalgebra on superkätevää.  Ei typeriä sisäkkäisiä silmukoita!
Miellyttävää korkean tason koodia, helppoa ymmärtää.  Komentotulkissa
käsiteltävien tiedostojen pitää olla sortattuja.

```bash
cut -d'|' -f1 vaylat.csv | sort -u - foo
```

```clojure
(require '[clojure.set :as set])
(set/union (map first vaylat-rel) (lines "foo"))
```

```bash
sort foo > foo.sorted
cut -d'|' -f1 vaylat.csv | sort | comm -12 - foo.sorted
```

```clojure
(set/intersection (set (map first vaylat-rel)) (set (lines "foo")))
```

```bash
cut -d'|' -f1 vaylat.csv | sort | comm -23 foo.sorted -
```

```clojure
(set/difference (set (lines "foo")) (set (map first vaylat-rel)))
```

## Relaatioalgebra

Relaatioalgebra on tuttua SQL-tietokannoista.  Sillä voi selvittää
käytännössä mitä vain isoista tietomääristä.  Vaatimus, että tiedot
täytyy pitää sortattuina, hankaloittaa relaatioalgebraa jonkin verran
komentotulkissa.

```bash
```

```clojure
```

## Listaoperaatiot

## Sort-muunnokset

```bash
```
```clojure
```


