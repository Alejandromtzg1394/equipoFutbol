package servicios;

import entidades.Gol;
import entidades.Partido;
import entidades.Jugador;
import java.util.List;

public interface GolService {
    Gol guardarGol(Gol gol);
    Gol buscarGolPorId(Long id);
    List<Gol> listarTodosGoles();
    void eliminarGol(Long id);
    Gol actualizarGol(Gol gol);

    List<Gol> buscarGolesPorPartido(Partido partido);
    List<Gol> buscarGolesPorJugador(Jugador jugador);
    Long contarGolesPorJugador(Jugador jugador);
    Long contarGolesPorPartido(Partido partido);
    List<Object[]> obtenerGoleadores();
    List<Gol> buscarGolesPorMinuto(int minuto);
}