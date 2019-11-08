package com.usco.psp.mario.springboot.app.models.entity;
/**
 * Se importan las librerias necesarias para que las funciones declaradas en la clase
 * puedan ser ejecutadas correctamente
 */
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Se anota la clase como una clase de entidad, tambien se anota con 'Table' para 
 * configurar el nombre de la tabla en la base de datos, ademas se implementa la interfaz 
 * Serializable para poder serializar el objeto y poderla enviarla o transportarla  
 * @author mario
 *
 */
@Entity
@Table(name = "lineas")
public class LinesCounted implements Serializable {

	/**
	 * Se crea una variable default de serializacion
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Se declaran los atributos que tendra el objeto 
	 * LinesCounted, ademas existe un atributo adicional
	 * 'Id' para poder identificar los datos en las bases de 
	 * datos, ese atributo de anota como Id y tambien se 
	 * anota para que se genere el valor automatico
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public Long methods;
	public Long linesEmpty;
	public Long linesTotal;
	public Long linesComment;
	public Long linesAttribute;
	public Long linesLogical;
	public String nameFile;

	//Se crea un constructor
	public LinesCounted() {
	}

	/**
	 *Se declaran los respectivos getter y setters para cada 
	 *uno de los atributos antes declarados 
	 */
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMethods() {
		return methods;
	}

	public void setMethods(Long methods) {
		this.methods = methods;
	}

	public Long getLinesEmpty() {
		return linesEmpty;
	}

	public void setLinesEmpty(Long linesEmpty) {
		this.linesEmpty = linesEmpty;
	}

	public Long getLinesTotal() {
		return linesTotal;
	}

	public void setLinesTotal(Long linesTotal) {
		this.linesTotal = linesTotal;
	}
	
	/**
	 * Se realiza un setter nuevo para las lineas totales, con el fin de 
	 * obtener la suma total de las lineas
	 */
	public void setLinesTotal() {
		this.linesTotal = linesComment + linesEmpty + linesAttribute + linesLogical + methods;
	}

	public Long getLinesComment() {
		return linesComment;
	}

	public void setLinesComment(Long linesComment) {
		this.linesComment = linesComment;
	}

	public Long getLinesAttribute() {
		return linesAttribute;
	}

	public void setLinesAttribute(Long linesAttribute) {
		this.linesAttribute = linesAttribute;
	}

	public Long getLinesLogical() {
		return linesLogical;
	}

	public void setLinesLogical(Long linesLogical) {
		this.linesLogical = linesLogical;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

}
