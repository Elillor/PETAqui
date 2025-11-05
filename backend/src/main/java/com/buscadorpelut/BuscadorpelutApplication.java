package com.buscadorpelut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot "BuscadorPelut".
 * 
 * La anotación {@link SpringBootApplication} habilita:
 * - Configuración automática de Spring Boot.
 * - Escaneo de componentes en el paquete actual y subpaquetes.
 * - Soporte para propiedades de configuración.
 */
@SpringBootApplication
public class BuscadorpelutApplication {

	/**
     * Método principal que inicia el contexto de Spring Boot y levanta el servidor embebido
     * (por defecto, Tomcat en el puerto 8080).
     * 
     * @param args Argumentos de línea de comandos pasados al iniciar la aplicación.
     */
	public static void main(String[] args) {
		SpringApplication.run(BuscadorpelutApplication.class, args);
	}

}
