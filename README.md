# project-awesome-pizza

## Stack Tecnologico

Il progetto **Awesome Pizza** è una soluzione moderna per la gestione di una pizzeria, sviluppata in Java 21 e basata su Spring Boot 3.5.5. L'architettura è modulare e prevede due moduli principali:
- **brick**: il microservizio principale che espone le API REST per la gestione di pizze e ordini.
- **commons**: una libreria condivisa che contiene modelli, DTO e logica riutilizzabile, pensata per essere integrata facilmente in altri microservizi tramite Feign Client (che verranno integrati nella prossima iterazione).

### Motivazione delle scelte tecnologiche
- **Spring Boot**: per la rapidità di sviluppo, la robustezza e l'integrazione con l'ecosistema Spring.
- **Lombok**: riduce il boilerplate generando automaticamente getter, setter, costruttori, ecc.
- **MapStruct**: facilita il mapping tra entità e DTO in modo sicuro e performante, evitando errori manuali.
- **Spring Data JPA**: per la gestione della persistenza dei dati.
- **H2 Database**: database in-memory per test e sviluppo rapido.
- **Springdoc OpenAPI**: genera automaticamente la documentazione interattiva delle API.

## Librerie principali e motivazione
- **Lombok** (`org.projectlombok:lombok`): semplifica il codice eliminando la necessità di scrivere metodi ripetitivi.
- **MapStruct** (`org.mapstruct:mapstruct`): mapping automatico tra oggetti Java, configurato anche come annotation processor.
- **Springdoc OpenAPI** (`org.springdoc:springdoc-openapi-starter-webmvc-ui`): fornisce una UI Swagger per testare e documentare le API.
- **Jakarta Validation**: per la validazione dei dati in ingresso.
- **Spring Boot Starter Web/Data JPA/Test**: per REST, persistenza e testing.

## API esposte
Le API sono suddivise in due macro-aree:

### Gestione Pizze (`/api/pizzas`)
- **POST /api/pizzas**: crea una nuova pizza (uso interno).
- **GET /api/pizzas**: restituisce la lista di tutte le pizze disponibili (uso interno).
- **POST /api/pizzas/by-ids**: recupera pizze per lista di ID (body: array di ID).
- **PUT /api/pizzas/{pizzaId}**: aggiorna una pizza esistente.
- **DELETE /api/pizzas/{pizzaId}**: elimina una pizza.

### Gestione Ordini (`/api/orders`)
- **POST /api/orders**: crea un nuovo ordine. Body:
  ```json
  {
    "user": {
      "name": "Mario Rossi",
      "pickupFrom": "2025-10-01T12:00:00",
      "pickupTo": "2025-10-01T13:00:00",
      "userComment": "Potrei arrivare in ritardo"
    },
    "pizzas": [
      {
        "pizzaId": 1,
        "descriptionUser": "Senza basilico"
      }
    ]
  }
  ```
- **GET /api/orders**: recupera tutti gli ordini, con possibilità di filtri per stato (`statuses`) e data di ritiro (`pickupDate`).
- **GET /api/orders/{id}**: recupera un ordine per ID.
- **GET /api/orders/by-code?code=...**: recupera un ordine tramite codice.
- **PUT /api/orders/update?code=...**: aggiorna un ordine tramite codice. Body come sopra.
- **PUT /api/orders/update-status?code=...&status=...**: aggiorna lo stato di un ordine (solo pizzaiolo).
- **DELETE /api/orders?code=...&force=...**: elimina un ordine tramite codice, con opzione di forza per il pizzaiolo.

Tutte le API sono documentate tramite OpenAPI/Swagger e sono pensate per l'uso interno da parte del personale della pizzeria tramite portale.

## Modulo commons
Il modulo `commons` contiene:
- Modelli condivisi (es. `PizzaModel`, `OrderModel`, ecc.)
- DTO e classi di validazione
- Logica riutilizzabile e costanti

Questo modulo è già predisposto per essere integrato in altri microservizi, ad esempio tramite **Feign Client** (Spring Cloud OpenFeign), per favorire la scalabilità e la riusabilità del codice in un contesto di architettura a microservizi.

## Testing
Il progetto include una suite di test automatizzati, principalmente basati su **Spring Boot Test**. Sono presenti test per:
- Controller REST (verifica delle API e dei flussi principali)
- Service (logica di business)

I test sono suddivisi tra controller e service.

## Possibili evoluzioni: WebSocket e gestione live
Qualora venisse integrata una parte grafica per la gestione live degli ordini (ad esempio una dashboard per il personale), potrebbe essere utile introdurre l’utilizzo di **WebSocket**. Questo permetterebbe di:
- Aggiornare in tempo reale lo stato degli ordini
- Notificare la preparazione o il ritiro di una pizza
- Migliorare l’esperienza utente con aggiornamenti push

Spring Boot offre un supporto nativo per WebSocket, facilmente integrabile nel progetto.

## Come eseguire il progetto
1. Clonare il repository
2. Eseguire `mvn clean install` dalla root per compilare entrambi i moduli
3. Avviare il microservizio principale:
   ```
   cd brick
   mvn spring-boot:run
   ```
4. Accedere alla documentazione interattiva delle API su `http://localhost:8090/swagger-ui.html`

## Note future
- Il modulo `commons` potrà essere esteso con interfacce Feign Client per facilitare l'integrazione con altri microservizi.
- Possibile estensione verso database esterni e sistemi di autenticazione.
- Integrazione di WebSocket per la gestione live degli ordini.

---
