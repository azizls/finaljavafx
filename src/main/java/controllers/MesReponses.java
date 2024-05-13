package controllers;

import entities.Reclamation;
import entities.Reponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import services.ReclamationServices;
import utils.MyConnection;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MesReponses {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Reponse> myList;
    @FXML
    private Button cloturerBtn;

    @FXML
    private Label labelTitre;

    @FXML
    private Label idLabel;

    ReclamationServices RS = new ReclamationServices();
    public ObservableList<Reponse> getAllData(int id_reclamation) {
        ObservableList<Reponse> reponses = FXCollections.observableArrayList();
        String requete = "SELECT * FROM reponse WHERE id_reclamation=" + id_reclamation;
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Reponse reponse = new Reponse(rs.getInt("id_reponse"),
                        rs.getInt("id_reclamation"),
                        rs.getString("contenu"));
                reponses.add(reponse);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reponses;
    }

    public void setReclamation(Reclamation R){

        idLabel.setText(String.valueOf(R.getId_reclamation()));
        idLabel.setText(String.valueOf(R.getId_reclamation()));
    }

    @FXML
    void onClickCloturer(ActionEvent event) {
        try {
            ObservableList<Reponse> reponses = myList.getItems();
            Reponse rep = reponses.get(0);
            Reclamation Rec = RS.getReclamationById(rep.getId_reclamation());
            RS.updateStatus(Rec,"Cloturée");
            // Assuming you have a reference to the current stage
            Stage stage = (Stage) cloturerBtn.getScene().getWindow();
// Closing the current stage
            stage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Reclamation cloturée avec succés");
            alert.show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Reponse R = this.myList.getSelectionModel().getSelectedItem();



    }

    @FXML
    void initialize() {
        assert cloturerBtn != null : "fx:id=\"cloturerBtn\" was not injected: check your FXML file 'mesReponses.fxml'.";
        assert labelTitre != null : "fx:id=\"labelTitre\" was not injected: check your FXML file 'mesReponses.fxml'.";
        assert myList != null : "fx:id=\"myList\" was not injected: check your FXML file 'mesReponses.fxml'.";

    }

    public void setReponses(int id_reclamation) {
        myList.setItems(getAllData(id_reclamation));
        myList.setCellFactory(param -> new ListCell<Reponse>() {


            private final HBox hbox = new HBox();

            {
                hbox.setSpacing(10); // Set horizontal spacing between attributes
            }


            @Override
            protected void updateItem(Reponse rep, boolean empty) {
                super.updateItem(rep, empty);


                if (rep == null || empty) {
                    setText(null);
                    setGraphic(null);

                } else {
                    Label t = new Label(String.valueOf(rep.getId_reclamation()));
                    TextArea c = new TextArea(rep.getContenu());

                    c.setFocusTraversable(false);
                    c.setWrapText(true);
                    c.setEditable(false);
                    t.setStyle("-fx-padding: 10px;");
                    c.setStyle("-fx-padding: 10px;");

                    hbox.getChildren().clear(); // Clear existing content

                    // Add type label
                    hbox.getChildren().add(t);

                    // Add region for flexible spacing
                    Region region = new Region();
                    HBox.setHgrow(region, Priority.ALWAYS); // Allow region to grow horizontally
                    hbox.getChildren().add(region);

                    // Add contenu text area
                    hbox.getChildren().add(c);
                    setGraphic(hbox);


                }


            }

        });

        myList.setFixedCellSize(150);
}









}
