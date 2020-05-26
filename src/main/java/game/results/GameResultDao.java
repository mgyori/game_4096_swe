package game.results;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import java.util.List;

/**
 * DAO class for the {@link GameResult} entity.
 */
public class GameResultDao extends GenericJpaDao<GameResult> {

    public GameResultDao() {
        super(GameResult.class);
    }

    /**
     * Returns the list of {@code n} best results with respect to the time
     * spent for solving the puzzle.
     *
     * @param grid the grid index
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to the time
     * spent for solving the puzzle
     */
    @Transactional
    public List<GameResult> findBest(int grid, int n) {
        return entityManager.createQuery("SELECT r FROM GameResult r WHERE r.grid = :grid ORDER BY r.score DESC, r.duration ASC, r.created DESC", GameResult.class)
                .setParameter("grid", grid)
                .setMaxResults(n)
                .getResultList();
    }

}
