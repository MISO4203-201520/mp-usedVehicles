package co.edu.uniandes.csw.mpusedvehicle.persistence;

import co.edu.uniandes.csw.mpusedvehicle.entities.ClientEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

/**
 * @generated
 */
@Stateless
public class ClientPersistence extends CrudPersistence<ClientEntity> {

    /**
     * @generated
     */
    public ClientPersistence() {
        this.entityClass = ClientEntity.class;
    }
    
    public ClientEntity getClientByUserId(String userId){
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_id", userId);
            return this.executeSingleNamedQuery("Client.getByUserId", params);
        } catch (NoResultException e) {
            Logger.getGlobal().severe(e.getMessage());
            return null;
        }
    }
}
