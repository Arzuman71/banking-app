package org.example.bankingapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.bankingapp.dto.UserChangeDto;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    Account account;
    @OneToOne
    @JoinColumn(name = "id")
    private EmailData emailData;
    @OneToMany()
    @JoinColumn(name = "id")
    List<PhoneData> phoneData;

    public User(Long id) {
        this.id = id;
    }

    public void userChange(UserChangeDto userDto) {
        this.name = userDto.getName();
        this.dateOfBirth = LocalDate.parse(userDto.getDateOfBirth());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                ", account=" + account +
                ", emailData=" + emailData +
                ", phoneData=" + phoneData +
                '}';
    }
}
