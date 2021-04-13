### Testausdokumentti

Testit on toteutettu JUnit:illa. Kaikki (myös pohjaprojektin) testit saa juostua siis komennolla
``` ./gradlew test ```
Ja jos haluaa vain tämän tekoälyprojektin testit, niin esim. 
``` ./gradlew test --tests movegeneration.*```
(testiluokka vaatinee refaktorointia ^)

#### MiniMax (& Alpha-Beta Pruning)
Minimaxin testaamista varten testiluokassa ensin alustetaan tavanomaisia shakkitilanteita, jonka jälkeen MiniMax käy ne läpi. MiniMaxin (luontevasti) odotetaan palauttavan aina paras liike, toistaiseksi perustuen SimpleEvaluator -luokan arviontiin. 
Esimerkkitilanteita ovat mm:

Yhden siirron shakkimatti  |  'Ilmainen' kuningatar | 'Ilmainen' kuningatar kahden siirron päässä | 'Royal Fork' |
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:|
<img src="https://github.com/ArtKoski/chessBot/tree/master/documentation/tekoäly/kuvat/checkMate.png"> |  <img src="https://github.com/ArtKoski/chessBot/tree/master/documentation/tekoäly/kuvat/freeQueen.png"> | <img src="https://github.com/ArtKoski/chessBot/tree/master/documentation/tekoäly/kuvat/freeQueenDiscover.png">  | <img src="https://github.com/ArtKoski/chessBot/tree/master/documentation/tekoäly/kuvat/royalFork.png">








