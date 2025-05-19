package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User findByName(String username) {
        return em.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :id", User.class)
                .setParameter("id", username)
                .getResultList().stream().findAny().orElse(null);
    }

    @Override
    public  void delete(Long id) {
        User us = em.find(User.class, id);
        em.remove(us);
    }

    @Override
    public void update(User us) {
        em.merge(us);
    }

    @Override
    public boolean add(User user) {
        em.persist(user);
        return true;
    }

    @Override
    public List<User> listUsers() {
        return em.createQuery("SELECT s FROM User s", User.class).getResultList();
    }

    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }
}

