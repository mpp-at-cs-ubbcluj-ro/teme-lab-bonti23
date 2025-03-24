package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositoryDBGame implements RepositoryGame {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepositoryDBGame(JdbcUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Optional<Game> findOne(Long id) {
        logger.traceEntry("Finding game by id {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preStmt = conn.prepareStatement("SELECT * FROM Games WHERE id = ?")) {
            preStmt.setLong(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    String teamA = resultSet.getString("teamA");
                    String teamB = resultSet.getString("teamB");
                    String date = resultSet.getString("date");
                    float price = resultSet.getFloat("price");
                    Type type = Type.valueOf(resultSet.getString("type"));
                    int seats = resultSet.getInt("seats");

                    Game game = new Game(teamA, teamB, date, price, type, seats);
                    game.set_identitykey(ID1);
                    return Optional.of(game);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding game by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Game> findAll() {
        logger.traceEntry("Finding all games");
        List<Game> games = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Games")) {
            try (ResultSet resultSet = preStmt.executeQuery()) {
                while (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    String teamA = resultSet.getString("teamA");
                    String teamB = resultSet.getString("teamB");
                    String date = resultSet.getString("date");
                    float price = resultSet.getFloat("price");
                    Type type = Type.valueOf(resultSet.getString("type"));
                    int seats = resultSet.getInt("seats");

                    Game game = new Game(teamA, teamB, date, price, type, seats);
                    game.set_identitykey(ID1);
                    games.add(game);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching all games", e);
        }
        return games;
    }

    @Override
    public Optional<Game> save(Game entity) {
        logger.traceEntry("Saving game {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Games (teamA, teamB, date, price, type, seats) VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, entity.getTeamA());
            preStmt.setString(2, entity.getTeamB());
            preStmt.setString(3, entity.getDate());
            preStmt.setFloat(4, entity.getPrice());
            preStmt.setString(5, entity.getType().name());
            preStmt.setInt(6, entity.getSeats());
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
            logger.error("Error saving game", ex);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Game> delete(Long id) {
        logger.traceEntry("Deleting game with id {}", id);
        Optional<Game> gameToDelete = findOne(id);
        if (gameToDelete.isEmpty()) {
            return Optional.empty();
        }
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Games WHERE id = ?")) {
            preStmt.setLong(1, id);
            preStmt.executeUpdate();
            return gameToDelete;
        } catch (SQLException e) {
            logger.error("Error deleting game", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Game> update(Game entity) {
        logger.traceEntry("Updating game {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Games SET teamA = ?, teamB = ?, date = ?, price = ?, type = ?, seats = ? WHERE id = ?")) {
            preStmt.setString(1, entity.getTeamA());
            preStmt.setString(2, entity.getTeamB());
            preStmt.setString(3, entity.getDate());
            preStmt.setFloat(4, entity.getPrice());
            preStmt.setString(5, entity.getType().name());
            preStmt.setInt(6, entity.getSeats());
            preStmt.setLong(7, entity.get_identitykey());
            int result = preStmt.executeUpdate();

            if (result > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error updating game", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Game> findByTypeOrderedByDate(String type) {
        logger.traceEntry("Finding games by type {} ordered by date", type);
        List<Game> games = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Games WHERE type = ? ORDER BY date ASC")) {
            preStmt.setString(1, type);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                while (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    String teamA = resultSet.getString("teamA");
                    String teamB = resultSet.getString("teamB");
                    String date = resultSet.getString("date");
                    float price = resultSet.getFloat("price");
                    Type gameType = Type.valueOf(resultSet.getString("type"));
                    int seats = resultSet.getInt("seats");

                    Game game = new Game(teamA, teamB, date, price, gameType, seats);
                    game.set_identitykey(ID1);
                    games.add(game);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding games by type", e);
        }
        return games;
    }
}
