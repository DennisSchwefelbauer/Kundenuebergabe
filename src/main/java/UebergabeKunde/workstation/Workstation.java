package UebergabeKunde.workstation;

import UebergabeKunde.Constructor.Ready;
import UebergabeKunde.Constructor.Recontact;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Workstation implements Initializable {
    @FXML
    private Button bttnFinished;
    @FXML
    private Button bttnDelete;

    @FXML
    private Button bttnSave;

    @FXML
    private CheckBox cbDamage;

    @FXML
    private CheckBox cbKV;

    @FXML
    private CheckBox cbNoDamage;

    @FXML
    private CheckBox cbNoKV;

    @FXML
    private CheckBox cbTalked;

    @FXML
    private TextArea inputInfo;

    @FXML
    private TextField inputName;

    @FXML
    private TextField inputVehicle;

    @FXML
    private TableView<Recontact> tvCallback;

    @FXML
    private TableView<Ready> tvReady;

    int damage = 0;
    int talked = 0;
    int kv = 0;
    String nameReady;
    String nameRecontact;
    String nameFinished;

    ArrayList<Ready> arrayReady = new ArrayList<>();
    ArrayList<Recontact> arrayRecontact = new ArrayList<>();

    DatabaseReady dbReady = DatabaseReady.getInstance();
    DatabaseRecontact dbRecontact = DatabaseRecontact.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reset();

        cbNoDamage.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    cbDamage.setDisable(true);
                    cbDamage.setSelected(false);
                    bttnSave.setDisable(false);
                    damage = 0;
                } else {
                    cbDamage.setDisable(false);
                    bttnSave.setDisable(true);
                    damage = 1;
                }
            }
        });
        cbDamage.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    cbNoDamage.setDisable(true);
                    cbKV.setDisable(false);
                    cbNoKV.setDisable(false);

                    cbKV.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                            if (t1) {
                                cbNoKV.setDisable(true);
                                bttnSave.setDisable(false);
                                damage = 1;
                                kv = 1;

                            } else {
                                cbNoKV.setDisable(false);
                                bttnSave.setDisable(true);
                                damage = 0;
                                kv = 0;
                            }
                        }
                    });
                    cbNoKV.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                            if (t1) {
                                cbKV.setDisable(true);
                                bttnSave.setDisable(false);
                                damage = 1;
                                kv = 0;

                            } else {
                                cbKV.setDisable(false);
                                bttnSave.setDisable(true);
                                damage = 0;
                            }
                        }
                    });

                } else {
                    cbNoDamage.setDisable(false);
                    cbKV.setDisable(true);
                    cbKV.setSelected(false);
                    cbNoKV.setDisable(true);
                    cbNoKV.setSelected(false);
                }
            }
        });

        cbTalked.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    talked = 1;
                } else {
                    talked = 0;
                }
            }
        });
    }

    @FXML
    void bttnDelete_Clicked(ActionEvent event) {
        try {
            dbReady.deleteReady(nameReady);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bttnDelete.setDisable(true);
        refreshReady();
    }

    @FXML
    void bttnExit_Clicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void bttnFinished_Clicked(ActionEvent event) {
        try {
            dbRecontact.deleteRecontact(nameFinished);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reset();
    }

    @FXML
    void bttnReset_Clicked(ActionEvent event) {
        reset();
    }

    @FXML
    void bttnSave_Clicked(ActionEvent event) {
        if (!inputName.getText().isEmpty() || !inputVehicle.getText().isEmpty() || !inputInfo.getText().isEmpty()) {
            try {
                dbReady.setReady(
                        inputName.getText(),
                        inputVehicle.getText(),
                        inputInfo.getText(),
                        String.valueOf(damage),
                        String.valueOf(talked),
                        String.valueOf(kv));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            reset();

        }
    }

    @FXML
    void bttnShowFinished_Clicked(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage = new Stage();
        fxmlLoader.setLocation(getClass().getResource("/finished/finished.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Abgeholte Fahrzeuge");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private void reset() {
        bttnFinished.setDisable(true);
        bttnDelete.setDisable(true);
        bttnSave.setDisable(true);
        cbKV.setDisable(true);
        cbNoKV.setDisable(true);
        inputName.clear();
        inputVehicle.clear();
        inputInfo.clear();
        cbTalked.setSelected(false);
        cbNoDamage.setSelected(false);
        cbDamage.setSelected(false);
        cbKV.setSelected(false);
        cbNoKV.setSelected(false);
        arrayRecontact.clear();
        arrayReady.clear();

        refreshRecontact();
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
        columnName.setMinWidth(275);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Ready, String> columnVehicle = new TableColumn<>("Fahrzeug");
        columnVehicle.setMinWidth(275);
        columnVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicle"));

        tvReady.setItems(getReady());
        tvReady.getColumns().addAll(columnName, columnVehicle);

        tvReady.setRowFactory(tv -> {
            TableRow<Ready> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        bttnDelete.setDisable(false);
                        nameReady = row.getItem().getName();
                    }
                }
            });
            return row;
        });
    }

    private void refreshRecontact() {
        tvCallback.getColumns().clear();
        arrayRecontact.clear();

        try {
            dbRecontact.getRecontact();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TableColumn<Recontact, String> columnName = new TableColumn<>("Name");
        columnName.setMinWidth(275);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Recontact, String> columnVehicle = new TableColumn<>("Fahrzeug");
        columnVehicle.setMinWidth(275);
        columnVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicle"));

        tvCallback.setItems(getRecontact());
        tvCallback.getColumns().addAll(columnName, columnVehicle);

        tvCallback.setRowFactory(tv -> {
            TableRow<Recontact> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        bttnFinished.setDisable(false);
                        nameFinished = row.getItem().getName();
                    }
                }
            });
            return row;
        });
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

    private ObservableList<Recontact> getRecontact() {
        ObservableList<Recontact> recontact = FXCollections.observableArrayList();
        arrayRecontact = dbRecontact.getArrayRecontact();
        for (int i = 0; i < arrayRecontact.size(); i++) {
            String name = arrayRecontact.get(i).getName();
            String vehicle = arrayRecontact.get(i).getVehicle();

            recontact.add(new Recontact(name, vehicle));
        }
        return recontact;
    }

}
