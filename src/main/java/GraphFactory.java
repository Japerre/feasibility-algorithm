import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphFactory {

    private static int nTimePoints;
    private static int nArcs;
    private static List<TimePoint> timePoints;
    private static List<Arc> arcs;

    GraphFactory() {
        timePoints = new ArrayList<>();
        arcs = new ArrayList<>();
    }

    private static void readJson(File file) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object obj = new JSONParser().parse(new FileReader(file));
        JSONObject jsonObject = (JSONObject) obj;

        nTimePoints = (int)(long) jsonObject.get("num-variables");
        nArcs = (int)(long)jsonObject.get("num-constraints");

        JSONArray variables = (JSONArray) jsonObject.get("variables");
        Iterator<JSONObject> itr1 = variables.iterator();
        while (itr1.hasNext()) {
            JSONObject t = itr1.next();
            List<Domain> domainList = new ArrayList<>();
            JSONArray domains = (JSONArray) t.get("domains");
            Iterator<JSONObject> itr2 = domains.iterator();
            while (itr2.hasNext()) {
                JSONObject d = itr2.next();
                domainList.add(new Domain((int)(long) d.get("l"), (int)(long) d.get("u"), (int)(long) d.get("profit")));
            }
            timePoints.add(new TimePoint((int)(long) t.get("id"), (int)(long) t.get("domain-size"), domainList));
        }

        JSONArray constraints = (JSONArray) jsonObject.get("constraints");
        itr1 = constraints.iterator();
        while (itr1.hasNext()) {
            Object o = itr1.next();
            String next = (String) o;
            String[] arcData = next.split(" ");
            arcs.add(new Arc(Integer.parseInt(arcData[0]), Integer.parseInt(arcData[1]), Integer.parseInt(arcData[2])));
        }
    }

    private List<List<Arc>> createAdjList (){

        List<List<Arc>> adjList = new ArrayList<>();
        adjList.add(new ArrayList<>());

        for(int i=1; i<nTimePoints+1; i++){
            ArrayList<Arc> arcsFromI = new ArrayList<>();
            adjList.add(arcsFromI);
            for(Arc a: arcs){
                if(a.getSource() == i){
                    arcsFromI.add(a);
                }
            }
        }
        return adjList;
    }

    public Graph getGraph(File file) throws IOException, ParseException {
        readJson(file);
        return new Graph(nTimePoints, nArcs, timePoints, createAdjList());
    }

}
