package com.starapps.apexnew.model.interactor;


import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.starapps.apexnew.ApexApplication;
import com.starapps.apexnew.AppConstants;
import com.starapps.apexnew.entity.body.SignInBody;
import com.starapps.apexnew.entity.response.UserAuth;
import com.starapps.apexnew.model.repository.SignInRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SignInInteractor {

    @Inject
    public SignInRepository signInRepository;


    public SignInInteractor(SignInRepository signInRepository) {
        ApexApplication.getAppComponent().inject(this);
        this.signInRepository = signInRepository;
    }


    public Observable<UserAuth> signIn(SignInBody signInBody, Activity context) {
        return signInRepository.signInFireBase(signInBody, context)
            .doOnNext(task -> signInBody.setFirebaseToken(FirebaseInstanceId.getInstance().getToken()))
            .doOnNext(task -> signInBody.setPassword(AppConstants.DEFAULT_PASSWORD))
            .flatMap(task -> signInRepository.signInWithEmail(signInBody));
    }
}
