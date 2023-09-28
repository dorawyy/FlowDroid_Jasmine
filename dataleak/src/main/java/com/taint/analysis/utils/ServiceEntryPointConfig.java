package com.taint.analysis.utils;

import soot.Scene;
import soot.SootMethod;

import java.util.ArrayList;

public class ServiceEntryPointConfig {

    public static ArrayList<SootMethod> getEntryPoints(BenchmarkName benchmark) {

        switch (benchmark) {
            case PIGGYMETRICS_ACCOUNT_SERVICE:
                return getEntryPointsFromPiggymetricsAccountService();
            default:
                return null;
        }

    }

    private static ArrayList<SootMethod> getEntryPointsFromPiggymetricsAccountService() {
        ArrayList<SootMethod> entryPoints = new ArrayList<SootMethod>();
        entryPoints.add(Scene.v().getSootClass("com.piggymetrics.account.controller.AccountController").getMethod("com.piggymetrics.account.domain.Account getAccountByName(java.lang.String)"));
        entryPoints.add(Scene.v().getSootClass("com.piggymetrics.account.controller.AccountController").getMethod("com.piggymetrics.account.domain.Account getCurrentAccount(java.security.Principal)"));
        entryPoints.add(Scene.v().getSootClass("com.piggymetrics.account.controller.AccountController").getMethod("void saveCurrentAccount(java.security.Principal,com.piggymetrics.account.domain.Account)"));
        entryPoints.add(Scene.v().getSootClass("com.piggymetrics.account.controller.AccountController").getMethod("com.piggymetrics.account.domain.Account createNewAccount(com.piggymetrics.account.domain.User)"));
        
        return entryPoints;
    }
}
