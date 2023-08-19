package com.example.repository;

import com.example.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {

    Optional<ImageModel> findByUserId(Long id);
    Optional<ImageModel> findByPostId(Long id);

}
