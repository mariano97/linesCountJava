package com.usco.psp.mario.springboot.app.models.service;

/**
 * Se realizan las importaciones de las librerias necesarias para que las funciones
 * declaradas en el cuerpo de la clase puedan ejecutarse correctamente
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.usco.psp.mario.springboot.app.configuration.properties.StorageProperties;

/**
 * clase que implemnta 'IUploadFileService', aqui se declara el comportamiento de los metodos
 * encargados de tratar con el archivo
 * @author mario
 *
 */
@Service
public class UploadFileServiceImpl implements IUploadFileService{
	
	/**
	 * Artributo que permite la impersion en la consola durante el tiempo de 
	 * ejecucion de la aplicacion
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * Se declara una constante que sera el nombre de la carpeta donde se almacenara los archivos
	 */
	private static final String UPLOAD_FOLDER = "uploads";
	
	/**
	 * Atributo que almacenara el path a la carpeta que contiene los archivos
	 */
	private final Path rootLocation;

	/**
	 * constructor que inicializa la variable que contiene el path del directorio
	 * @param storageProperties
	 */
	@Autowired
	public UploadFileServiceImpl(StorageProperties storageProperties) {
		this.rootLocation = Paths.get(storageProperties.getLocation());
	}
	
	/**
	 * metodo encargado de concatenar el path base del directorio con el nombre del archivo
	 * @param fileName
	 * @return path que tiene la direccion relativa del archivo en el servidor
	 */
	public Path getPath(String fileName) {
		return Paths.get(UPLOAD_FOLDER).resolve(fileName).toAbsolutePath();
	}

	/**
	 * metodo encargado de crear el directorio en el servidor
	 */
	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get(UPLOAD_FOLDER));

	}

	/**
	 * metodo encargado de eliminar el directorio y su contenido
	 */
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOAD_FOLDER).toFile());
		
	}

	/**
	 * metodo encargdo de copia el archivo del disco fisco y ponerlo en el servidor, genrando un 
	 * unico id con el fin de evitar coincidencias y concatenandolo al nombre del archivo, para 
	 * finalmente copiarlo en el directorio
	 * @param multipartFile
	 * @return String con el nuevo nombre del archivo
	 */
	@Override
	public String copy(MultipartFile multipartFile) throws IOException {
		String uniqueFileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
		Path rootPath = getPath(uniqueFileName);
		log.info("rootPath: " + rootPath);
		log.info("file: " + multipartFile.getName().toString() + "  " + multipartFile.getOriginalFilename().toString());
		Files.copy(multipartFile.getInputStream(), rootPath);
		return uniqueFileName;
	}

	/**
	 * metod encargado de obtener la ruta relativa del archivo
	 * @param filename
	 * @return path con la ruta relativa del archivo
	 */
	@Override
	public Path loadFile(String filename) {
		return rootLocation.resolve(filename);
	}

}
