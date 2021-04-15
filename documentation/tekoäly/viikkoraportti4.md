### Viikkoraportti 4

|   pv	|   	aihe   	|  tuntia 	|
|---	|---		|---	|
|13.4  	|   ?	   	|   2	|
|14.4 | Tein ComplexEvaluator- luokan, joka perii SimpleEvaluatorin ja lisää muutaman lisälaskun pisteiden laskentaan (ei oikeastaan ollenkaan kompleksi ainakaan toistaiseksi). Refaktoroin BitOperations luokasta siihen kuulumattomat metodit muualle.  | 4?
| | MiniMaxin hitaus häiritsi, joten tein pienen muutoksen GenerateLegalMoves:iin: liikelista järjestestään järjestykseen  'checks', 'attacks', 'the rest'. Tämä nopeutti minimaxia ainakin nykyisissä testeissä *huomattavasti*. 
|15.4 | Koitin ymmärtää miten pohjaprojektin 'GameState' toimii, jotta voisin sen avulla tehdä suorituskykytestejä | 1
| | Tajusin, että sitä mukaa kun Evaluatoriin lisää tavaraa, niin laskentanopeus hidastuu. Esim. ilman 'mobility bonus':ta syvyydellä 5 MiniMax laski kaikki pulmat alle 15 sekuntiin, kun taas ilman sitä laskuaika vähintään tuplaantui. Tässä on ongelmana liikkeiden generoinnin hitaus, erityisesti 'liukuvien' palasten hyökkäysten laskemisessa ('hyvissä' engineissä käytetään 'magic bitboardeja', johon en ehtinyt itse perehtyä).| 3
| | **Siis**: Pitää luultavasti vain hyväksyä, että nykyisellä toteutuksella pitää pysyä noin syvyydessä 3, ellei halua odotella joka liikkeen välissä.   |
| | Pelasin pitkästä aikaa botillla lichesin bottia vastaan: vain syvyydellä 3 botti näytti lupaavalta. (aloituksessa hieman outoja agressiivisia liikkeitä)|
