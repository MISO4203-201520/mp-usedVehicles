/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mpusedvehicle.services;

import co.edu.uniandes.csw.mpusedvehicle.api.IAdminLogic;
import co.edu.uniandes.csw.mpusedvehicle.api.IClientLogic;
import co.edu.uniandes.csw.mpusedvehicle.api.IProviderLogic;
import co.edu.uniandes.csw.mpusedvehicle.dtos.AdminDTO;
import co.edu.uniandes.csw.mpusedvehicle.dtos.ClientDTO;
import co.edu.uniandes.csw.mpusedvehicle.dtos.ProviderDTO;
import co.edu.uniandes.csw.mpusedvehicle.dtos.UserDTO;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountStatus;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.group.GroupList;
import com.stormpath.sdk.resource.ResourceException;
import com.stormpath.shiro.realm.ApplicationRealm;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author Jhonatan
 * @modified djimenez
 */
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

    private static final String NADA = "nada";
    private static final String ADMINISTRATOR = "administrator";
    private static final String PROVIDER = "provider";
    private static final String USER = "user";
    
    @Inject
    private IClientLogic clientLogic;

    @Inject
    private IProviderLogic providerLogic;
    
    @Inject
    private IAdminLogic adminLogic;
    
    @GET
    public List<UserDTO> getUsers() {
        List<UserDTO> allUsers = new ArrayList<UserDTO>();
        
        for (ClientDTO client : clientLogic.getClients(null, null)) {
            UserDTO clientDTO = new UserDTO();
            clientDTO.setName(client.getName());
            clientDTO.setRememberMe(false);
            allUsers.add(clientDTO);
        }
        
        for (ProviderDTO provider : providerLogic.getProviders(null, null)) {
            UserDTO providerDTO = new UserDTO();
            providerDTO.setName(provider.getName());
            providerDTO.setRememberMe(true);
            allUsers.add(providerDTO);
        }
        
        return allUsers;
    }

    @Path("/login")
    @POST
    public Response login(UserDTO user) {
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword(), user.isRememberMe());
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            ClientDTO client = clientLogic.getClientByUserId(currentUser.getPrincipal().toString());
            if (client != null) {
                currentUser.getSession().setAttribute("Client", client);
                return Response.ok(client).build();
            } else {
                ProviderDTO provider = providerLogic.getProviderByUserId(currentUser.getPrincipal().toString());
                if (provider != null) {
                    currentUser.getSession().setAttribute("Provider", provider);
                    return Response.ok(provider).build();
                } else {
                    AdminDTO admin = adminLogic.getAdminByUserId(currentUser.getPrincipal().toString());
                    if (admin != null) {
                        currentUser.getSession().setAttribute("Admin", admin);
                        return Response.ok(admin).build();
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST)
                            .entity(" User is not registered")
                            .type(MediaType.TEXT_PLAIN)
                            .build();
                    }
                }
            }
        } catch (AuthenticationException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @Path("/logout")
    @GET
    public Response logout() {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
            return Response.ok().build();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @Path("/isAdmin")
    @GET
    public Response isCurrentUserAdmin() {
        Subject currentUser = SecurityUtils.getSubject();
        AdminDTO admin = adminLogic.getAdminByUserId(currentUser.getPrincipal().toString());
        return Response.ok(admin).build();
    }

    @Path("/currentUser")
    @GET
    public Response getCurrentUser() {
        UserDTO user = new UserDTO();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Map<String, String> userAttributes = (Map<String, String>) currentUser.getPrincipals().oneByType(java.util.Map.class);
            user.setName(userAttributes.get("givenName") + " " + userAttributes.get("surname"));
            user.setEmail(userAttributes.get("email"));
            user.setUserName(userAttributes.get("username"));
            if(currentUser.hasRole(USER)){
                user.setRole(USER);
            }else if (currentUser.hasRole(PROVIDER)) {
                user.setRole(PROVIDER);
            }else if(currentUser.hasRole(ADMINISTRATOR)){
                user.setRole(ADMINISTRATOR);
            }else{
                user.setRole(NADA);
            }
            
            return Response.ok(user).build();
        } catch (AuthenticationException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
    
    @Path("/register")
    @POST
    public Response setUser(UserDTO user) {

        try {
            switch (user.getRole()) {
                case USER:
                    Account acctClient = createUser(user);
                    ClientDTO newClient = new ClientDTO();
                    newClient.setName(user.getUserName());
                    newClient.setUserId(acctClient.getHref());
                    newClient = clientLogic.createClient(newClient);
                    break;

                case PROVIDER:
                    Account acctProvider = createUser(user);
                    ProviderDTO newProvider = new ProviderDTO();
                    newProvider.setName(user.getUserName());
                    newProvider.setUserId(acctProvider.getHref());
                    newProvider = providerLogic.createProvider(newProvider);
                    break;                                      
            }
            return Response.ok().build();
        } catch (ResourceException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            return Response.status(e.getStatus())
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
    
    @Path("/create")
    @POST
    public Response createUser(ClientDTO client) {

        try {
            clientLogic.createClient(client);
            return Response.ok().build();
        } catch (ResourceException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            return Response.status(e.getStatus())
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
    
    private Account createUser(UserDTO user) {
        ApplicationRealm realm = ((ApplicationRealm) ((RealmSecurityManager) SecurityUtils.getSecurityManager()).getRealms().iterator().next());
        Client client = realm.getClient();
        Application application = client.getResource(realm.getApplicationRestUrl(), Application.class);
        Account acct = client.instantiate(Account.class);
        acct.setUsername(user.getUserName());
        acct.setPassword(user.getPassword());
        acct.setEmail(user.getEmail());
        acct.setGivenName(user.getName());
        acct.setSurname(user.getName());
        acct.setStatus(AccountStatus.ENABLED);
        GroupList groups = application.getGroups();
        for (Group grp : groups) {
            if (grp.getName().equals(user.getRole())) {
                acct = application.createAccount(acct);
                acct.addGroup(grp);
                break;
            }
        }
        return acct;
    }
}
