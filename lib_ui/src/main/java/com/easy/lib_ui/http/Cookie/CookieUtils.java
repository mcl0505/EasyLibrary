package com.easy.lib_ui.http.Cookie;

import com.easy.lib_util.app.EasyApplication;

import java.util.List;

import okhttp3.Cookie;

/**
 * 获取单例的cookie
 */
public class CookieUtils {
   private static PersistentCookieStore cookieStore;
    public static PersistentCookieStore GetCookie(){
        if(cookieStore == null){
            synchronized (PersistentCookieStore.class){
                if(cookieStore == null){
                    cookieStore = new PersistentCookieStore(EasyApplication.Companion.getInstance().getBaseContext());
                }
            }
        }
        return cookieStore;
    }

    public static void ClearCokie(){
        if(GetCookie() != null){
            List<Cookie> cookies = GetCookie().getCookies();
            cookies.clear();
        }
    }
}
