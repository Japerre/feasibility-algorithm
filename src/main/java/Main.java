import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    static final int tAlfa = 0;

    private static void writeSolutionToJsonFile(int[] solutions, File file) throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(file));
        JSONObject jsonObject = (JSONObject) obj;

        int firstDotIndex = file.getName().indexOf(".");
        String fileWithoutExtension = file.getName().substring(0, firstDotIndex);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < solutions.length; i++) {
            JSONObject sol_i = new JSONObject();
            sol_i.put("ID", i);
            sol_i.put("solution", solutions[i]);
            jsonArray.add(sol_i);
        }
        jsonObject.put("solutions", jsonArray);
        File dir = new File("solutions");
        dir.mkdir();
        FileWriter fileWriter = new FileWriter("solutions/" + fileWithoutExtension + "_sol.json");
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.close();
    }

    private static boolean checkDomain(Graph graph, int t_iID, int[] s, int[] l) {

        int s_i = s[t_iID];

        TimePoint t_i = graph.getTimePoint(t_iID);
        Domain currentDomain = t_i.getCurrentDomain();
        if (!(currentDomain.getL() <= -s_i && currentDomain.getU() >= -s_i)) {
            if (t_i.isLastDomain()) {
                return false; //instance infeasible
            } else {
                while (!t_i.isLastDomain()) {
                    currentDomain = t_i.getNextDomain();
//                    graph.updateGraphArcs(t_iID);
                    if (currentDomain.getL() <= -s_i && currentDomain.getU() >= -s_i) {
//                        s_i = Integer.min(s_i, -currentDomain.getL());
                        // s_i = s_i
                        return true;
                    }
                    else if (-s_i <= currentDomain.getL()){
                        s_i = -currentDomain.getL();
                        s[t_iID] = s_i;
                        l[t_iID] = 1;
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    private static boolean propagate(Graph graph, int t_iID, int[] s, Queue<Integer> queue, int[] l) {

        int s_i = s[t_iID];
        for (Arc a : graph.getOutgoingArcs(t_iID)) { // graph.getOutgoingArcs bug ==> updateArcs toevoegen
            int t_jID = a.getDestination();
            int s_j = s[t_jID];
            if (s_j > s_i + a.getWeight()) {
//                if(t_jID == tAlfa) return false;
                s_j = s_i + a.getWeight();
                s[t_jID] = s_j;
                l[t_jID] = l[t_iID] + 1;
                if (t_jID != tAlfa && l[t_jID] >= graph.getnTimePoints()) {
                    return false; // instance infeasable
                }
                if (t_jID != tAlfa && !queue.contains(t_jID)) {
                    queue.add(t_jID);
                }
            }
        }
        return true;
    }


    public static void main(String[] args) throws IOException, ParseException {

        File file = new File("src/main/resources/feas/inc-1.json");
        Graph graph = new GraphFactory().getGraph(file);

        int nTimePoints = graph.getnTimePoints();
        int[] s = new int[nTimePoints + 1];
        int[] l = new int[nTimePoints + 1];

        s[tAlfa] = 0;
        Queue<Integer> queue = new LinkedList<>();

        //propagate tAlfa by setting all the s[i] to the -lower bound of first domain
        for (TimePoint t : graph.getTimePoints()) {
            s[t.getId()] = -t.getCurrentDomain().getL();
            l[t.getId()] = 1;
            queue.add(t.getId());
        }

        boolean feasible = true;
        while (!queue.isEmpty()) {
            int t_iID = queue.remove();
            if (!checkDomain(graph, t_iID, s, l)) {
                feasible = false;
                break;
            }
            if (!propagate(graph, t_iID, s, queue, l)) {
                feasible = false;
                break;
            }
        }
        if(feasible){
            writeSolutionToJsonFile(s, file);
        }
    }
}
