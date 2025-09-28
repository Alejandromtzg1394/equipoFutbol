package DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class inicializarConexion {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    // Inicializa la conexión
    public static void inicializar() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("TorneoFutbolPU");
            em = emf.createEntityManager();
        }
    }

    // Devuelve el EntityManager
    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            inicializar();
        }
        return em;
    }

    // Cierra la conexión
    public static void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
