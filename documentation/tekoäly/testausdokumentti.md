### Testausdokumentti

Testit on toteutettu JUnit:illa. Kaikki (myös pohjaprojektin) testit saa juostua siis komennolla
``` ./gradlew test ```
Ja jos haluaa vain tämän tekoälyprojektin testit, niin esim. 
``` ./gradlew test --tests movegeneration.*```
( testiluokan nimeämiset vaatinee refaktorointia )

#### MiniMax (& Alpha-Beta Pruning)
Minimaxin testaamista varten testiluokassa ensin alustetaan tavanomaisia shakkitilanteita, jonka jälkeen MiniMax käy ne läpi. MiniMaxin (luontevasti) odotetaan palauttavan aina paras liike, toistaiseksi perustuen SimpleEvaluator -luokan arviontiin. 
Esimerkkitilanteita ovat mm:


| Yhden siirron shakkimatti  |  'Ilmainen' kuningatar | 'Ilmainen' kuningatar kahden siirron päässä | 'Royal Fork' |
|:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:|
![](https://github.com/ArtKoski/chessBot/blob/415c9f0c4ad8b75753d35fe8b6c007bcbde85547/documentation/teko%C3%A4ly/kuvat/checkMate.png) | ![](https://github.com/ArtKoski/chessBot/blob/415c9f0c4ad8b75753d35fe8b6c007bcbde85547/documentation/teko%C3%A4ly/kuvat/freeQueen.png) | ![](https://github.com/ArtKoski/chessBot/blob/415c9f0c4ad8b75753d35fe8b6c007bcbde85547/documentation/teko%C3%A4ly/kuvat/freeQueenDiscover.png) | ![](https://github.com/ArtKoski/chessBot/blob/415c9f0c4ad8b75753d35fe8b6c007bcbde85547/documentation/teko%C3%A4ly/kuvat/royalFork.png) |






