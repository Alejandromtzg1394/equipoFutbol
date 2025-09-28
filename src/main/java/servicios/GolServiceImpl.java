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
    public Gol buscarGolPorId(Long id) {
        return golDAO.buscarPorId(id);
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

    @Override
    public Gol actualizarGol(Gol gol) {
        try {
            entityManager.getTransaction().begin();
            Gol golActualizado = golDAO.actualizar(gol);
            entityManager.getTransaction().commit();
            return golActualizado;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar gol: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Gol> buscarGolesPorPartido(Partido partido) {
        return golDAO.buscarPorPartido(partido);
    }

    @Override
    public List<Gol> buscarGolesPorJugador(Jugador jugador) {
        return golDAO.buscarPorJugador(jugador);
    }

    @Override
    public Long contarGolesPorJugador(Jugador jugador) {
        return golDAO.contarGolesPorJugador(jugador);
    }

    @Override
    public Long contarGolesPorPartido(Partido partido) {
        return golDAO.contarGolesPorPartido(partido);
    }

    @Override
    public List<Object[]> obtenerGoleadores() {
        return golDAO.obtenerGoleadores();
    }

    @Override
    public List<Gol> buscarGolesPorMinuto(int minuto) {
        return golDAO.buscarPorMinuto(minuto);
    }
}