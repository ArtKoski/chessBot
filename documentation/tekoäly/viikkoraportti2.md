## Viikkoraportti 2

_____________________
|   pv	|   	aihe   	|  tuntia 	|
|---	|---		|---	|
|19.3  	|   Ensimmäinen iteraatio sotilaan liikkeiden generaatiosta sekä aiheen opiskelua   	   	|   2	|
|22.3   |   Bitboardin opiskelu liikkeiden generointia varten. Opin muun muassa: yleisesti laudan esitys bitboard-muodossa, bitscan forward, AND-operaatio yksittäisen bitin(=nappulan) poistamiseen. Yksinkertainen bittikomplementti Javassa ~. Tein metodin generateKnightMoves ottaen netistä aika paljon mallia. Aloitin myös Bitboard luokan tekemisen, jonka avulla katsotaan aina yksittäisen nappulan mahdolliset siirtymät, tässäkin otin paljon mallia netistä sekä metoditason toteutuksessa että rakenteessa.|4.5|
||Ajatus/huoli: Hieman tuntuu siltä, että joutuu ottamaan liikaa mallia valmiista toteutuksista, sillä jos mallia ei ota ollenkaan niin edistystä ei tunnu olevan. (esim bitboardin hyödynnyksessä)   |  	|
|   	| Tein generateKnightMoves:ille muutamat testit.   	   	|   	|
|23.3|  Lisää bitti-operaatioiden opiskelua, mm. bit shift, jonka avulla saadaan esim. sotilaan edessä oleva ruutu käsiteltäväksi. Tein metodit generatePawnMoves/Captures sekä niihin tarvittavat operaatiot. Toteutus edelleen nojaa vahvasti chesslib kirjaston toteutukseen. Tein myös muutamat testit sekä sotilaan liikkeille että captureille. | 5 |
|24.3| Aloitin päivän tutkimalla 'magic bitboardeja'. Tuli pienet TIRA-flashbackit, ja päätin tehdä sittenkin omat epätehokkaat metodit tornin sekä lähetin liikkeille. Magic bitboardeja käyttäen toteutus olisi paljon tehokkaampi, mutta se olisi ollut käytännössä copy-paste jostain jota en ymmärrä. Näillä näkymin siis tornin sekä lähetin (ja näin myös kuningattaren) liikkeet generoituu toimivasti muttei täydellisen tehokkaasti. Sain myös hieman lisäymmärrystä bitshiftin hyötyihin.  | 4 |
|25.3| Ymmärsin vihdoin miten voidaan kivasti generoida taulut joista löytyy mahdolliset liikkeet kullekkin nappulalle. Korvasin valmiiden kirjastojen tuomat liiketaulut. Tässä auttoi erityisesti [tämä](https://gekomad.github.io/Cinnamon/BitboardCalculator/). Tein liikkeiden generoinnin kuninkaalle, kirjoitin lähetin logiikkaa uusiksi sekä lisäilin kommentteja. Lisäsin myös pari testiä.  | 7  ||
||  |= n. 23 tuntia|

**Yhteenvetona** viikosta: rupeaa pikkuhiljaa aukeamaan bitboardien maailma, eikä enää tunnu siltä, että joutuu kokoajan katsomaan netistä apua. Magic Bitboardit toki ovat hieman mysteeri, mutta en tiedä onko niihin perehtyminen olennaista. Näillä näkymin liikkeiden generoinnin teemaan liittyen pitäisi vielä: 
 - Parannella torniin liittyviä metodeja (joka vaikuttaa myös kuningattareen)
 - Lisäillä testejä/Javadoc
 - Ainakin tornitus voisi olla melko vaivaton  




Löysin apuvälineen bitboardien havainnollistamiseen:
https://gekomad.github.io/Cinnamon/BitboardCalculator/





