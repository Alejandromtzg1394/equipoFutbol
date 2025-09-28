package entidades;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partidos")
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Partido")
    private Long idPartido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Equipo_Local")
    private Equipo equipoLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Equipo_Visitante")
    private Equipo equipoVisitante;

    @Column(name = "Goles_Local")
    private Integer golesLocal = 0;

    @Column(name = "Goles_Visitante")
    private Integer golesVisitante = 0;

    @Column(name = "Fecha_Partido")
    private LocalDate fechaPartido;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Gol> goles = new ArrayList<>();

    public Partido() {}
    public Partido(Equipo equipoLocal, Equipo equipoVisitante, LocalDate fechaPartido) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fechaPartido = fechaPartido;
    }

    // Getters y Setters
    public Long getIdPartido() { return idPartido; }
    public void setIdPartido(Long idPartido) { this.idPartido = idPartido; }
    public Equipo getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(Equipo equipoLocal) { this.equipoLocal = equipoLocal; }
    public Equipo getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(Equipo equipoVisitante) { this.equipoVisitante = equipoVisitante; }
    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }
    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }
    public LocalDate getFechaPartido() { return fechaPartido; }
    public void setFechaPartido(LocalDate fechaPartido) { this.fechaPartido = fechaPartido; }
    public List<Gol> getGoles() { return goles; }
    public void setGoles(List<Gol> goles) { this.goles = goles; }

    public void agregarGol(Gol gol) {
        goles.add(gol);
        gol.setPartido(this);
    }

    public String getResultado() {
        return golesLocal + " - " + golesVisitante;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaStr = fechaPartido != null ? fechaPartido.format(formatter) : "Sin fecha";
        String local = equipoLocal != null ? equipoLocal.getNombre() : "Sin equipo";
        String visitante = equipoVisitante != null ? equipoVisitante.getNombre() : "Sin equipo";
        return local + " vs " + visitante + " (" + fechaStr + ")";
    }


}