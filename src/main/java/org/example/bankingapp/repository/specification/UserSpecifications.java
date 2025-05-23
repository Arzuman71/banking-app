package org.example.bankingapp.repository.specification;

import org.example.bankingapp.model.EmailData;
import org.example.bankingapp.model.PhoneData;
import org.example.bankingapp.model.User;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;

public class UserSpecifications {

    public static Specification<User> nameLike(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<User> dateOfBirthAfter(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThan(root.get("dateOfBirth"), date);
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null) return null;
            Join<User, EmailData> emailJoin = root.join("emailData", JoinType.INNER);
            return cb.equal(emailJoin.get("email"), email);
        };
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null) return null;
            Join<User, PhoneData> phoneJoin = root.join("phoneData", JoinType.INNER);
            return cb.equal(phoneJoin.get("phone"), phone);
        };
    }

    public static Specification<User> withFilters(
            String name, String email, String phone, LocalDate dateOfBirth) {
        return Specification.where(nameLike(name))
                .and(hasEmail(email))
                .and(hasPhone(phone))
                .and(dateOfBirthAfter(dateOfBirth));
    }
}