package entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipos")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Equipo")
    private Long idEquipo;

    @Column(name = "Nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Jugador> jugadores = new ArrayList<>();

    @OneToMany(mappedBy = "equipoLocal")
    private List<Partido> partidosLocal = new ArrayList<>();

    @OneToMany(mappedBy = "equipoVisitante")
    private List<Partido> partidosVisitante = new ArrayList<>();

    public Equipo() {}

    public Equipo(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getIdEquipo() { return idEquipo; }
    public void setIdEquipo(Long idEquipo) { this.idEquipo = idEquipo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
        jugador.setEquipo(this);
    }



    @Override
    public String toString() {
        return nombre;
    }

}