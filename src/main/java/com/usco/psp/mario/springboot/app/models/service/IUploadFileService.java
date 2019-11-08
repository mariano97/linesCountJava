package com.usco.psp.mario.springboot.app.models.service;
/**
 * Se realizan las importaciones de las librerias necesarias para que las funciones
 * declaradas en el cuerpo de la clase puedan ejecutarse correctamente
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
/**
 * interface que permite gestionar el tratamiento de los archivos
 * @author mario
 *
 */
public interface IUploadFileService {
	//metodo definido en otra clase
	public String copy(MultipartFile multipartFile) throws IOException;
	//metodo definido en otra clase
	public void init() throws IOException;
	//metodo definido en otra clase
	public void deleteAll();
	//metodo definido en otra clase
	public Path loadFile(String filename);
	
}
