## Viikkoraportti 2

_____________________
|   pv	|   	aihe   	|  tuntia 	|
|---	|---		|---	|
|19.3  	|   Ensimmäinen iteraatio sotilaan liikkeiden generaatiosta sekä aiheen opiskelua   	   	|   2	|
|22.3   |   Bitboardin opiskelu liikkeiden generointia varten. Opin muun muassa: yleisesti laudan esitys bitboard-muodossa, bitscan forward, AND-operaatio yksittäisen bitin(=nappulan) poistamiseen. Yksinkertainen bittikomplementti Javassa ~. Tein metodin generateKnightMoves ottaen netistä aika paljon mallia. Aloitin myös Bitboard luokan tekemisen, jonka avulla katsotaan aina yksittäisen nappulan mahdolliset siirtymät, tässäkin otin paljon mallia netistä sekä metoditason toteutuksessa että rakenteessa.|4.5|
||Ajatus/huoli: Hieman tuntuu siltä, että joutuu ottamaan liikaa mallia valmiista toteutuksista, sillä jos mallia ei ota ollenkaan niin edistystä ei tunnu olevan. (esim bitboardin hyödynnyksessä)   |  	|
|   	| Tein generateKnightMoves:ille muutamat testit.   	   	|   	|
|23.3|  Lisää bitti-operaatioiden opiskelua, mm. bit shift, jonka avulla saadaan esim. sotilaan edessä oleva ruutu käsiteltäväksi. Tein metodit generatePawnMoves/Captures sekä niihin tarvittavat operaatiot. Toteutus edelleen nojaa vahvasti chesslib kirjaston toteutukseen. Tein myös muutamat testit sekä sotilaan liikkeille että captureille. | 4.5 |
||  ||  ||
||  ||  ||
||  ||  ||

Löysin apuvälineen bitboardien käyttöön:
https://gekomad.github.io/Cinnamon/BitboardCalculator/
