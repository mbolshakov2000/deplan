import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

class PredictThread implements Runnable {

    int result = 0;

    public void run(){

        ArrayList<CurrentLevel> currentLevels = new ArrayList<>();
        String competence_id="";
        String level_id="";
        String event_id="";
        String result_id="";
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select cl.competence_id, cl.level_id as \"result_id\", cl.event_id, " +
                    "max(cl2.level_id) keep (dense_rank first order by cl2.cl_date) as \"level_id\" from current_level cl join " +
                    "current_level cl2 on cl.employee_id = cl2.employee_id and cl.competence_id = cl2.competence_id and cl.level_id >= cl2.level_id and " +
                    "cl2.cl_date < cl.cl_date and cl2.event_id = cl.event_id " +
                    "where (cl.is_deleted is null or cl.is_deleted = 0) and (cl2.is_deleted is null or cl2.is_deleted = 0) " +
                    "group by cl.event_id, cl.competence_id, cl.level_id, cl.employee_id, cl.cl_date " +
                    "order by cl.event_id, cl.competence_id, cl.cl_date");
            while (rs.next()) {
                competence_id = rs.getString("competence_id");
                level_id = rs.getString("level_id");
                event_id = rs.getString("event_id");
                result_id = rs.getString("result_id");
                currentLevels.add(new CurrentLevel(competence_id, level_id, event_id, result_id));
            }
        } catch (SQLException ex) {
            result = -1;
        }
        File file = new File("train.csv");
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Competence,Event,Level,Dest,Dest1");
            bw.newLine();
            int cur_oper = -1;
            for (int i = 0; i < currentLevels.size(); i++) {
                //if (Integer.parseInt(currentLevels.get(i).getLevel_id()) < Integer.parseInt(currentLevels.get(i).getResult_id()) && cur_oper != 0 ||
                        //Integer.parseInt(currentLevels.get(i).getLevel_id()) == Integer.parseInt(currentLevels.get(i).getResult_id()) && cur_oper != 1) {
                    bw.write(currentLevels.get(i).getCompetence_id() + "," + currentLevels.get(i).getEvent_id() + "," +
                            currentLevels.get(i).getLevel_id() + "," + currentLevels.get(i).getResult_id() + "," +
                            (Integer.parseInt(currentLevels.get(i).getResult_id()) > Integer.parseInt(currentLevels.get(i).getLevel_id()) ? 1 : 0));
                    bw.newLine();
                    cur_oper = Integer.parseInt(currentLevels.get(i).getLevel_id()) < Integer.parseInt(currentLevels.get(i).getResult_id()) ? 0 : 1;
               // }
            }
            bw.close();
            fw.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String line = "python src/main/resources/predict_result.py";
        //String line = "python predict_result.py";
        CommandLine cmdLine = CommandLine.parse(line);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);
        try {
            int exitCode = executor.execute(cmdLine);
            System.out.println(outputStream.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
            Statement s = c.createStatement();
            s.executeQuery("delete from developed_competence purge");
            BufferedReader reader = new BufferedReader(new FileReader("text.txt"));
            String newLine = reader.readLine();
            while (newLine != null) {
                String[] parts = newLine.split(" ");
                s.executeQuery("INSERT INTO developed_competence VALUES (" + parts[1] + "," + parts[0] + "," + parts[2] + "," + parts[3] + ")");
                newLine = reader.readLine();
            }
            reader.close();
        }
        catch (Exception errrr) {
            errrr.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        result = 1;
    }

    public int getResult(){
        return result;
    }
}