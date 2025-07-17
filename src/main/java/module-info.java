module javafx.play {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires atlantafx.base;
    requires fr.brouillard.oss.cssfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.codicons;
    requires org.kordamp.ikonli.fluentui;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.fxmisc.richtext;
    requires reactfx;
    requires org.fxmisc.flowless;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires jakarta.persistence;
    requires java.naming;
    requires jakarta.transaction;
    requires org.kordamp.ikonli.core;
    requires org.xerial.sqlitejdbc;
    requires com.google.gson;


    // ======= OPENS - Accesso alle Risorse =======
    opens configuration;
    opens css.component;
    opens css.util;
    opens css.view;
    opens fonts.jetbrainsmono;
    opens fonts.sfprodisplay;
    opens fxml;
    opens i18n.exercise;
    opens i18n.enums;
    opens i18n.error;
    opens i18n.ui;
    opens image.avatar;
    opens image.logo;
    opens exercise;


    // ======= OPENS - Accesso per Riflessione =======
    // Per Hibernate
    opens com.play.model.entity.user to org.hibernate.orm.core;
    opens com.play.model.entity.exercise to org.hibernate.orm.core;
    opens com.play.model.embedded to org.hibernate.orm.core;
    opens com.play.model.entity to org.hibernate.orm.core;

    // Per JavaFX (FXML)
    opens com.play to javafx.fxml;
    opens com.play.controllers.view to javafx.fxml;
    opens com.play.controllers.component to javafx.fxml;


    // ======= EXPORTS =======
    exports com.play;
    exports com.play.model.enums;
    exports com.play.util.provider;
    exports com.play.util.validation;
    exports com.play.model.entity.user;
    exports com.play.model.entity.exercise;
    exports com.play.model.embedded;
    exports com.play.factory;
    exports com.play.dto;
    exports com.play.dto.user;
    exports com.play.dto.exercise;
    exports com.play.dao.user;
    exports com.play.dao.exercise;
    exports com.play.service;
}