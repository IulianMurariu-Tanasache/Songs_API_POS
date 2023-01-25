----------------------------------------------------------

# Prezentare  

Proiectul prezinta pe partea de back-end cam toate functionalitatile cerute, precum toate operatiile CRUD pe serviciile REST (artists, songs, userdata, playlists), cu HATEOAS si cu toate filtrele ca si query params, si toate CRUD pentru SOAP(IDM). Serviciile de artists si songs au fost separate si s-a adaugat in plus un serviciu de date personale ale fiecarui utilizator. S-a folosit bazele de date MySQL(artists, songs, idm) si Mongo(playlists, userdata). MySQL are un [script de initializare](ops/scripts/init.sql) cand nu exista deja un volum creat de container.

MySQL  
![imagine](screenshots/MySQL_Structure.png)  
  
Mongo  
![imagine](screenshots/mongo_structure.png)  

Toate cererile trec printr-un serviciu tip Gateway care este responsabil de a autentifica si autoriza un user(folosind JWT-uri si serviciul de IDM). Daca user-ul are dreptul sa faca respectiva cerere, ea va fi trimisa la serviciul corespunzator care va intoarce un rezultat la apelant.  

Roluri:  
![imagine](screenshots/roles.png)  

Exemplu log-uri Get song by id:  

![imagine](screenshots/get-song-id-logs.png)  

Toate serviciile ruleaza in containere de docker. Imaginile pentru servicii au fost create folosind plugin-ul de docker din [pom](pom.xml) si apoi a fost creat un [docker-compose](ops/docker-compose.yml).  

![imagine](screenshots/docker.png)  

Se observa si un container de prometheus si unul de alertmanager. Prometheus are rolul de a prelua metrici de la servicii si de a verifica daca inca sunt active. Cand unul din servicii este offline, se va trimite pe mail o alerta cu serviciul care a picat. [Configurari prometheus](ops/prometheus).

Prometheus:  
![imagine](screenshots/prometheus-all.png)  

Prometheus, serviciu picat:  
![imagine](screenshots/prometheus-down.png)  

Prometheus, alerta:  
![imagine](screenshots/prometheus-alert.png)  

Alerta in gmail:  
![imagine](screenshots/mail-alert.png)  

Un user trebuie prima oara sa se logheze pentru a obtine un jwt, pe care il va folosi intr-un header "Authorization: Bearer _jwt_" la toate request-urile viitoare pentru a avea acces, altfel va primi ca raspuns 403 (_Forbidden_). Poate face logout oricand, invalidand token-ul.  

Login:  
![imagine](screenshots/login.png)  

Invalid/No JWT/Permisiuni incorecte pentru request:  
![imagine](screenshots/forbidden-delete-user.png)  

Logout:  
![imagine](screenshots/logout.png)  

Dupa logout(acelasi token):  
![imagine](screenshots/forbidden-logout.png)  

Avand un jwt, se pot face request-uri la gateway, care va face forward la serviciul potrivit (daca are permisiuni). Pentru a vedea interfata unui serviciu, se poate obtine fisierul de open-api la adresa /api/docs sau se poate vedea interfata swagger.  

Open-API:  
![imagine](screenshots/api-docs.png)  

Swagger:  
![imagine](screenshots/swagger.png)  

Exemple request-uri REST(+ HATEOAS):  

Get songs:
![imagine](screenshots/getallsongs.png)  
Get song by id:  
![imagine](screenshots/get-song-id.png)  
Post song:
![imagine](screenshots/post-song.png)  
Put song - create:  
![imagine](screenshots/put-song-201-create.png)  
Delete song:
![imagine](screenshots/delete-song.png)  
Get songs of artist:  
![imagine](screenshots/get-songs-of-artist.png)  
Paginare:  
![imagine](screenshots/pagination.png)  
Filtru gen muzical pe songs:  
![imagine](screenshots/filter-songs-metal.png) 
Put song - update:  
![imagine](screenshots/put-song-204-exist.png)  
Filtru gen muzical pe songs:  
![imagine](screenshots/filter-songs-metal.png)  
Filtru nume pe songs:  
![imagine](screenshots/filter-song-name-non-exact.png) 
Filtru nume EXACT pe songs:  
![imagine](screenshots/filter-song-name-exact.png) 
Filtrul de nume pentru serviciul de userdata nu tine cont daca numele e exact sau de ordinea de First Name si Last Name:  
![imagine](screenshots/filter-name-1-userdata.png)  
![imagine](screenshots/filter-name-2-userdata.png)  

Serviciul IDM este un serviciu SOAP care se ocupa de JWT-uri (creare, verificare) si de users(CRUD). A fost integrat si el in Gateway, care expune un API(un wrapper practic) de tip REST pentru a facilita utilizarea sa.  

WSDL(la adresa: localhost:8001/soapUserService/users.wsdl):  
![imagine](screenshots/wsdl.png)  
Validate token in IDM:  
![imagine](screenshots/soap-jwt-auth.png)  
REST get user by id prin Gateway:  
![imagine](screenshots/rest-idm-get-user.png)  

Gateway-ul are rolul de a trimite jwt-ul la IDM pentru a fi verificat si dezpachetat. Daca este valid, se primeste id-ul user-ului si rolurile sale. Apoi sa verifica daca are permisiuni acel user pntru respectiva cerere facuta sau daca lucreaza pe un obiect propriu(De exemplu, daca vrea sa isi modifice propriile date personale, nu se pot modifica datele altcuiva). S-a folosit Spring Security pentru asta.  

Baza de date pt token. Se verifica daca token-ul este in BD, altfel este invalid(de exemplu, la logout, se sterge din bd facand-ul invalid):  
![imagine](screenshots/tokens-db.png)  
Forbidden cand se modifica datele altui user:  
User id: 2  
![imagine](screenshots/login-cmanager.png)  
Modificare date ale user cu id 3:  
![imagine](screenshots/forbidden-update-userdata.png)  

Interfata a fost realizata cu framework-ul React. Voi lasa acum cateva screenshot-uri cu interfata:  
Login:  
![imagine](screenshots/react-login.png)  
Login autentificare esuata:  
![imagine](screenshots/react-bad-credentials.png)  
User info:  
![imagine](screenshots/react-userdata.png)  
Playlist-uri proprii:  
![imagine](screenshots/react-platlist-own.png)  
Detalii playlist:  
![imagine](screenshots/react-playlist-details.png)  
Artists:  
![imagine](screenshots/react-artists.png)  
Detalii artist:  
![imagine](screenshots/react-artist-details.png)   
Adaugare artist:   
![imagine](screenshots/react-add-artist.png)  
Songs:  
![imagine](screenshots/react-songs.png)  
Detalii melodie:  
![imagine](screenshots/react-song-details.png)  
Adaugare melodie:  
![imagine](screenshots/react-add-song.png)  
Filtrare melodii
![imagine](screenshots/react-filter.png)  