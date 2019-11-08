package com.usco.psp.mario.springboot.app.models.service;
/**
 * Interface que permite gestionar los procesos de la extension de archivos 
 * @author mario
 *
 */
public interface IFileExtensionService {
	//metodo definido en otra clase 
	public boolean isValidExtension(String file);
}
