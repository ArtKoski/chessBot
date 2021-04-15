### Testausdokumentti

Testit on toteutettu JUnit:illa. Kaikki (myös pohjaprojektin) testit saa juostua siis komennolla
``` ./gradlew test ```  
Ja jos haluaa vain tämän tekoälyprojektin testit, niin esim.  
``` ./gradlew test --tests movegeneration.*```  
( testiluokan nimeämiset vaatinee refaktorointia )

#### MiniMax (& Alpha-Beta Pruning) | Luokka: MiniMaxTest.java
Minimaxin testaamista varten testiluokassa ensin alustetaan tavanomaisia shakkitilanteita, jonka jälkeen MiniMax käy ne läpi. MiniMaxin (luontevasti) odotetaan palauttavan aina paras liike, perustuen ComplexEvaluator -luokan arviontiin. 
Esimerkkitilanteita ovat mm:


| Yhden siirron shakkimatti (musta siirtovuoro)  |  'Ilmainen' kuningatar (kumpi tahansa) | 'Ilmainen' kuningatar kahden siirron päässä (valkoinen) | 'Royal Fork' (valkoinen) |
|:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:|
|![g1a1](https://github.com/ArtKoski/chessBot/blob/415c9f0c4ad8b75753d35fe8b6c007bcbde85547/documentation/teko%C3%A4ly/kuvat/checkMate.png) | ![](https://github.com/ArtKoski/chessBot/blob/415c9f0c4ad8b75753d35fe8b6c007bcbde85547/documentation/teko%C3%A4ly/kuvat/freeQueen.png) | ![](https://github.com/ArtKoski/chessBot/blob/415c9f0c4ad8b75753d35fe8b6c007bcbde85547/documentation/teko%C3%A4ly/kuvat/freeQueenDiscover.png) | ![](https://github.com/ArtKoski/chessBot/blob/415c9f0c4ad8b75753d35fe8b6c007bcbde85547/documentation/teko%C3%A4ly/kuvat/royalFork.png) |
| g1a1 | a1h1 / h1a1 | d3d5 | e4g3  |

Kuvan alhaalla on merkitty paras siirto, eli siis MiniMaxin odotettu ulostulo. Koodissa toteutus on tehty siten, että kukin 'Setup' tallenetaan HashMappiin sen FEN-notaatio ID:llä (FEN-id suoraan chessLib:in metodista), ja arvoparina löytyy tilanteen oikea ratkaisu String-muodossa. Ennen testien runnausta Setupit alustetaan, jonka jälkeen testimetodi käy läpi kunkin HashMapin avaimen (=pelitilanteen) verraten saatua MiniMax-Algon tulosta sekä HashMapin arvoparia (=haluttua siirtoa).


#### Esilasketut 'liiketaulut' | Luokka: BitOperationsTest.java
Kyseessä siis esim. hevosen mahdollisten liikkeiden generointi kussakin ruudussa. Testasin omat toteutukset yksinkertaisesti vertaamalla valmiisiin (oikeisiin) esilaskettuihin arrayihin. Käytin tässä hyödyksi bhlangonijr.chesslib:iä.

#### (pseudo)Laillisten liikkeiden generointi | Luokka: MoveGeneratorTest.java
Kyseessä kunkin palasen mahdollisten liikkeiden generointi tietyssä pelitilanteessa. Testattu, että kukin nappula löytää kussakin geneerisessä tilanteessa (nappula kavereiden ympäröimänä / nappula tyhjällä pöydällä / nappula vihollisten ympäröimänä) oikean määrän siirtoja. Tässä testautuu siis vähän samoja asioita kun puhtaassa liikkeiden generoinnissa tyhjällä laudalla, mutta näiden lisäksi myös tilanteet joissa omat/vastustajan palaset leikkaavat kulkuväylän.  

#### Lailliset liikkeet psedoliikkeistä | Luokka: BoardOperationsTest.java
Tällä hetkellä testattu vain muutamassa kohtaa (ettei esim. *pinnattu* sotilas voi liikkua ja aiheuttaa laitonta *checkkiä*). Vaatii (ehkä) vielä hieman työtä.

#### Evaluator testit | EvaluationTest.java
Muutamat testit Evaluator-luokkien yksittäisille metodeille. 




