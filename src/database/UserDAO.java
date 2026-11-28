package database;

import model.Administrator;
import model.RepairWorker;
import model.Resident;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users(id,name,phone,password,role,trade,available,address) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPhoneNumber());
            statement.setString(4, user.getPassword());

            if (user instanceof RepairWorker) {
                RepairWorker worker = (RepairWorker) user;
                statement.setString(5, "WORKER");
                statement.setString(6, worker.getTrade());
                statement.setInt(7, worker.isAvailable() ? 1 : 0);
                statement.setNull(8, Types.VARCHAR);
            } else if (user instanceof Resident) {
                Resident resident = (Resident) user;
                statement.setString(5, "RESIDENT");
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.INTEGER);
                statement.setString(8, resident.getAddress());
            } else {
                statement.setString(5, "ADMIN");
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.INTEGER);
                statement.setNull(8, Types.VARCHAR);
            }
            statement.executeUpdate();
        }
    }

    public List<User> loadAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                if (role.equals("WORKER")) {
                    users.add(new RepairWorker(id, name, phoneNumber, password,
                            resultSet.getString("trade"),
                            resultSet.getInt("available") == 1));
                } else if (role.equals("RESIDENT")) {
                    users.add(new Resident(id, name, phoneNumber, password,
                            resultSet.getString("address")));
                } else {
                    users.add(new Administrator(id, name, phoneNumber, password));
                }
            }
        }
        return users;
    }
}
