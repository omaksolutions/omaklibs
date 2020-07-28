package com.omak.omakhelpers.RealmHelpers;

import android.content.Context;

import com.omak.omakhelpers.HelperFunctions;
import com.omak.omakhelpers.firebaseNotification.RealmNotificationModel;
import com.omak.omakhelpers.firebaseNotification.notiData;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class RealmHelpers {

    public static String libraryRealm = "omakhelpersdb.realm";
    public Integer nextNotificationId;
    Context context;
    Realm realm;
    Realm anotherRealm;

    public RealmHelpers(Context context) {
        this.context = context;
        realm = getRealm(libraryRealm, context);
    }

    public void setAnotherRealm(Realm realm) {
        this.anotherRealm = realm;
    }

    /*Realm Configuration */
    public static Realm getRealm(String whichRealm, Context context) {
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(libraryRealm)
                .deleteRealmIfMigrationNeeded()
                .modules(new OmakHelpersModule())
                //.schemaVersion(1)
                .build();

        // Realm.setDefaultConfiguration(config);
        return Realm.getInstance(config);
    }

    public boolean getBooleanFlag(String flagName) {
        RealmQuery<RealmFlags> query = realm.where(RealmFlags.class).equalTo("key", flagName);
        RealmFlags realmFlags = query.findFirst();

        if (realmFlags != null) {
            return realmFlags.getBooleanValue();
        }

        return false;
    }

    public void setBooleanFlag(final String flagName, final boolean value) {
        RealmQuery<RealmFlags> query = realm.where(RealmFlags.class).equalTo("key", flagName);
        final RealmFlags isUpdatedFlag = query.findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isUpdatedFlag != null) {
                    isUpdatedFlag.setBooleanValue(value);
                } else {
                    final RealmFlags realmFlags = new RealmFlags();
                    realmFlags.setKey(flagName);
                    realmFlags.setBooleanValue(value);
                    realm.insertOrUpdate(realmFlags);
                }
            }
        });
    }

    public int getNotificationId(HashMap<String, String> notiData) {
        RealmNotificationModel.createAndInsert(context, notiData);
        realm.executeTransaction(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number currentIdNum = realm.where(RealmNotificationModel.class).max("id");
                        nextNotificationId = (currentIdNum == null) ? 1 : currentIdNum.intValue();
                    }
                });

        return nextNotificationId;
    }

    public int getNotificationId(notiData notiData) {
        RealmNotificationModel.createAndInsert(context, notiData);
        realm.executeTransaction(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number currentIdNum = realm.where(RealmNotificationModel.class).max("id");
                        nextNotificationId = (currentIdNum == null) ? 1 : currentIdNum.intValue();
                    }
                });

        return nextNotificationId;
    }

    public <T extends RealmObject> List<T> getNotifications(Class<T> tClass) {
        return realm.where(tClass).findAll();
    }

    // RealmHelpers for the App using the library
    public <T extends RealmObject> int getLatestId(String primaryKey, Class<T> tClass) {
        Number currentIdNum = anotherRealm.where(RealmNotificationModel.class).max(primaryKey);
        return (currentIdNum == null) ? 1 : currentIdNum.intValue();
    }

    public <T extends RealmObject> int getLatestId(Class<T> tClass) {
        Number currentIdNum = anotherRealm.where(RealmNotificationModel.class).max("id");
        return (currentIdNum == null) ? 1 : currentIdNum.intValue();
    }


    public <T extends RealmObject> T getSingleRowContains(String field, String value, Class<T> clazz) {
        RealmQuery query = anotherRealm.where(clazz);
        query.contains(field, value);
        RealmResults<T> foundRows = query.findAll();
        if (foundRows.size() > 0) {
            return foundRows.get(0);
        }

        return null;
    }

    public <T extends RealmObject> RealmResults<T> getOption(Class<T> clazz, String type, String value) {
        RealmQuery uniqueFieldQuery = anotherRealm.where(clazz);
        uniqueFieldQuery.equalTo(type, value).findAll();
        RealmResults<T> uniqueValueProjects = uniqueFieldQuery.findAll();
        return uniqueValueProjects;
    }

    /*
    param String field
    param Class<T> tClass
     */
    public <T extends RealmObject> RealmResults<T> getUniqueFieldValuesFromRealm(String field, Class<T> tClass) {
        RealmQuery uniqueFieldQuery = anotherRealm.where(tClass).distinct(field);
        uniqueFieldQuery.notEqualTo(field, "");
        RealmResults<T> uniqueValueProjects = uniqueFieldQuery.findAll();
        return uniqueValueProjects;
    }

    public <T extends RealmObject> RealmResults<T> getFromRealm(String field, String value, Class<T> tClass) {
        return anotherRealm.where(tClass).equalTo(field, value).findAll();
    }

    public <T extends RealmObject> RealmResults<T> getFromRealm(String field, Integer value, Class<T> tClass) {
        return anotherRealm.where(tClass).equalTo(field, value).findAll();
    }

    /**
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T extends RealmObject> RealmResults<T> getFromRealm(Class<T> tClass) {
        return anotherRealm.where(tClass).findAll();
    }

    /**
     *
     * @param field
     * @param value
     * @param tClass
     * @param <T>
     * @return
     */
    public <T extends RealmObject> T getSingleFromRealm(String field, String value, Class<T> tClass) {
        return anotherRealm.where(tClass).equalTo(field, value).findFirst();
    }

    /**
     *
     * @param field
     * @param value
     * @param tClass
     * @param <T>
     * @return
     */
    public <T extends RealmObject> T getSingleFromRealm(String field, Integer value, Class<T> tClass) {
        return anotherRealm.where(tClass).equalTo(field, value).findFirst();
    }

    /**
     *
     * @param field
     * @param value
     * @param tClass
     * @param <T>
     */
    public <T extends RealmObject> void deleteFromRealm(String field, Integer value, Class<T> tClass) {
        final RealmResults<T> foundRows = anotherRealm.where(tClass).equalTo(field, value).findAll();
        anotherRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    foundRows.deleteAllFromRealm();
                } catch (RealmPrimaryKeyConstraintException e) {
                    HelperFunctions.toaster(context, "Local DB Failure!");
                }
            }
        });
    }

    /**
     *
     * @param field
     * @param value
     * @param tClass
     * @param <T>
     */
    public <T extends RealmObject> void deleteFromRealm(String field, String value, Class<T> tClass) {
        final RealmResults<T> foundRows = anotherRealm.where(tClass).equalTo(field, value).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    foundRows.deleteAllFromRealm();
                } catch (RealmPrimaryKeyConstraintException e) {
                    HelperFunctions.toaster(context, "Local DB Failure!");
                }
            }
        });
    }

    /**
     *
     * @param tClass
     * @param <T>
     */
    public <T extends RealmObject> void deleteFromRealm(Class<T> tClass) {
        final RealmResults<T> foundRows = anotherRealm.where(tClass).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    foundRows.deleteAllFromRealm();
                } catch (RealmPrimaryKeyConstraintException e) {
                    HelperFunctions.toaster(context, "Local DB Failure!");
                }
            }
        });
    }
}
