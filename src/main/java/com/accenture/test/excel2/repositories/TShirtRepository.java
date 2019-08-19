package com.accenture.test.excel2.repositories;

import com.accenture.test.excel2.model.TShirt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TShirtRepository extends JpaRepository<TShirt, String> {

    Page<TShirt>findAllBySizeDiff(String size, Pageable pageable);
}
