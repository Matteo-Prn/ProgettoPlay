# Play - Applicazione Educativa JavaFX

Play è un'applicazione desktop sviluppata in JavaFX,
pensata per aiutare gli utenti a imparare e fare pratica con i fondamenti della programmazione Java attraverso una serie di esercizi interattivi. 
L'applicazione offre due profili utente (_Admin_ e _Studente_), ognuno con le proprie funzionalità dedicate.


### Prerequisiti

Prima di iniziare, assicurati di avere installato sul tuo sistema:


* **Java** Development Kit (JDK): Versione 22 o superiore.
* **Maven**: Per la gestione delle dipendenze e la compilazione del progetto. 

### Guida all'Avvio

Segui questi passaggi per compilare ed eseguire l'applicazione.
#### 1. Costruire il Progetto

Usa Maven per compilare il codice sorgente e creare un file JAR eseguibile che include tutte le dipendenze necessarie.

Esegui il seguente comando dalla directory principale del progetto:

```bash
mvn clean install
```

* **clean**: Pulisce la directory target da build precedenti.
* **install**: Compila il codice, esegue i test e crea il file javafx-play-1.0-SNAPSHOT.jar nella directory target.

### Eseguire l'Applicazione

#### Esecuzione da Terminale

Per eseguire l'applicazione, utilizza il comando Maven seguente dalla directory principale del progetto:

```bash
mvn javafx:run
```

### Nota sul Database

L'applicazione utilizza un database **_SQLite_** (**database**/**_play-javafx.db_**). 

Al primo avvio, il database e le tabelle necessarie vengono creati automaticamente. Gli esercizi di default, letti dal file default_exercises.json, vengono inseriti nel database solo se non sono già presenti.

### Screenshot dell'Applicazione

#### Login (Studente)
![Login - Studente](screen-app/login.png)
### Errori Login (Studente)
![Errori Login - Studente](screen-app/error-login-student.png)
#### Loading (Studente)
![login.png](screen-app/login.png)
#### Homepage (Studente)
![Homepage - Studente](screen-app/homepagestudent.png)
#### Esecizi (Studente)
![Esecizi - Studente](screen-app/exercise-student.png)
#### Esercizi Filitri (Studente)
![exercise-student-filter.png](screen-app/exercise-student-filter.png)
#### Esecuzione Esercizio (Studente)
![esercuzione-esercizi.png](screen-app/esercuzione-esercizi.png)
#### Abbandono Esercizio (Studente)
![abbandono-esercizio.png](screen-app/abbandono-esercizio.png)
#### Risultato Esercizio (Studente)
![risultato-esercizio.png](screen-app/risultato-esercizio.png)
#### Riepilogo Esercizi (Studente)
![dettagli-recap-esercizio.png](screen-app/dettagli-recap-esercizio.png)
#### Statistiche (Studente)
![stats-student.png](screen-app/stats-student.png)
#### Classfica (Studente)
![rank-student.png](screen-app/rank-student.png)



#### Login (Admin)
![login-admin.png](screen-app/login-admin.png)
#### Homepage (Admin)
![homepage-admin.png](screen-app/homepage-admin.png)
#### Creazione Esercizio (Admin)
![create-admin.png](screen-app/create-admin.png)
#### Errori Creazione Esercizio (Admin)
![error-create.png](screen-app/error-create.png)
#### Creazione Domande
![create-question.png](screen-app/create-question.png)
![create-question2.png](screen-app/create-question2.png)
#### Riepilogo Esercizi (Admin)
#### Esercizi (Admin)
![exercise-admin.png](screen-app/exercise-admin.png)
#### Elimina Esercizio (Admin)
![exercise-delete-admin.png](screen-app/exercise-delete-admin.png)
#### Statistiche (Admin)
![stats-admin.png](screen-app/stats-admin.png)




