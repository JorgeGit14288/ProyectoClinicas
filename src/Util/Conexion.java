/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jorge
 */
public class Conexion {
    
    public Conexion()
    {
         emf = Persistence.createEntityManagerFactory("PUClinicas");
        emf.createEntityManager();
    }
    EntityManagerFactory emf;

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public EntityManager getEm() {
        return em;
    }
     EntityManager em;
     // creamos la unidad para poder realizar lo que tenemos que realizar.
    
}
