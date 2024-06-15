import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class AnnealingAlgorithmThread implements Runnable {

    int result = 0;
    ArrayList<Employee> employees = new ArrayList<>();
    ArrayList<CompetenceEmployee> competenceEmployees = new ArrayList<>();
    ArrayList<Event> events = new ArrayList<>();
    ArrayList<CompetenceEvent> competenceEvents = new ArrayList<>();
    Ant best;
    int tmin = 1;
    double tmax = 100000;
    double coolingRate = 0.0003; // охлаждение

    AnnealingAlgorithmThread(ArrayList<Employee> employees, ArrayList<CompetenceEmployee> competenceEmployees, ArrayList<Event> events, ArrayList<CompetenceEvent> competenceEvents){
        this.employees = employees;
        this.competenceEmployees = competenceEmployees;
        this.events = events;
        this.competenceEvents = competenceEvents;
    }

    public void run(){

        long startTime = System.currentTimeMillis();
        int numberCourse = events.size();
        int numberEmployee = employees.size();

        double[][] matrixF = new double[numberEmployee][numberCourse];
        // Матрица соответствия
//        System.out.println("Матрица соответствия");
        for (int i = 0; i < numberEmployee; i++) {
            for (int j = 0; j < numberCourse; j++) {
                matrixF[i][j] = 0;
                for (int k = 0; k < competenceEmployees.size(); k++) {
                    for (int m = 0; m < competenceEvents.size(); m++) {
                        if (employees.get(i).id.equals(competenceEmployees.get(k).employee_id) &&
                                events.get(j).id.equals(competenceEvents.get(m).event_id) &&
                                competenceEvents.get(m).id.equals(competenceEmployees.get(k).id)){ // равны id
                            if (Integer.parseInt(competenceEvents.get(m).level_id) >
                                    Integer.parseInt(competenceEmployees.get(k).level_id)){
                                matrixF[i][j] = 0;
                                break;
                            }
                            else if (Integer.parseInt(competenceEvents.get(m).level_id) <=
                                    Integer.parseInt(competenceEmployees.get(k).level_id) &&
                                    Integer.parseInt(competenceEvents.get(m).result_id) >
                                            Integer.parseInt(competenceEmployees.get(k).level_id)){ // развитие +lvls
                                matrixF[i][j] += (Integer.parseInt(competenceEvents.get(m).result_id) -
                                        Integer.parseInt(competenceEmployees.get(k).level_id));
                            }
                            else if (Integer.parseInt(competenceEvents.get(m).level_id) <=
                                    Integer.parseInt(competenceEmployees.get(k).level_id) &&
                                    Integer.parseInt(competenceEvents.get(m).result_id) ==
                                            Integer.parseInt(competenceEmployees.get(k).level_id)){ // поддержание +0.5
                                matrixF[i][j] += 0.5;
                            }
                        }
                    }
                }
//                System.out.print(matrixF[i][j] + " ");
            }
//            System.out.println();
        }

        double temp = tmax; // tmax


        // Начальное случайное значение
        Ant currentSolution = new Ant();
        for (int i = 0; i < numberEmployee; i++){
            currentSolution.add((int) (Math.random()*numberCourse));
        }

//        System.out.println("Initial solution distance: " + currentSolution.distance);

        // Лучшее решение
        best = new Ant();
        for (int i =0; i<numberEmployee; i++){
            best.add(currentSolution.get(i));
        }

        // Пока позволяет температура выполняем действия
        while (temp > tmin) {
            Ant newSolution = new Ant();
            for (int i =0; i<numberEmployee; i++){
                newSolution.add(currentSolution.get(i));
            }

            int tourPos1 = (int) (newSolution.way.size() * Math.random());
//            int tourPos2 = (int) (newSolution.way.size() * Math.random());

//            int citySwap1 = newSolution.get(tourPos1);
//            int citySwap2 = newSolution.get(tourPos2);
            int citySwap2 = (int) (Math.random() * numberCourse);

            // меняем местами
//            newSolution.set(tourPos2, citySwap1);
            newSolution.set(tourPos1, citySwap2);

            double currentEnergy = 0;
            for (int i = 0; i < numberEmployee; i++){
                int curPoint = currentSolution.get(i);
                currentEnergy += matrixF[i][curPoint];
            }
            currentSolution.distance = currentEnergy;

            double neighbourEnergy = 0;
            for (int i = 0; i < numberEmployee; i++){
                int curPoint = newSolution.get(i);
                neighbourEnergy += matrixF[i][curPoint];
            }
            newSolution.distance = neighbourEnergy;

            // Переход
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Ant();
                for (int i =0; i<numberEmployee; i++){
                    currentSolution.add(newSolution.get(i));
                }
                currentSolution.distance = newSolution.distance;
            }

            double bestEnergy = 0;
            for (int i = 0; i < numberEmployee; i++){
                int curPoint = best.get(i);
                bestEnergy += matrixF[i][curPoint];
            }
            best.distance = bestEnergy;

            // Лучшее решение сохраняем
            if (currentSolution.distance > best.distance) {
                best = new Ant();
                for (int i =0; i<numberEmployee; i++){
                    best.add(currentSolution.get(i));
                }
                best.distance = currentSolution.distance;
            }

            // Охлаждение
            temp *= 1-coolingRate;
        }

//        System.out.println("Final solution distance: " + best.distance);
//        System.out.println("Tour: " + best.way);

        long endTime = System.currentTimeMillis();
        result = (int) (endTime - startTime);
//        System.out.println(best);
        System.out.println(best.distance);
        System.out.println(best.way);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        result = 1;
    }

    // Возможность перехода
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // Если лучше, то вероятность 1
        if (newEnergy > energy) {
            return 1.0;
        }
        // Если хуже, рассчитываем вероятность по формуле
        return Math.exp((newEnergy - energy) / temperature);
    }

    public int getResult(){
        return result;
    }

    public ArrayList<Integer> getWay() { return best.way;}

    public double getDistance() { return best.distance;}

    public Ant getBest() {return best;}
}