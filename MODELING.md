# Diagrammer
## Use case-diagram
Et use case-diagram viser et brukstilfelle, altså hvordan en aktør målrettet samhandler med systemet. Et use case har et større fokus på hvordan systemet fungerer, og dets funksjoner. Vi ønsker å bruke et use case-diagram for å vise hvordan en bruker kan interagere med applikasjonen vår. Det er en enkel og oversiktlig måte å vise funksjonalitet fra en brukers perspektiv, uten mengder med tekniske detaljer.
Diagrammet under illustrerer...

*bilde*

### Tekstlig beskrivelse av use case

## Sekvensdiagram
Et sekvensdiagram brukes for å illustrere hvordan objekter og/eller aktører interagerer med hverandre når brukeren utfører en spesifikk handling, for eksempel velge en turrute. Vi ønsket å bruke dette for å enkelt vise rekkefølgen av handlingene som skjer i applikasjonen, og hvordan de ulike delene av systemet samhandler. Diagrammet under illustrerer samhandlingen mellom bruker, applikasjon, API-er og databasen. 

```mermaid
sequenceDiagram
    participant Bruker
    participant App
    participant KartAPI
    participant LocationForecastAPI
    participant MetAlertsAPI
    participant OpenAI
    participant Database

    Bruker ->> App: Åpner appen
    App ->> Bruker: Viser startskjerm
    Bruker ->> App: Oppretter brukerprofil
    App ->> Database: Lagrer brukerprofil
    alt Bruker trykker på søkefelt
        Bruker ->> App: Skriver inn sted
    else Bruker trykker på kart
        Bruker ->> App: Trykker på kart
    else Bruker spør Chatbot om turrute
        Bruker ->> App: Trykker på Chatbot
        App ->> Bruker: Viser chatbot-skjermen
        Bruker ->> App: Skriver inn en melding
        App ->> OpenAI: Sender prompt fra bruker
        OpenAI ->> App: Sender en respons LLM
        App ->> Bruker: Viser responsen
    end
    App ->> KartAPI: Ber om ruter basert på lokasjon
    KartAPI ->> App: Returnerer ruter
    App ->> LocationForecastAPI: Ber om værdata for gitt lokasjon
    LocationForecastAPI ->> App: Returnerer værdata
    App ->> MetAlertsAPI: Ber om farevarsel-data for gitt lokasjon
    MetAlertsAPI ->> App: Returnerer farevarsel-data
    App ->> Bruker: Viser ruter basert på lokasjon
    Bruker ->> App: Velger rute
    App ->> OpenAI: Ber om rutebeskrivelse ut ifra ruten sin metadata
    OpenAI ->> App: Returnerer beskrivelsen
    App ->> Bruker: Viser informasjon om ruten
    opt Legge til ruten som favoritt
        Bruker ->> App: Legger til ruten som favoritt
        App ->> Database: Lagt til i database
    end
    opt Legge til ruten i loggen
        Bruker ->> App: Legger til ruten i loggen
        App ->> Database: Lagt til i database
        Bruker ->> App: Skriver notat til turen
        App ->> Database: Lagt til notat i database
    end
    alt Bruker trykker for å se været i dag
        Bruker ->> App: Trykker på været i dag
        App ->>  LocationForecastAPI: Ber om værdata basert på lokasjon
        LocationForecastAPI ->> App: Returnerer værdata
        App ->> Bruker: Viser værdata
    else Bruker trykker på "Se været andre dager"
        Bruker ->> App: Trykker på knappen
        App ->>  LocationForecastAPI: Ber om værdata basert på lokasjon
        LocationForecastAPI ->> App: Returnerer værdata for de neste syv dagene
        App ->> Bruker: Viser værdata for de neste syv dagene
        Bruker ->> App: Klikker på en dag
        App ->>  LocationForecastAPI: Ber om værdata (time for time) basert på valgt dag
        LocationForecastAPI ->> App: Returnerer værdata (time for time)
        App ->> Bruker: Viser værdata (time for time)
    end
```

## Klassediagram
Et klassediagram brukes for å illustrere systemets struktur, altså klasser og deres tilhørende metoder og egenskaper, samt hvordan de er koblet sammen gjennom for eksempel arv. Vi ønsket å bruke dette diagrammet for å definere klassene og beskrive relasjoner og avhengigheter mellom dem, samt forbedre forståelsen vår av systemets arkitektur. Diagrammet under viser oversikten over de implementerte klassene i prosjektet. Det illustrerer arv mellom klasser (hvem arver fra hvem), instansvariabler og deres typer og metoder (funksjoner) som er definert i hver klasse, inkludert arvede metoder.

```mermaid
classDiagram
    class ActivityRepository {
        +addLog(Activity)
        +deleteLog(Activity)
        +getAllLogs(String) List<Int>
        +adjustTimesWalked(String, Int, Int)
        +getTotalTimesWalked(String) Int
        +addNotesToLog(String, Int, String)
        +getNotesForHike(String, Int) String
        +getTimesWalkedForHike(String, Int) Int
    }

    class HikeScreenViewModel {
        -_hikeScreenUIState
        +hikeScreenUIState StateFlow<HikeScreenUIState>
        +updateHike(Feature)
        +updateRecommendedHikes(List<Feature>)
        +updateRecommendedHikesLoaded(Boolean)
        +getHikeDescription(HomeScreenViewModel, OpenAIViewModel)
        +updateDescriptionAlreadyLoaded(Boolean)
        +updateSelectedDay(String)
        +updateSelectedDate(String)
    }

    class HikeScreenUIState {
    }

    class FavoritesScreenViewModel {
        -_favoriteScreenUIState
        +favoriteScreenUIState StateFlow<FavoriteScreenUIState>
        +loadFavorites()
        +setUser()
        +updateUserLocationFromMapbox()
        +updateUserLocation(Point)
        +getAllFavorites(String) List<Int>
        +isHikeFavorite(Feature) Boolean
        +getAllConverteFavorites()
        +addFavorite(Int)
        +deleteFavorite(Int)
    }

    class OpenAIDatasource {
        -client
        -endpoint
        -apiKey
        -modelName
        -apiVersion
        +getCompletionsSamples(String) ChatCompletionsResponse
        +getCompletionsStream(String) Flow<String>
        -processJsonLine(String) String?
    }

    class LocationForecastDatasource {
        +getLocationForecast(Double, Double) Locationforecast?
    }

    class ProfileDatabase_Companion_DatabaseCallBack {
        +onCreate(SupportSQLiteDatabase)
        -populateDatabase()
    }

    class ProfileDatabase_Companion {
        -INSTANCE ProfileDatabase?
        +getDatabase(Context) ProfileDatabase
    }

    class ProfileDatabase {
        <<abstract>>
        +favoriteDao() FavoriteDao
        +profileDao() ProfileDao
        +logDao() ActivityDao
    }

    class HikeAPIDatasource {
        -ktorHttpClient
        +getHikes(Double, Double, Int, String, Int) List<Feature>
        +getHikesById(List<String>, Point) List<Feature>
    }

    class null_1 {
        +migrate(SupportSQLiteDatabase)
    }

    class null_2 {
        +migrate(SupportSQLiteDatabase)
    }

    class OpenAIViewModel {
        -openAIRepository
        -hikeAPIRepository
        -_openAIUIState
        +openAIUIState StateFlow<OpenAIUIState>
        -_conversationHistory
        +conversationHistory List<ChatbotMessage>
        +addUserMessage(String)
        +addBotMessage(String)
        +getCompletionsSamples(String, (String) -> Unit)
        +getCompletionsStream(String)
        +addFeature()
        +getChatbotResponse(String, HomeScreenViewModel)
        +getRecommendedHikes(HomeScreenViewModel, HikeScreenViewModel)
    }

    class Weekdays_Companion {
        +indexOf(String) Int
    }

    class Weekdays {
        <<enumeration>>
        Mandag
        Tirsdag
        Onsdag
        Torsdag
        Fredag
        Lørdag
        Søndag
    }

    class FavoriteRepository {
        +addFavorite(Favorite)
        +deleteFavorite(Favorite)
        +getAllFavorites(String) List<Int>
    }

    class MapboxViewModel {
        -placeAutocomplete
        -_mapboxUIState MutableStateFlow<MapboxUIState>
        +mapboxUIState StateFlow<MapboxUIState>
        +updateMapStyle(String)
        +updatePointerCoordinates(Point?)
        +resetShouldFetchHikes()
        +updateSearchQuery(String)
        +getSelectedSearchResultPoint(PlaceAutocompleteSuggestion)
        +setLoaderState(Boolean)
        +updatePolylineAnnotationsFromFeatures(List<Feature>)
        +clearPolylineAnnotations()
        +centerOnUserPosition()
        +updateLatestUserPosition(Point)
    }

    class MetAlertsDatasource {
        +getMetAlerts() MetAlerts?
    }

    class HomeScreenViewModel {
        -hikeAPIRepository
        -locationForecastRepository
        -metAlertsRepository
        -_homeScreenUIState
        +homeScreenUIState StateFlow<HomeScreenUIState>
        -_sheetStateTarget
        +sheetStateTarget
        -_currentSheetOffset
        +currentSheetOffset
        +markAanundDialogShown()
        +setSheetState(SheetDrawerDetent)
        +updateNetworkStatus(Boolean)
        +updateSheetOffset(Float)
        +fetchHikes(Double, Double, Int, String, Int)
        +fetchForecast(Point)
        +fetchAlerts()
        +clearHikes()
        +timeSeriesFromDate(String) List<TimeSeries>?
        +daysHighestTemp(String) Double
        +daysLowestTemp(String) Double
        +daysAverageWindSpeed(String) Double
    }

    class Screen_Welcome {
    }

    class Screen_Home {
    }

    class Screen_Favorites {
    }

    class Screen_HikeScreen {
    }

    class Screen_Chatbot {
    }

    class Screen_Profile {
    }

    class Screen_ProfileSelect {
    }

    class Screen_Settings {
    }

    class Screen_MapPreview {
    }

    class Screen_LocationForecast {
    }

    class Screen_LocationForecastDetailed {
    }

    class Screen {
    }

    class FavoriteDao {
        <<interface>>
        +saveFavorite(Favorite)
        +deleteFavorite(Favorite)
        +getAllFavorites(String) List<Int>
    }

    class OpenAIRepository {
        -openAIDatasource
        +getCompletionsSamples(String) ChatCompletionsResponse
        +getCompletionsStream(String) Flow<String>
    }

    class ActivityScreenViewModel {
        -_activityScreenUIState
        +activityScreenUIState StateFlow<ActivityScreenUIState>
        +loadActivities()
        +setUser()
        +updateUserLocationFromMapbox()
        +updateUserLocation(Point)
        +getConvertedActivities()
        +addToActivityLog(Int)
        +removeFromActivityLog(Int)
        +addNotesToActivityLog(Int, String)
        +adjustTimesWalked(Int, Int)
        +getNotesForHike(Int)
        +getTotalTimesWalked()
        +getTimesWalkedForHike(Int)
        +calculateTotalDistance()
    }

    class MainActivity {
        +onCreate(Bundle?)
    }

    class ActivityDao {
        <<interface>>
        +saveLog(Activity)
        +deleteLog(Activity)
        +getAllLogs(String) List<Int>
        +adjustTimesWalked(String, Int, Int)
        +addNotesToLog(String, Int, String)
        +getNotesForHike(String, Int) String
        +getTotalTimesWalked(String) Int
        +getTimesWalkedForHike(String, Int) Int
    }

    class ActivityScreenViewModelFactory {
        +create(Class<T>) T
    }

    class MetAlertsRepository {
        -datasource
        +getAlerts() MetAlerts?
    }

    class FavoritesScreenViewModelFactory {
        +create(Class<T>) T
    }

    class LocationForecastRepository {
        -datasource
        +getForecast(Double, Double) Locationforecast?
    }

    class ProfileScreenViewModel {
        -profileRepository ProfileRepository
        -_profileScreenUIState
        +profileScreenUIState StateFlow<ProfileScreenUIState>
        +addProfile(String)
        +deleteProfile(String)
        +selectProfile(String, () -> Unit)
        +setProfile()
        +getAllProfiles()
    }

    class SheetDrawerDetent {
        <<enumeration>>
        HIDDEN
        SEMIPEEK
        PEEK
        FULLYEXPANDED
    }

    class HikeAPIRepository {
        -hikeAPIDatasource
        -colorIndex
        -coroutineScope
        -generatedNames
        +getHikes(Double, Double, Int, String, Int) List<Feature>
        -generateAndSaveName(Feature, OpenAIViewModel)
        -getColor() Color
        -generateDifficulty(Feature)
        -getDifficultyInfo(String) DifficultyInfo
        +getHikesById(List<Int>, Point) List<Feature>
    }

    class ProfileDao {
        <<interface>>
        +insertUser(Profile)
        +deleteUser(Profile)
        +getAllUsers() List<Profile>
        +selectUser(String)
        +getSelectedUser() Profile?
        +getDefaultUser() Profile?
        +unselectUser()
        +clearAllUsers()
    }

    class ProfileRepository_Companion {
        -INSTANCE ProfileRepository?
        +getInstance(Context, CoroutineScope) ProfileRepository
    }

    class ProfileRepository {
        +addUser(Profile)
        +deleteProfile(Profile)
        +selectProfile(String)
        +unselectUser()
        +getSelectedUser() Profile
        +getAllUsers() List<Profile>
        +clearAllUsers()
    }

    HikeScreenViewModel --|> ViewModel
    FavoritesScreenViewModel --|> AndroidViewModel
    ProfileDatabase_Companion --> ProfileDatabase_Companion_DatabaseCallBack
    ProfileDatabase_Companion_DatabaseCallBack --|> Callback
    ProfileDatabase --> ProfileDatabase_Companion
    ProfileDatabase --|> RoomDatabase
    null_1 --|> Migration
    null_2 --|> Migration
    OpenAIViewModel --|> ViewModel
    Weekdays --> Weekdays_Companion
    MapboxViewModel --|> ViewModel
    HomeScreenViewModel --|> ViewModel
    Screen --> Screen_Welcome
    Screen_Welcome --|> Screen
    Screen --> Screen_Home
    Screen_Home --|> Screen
    Screen --> Screen_Favorites
    Screen_Favorites --|> Screen
    Screen --> Screen_HikeScreen
    Screen_HikeScreen --|> Screen
    Screen --> Screen_Chatbot
    Screen_Chatbot --|> Screen
    Screen --> Screen_Profile
    Screen_Profile --|> Screen
    Screen --> Screen_ProfileSelect
    Screen_ProfileSelect --|> Screen
    Screen --> Screen_Settings
    Screen_Settings --|> Screen
    Screen --> Screen_MapPreview
    Screen_MapPreview --|> Screen
    Screen --> Screen_LocationForecast
    Screen_LocationForecast --|> Screen
    Screen --> Screen_LocationForecastDetailed
    Screen_LocationForecastDetailed --|> Screen
    ActivityScreenViewModel --|> AndroidViewModel
    MainActivity --|> ComponentActivity
    ActivityScreenViewModelFactory ..|> Factory
    FavoritesScreenViewModelFactory ..|> Factory
    ProfileScreenViewModel --|> AndroidViewModel
    ProfileRepository --> ProfileRepository_Companion
```

## Aktivitetsdiagram
Et aktivitetsdiagram (eller flytdiagram) brukes for å illustrere flyten av aktiviteter og beslutninger i applikasjonen, og hvordan prosesser utføres i praksis. Siden det er flere ulike måter å utføre samme oppgave i applikasjonen vår, altså å finne en tur, ønsket vi å bruke dette diagrammet for å vise hvordan ulike valg og beslutninger påvirker flyten. Diagrammet hjelper oss med å eventuelt finne alterantive flyter, logiske feil eller manglende steg i prosessene. Diagrammet under viser en oversikt over hvordan en bruker kan navigere i applikasjonen, og hvilke beslutninger som kan tas underveis. Det viser også hvordan de ulike aktivitetene er koblet sammen og påvriker hverandre. 

```mermaid
flowchart TB; 
start((Start))
slutt(((slutt)))

startskjerm{{Startskjerm}}
lagBruker(Lag bruker)
velgBruker(Velg bruker)
finneRute{{Finne rute}}
trykkPaaKart(Velg lokasjon på kart)
soek(Bruk søkefunksjon)
chatbot(Finn rute ved hjelp av chat)
AIAnbefalinger(Bruker anbefalingene fra AI)
velgRute(Velg rute)
informasjonKort(Informasjon om valgt rute generert av AI)
informasjonKortEtterVaer(Informasjon om valgt rute for valgt dag generert av AI)

favoritt{{Vil du legge til rute som favoritt?}}
lagtTilFav(Lagt til som favoritt)
slettFav{{Vil du slette fra favoritt?}}
slettet(Slettet fra favoritt)
logg{{Vil du legge til rute i logg?}}
lagtTilLogg(Lagt til i loggen)
notat{{Vil du legge til notat i loggen?}}
notatLagtTil(Legger til notat i loggen)
vaer{{Vil du bruke været for å velge dag?}}
vaerDagens(Viser været for dagen i dag time for time)
vaerNesteUke(Viser været for de neste syv dagene)
vaerNesteUkeTime(Viser været timebasert for de neste syv dagene)

%% Style
style startskjerm fill: blue
style finneRute fill: blue
style logg fill: blue
style favoritt fill: blue
style notat fill: blue
style slettFav fill: blue
style vaer fill: blue

%% Relasjoner
start --> startskjerm
startskjerm --> lagBruker
startskjerm --deafult bruker--> velgBruker
lagBruker --> velgBruker
velgBruker --> finneRute
subgraph rutevalg [Valg av rute]
    finneRute --> trykkPaaKart --> velgRute
    finneRute --> soek --> velgRute
    finneRute --> chatbot --> velgRute
    finneRute --> AIAnbefalinger --> velgRute
end
velgRute --> informasjonKort

subgraph valgAvDag [Valg av dag]
    informasjonKort --> vaer
    vaer --ja, neste uken--> vaerNesteUke
    vaer --ja, dagens--> vaerDagens
    vaer --nei--> favoritt
    vaer --nei--> logg
    vaerDagens --> informasjonKortEtterVaer
    vaerNesteUke --timebasert--> vaerNesteUkeTime
    vaerNesteUkeTime --> informasjonKortEtterVaer
end
informasjonKortEtterVaer --> favoritt
favoritt --ja--> lagtTilFav
lagtTilFav --> slettFav
slettFav --ja--> slettet
slettet --> slutt
slettFav --nei--> slutt
favoritt --nei--> slutt

informasjonKortEtterVaer --> logg
logg --ja--> lagtTilLogg
lagtTilLogg --> notat
notat --ja--> notatLagtTil
notat --nei--> slutt
logg --nei--> slutt
notatLagtTil --nei--> slutt
```