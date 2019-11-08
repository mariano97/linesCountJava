package com.usco.psp.mario.springboot.app.models.service;
/**
 * Se realizan las importaciones de las librerias necesarias para que las funciones
 * declaradas en el cuerpo de la clase puedan ejecutarse correctamente
 */
import java.io.IOException;

import com.usco.psp.mario.springboot.app.models.entity.LinesCounted;
/**
 * interface que permite gestionar el conteo de linea
 * @author mario
 *
 */
public interface ICountLinesService {
	//metodo definido en otra clase 
	public LinesCounted countLines(String pathFilename) throws IOException;
	//metodo definido en otra clase 
	public void restoreVariables();
	//metodo definido en otra clase 
	public void save(LinesCounted linesCounted);

}
