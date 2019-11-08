package com.usco.psp.mario.springboot.app.models.service;

/**
 * Se realizan las importaciones de las librerias necesarias para que las funciones
 * declaradas en el cuerpo de la clase puedan ejecutarse correctamente
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.psp.mario.springboot.app.configuration.properties.LinesProperties;
import com.usco.psp.mario.springboot.app.models.dao.ICountLinesDaoRepository;
import com.usco.psp.mario.springboot.app.models.entity.LinesCounted;

/**
 * Clase que implementa la interface que permite la conexion con el controlador de las vistas, 
 * se encuentra anotado como servicio, ya que en si es un servicio
 * @author mario
 *
 */
@Service
public class CountLinesServiceImpl implements ICountLinesService {
	
	/**
	 * Artributo que permite la impersion en la consola durante el tiempo de 
	 * ejecucion de la aplicacion
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * atributos que albergaran el de la sumatoria de cada linea, todos de tipo entero 
	 * 
	 */
	private Integer sumTotalLines = 0;
	private Integer sumEmptyLines = 0;
	private Integer sumCommentsLines = 0;
	private Integer sumLogicalsLines = 0;
	private Integer sumAttributeLines = 0;
	private Integer sumMethodsLines = 0;
	
	/**
	 * atributos que alojaran los valores de las propiedades de configuracion
	 */
	private final String[] charactersComments;
	private final String[] accersors;
	private final String[] dataTypes;
	private final String annotation;
	private final String[] keywords;
	private final String methodTypeData;
	
	/**
	 * atributo encargado de confimar si una linea es comentario o no
	 */
	private boolean isLineContinumComment;
	
	/**
	 * atributo encargado de realizar una comunicacion con la interface que extiende 
	 * a la clase de ejecutar SQL, anotado para que se aunto llame cuando se necesite
	 */
	@Autowired
	private ICountLinesDaoRepository countLinesDao;
	
	/**
	 * Constructor que inicializa los atributos de propiedades, anotado para que sea 
	 * automatico
	 * @param linesProperties
	 */
	@Autowired
	public CountLinesServiceImpl(LinesProperties linesProperties) {
		this.charactersComments = linesProperties.getComments().split(",");
		this.accersors = linesProperties.getAccesors().split(",");
		this.dataTypes = linesProperties.getTypedata().split(",");
		this.annotation = linesProperties.getAnnotation();
		this.keywords = linesProperties.getKeywords().split(",");
		this.methodTypeData = linesProperties.getMethodtypedata();
	}

	/**
	 * Sobre escribiendo el metodo de la interface implementada, en este metodo se cuentan las lines
	 * recibiendo el path donde se encuentra albergado el archivo, arroja una exception si el 
	 * archivo no se puede leer
	 * @param pathFilename, el cual contendra la ruta de almacenamiento del archivo
	 * @return LinesCounted, objeto que contendra todo los valores de la sumatoria
	 */
	@Override
	public LinesCounted countLines(String pathFilename) throws IOException{
		String lineReader;
		Reader reader = new FileReader(new File(pathFilename));
		BufferedReader bufferedReader = new BufferedReader(reader);
		while((lineReader = bufferedReader.readLine()) != null) {
			lineReader = lineReader.trim();
			sumTotalLines++;
			if(isEmptyLine(lineReader) || isLineUniqueCharacter(lineReader) || isCommentLine(lineReader) || isAnnotationLine(lineReader)) {
				log.info(sumTotalLines + "| lineGeneral: " + lineReader);
				continue;
			}
			if(isAttributeLine(lineReader)) {
				sumAttributeLines++;
				log.info(sumTotalLines + "| lineAttribute1: " + lineReader);
			}else if(isMethodLine(lineReader)) {
				sumMethodsLines++;
				log.info(sumTotalLines + "| lineMethod1: " + lineReader);
			}else {
				sumLogicalsLines++;
			}
			
		}
		bufferedReader.close();
		log.info("countLine: empty=" + sumEmptyLines + " logicals=" + sumLogicalsLines + " comment=" + sumCommentsLines + " annotation=" + sumLogicalsLines);
		log.info("countLine2: attribute=" + sumAttributeLines + " method=" + sumMethodsLines + " total=" + sumTotalLines);
		LinesCounted linesCounted = new LinesCounted();
		linesCounted.setLinesAttribute(sumAttributeLines.longValue());
		linesCounted.setLinesComment(sumCommentsLines.longValue());
		linesCounted.setLinesEmpty(sumEmptyLines.longValue());
		linesCounted.setLinesLogical(sumLogicalsLines.longValue());
		linesCounted.setMethods(sumMethodsLines.longValue());
		linesCounted.setLinesTotal();
		linesCounted.setNameFile(pathFilename.replace("./uploads/", ""));
		return linesCounted;
	}
	
	/**
	 * metodo encargado de restaurar los valores de los atributos a sus valores 
	 * por defecto
	 */
	@Override
	public void restoreVariables() {
		sumAttributeLines = 0;
		sumCommentsLines = 0;
		sumEmptyLines = 0;
		sumLogicalsLines = 0;
		sumMethodsLines = 0;
		sumTotalLines = 0;
	}
	
	/**
	 * Metodo encargado de almacenar los datos en la base datos, por medio de la interface
	 * @param linesCounted, recive el objetos con los valores de las lineas
	 * 
	 */
	@Override
	@Transactional
	public void save(LinesCounted linesCounted) {
		countLinesDao.save(linesCounted);
	}
	
	/**
	 * metodo encargado de evaluar si la linea leida es una anotacion, de ser real 
	 * suma 1 a la sumatoria de lineas logicas
	 * @param line
	 * @return boolean, verdadero si la linea inicia con el valor traido de propiedades '@'
	 * falso si no es asi
	 */
	private boolean isAnnotationLine(String line) {
		if(line.startsWith(annotation)) {
			sumLogicalsLines++;
			return true;
		}
		return false;
	}

	/**
	 * metodo encargado de evaluar si la linea tiene un modificador de acceso
	 * @param line
	 * @return true si y solo si la linea inicia con alguno de los modificadores de acceso
	 */
	private boolean hasAccesor(String line) {
		for(String accesor : accersors) {
			if(line.startsWith(accesor.trim())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * metodo encargado de evaluar si la linea tiene alguno de los datos primitivos en su firma
	 * @param line
	 * @return true si y solo si la linea inicia con alguno de los tipos primitivos
	 */
	private boolean hasReturnDataType(String line) {
		for(String dataType : dataTypes) {
			if(line.startsWith(dataType.trim())) {
				log.info("contain: " + dataType);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * metodo encargado de evaluar si la primera letra de la linea es mayuscula
	 * @param character
	 * @return true si y solo si el caracter es mayuscula
	 */
	private boolean isReturnDataTypeObject(char character) {
		return Character.isUpperCase(character);
	}
	
	/**
	 * metodo encargado de evaluar si la linea es una linea en blanco, de ser verdadero
	 * se sumara 1 a la sumatoria de las lineas vacias
	 * @param line
	 * @return true si y solo si la lines es blanca
	 */
	private boolean isEmptyLine(String line) {
		if(line.isBlank()) {
			sumEmptyLines++;
			return true;
		}
		return false;
	}
	
	/**
	 * metodo encargado de verificar que la linea solo tenga un unico caracter en especifico e 
	 * incrementar en 1 a la sumatoria de las lineas logicas si es verdadero
	 * @param line
	 * @return true si y solo si la lines tiene '}'
	 */
	private boolean isLineUniqueCharacter(String line) {
		if(line.equals("}")) {
			sumLogicalsLines++;
			return true;
		}
		return false;
	}
	
	/**
	 * metodo encargado de verificar si la linea es una linea de comentario e incrementar en 1 la 
	 * sumatoria de lineas de comentario
	 * @param line
	 * @return true si y solo si la linea contiene alguno de los caracteres definidos en el archivo
	 * de propiedades
	 */
	private boolean isCommentLine(String line) {
		if(line.indexOf(charactersComments[0].trim())>=0) {
			sumCommentsLines++;
			return true;
		}else if(line.indexOf(charactersComments[1].trim())>=0) {
			sumCommentsLines++;
			isLineContinumComment = true;
			return true;
		}else if(line.indexOf(charactersComments[2].trim())>=0) {
			sumCommentsLines++;
			isLineContinumComment = false;
			return true;
		}else if(isLineContinumComment){
			sumCommentsLines++;
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * metodo encargado de remover valores de una linea especificada, remplazandolos por ningun
	 * caracter
	 * @param line
	 * @param values
	 * @return String con la nueva cadena si en el caracter ya eliminado
	 */
	private String removeOfString(String line, String... values) {
		String newLine = line;
		for(String value : values) {
			newLine = newLine.replace(value.concat(" "), "");
		}
		return newLine;
	}
	
	/**
	 * metodo encargado de remover los modificadores de acceso de la linea actual
	 * @param line
	 * @return String con la nueva linea sin los modificadores de acceso
	 */
	private String removeAccesors(String line) {
		String newLine = line;
		for(String accesor : accersors) {
			newLine = removeOfString(newLine, accesor.trim());
		}
		return newLine;
	}
	
	/**
	 * metodo encagado de evaluar si tiene substrings seprados por el '.'
	 * @param split
	 * @return true si en la linea se encuentra presente el '.'
	 */
	private boolean hasSubStrings(String split) {
		return split.contains(".");
	}
	
	/**
	 * metodo encargado de validar si la linea es atributo, para ello evalua si tiene modificadores
	 * de acceso, de ser asi los remueve y tambien remueve las palabras reservadas 'static' y 'final'
	 * @param line
	 * @return true si la linea cumple con que tenga alguno de los tipos de datos primitivos o que 
	 * tenga un tipo de dato Object, tambie que no tenga substrings y tambien que al final 
	 * de la linea este presente ';'
	 */
	private boolean isAttributeLine(String line) {
		//log.info("ISATTRIBUTE: " + line);
		String newLine = line;
		if(hasAccesor(line)) {
			 newLine = removeAccesors(line);	
		}
		newLine = removeOfString(newLine, keywords);
		//log.info("ISATTRIBUTE: " + newLine);
		String[] splits = newLine.split(" ");
		return ((hasReturnDataType(splits[0]) || isReturnDataTypeObject(splits[0].charAt(0))) 
				&& !hasSubStrings(splits[0]) && splits[splits.length-1].endsWith(";"));
	}
	
	/**
	 * metodo encagado de validar si la linea es un metodo, para ello evalua si tiene modificadores
	 * de acceso, de ser asi los remueve asi como las palabras reservadas 'static' y 'final'
	 * @param line
	 * @return true si la linea cumple con que tenga alguno de los tipos de datos primitivos o que
	 * tenga un tipo de datos Object o que tenga tipo de datos 'void' y que al final de la linea
	 * tenga presente '{'
	 */
	private boolean isMethodLine(String line) {
		//log.info("ISMETHOD: " + line);
		String newLine = line;
		if(hasAccesor(line)) {
			 newLine = removeAccesors(line);	
		}
		newLine = removeOfString(newLine, keywords);
		//log.info("ISMETHOD: " + newLine);
		String[] splits = newLine.split(" ");
		return ((splits[0].equals(methodTypeData) || hasReturnDataType(splits[0]) || isReturnDataTypeObject(splits[0].charAt(0)))
				&& splits[splits.length-1].endsWith("{"));
	}

}
