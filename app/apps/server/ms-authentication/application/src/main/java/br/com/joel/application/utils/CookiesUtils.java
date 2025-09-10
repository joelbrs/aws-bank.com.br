package br.com.joel.application.utils;

import jakarta.servlet.http.Cookie;

import java.util.List;

public class CookiesUtils {
    public static Cookie generateHttpOnlyCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(Boolean.TRUE);
        cookie.setSecure(Boolean.FALSE); //TODO: activate on production
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static List<Cookie> clearCookies(List<String> cookieNames) {
        return cookieNames.stream()
                .map(cookieName -> CookiesUtils.generateHttpOnlyCookie(cookieName, "", 0))
                .toList();
    }
}
