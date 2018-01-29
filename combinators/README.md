# Johdatus kombinaattoreihin

## Miksi?

- Kombinaattorit ovat keskeinen laskennan teorian tutkimuskohde
- Kombinaattorit tarjoavat yhden (melko) tehokkaan tavan toteuttaa
  funktionaalisia ohjelmointikieliä
- Nykyään on aikamoinen trendi määritellä funktiot yhdistelemällä
  olemassa olevia funktioita erilaisilla kombinaattoreilla
  -> tällaista koodia (tai ainakin sen lievempiä esiasteita) voi joutua
  lukemaan

## Mitä ovat kombinaattorit?

Funktioita, jotka eivät viittaa mihinkään muuhun kuin argumentteihinsa.
Esimerkiksi kompositiokombinaattori:

```
(.) = B = \ x y z -> x (y z)
```

Funktio `B` on kombinaattori, koska sen määrittelyssä käytetään vain sen
argumentteja x, y ja z.

## Vähän historiaa

- Haskell Curry tutki kombinaattoreita suunnilleen samaan aikaan (jopa
  vähän ennen) kuin Alonzo Church muotoili lambda-kalkyylia.
- Monet funktio-oppiin kuuluvat todistukset on tehty ensin
  kombinaattorikalkyylilla (= pelkkiä kombinaattoreita käsittelevällä
  laskentajärjestelmällä)

## Mitä väliä?

- Lambda-lausekkeet ja kombinaattorilausekkeet ovat mekaanisesti
  muunnettavissa toisikseen.
- Kombinaattorilausekkeet ovat helppoja käsitellä, koska niissä ei ole
  muuttujia eikä muuttujia tarvitse siksi esim. uudelleennimetä
- Joskus kombinaattorien käyttö tuottaa oikeastikin selkeämpää koodia
  kuin eksplisiittisiä syötteitä käyttävät funktiot
- ns. point-free notation on olennaisesti funktioiden muuttamista
  kombinaattorilausekkeiksi


