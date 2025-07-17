package com.play.factory;

import com.play.dto.user.AdminDto;
import com.play.dto.user.StudentDto;
import com.play.model.entity.user.Admin;
import com.play.model.entity.user.Student;

/**
 * Factory per la creazione di entità utente ({@link Student}, {@link Admin}) dai rispettivi DTO.
 */
public final class UserFactory {

    private UserFactory() {}

    /**
     * Crea un'istanza di {@link Student} a partire da {@link StudentDto}.
     *
     * @param StudentDto DTO dello studente
     * @return istanza di {@link Student}
     */
    public static Student createStudent(StudentDto StudentDto) {
        return new Student.StudentBuilder()
                .setUsername(StudentDto.username())
                .setPassword(StudentDto.password())
                .setFirstName(StudentDto.firstName())
                .setLastName(StudentDto.lastName())
                .setLanguage(StudentDto.language())
                .build();
    }

    /**
     * Crea un'istanza di {@link Admin} a partire da {@link AdminDto}.
     *
     * @param AdminDto DTO dell'amministratore
     * @return istanza di {@link Admin}
     */
    public static Admin createAdmin(AdminDto AdminDto) {

        return new Admin.AdminBuilder()
                .setUsername(AdminDto.username())
                .setPassword(AdminDto.password())
                .setFirstName(AdminDto.firstName())
                .setLastName(AdminDto.lastName())
                .setLanguage(AdminDto.language())
                .build();
    }

}
