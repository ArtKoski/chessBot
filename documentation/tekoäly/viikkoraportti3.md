## Viikkoraportti 3

_____________________
|   pv	|   	aihe   	|  tuntia 	|
|---	|---		|---	|
|29.3  	|   Tornin ja lähetin liikkeiden generoinnin 'refaktorointia'.   	   	|   2	|
|30.3   | Kuningattarelle pari uutta testiä sekä pari vanhaa kesken jäänyttä testiä valmiiksi. Koodista luettavampaa (esim. 0xASDL sijaan file[asd]). Ärsyttävä bugi: pseudolaillisia siirtoja generoidessa viimeinen ruutu 64 saa väärät arvot, kaikki muut saavat oikeat. Long muuttuja kyseessä, ja tuntuu kuin olisi overflow. En tiennyt mitä tehdä, joten ruudusta 64 siirrot on hardcodettu erikseen. ||
| |Aloitin tekemään 'isMoveLegal' -metodia. Tähän tarvitsen myös pari apumetodia, esim. squareAttackedBy() sekä getKingSquare().   |3|
|31.3| isMoveLegal sekä squareAttackedBy -metodien väsäämistä. Jälkimmäiselle myös pari testiä.  | 2|
|3.4| isMoveLegal testausta. ~~En keksinyt nättiä tapaa testata erilaisia laittomia siirtoja.~~ (sain hieman siistittyä)  |5
| | Tein muutokset pawnMoves -metodeihin, jotta sotilaat voivat korottua viimeisellä rivillä. Tämä oli helpompi homma kuin oletin, ainakin käyttäen valmista chesslib luokkaa 'Move'. Myös testattu.  | 
| | Tajusin, että tornitus vaatii (ärsyttävän paljon) työtä, joten päätin jättää sen tekemättä toistaiseksi. Metodini isKingChecked aiheuttaa myös hämminkiä. |
| | **Aiheen vaihto:** Liikkeiden generointi tuntuu sujuvan ihan ok: se pelasi ainakin pari peliä luomatta laittomia siirtoja. 'en passant':ia eikä tornitusta ole vielä toteutettu. Saatan tehdä ne myöhemmin. Aion aloittaa ensi viikosta mini-maxin / evaluate metodien toteuttamisen. | 
|5.4| Aloitin MiniMaxin toteutuksen, ja käytin pelitilanteen arviointiin yksikertaista pelinappuloiden arvojen laskemista. Minimax oli yllättävän yksinkertainen toteuttaa. Käytin apuna muutamaa [youtube-videota](https://www.youtube.com/watch?v=0Rq3uPrxVMA) jossa tehdään minimax shakkiin.  | 3
|6.4| Tein minimaxille lisää testejä ja taistelin bugin kanssa liittyen laillisten siirtojen tarkistamiseen. Refaktoroin 'simpleEvaluator' luokkaa. Minimax tuntuu toimivan mukavasti, mutta en ajatellut että se olisi niin hidas: jo syvyydellä 3 (eli kuinka pitkälle siirtoja lasketaan) joutuu odottamaan yli 10 sekunttia. Aion siis luultavasti siirtyä alpha-beta pruningin toteuttamiseen heti seuraavaksi. | 5


