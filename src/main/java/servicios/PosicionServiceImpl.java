package servicios;

import entidades.Posicion;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PosicionServiceImpl implements PosicionService {

    private EntityManager em;

    public PosicionServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void guardarPosicion(Posicion p) {
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al guardar posición: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarPosicion(Long id) {
        try {
            em.getTransaction().begin();
            Posicion p = em.find(Posicion.class, id);
            if (p != null) em.remove(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar posición: " + e.getMessage(), e);
        }
    }

    @Override
    public Posicion buscarPorId(Long id) {
        return em.find(Posicion.class, id);
    }

    @Override
    public List<Posicion> listarTodasPosiciones() {
        return em.createQuery("SELECT p FROM Posicion p", Posicion.class).getResultList();
    }
}
