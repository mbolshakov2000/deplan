import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class AntAlgorithmThread implements Runnable {

    int result = 0;
    ArrayList<Teacher> teachers = new ArrayList<>();
    ArrayList<CompetenceTeacher> competenceTeachers = new ArrayList<>();
    ArrayList<Event> events = new ArrayList<>();
    ArrayList<CompetenceEvent> competenceEvents = new ArrayList<>();
    Ant[] ant;
    int bestAnt;
    int numberAnt;
    double q = 0.8;
    double p = 0.2;
    double Part = 0.95;

    AntAlgorithmThread(ArrayList<Teacher> teachers, ArrayList<CompetenceTeacher> competenceTeachers, ArrayList<Event> events, ArrayList<CompetenceEvent> competenceEvents){
        this.teachers = teachers;
        this.competenceTeachers = competenceTeachers;
        this.events = events;
        this.competenceEvents = competenceEvents;
    }

    public void run(){

        if (numberAnt == 0)
            numberAnt = teachers.size()*events.size();
        long startTime = System.currentTimeMillis();
        ant = new Ant[numberAnt];
        int i = 0;
        int j;
        int numberCourse = events.size();
        int numberTeacher = teachers.size();

        double bestSolution = 0;
        bestAnt = 0;
        double[][] matrixF = new double[numberTeacher][numberCourse];
        double[][] matrixP = new double[numberTeacher][numberCourse];
        // Матрица соответствия
//        System.out.println("Матрица соответствия");
        for (i = 0; i < numberTeacher; i++) {
            for (j = 0; j < numberCourse; j++) {
                matrixF[i][j] = 0;
                for (int k = 0; k < competenceTeachers.size(); k++) {
                    for (int m = 0; m < competenceEvents.size(); m++) {
                        if (teachers.get(i).id.equals(competenceTeachers.get(k).employee_id) &&
                                events.get(j).id.equals(competenceEvents.get(m).event_id) &&
                                competenceEvents.get(m).id.equals(competenceTeachers.get(k).id)){ // равны id
                            if (Integer.parseInt(competenceEvents.get(m).level_id) >
                                    Integer.parseInt(competenceTeachers.get(k).level_id)){
                                matrixF[i][j] = 0;
                                break;
                            }
                            else if (Integer.parseInt(competenceEvents.get(m).level_id) <=
                                        Integer.parseInt(competenceTeachers.get(k).level_id) &&
                                Integer.parseInt(competenceEvents.get(m).result_id) >
                                        Integer.parseInt(competenceTeachers.get(k).level_id)){ // развитие +lvls
                                matrixF[i][j] += (Integer.parseInt(competenceEvents.get(m).result_id) -
                                        Integer.parseInt(competenceTeachers.get(k).level_id));
                            }
                            else if (Integer.parseInt(competenceEvents.get(m).level_id) <=
                                    Integer.parseInt(competenceTeachers.get(k).level_id) &&
                                    Integer.parseInt(competenceEvents.get(m).result_id) ==
                                            Integer.parseInt(competenceTeachers.get(k).level_id)){ // поддержание +0.5
                                    matrixF[i][j] += 0.5;
                            }
                        }
                    }
                }
//                System.out.print(matrixF[i][j] + " ");
            }
//            System.out.println();
        }
         //Матрица феромона
        Random random = new Random();
        for (i = 0; i < numberTeacher; i++) {
            for (j = 0; j < numberCourse; j++) {
                matrixP[i][j] = 0.5;
            }
        }
        //Инициализация
        for (i = 0; i < numberAnt; i++) {
            ant[i] = new Ant();
        }

        for (j = 0; j < numberAnt; j++) {
            int[] quantity = new int[events.size()];
            for (int s = 0; s < events.size(); s++){
                quantity[s] = Integer.parseInt(events.get(s).quantity);
            }
            ant[j].add(j % numberCourse);
            ant[j].distance = ant[j].distance + matrixF[0][j % numberCourse];
            quantity[j % numberCourse]--;
            for (int k = 1; k < numberTeacher; k++) {
                double sum = 0;
                int currentTeacher = k;
                double F;
                // Сумма для преподавателей без курса
                for (i = 0; i < numberCourse; i++) {
                    F = matrixF[currentTeacher][i];
                    sum += (Math.pow(F, q) * Math.pow(matrixP[currentTeacher][i], p));
                }
                double P = 0;
                double probility = random.nextDouble();
                boolean fl = false;
                // Подбор курсу преподавателя
                for (i = 0; i < numberCourse; i++) {
                    F = matrixF[currentTeacher][i];
                    P += (Math.pow(F, q) * Math.pow(matrixP[currentTeacher][i], p)) / sum;
                    // Условие оптимальности пути
                    if (probility < P && quantity[i]>0) {
                        ant[j].distance = ant[j].distance + matrixF[currentTeacher][i];
                        ant[j].add(i);
                        quantity[i]--;
                        fl = true;
                        matrixP[currentTeacher][i] = matrixP[currentTeacher][i] + 1 / matrixF[currentTeacher][i];
                        break;
                    }
                }
                if (!fl) k--;
                // Испарение феромона
                matrixP = evaporation(numberCourse, numberTeacher, Part, matrixP);
            }
            // Проверяем путь муравья на лучшее решение
            if (bestSolution < ant[j].distance) {
                bestSolution = ant[j].distance;
                bestAnt = j;
//                System.out.println(Arrays.toString(quantity));
            }
        }
        long endTime = System.currentTimeMillis();
//        System.out.println("opt " + (endTime - startTime) + "ms");
        result = (int) (endTime - startTime);

//        System.out.println(bestAnt);
        System.out.println(ant[bestAnt].distance);
//        System.out.println(ant[bestAnt].way);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        result = 1;
    }

    public double[][] evaporation ( int numberCourse, int numberTeacher, double Part, double[][] matrixF){
        int i;
        for (i = 0; i < numberTeacher; i++) {
            for (int m = 0; m < numberCourse; m++) {
                matrixF[i][m] = matrixF[i][m] * Part;
                if (matrixF[i][m] < 0.5) matrixF[i][m] = 0.5;
            }
        }
        return matrixF;
    }

    public int getResult(){
        return result;
    }

    public ArrayList<Integer> getWay() { return ant[bestAnt].way;}

    public double getDistance() { return ant[bestAnt].distance;}

    public Ant getBest() {return ant[bestAnt];}
}