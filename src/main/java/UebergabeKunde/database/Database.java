package UebergabeKunde.database;

import java.sql.*;

public class Database {
    public static final String DB_NAME = "Datenbank.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    private Connection connection = null;
    private Statement statement = null;

    private static final String TABLE_READY = "ready";
    private static final String COLUMN_CUSTOMER = "kunde";
    private static final String COLUMN_VEHICLE = "fahrzeug";
    private static final String COLUMN_INFO = "info";
    private static final String COLUMN_KV = "kv";
    private static final String COLUMN_DAMAGE = "mangel";
    private static final String COLUMN_TALKED = "abgeklaert";

    private static final String TABLE_RECONTACT = "reContact";

    private static final String TABLE_FINISHED = "finished";
    private static final String COLUMN_NOTCUSTOMER = "abgeholt_von";
    private static final String COLUMN_WHOGAVEIT = "abgegeben_von";
    private static final String COLUMN_DATE = "datum";

    private static final Database instance = new Database();

    private Database() {

    }

    public static Database getInstance() {
        return instance;
    }

    public void connectDB() throws SQLException {
        connection = DriverManager.getConnection(CONNECTION_STRING);
        statement = connection.createStatement();
    }

    public void createDatabase() throws SQLException {
        connectDB();

        statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_READY + "( " +
                COLUMN_CUSTOMER + " TEXT NOT NULL, " +
                COLUMN_VEHICLE + " TEXT NOT NULL, " +
                COLUMN_INFO + " TEXT NOT NULL, " +
                COLUMN_DAMAGE + " INTEGER NOT NULL, " +
                COLUMN_TALKED + " INTEGER NOT NULL, " +
                COLUMN_KV + " INTEGER NOT NULL)");

        statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_RECONTACT + "( " +
                COLUMN_CUSTOMER + " TEXT NOT NULL, " +
                COLUMN_VEHICLE + " TEXT NOT NULL)");

        statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_FINISHED + "( " +
                COLUMN_CUSTOMER + " TEXT NOT NULL, " +
                COLUMN_VEHICLE + " TEXT NOT NULL, " +
                COLUMN_WHOGAVEIT + " TEXT NOT NULL, " +
                COLUMN_NOTCUSTOMER + " TEXT, " +
                COLUMN_DATE + " TEXT NOT NULL)");

        statement.close();
    }

    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }
}
