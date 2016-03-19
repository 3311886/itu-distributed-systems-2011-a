Rapport Notater
Udregning af værdier til app	1
Sundhed og grænseværdier	2
Miljøstyrelsen	2
WHO	2
Astma- og Allergi forbundet	3
How does the sensor work	3


Udregning af værdier til app
Sensor output
2.2 ± 0.5 µA/ppm

Android input og kalkulering
µg/m3 = x
ppmværdi = y
Input = a
y = a/2.2
x = y\*1880

x = 1880 **a/2.2**

Omregning
1ppm = 1880 µg/m3;  1mg/m3  = 0,53ppm;  1µg/m3  = 5,32 x10-4ppm.

Grænseværdier
Et af EU og WHO udstedt direktiv opridser grænseværdierne:
200 µg/m3 (0,1064 ppm) må ikke overskrides i en time mere end 18 gange årligt. Det bliver det heller aldrig.
40 µg/m3 (0,02128 ppm) er årsmiddelgrænseværdien som konstant overskrides for både Jagtvej og HC Andersens Boulevard (Se DMU: NO2 og NO tendenser for årsmiddelværdier)

Visualisering
Grundet at der kun i WHOs fastsatte grænseværdier er kalkuleret med en faktor 2 hvor der normalt ved befolkningens sundhed regnes med 10 (Se Miljøstyrelsens skrift om udendørs luftforurening på: http://www.mst.dk/Virksomhed_og_myndighed/
Luft/Hvad\_er\_luftforurening/effekter\_paa\_mennesker\_og\_miljoe/Arbejdsgruppe\_om\_udendoers\_luftforurening/05040104.htm). Hvis de 200 µg/m3 er lagt ved en faktor 2, må den højeste grænseværdi for en faktor 10 være 40 µg/m3. De 40 µg/m3 som er lagt som tilladt årsmiddelværdi af EU er ligeledes udregnet ud fra en faktor 2 og ville med en faktor 10 svare til 8µg/m3. Der skal tydeligt ved start af app gøres opmærksom på at dette kun kan være skadeligt hvis brugeren konstant i et år udsættes for denne mængde.
Vores grænseværdier for vores tre fastlagte niveauer (grøn, gul og rød) er således:
Grøn < 8µg/m3
Gul < 8µg/m3 and > 40µg/m3
Rød > 40µg/m3

Sundhed og grænseværdier
Miljøstyrelsen
Diskussion af grænseværdierne
”WHO (1999) har som nævnt (se afsnit 1) anbefalet en værdi på 200 µg/m3som 1-times værdi, begrundet i lette lungefunktionsforandringer hos astmatikere ved et niveau på 365-565 µg/m3, dvs. et såkaldt LOAEL. Der er brugt en usikkerhedsfaktor på 2, dvs. at der ikke er kalkuleret med den grad af beskyttelse, som normalt tilstræbes ved befolkningens udsættelse for miljøfremmede stoffer (hvor der oftest anvendes en usikkerhedsfaktor på 10 eller mere).
Skal der indlægges det sædvanlige sikkerhedsniveau, bør grænseværdien ligge væsentligt lavere end de foreslåede 200 µg/m3, fx 50 µg/m3. Et højt beskyttelsesniveau er bl.a. relevant pga. den tilsyneladende stigning i forekomsten i Danmark af personer med astma.”

Risikogrupper
”Både børn, astmatikere og personer med kronisk obstruktive lungesygdomme må betragtes som en særlig følsom gruppe.
Ud over grupper med særlig følsomhed er grupper, der er særligt udsatte for NO2-forureningen, også potentielle risikogrupper. Dette inkluderer bl.a. personer, der arbejder eller af anden grund opholder sig i længere tid i miljøer med tæt trafik, fx chauffører, cykelbude, gadehandlere, sportsfolk/motionister og legende børn.”

Vores tilgang
Et eventuelt skel mellem værdierne vil dermed kunne lægges ved 0-35µg/m3 er ren luft, 35-45µg/m3 er let forurenet luft, 45+ µg/m3 er forurenet luft. Hvis vi indsætter en tekstbox når app'en tændes der siger at indikatoren på luftforureningen er grænsesat udfra EU's direktiv om årsmiddelværdien, så kan vi godt bruge de grænseværdier. (Se Miljøstyrelsens skrift om udendørs luftforurening på: http://www.mst.dk/Virksomhed_og_myndighed/Luft/Hvad_er_luftforurening/effekter_paa_mennesker_og_miljoe/Arbejdsgruppe_om_udendoers_luftforurening/05040104.htm)
WHO
WHO Air quality guidelines
“Concentrations of NO2 are often strongly correlated with those of other toxic pollutants, and being the easier to measure, is often used as a surrogate for the pollutant mixture as a whole. Achieving guideline concentrations for individual pollutants such as NO2 may there¬fore bring public health benefits that exceed those anticipated on the basis of estimates of a single pollutant’s toxicity.” P. 7
“As noted above, the epidemiological evidence indicates that the possibility of adverse health effects remains even if the guideline value is achieved, and for this reason some countries might decide to adopt lower concentrations than the guidelines WHO guideline values as their national air quality standards” P. 8

“Recent indoor studies have provided evidence of effects on respiratory symptoms among infants at NO2 concentrations below 40 ìg/m3.” P. 15

(WHO Air quality guidelines for particulate matter, ozone, nitrogen dioxide and sulfur dioxide, Global update 2005, Summary of risk assessment, World health Organization, Geneva 2006, Retrieved 03.11.11,  http://whqlibdoc.who.int/hq/2006/WHO_SDE_PHE_OEH_06.02_eng.pdf)

Astma- og Allergi forbundet
Astma- og allergi forbundet
Studier tyder dog på, at effekten på vores helbred forstærkes, når flere stoffer optræder samtidig. Eksempelvis ved vi, at luftforureningen - udover at give en forværring af astmasymptomer - også forstærker den negative effekt ved pollen hos pollenallergikere.(http://dinhverdag.astma-allergi.dk/luftforurening/saadanpaavirkesdithelbred)

How does the sensor work
•	We won't discuss how this sensor works internally, for our purposes this depths of knowledge is not necessary. All we need to know is how to use the output pins. and how to use that information.
o	Sensing: connected to analog pin
o	Counter: not connected (used internally)
o	Auxilary: connected to analog pin
o	Reference: not connected (used internally)
•	The sensing pin generates a signal in response to atmospheric gases. By reading this signal into an analog pin, we convert this analog signal to a digital value. The sensing pin is sensitive to certain atmospheric conditions such as humidity and temperature, so we cannot fully rely on the signal value generated.
•	The Auxilary pin is also affected by certain atmospheric conditions, but doesn't react to the atmospheric gases specified for this sensor (CO or O3/NO2).
•	Hence we can simply substract the auxilary value from the sensing value and will optain the correct PPM information.
•	Please note that the sensor is shipped with 2 grounding springs attached between the 4 output pins. Those springs should be reattached after each usage.
(http://www.beatrizdacosta.net/stillopen/sensor.php)