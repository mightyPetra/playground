package org.spend.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.spend.model.CartItem;
import org.spend.model.User;

@Slf4j
// TODO: set logs to write to tomcat log
// use resources
public class DatabaseManager {

    //TODO this can be written better
    private static final String USERS_PATH = "/Users/anastasia/IdeaProjects/playground/shopping/src/main/webapp/WEB-INF/database/users.txt";
    private static final String CARTS_PATH = "/Users/anastasia/IdeaProjects/playground/shopping/src/main/webapp/WEB-INF/database/carts.txt";


    public Map<String, User> getUsers() {
        try {
            File usersDb = new File(USERS_PATH);

            if (usersDb.isFile() && usersDb.length() != 0L) {
                FileInputStream fis = new FileInputStream(usersDb);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Map<String, User> users = (Map<String, User>) ois.readObject();
                ois.close();
                return users;
            }
        } catch (FileNotFoundException e) {
            logFailedFindData();
        } catch (IOException e) {
            logFailedSaveData();
        } catch (Exception e) {
            logUnexpectedError();
        }
        return new HashMap<>();
    }

    public Map<String, Map<CartItem, Integer>> getUserCarts() {
        try {
            File cartsDb = new File(CARTS_PATH);

            if (cartsDb.isFile() && cartsDb.length() != 0L) {
                FileInputStream fis = new FileInputStream(cartsDb);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Map<String, Map<CartItem, Integer>> userCarts = (Map<String, Map<CartItem, Integer>>) ois.readObject();
                ois.close();
                return userCarts;
            }
        } catch (FileNotFoundException e) {
            logFailedFindData();
        } catch (IOException e) {
            logFailedSaveData();
        } catch (Exception e) {
            logUnexpectedError();
        }
        return new HashMap<>();
    }

    public void writeUsers(Map<String, User> users) {
        try {
            File usersDb = new File(this.USERS_PATH);

            if (usersDb.isFile() && usersDb.length() != 0L) {
                usersDb.delete();
                usersDb.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(usersDb);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);

            oos.flush();
            oos.close();
        } catch (IOException e) {
            logErrorSavingData();
        } catch (Exception e) {
            logUnexpectedError();
        }
    }

    public void writeUserCarts(Map<String, Map<CartItem, Integer>> userCarts) {
        try {
            File cartsDb = new File(this.CARTS_PATH);

            if (cartsDb.isFile() && cartsDb.length() != 0L) {
                cartsDb.delete();
                cartsDb.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(cartsDb);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userCarts);

            oos.flush();
            oos.close();
        } catch (IOException e) {
            //TODO replace with exceptions
            logErrorSavingData();
        } catch (Exception e) {
            logUnexpectedError();
        }
    }

    private static void logFailedFindData() {
        log.error("FAILED TO FIND SAVED DATA.");
    }

    private static void logFailedSaveData() {
        log.error("FAILED TO SAVE DATA.");
    }

    private static void logUnexpectedError() {
        log.error("UNEXPECTED ERROR OCCURED.");
    }

    private void logErrorSavingData() {
        log.error("ERROR SAVING DATA");
    }
}