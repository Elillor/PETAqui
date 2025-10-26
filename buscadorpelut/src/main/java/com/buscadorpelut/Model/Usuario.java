package com.buscadorpelut.Model;

import java.io.Serializable;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "usuari")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codiUs")
    private long codiUs;

    @Nonnull
    @Column(name = "nomUs")
    private String nomUs;

    @Nonnull
    @Column(name = "cognom1")
    private String cognom1;

    @Column(name = "cognom2")
    private String cognom2;

    @Nonnull
    @Column(name = "clauPas")
    private String clauPas;

    @Nonnull
    @Column(name = "emailUs", unique = true)
    private String emailUs;

    @Nonnull
    @Enumerated(EnumType.STRING)
    @Column(name = "rolUs")
    private Rol rolUs = Rol.adoptant;

    //Constructors
    public Usuario() {
    }

    public Usuario(Long codiUs, String nomUs, String cognom1, String cognom2, String clauPas, String emailUs, Rol rolUs) {
        this.codiUs = codiUs;
        this.nomUs = nomUs;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.clauPas = clauPas;
        this.emailUs = emailUs;
        this.rolUs = rolUs;
    }

//Getters i setters    
    /*De codiUs només fem get perquè és un valor autoincremental i no es modificarà
    mai des de el codi*/
    public Long getCodiUs() {
        return this.codiUs;
    }

    //Nom usuari
    public String getnomUs() {
        return this.nomUs;
    }

    public void setnomUs(String nom) {
        this.nomUs = nom;
    }

    //Cognoms Usuari
    public String getcognom1() {
        return this.cognom1;
    }

    public void setcognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    public String getcognom2() {
        return this.cognom2;
    }

    public void setcognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    //Clau de pas
    public String getclauPas() {
        return this.clauPas;
    }

    public void setclauPas(String clauPas) {
        this.clauPas = clauPas;
    }

    //Email usuari
    public String getemailUs() {
        return this.emailUs;
    }

    public void setemailUs(String emailUs) {
        this.emailUs = emailUs;
    }

    //Rol
    public Rol getrol() {
        return this.rolUs;
    }

    public void setrol(Rol rolUs) {
        this.rolUs = rolUs;
    }

}
