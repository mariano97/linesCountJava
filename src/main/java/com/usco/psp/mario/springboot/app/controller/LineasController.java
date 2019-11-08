package com.usco.psp.mario.springboot.app.controller;

/**
 * Se realiza la importación de las librerias necesarias 
 * para el funcionamiento de las funciones declaradas en la 
 * clase
 */
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.psp.mario.springboot.app.models.entity.LinesCounted;
import com.usco.psp.mario.springboot.app.models.service.ICountLinesService;
import com.usco.psp.mario.springboot.app.models.service.IFileExtensionService;
import com.usco.psp.mario.springboot.app.models.service.IUploadFileService;

/**
 * Se anota la clase como controlador, aqui sera donde se manejara el conportamiento 
 * de las vistas y las respuestas HTTP
 * 
 * @author mario
 */
@Controller
public class LineasController {
	
	/**
	 * Artributo que permite la impersion en la consola durante el tiempo de 
	 * ejecucion de la aplicacion
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * Se declaran los atributos referenciados a interfaces, ademas
	 * se anotan para que sean auto instanciados cunado se necesiten
	 */
	@Autowired
	private IUploadFileService uploadFileServiceImpl;
	
	@Autowired
	private IFileExtensionService fileExtension;
	
	@Autowired
	private ICountLinesService countLinesServiceImpl;
	
	/**
	 * Este metodo se encarga de crear la vista principal
	 * @param model
	 * @return String con el nombre de la vista
	 */
	@GetMapping("/index")
	public String upload(Model model) {
		model.addAttribute("titulo", "Contador de lineas de archivos .java");
		model.addAttribute("tituloHeader", "Cargar arhivo .java");
		return "index";
	}
	
	/**
	 * Se crea un metodo que se encargara de ejecutar el conteo de las lineas, este metodo
	 * se anota con un mapeo a una URL que contiene el nombre del archivo en el servidor, aqui se llama 
	 * a un servicio encargado de contar las lineas.
	 * @param nameFile, es e contendra el nombre del archivo
	 * @param model
	 * @param redirectAttributes
	 * @return String con el nombre de la vista si no existe error alguno, de existir error se redirecciona a otra vista
	 */
	@GetMapping(value = "/count/{nameFile}")
	public String countLines(@PathVariable(name = "nameFile") String nameFile, Model model, RedirectAttributes redirectAttributes) {
		log.info("PAGEname: " + nameFile);
		if(nameFile.isBlank()) {
			log.info("PAGEname: " + nameFile);
			redirectAttributes.addFlashAttribute("error", "No se encontro el archivo");
			return "redirect:/index";
		}
		try {
			LinesCounted linesCounted = countLinesServiceImpl.countLines(uploadFileServiceImpl.loadFile(nameFile).toString());
			countLinesServiceImpl.restoreVariables();
			log.info("PAGECOUNT: " + linesCounted.getLinesTotal());
			String[] filename = nameFile.split("_");
			model.addAttribute("linesCounted", linesCounted);
			model.addAttribute("titulo", "Contador de lineas de archivos .java");
			model.addAttribute("tituloHeader", "El conteo de las lineas para el archivo " + filename[1] + " es:");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", "El archivo no existe");
			return "redirect:/index";
		}
		return "countLines";
		
	}
	
	/**
	 * Se crea un metodo con controlara el guardado de los datos, se anota con con un mapeo de respuesta de metodo POST,
	 * Se encarga de llamar al servicio encargado de guardar en la base de datos.
	 * @param linesCounted, contiene los datos extraidos de las vistas
	 * @param model
	 * @param redirectAttributes
	 * @return String con el redireccionamiento a la vista principal
	 */
	@RequestMapping(value = "/countLines", method = RequestMethod.POST)
	public String guardarDatos(@ModelAttribute("linesCounted") LinesCounted linesCounted, Model model, RedirectAttributes redirectAttributes) {
		countLinesServiceImpl.save(linesCounted);
		redirectAttributes.addFlashAttribute("success", "Los datos fueron guardados con exito");
		return "redirect:/index";
	}
	
	/**
	 * Es un metodo cargado de cargar el archivo al servidor, esta anotado con un mapeo de respuesta
	 * de metodo POST, aqui se evalua si tiene la extension valida y tambien que si exista un archivo listo para
	 * cargarse
	 * @param multipartFile, contiene la direccion del archivo que se va a cargar
	 * @param redirectAttributes
	 * @return String con el redireccionamiento a la pagina principal si no es el archivo valido o a la pagina de 
	 * conteo de lineas.
	 */
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String cargarFile(@RequestParam("file") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
		if(!fileExtension.isValidExtension(multipartFile.getOriginalFilename().toString())) {
			log.info("return false");
			redirectAttributes.addFlashAttribute("error", "El archivo que esta intentando cargar no tiene una extensión "
					+ "\".java\", por favor cargue un archivo con esta extensión");
			redirectAttributes.addFlashAttribute("labelValue", multipartFile.getOriginalFilename().toString());
			return "redirect:/index";
		}
		String uniqueFilename = "";
		if(!multipartFile.isEmpty()) {
			try {
				uniqueFilename = uploadFileServiceImpl.copy(multipartFile);
				log.info("si .java");
				redirectAttributes.addFlashAttribute("success", "El archivo fue cargado satisfactoriamente y se proceso correctamente");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/count/".concat(uniqueFilename);
	}
}

