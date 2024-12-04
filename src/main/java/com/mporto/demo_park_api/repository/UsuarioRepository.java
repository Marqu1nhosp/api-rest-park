package com.mporto.demo_park_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import com.mporto.demo_park_api.entity.Usuario;
import com.mporto.demo_park_api.entity.Usuario.Role;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    @Query("select u.role from Usuario u where u.username Like :username")
    Role findRoleByUsername(String username);
    
}
