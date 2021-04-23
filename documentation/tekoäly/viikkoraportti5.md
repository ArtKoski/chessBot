### Viikkoraportti 5
|   pv	|   	aihe   	|  tuntia 	|
|---	|---		|---	|
|19/21.4 | Bugin etsimistä isCheckMate()/isMoveLegal:in kanssa. Luovutin toistaiseksi (toimii siis vaihtoehtoisella, 'epätehokkaalla tavalla')..   	   	|   2-3	|
| 21.4 | Yritin tehostaa minimaxia sekä arviontia: | 3
| | Minimax: Jos checkmate havaitaan, niin algoritmi ei enää lähde arvioimaan lautaa, vaan palauttaa vain suuren arvon kerrottuna sen hetkisellä syvyydellä. |
| | Evaluation: Luin Zobrist-avainten käytöstä. Tein tästä todella yksinkertaisen version. En edes tiedä toimiiko se halutulla tavalla, mutta testit ainakin menevät läpi ja tehokkuus parani ~*20%* <- **ei luultavasti toimi halutulla tavalla**|
|22.4| Halusin testata kuinka paljon liikkeiden generointi hidastaa algoritmiä. Vertasin chessLibin 'generateRook/Bishop/Queen' metodeja omiin, ja ero oli järjettömän suuri. Pelin ensimmäiset 5 siirtoa tippui 3 minuutista puoleentoista (ilman muita hienouksia kuten esim. moveListin järjestäminen). Näyttäisi siis siltä, että liikkeiden generointia on tehostettava, jos haluaa päästä tehokkaampiin tuloksiin.   -> Rupesin käymään bishop/rook liikegenerointia uusiksi läpi| 2.5
| | bugien etsimistä, tehokkuustestausta jotta tietää mihin pitäisi keskittyä | 2.5
| | Löysin kivan tavan tehdä bishop/rook generoinnit uusiksi ('Hyperbola Quintessence'). Täytyy perehtyä vielä hieman enemmän ja lähteä koodaamaan. | 1
| 23.4 | En vieläkään saanut isMoveLegal bugia korjattua | 1
| | MovesGenerator luokkaa refaktoroitu ja tehostettu. | 2
| | Evaluatoriin laitettu pari taulua, joiden avulla luokka suosii esim. keskellä olevia ratsuja reunalla olevien sijaan.  | 1
| | Tehokkuustestausta ja pohdintaa siitä miten saada koko hommaa tehokkaammaksi. Pelin alku on hidas, sekä myös vaikeat puzzlet ovat hitaita laskea. Tällä hetkellä 'Zobrist' -avainten käyttö  voisi auttaa.|
|||total: 16h

#### Yhteenveto
Tässä projektissa on monta liikkuvaa osaa, ja kokoajan jää jokin osa kesken, kun ei halua juuttua yhteen asiaan liian pitkäksi aikaa kerrallaan. 

**Pieni huolenaihe on siis**: onko prioriteettinä tekoälyn parantaminen vaiko tietorakenteiden omien versioiden rakentaminen? Toisin sanoen: Keskitynkö siihen, että laskentateho sekä puun oksien karsintakyky paranee vai siihen, että esim. hashmap sekä movelist -luokat ovat omaa tekoa? Toistaiseksi olen tehnyt kompromissejä sen eteen, että voisin keskittyä tekoälyn nopeuttamiseen. Käytössä on siis paljon esim. chessLib:in metodeja että Javan valmiita (LinkedList, HashMap). Aika ei tunnu riittävän molempiin.
