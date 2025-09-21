#  Backend – Transport CO₂

Ce projet constitue la partie backend de l'application **Transport CO₂**, développée dans le cadre du projet universitaire MIF10 (Master Informatique Lyon 1).  
Il expose une **API REST sécurisée** permettant la simulation, la comparaison, et l’analyse des émissions de CO₂ selon les moyens de transport entre deux lieux.

---

##  Technologies utilisées

- **Langage** : Java 21  
- **Framework** : Spring Boot 3.4.4  
- **Base de données** : PostgreSQL  
- **Sécurité** : JWT (Json Web Token)  
- **Documentation API** : Swagger (Springdoc OpenAPI)  
- **Build Tool** : Maven (packaging `.war` pour Tomcat ou systemd)  
- **CI/CD** : GitLab Forge Lyon 1 + SonarQube  

---

##  Dépendances principales

| Catégorie       | Dépendances clés |
|------------------|------------------|
| Web & REST       | `spring-boot-starter-web` |
| Base de données  | `spring-boot-starter-data-jpa`, `org.postgresql:postgresql` |
| Sécurité         | `spring-boot-starter-security`, `jjwt-api`, `jjwt-impl`, `jjwt-jackson` |
| Validation       | `spring-boot-starter-validation` |
| Monitoring       | `spring-boot-starter-actuator` |
| Documentation    | `springdoc-openapi-starter-webmvc-ui` |
| Utilitaires      | `lombok`, `spring-boot-devtools` |
| Test             | `spring-boot-starter-test`, `spring-security-test`, `h2` |
| Couverture       | `jacoco-maven-plugin` |

---

##  Configuration de l'environnement

###  Utilisation de `.env.local` (recommandé)

Créez un fichier `.env.local` à la racine du projet contenant vos paramètres :

```properties

spring.datasource.password=CHANGER_CE_SECRET
jwt.secret=CHANGER_CE_SECRET
google.api.key=CHANGER_CE_SECRET

````

Ensuite, pour le charger dans votre terminal :

####  Sur Linux / macOS :

```bash
while IFS='=' read -r key value; do
  [[ -z "$key" || "$key" == \#* ]] && continue
  export "$key"="$(echo "$value" | sed 's/^ *//;s/ *$//')"
done < .env.local
```

####  Sur Windows PowerShell :

```powershell
Get-Content .env.local | ForEach-Object {
  if ($_ -match "^\s*$") { return }
  $parts = $_.Split("=", 2)
  if ($parts[0] -and $parts[1]) {
    [System.Environment]::SetEnvironmentVariable($parts[0].Trim(), $parts[1].Trim())
  }
}
```

---

##  Procédure de build

###  Prérequis

* Java 21 installé
* PostgreSQL opérationnel
* Maven ≥ 3.8

###  Commandes

```bash
# Cloner le projet
git clone https://forge.univ-lyon1.fr/mif10_grp10_2024/backendco2.git
cd backendco2

# Nettoyer et compiler le projet
./mvnw clean install

# Générer le fichier WAR pour déploiement
./mvnw package
```

Fichier généré :

```bash
target/co2.war
```

---

##  Lien de démonstration (VM)

*  Frontend : [http://192.168.75.53](http://192.168.75.53)
*  API REST : [http://192.168.75.53:8080](http://192.168.75.53:8080)
*  Swagger UI : [http://192.168.75.53:8080/swagger-ui.html](http://192.168.75.53:8080/swagger-ui.html)

>  Accès restreint au réseau de l’université (VPN requis)

---

##  Déploiement automatique (CI/CD)

Le backend est automatiquement **buildé et déployé sur la VM** via GitLab CI/CD.

### Étapes :

1. Le job `build-backend` génère :

   ```bash
   target/co2.war
   ```

2. Le job `deploy-backend` utilise `rsync` pour le transférer vers :

   ```bash
   /var/www/backendco2/co2.war
   ```

3. Le redémarrage est géré via **`systemd`**, avec le service :

   ```bash
   /etc/systemd/system/co2.service
   ```

4. Le backend est désormais contrôlé proprement par :

   ```bash
   sudo systemctl start co2
   sudo systemctl stop co2
   sudo systemctl restart co2
   ```

---

##  Analyse qualité avec SonarQube

*  https://sonar.info.univ-lyon1.fr/dashboard?id=mif10_grp10_2024_backendco2
* Analyse automatisée via GitLab CI/CD

---

##  Documentation API

* Swagger UI : [http://192.168.75.53:8080/swagger-ui.html](http://192.168.75.53:8080/swagger-ui.html)

---

##  Communication & Outils

* **GitLab Forge Lyon 1** — tickets, merge requests, CI/CD
* **SonarQube** — analyse qualité automatique
* **Postman** — tests manuels
* **Discord** — communication de groupe
* **Mattermost** — communication avec les encadrants


