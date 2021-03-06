package co.edu.uniandes.csw.mpusedvehicle.services;

import co.edu.uniandes.csw.mpusedvehicle.api.ICartItemLogic;
import co.edu.uniandes.csw.mpusedvehicle.api.IOrderLogic;
import co.edu.uniandes.csw.mpusedvehicle.dtos.CartItemDTO;
import co.edu.uniandes.csw.mpusedvehicle.dtos.ClientDTO;
import co.edu.uniandes.csw.mpusedvehicle.dtos.OrderDTO;
import co.edu.uniandes.csw.mpusedvehicle.dtos.ProviderDTO;
import co.edu.uniandes.csw.mpusedvehicle.enums.OrderStatus;
import co.edu.uniandes.csw.mpusedvehicle.providers.StatusCreated;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.shiro.SecurityUtils;

/**
 * @generated
 */
@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderService {

    @Inject private ICartItemLogic cartItemLogic;
    @Inject private IOrderLogic orderLogic;
    
    /**
     * Cliente que ha iniciado sesion
     */
    private ClientDTO client = (ClientDTO)SecurityUtils.getSubject().getSession().getAttribute("Client");
    /**
     * Provedor que ha iniciado sesion
     */
    private ProviderDTO provider = (ProviderDTO)SecurityUtils.getSubject().getSession().getAttribute("Provider");
    /**
     * @generated
     */
    @POST
    @StatusCreated
    public OrderDTO createOrder(OrderDTO order) {
        return orderLogic.submitOrder(order);
    }

    /**
     * @generated
     */
    @GET
    public List<OrderDTO> getOrders() {       
//       return orderLogic.getOrders();
       return orderLogic.getOrdersByStatus(OrderStatus.NEW);
    }

    /**
     * @generated
     */
    @GET
    @Path("{id: \\d+}")
    public CartItemDTO getCartItem(@PathParam("id") Long id) {
        return cartItemLogic.getCartItemsByClientById(id, client.getId());
    }

    /**
     * @generated
     */
    @PUT
    @Path("{id: \\d+}")
    public OrderDTO updateOrder(@PathParam("id") Long id, OrderDTO dto) {
        return orderLogic.updateOrder(id, dto);
    }

    /**
     * @generated
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteCartItem(@PathParam("id") Long id) {
        cartItemLogic.deleteCartItemByClient(client.getId(), id);
    }
    
    /**
     * Sercicio REST encargado de obtener la lista de ordenes de un provedor.
     * @param id. Devuelve el id del provedor.
     * @return List. Lista de ordenes.
     */
    @GET
    @Path("/provider/{id:\\d+}")
    public List<OrderDTO> getOrdersByProvider(@PathParam("id") Long id) {
        if(provider!= null)
        {
            return orderLogic.getOrdersByProvider(id);
        }
        return Collections.emptyList();
    }
    
    /**
     * Sercicio REST encargado de obtener la lista de ordenes de un cliente.
     * @param id. Devuelve el id del cliente.
     * @return List. Lista de ordenes.
     */
    @GET
    @Path("/client/{id:\\d+}")
    public List<OrderDTO> getOrdersByClient(@PathParam("id") Long id) {
        if(client!= null)
        {
            return orderLogic.getOrdersByClient(id);
        }
        return Collections.emptyList();
    }
}
