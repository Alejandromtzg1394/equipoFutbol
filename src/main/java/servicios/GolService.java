package servicios;

import entidades.Gol;
import entidades.Partido;
import entidades.Jugador;
import java.util.List;

public interface GolService {
    Gol guardarGol(Gol gol);
    List<Gol> listarTodosGoles();
    void eliminarGol(Long id);

}