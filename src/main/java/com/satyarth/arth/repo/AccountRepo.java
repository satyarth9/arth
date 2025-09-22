package com.satyarth.arth.repo;

import com.satyarth.arth.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {
}
