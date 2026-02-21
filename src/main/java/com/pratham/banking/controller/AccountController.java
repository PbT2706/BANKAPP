package com.pratham.banking.controller;

import com.pratham.banking.dto.ApiResponse;
import com.pratham.banking.dto.AccountResponse;
import com.pratham.banking.dto.CreateAccountRequest;
import com.pratham.banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for account operations.
 */
@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account APIs")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(
            summary = "Create account",
            description = "Creates a new bank account for an existing user with zero initial balance."
    )
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse accountResponse = accountService.createAccount(request);
        ApiResponse<AccountResponse> response = ApiResponse.<AccountResponse>builder()
                .success(true)
                .message("Account created successfully")
                .data(accountResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
