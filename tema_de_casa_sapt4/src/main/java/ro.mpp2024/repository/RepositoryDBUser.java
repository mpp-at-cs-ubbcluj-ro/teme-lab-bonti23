package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBUser implements RepositoryUser {
    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public RepositoryDBUser(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Optional<User> findOne(Long id) {
        logger.traceEntry("Finding user by id {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preStmt = conn.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            preStmt.setLong(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    User user = new User(name, username, password);
                    user.set_identitykey(ID1);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry("Finding all users");
        List<User> users = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Users")) {
            try (ResultSet resultSet = preStmt.executeQuery()) {
                while (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    User user = new User(name, username, password);
                    user.set_identitykey(ID1);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching all users", e);
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        logger.traceEntry("Saving user {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Users (name, username, password) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, entity.getName());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getPassword());
            int result = preStmt.executeUpdate();

            if (result > 0) {
                try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.set_identitykey(generatedKeys.getLong(1));
                    }
                }
                return Optional.of(entity);
            }
        } catch (SQLException ex) {
            logger.error("Error saving user", ex);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(Long id) {
        logger.traceEntry("Deleting user with id {}", id);
        Optional<User> userToDelete = findOne(id);
        if (userToDelete.isEmpty()) {
            return Optional.empty();
        }
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Users WHERE id = ?")) {
            preStmt.setLong(1, id);
            preStmt.executeUpdate();
            return userToDelete;
        } catch (SQLException e) {
            logger.error("Error deleting user", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        logger.traceEntry("Updating user {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Users SET name = ?, username = ?, password = ? WHERE id = ?")) {
            preStmt.setString(1, entity.getName());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getPassword());
            preStmt.setLong(4, entity.get_identitykey());
            int result = preStmt.executeUpdate();

            if (result > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error updating user", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findByUsernameAlphabetically(String username) {
        logger.traceEntry("Finding users by username {} alphabetically", username);
        List<User> users = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Users WHERE username LIKE ? ORDER BY username ASC")) {
            preStmt.setString(1, username + "%");
            try (ResultSet resultSet = preStmt.executeQuery()) {
                while (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String user = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    User foundUser = new User(name, user, password);
                    foundUser.set_identitykey(ID1);
                    users.add(foundUser);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding users by username", e);
        }
        return users;
    }
}
