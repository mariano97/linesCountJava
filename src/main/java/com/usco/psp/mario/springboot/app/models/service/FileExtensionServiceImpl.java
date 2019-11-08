package com.usco.psp.mario.springboot.app.models.service;

/**
 * Se realizan las importaciones de las librerias necesarias para que las funciones
 * declaradas en el cuerpo de la clase puedan ejecutarse correctamente
 */
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Se crea una clase que implementa la interface 'IFileExtensionService', aqui se verificara 
 * el tipo de extension del archivo, se anota como servicio
 * @author mario
 *
 */
@Service
public class FileExtensionServiceImpl implements IFileExtensionService {
	/**
	 * Artributo que permite la impersion en la consola durante el tiempo de 
	 * ejecucion de la aplicacion
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * Se declara un atributo con el nombre de la extension que debe tener el 
	 * archivo
	 */
	private final String validsExtensions = "java";

	/**
	 * El metodo evaluara si la extension del archivo es valida, obteniendo la extension
	 * y verificando que no se encuentre en blanco y verificando que sea igual a la extension 
	 * requerida
	 * @param file
	 * @return true si y solo si la linea cumple con que la extension obtenida se igual a la 
	 * palabra guradada en el atributo declarado
	 */
	@Override
	public boolean isValidExtension(String file) {
		String fileExtension = getFileExtension(file).toLowerCase();
		log.info("extension: " + fileExtension);
		if(fileExtension == null || fileExtension.isBlank()) {
			return false;
		}
		if(fileExtension.equalsIgnoreCase(validsExtensions)) {
			return true;
		}
		return false;
	}
	
	/**
	 * metodo encargado de obtenr la extension del archivo
	 * @param filename
	 * @return String con la extension del archivo
	 */
	private String getFileExtension(String filename) {
		String extension = FilenameUtils.getExtension(filename);
		return extension;
	}

}
