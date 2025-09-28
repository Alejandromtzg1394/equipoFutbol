package servicios;

import entidades.Posicion;
import java.util.List;

public interface PosicionService {
    void guardarPosicion(Posicion p);
    void eliminarPosicion(Long id);
    Posicion buscarPorId(Long id);
    List<Posicion> listarTodasPosiciones();
}
