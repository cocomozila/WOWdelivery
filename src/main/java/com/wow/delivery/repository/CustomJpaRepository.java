package com.wow.delivery.repository;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

    default T getById(ID id) {
        return findById(id)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "일치하는 데이터를 찾을 수 없습니다."));
    }
}
