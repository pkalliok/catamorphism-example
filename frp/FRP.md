# FRP (funktionaalinen reaktiivinen ohjelmointi)

FP monthly, Solita, 4.7.2017

## Mitä on FRP?

Itse sanoisin näin:

> FRP on ohjelmointityyli, jossa ohjelman käytös (= reagointitapa eri
> tilanteissa) määritellään eksplisiittisesti kertomalla se suhde, joka
> on tulosten ja syötteiden välillä.

Esimerkiksi "vessan varattu-valo on päällä, jos ovi on kiinni ja
lukitse-napin painamisesta on lyhyempi aika kuin oven käyttämisestä".

FRP:n keskeinen idea on se, että kätevä tapa määrittää näitä tulosten ja
syötteiden suhteita on uusi tietorakenne, joka mallintaa jonkin
"muuttuvan asian" aikakehityksen.

### Esimerkki

```
> ovi_kiinni = b.Bus()  // jotta voi luoda tapahtumia käsin
> lukitusnappi_painettu = b.Bus()
> varausvalo_palaa = ovi_kiinni.toProperty(false).
	and(lukitusnappi_painettu.map(true).merge(ovi_kiinni.map(false)))
> varausvalo_palaa.log("Varausvalo palaa:")
> ovi_kiinni.push(false)  // alussa varausvalo ei pala
Varausvalo palaa: false
> ovi_kiinni.push(true)   // oven sulkeminen ei yksistään sytytä valoa
Varausvalo palaa: false
> lukitusnappi_painettu.push()   // nyt vessa on varattu
Varausvalo palaa: true
> ovi_kiinni.push(false)  // kun ovi avataan, varausvalo sammuu
Varausvalo palaa: false
> lukitusnappi_painettu.push()   // ei varaa vessaa, kun ovi on auki
Varausvalo palaa: false
> ovi_kiinni.push(true)   // vaikka ovi suljetaan, pitää lukittaa vielä erikseen
Varausvalo palaa: false
> lukitusnappi_painettu.push()   // nyt vessa on taas varattu
Varausvalo palaa: true
```

### FRP, muutosnäkökulma

FRP:ssä korvataan arvot (luvut, merkkijonot, ...) tietotyypillä, joka
mallittaa "muuttuvan arvon".  (Tämä tyyppi on monadinen ja monoidinen,
mikä on kätevää muttei olennaisen tärkeää.)  Kukin muuttuva arvo
sisältää tiedon dependensseistään eli lähteistään ja muuttuu, kun
lähteet muuttuvat.

Tavalliset arvot:

```
> var a = 3, x = 5;
> var c = a + x;
> console.log(c);
8
> a = 5
> console.log(c);
8
```

FRP-arvot:

```
> var a = b.sequentially(1000, [3,5]);
> var x = b.once(5);
> var c = b.combineWith(((n,m) => n+m), a, x);
> c.log();
8
10
<end>
```

Tämä ajatus on tuttu perinteisestä tietovuo-ohjelmoinnista (dataflow
programming).  Tietovuo-ohjelmoinnissa _kaikki_ arvot ovat tämmöisiä
dynaamisesti päivittyviä arvoja.  FRP:ssä taas "normaaliin"
ohjelmointikieleen upotetaan omina tyyppeinään tietovuolaskentamalli.

Koko ohjelman toiminta mallinnetaan muuttuvien asioiden eksplisiittisinä
suhteina, ja sitten tämä kuvaus annetaan jonkinlaiselle runtimelle
"suoritettavaksi".  Javascriptissä usein tämän runtimen joutuu
rakentamaan itse eli mäppäämään käsin muuttuvat syötteet virroiksi ja
muuttuvat tulokset UI:ssa (ja tietokannassa yms) tapahtuviksi
muutoksiksi.  Se on tehty helpoksi mutta on epäfunktionaalista ja
hämärtää FRP:n pointtia ja vastuunjakoa.

### FRP, tietorakennenäkökulma

Tapahtumien ja diskreettien arvomuutosten virrat ovat kuin listoja,
joten niitä voi käsitellä samaan tapaan kuin listoja.  Eli listojen
perinteiset kombinaattorit (concat, map, filter, reduce, scan, groupBy,
partition, ...) ovat kaikki käytettävissä, ja lisäksi hämmentävä määrä
"temporaalisia kombinaattoreita" jotka tekevät virralle jotain sen
perusteella, _mihin hetkeen_ jokin tapahtuma tai arvomuutos kohdistuu.
Esimerkiksi merge yhdistelee kaksi virtaa yhdeksi siten, että tapahtumat
ovat aikajärjestyksessä.

```
> b.sequentially(50, [1,2,3]).merge(b.sequentially(79, [-1, -2, -3])).log();
1
-1
2
3
-2
-3
<end>
> b.sequentially(50, [1,2,3]).merge(b.sequentially(79, [-1, -2, -3])).
		scan(0, ((n,m)=>n+m)).log();
0
1
0
2
5
3
0
<end>
```

Perinteisessä FRP:ssä on myös jatkuvasti muuttuvia arvoja, eli arvoja,
joiden muutos ei tapahdu sykäyksittäin vaan liukuvasti.
Konseptuaalisesti ne vastaavat funktioita, koska arvo vaihtelee ajan
funktiona.  Tietyt kombinaattorit (map, delay) ovat järkeviä jatkuville
arvoille, monet muut (filter, reduce, scan) eivät.

## Mikä FRP:ssä on kätevää?



