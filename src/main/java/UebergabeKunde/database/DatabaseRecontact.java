package UebergabeKunde.database;

import UebergabeKunde.Constructor.Recontact;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseRecontact {

    private static final DatabaseRecontact instance = new DatabaseRecontact();
    private static Statement statement = null;
    private PreparedStatement preparedStatement = null;
    static Database db = Database.getInstance();

    private DatabaseRecontact() {
    }

    public static DatabaseRecontact getInstance() {
        try {
            db.connectDB();
            statement = db.getStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    ArrayList<Recontact> arrayRecontact = new ArrayList<>();

    private static final String TABLE_RECONTACT = "reContact";
    private static final String COLUMN_CUSTOMER = "kunde";

    public void getRecontact() throws SQLException {
        String query = "SELECT * FROM " + TABLE_RECONTACT;
        ResultSet recontactRS = statement.executeQuery(query);

        while (recontactRS.next()) {
            String name = recontactRS.getString(1);
            String vehicle = recontactRS.getString(2);

            Recontact recontact = new Recontact(name, vehicle);

            arrayRecontact.add(recontact);
        }

        statement.close();
        recontactRS.close();
    }

    public void setRecontact(String name, String vehicle) throws SQLException {
        String query = "INSERT INTO " + TABLE_RECONTACT + " VALUES(?,?)";
        preparedStatement = db.getConnection().prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, vehicle);
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }

    public void deleteRecontact(String name) throws SQLException {
        String query = "DELETE FROM " + TABLE_RECONTACT + " WHERE " + COLUMN_CUSTOMER + " = ?";
        preparedStatement = db.getConnection().prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }

    public ArrayList<Recontact> getArrayRecontact() {
        return arrayRecontact;
    }
}
