package UebergabeKunde.finished;

import UebergabeKunde.Constructor.Finished;
import UebergabeKunde.database.DatabaseFinished;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerFinished implements Initializable {

    DatabaseFinished dbFinished = DatabaseFinished.getInstance();
    ArrayList<Finished> arrayFinished = new ArrayList<>();

    @FXML
    private TableView<Finished> tvFinished;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        arrayFinished.clear();
        tvFinished.getColumns().clear();

        try {
            dbFinished.getFinished();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        TableColumn<Finished, String> columnName = new TableColumn<>("Name");
        columnName.setMinWidth(200);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Finished, String> columnVehicle = new TableColumn<>("Fahrzeug");
        columnVehicle.setMinWidth(200);
        columnVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicle"));

        TableColumn<Finished, String> columnWorker = new TableColumn<>("Abgegeben von");
        columnWorker.setMinWidth(100);
        columnWorker.setCellValueFactory(new PropertyValueFactory<>("worker"));

        TableColumn<Finished, String> columnNotCustomer = new TableColumn<>("Abgeholt von");
        columnNotCustomer.setMinWidth(200);
        columnNotCustomer.setCellValueFactory(new PropertyValueFactory<>("notCustomer"));

        TableColumn<Finished, String> columnDate = new TableColumn<>("Datum");
        columnDate.setMinWidth(150);
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        tvFinished.setItems(getFinished());
        tvFinished.getColumns().addAll(columnName, columnVehicle, columnWorker, columnNotCustomer, columnDate);

        arrayFinished.clear();
    }

    ObservableList<Finished> getFinished() {
        ObservableList<Finished> finished = FXCollections.observableArrayList();
        arrayFinished = dbFinished.getArrayFinished();
        for (int i = 0; i < arrayFinished.size(); i++) {
            String name = arrayFinished.get(i).getName();
            String vehicle = arrayFinished.get(i).getVehicle();
            String worker = arrayFinished.get(i).getWorker();
            String notCustomer = arrayFinished.get(i).getNotCustomer();
            String date = arrayFinished.get(i).getDate();

            finished.add(new Finished(name, vehicle, worker, notCustomer, date));
        }
        return finished;
    }
}
