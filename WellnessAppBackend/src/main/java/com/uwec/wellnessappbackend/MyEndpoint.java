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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "wellnessappbackend.uwec.com", ownerName = "wellnessappbackend.uwec.com", packagePath = ""))
public class MyEndpoint {

    private static final String KEY_KIND = "UserBeanParent";
    private static final String KEY_NAME_USER_OBJECT = "userbeanobject.object";

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    /**
     * Stores a new userBean (user object)
     * @param userBean
     * @return
     */
    @ApiMethod(name = "storeUserBean")
    public void storeUserBean(UserBean userBean) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction transaction = datastoreService.beginTransaction();

        try{
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
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction transaction = datastoreService.beginTransaction();
        return null;
    }

}
