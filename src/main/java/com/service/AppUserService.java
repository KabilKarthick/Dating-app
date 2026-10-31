package com.service;

import com.dto.requestdto.AppUserReqDTO;
import com.dto.requestdto.LoginReqDTO;
import com.entity.AppUser;
import com.exception.AuthException;
import com.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUserReqDTO saveUser(AppUserReqDTO appUserReqDTO) {

        Optional<AppUser> existing = appUserRepository.findByEmail(appUserReqDTO.getEmail());
        if (existing.isPresent()) {
            throw new AuthException("Email is already registered");
        }

        AppUser appUser = new AppUser();

        appUser.setName(appUserReqDTO.getName());
        appUser.setEmail(appUserReqDTO.getEmail());
        appUser.setPassword(appUserReqDTO.getPassword());

        appUserRepository.save(appUser);

        return appUserReqDTO;
    }

    public AppUser loginUser(LoginReqDTO loginReqDTO) {

        AppUser appUser = appUserRepository.findByEmail(loginReqDTO.getEmail())
                .orElseThrow(() -> new AuthException("Invalid email or password"));

        if (!appUser.getPassword().equals(loginReqDTO.getPassword())) {
            throw new AuthException("Invalid email or password");
        }

        return appUser;
    }

    public Optional<AppUser> findUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public AppUser getUserById(Integer id) {
        return appUserRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public AppUser getUserByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public void connectUsers(Integer user1Id, Integer user2Id) {
        AppUser user1 = getUserById(user1Id);
        AppUser user2 = getUserById(user2Id);
        user1.setPartnerId(user2.getId());
        user2.setPartnerId(user1.getId());

        appUserRepository.save(user1);
        appUserRepository.save(user2);
    }
}