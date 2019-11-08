package com.usco.psp.mario.springboot.app.configuration.properties;

/**
 * Se realizan las importaciones de las librerias necesarias para que las funciones
 * declaradas en el cuerpo de la clase puedan ejecutarse correctamente
 */
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Clase encarga de accesar a las propiedades de almacenamiento, se encuntra anotada como 
 * propiedades de configuracion y lleva el prefijo de la propiedad
 * 
 * @author mario
 *
 */
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
	
	//Atributo en el que se almacenara la propiedad albergada en un archivo de propiedades
	private String location;
	
	//Getter del atributo
	public String getLocation() {
		return location;
	}
	
	//Setter del atributo
	public void setLocation(String location) {
		this.location = location;
	}

}
