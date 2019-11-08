package com.usco.psp.mario.springboot.app.configuration.properties;

/**
 * Se realizan las importaciones de las librerias necesarias para que las funciones
 * declaradas en el cuerpo de la clase puedan ejecutarse correctamente
 */
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Clase que contendra el medio de accesar a propiedades estaticas
 * se anota como propiedades de configuracion y se le da el prefijo de las 
 * propiedades
 * 
 * @author mario
 *
 */
@ConfigurationProperties(prefix = "lines")
public class LinesProperties {

	/**
	 * Se declaran atributos necesarios para la obtencion de las propiedades 
	 * estaticas albergadas en un archivo de configuracion
	 */
	private String comments;
	private String accesors;
	private String typedata;
	private String annotation;
	private String keywords;
	private String methodtypedata;

	/**
	 * Se realizan los respectivos getters y setters a cada uno de los atributos
	 * antes declarados
	 * 
	 */
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAccesors() {
		return accesors;
	}

	public void setAccesors(String accesors) {
		this.accesors = accesors;
	}

	public String getTypedata() {
		return typedata;
	}

	public void setTypedata(String typedata) {
		this.typedata = typedata;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getMethodtypedata() {
		return methodtypedata;
	}

	public void setMethodtypedata(String methodtypedata) {
		this.methodtypedata = methodtypedata;
	}
	
}
