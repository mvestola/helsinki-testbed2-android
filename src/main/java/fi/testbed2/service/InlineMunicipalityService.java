package fi.testbed2.service;

import fi.testbed2.data.Municipality;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Municipality service containing the municipalities inline in the Java code.
 */
public class InlineMunicipalityService implements MunicipalityService {

    private SortedMap<String, Municipality> municipalitiesInTestbedMap;

    public InlineMunicipalityService() {

    }

    @Override
    public Municipality getMunicipality(String name) {
        return getFinlandMunicipalitiesShownInTestbedMap().get(name);
    }

    private void addMunicipality(String name, double lat, double lon) {

        /*
         * Limit municipalities to those visible in the map.
         * Limit lon (x-axis) both directions and lat (y-axis) only from the top boundary
         * (cause no Finnish municipalities are below the map bottom boundary).
         */
        if (lat<=61.005 && lon>=22.657 && lon<=26.792) {
            municipalitiesInTestbedMap.put(name, new Municipality(name, lat, lon));
        }

    }

    @Override
    public String[] getFinlandMunicipalityNamesShownInTestbedMap() {
        SortedMap<String, Municipality> map = getFinlandMunicipalitiesShownInTestbedMap();
        return map.keySet().toArray(new String[0]);
    }

    /**
     * Returns list of municipalities (kunnat) in Finland which are shown
     * in the testbed map still image. List of municipalities is copied from
     * http://fi.wikipedia.org/wiki/Luettelo_Suomen_kuntien_koordinaateista .
     * Not all municipalities are shown but only a small number of them.
     *
     * @return
     */
    @Override
    public SortedMap<String, Municipality> getFinlandMunicipalitiesShownInTestbedMap() {

        if (municipalitiesInTestbedMap==null) {

            municipalitiesInTestbedMap = new TreeMap<String, Municipality>();

            addMunicipality("Akaa", 61.169, 23.858);
            addMunicipality("Alajärvi", 63, 23.817);
            addMunicipality("Alastaro", 60.957, 22.86);
            addMunicipality("Alavieska", 64.167, 24.3);
            addMunicipality("Alavus", 62.583, 23.617);
            addMunicipality("Anjalankoski", 60.697, 26.846);
            addMunicipality("Artjärvi", 60.75, 26.083);
            addMunicipality("Asikkala", 61.173, 25.549);
            addMunicipality("Askainen", 60.571, 21.866);
            addMunicipality("Askola", 60.53, 25.599);
            addMunicipality("Aura", 60.646, 22.577);
            addMunicipality("Brändö", 60.413, 21.047);
            addMunicipality("Dragsfjärd", 60.067, 22.483);
            addMunicipality("Eckerö", 60.213, 19.614);
            addMunicipality("Elimäki", 60.717, 26.452);
            addMunicipality("Eno", 62.8, 30.15);
            addMunicipality("Enonkoski", 62.083, 28.933);
            addMunicipality("Enontekiö", 68.383, 23.633);
            addMunicipality("Espoo", 60.209, 24.658);
            addMunicipality("Eura", 61.133, 22.133);
            addMunicipality("Eurajoki", 61.2, 21.733);
            addMunicipality("Evijärvi", 63.367, 23.483);
            addMunicipality("Finström", 60.228, 19.995);
            addMunicipality("Forssa", 60.819, 23.621);
            addMunicipality("Föglö", 60.03, 20.392);
            addMunicipality("Geta", 60.383, 19.85);
            addMunicipality("Haapajärvi", 63.752, 25.329);
            addMunicipality("Haapavesi", 64.145, 25.377);
            addMunicipality("Hailuoto", 65.01, 24.715);
            addMunicipality("Halikko", 60.4, 23.083);
            addMunicipality("Halsua", 63.467, 24.167);
            addMunicipality("Hamina", 60.564, 27.201);
            addMunicipality("Hammarland", 60.217, 19.75);
            addMunicipality("Hankasalmi", 62.385, 26.431);
            addMunicipality("Hanko", 59.833, 22.95);
            addMunicipality("Harjavalta", 61.316, 22.135);
            addMunicipality("Hartola", 61.583, 26.018);
            addMunicipality("Hattula", 61.067, 24.383);
            addMunicipality("Hauho", 61.167, 24.55);
            addMunicipality("Haukipudas", 65.183, 25.35);
            addMunicipality("Hausjärvi", 60.783, 25);
            addMunicipality("Heinola", 61.205, 26.04);
            addMunicipality("Heinävesi", 62.433, 28.6);
            addMunicipality("Helsinki", 60.17, 24.931);
            addMunicipality("Himanka", 64.067, 23.65);
            addMunicipality("Hirvensalmi", 61.642, 26.788);
            addMunicipality("Hollola", 60.987, 25.537);
            addMunicipality("Honkajoki", 61.983, 22.267);
            addMunicipality("Houtskari", 60.217, 21.383);
            addMunicipality("Huittinen", 61.178, 22.689);
            addMunicipality("Humppila", 60.933, 23.367);
            addMunicipality("Hyrynsalmi", 64.677, 28.51);
            addMunicipality("Hyvinkää", 60.633, 24.876);
            addMunicipality("Hämeenkoski", 61.03, 25.157);
            addMunicipality("Hämeenkyrö", 61.633, 23.2);
            addMunicipality("Hämeenlinna", 61, 24.45);
            addMunicipality("Ii", 65.317, 25.367);
            addMunicipality("Iisalmi", 63.567, 27.183);
            addMunicipality("Iitti", 60.89, 26.35);
            addMunicipality("Ikaalinen", 61.772, 23.067);
            addMunicipality("Ilmajoki", 62.732, 22.578);
            addMunicipality("Ilomantsi", 62.667, 30.917);
            addMunicipality("Imatra", 61.19, 28.774);
            addMunicipality("Inari", 68.9, 27.017);
            addMunicipality("Iniö", 60.05, 24.017);
            addMunicipality("Inkoo", 60.05, 24.017);
            addMunicipality("Isojoki", 62.117, 21.967);
            addMunicipality("Isokyrö", 63, 22.317);
            addMunicipality("Jaala", 61.05, 26.483);
            addMunicipality("Jalasjärvi", 62.49, 22.764);
            addMunicipality("Janakkala", 60.9, 24.6);
            addMunicipality("Joensuu", 62.6, 29.767);
            addMunicipality("Jokioinen", 60.803, 23.483);
            addMunicipality("Jomala", 60.155, 19.958);
            addMunicipality("Joroinen", 62.183, 27.833);
            addMunicipality("Joutsa", 61.748, 26.129);
            addMunicipality("Joutseno", 61.117, 28.518);
            addMunicipality("Juankoski", 63.064, 28.32);
            addMunicipality("Juuka", 63.246, 29.251);
            addMunicipality("Juupajoki", 61.783, 24.45);
            addMunicipality("Juva", 61.899, 27.863);
            addMunicipality("Jyväskylä", 62.233, 25.733);
            addMunicipality("Jyväskylän maalaiskunta", 62.287, 25.747);
            addMunicipality("Jämijärvi", 61.817, 22.7);
            addMunicipality("Jämsä", 61.867, 25.2);
            addMunicipality("Jämsänkoski", 61.917, 25.183);
            addMunicipality("Järvenpää", 60.478, 25.083);
            addMunicipality("Kaarina", 60.391, 22.376);
            addMunicipality("Kaavi", 62.976, 28.483);
            addMunicipality("Kajaani", 64.233, 27.683);
            addMunicipality("Kalajoki", 64.25, 23.95);
            addMunicipality("Kalvola", 61.1, 24.117);
            addMunicipality("Kangasala", 61.463, 24.076);
            addMunicipality("Kangasniemi", 61.983, 26.633);
            addMunicipality("Kankaanpää", 61.803, 22.39);
            addMunicipality("Kannonkoski", 62.967, 25.25);
            addMunicipality("Kannus", 63.904, 23.896);
            addMunicipality("Karijoki", 62.3, 21.7);
            addMunicipality("Karjaa", 60.069, 23.68);
            addMunicipality("Karjalohja", 60.25, 23.717);
            addMunicipality("Karkkila", 60.532, 24.219);
            addMunicipality("Karstula", 62.867, 24.783);
            addMunicipality("Karttula", 62.897, 26.965);
            addMunicipality("Karvia", 62.133, 22.567);
            addMunicipality("Kaskinen", 62.376, 21.231);
            addMunicipality("Kauhajoki", 62.424, 22.17);
            addMunicipality("Kauhava", 63.1, 23.083);
            addMunicipality("Kauniainen", 60.212, 24.726);
            addMunicipality("Kaustinen", 63.533, 23.7);
            addMunicipality("Keitele", 63.183, 26.367);
            addMunicipality("Kemi", 65.733, 24.567);
            addMunicipality("Kemijärvi", 66.719, 27.425);
            addMunicipality("Keminmaa", 65.817, 24.533);
            addMunicipality("Kemiö", 60.167, 22.7);
            addMunicipality("Kempele", 64.917, 25.5);
            addMunicipality("Kerava", 60.4, 25.117);
            addMunicipality("Kerimäki", 61.917, 29.283);
            addMunicipality("Kestilä", 64.35, 26.27);
            addMunicipality("Kesälahti", 61.893, 29.834);
            addMunicipality("Keuruu", 62.257, 24.706);
            addMunicipality("Kihniö", 62.2, 23.183);
            addMunicipality("Kiikala", 60.465, 23.55);
            addMunicipality("Kiikoinen", 61.45, 22.583);
            addMunicipality("Kiiminki", 65.133, 25.733);
            addMunicipality("Kinnula", 63.37, 24.97);
            addMunicipality("Kirkkonummi", 60.117, 24.433);
            addMunicipality("Kisko", 60.233, 23.483);
            addMunicipality("Kitee", 62.1, 30.15);
            addMunicipality("Kittilä", 67.651, 24.903);
            addMunicipality("Kiukainen", 61.217, 22.083);
            addMunicipality("Kiuruvesi", 63.65, 26.617);
            addMunicipality("Kivijärvi", 60.967, 21.883);
            addMunicipality("Kokemäki", 61.25, 22.35);
            addMunicipality("Kokkola", 63.844, 23.128);
            addMunicipality("Kolari", 67.331, 23.788);
            addMunicipality("Konnevesi", 62.617, 26.317);
            addMunicipality("Kontiolahti", 62.767, 29.85);
            addMunicipality("Korpilahti", 62.018, 25.565);
            addMunicipality("Korppoo", 60.167, 21.567);
            addMunicipality("Korsnäs", 60.167, 21.833);
            addMunicipality("Koski Tl", 60.655, 23.144);
            addMunicipality("Kotka", 60.46, 26.946);
            addMunicipality("Kouvola", 60.867, 26.7);
            addMunicipality("Kristiinankaupunki", 62.277, 21.358);
            addMunicipality("Kruunupyy", 63.717, 23.033);
            addMunicipality("Kuhmalahti", 61.5, 24.567);
            addMunicipality("Kuhmo", 64.125, 29.511);
            addMunicipality("Kuhmoinen", 61.567, 25.183);
            addMunicipality("Kumlinge", 60.261, 20.779);
            addMunicipality("Kuopio", 62.9, 27.683);
            addMunicipality("Kuortane", 62.8, 23.5);
            addMunicipality("Kurikka", 62.617, 22.417);
            addMunicipality("Kuru", 61.88, 23.72);
            addMunicipality("Kustavi", 60.55, 21.35);
            addMunicipality("Kuusamo", 65.967, 29.183);
            addMunicipality("Kuusankoski", 60.91, 26.624);
            addMunicipality("Kuusjoki", 60.517, 23.2);
            addMunicipality("Kylmäkoski", 61.167, 23.683);
            addMunicipality("Kyyjärvi", 63.033, 24.567);
            addMunicipality("Kälviä", 63.867, 23.433);
            addMunicipality("Kärkölä", 60.872, 25.269);
            addMunicipality("Kärsämäki", 63.98, 25.755);
            addMunicipality("Kökar", 59.933, 20.883);
            addMunicipality("Köyliö", 61.117, 22.35);
            addMunicipality("Lahti", 60.981, 25.655);
            addMunicipality("Laihia", 62.967, 22.017);
            addMunicipality("Laitila", 60.88, 21.689);
            addMunicipality("Lammi", 61.083, 25.017);
            addMunicipality("Lapinjärvi", 60.633, 26.217);
            addMunicipality("Lapinlahti", 63.366, 27.392);
            addMunicipality("Lappajärvi", 63.2, 23.633);
            addMunicipality("Lappeenranta", 61.056, 28.189);
            addMunicipality("Lappi", 61.104, 21.837);
            addMunicipality("Lapua", 62.96, 23.03);
            addMunicipality("Laukaa", 62.419, 25.953);
            addMunicipality("Lavia", 61.598, 22.593);
            addMunicipality("Lehtimäki", 62.65, 22.933);
            addMunicipality("Lemi", 61.05, 27.8);
            addMunicipality("Lemland", 60.071, 20.085);
            addMunicipality("Lempäälä", 61.317, 23.75);
            addMunicipality("Lemu", 60.57, 21.97);
            addMunicipality("Leppävirta", 62.483, 27.783);
            addMunicipality("Lestijärvi", 63.533, 24.65);
            addMunicipality("Lieksa", 63.317, 30.017);
            addMunicipality("Lieto", 60.507, 22.451);
            addMunicipality("Liljendal", 60.575, 26.058);
            addMunicipality("Liminka", 64.817, 25.4);
            addMunicipality("Liperi", 62.533, 29.367);
            addMunicipality("Lohja", 60.25, 24.083);
            addMunicipality("Lohtaja", 64.017, 23.5);
            addMunicipality("Loimaa", 60.851, 23.043);
            addMunicipality("Loppi", 60.713, 24.438);
            addMunicipality("Loviisa", 60.456, 26.222);
            addMunicipality("Luhanka", 61.783, 25.7);
            addMunicipality("Lumijoki", 64.84, 25.19);
            addMunicipality("Lumparland", 60.12, 20.264);
            addMunicipality("Luoto", 63.751, 22.745);
            addMunicipality("Luumäki", 60.911, 27.568);
            addMunicipality("Luvia", 61.365, 21.618);
            addMunicipality("Maalahti", 62.933, 21.567);
            addMunicipality("Maaninka", 63.157, 27.304);
            addMunicipality("Maarianhamina", 60.1, 19.95);
            addMunicipality("Marttila", 60.817, 25.383);
            addMunicipality("Masku", 60.567, 22.1);
            addMunicipality("Mellilä", 60.78, 22.91);
            addMunicipality("Merijärvi", 63.7, 23.183);
            addMunicipality("Merikarvia", 61.863, 21.507);
            addMunicipality("Merimasku", 60.48, 21.869);
            addMunicipality("Miehikkälä", 60.667, 27.7);
            addMunicipality("Mikkeli", 61.683, 27.25);
            addMunicipality("Mouhijärvi", 61.5, 23.017);
            addMunicipality("Muhos", 64.809, 25.991);
            addMunicipality("Multia", 62.414, 24.801);
            addMunicipality("Muonio", 67.958, 23.679);
            addMunicipality("Mustasaari", 63.117, 21.717);
            addMunicipality("Muurame", 62.133, 25.667);
            addMunicipality("Muurla", 60.35, 23.283);
            addMunicipality("Mynämäki", 60.678, 21.99);
            addMunicipality("Myrskylä", 60.667, 25.85);
            addMunicipality("Mäntsälä", 60.633, 25.317);
            addMunicipality("Mänttä", 62.033, 24.633);
            addMunicipality("Mäntyharju", 61.417, 26.877);
            addMunicipality("Naantali", 60.465, 22.019);
            addMunicipality("Nakkila", 61.367, 22);
            addMunicipality("Nastola", 60.944, 25.937);
            addMunicipality("Nauvo", 60.011, 21.906);
            addMunicipality("Nilsiä", 63.212, 28.06);
            addMunicipality("Nivala", 63.923, 24.958);
            addMunicipality("Nokia", 61.467, 23.5);
            addMunicipality("Noormarkku", 61.594, 21.869);
            addMunicipality("Nousiainen", 60.59, 22.08);
            addMunicipality("Nummi-Pusula", 60.397, 23.893);
            addMunicipality("Nurmes", 63.544, 29.145);
            addMunicipality("Nurmijärvi", 60.467, 24.8);
            addMunicipality("Närpiö", 62.474, 21.336);
            addMunicipality("Oravainen", 63.297, 22.357);
            addMunicipality("Orimattila", 60.8, 25.736);
            addMunicipality("Oripää", 60.85, 22.683);
            addMunicipality("Orivesi", 61.677, 24.363);
            addMunicipality("Oulainen", 64.269, 24.818);
            addMunicipality("Oulu", 65.017, 25.467);
            addMunicipality("Oulunsalo", 64.933, 25.417);
            addMunicipality("Outokumpu", 62.727, 29.038);
            addMunicipality("Padasjoki", 61.35, 25.283);
            addMunicipality("Paimio", 60.456, 22.687);
            addMunicipality("Paltamo", 64.417, 27.833);
            addMunicipality("Parainen", 60.302, 22.291);
            addMunicipality("Parikkala", 61.55, 29.5);
            addMunicipality("Parkano", 62.017, 23.017);
            addMunicipality("Pedersören kunta", 63.601, 22.791);
            addMunicipality("Pelkosenniemi", 67.111, 27.511);
            addMunicipality("Pello", 66.796, 24);
            addMunicipality("Perho", 63.217, 24.417);
            addMunicipality("Pernaja", 60.45, 26.05);
            addMunicipality("Perniö", 60.205, 23.134);
            addMunicipality("Pertteli", 60.44, 23.26);
            addMunicipality("Pertunmaa", 61.5, 26.483);
            addMunicipality("Petäjävesi", 62.255, 25.19);
            addMunicipality("Pieksämäki", 62.3, 27.133);
            addMunicipality("Pielavesi", 63.235, 26.758);
            addMunicipality("Pietarsaari", 63.677, 22.706);
            addMunicipality("Pihtipudas", 63.376, 25.575);
            addMunicipality("Piikkiö", 60.423, 22.52);
            addMunicipality("Piippola", 64.178, 25.966);
            addMunicipality("Pirkkala", 61.467, 23.626);
            addMunicipality("Pohja", 60.097, 23.526);
            addMunicipality("Polvijärvi", 62.857, 29.376);
            addMunicipality("Pomarkku", 61.698, 22.003);
            addMunicipality("Pori", 61.483, 21.783);
            addMunicipality("Pornainen", 60.481, 25.363);
            addMunicipality("Porvoo", 60.398, 25.669);
            addMunicipality("Posio", 66.11, 28.179);
            addMunicipality("Pudasjärvi", 65.367, 27.001);
            addMunicipality("Pukkila", 60.65, 25.6);
            addMunicipality("Pulkkila", 64.267, 25.86);
            addMunicipality("Punkaharju", 61.752, 29.393);
            addMunicipality("Punkalaidun", 61.117, 23.1);
            addMunicipality("Puolanka", 64.872, 27.66);
            addMunicipality("Puumala", 61.526, 28.184);
            addMunicipality("Pyhtää", 60.483, 26.533);
            addMunicipality("Pyhäjoki", 64.47, 24.268);
            addMunicipality("Pyhäjärvi", 63.682, 25.97);
            addMunicipality("Pyhäntä", 64.102, 26.356);
            addMunicipality("Pyhäranta", 60.951, 21.447);
            addMunicipality("Pyhäselkä", 62.433, 29.967);
            addMunicipality("Pylkönmäki", 62.667, 24.8);
            addMunicipality("Pälkäne", 61.333, 24.28);
            addMunicipality("Pöytyä", 60.71, 22.61);
            addMunicipality("Raahe", 64.677, 24.459);
            addMunicipality("Raisio", 60.483, 22.183);
            addMunicipality("Rantasalmi", 62.067, 28.3);
            addMunicipality("Rantsila", 64.517, 25.65);
            addMunicipality("Ranua", 65.93, 26.51);
            addMunicipality("Rauma", 61.134, 21.505);
            addMunicipality("Rautalampi", 62.633, 26.833);
            addMunicipality("Rautavaara", 63.483, 28.3);
            addMunicipality("Rautjärvi", 61.431, 29.35);
            addMunicipality("Reisjärvi", 63.61, 24.934);
            addMunicipality("Renko", 60.9, 24.283);
            addMunicipality("Riihimäki", 60.739, 24.774);
            addMunicipality("Ristiina", 61.507, 27.266);
            addMunicipality("Ristijärvi", 64.507, 28.201);
            addMunicipality("Rovaniemi", 66.5, 25.717);
            addMunicipality("Ruokolahti", 61.294, 28.832);
            addMunicipality("Ruotsinpyhtää", 60.533, 26.45);
            addMunicipality("Ruovesi", 61.983, 24.083);
            addMunicipality("Rusko", 60.533, 22.217);
            addMunicipality("Rymättylä", 60.367, 21.95);
            addMunicipality("Rääkkylä", 62.317, 29.617);
            addMunicipality("Saarijärvi", 62.708, 25.247);
            addMunicipality("Salla", 66.833, 28.667);
            addMunicipality("Salo", 60.383, 23.133);
            addMunicipality("Saltvik", 60.283, 20.05);
            addMunicipality("Sammatti", 60.317, 23.817);
            addMunicipality("Sauvo", 60.346, 22.69);
            addMunicipality("Savitaipale", 61.195, 27.684);
            addMunicipality("Savonlinna", 61.867, 28.883);
            addMunicipality("Savonranta", 62.171, 29.215);
            addMunicipality("Savukoski", 67.292, 28.158);
            addMunicipality("Seinäjoki", 62.798, 22.832);
            addMunicipality("Sievi", 63.909, 24.515);
            addMunicipality("Siikainen", 61.867, 21.833);
            addMunicipality("Siikajoki", 64.833, 24.733);
            addMunicipality("Siilinjärvi", 63.074, 27.665);
            addMunicipality("Simo", 65.667, 25.05);
            addMunicipality("Sipoo", 60.367, 25.267);
            addMunicipality("Siuntio", 60.183, 24.2);
            addMunicipality("Sodankylä", 67.417, 26.599);
            addMunicipality("Soini", 62.872, 24.214);
            addMunicipality("Somero", 60.617, 23.533);
            addMunicipality("Sonkajärvi", 63.667, 27.517);
            addMunicipality("Sotkamo", 64.133, 28.417);
            addMunicipality("Sottunga", 60.128, 20.668);
            addMunicipality("Sulkava", 61.79, 28.364);
            addMunicipality("Sund", 60.25, 20.116);
            addMunicipality("Suomenniemi", 61.317, 27.45);
            addMunicipality("Suomusjärvi", 60.385, 23.574);
            addMunicipality("Suomussalmi", 64.883, 28.9);
            addMunicipality("Suonenjoki", 62.631, 27.113);
            addMunicipality("Sysmä", 61.506, 25.673);
            addMunicipality("Säkylä", 61.054, 22.338);
            addMunicipality("Särkisalo", 60.117, 22.95);
            addMunicipality("Taipalsaari", 61.15, 28.05);
            addMunicipality("Taivalkoski", 65.573, 28.233);
            addMunicipality("Taivassalo", 60.563, 21.618);
            addMunicipality("Tammela", 60.809, 23.767);
            addMunicipality("Tammisaari", 59.967, 23.433);
            addMunicipality("Tampere", 61.5, 23.75);
            addMunicipality("Tarvasjoki", 60.583, 22.733);
            addMunicipality("Tervo", 62.95, 26.75);
            addMunicipality("Tervola", 66.084, 24.811);
            addMunicipality("Teuva", 62.483, 21.733);
            addMunicipality("Tohmajärvi", 62.226, 30.333);
            addMunicipality("Toholampi", 63.778, 24.246);
            addMunicipality("Toivakka", 62.1, 26.083);
            addMunicipality("Tornio", 65.85, 24.183);
            addMunicipality("Turku", 60.459, 22.25);
            addMunicipality("Tuulos", 61.12, 24.85);
            addMunicipality("Tuusniemi", 62.817, 28.5);
            addMunicipality("Tuusula", 60.407, 25.036);
            addMunicipality("Tyrnävä", 64.767, 25.65);
            addMunicipality("Töysä", 62.633, 23.817);
            addMunicipality("Ullava", 63.71, 23.9);
            addMunicipality("Ulvila", 61.433, 21.883);
            addMunicipality("Urjala", 61.083, 23.533);
            addMunicipality("Utajärvi", 64.761, 26.413);
            addMunicipality("Utsjoki", 69.91, 27.018);
            addMunicipality("Uurainen", 62.5, 25.45);
            addMunicipality("Uusikaarlepyy", 63.523, 22.528);
            addMunicipality("Uusikaupunki", 60.801, 21.411);
            addMunicipality("Vaala", 64.561, 26.831);
            addMunicipality("Vaasa", 63.1, 21.6);
            addMunicipality("Vahto", 60.613, 22.298);
            addMunicipality("Valkeakoski", 61.267, 24.033);
            addMunicipality("Valkeala", 60.932, 26.808);
            addMunicipality("Valtimo", 63.685, 28.812);
            addMunicipality("Vammala", 61.34, 22.925);
            addMunicipality("Vampula", 61.017, 22.7);
            addMunicipality("Vantaa", 60.285, 25.05);
            addMunicipality("Varkaus", 62.317, 27.917);
            addMunicipality("Varpaisjärvi", 63.367, 27.75);
            addMunicipality("Vehmaa", 60.68, 21.664);
            addMunicipality("Velkua", 60.467, 21.667);
            addMunicipality("Vesanto", 62.933, 26.417);
            addMunicipality("Vesilahti", 61.317, 23.617);
            addMunicipality("Veteli", 63.467, 23.767);
            addMunicipality("Vieremä", 63.747, 27.005);
            addMunicipality("Vihanti", 64.487, 24.98);
            addMunicipality("Vihti", 60.417, 24.333);
            addMunicipality("Viitasaari", 63.086, 25.866);
            addMunicipality("Vilppula", 62.017, 24.517);
            addMunicipality("Vimpeli", 63.16, 23.85);
            addMunicipality("Virolahti", 60.584, 27.706);
            addMunicipality("Virrat", 62.27, 23.7);
            addMunicipality("Vårdö", 60.242, 20.378);
            addMunicipality("Vähäkyrö", 63.067, 22.1);
            addMunicipality("Västanfjärd", 60.05, 22.683);
            addMunicipality("Vöyri-Maksamaa", 63.15, 22.25);
            addMunicipality("Yli-Ii", 65.371, 25.834);
            addMunicipality("Ylikiiminki", 65.033, 26.117);
            addMunicipality("Ylitornio", 66.321, 23.673);
            addMunicipality("Ylivieska", 64.083, 24.55);
            addMunicipality("Ylämaa", 60.8, 28);
            addMunicipality("Yläne", 60.883, 22.417);
            addMunicipality("Ylöjärvi", 61.56, 23.582);
            addMunicipality("Ypäjä", 60.804, 23.28);
            addMunicipality("Äetsä", 61.296, 22.707);
            addMunicipality("Ähtäri", 62.556, 24.096);
            addMunicipality("Äänekoski", 62.6, 25.729);

        }

        return municipalitiesInTestbedMap;

    }


}
