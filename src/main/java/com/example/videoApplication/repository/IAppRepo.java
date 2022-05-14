package com.example.videoApplication.repository;

import com.example.videoApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppRepo extends JpaRepository<User, Integer> {
}
