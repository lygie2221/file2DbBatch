package de.lygie.batch.Model;

import javax.persistence.*;

@Entity
@Table(name = "arbeitstabelle")
public class Arbeitstabelle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "status", nullable = false)
    private Short status;

    @Column(name = "verfahren", nullable = false, length = 30)
    private String verfahren;

    @Column(name = "liefernummer", nullable = false, length = 30)
    private String liefernummer;

    @Lob
    @Column(name = "daten", nullable = false)
    private String daten;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getVerfahren() {
        return verfahren;
    }

    public void setVerfahren(String verfahren) {
        this.verfahren = verfahren;
    }

    public String getLiefernummer() {
        return liefernummer;
    }

    public void setLiefernummer(String liefernummer) {
        this.liefernummer = liefernummer;
    }

    public String getDaten() {
        return daten;
    }

    public void setDaten(String daten) {
        this.daten = daten;
    }

}