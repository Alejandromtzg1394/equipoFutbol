package servicios;

import entidades.Partido;
import entidades.Equipo;
import DAO.PartidoDAO;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class PartidoServiceImpl implements PartidoService {

    private PartidoDAO partidoDAO;
    private EntityManager entityManager;

    public PartidoServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.partidoDAO = new PartidoDAO(entityManager);
    }

    @Override
    public Partido guardarPartido(Partido partido) {
        try {
            entityManager.getTransaction().begin();
            Partido partidoGuardado = partidoDAO.guardar(partido);
            entityManager.getTransaction().commit();
            return partidoGuardado;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar partido: " + e.getMessage(), e);
        }
    }

    @Override
    public Partido buscarPartidoPorId(Long id) {
        return partidoDAO.buscarPorId(id);
    }

    @Override
    public List<Partido> listarTodosPartidos() {
        return partidoDAO.listarTodos();
    }

    @Override
    public void eliminarPartido(Long id) {
        try {
            entityManager.getTransaction().begin();
            partidoDAO.eliminar(id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar partido: " + e.getMessage(), e);
        }
    }

    @Override
    public Partido actualizarPartido(Partido partido) {
        try {
            entityManager.getTransaction().begin();
            Partido partidoActualizado = partidoDAO.actualizar(partido);
            entityManager.getTransaction().commit();
            return partidoActualizado;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar partido: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Partido> buscarPartidosPorEquipo(Equipo equipo) {
        return partidoDAO.buscarPorEquipo(equipo);
    }

    @Override
    public List<Partido> buscarPartidosPorFecha(LocalDate fecha) {
        return partidoDAO.buscarPorFecha(fecha);
    }

    @Override
    public List<Partido> buscarPartidosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return partidoDAO.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    @Override
    public Long contarPartidosJugados() {
        return partidoDAO.contarPartidosJugados();
    }

    @Override
    public List<Partido> buscarPartidosConGoles() {
        return partidoDAO.buscarPartidosConGoles();
    }
}