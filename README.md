# IN2000 - Software Engineering med Prosjektarbeid

Team 3 har gjennom prosjektet i emnet [IN2000 - Software Engineering med prosjektarbeid](https://www.uio.no/studier/emner/matnat/ifi/IN2000/) ved Universitetet i Oslo utviklet TrAIls, en android applikasjon som ønsker å senke terskelen for å komme seg ut på tur. Den er utviklet for å hjelpe brukere med å finne og planlegge turer, både i nærområdet og i nye omgivelser, ved hjelp av kunstig intelligens (KI). TrAIls gjør det mulig for brukeren å finne og lagre turforslag, legge til egne notater og skape en personlig utfordring ved å konkurrere mot seg selv. En sentral funksjon i applikasjonen er bruken av KI for å finne turer (enten via anbefalte turer eller chatbot), samt få informasjon og tips til planlegging. En annen sentral del av applikasjonen er bruken av API-et til Meteorologisk institutt. Vi har benyttet værdata for å enklere finne de beste dagene for gode turopplevelser. Ved å bidra til at brukeren får mest mulig ut av sine turer, både med tanke på sikkerhet og opplevelse, underbygger vi målet vårt om å senke terskelen for å komme seg på tur.

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
* [Biblioteker og rammeverk](#biblioteker-og-rammeverk)

***


## Dokumentasjon
Dokumentasjonen for turDB er tilgjengelig [her](http://turdb.info.gf:3000).


## Hvordan kjøre appen
For å kjøre appen må du ha Android Studio installert og tilgang på internett. Når dette er på plass kan du følge disse stegene:
1. Åpne terminalen
2. Naviger til der du ønsker å laste ned prosjektet på din pc/mac
3. Videre trenger du URL-en til prosjektet, denne finner du ved å trykke på den grønne "code"-knappen på github siden til prosjektet.
4. Kopier HTTPS URL-en. Skriv git clone etterfulgt av URL-en i terminalen og trykk enter. Det skal se slik ut: 
   
   ```git clone https://github.uio.no/IN2000-V25/team-3.git```
5. Når nedlastningene er ferdig åpner du Android Studio og trykker på "open"
6. Naviger til der du lagret prosjektet og velg den øverste prosjekmappen, og trykk "open"
7. Når Android Studio har lastet inn prosjektet trykker du på den grønne "run"-knappen for å kjøre appen. Vi anbefaler å kjøre appen på API-nivå 36 fordi det har vært noen problemer med brukerposisjon på eldre API nivåer når applikasjonen kjøres i emulatoren. 
8. For at appen skal fungere er du avhengig av å dele din lokasjon, så når du starter appen må du velge "While using the app" eller "Only this time".

Om du heller ønsker å laste ned zip-filen til prosjeketet er dette også mulig når du klikker på den grønne "code"-knappen på github siden til prosjektet. Deretter pakker du ut ZIP-filen på din pc/mac før du videre kan følge de samme stegene fra punkt 5.


**MERK:** Vi har noen problemer med emulatorposisjonen der posisjonen til emulatoren først settes til et punkt i San Jose før den flyttes til riktig plassering. Dette er IKKE et problem på fysiske enheter, og oppstod kun etter oppdatering av Android Studio. Dette problemet er derimot løst dersom du kjører emulatoren på API-nivå 36.



## Biblioteker og rammeverk
**Språk:** Kotlin, **Brukergrensesnitt:** Jetpack Compose, **Byggverktøy:** Gradle

### API-er
**MET(Meteorologisk Institutt) API-er:**
- MET Location Forecast API: Brukes til å hente værdata for spesifikke geografiske koordinater. Det gir oss relevant værdata for den neste uken basert på disse koordinatene, som er viktig for å kunne vise temperatur og data for de ulike turene.
- MET Alerts API: Brukes til å hente værvarsler og advarsler, som sterk vind eller fare for skogbrann, basert på brukerens gitte eller valgte lokasjon.

**Mapbox API:** Det er et bibliotek som tilbyr kart og stedstjenester. Vi har brukt det til å hente kartdata og generere statiske kartbilder, søke etter steder (via Autocomplete) og visualisere turruter fra TurDB. Andre inkluderte funksjoner i Mapbox er lokasjon og sanntidssporing, altså at brukeren kan se hvor de befinner seg på kartet i sanntid. Kartvisningen gjøres med en Mapbox Composable, som håndterer API-integrasjonen uten behov for egen Datasource eller Repository. Mapbox sine innebygde LineStrings gir oss ikke den kontrollen vi ønsket, så vi valgte å heller vise turrutene som Polylines via PolylineGroup.

**TurDB REST-API:** API-et er basert på data fra Geonorge (65 000+ ruter). Det brukes for å effektivt hente informasjon om turstier basert på geografiske koordinater, og vise denne dataen på kartet. Rutene som returneres til applikasjonen er automatisk tildelt en farge og vanskelighetsgrad. På grunn av ytelsesproblemer i emulatoren ved lokal prosessering, satte vi opp en egen Node.js-server, som ga opptil 50 ganger raskere responstid, dersom vi ser bort i fra tiden for DNS lookup og pakke sendingen. API-et kan du se [her](http://turdb.info.gf:3000/) eller under [Dokumentasjon](#dokumentasjon).

**Open AI:** Under utviklingen har GPT-4o-modellen fra Azure OpenAI blitt brukt til å blant annet gi turrute anbefalinger til brukeren (både på forsiden og via chatbotten Ånund). Den har i tillegg blitt brukt til å generere informasjon om turrutene. OpenAI gir mer presise svar og kan håndtere flere tokens enn gratisversjonen av Gemini, som vi bruke mens vi ventet på tilgang til OpenAI.


### Biblioteker
**Coroutines** er et kotlin-bibliotek som brukes for asynkron programmering. Det gjør at det er mulig å skrive kode som ser synkron ut, men som kjøres i bakgrunnen, og dermed ikke blokkerer UI-tråden. Dette fører til at det ikke oppstår forsinkelser og appen kan kjøre mer effektivt. Eksempler på asynkrone oppgaver er nettverkskall og databaseoperasjoner.

**Coil (Coroutine Image Loader)** er et kotlin-bibliotek som brukes for å laste inn bilder asynkront og caching. Det integreres godt med coroutines, og sørger for at bildes lastes inn raskt, selv med dårlig internettforbindelse.

**Custom BottomSheet** er en UI-komponent som kan trekkes opp fra bunnen av skjermen for å vise ekstra innhold. Vi har tilpasset denne menyen til å vise ulike turruter, enten AI-anbefalinger eller ruter hentet utifra brukerens gitte eller valgte lokasjon.

**Ktor** er et nettverksbibliotek med mange funksjoner for bygging av serverklienter, nettverksforespørsler (som et HTTP-kall) og integrere API-er. Til tross for at det er et stort bibliotek med mange muligheter, er det enkelt å bruke til å blant annet hente eller sende data over internett. Et eksempel på dette er å hente data fra TurDB.

**Serialization** er et bibliotek som brukes for serialisering og deserialisering av JSON-data. Dette gjør det enklere å jobbe med dataobjekter.

**Room database** er et android-bibliotek for lokal datalagring uavhengig av om applikasjonen er åpen eller ikke. Dette er en enkel og trygg måte å lagre og hente data lokalt, og vi bruker det for å lagre turruter som favoritter og i loggen hos hver enkelt bruker.
