package com.wow.delivery.repository;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

    default T findByIdOrThrow(ID id, ErrorCode errorCode, String errorMessage) {
        return findById(id)
            .orElseThrow(() -> {
                String message = (errorMessage != null) ? errorMessage : errorCode.getMessage();
                return new DataNotFoundException(errorCode, message);
            });
    }
}
