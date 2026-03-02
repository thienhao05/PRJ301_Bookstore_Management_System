package model;

import javax.persistence.EntityManager;
import utils.JPAUtil;

public class UserDAO {

    public UserDTO login(String email, String passwordHash) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            String jpql = "SELECT u FROM UserDTO u "
                    + "WHERE u.email = :email "
                    + "AND u.passwordHash = :passwordHash "
                    + "AND u.status = :status";

            return em.createQuery(jpql, UserDTO.class)
                    .setParameter("email", email)
                    .setParameter("passwordHash", passwordHash)
                    .setParameter("status", "ACTIVE")
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }
}
