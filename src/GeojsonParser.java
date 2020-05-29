
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

        ArrayList<Coordinate> Polygon = new ArrayList<>();
        ArrayList<ArrayList<Coordinate>> MultiPolygon = new ArrayList<>();

        //si Polygon ([] tab 1 dimension )
        if(geoType.equals("Polygon")){
            coordArray.forEach(pol->parsePolygon((JSONArray)pol,1, Polygon));
        }else{//si MultiPolygon([[]] tab 2 dimensions )
            coordArray.forEach(pol->parseMultiplePolygon((JSONArray)pol,2, Polygon, MultiPolygon));
        }


        System.out.println("("+ ISO_A3 +") "+ ADMIN );
        System.out.println("- " + Polygon.size() + " coordinate");
        //System.out.println("- " + MultiPolygon.size() + " coordinate");

    }

    /**
     * Permet de naviguer jusqu'au coordonées
     * @param pol
     * @param depth
     * @param Polygon
     * @param type 0 = Polygon, 1 = MultiPolygon
     */
    private static void parsePolygon(JSONArray pol,int depth,ArrayList<Coordinate> Polygon) {
        if(depth > 0) {
            pol.forEach(polparse -> parsePolygon((JSONArray) polparse, depth - 1,Polygon));
        }else{
            Polygon.add(
                    new Coordinate(Double.toString((Double)pol.get(0)), Double.toString((Double)pol.get(1)))
            );
        }
    }
    /**
     * Permet de naviguer jusqu'au coordonées
     * @param pol
     * @param depth
     * @param Polygon
     * @param type 0 = Polygon, 1 = MultiPolygon
     */
    private static void parseMultiplePolygon(JSONArray pol,int depth,ArrayList<Coordinate> Polygon,ArrayList<ArrayList<Coordinate>> MultiPolygon) {
        if(depth > 0) {
            pol.forEach(polparse -> parseMultiplePolygon((JSONArray) polparse, depth - 1,Polygon, MultiPolygon));
        }else{
            Polygon.add(
                    new Coordinate(Double.toString((Double)pol.get(0)), Double.toString((Double)pol.get(1)))
            );
        }
        if(depth == 1){
            MultiPolygon.add(Polygon);
            System.out.println("in");
            Polygon.clear();
        }
    }
}


class Coordinate {
    private String x;
    private String y;

    public Coordinate(String x, String y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return x + "," + y + " ";
    }
}