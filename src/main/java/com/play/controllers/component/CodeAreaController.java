package com.play.controllers.component;

import com.play.controllers.util.SyntaxCode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller per il componente {@link CodeArea} di RichTextFX.
 * Gestisce la visualizzazione del codice, inclusa l'evidenziazione della sintassi Java,
 * la numerazione delle linee e la formattazione di base.
 */
public class CodeAreaController  implements Initializable {

    @FXML
    private CodeArea codeArea;

    /**
     * Inizializza il controller.
     * Carica il foglio di stile CSS per l'evidenziazione della sintassi e
     * imposta la factory per visualizzare i numeri di riga.
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String cssPath = getClass().getResource("/css/util/java-keywords.css").toExternalForm();
        if (!codeArea.getStylesheets().contains(cssPath)) {
            codeArea.getStylesheets().add(cssPath);
        }
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    }


    public CodeArea getCodeArea() {
        return codeArea;
    }

    /**
     * Abilita l'evidenziazione della sintassi per il codice Java.
     * Formatta il codice, applica gli stili CSS, rende l'area non editabile
     * e imposta un listener per aggiornare l'evidenziazione al cambio del testo.
     */
    public void enableHighlighting() {

        formatJavaCode();

        // Se il foglio di stile è già caricato, puliamo e riapplichiamo gli stili
        codeArea.clearStyle(0, codeArea.getLength());

        // Riapplica il foglio di stile
        String cssPath = getClass().getResource("/css/util/java-keywords.css").toExternalForm();

        // Rimuove i fogli di stile esistenti per evitare duplicati
        codeArea.getStylesheets().removeIf(sheet -> sheet.contains("java-keywords.css"));

        // Aggiunge il foglio di stile
        codeArea.getStylesheets().add(cssPath);

        // Applica l'evidenziazione
        SyntaxCode.applyHighlighting(codeArea);

        // Forza un refresh della vista
        codeArea.requestFocus();
        codeArea.setEditable(false);


        // Osserva i cambiamenti nel testo per riapplicare gli stili
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            SyntaxCode.applyHighlighting(codeArea);
        });
    }

    /**
     * Disabilita l'evidenziazione della sintassi.
     * Rimuove tutti gli stili applicati e rende l'area di testo nuovamente editabile.
     */
    public void disableHighlighting() {
        codeArea.clearStyle(0, codeArea.getLength());
        codeArea.setEditable(true);
    }

    /**
     * Formatta il codice Java presente nell'area di testo.
     * Applica un'indentazione di base e riapplica l'evidenziazione.
     */
    public void formatJavaCode() {
        // Recupera il testo attuale
        String code = codeArea.getText();

        // Ottiene il testo formattato
        String formattedCode = formatCode(code);

        // Applica il testo formattato
        codeArea.replaceText(formattedCode);

        // Riapplica l'evidenziazione
        SyntaxCode.applyHighlighting(codeArea);
    }

    /**
     * Algoritmo di base per formattare una stringa di codice con indentazione.
     *
     * @param code Il codice sorgente da formattare.
     * @return Il codice formattato con indentazione.
     */
    private String formatCode(String code) {
        StringBuilder formatted = new StringBuilder();
        String[] lines = code.split("\n");
        int indentLevel = 0;
        boolean inComment = false;

        for (String line : lines) {
            // Rimuovi gli spazi all'inizio e alla fine ma preserva una copia originale
            String originalLine = line;
            String trimmedLine = line.trim();

            // Gestisci i commenti multilinea
            if (trimmedLine.contains("/*") && !trimmedLine.contains("*/")) {
                inComment = true;
            }

            if (trimmedLine.contains("*/")) {
                inComment = false;
            }

            // Controlla se la linea chiude un blocco
            if (trimmedLine.startsWith("}") || trimmedLine.startsWith(")") || trimmedLine.startsWith("]")) {
                indentLevel = Math.max(0, indentLevel - 1);
            }

            // Aggiungi l'indentazione corretta e preserva eventuali spazi dopo l'indentazione
            if (!trimmedLine.isEmpty()) {
                if (inComment) {
                    // Mantieni l'indentazione per i commenti multilinea
                    String indent = "    ".repeat(indentLevel);
                    if (trimmedLine.startsWith("*")) {
                        // Per le linee di commento che iniziano con * aggiungi uno spazio
                        formatted.append(indent).append(" * ").append(trimmedLine.substring(1).trim()).append("\n");
                    } else {
                        formatted.append(indent).append(trimmedLine).append("\n");
                    }
                } else {
                    // Formatta normalmente per il codice
                    formatted.append("    ".repeat(indentLevel)).append(trimmedLine).append("\n");
                }
            } else {
                // Mantieni le linee vuote
                formatted.append("\n");
            }

            // Controlla se la linea apre un nuovo blocco
            if ((trimmedLine.endsWith("{") || trimmedLine.endsWith("(") || trimmedLine.endsWith("[")) && !trimmedLine.contains("//") && !inComment) {
                indentLevel++;
            }
        }

        return formatted.toString();
    }

    /**
     * Imposta il testo nell'area del codice.
     *
     * @param text Il testo da visualizzare.
     */
    public void setText(String text) {
        codeArea.replaceText(text);
    }

    /**
     * Restituisce il testo corrente dall'area del codice.
     *
     * @return Il testo contenuto nell'area.
     */
    public String getText() {
        return codeArea.getText();
    }

    /**
     * Imposta lo stato di sola lettura per l'area del codice.
     *
     * @param readOnly true per rendere l'area non editabile, false altrimenti.
     */
    public void setReadOnly(boolean readOnly) {
        codeArea.setEditable(!readOnly);
    }
}
