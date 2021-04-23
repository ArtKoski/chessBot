## Toteutusdokumentti (pohja)

### yleisrakenne (NA)

### Suorituskykyvertailu 
Suorituskyky on kokoajan kehityksessä -> En tee vielä vertailuja

### Bugs & Problems
- Ruutu 64 bitboard esityksenä tekee long-muuttujan ylivuodon. (ainakin oman analyysin perustella). Tästä johtuen valmiiksi laskettujen liikelistojen viimeinen ruutu on aina 'kovakoodattu'.
- BoardOperations.isMoveLegal -metodi ei toimi halutulla metodilla (aiheuttaa bugin jota en ole saanut korjattua). Sen sijaan käytössä on epätehokkaampi versio.

### Lähteitä
['Liukuvien' hyökkäysten](https://www.chessprogramming.org/Hyperbola_Quintessence) laskemiseen käytetty temppu.
[BitBoard operaatioiden](https://github.com/bhlangonijr/chesslib/tree/e6acbcb9d429c08918774edb2647b6f8e88db1cc/src/main/java/com/github/bhlangonijr/chesslib) käyttö. Samasta projektista myös monta luokkaa käytössä, mm. Square, Side, Piece, Board. 

