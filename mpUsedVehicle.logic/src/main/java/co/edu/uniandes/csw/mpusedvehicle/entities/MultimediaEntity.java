/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mpusedvehicle.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author miguelolivares
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "MultimediaEntity.getImages", query = "SELECT m FROM MultimediaEntity m WHERE m.vehiclename= :name")})
public class MultimediaEntity implements Serializable {
    
    @Id
    @GeneratedValue(generator = "MultimediaEntity")
    private Long id;
    
    
    private String vehiclename;
    
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehiclename() {
        return vehiclename;
    }

    public void setVehiclename(String vehiclename) {
        this.vehiclename = vehiclename;
    }

    

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    
    
}
