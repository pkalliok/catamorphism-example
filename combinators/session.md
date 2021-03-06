Ohjelmalla comb.sh voi sieventää kombinaattorilausekkeita.

```
Prelude> :load comb.sh
*Main> norm (Ap (Ap (Ap S I) C) B)
[Ap (Ap (Ap S I) C) B,Ap (Ap I B) (Ap C B)]
```

Kombinaattoreita on käytettävissä ihan tavallisissa funktionaalisissa
kielissä.  Niillä pystyy määrittelemään funktioita yhdistelminä toisista
funktioista.

```
*Main> sumOfSquares = sum . map (^2)
*Main> sumOfSquares [1,2]
5
```

Kombinaattorikalkyyli siis eliminoi muuttujat.  Lambdakalkyyli tekee
taas niistä kaikista eksplisiittisiä.

```
*Main> \x -> x
    • No instance for (Show (p0 -> p0)) arising from a use of ‘print’
        (maybe you haven't applied a function to enough arguments?)
    • In a stmt of an interactive GHCi command: print it
*Main> (\x -> x) 3
3
```

Monet kombinaattorit elävät edelleen funktionaalisten kielten
vakiokirjastoissa.  K on usein nimeltään "always" tai "const":

```
*Main> const
    • No instance for (Show (a0 -> b0 -> a0))
        (maybe you haven't applied a function to enough arguments?)
*Main> const 3
    • No instance for (Show (b0 -> Integer))
        (maybe you haven't applied a function to enough arguments?)
*Main> (const 3) 5
3
```

const on "vakiofunktiomuodostin", koska se tuottaa funktioita, jotka
palauttavat aina tietyn arvon:

```
*Main> (const 3) "jippii"
3
*Main> (const 3) "hei voisitko antaa jotain muuta"
3
*Main> map (const "jee") [1..10]
["jee","jee","jee","jee","jee","jee","jee","jee","jee","jee"]
```

Tähän on kyllä helpompiakin tapoja:

```
*Main> take 10 $ repeat "jee"
["jee","jee","jee","jee","jee","jee","jee","jee","jee","jee"]
```

S-kombinaattorilla ei ole järkevää nimeä, mutta monadinen "ap" on
funktiomonadille täsmälleen sama kuin S-kombinaattori:

```
*Main> import Control.Monad (ap)
*Main Control.Monad> ap
    • No instance for (Show (m0 (a0 -> b0) -> m0 a0 -> m0 b0))
        (maybe you haven't applied a function to enough arguments?)
```

I-kombinaattorin nimi on yleensä "identity" tai "id":

```
*Main Control.Monad> id 3
3
```

B-kombinaattori on Haskellin tuttu kompositio-operaattori, muissa
kielissä yleensä "comp" tai "compose":

```
*Main Control.Monad> (.)
    • No instance for (Show ((b0 -> c0) -> (a0 -> b0) -> a0 -> c0))
        (maybe you haven't applied a function to enough arguments?)
```

C-kombinaattori on (vähemmän tunnettu) "flip", joka kääntää
argumenttifunktionsa kaksi ensimmäistä argumenttia toisin päin:

```
*Main Control.Monad> flip
    • No instance for (Show ((a0 -> b0 -> c0) -> b0 -> a0 -> c0))
        (maybe you haven't applied a function to enough arguments?)
*Main Control.Monad> cCombinator = \x y z -> x z y
*Main Control.Monad> cCombinator (/) 3 9
3.0
*Main Control.Monad> flip (/) 3 9
3.0
```

W-kombinaattori on vaipunut vähän historian hämäriin, varsinkin kun sen
voi aina korvata S-kombinaattorilla:

```
*Main Control.Monad> wComb = \f x -> f x x
*Main Control.Monad> (wComb (*)) 9
81
*Main Control.Monad> sq = ap (*) id
*Main Control.Monad> sq 9
81
```

Muutamia esimerkkejä, miten mitä hyvänsä funktioita voi kirjoittaa
kombinaattoreilla.  Funktio, joka palauttaa kahdesta argumentista
ensimmäisen ("tosi"):

```
*Main Control.Monad> first = const
*Main Control.Monad> first 3 4
3
```

Funktio, joka palauttaa kahdesta argumentista toisen ("epätosi"):

```
*Main Control.Monad> second = const id
*Main Control.Monad> second 3 4
4
*Main Control.Monad> iComb = ap const const
*Main Control.Monad> second = const iComb 
*Main Control.Monad> second 3 4
4
```

Miksi se toimii?  Koska

```
const iComb 3 4 =
(const iComb 3) 4 =
iComb 4 =
4
```

S-kombinaattorilla voi korvata kaikki kombinaattorit paitsi K:n.
I-kombinaattorin voi määritellä (S K K), eli yllä olevan second-funktion
voi määritellä (K (S K K)).

```
*Main Control.Monad> (Ap K (Ap (Ap S K) K))
Ap K (Ap (Ap S K) K)
*Main Control.Monad> (Ap (Ap (Ap K (Ap (Ap S K) K)) (Var "eka")) (Var "toka"))
Ap (Ap (Ap K (Ap (Ap S K) K)) (Var "eka")) (Var "toka")
*Main Control.Monad> rewrite $ Ap (Ap (Ap K (Ap (Ap S K) K)) (Var "eka")) (Var "toka")
Ap (Ap (Ap S K) K) (Var "toka")
*Main Control.Monad> rewrite $ Ap (Ap (Ap S K) K) (Var "toka")
Ap (Ap K (Var "toka")) (Ap K (Var "toka"))
*Main Control.Monad> rewrite $ Ap (Ap K (Var "toka")) (Ap K (Var "toka"))
Var "toka"
```

Kun kirjoitetaan oikeasti kombinaattorikoodia lähdekoodiin, ei
tietenkään käytetä pelkkiä S- ja K-kombinaattoreita, vaan mahdollisimman
kuvaavia kombinaattoreita.  Esimerkiksi liftM2 antaa saman syötteen
kahdelle funktiolle ja yhdistää niiden tulokset kolmannella:

```
*Main> import Control.Monad (ap, liftM2)
*Main Control.Monad> liftM2 (+) (^2) (^3) 2
12
```

Ikuisen silmukan voi tehdä kombinaattoreilla (mutta Haskellin
tyyppijärjestelmä ei tykkää siitä):

```
*Main Control.Monad> ap id id (ap id id)
    • Occurs check: cannot construct the infinite type: a0 ~ a0 -> t
*Main Control.Monad> (Ap (Ap (Ap S I) I) (Ap (Ap S I) I))
Ap (Ap (Ap S I) I) (Ap (Ap S I) I)
*Main Control.Monad> take 10 $ norm (Ap (Ap (Ap S I) I) (Ap (Ap S I) I))
[Ap (Ap (Ap S I) I) (Ap (Ap S I) I),
 Ap (Ap I (Ap (Ap S I) I)) (Ap I (Ap (Ap S I) I)),
 Ap (Ap (Ap S I) I) (Ap I (Ap (Ap S I) I)),
 Ap (Ap I (Ap I (Ap (Ap S I) I))) (Ap I (Ap I (Ap (Ap S I) I))),
 Ap (Ap I (Ap (Ap S I) I)) (Ap I (Ap I (Ap (Ap S I) I))),
 Ap (Ap (Ap S I) I) (Ap I (Ap I (Ap (Ap S I) I))),
 Ap (Ap I (Ap I (Ap I (Ap (Ap S I) I)))) (Ap I (Ap I (Ap I (Ap (Ap S I) I)))),
 Ap (Ap I (Ap I (Ap (Ap S I) I))) (Ap I (Ap I (Ap I (Ap (Ap S I) I)))),
 Ap (Ap I (Ap (Ap S I) I)) (Ap I (Ap I (Ap I (Ap (Ap S I) I)))),
 Ap (Ap (Ap S I) I) (Ap I (Ap I (Ap I (Ap (Ap S I) I))))]
```

Myös muunnos lambda-lausekkeista kombinaattoreiksi on mahdollista tehdä
automaattisesti.  Onnistuu aina, koska minkä tahansa funktion voi
kirjoittaa S- ja K-kombinaattoreilla, mutta riippuu vähän tuurista,
löytävätkö uudelleenkirjoitussäännöt mitään elegantimpia
kombinaattoreita:

```
[atehwa@undantag ~/proj/fp-monthly/combinators]$ lambdabot 
lambdabot> pl \ a b -> (*) (a-b) (a-b)
ap (ap . ((*) .) . (-)) (-)
```

Joillekin funktioille tulokset ovat parempia:

```
lambdabot> pl \ ls -> map show (filter (\ (x, _) -> even x) (zip [0..] ls))
map show . filter (even . fst) . zip [0..]
```

Yleisesti ottaen point-free alkaa näyttää pahalta (viimeistään) siinä
vaiheessa, kun mukaan tulee jonkinlaista ehtologiikkaa tai useampia
argumentteja.

