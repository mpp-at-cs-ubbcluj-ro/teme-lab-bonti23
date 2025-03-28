package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositoryDBPurchase implements RepositoryPurchase {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepositoryDBPurchase(JdbcUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Optional<Purchase> findOne(Long id) {
        logger.traceEntry("Finding purchase by id {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preStmt = conn.prepareStatement("SELECT * FROM Purchases WHERE id = ?")) {
            preStmt.setLong(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    String client = resultSet.getString("client");
                    Long game = resultSet.getLong("game");
                    int seats = resultSet.getInt("seats");
                    String address = resultSet.getString("address");

                    Purchase purchase = new Purchase(client, game, seats, address);
                    purchase.set_identitykey(ID1);
                    return Optional.of(purchase);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding purchase by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Purchase> findAll() {
        logger.traceEntry("Finding all purchases");
        List<Purchase> purchases = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Purchases")) {
            try (ResultSet resultSet = preStmt.executeQuery()) {
                while (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    String client = resultSet.getString("client");
                    Long game = resultSet.getLong("game");
                    int seats = resultSet.getInt("seats");
                    String address = resultSet.getString("address");

                    Purchase purchase = new Purchase(client, game, seats, address);
                    purchase.set_identitykey(ID1);
                    purchases.add(purchase);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching all purchases", e);
        }
        return purchases;
    }

    @Override
    public Optional<Purchase> save(Purchase entity) {
        logger.traceEntry("Saving purchase {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Purchases (client, game, seats, address) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, entity.getClient());
            preStmt.setLong(2, entity.getGame());
            preStmt.setInt(3, entity.getSeats());
            preStmt.setString(4, entity.getAddress());
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
            logger.error("Error saving purchase", ex);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Purchase> delete(Long id) {
        logger.traceEntry("Deleting purchase with id {}", id);
        Optional<Purchase> purchaseToDelete = findOne(id);
        if (purchaseToDelete.isEmpty()) {
            return Optional.empty();
        }
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Purchases WHERE id = ?")) {
            preStmt.setLong(1, id);
            preStmt.executeUpdate();
            return purchaseToDelete;
        } catch (SQLException e) {
            logger.error("Error deleting purchase", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Purchase> update(Purchase entity) {
        logger.traceEntry("Updating purchase {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Purchases SET client = ?, game = ?, seats = ?, address = ? WHERE id = ?")) {
            preStmt.setString(1, entity.getClient());
            preStmt.setLong(2, entity.getGame());
            preStmt.setInt(3, entity.getSeats());
            preStmt.setString(4, entity.getAddress());
            preStmt.setLong(5, entity.get_identitykey());
            int result = preStmt.executeUpdate();

            if (result > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Error updating purchase", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Purchase> findByClientOrderedBySeats(String client) {
        logger.traceEntry("Finding purchases by client {} ordered by seats", client);
        List<Purchase> purchases = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Purchases WHERE client = ? ORDER BY seats DESC")) {
            preStmt.setString(1, client);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                while (resultSet.next()) {
                    Long ID1 = resultSet.getLong("id");
                    Long game = resultSet.getLong("game");
                    int seats = resultSet.getInt("seats");
                    String address = resultSet.getString("address");

                    Purchase purchase = new Purchase(client, game, seats, address);
                    purchase.set_identitykey(ID1);
                    purchases.add(purchase);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding purchases by client", e);
        }
        return purchases;
    }
}
