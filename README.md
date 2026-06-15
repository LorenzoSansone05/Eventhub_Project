EventHub:



EventHub è una piattaforma backend e frontend per la gestione completa di eventi. L'applicazione permette a diversi tipi di utenti di interagire con il sistema in base al proprio ruolo:



L'utente non loggato (GUEST) puo solo vedere gli eventi, ma non prenotarsi a differenza dell'utente base.



Gli Utenti Standard (USER) possono consultare il catalogo degli eventi filtrandoli per data, tag, organizer email o location, prenotare biglietti (Standard o VIP), gestire le proprie prenotazioni e lasciare recensioni e punteggi (feedback) per gli eventi passati.



Gli Organizzatori (ORGANIZER) possono inserire nuovi eventi nel sistema, modificarli, cancellarli, visualizzare la lista dei partecipanti che hanno prenotato un biglietto per i loro eventi e visualizzare le recensioni di ognio singolo evento creato dal singolo organizer



Gli Amministratori (ADMIN) hanno il controllo totale sul sistema: gestiscono i ruoli degli utenti, possono applicare ban in caso di violazioni, moderano i feedback e gestiscono le anagrafiche dei tag, dei relatori (speaker) e delle location (venues).



Stack Tecnico Obbligatorio



Backend: Java 17+, Spring Boot 3.x, Spring Data JPA con Hibernate, Spring Security con autenticazione JDBC e password BCrypt, autorizzazione HTTP Basic con restrizione URL per ruolo, Maven per build e dipendenze, PostgreSQL in Docker, springdoc-openapi per Swagger UI, Bean Validation sulle DTO, Global Exception Handler.





Frontend: HTML5, CSS3, JavaScript ES6+, nessun framework o bundler, solo file statici HTML CSS JS.





Strumenti: Git, Docker e Docker Compose, Postman.

Prerequisiti

Java 17+, Maven, Docker, Git, Postman.





Configurazione database PostgreSQL: (CREDENZIALI FILE .env)

services:

&#x20; postgres:

&#x20;   image: postgres:16

&#x20;   container\_name: project\_postgres

&#x20;   restart: unless-stopped



&#x20;   env\_file:

&#x20;     - .env



&#x20;   environment:

&#x20;     POSTGRES\_USER: ${POSTGRES\_USER}

&#x20;     POSTGRES\_PASSWORD: ${POSTGRES\_PASSWORD}

&#x20;     POSTGRES\_DB: ${POSTGRES\_DB}



&#x20;   ports:

&#x20;     - "${POSTGRES\_PORT}:5432"



&#x20;   volumes:

&#x20;     - postgres\_data:/var/lib/postgresql/data



&#x20; adminer:

&#x20;   image: adminer

&#x20;   container\_name: project\_adminer

&#x20;   restart: unless-stopped



&#x20;   ports:

&#x20;     - "${ADMINER\_PORT}:8080"



&#x20;   depends\_on:

&#x20;     - postgres



volumes:

&#x20; postgres\_data:





Istruzioni di avvio dell'app:



1. Clonare il repository: git clone https://github.com/LorenzoSansone05/Eventhub\_Project.git , cd Eventhub\_Project



2\. Avviare il server PostgreSQL: docker compose up -d



3\. verificare l'avvio del servizio: docker ps

4. Compilare e avviare il BE: mvn clean install, mvn spring-boot:run (oppure farlo da IDE se si preferisce)



5\. Accedere alla documentazione per avere chiare le idee sull'app: http://localhost:8080/swagger-ui.html

6. Quando si vuole chiudere l'app bisogna anche fermare il servizio docker: docker-compose down




Screenshot dell'app:

!\[Schermata Login](eventhub/docs/screenshots/EventHub\_Login.png)



!\[Gestione Venues ADMIN](eventhub/docs/screenshots/Admin\_Venues.png)



!\[Gestione evento ORGANIZER](eventhub/docs/screenshots/Organizer\_Events.png)



!\[Ricerca eventi USER GUEST](eventhub/docs/screenshots/Search\_Events.png)



!\[Documentazione Swagger](eventhub/docs/screenshots/Swagger\_Documentation.png)





