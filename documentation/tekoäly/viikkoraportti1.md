## Viikkoraportti 1

Päätettyäni aiheeni (shakkitekoäly) minulla meni jonkin aikaa lueskella ja opiskella projektipohjaa. Saatuani Lichessin toimimaan botin kanssa, rupesin tutkimaan tekoälyn toteutusta. Baeldungin artikkeli antoi sellaisen kuvan, että projekti tulisi olemaan mukava ja leppoisa, mutta ei mennyt kauaa ymmärtää, että asiat eivät olekaan niin yksinkertaisia. 

Lähdin jo koodailemaan 'evaluate' funktiota, jonka on tarkoitus arvioida kyseinen pelitilanne. Tein alkuun hyvin yksinkertaisen toteutuksen, jossa arvio vain lasketaan kummankin puolen nappuloiden määrästä. Haasteena onkin ottaa huomioon pienet yksityiskohdat.

Tässä vaiheessa tajusin, että valmista 'GenerateLegalMoves':ia ei luultavasti saa käyttää. Päätin kuitenkin toistaiseksi käyttää sitä jotta voin keskittyä esim. minimaxin toteutukseen. Minimaxin idea on hyvin yksinkertainen, mutta sen toteutus saattaa olla hankala. 

En ole siis vielä lähtenyt kunnolla koodaamaan mitään, mutta perusidea rupeaa syntymään. Olen oppinut minimaxin sekä siihen tehtävän optimoinnin idean. 

Jos siis lailliset liikket pitää itse generoida, niin uskon takertuvani siihen seuraavaksi, koska siinä luultavasti tulee menemään hetki. Minimaxin toteutus sekä tilannearvion hiominen kuulostaa kuitenkin paljon kiinnostavammalta. 
Hieman **epäselvää** siis on: onko laillisten liikkeiden generointi olennainen osa projektia? Itse näen minimaxin sekä alpha-beta pruningin olevan se tärkeä asia.

Yksi haastava asia minulle tällä hetkellä on ymmärtää, että miten lautaa tulkitaan koodissa. Jos siis esim. haluan tutkia tietyn palikan ympäröiviä ruutuja, niin miten se onnistuu. Oletan, että voin käyttää pohjaprojektin metodeja getFen/getBitboard tässä hyödykseni.

Käytin projektiin noin 6-8 tuntia tällä viikolla. 

