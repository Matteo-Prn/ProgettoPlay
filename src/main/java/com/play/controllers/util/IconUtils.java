package com.play.controllers.util;

import com.play.model.enums.Category;
import org.kordamp.ikonli.Ikon;

import static org.kordamp.ikonli.materialdesign2.MaterialDesignA.ALERT;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignA.ALERT_CIRCLE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignB.BUG;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignC.*;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignE.EYE;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignI.INFORMATION;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PENCIL;
import static org.kordamp.ikonli.materialdesign2.MaterialDesignP.PENCIL_CIRCLE;

/**
 * Classe utility per gestire le icone nell'applicazione.
 */
public final class IconUtils {

    // Category Icons
    private static final Ikon ICON_CATEGORY_FIND_ERROR = BUG;
    private static final Ikon ICON_CATEGORY_OUTPUT = CODE_GREATER_THAN;
    private static final Ikon ICON_CATEGORY_SOLVE_CODE = CODE_JSON;

    // UI Icons
    private static final Ikon ICON_FORUM = PENCIL_CIRCLE;
    private static final Ikon ICON_RECAP = CARD_BULLETED;
    private static final Ikon ICON_PENCIL = PENCIL;
    private static final Ikon ICON_EYE = EYE;


    // Notification Icons
    private static final Ikon ICON_NOTIFICATION_ERROR = ALERT;
    private static final Ikon ICON_NOTIFICATION_SUCCESS = CHECK_CIRCLE;
    private static final Ikon ICON_NOTIFICATION_WARNING = ALERT_CIRCLE;
    private static final Ikon ICON_NOTIFICATION_INFO = INFORMATION;

    private IconUtils() {}

    /**
     * Restituisce l'icona associata alla categoria.
     *
     * @param category la categoria per cui si vuole l'icona
     * @return l'icona associata alla categoria
     */
    public static Ikon getIconForCategory(Category category) {
        return switch (category) {
            case FIND_ERROR -> ICON_CATEGORY_FIND_ERROR;
            case OUTPUT -> ICON_CATEGORY_OUTPUT;
            case SOLVE_CODE -> ICON_CATEGORY_SOLVE_CODE;
        };
    }

    public static Ikon getIconForum() {
        return ICON_FORUM;
    }

    public static Ikon getInconRecap() {
        return ICON_RECAP;
    }

    public static Ikon getIconPencil() {
        return ICON_PENCIL;
    }

    public static Ikon getIconEye() {
        return ICON_EYE;
    }

    /**
     * Restituisce l'icona associata al tipo di notifica.
     * @param type il tipo di notifica per cui si vuole l'icona
     * @return l'icona associata al tipo di notifica
     **/
    public static Ikon getIconForNotification(NotificationType type) {
        return switch (type) {
            case ERROR -> ICON_NOTIFICATION_ERROR;
            case WARNING -> ICON_NOTIFICATION_WARNING;
            case INFO -> ICON_NOTIFICATION_INFO;
            case SUCCESS -> ICON_NOTIFICATION_SUCCESS;
        };
    }
}
