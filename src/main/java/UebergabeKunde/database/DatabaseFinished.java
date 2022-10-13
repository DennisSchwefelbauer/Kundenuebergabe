package UebergabeKunde.database;

import UebergabeKunde.Constructor.Finished;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseFinished {

    private static final DatabaseFinished instance = new DatabaseFinished();
    private static Statement statement = null;
    static Database db = Database.getInstance();
    private DatabaseFinished() {}
    public static DatabaseFinished getInstance() {
        try {
            db.connectDB();
            statement = db.getStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    ArrayList<Finished> arrayFinished = new ArrayList<>();

    private static final String TABLE_FINISHED = "finished";

    public void getFinished() throws SQLException {
        String query = "SELECT * FROM " + TABLE_FINISHED;
        ResultSet finishedRS = statement.executeQuery(query);

        while (finishedRS.next()) {
            String name = finishedRS.getString(1);
            String vehicle = finishedRS.getString(2);
            String worker = finishedRS.getString(3);
            String notCustomer = finishedRS.getString(4);
            String date = finishedRS.getString(5);

            Finished finished = new Finished(name, vehicle, worker, notCustomer, date);

            arrayFinished.add(finished);
        }

        statement.close();
        finishedRS.close();
    }

    public void setFinished(String name, String vehicle, String worker, String notCustomer) throws SQLException {
        String query = "INSERT INTO " + TABLE_FINISHED + " VALUES(?,?,?,?,DATETIME('now'))";
        PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, vehicle);
        preparedStatement.setString(3, worker);
        preparedStatement.setString(4, notCustomer);
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }



    public ArrayList<Finished> getArrayFinished() {
        return arrayFinished;
    }
}
