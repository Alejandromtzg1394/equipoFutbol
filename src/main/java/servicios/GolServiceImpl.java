package servicios;

import entidades.Gol;
import entidades.Partido;
import entidades.Jugador;
import DAO.GolDAO;
import jakarta.persistence.EntityManager;
import java.util.List;

public class GolServiceImpl implements GolService {

    private GolDAO golDAO;
    private EntityManager entityManager;

    public GolServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.golDAO = new GolDAO(entityManager);
    }

    @Override
    public Gol guardarGol(Gol gol) {
        try {
            entityManager.getTransaction().begin();
            Gol golGuardado = golDAO.guardar(gol);
            entityManager.getTransaction().commit();
            return golGuardado;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar gol: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Gol> listarTodosGoles() {
        return golDAO.listarTodos();
    }

    @Override
    public void eliminarGol(Long id) {
        try {
            entityManager.getTransaction().begin();
            golDAO.eliminar(id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar gol: " + e.getMessage(), e);
        }
    }


}