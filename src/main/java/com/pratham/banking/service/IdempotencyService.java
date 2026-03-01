package com.pratham.banking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratham.banking.entity.IdempotencyRecord;
import com.pratham.banking.exception.IdempotencyConflictException;
import com.pratham.banking.repository.IdempotencyRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * Service for managing idempotency records and ensuring retry-safe transfer operations.
 */
@Service
public class IdempotencyService {

    private static final long EXPIRATION_HOURS = 24;

    private final IdempotencyRepository idempotencyRepository;
    private final ObjectMapper objectMapper;

    public IdempotencyService(IdempotencyRepository idempotencyRepository, ObjectMapper objectMapper) {
        this.idempotencyRepository = idempotencyRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public <T> T execute(String key, String requestHash, Supplier<T> action) {
        IdempotencyRecord existingRecord = idempotencyRepository.findById(key).orElse(null);

        if (existingRecord != null) {
            LocalDateTime expirationThreshold = LocalDateTime.now().minusHours(EXPIRATION_HOURS);
            if (existingRecord.getCreatedAt().isBefore(expirationThreshold)) {
                idempotencyRepository.delete(existingRecord);
            } else {
                if (!existingRecord.getRequestHash().equals(requestHash)) {
                    throw new IdempotencyConflictException("Idempotency key reuse with different request");
                }

                return deserializeResponse(existingRecord.getResponseBody());
            }
        }

        T response = action.get();
        String responseBody = serializeResponse(response);

        IdempotencyRecord newRecord = IdempotencyRecord.builder()
                .key(key)
                .requestHash(requestHash)
                .responseBody(responseBody)
                .build();

        try {
            idempotencyRepository.save(newRecord);
        } catch (DataIntegrityViolationException ex) {
            IdempotencyRecord record = idempotencyRepository.findById(key)
                    .orElseThrow(() -> new IdempotencyConflictException("Idempotency key reuse with different request"));

            if (!record.getRequestHash().equals(requestHash)) {
                throw new IdempotencyConflictException("Idempotency key reuse with different request");
            }

            return deserializeResponse(record.getResponseBody());
        }

        return response;
    }

    private <T> String serializeResponse(T response) {
        try {
            CachedResponse cachedResponse = CachedResponse.builder()
                    .className(response.getClass().getName())
                    .payload(objectMapper.writeValueAsString(response))
                    .build();
            return objectMapper.writeValueAsString(cachedResponse);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize idempotency response", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T deserializeResponse(String responseBody) {
        try {
            CachedResponse cachedResponse = objectMapper.readValue(responseBody, CachedResponse.class);
            Class<?> responseClass = Class.forName(cachedResponse.getClassName());
            Object response = objectMapper.readValue(cachedResponse.getPayload(), responseClass);
            return (T) response;
        } catch (JsonProcessingException | ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize idempotency response", ex);
        }
    }

    @lombok.Getter
    @lombok.Setter
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    private static class CachedResponse {
        private String className;
        private String payload;
    }
}
