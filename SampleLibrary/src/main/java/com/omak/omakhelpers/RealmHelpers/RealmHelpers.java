package com.omak.omakhelpers.RealmHelpers;

import android.content.Context;

import com.omak.omakhelpers.HelperFunctions;
import com.omak.omakhelpers.firebaseNotification.RealmNotificationModel;
import com.omak.omakhelpers.firebaseNotification.notiData;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.RealmModule;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class RealmHelpers {

    public static String libraryRealm = "omakhelpersdb.realm";
    public Integer nextNotificationId;
    Context context;
    Realm realm;

    public RealmHelpers(Context context) {
        this.context = context;
        realm = getRealm(libraryRealm, context);
    }

    public RealmHelpers(Context context, String realmName) {
        this.context = context;
        realm = getRealm(realmName, context);
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

    public <T extends RealmObject> RealmResults<T> getUniqueFieldValuesFromRealm(String field, Class<T> clazz) {
        RealmQuery uniqueFieldQuery = realm.where(clazz).distinct(field);
        uniqueFieldQuery.notEqualTo(field, "");
        RealmResults<T> uniqueValueProjects = uniqueFieldQuery.findAll();
        return uniqueValueProjects;
    }


    public <T extends RealmObject> RealmResults<T> getFromRealm(String field, String value, Class<T> clazz) {
        return realm.where(clazz).equalTo(field, value).findAll();
    }

    public <T extends RealmObject> RealmResults<T> getFromRealm(String field, Integer value, Class<T> clazz) {
        return realm.where(clazz).equalTo(field, value).findAll();
    }

    public <T extends RealmObject> RealmResults<T> getFromRealm(Class<T> clazz) {
        return realm.where(clazz).findAll();
    }

    public <T extends RealmObject> T getSingleFromRealm(String field, String value, Class<T> clazz) {
        return realm.where(clazz).equalTo(field, value).findFirst();
    }

    public <T extends RealmObject> T getSingleFromRealm(String field, Integer value, Class<T> clazz) {
        return realm.where(clazz).equalTo(field, value).findFirst();
    }

    public <T extends RealmObject> void deleteFromRealm(String field, Integer value, Class<T> clazz) {
        final RealmResults<T> foundRows = realm.where(clazz).equalTo(field, value).findAll();
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

    public <T extends RealmObject> void deleteFromRealm(String field, String value, Class<T> clazz) {
        final RealmResults<T> foundRows = realm.where(clazz).equalTo(field, value).findAll();
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
