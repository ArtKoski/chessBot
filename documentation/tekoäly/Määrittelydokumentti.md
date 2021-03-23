# BlunderBot
Kyseessä on shakkitekoäly, jossa käytän projektia 'TiraLabra/chess' pohjana. Ohjelmointikieli tulee olemaan Java.   
Olennainen ongelma on luoda tekoäly, joka pelaa 'fiksusti' ihmisen näkökulmasta. On myös tärkeää, että tekoäly pystyy laskemaan liikkeensä nopeasti.

## Tietorakenteet/algoritmit
Projekti jakautuu about kolmeen osaan:
1. Laillisten liikkeiden generointi
2. Minimaxin toteutus sekä Alpha-Beta pruning optimointiin
3. Pelitilanteen arviointi 

## Laillisten liikkeiden generointi
Tuntuu sen verran työläältä, että toistaiseksi on suunnitelmissa jättää ainakin 'en passant' ja tornitus pois. (riippuu kauan menee ihan perussiirtojen tekemisessä)

## Pelitilanteen arviointi
Siirtoja vertaillaan toisiinsa niiden pistevaikutuksen perusteella. Tällöin **yksinkertaisimmillaan**:
- Sotilaan liike eteenpäin aloituksessa -> 0 pistettä (neutraali)
- Musta sotilas syö valkoisen hevosen = valkoinen menettää 3 pisteen arvoisen yksilön -> -3 pistettä (musta voitolla)

Järkevämmässä toteutuksessa on tarkoitus ottaa myös vähemmän ilmiselvät asiat huomioon, esim:
- Sotilas syö toisen sotilaan, mutta nyt omat sotilaat ovat 'peräkkäin' -> ei välttämättä yhtä hyvä tilanne kuin että jos sotilaat olisivat vierekkäin
- Hevonen siirtyy laudan reunalle -> mahdollisten siirtojen määrä vähenee, siis huonompi tilanne 

Minimaxi arvioi kaikki mahdolliset tilanteet ja valitsee niistä parhaimman.
Aloittelijakin kuitenkin näkee usein jopa parin siirron päähän, ja näin tekoälynkin pitäisi pystyä näkemään esim. ilmiselvät vaihtokaupat. Tullaan siis tarviamaan rekursiota, mikä kuuluukin minimaxin toimintaan.

Tehokkuden kannalta kaikista olennaisinta on, että 'turhien' siirtojen laskemiseen ei käytetä aikaa. (Tähän tarkoitus käyttää 'alpha-beta pruning':ia)


## Minimax
Minimax algoritmi käy läpi puuta, josta löytyy mahdolliset pelisiirrot ja niiden pistevaikutukset. Algoritmi etsii puusta ne siirrot, jotka tuottavat maksimaalisen/minimaalisen pistemäärän pelaajalle (maksimaalisen jos pelaa valkoisilla, ja minimaalisen jos mustilla).
Algoritmin aikavaativuus on O(b^n), 
ja tilavaativuus O(bn), jossa
b = lailliset siirrot sekä
n = puun syvyys

~~Epäselvää tällä hetkellä on:~~
- ~~Onko tarkoitus tehdä shakin 'säännöt' omalla toteutuksella, vai saako valmista MoveGenerator.generateLegalMoves() käyttää, joka siis palauttaa kaikki lailliset siirrot. Jos tämä on tarkoitus itse koodata, niin oletan, että tämä tulee olemaan hyvin työläs osa projektia.~~

Opinto-ohjelma: TKT kandi ja kielenä suomenkieli. Koska pohjaprojekti on koodattu sekä kommentoitu englanniksi, niin aion luultavasti tehdä koodin englanniksi.


Inspiraatio & lähteet
[Yleisesti](https://www.freecodecamp.org/news/simple-chess-ai-step-by-step-1d55a9266977/)
[Minimax](https://cis.temple.edu/~vasilis/Courses/CIS603/Lectures/l7.html)
[Minimax, Alpha-Beta](https://github.com/carterjbastian/alpha-beta-chess-ai/blob/master/chess-ai.pdf)


