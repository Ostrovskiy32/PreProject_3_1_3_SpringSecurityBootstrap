package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class RoleRepositoriesHm implements RoleRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("select role from Role role", Role.class).getResultList();
    }

    @Override
    public Set<Role> findAllId(List<Long> ids) {
        String jpql = "SELECT r FROM Role r WHERE r.id IN :ids";
        return new HashSet<>(entityManager.createQuery(jpql, Role.class)
                .setParameter("ids", ids)
                .getResultList());
    }

    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Override
    public Role getById(Long id) {
        return entityManager.find(Role.class, id);
    }
}
