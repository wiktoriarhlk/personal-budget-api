# personal-budget-api

REST API do zarządzania budżetem osobistym. Umożliwia śledzenie przychodów i wydatków przypisanych do kont.

## Technologie

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA + Hibernate
- PostgreSQL
- Lombok
- Swagger / OpenAPI (springdoc)
- Maven

## Wymagania

- Java 17+
- PostgreSQL 14+
- Maven (lub użyj dołączonego `mvnw`)

## Konfiguracja bazy danych

1. Zainstaluj PostgreSQL
2. Stwórz bazę danych:
```sql
CREATE DATABASE budget_db;
```
3. Ustaw zmienną środowiskową z hasłem do PostgreSQL:

**Windows (PowerShell):**
```powershell
$env:DB_PASSWORD="twoje_haslo"
```

**Linux/Mac:**
```bash
export DB_PASSWORD=twoje_haslo
```

W IntelliJ: `Edit Configurations` → `Environment variables` → dodaj `DB_PASSWORD=twoje_haslo`

## Uruchomienie

```bash
git clone https://github.com/wiktoriarhlk/personal-budget-api.git
cd personal-budget-api
./mvnw spring-boot:run
```

Na Windowsie:
```bash
mvnw.cmd spring-boot:run
```

Aplikacja uruchomi się na porcie `8080`.

## Dokumentacja API

Po uruchomieniu dostępna pod:

```
http://localhost:8080/swagger-ui.html
```

## Endpointy

### Konta

| Metoda | URL | Opis |
|--------|-----|------|
| GET | `/accounts` | Lista wszystkich kont |
| POST | `/accounts` | Utwórz nowe konto |
| GET | `/accounts/{id}` | Szczegóły konta z saldem |
| DELETE | `/accounts/{id}` | Usuń konto (tylko bez transakcji) |
| GET | `/accounts/{id}/summary` | Podsumowanie przychodów i wydatków |

### Transakcje

| Metoda | URL | Opis |
|--------|-----|------|
| GET | `/accounts/{id}/transactions` | Lista transakcji (filtry: `?from=`, `?to=`, `?category=`) |
| POST | `/accounts/{id}/transactions` | Dodaj transakcję (saldo aktualizuje się automatycznie) |
| DELETE | `/accounts/{id}/transactions/{txId}` | Usuń transakcję (saldo cofa się) |
| GET | `/accounts/{id}/transactions/export` | Eksport transakcji do CSV |

## Przykłady użycia

### Utwórz konto

```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"name": "Konto główne"}'
```

### Dodaj transakcję

```bash
curl -X POST http://localhost:8080/accounts/1/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 3000,
    "type": "INCOME",
    "category": "Wynagrodzenie",
    "description": "Pensja styczeń",
    "date": "2024-01-01"
  }'
```

### Pobierz podsumowanie

```bash
curl http://localhost:8080/accounts/1/summary
```

## Kody HTTP

| Kod | Znaczenie |
|-----|-----------|
| 200 | OK |
| 201 | Zasób utworzony |
| 204 | Usunięto |
| 400 | Błąd walidacji danych wejściowych |
| 404 | Zasób nie istnieje |
| 409 | Konflikt (np. próba usunięcia konta z transakcjami) |

## Testy

```bash
./mvnw test
```