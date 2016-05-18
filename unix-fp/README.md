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

> Montako riviä, joilla ensin "hie" ja sitten "no"?

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

> Käännä rivien sisältö ja lisää eteen "upponalle "

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

> Rivit 7-20 väylistä, rivi 32 väylistä

```bash
head -20 vaylat.csv | tail -13
sed -ne 32p vaylat.csv
```

```clojure
(def vaylat (lines "vaylat.csv"))
(->> vaylat (take 20) (take-last 13))
(nth vaylat 32)
```

> Väylät, joiden nimessä "Oulu"

```bash
grep Oulu vaylat.csv
```

```clojure
(filter #(.contains % "Oulu") vaylat)
```

> Muuta "Oulu" "Koulu":ksi väylien nimissä

```bash
sed 's/Oulu/Koulu/g' vaylat.csv
```

```clojure
(map #(clojure.string/replace % "Oulu" "Koulu") vaylat)
```

> Avaa välilyönnin erottamat asiat rivillä listoiksi

```bash
cat vaylat.csv | tr ' ' \\012
```
```clojure
(mapcat (clojure.string/split % #" ") vaylat)
```

> Järjestä väylät aakkosjärjestykseen, kuinka monta väylää on?

```bash
sort vaylat.csv
cat vaylat.csv | wc -l
```

```clojure
(sort vaylat)
(count vaylat)
```

> Elementin järjestysnumero muistiin

```bash
cat -n vaylat.csv
```

```clojure
(map vector (range) vaylat)
```

> Kenttä 2 (eli väylän tunnus) väylistä

```bash
cut -d'|' -f2 vaylat.csv
```

```clojure
(def vaylat-rel (map #(clojure.string/split % #"\|") vaylat))
(map second vaylat-rel)
```

> Kuinka monta aliväylää on kullakin pääväylällä

```bash
cut -d'|' -f3 vaylat.csv | sort | uniq -c
```

```clojure
(frequencies (map #(get % 2) vaylat-rel))
```

> Uniikit pääväylät

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

> Yhdistä väylien nimet vaylat.csv:sta ja foo:sta, poista duplikaatit

```bash
cut -d'|' -f1 vaylat.csv | sort -u - foo
```

```clojure
(require '[clojure.set :as set])
(set/union (map first vaylat-rel) (lines "foo"))
```

> Väylien nimet, jotka löytyvät myös foo:sta

```bash
sort foo > foo.sorted
cut -d'|' -f1 vaylat.csv | sort | comm -12 - foo.sorted
```

```clojure
(set/intersection (set (map first vaylat-rel)) (set (lines "foo")))
```

> foo:n rivit, jotka eivät ole väylien nimiä

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

> Väylät, joiden pääväylä on 96

```bash
awk -F '|' '$3 == "96"' vaylat.csv
```

```clojure
(filter #(= (get % 2) "96") vaylat-rel)
```

> Kaikki tiedot väylistä, joiden nimi löytyy foo:sta

```bash
sort -t'|' -k1,1 vaylat.csv | join -t'|' - foo.sorted
```

```clojure
(let [foo-set (set (lines "foo"))]
  (filter #(foo-set (first %)) vaylat-rel))
```

> kaikki väylät yhdistettynä pääväylänsä tietoihin

```bash
sort -t'|' -k2,2 vaylat.csv > vaylat.sorted
sort -t'|' -k3,3 vaylat.csv | join -t'|' -1 3 -2 2 - vaylat.sorted 
```

```clojure
(set/join vaylat-rel vaylat-rel {2 1})
```

## Sort-muunnokset

Clojuressa on sort-by, jota voi yleensä emuloida sort-muunnoksilla.

> Monennellako rivillä on aakkosjärjestyksessä ensimmäinen väylä?

```bash
cat -n vaylat.csv | sort -k2 | head -1 | awk '{print $1}'
```

```clojure
(->> vaylat (map vector (range)) (sort-by second) (first) (first))
```

> väylät syvyysjärjestyksessä

```bash
# metodi 1
grep '(.*)' vaylat.csv | sort -n -t'(' -k2
# metodi 2
sed -ne 's/^.*(\(.*\)).*$/\1|&/p' vaylat.csv | sort -n | sed 's/^[^|]*|//'
```

```clojure
(sort-by #(Float/parseFloat (subs % (inc (.indexOf % "(")) (.indexOf % ")")))
         vaylat)
```

