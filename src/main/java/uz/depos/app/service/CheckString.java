package uz.depos.app.service;

import org.springframework.stereotype.Service;

@Service
public class CheckString {

    public boolean check(final String s) {
        return s == null || s.trim().isEmpty();
    }
}
