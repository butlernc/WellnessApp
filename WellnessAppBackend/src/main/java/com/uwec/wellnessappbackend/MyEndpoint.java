/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.uwec.wellnessappbackend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.List;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "wellnessappbackend.uwec.com", ownerName = "wellnessappbackend.uwec.com", packagePath = ""))
public class MyEndpoint {

    private static final String KEY_KIND = "UserBeanParent";
    private static final String KEY_NAME_USER_OBJECT = "userbeanobject.object";

    /**
     * Stores a new userBean (user object)
     * @param userBean
     * @return
     */
    @ApiMethod(name = "storeUserBean")
    public void storeUserBean(UserBean userBean) {
        //connection
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction transaction = datastoreService.beginTransaction();

        try{
            //save the given userBean
            Key userBeanParentKey = KeyFactory.createKey(KEY_KIND, KEY_NAME_USER_OBJECT);
            Entity userBeanEntity = new Entity(userBeanParentKey);
            userBeanEntity.setProperty("data", userBean);
            datastoreService.put(userBeanEntity);
            transaction.commit();
        } finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    /**
     * Retrieves a userBeanEntity object from the given email,
     * grabs the userBean object stored in it, and returns that object.
     * @param email
     * @return
     */
    @ApiMethod(name = "getUserBean")
    public UserBean getUserBean(@Named("email") String email) {
        //connection
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key userBeanParentKey = KeyFactory.createKey(KEY_KIND, KEY_NAME_USER_OBJECT);

        //grab all users
        Query query = new Query(userBeanParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        //grab current user if exists
        UserBean userBean = null;
        for(Entity result: results) {
            UserBean current = (UserBean) result.getProperty("data");
            if(current.getEmail().equals(email)) {
                userBean = new UserBean(current);
            }
        }

        return userBean;
    }

}
