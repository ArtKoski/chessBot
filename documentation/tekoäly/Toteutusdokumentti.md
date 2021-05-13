## Toteutusdokumentti

## Ohjelman rakenne
Selkeästi eroavat kokonaisuudet ovat: 
- Liikkeiden generointi -> luokat BitOperations/MoveGenerator
- MiniMax ja sen sovellukset -> luokat MiniMax, MiniMaxAB
- Evaluation (pelitilanteen arvionti) -> luokat datastructureproject.Evaluation
- Itse laudan toiminta -> luokat datastructureproject.Board

---------------


## Tehokkuustestejä
Testauksen kohteina on helpot puzzlet sekä pelin ensimmäiset 10 siirtoa.
**Helpot puzzlet** koostuvat lähinnä sellaisista pelitilanteista, joista löytyy helppo materiaalinen voitto tai checkmate. Todellisessa pelissä tällaisia tilanteita on melko vähän.
Ensimmäiset **10 siirtoa** taas vaativat hieman enemmän laskentaa.
#### Helpot Puzzlet
| |   MiniMax | MiniMax w/ AB Pruning  |
|---	|---		|---	|
| **Syvyys 4**| |  
|  avg. aika (per siirto)| 23s | < 1 s
| Vaihtelu (s) | [7 ; 33] s | [0.1 ; 1.2] s  
|Testit läpi | Kyllä | Kyllä 
| Arvionteja laskettu yht. | ~1 Miljoona  | 12 000 
|**Syvyys 5** | | 
| avg. aika | Hyvin pitkään | ~2 s
| Vaihtelu | Suurta| [1 ; 6] s 
| Testit läpi | ? | Kyllä
| Arvionteja laskettu yht. | -  | 100 000
#### Pelin alku (10 siirtoa)

| |   MiniMax | MiniMax w/ AB Pruning
|---	|---		|---	|
| **Syvyys 3** | | 
| avg. aika | ~ 2 s | ~0.3 s
| Arvionteja laskettu yht. | 200 000 | 20 000
|**Syvyys 4** | |
| avg. aika | >20 s | ~ 2.5 s
| Arvionteja laskettu yht.| 200 000 jo ensimmäiseen siirtoon | 200 000
|**Syvyys 5** | |
| avg. aika | - | ~ 17 s
| Liikkeitä laskettu yht. | 20^5? | 1 300 000

#### Yhteenveto tehokkuustesteistä
AB-pruning selkeästi vähentää laskettavien polkujen määrää. Helpoissa puzzleissa päästään parhaimmillaan jopa satakertaisiin eroihin, joka itse AB-pruning -toiminnan ohella johtuu 
1) puzzlejen yksinkertaisuudesta
2) siitä, että generoitu 'MoveList' aina järjestetään järjestykseen 'Checks, Captures, others'. 

Myös pelin alussa nähdään ainakin 10x erot.

### Saavutetut aika- ja tilavaativuudet
Pelkkä Minimaxin teoreettinen aikavaatimus on O(b^m), jossa 
- b = lailliset liikkeet 
- m = syvyys

Syvyydellä 3 luokka PerformanceTest laskee pelin ensimmäisellä siirrolla ~9000 tilannetta. Pelin alussa on 20 eri siirtovaihoehtoa, eli verrattuna teoreettiseen 20^3 = 8000, ero ei ole suuri. 
MinimaxAB sen sijaan laskee samassa tilanteessa samalla syvyydellä vain ~2000 tilannetta. 
Minulla ei ole syytä olettaa, että tilavaativuudet eroaisivat teoreettisista vaativuuksista.

--------------------


### Bugs & Problems
- Ruutu 64 bitboard esityksenä tekee long-muuttujan ylivuodon. (ainakin oman analyysin perustella). Tästä johtuen valmiiksi laskettujen liikelistojen viimeinen ruutu on aina 'kovakoodattu'.
- BoardOperations.isMoveLegal -metodi ei toimi halutulla metodilla (aiheuttaa bugin jota en ole saanut korjattua). Sen sijaan käytössä on epätehokkaampi versio.
- ~~Toteutukseni Zobrist Hashing:ista on hyvin alkeellinen. Useissa tilanteissa se siis saattaa johtaa siihen, että objektiivisesti parasta siirtoa ei löydetäkkään.~~ En saanut Zobristia toimimaan halutulla tavalla ja aika loppui kesken, joten päätin ottaa sen pois käytöstä.
- Tein oman toteutuksen LinkedLististä, mutta en saanut sitä toimimaan, joten ajan puutteen takia reverttasin takaisin javan toteutukseen. 

### Mitä jäi puuttumaan
- Zobrist Hashing olisi ollut kiva saada toimimaan kunnolla (käytin siihen kuitenkin ~10 tuntia)
- LinkedList sama homma (myös >5 tuntia 'hukkaan')
- Liikegeneroinnista puuttuu: linnoitus ja en passant

### Lähteitä
['Liukuvien' hyökkäysten](https://www.chessprogramming.org/Hyperbola_Quintessence) laskemiseen käytetty temppu.
[BitBoard operaatioiden](https://github.com/bhlangonijr/chesslib/tree/e6acbcb9d429c08918774edb2647b6f8e88db1cc/src/main/java/com/github/bhlangonijr/chesslib) käyttö. 
[Evaluation](https://adamberent.com/2019/03/02/chess-board-evaluation/) tauluja
[LinkedList ](https://www.youtube.com/watch?v=WEW7QkLFvko)(en edes saanut toimimaan)

