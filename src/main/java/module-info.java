module com.example.uebergabekunde {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires java.sql;


    opens UebergabeKunde.main to javafx.fxml, java.base;
    opens UebergabeKunde.kasse to javafx.fxml, java.base;
    opens UebergabeKunde.workstation to javafx.fxml, java.base;
    opens UebergabeKunde.finished to javafx.fxml, java.base;
    opens UebergabeKunde.database to java.base;
    opens UebergabeKunde.Constructor to java.base;

    exports UebergabeKunde.main;
    exports UebergabeKunde.database;
    exports UebergabeKunde.kasse;
    exports UebergabeKunde.workstation;
    exports UebergabeKunde.Constructor;
    exports UebergabeKunde.finished;
}