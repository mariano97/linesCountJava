package com.usco.psp.mario.springboot.app;

/**
 * Se importan las librerias necesarias para que las funciones declaradas
 * en la clase se puedan ejecutar correctamente
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.usco.psp.mario.springboot.app.configuration.properties.LinesProperties;
import com.usco.psp.mario.springboot.app.configuration.properties.StorageProperties;
import com.usco.psp.mario.springboot.app.models.service.IUploadFileService;

/**
 * Se crea una clase que contendra el metodo principal, esta clase se encuentra
 * anotada como una aplicacion SprimgBoot y tambien con la habilitacion de las propiedades
 * de configuracion
 * 
 * @author mario
 */
@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class, LinesProperties.class})
public class PspMarioContadorLineasApplication implements CommandLineRunner{
	
	/**
	 * Metodo anotado de auto llamdo
	 */
	@Autowired
	IUploadFileService uploadFileService;

	/**
	 * metodo principal encargado de ejecutar la aplicacion
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(PspMarioContadorLineasApplication.class, args);
	}

	/**
	 * Metodo en donde crea la carpeta que albergara los archivo y que los borrara
	 */
	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
	}

}
