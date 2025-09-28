package entidades;


import jakarta.persistence.*;

@Entity
@Table(name = "goles")
public class Gol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Gol")
    private Long idGol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Partido", nullable = false)
    private Partido partido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Jugador", nullable = false)
    private Jugador jugador;

    @Column(name = "Minuto", nullable = false)
    private Integer minuto;

    public Gol() {}
    public Gol(Partido partido, Jugador jugador, Integer minuto) {
        this.partido = partido;
        this.jugador = jugador;
        this.minuto = minuto;
    }

    // Getters y Setters
    public Long getIdGol() { return idGol; }
    public void setIdGol(Long idGol) { this.idGol = idGol; }
    public Partido getPartido() { return partido; }
    public void setPartido(Partido partido) { this.partido = partido; }
    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }
    public Integer getMinuto() { return minuto; }
    public void setMinuto(Integer minuto) { this.minuto = minuto; }
}