package database;

import model.RepairWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkerDAO {

    public List<RepairWorker> loadAllWorkers() throws SQLException {
        List<RepairWorker> workers = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "WORKER");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    workers.add(new RepairWorker(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getString("password"),
                            resultSet.getString("trade"),
                            resultSet.getInt("available") == 1));
                }
            }
        }
        return workers;
    }

    public void updateAvailable(String workerId, boolean available) throws SQLException {
        String sql = "UPDATE users SET available = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, available ? 1 : 0);
            statement.setString(2, workerId);
            statement.executeUpdate();
        }
    }
}
