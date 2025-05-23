package org.example.bankingapp.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter

@Table(name = "email_data")
public class EmailData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String email;
    @OneToOne
    User user;

    public EmailData(String email, User user) {
        this.email = email;
        this.user = user;
    }

    public EmailData(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmailData{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", user=" + user +
                '}';
    }
}
