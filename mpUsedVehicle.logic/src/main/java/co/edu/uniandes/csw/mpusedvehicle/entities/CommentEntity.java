package co.edu.uniandes.csw.mpusedvehicle.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author guillermo.ferro
 */
@Entity
@Table(name = "commententity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commententity.getByProduct", query = "SELECT c FROM CommentEntity c WHERE c.id = :product_id")})
public class CommentEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "Comment")
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Size(max = 255)
    private String description;
    
    @ManyToOne
    private ClientEntity client;
    
    @ManyToOne
    private ProductEntity product;

    public CommentEntity() {
    }

    public CommentEntity(Long id) {
        this.id = id;
    }

    public CommentEntity(Long id, Date date) {
        this.id = id;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CommentEntity)) {
            return false;
        }
        CommentEntity other = (CommentEntity) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "co.edu.uniandes.csw.mpusedvehicle.entities.Commententity[ id=" + id + " ]";
    }
    
}
