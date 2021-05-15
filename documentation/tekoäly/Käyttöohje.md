## Käyttöohje 
*Jos* kiinnostaa tehdä tai on jo olemassa Lichess-botti -tunnus, niin botilla voit pelata muita lichess-botteja vastaan. [täältä](https://github.com/ArtKoski/chessBot/blob/master/documentation/projektiPohja/Beginners_guide.md#playing-on-lichess) löytyy ohjeet. Myös XBoard (varmaankin) toimii, mutta en ole itse sitä käyttänyt.
(TLDR; Kun lichess-botti on olemassa, tarvitset botin 'tokenin'. Avaa lichessissä peli bottia vastaan ja aja
```./gradlew run --args="--lichess --token=put_token_here" ``` omalla tokenilla.)  
*huom* koska linnoitusta eikä 'en passant':ia ole toteutettu, bitboard menee 'rikki' näistä siirroista.

#### Jos ei,
niin botin toimintaa simuloi PerformanceTest -luokka, joka käynnistyy *jar*:ista. 

#### Jar:in käyttö
Lataa uusin release, pura lataus, mene latauskansioon, aja jar komennolla
``` java -jar chess.jar ```
Jar siis vain käynnistää PerformanceTest -luokan, jossa botti pelaa itseään vastaan syvyydellä 4. Siirrot tulostuvat terminaaliin.

#### Manuaalinen käynnistys
Lataa projekti, aja komento
```./gradlew run performanceTest ```
Syvyyttä voi muokata luokasta **chess.bot.BlunderBot** 
vaihtamalla miniMax.launch(x, y) y-parametria.
