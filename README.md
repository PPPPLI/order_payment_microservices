# Docker - Microservices - Order - Payment - Product - E-commerce project

## **Si vous voulez tester directement le projet, ici le lien sur Azure Cloud: http://40.66.40.168/home** (Si le serveur est disponible)
N.B. Le projet n'accepte momentanement que la version ordinateur, veuillez utiliser un navigateur sur l'ordinateur s'il vous plaît.

<h3><b>Introduction</b></h3>

Ce projet est un simple système de e-commerce. Il contient au total 4 services principaux(Authentification, Commande, Paiement, Produit), 1 Mysql, 1 Redis, 1 Gateway ainsi que 1 centre de gestion et d'orchestration des servies.

Tout d'abord, le **gateway** agit comme une façade unique qui redirige les requêtes des utilisateurs vers les différents services internes. Il distribue les requêtes en fonction de la route ou du type de service demandé, ce qui facilite la gestion du trafic vers les services appropriés sans que le client ait à connaître l'architecture sous-jacente.

Le **service d'authentification** est utilisé pour générer un token permettant aux utilisateurs de s'authentifier automatiquement auprès des autres services.

Le **service Order** gère tout ce qui concerne les commandes, telles que la création, la mise à jour, ainsi que la suppression. Il est connecté à deux types de bases de données : MySQL et Redis. Pour la lecture des données, il interroge d'abord le cache (Redis). Si les données ne sont pas trouvées dans Redis, il consulte MySQL. Après lecture, les données sont insérées dans le cache pour faciliter les futures lectures.

Une fois que le service Order reçoit une requête de paiement, il communique avec le **service Payment** pour procéder au paiement via **Grpc**, puis stocke les informations dans la base de données.

Le service Order envoie ensuite une requête au **service Product**, aussi via Grpc protocol, afin de mettre à jour le stock dans la base de données.

En cas d'échec lors de l'enregistrement des données, le **modèle SAGA** est utilisé pour compenser les opérations et garantir la cohérence des données.

N.B. Tous les services sont inscrits dans un **centre de gestion et d'orchestration des services**, ici j'utilise **Consul**.

<h3><b>Architecture du projet</b></h3>

![image](https://github.com/user-attachments/assets/4d01d059-485b-49d1-a47e-e713cf499813)


<br>

## **Démarrage classique**

1. Colon ce repository
2. Modifier la configuration pour la partie de datasource en fonction de paramètres locaux lié à votre propre base de données(username, password...)
   
   ![image](https://github.com/user-attachments/assets/003af4a1-d0bc-4f89-b3c7-4f6bd4c9c0b4)

3. Démarrer Consul service
4. Démarrer Redis server
5. Démarrer les 5 services repectivement sauf le module Common

## **Démarrage en Docker**

1. Phase préparatoire
   
   - Pull de Docker Hub les images nécessaires : mysql:8.0 | consul:1.15.4 | redis:7.4
   - Classifier les fichiers Jars, Dockerfiles et docker-compose.yml en suivant la structure dessous

   ![image](https://github.com/user-attachments/assets/a1950bff-5ba4-4428-9051-220f6f46cc11)


2. Phase d'exécution

   - Lancer la commande "docker compose up --build -d" au chemin /docker

## **URL d'api-doc**

- **http://localhost:9090/order/api-doc**
- **http://localhost:9090/payment/api-doc**
- **http://localhost:9090/product/api-doc**
  
  ![image](https://github.com/user-attachments/assets/2126b4ab-e670-4c4d-b775-7b65881f21f2)
  ![image](https://github.com/user-attachments/assets/e6d9c638-3c24-4913-a3ae-da7aa818d2e2)
  ![image](https://github.com/user-attachments/assets/6f4d68c8-0626-4b02-8c28-c12d7171cc98)



## Golbal Exception Handler

### - Rest Api
  - @RestControllerAdvice et @ExceptionHandler
 
    ![image](https://github.com/user-attachments/assets/38f4ee12-9c7e-47d3-8402-e5d22d431692)


### - Grpc
  - @GrpcGlobalServerInterceptor
  - Héritage de l'interface: ServerInterceptor
  - Implémentation de la méthode interceptCall
  - Convertir l'exception en Status
 
  ![image](https://github.com/user-attachments/assets/68d25355-22f1-4f52-a136-c949326facbf)


## **Configuration de niveau de logging**

![image](https://github.com/user-attachments/assets/b3fb8f73-2b47-4d8b-b26a-16c41c8082b8)


## **Imprimer l'historique de log dans le terminal**

  - @slf4j
  - log.info()
  - log.error()

## **Modèle Saga en cas d'échec lors de l'enregistrement des données**

![image](https://github.com/user-attachments/assets/6ac9817e-7977-4d3c-87aa-2bfacd4b8283)

## **Unit Test**

- @SpringBootTest
- @AutoConfigureMockMvc
- @Test
- @MockBean

### **L'un des exemples de test**
![image](https://github.com/user-attachments/assets/8e46fad4-6077-4584-aba9-0bb07326ccb2)

![image](https://github.com/user-attachments/assets/69ad9907-5dec-43ab-becd-2b6845bd0d88)

![image](https://github.com/user-attachments/assets/579044fc-3fab-434d-8014-8b31c6bd94b5)
![image](https://github.com/user-attachments/assets/c6442012-07cf-41d9-bbc4-b90a754db916)




    

 


