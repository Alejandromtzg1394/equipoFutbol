package servicios;

import entidades.Posicion;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PosicionServiceImpl implements PosicionService {

    private EntityManager em;

    //se llama em pero puede ser lo que sea

    public PosicionServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Posicion> listarTodasPosiciones() {
        return em.createQuery("SELECT p FROM Posicion p", Posicion.class).getResultList();
    }
}
