## Toteutusdokumentti

## Ohjelman rakenne
Selkeästi eroavat kokonaisuudet ovat: 
- Liikkeiden generointi
- MiniMax ja sen sovellukset
- Evaluation
- Itse laudan toiminta

Näistä viimeiseen *en* ole itse tehnyt luokkia, vaan käyttänyt chessLibia, mm: Piece, PieceType, Square, Side, ja tärkeimpänä Board. Jos ehdin, niin tulen tekemään näistä omat versiot.

---------------


## Tehokkuustestejä
Tässä joitain tämänhetkisiä tehokkuustuloksia.

| |   MiniMax (SimpleEval) | MiniMax w/ AB Pruning | MiniMax w/ AB & Zobrist Hashing |
|---	|---		|---	|---     |
| **Syvyys 4**| | **Paras tulos** |
| Helpot Puzzlet  (avg) | > 1 min| 1.5 s | -
| Vaihtelu (s) | [30 ; 120] s | [0.7 ; 3.7] s | -   
|Testit läpi | Kyllä | Kyllä | Ei..  |
|**Syvyys 5** | | | **Paras tulos**
| Helpot Puzzlet (avg) | Hyvin pitkään | 8.5 s | 3.7 s
| Vaihtelu | Suurta| [2 ; 30] s | [1 ; 15]  |
| Testit läpi | Kyllä | Kyllä | Kyllä |

| |   MiniMax | MiniMax w/ AB Pruning | MiniMax w/ AB & Zobrist Hashing |
|---	|---		|---	|---     |
| **Syvyys 3** | | |**Paras tulos**
| Pelin alku (avg) | > 15s | 3 s | 2.6 s
|**Syvyys 4** | | |**Paras tulos**
| Pelin alku (avg) | Kauan | > 30 s | 16 s

Yhteenvetona tuloksista voi sanoa, että algoritmi laskee pulmat melko tehokkaasti, sillä niissä 'check' on yleensä voittava siirto. Normaaleissa pelitilanteissa kuitenkin (kuten pelin alussa) jo syvyydellä 4 joudutaan odottamaan epämieluisan pitkään.    

### Bugs & Problems
- Ruutu 64 bitboard esityksenä tekee long-muuttujan ylivuodon. (ainakin oman analyysin perustella). Tästä johtuen valmiiksi laskettujen liikelistojen viimeinen ruutu on aina 'kovakoodattu'.
- BoardOperations.isMoveLegal -metodi ei toimi halutulla metodilla (aiheuttaa bugin jota en ole saanut korjattua). Sen sijaan käytössä on epätehokkaampi versio.
- Toteutukseni Zobrist Hashing:ista on hyvin alkeellinen. Useissa tilanteissa se siis saattaa johtaa siihen, että objektiivisesti parasta siirtoa ei löydetäkkään. 

### Lähteitä
['Liukuvien' hyökkäysten](https://www.chessprogramming.org/Hyperbola_Quintessence) laskemiseen käytetty temppu.
[BitBoard operaatioiden](https://github.com/bhlangonijr/chesslib/tree/e6acbcb9d429c08918774edb2647b6f8e88db1cc/src/main/java/com/github/bhlangonijr/chesslib) käyttö. Samasta projektista myös monta luokkaa käytössä, mm. Square, Side, Piece, Board. 
[LinkedList (en ole vielä saanut toimimaan)](https://www.youtube.com/watch?v=WEW7QkLFvko)
