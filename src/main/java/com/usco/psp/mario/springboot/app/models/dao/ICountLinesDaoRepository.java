package com.usco.psp.mario.springboot.app.models.dao;

/**
 * Se realizan las importaciones de las librerias necesarias para que las funciones
 * declaradas en el cuerpo de la clase puedan ejecutarse correctamente
 */
import org.springframework.data.repository.PagingAndSortingRepository;

import com.usco.psp.mario.springboot.app.models.entity.LinesCounted;

/**
 * Se crea una interface que extiende a la clase 'PagingAndSortingRepository' la cual 
 * permite realizar las operacions respectivas a un SQL
 * @author mario
 *
 */
public interface ICountLinesDaoRepository extends PagingAndSortingRepository<LinesCounted, Long>{

}
