# Content #
About how to estimate air pollution, in NO2, that is unhealthy for humans from measuring NOx.

## **Estimation of NO2** ##
The estimation of unhealthy NOx gasses must be given as the NOx's NO2 equivalence. The NO2 content in the air along roads is very dependant of how many percent NOx of the cars exhaust is NO2 and furthermore how much O3 (Ozon) there is in the air. The determination of how much NOx is transformed to NO2 is very dependant on how much Ozon that is in the air, and that content is again dependant on other factors such as weather and the NO-content in the air.
The way to estimate the O3-content in the air would be to use the measurements made by DMU on a certain location for 13 years (http://www2.dmu.dk/1_Viden/2_miljoetilstand/3_luft/4_maalinger/5_niveauer/6_o3/O3_trend_month.asp).

## **Calculation of NO2** ##
What we need now is a formula that can be implemented in our Android/Ardiuno program that knows the time of the year and thereby the estimated O3 content, the approximate distance to the car, the approximate NO2 and NOx content in the exhaust of the cars and the NOx content at the bike and the transformation-factor for NOx to NO2 due to the found O3 content.

## **Measuring the unhealthiness** ##
To measure how much NO2 should breathe before it is unhealthy the limits given by EU is needed. The limits is that a human must not breathe over 40 my gram pr cubic meter NO2 on average a year, and cannot breathe 200 my gram pr cubic meter NO2 more than 28 hours a year.

## **The accuracy of the measurements** ##
The accuracy of the measurements can be found by knowing the accuracy on the sensor and by eventually calibrating with with the known and accurate values measured by DMU with station placed at HC Andersens Boulevard and Jagtvej in Copenhagen (http://www2.dmu.dk/1_Viden/2_miljoe-tilstand/3_luft/4_maalinger/5_niveauer/6_NOX/NOX_trend_ave.asp , http://www2.dmu.dk/1_Viden/2_Miljoe-tilstand/3_luft/4_Maalinger/5_stationer/copenh.asp)

## **Literature on the subject** ##
- http://www2.dmu.dk/1_Viden/2_miljoe-tilstand/3_luft/4_maalinger/5_niveauer/6_o3/O3_trend_month.asp
- http://www2.dmu.dk/1_Viden/2_miljoe-tilstand/3_luft/4_maalinger/5_niveauer/6_o3/O3_generelt.asp
- http://www.dmu.dk/Luft/Stoffer/Graensevaerdier/
- http://www.force.dk/da/Menu/Services/Emissioner/Luftforurening/
- http://www2.dmu.dk/1_Viden/2_miljoe-tilstand/3_luft/4_maalinger/5_niveauer/smog.asp
- http://www2.dmu.dk/1_Viden/2_miljoe-tilstand/3_luft/4_maalinger/5_niveauer/6_NOX/NOX_generelt.asp
- http://www2.dmu.dk/1_Viden/2_miljoe-tilstand/3_luft/4_maalinger/5_niveauer/6_NOX/NOX_trend_ave.asp
- http://www2.dmu.dk/1_Viden/2_miljoe-tilstand/3_luft/4_maalinger/5_niveauer/6_NOX/NOX_trend_ave.asp
- http://www2.dmu.dk/1_Viden/2_Miljoe-tilstand/3_luft/4_Maalinger/5_stationer/copenh.asp