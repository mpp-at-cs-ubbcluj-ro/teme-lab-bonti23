package ro.mpp2024.repository;

import ro.mpp2024.domain.Game;

import java.util.List;

public interface RepositoryGame extends Repository<Long, Game> {
    List<Game> findByTypeOrderedByDate(String type);
}
