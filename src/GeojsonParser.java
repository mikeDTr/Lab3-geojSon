
// sources : https://howtodoinjava.com/json/json-simple-read-write-json-examples/

import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
        import org.json.simple.JSONObject;
        import org.json.simple.parser.JSONParser;
        import org.json.simple.parser.ParseException;
public class GeojsonParser{

    public static void main(String[] args) {

        //JSON parser object pour lire le fichier
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/countriesSimple.geojson")) {


            // lecture du fichier
            Object obj = jsonParser.parse(reader);

            JSONObject FeatureCollection = (JSONObject) obj;
            JSONArray features = (JSONArray) FeatureCollection.get("features");

            /*System.out.println("features");
            System.out.println(features);*/

            // parcours du tableau de features
            features.forEach(feature->parseFeatureObject((JSONObject)feature));


          /*  // lecture du fichier
            Object obj = jsonParser.parse(reader);

            JSONArray personne = (JSONArray) obj;
            System.out.println(personne);

            // parcours du tableau de personnes
            personne.forEach(pers->parsePersonneObject((JSONObject)pers));*/

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * parcour chaque features
     * @param feature
     */
    private static void parseFeatureObject(JSONObject feature) {


        // Obtenir l'objet properties dans la liste
        JSONObject properties = (JSONObject) feature.get("properties");

        String ADMIN    = (String) properties.get("ADMIN");
        String ISO_A3 = (String) properties.get("ISO_A3");


        // Obtenir l'objet geometry dans la liste
        JSONObject geometry = (JSONObject) feature.get("geometry");

        String geoType = (String) geometry.get("type");

        // Obtenir l'objet le tableau de coordonées dans la liste
        JSONArray coordArray = (JSONArray) geometry.get("coordinates");


        //si Polygon ([] tab 1 dimension )
        if(geoType.equals("Polygon")){
            //coordArray.forEach(pol->parsePolygon((JSONArray)pol,1));
        }else{//si MultiPolygon([[]] tab 2 dimensions )
            coordArray.forEach(pol->parsePolygon((JSONArray)pol,2));
        }

        //System.out.println(properties);
        //System.out.println(geoType);

    }

    /**
     * Permet de naviguer jusqu'au coordonées
     * @param pol   tableaux
     * @param depth profondeur des tableaux
     */
    private static void parsePolygon(JSONArray pol,int depth) {
        if(depth > 0) {
            pol.forEach(polparse -> parsePolygon((JSONArray) polparse, depth - 1));
        }else{
            //
            System.out.println(pol.get(0));
            System.out.println(pol.get(1));
        }


    }
}