package ro.mpp2024.repository;

import ro.mpp2024.domain.Purchase;

import java.util.List;

public interface RepositoryPurchase extends Repository<Long, Purchase> {
    List<Purchase> findByClientOrderedBySeats(String client);
}
