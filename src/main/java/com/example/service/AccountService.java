package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public boolean accountExist(int accountId){
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isPresent()){
            return true;
        } else {
            return false;
        }
    }

    public Account register(Account account) throws DuplicateUsernameException, ResourceNotFoundException {
        for(Account newAccount: accountRepository.findAll()){
            if(newAccount.getUsername().equals(account.getUsername())){
                throw new DuplicateUsernameException();
            }
        }
        if(account.getUsername().length() > 0 && account.getPassword().length() >= 4){
            accountRepository.save(account);
            return account;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public Account login(Account loginAccount) {
        for(Account account: accountRepository.findAll()){
            if(account.getUsername().equals(loginAccount.getUsername()) &&
             account.getPassword().equals(loginAccount.getPassword())){
                Account loginSuccessful = account;
                return loginSuccessful;
            }
        }
        return null;
    }

    
}
