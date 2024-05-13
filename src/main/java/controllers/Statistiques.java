package controllers;

import entities.Reclamation;
import entities.Reponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.ReclamationServices;
import services.ReponseServices;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class Statistiques {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PieChart idPie;
    @FXML
    private PieChart idPie2;
    @FXML
    private BarChart<String, Float> idBar;
    @FXML
    private Label labelTM;

    ReclamationServices RS = new ReclamationServices();
    ReponseServices rs = new ReponseServices();

    @FXML
    void initialize() {
        assert idBar != null : "fx:id=\"idBar\" was not injected: check your FXML file 'statistiques.fxml'.";
        assert idPie != null : "fx:id=\"idPie\" was not injected: check your FXML file 'statistiques.fxml'.";
        assert idPie2 != null : "fx:id=\"idPie2\" was not injected: check your FXML file 'statistiques.fxml'.";

        assert labelTM != null : "fx:id=\"labelTM\" was not injected: check your FXML file 'statistiques.fxml'.";
        setPieData();
        setBarData();
        setPieData2();

    }

    public void setPieData(){

        List<Reclamation> data = null;
        data = RS.getAllData();
        Map<String, Integer> valueCounts = new HashMap<>();
        for (Reclamation rec : data) {
            valueCounts.put(rec.getType(), valueCounts.getOrDefault(rec.getType(), 0) + 1);
        }

        // Create PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : valueCounts.entrySet()) {
            String label = entry.getKey() + ": " + entry.getValue();
            pieChartData.add(new PieChart.Data(label, entry.getValue()));
        }
        idPie.setData(pieChartData);

    }

    public void setPieData2(){

        List<Reclamation> data = null;
        data = RS.getAllData();
        Map<String, Integer> valueCounts = new HashMap<>();
        for (Reclamation rec : data) {
            valueCounts.put(rec.getStatus(), valueCounts.getOrDefault(rec.getStatus(), 0) + 1);
        }

        // Create PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : valueCounts.entrySet()) {
            String label = entry.getKey() + ": " + entry.getValue();
            pieChartData.add(new PieChart.Data(label, entry.getValue()));
        }
        idPie2.setData(pieChartData);

    }

    public void setBarData(){
        XYChart.Series<String, Float> series = new XYChart.Series<>();
        List<String> types = Arrays.asList("Evenement","Espace","MarketPlace");
        float TM =0;
        List<Reclamation> reclamations =RS.getAllData();
        for (String type : types){
            System.out.println(type);
            int nbr=0;
            float sommeDays =0;
            for (Reclamation rec : reclamations){
                if (rec.getType().equals(type)){
                    List<Reponse> reponses = rs.getAllDataId(rec.getId_reclamation());
                    if(!reponses.isEmpty()){
                        for (Reponse rep : reponses) {
                            System.out.println(rep);
                            LocalDate recDate = rec.getDate_env().toLocalDate();
                            System.out.println(recDate);
                            LocalDate repDate = rep.getDate_rep().toLocalDate();
                            System.out.println(repDate);
                            Period period = Period.between(recDate, repDate);
                            float s = (float) period.getDays();
                            System.out.println(s);
                            sommeDays += s;
                            nbr++;
                        }
                    }
                    else {
                        sommeDays +=0;
                        nbr++;
                    }
                }

            }
            System.out.println("*"+sommeDays/nbr);
            TM += sommeDays/nbr;
            series.getData().add(new XYChart.Data<>(type, sommeDays/nbr));
        }

        idBar.getYAxis().setLabel("jours");
        idBar.getData().add(series);

        labelTM.setText(String.format("%.2f", TM/3)+" jours ");
        labelTM.setPrefWidth(labelTM.getText().length() * 10);

    }


    @FXML
    void back1(ActionEvent event) throws IOException {
        Stage nouveauStage;
        Parent root = FXMLLoader.load(getClass().getResource("/admin.fxml"));
        nouveauStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        nouveauStage.setScene(scene);
    }

}