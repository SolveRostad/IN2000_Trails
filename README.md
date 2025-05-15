# IN2000 - Software Engineering med Prosjektarbeid

Team 3 har gjennom prosjektet i emnet IN2000 - Software Engineering med prosjektarbeid ved Universitetet i Oslo utviklet TrAIls, en android applikasjon som ønsker å senke terskelen for å komme seg ut på tur. Den er utviklet for å hjelpe brukere med å finne og planlegge turer, både i nærområdet og i nye omgivelser, ved hjelp av kunstig intelligens (KI). TrAIls gjør det mulig for brukeren å finne og lagre turforslag, legge til egne notater og skape en personlig utfordring ved å konkurrere mot seg selv. En sentral funksjon i applikasjonen er bruken av KI for å finne turer (enten via anbefalte turer eller chatbot), samt få informasjon og tips til planlegging. En annen sentral del av applikasjonen er bruken av API-et til Meteorologisk institutt. Vi har benyttet værdata for å enklere finne de beste dagene for gode turopplevelser. Ved å bidra til at brukeren får mest mulig ut av sine turer, både med tanke på sikkerhet og opplevelse, underbygger vi målet vårt om å senke terskelen for å komme seg på tur.

### Teammedlemmer
* Kaoutar Abdellaoui (kaoutara)
* Lennard Rolstad Denby (lennarrd)
* Nora Liheim Alfstad (noralalf)
* Sondre Muri Indset (sondrein)
* Sølve Rostad (solveros)
* Zara Hope Adair (zjadair)

***

* [Dokumentasjon](#dokumentasjon)
* [Hvordan kjøre appen](#hvordan-kjøre-appen)
* [Biblioteker_og_rammeverk](#biblioteker-og-rammeverk)

***

## Dokumentasjon
Dokumentasjonen for turDB er tilgjengelig [her](http://turdb.info.gf:3000).

## Hvordan kjøre appen
For å kjøre appen må du ha Android Studio installert og tilgang på internett. Når dette er på plass kan du følge disse stegene:
1. Åpne terminalen
2. Naviger til der du ønsker å laste ned prosjektet på din pc/mac
3. Videre trenger du URL-en til prosjektet, denne finner du ved å trykke på den grønne "code"-knappen på github siden til prosjektet.
4. Kopier HTTPS URL-en. Skriv git clone etterfulgt av URL-en i terminalen og trykk enter. Det skal se slik ut: git clone https://github.uio.no/IN2000-V25/team-3.git
5. Når nedlastningene er ferdig åpner du Android Studio og trykker på "open"
6. Naviger til der du lagret prosjektet og velg den øverste prosjekmappen, og trykk "open"
7. Når Android Studio har lastet inn prosjektet trykker du på den grønne "run"-knappen for å kjøre appen. For at appen skal fungere er du avhengig av å dele din lokasjon, så når du starter appen må du velge "While using the app" eller "Only this time".

Om du heller ønsker å laste ned zip-filen til prosjeketet er dette også mulig når du klikker på den grønne "code"-knappen på github siden til prosjektet. Deretter pakker du ut ZIP-filen på din pc/mac før du videre kan følge de samme stegene fra punkt 5.

## Biblioteker og rammeverk
**Språk:** Kotlin, Java, **Byggverktøy:** Gradle

### API-er
MET(Meteorologisk Institutt) API-er:
- MET Location Forecast API: Brukes til å hente værdata for spesifikke geografiske koordinater. Det gir oss relevant værdata for den neste uken basert på koordinater, som er viktig for å kunne vise temperatur og data for de ulike turene.
- MET Alerts API: Brukes til å hente værvarsler og advarsler, som sterk vind og fare for skogbrann, basert på brukerens valgte lokasjon.

Mapbox...

TurDB...

OpenAI...

### Biblioteker
Coroutines er et kotlin-bibliotek som brukes for asynkron programmering. Det gjør at det er mulig å skrive kode som ser synkron ut, men som kjøres i bakgrunnen, og dermed ikke blokkerer UI-tråden. Dette fører til at det ikke oppstår forsinkelser og appen kan kjøre mer effektivt. Eksempler på asynkrone oppgaver er nettverkskall og databaseoperasjoner.

Coil (Coroutine Image Loader) er et kotlin-bibliotek som brukes for å laste inn bilder asynkront og caching. Det integreres godt med coroutines, og sørger for at bildes lastes inn raskt, selv med dårlig internettforbindelse.

Custom BottomSheet er en UI-komponent som kan trekkes opp fra bunnen av skjermen for å vise ekstra innhold. Vi har tilpasset denne menyen til å vise ulike turruter, enten AI-anbefalinger eller ruter hentet utifra brukerens gitte eller valgte lokasjon.

Ktor er et nettverksbibliotek med mange funksjoner for bygging av serverklienter, nettverksforespørsler (som et HTTP-kall) og integrere API-er. Til tross for at det er et stort bibliotek med mange muligheter, er det enkelt å bruke til å blant annet hente eller sende data over internett. Et eksempel på dette er å hente data fra TurDB.

Serialization er et bibliotek som brukes for serialisering og deserialisering av JSON-data. Dette gjør det enklere å jobbe med dataobjekter.

Room database er et android-bibliotek for lokal datalagring uavhengig av om applikasjonen er åpen eller ikke. Dette er en enkel og trygg måte å lagre og hente data lokalt, og vi bruker det for å lagre turruter som favoritter og i loggen hos hver enkelt bruker.