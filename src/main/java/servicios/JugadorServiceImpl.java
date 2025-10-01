package servicios;

import entidades.Jugador;
import entidades.Equipo;
import entidades.Posicion;
import DAO.JugadorDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class JugadorServiceImpl implements JugadorService {

    private JugadorDAO jugadorDAO;
    private EntityManager entityManager;

    public JugadorServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jugadorDAO = new JugadorDAO(entityManager);
    }
    @Override
    public Jugador guardarJugador(Jugador jugador) {
        try {
            entityManager.getTransaction().begin();
            Jugador jugadorGuardado = jugadorDAO.guardar(jugador);
            entityManager.getTransaction().commit();
            return jugadorGuardado;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar jugador: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Jugador> listarTodosJugadores() {
        return jugadorDAO.listarTodos();
    }

    // Revisar Esta eliminarcion
    @Override
    public void eliminarJugador(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            jugadorDAO.eliminar(id);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al eliminar jugador: " + e.getMessage(), e);
        }
    }
}