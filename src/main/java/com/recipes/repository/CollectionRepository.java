package com.recipes.repository;

import com.recipes.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Collection entity
 */
@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Page<Collection> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT c FROM Collection c WHERE c.isPublic = true")
    Page<Collection> findPublicCollections(Pageable pageable);

    @Query("SELECT c FROM Collection c WHERE c.user.id = :userId OR c.isPublic = true")
    Page<Collection> findAccessibleCollections(Long userId, Pageable pageable);
}
