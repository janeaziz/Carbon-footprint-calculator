
#  Frontend – CO₂ Groupe 10

Ce frontend fait partie du projet de simulation et de comparaison des émissions de CO₂ selon les moyens de transport entre deux villes.  
Il est développé avec **Vite + React 19**, utilise **JavaScript (JSX)** avec prise en charge partielle de **TypeScript pour les tests/config**, et communique avec un backend Spring Boot.

---

##  Structure du projet

- **Framework** : Vite
- **Langage principal** : JavaScript (JSX)
- **TypeScript** : utilisé uniquement pour la configuration et les tests (`vite.config.ts`, `vitest.config.js`, etc.)
- **Style** : Tailwind CSS
- **Tests** : Vitest + React Testing Library
- **Fonctionnalités clés** : Axios, Chart.js, Framer Motion, Lucide Icons

---

##  Dépendances principales

- [`vite`](https://vitejs.dev/)
- [`react`](https://react.dev/)
- [`tailwindcss`](https://tailwindcss.com/)
- [`axios`](https://axios-http.com/)
- [`react-chartjs-2`](https://react-chartjs-2.js.org/)
- [`chartjs-plugin-datalabels`](https://chartjs-plugin-datalabels.netlify.app/)
- [`framer-motion`](https://www.framer.com/motion/)
- [`lucide-react`](https://lucide.dev/icons/)
- [`@vitejs/plugin-react-swc`](https://www.npmjs.com/package/@vitejs/plugin-react-swc)
- [`vitest`](https://vitest.dev/) + [`@testing-library/react`](https://testing-library.com/docs/react-testing-library/intro/)

---

##  Installation des dépendances

### Étapes

```bash
# Cloner le projet
git clone https://forge.univ-lyon1.fr/mif10_grp10_2024/mif10_grp10_2024.git>
cd transport-co2

# Installer toutes les dépendances nécessaires
npm install

# Si jamais node_modules est corrompu ou le lockfile est ancien :
rm -rf node_modules package-lock.json
npm install
````

---

##  Lancement en local

### Prérequis

* Node.js ≥ 18
* npm ≥ 9
* Être connecté au **VPN de l’université** pour accéder au backend distant

### Commande de lancement

```bash
npm run dev
```

Accès via :
 `http://localhost:5173` (en local)
 `http://192.168.75.53` (en VM)

---

##  Lancer les tests

```bash
npm test --coverage
```

---

##  Déploiement sur VM

Le frontend peut être déployé sur une machine distante (VM) et accéder à une API hébergée à l’adresse :

 `https://192.168.75.53:8080`

Configurer le fichier `.env.production` :

```env
VITE_API_URL=https://192.168.75.53:8080
```

---

##  Analyse qualité – SonarQube

Le code frontend est analysé automatiquement à chaque commit via GitLab CI/CD.

- Les rapports de couverture sont générés avec `Vitest` (`npm test --coverage`) et envoyés à SonarQube.
- Un quality gate est appliqué pour surveiller la qualité, la duplication de code, et la couverture des tests.

🔗 SonarQube : [https://sonar.info.univ-lyon1.fr/dashboard?id=mif10_grp10_2024_mif10_grp10_2024_AZXaG0J-Re7mU3_qr5Pk]

---

##  Communication & Outils

* **GitLab Forge Lyon 1** — gestion de projet (tickets, MR, CI/CD)
* **Discord** — communication interne du groupe
* **Mattermost** — communication avec les enseignants
