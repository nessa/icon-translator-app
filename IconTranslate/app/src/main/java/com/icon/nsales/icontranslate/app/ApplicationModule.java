package com.icon.nsales.icontranslate.app;

import android.app.Application;
import android.content.Context;

import com.icon.nsales.icontranslate.services.ContextService;
import com.icon.nsales.icontranslate.services.DataService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nsales on 7/10/16.
 */
@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    DataService provideDataService() {
        return new DataService();
    }

    @Provides
    @Singleton
    ContextService provideContextService() {
        return new ContextService();
    }
}
