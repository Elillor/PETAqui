package com.buscadorpelut.Model;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Entidad JPA que representa a un usuario en la base de datos.
 * 
 * Mapea la tabla "usuari" y contiene todos los atributos persistentes del usuario,
 * incluyendo credenciales y rol. Esta clase es parte del modelo de dominio y
 * debe usarse únicamente en la capa de persistencia o servicio.
 */
@Entity
@Table(name = "usuari")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único autogenerado del usuario.
     * Corresponde a la columna "codiUs" en la base de datos.
     */
    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codiUs")
    private long codiUs;

    
    @Nonnull
    @Column(name = "nomUs")
    private String nomUs; //Nombre del usuario. Campo obligatorio.

    @Nonnull
    @Column(name = "cognom1")
    private String cognom1; //Primer apellido del usuario. Campo obligatorio.

    @Column(name = "cognom2")
    private String cognom2; //Segundo apellido del usuario. Campo opcional.

    @Nonnull
    @Column(name = "clauPas")
    private String clauPas; //Contraseña del usuario (encryptada en la base de datos). Campo obligatorio.

    @NotNull (message = "El correo electrónico es obligatorio")
    @Column(name = "emailUs", unique = true)
    private String emailUs; //Correo electrónico del usuario (unico y utilizado para el login). Campo obligatorio.

    /**
     * Rol del usuario que se usa un enum para garantizar valores válidos.
     * Valor por defecto: ADOPTANT.
     * 
     * El enum se almacena como STRING en la base de datos ("adoptant", "admin").
     */
    @Nonnull
    @Enumerated(EnumType.STRING)
    @Column(name = "rolUs")
    private Rol rolUs = Rol.ADOPTANT;

    //Constructors

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Usuario() {
    }

    /**
     * Constructor completo para crear una instancia de Usuario con todos sus atributos.
     * 
     * @param codiUs    ID del usuario.
     * @param nomUs     Nombre del usuario.
     * @param cognom1   Primer apellido.
     * @param cognom2   Segundo apellido (puede ser null).
     * @param clauPas   Contraseña encriptada.
     * @param emailUs   Correo electrónico único.
     * @param rolUs     Rol del usuario.
     */
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
