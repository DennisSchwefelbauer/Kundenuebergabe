package UebergabeKunde.database;

import UebergabeKunde.Constructor.Ready;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseReady {

    private static Statement statement = null;
    private PreparedStatement preparedStatement = null;
    static Database db = Database.getInstance();

    private static final DatabaseReady instance = new DatabaseReady();

    private DatabaseReady() {}

    public static DatabaseReady getInstance() {
        try {
            db.connectDB();
            statement = db.getStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    ArrayList<Ready> arrayReady = new ArrayList<>();

    private static final String TABLE_READY = "ready";
    private static final String COLUMN_CUSTOMER = "kunde";

    public void getReady() throws SQLException {
        String query = "SELECT * FROM " + TABLE_READY;
        ResultSet readyRS = statement.executeQuery(query);

        while (readyRS.next()) {
            String name = readyRS.getString(1);
            String vehicle = readyRS.getString(2);
            String info = readyRS.getString(3);
            String damage = String.valueOf(readyRS.getString(4));
            String talked = String.valueOf(readyRS.getInt(5));
            String kv = String.valueOf(readyRS.getInt(6));

            Ready ready = new Ready(name, vehicle, info, damage, talked, kv);

            arrayReady.add(ready);
        }
        statement.close();
        readyRS.close();
    }

    public void setReady(String name, String vehicle, String info, String damage, String talked, String kv) throws SQLException {
        String query = "INSERT INTO " + TABLE_READY + " VALUES(?,?,?,?,?,?)";
        preparedStatement = db.getConnection().prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, vehicle);
        preparedStatement.setString(3, info);
        preparedStatement.setString(4, damage);
        preparedStatement.setString(5, talked);
        preparedStatement.setString(6, kv);
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }

    public void deleteReady(String name) throws SQLException {
        String query = "DELETE FROM " + TABLE_READY + " WHERE " + COLUMN_CUSTOMER + " = ?";
        preparedStatement = db.getConnection().prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }

    public ArrayList<Ready> getArrayReady() {
        return arrayReady;
    }

}
