# AI_NOTES.md

Podczas pracy nad projektem korzystałam z Claude (Anthropic) jako narzędzia wspomagającego.

## Do czego używałam AI

### Scaffolding i struktura projektu
AI pomogło mi skonfigurować projekt Spring Boot i zaplanować strukturę pakietów (`account`, `transaction`, `exception`).

### Generowanie kodu
AI generowało szkielety klas (encje, repozytoria, serwisy, kontrolery) które następnie analizowałam i rozumiałam przed użyciem. Każdy wygenerowany fragment kodu był przeze mnie czytany i omówiony z AI w celu zrozumienia.

### Wyjaśnienia konceptów
AI tłumaczyło mi:
- architekturę warstwową (Controller → Service → Repository)
- działanie adnotacji JPA (`@Entity`, `@ManyToOne`, `@OneToMany`)
- różnicę między `@Transactional` a zwykłymi metodami
- jak działa walidacja (`@Valid`, `@NotBlank`, `@DecimalMin`)
- jak Spring Data JPA generuje zapytania SQL z nazw metod (query methods)SQL z nazw metod (query methods)