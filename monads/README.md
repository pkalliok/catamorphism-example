# Monadit, osa 1

* Netissä ei tosiaankaan ole pulaa monadijohdannoista
  * Monet tosin huonoja (vaikeaselkoisia) mutta hyviäkin on paljon
* Tämä esittely yrittää panostaa kysymykseen "miksi" eli milloin
  halutaan käyttää monadia
* Pakko tehdä jotain, mitä on vaikeaa/rumaa tehdä ilman monadia, muuten
  pointti ei tule esiin

## Mikä on monadi?

* Ei kovin hyödyllinen tieto, mutta pakkohan sekin on tähän ottaa
* Matemaattiselta kannalta mikä tahansa _toisen asteen tyyppi_, jolla on
  tietyt operaatiot jotka noudattavat tiettyjä lakeja (tähän ei ehkä
  palata)
* Olio-ohjelmoinnin kannalta _rajapinta_.
* Luokat (tyypit), jotka toteuttavat rajapinnan "monadi", kuvaavat
  erilaisia "laskentaympäristöjä".  Niitä kutsutaan monadeiksi.
* Eli: ohjelmoijan kannalta monadi === laskentaympäristö.

## Mihin monadeja kannattaa käyttää?

* Monadilla saa siistityksi jossain erikoisessa laskentaympäristössä
  toimivan koodin sellaiseksi, että laskentaympäristön kirjanpito on
  faktoroitu monadiin
* Esimerkiksi ympäristö, jossa virhetilanteessa voidaan palata
  yrittämään jotain muuta laskentapolkua (ns. indeterminismimonadi)
* Tai vaikkapa ympäristö, joka laskee, kuinka monta askelta tietyn asian
  laskenta kesti
* Ylipäänsä mikä tahansa ympäristö, joka tarjoaa jotain "lisäpalveluita"
  joita ei voi tarjota pelkillä puhtailla funktioilla: 
  * satunnaisluvut,
  * tilamuuttujat, 
  * poikkeukset, 
  * rekursion suoritus leveyssuunnassa, 
  * lokitus,
  * ...
* monia monadeilla toteutettavista asioista ei osaa kaivata, koska niitä
  on niin pirun vaikeaa tehdä ilman monadeita että on vain
  yksinkertaisesti tottunut olemaan tekemättä niitä
* jos kielessä on sisäänrakennettu tuki jollekin, houkuttaa tietysti
  käyttää sitä tukea eikä mitään monadia

## WTF... millainen rajapinta on laskentaympäristöllä?

* Laskennassa on kinda kyse siitä, että annetaan arvoja funktioille ja
  saadaan niistä tuloksia
* Laskentaympäristö tarkoittaa, että siinä toimivat "funktiot" ottavat
  ehkä vastaan muutakin tietoa kuin argumenttinsa ja palauttavat ehkä
  muutakin tietoa kuin tuloksensa
* Laskentaympäristön pitää määrittää kaksi metodia:
  * value/result/return, jolla viedään arvo laskentaympäristön sisään: a
    -> m a
  * bind/chain/then, jolla lasketaan arvosta toinen arvo
    laskentaympäristön sisällä: (m a, a -> m b) -> m b
* bind vastaa apply-funktiota, mutta sillä on mahdollisuus kutsua
  argumenttifunktiotaan "haluamallaan tavalla"
  * esim. Maybe-monadi ei kutsukaan funktiota ollenkaan jos bind:n
    syötteeksi annettu arvo on Empty

## Mistä kaikista syistä monadeja käytetään?

* Jos kielessä ei ole tukea tietynlaiselle laskentaympäristölle
  * Esim. Haskellissa ei voi muuttaa muuttujan arvoa -> muuttujan "tila"
    pitää mallintaa threadaamalla muuttujan arvo kaiken laskennan läpi,
    minkä saa siistityksi monadilla
  * Javassa / Clojuressa ei ole indeterminismiä -> vaihtoehtoiset
    suorituspolut pitää säilöä laiskoihin listoihin / tarvittaessa
    käynnistettäviin jatkefunktioihin, näiden käsittelyn saa siistityksi
    monadilla
* Jos ylipäänsä haluaa kirjoittaa puhtaasti funktionaalista koodia, joka
  näyttää siistiltä, vaikka vaatii tietynlaista laskentaympäristöä
* Jos haluaa kirjoittaa koodinsa siten, että laskentaympäristöä pystyy
  jälkikäteen muuttamaan tarvitsematta muuttaa koodia
  * Esim. haluaa lisätä jälkikäteen mahdollisuuden keskeyttää laskenta
    ja palauttaa välittömästi jokin tulos
* Jos haluaa varmistaa, että funktion tyypissä näkyy, että se tarvitsee
  tietynlaista laskentaympäristöä
  * Haskell käyttää monadeita varmistamaan, että mikä tahansa
    IO-operaatioita käyttävä funktio on tyyppijärjestelmän kannalta
    IO-operaatio

## Mitä hyötyä on siitä, että monadeilla on yhteinen rajapinta?

* Ei välttämättä kovin paljon, sillä melkein joka laskentaympäristössä
  on omia ominaisuuksiaan joita halutaan käyttää
* Jos käyttää vain return:a ja bind:a, voisi ihan yhtä hyvin kirjoittaa
  koko koodin epämonadisesti
  * koska ne ovat käytännössä sama kuin identity ja apply, toimivat vain
    "monadin sisällä"
* Joitain avukkeita pystyy kuitenkin kirjoittamaan pelkästään
  monadirajapinnan avulla, esim. lift, map, sequence, jne

## License

Copyright © 2017 Panu Kalliokoski

Distributed under the CC0.
https://creativecommons.org/publicdomain/zero/1.0/deed.fi

