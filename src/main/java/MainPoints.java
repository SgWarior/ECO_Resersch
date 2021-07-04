import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;


public class MainPoints {
    public static void main(String[] args) throws IOException {
        AtomicReference<Double> total= new AtomicReference<>(0.0);

        File file = new File("K:\\points_project_java\\src\\main\\resources\\EC.txt");
        File resultF = new File("K:\\points_project_java\\src\\main\\resources\\LeaderBord.txt");
        TreeMap<String, Double> nameAndPoints = new TreeMap<>();
        String before = ", by my calculation, ";
        String aftName = " currently has ";
        String aftPoints= " points.";
        try( BufferedReader reader = new BufferedReader(new FileReader(file))
        ) {
            while (reader.ready()){
                String str= reader.readLine();
                if(str.contains(before)&& str.contains(aftName)&&str.contains("points.")){
                    int nameStartFrom = str.lastIndexOf('@');
                    int nameEnd = str.lastIndexOf(aftName);
                    int end =str.lastIndexOf(aftPoints);
                    String key = str.substring(++nameStartFrom, nameEnd);
                    Double value = Double.parseDouble(str.substring(15+nameEnd,end).replaceAll(",",""));
                    nameAndPoints.put(key,value);
                }else if (str.contains(before)&& str.contains(aftName)){
                    int nameStartFrom = str.lastIndexOf('@');
                    int nameEnd = str.lastIndexOf(aftName);
                    String key = str.substring(++nameStartFrom, nameEnd);
                    try{Double value = Math.floor(Double.parseDouble(str.substring(23+nameEnd,str.length()-1).replaceAll(",","")));
                    nameAndPoints.put(key,value);} catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try(BufferedWriter out = new BufferedWriter(new FileWriter(resultF))) {
            StringBuilder sb= new StringBuilder();
            StringBuilder allNames = new StringBuilder().append("!points");
            nameAndPoints.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .filter(stringDoubleEntry -> stringDoubleEntry.getValue()>=0)
                    .forEach(entry -> {
                        sb
                                .append(entry.getKey())
                                .append("\t")
                                .append(entry.getValue())
                                .append("\n");
                        allNames.append(" @").append(entry.getKey());
                    }
                    );
            out.write(sb.toString());
            out.write(allNames.toString());
            nameAndPoints.entrySet().stream()
                    .forEach(stringDoubleEntry -> total.updateAndGet(v -> (double) (v + stringDoubleEntry.getValue())));
        }
        System.out.println(total);
    }

}
