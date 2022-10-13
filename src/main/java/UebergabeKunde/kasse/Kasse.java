package UebergabeKunde.kasse;

import UebergabeKunde.Constructor.Ready;
import UebergabeKunde.database.DatabaseFinished;
import UebergabeKunde.database.DatabaseReady;
import UebergabeKunde.database.DatabaseRecontact;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Kasse implements Initializable {

    @FXML
    private Button bttnExit;

    @FXML
    private Button bttnFinished;

    @FXML
    private CheckBox cbNotCustomer;

    @FXML
    private CheckBox cbRecontact;

    @FXML
    private ChoiceBox<String> cbUser;

    @FXML
    private TextField inputCustomer;

    @FXML
    private Label labelKV;

    @FXML
    private Label labelNOK;

    @FXML
    private Label labelName;

    @FXML
    private Label labelOK;

    @FXML
    private Label labelTalked;

    @FXML
    private TextArea tfNotes;

    @FXML
    private TableView<Ready> tvReady;

    String name;
    String vehicle;
    int listener;

    ArrayList<Ready> arrayReady = new ArrayList<>();
    DatabaseReady dbReady = DatabaseReady.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reset();

        cbUser.getItems().addAll("Katja", "Andreas", "Markus", "Rico", "Bettina", "Jürgen", "Dennis", "Fzg wurde mit Zweitschlüssel geholt");
        cbNotCustomer.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    inputCustomer.setDisable(false);
                } else {
                    inputCustomer.setDisable(true);
                }
            }
        });
    }


    @FXML
    void txtfieldInput_Clicked(MouseEvent event) {
        try {
            Runtime.getRuntime().exec("cmd /c osk");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void reset() {
        labelOK.setText("");
        labelNOK.setText("");
        labelName.setText("");
        labelTalked.setText("");
        labelKV.setText("");
        cbUser.setValue("");
        inputCustomer.setDisable(true);
        tfNotes.setText("");
        cbRecontact.setSelected(false);
        cbNotCustomer.setSelected(false);
        inputCustomer.setText("");
        bttnFinished.setDisable(true);
        listener = 0;

        refreshReady();
    }

    private void refreshReady() {
        tvReady.getColumns().clear();
        arrayReady.clear();

        try {
            dbReady.getReady();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TableColumn<Ready, String> columnName = new TableColumn<>("Name");
        columnName.setMinWidth(300);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Ready, String> columnVehicle = new TableColumn<>("Fahrzeug");
        columnVehicle.setMinWidth(300);
        columnVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicle"));

        tvReady.setItems(getReady());
        tvReady.getColumns().addAll(columnName, columnVehicle);

        tvReady.setRowFactory(tv -> {
            TableRow<Ready> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 1) {
                        if (!(row.getItem() == null)) {
                            reset();
                            listener = 1;
                            labelName.setText(row.getItem().getName());
                            tfNotes.setText(row.getItem().getInfo());

                            name = row.getItem().getName();
                            vehicle = row.getItem().getVehicle();

                            if (Integer.parseInt(row.getItem().getTalked()) == 1) {
                                labelTalked.setText("Kunde wurde alles bereits erklärt");
                            } else {
                                labelTalked.setText("");
                            }
                            if (Integer.parseInt(row.getItem().getDamage()) == 0) {
                                labelOK.setText("Alles in Ordnung");
                            } else if (Integer.parseInt(row.getItem().getDamage()) == 1) {
                                labelNOK.setText("Mangel vorhanden");
                                if (Integer.parseInt(row.getItem().getKv()) == 1) {
                                    labelKV.setText("KV Liegt bei");
                                } else {
                                    labelKV.setText("KV wird nachgereicht");
                                }
                            }

                            cbUser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                                @Override
                                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                                    if (listener == 1) {
                                        if (!t1.isBlank()) {
                                            bttnFinished.setDisable(false);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
            return row;
        });
    }

    @FXML
    void bttnFinished_Clicked(ActionEvent event) {
        DatabaseFinished dbFinished = DatabaseFinished.getInstance();
        String notCustomer;
        if (cbNotCustomer.isSelected()) {
            notCustomer = inputCustomer.getText();
        } else {
            notCustomer = "Wurde von Kunde geholt";
        }
        try {
            dbFinished.setFinished(name, vehicle, cbUser.getValue(), notCustomer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (cbRecontact.isSelected()) {
            DatabaseRecontact recontactDB = DatabaseRecontact.getInstance();
            try {
                recontactDB.setRecontact(name, vehicle);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            dbReady.deleteReady(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reset();
    }

    @FXML
    void bttnExit_Clicked(ActionEvent event) {
        bttnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 3) {
                    Platform.exit();
                }
            }
        });
    }

    @FXML
    void bttnRefresh_Clicked(ActionEvent event) {
        reset();
        refreshReady();
    }

    private ObservableList<Ready> getReady() {
        ObservableList<Ready> ready = FXCollections.observableArrayList();
        arrayReady = dbReady.getArrayReady();
        for (int i = 0; i < arrayReady.size(); i++) {
            String name = arrayReady.get(i).getName();
            String vehicle = arrayReady.get(i).getVehicle();
            String info = arrayReady.get(i).getInfo();
            String damage = arrayReady.get(i).getDamage();
            String talked = arrayReady.get(i).getTalked();
            String kv = arrayReady.get(i).getKv();

            ready.add(new Ready(name, vehicle, info, damage, talked, kv));

        }
        return ready;
    }
}
