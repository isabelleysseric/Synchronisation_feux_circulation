# Système de contrôle de lumières

## Programmation Java de la Spécification en FSP : 
### <em>Synchronisation de feux de circulation</em>
<br> 
<br>

<p><a href="https://commons.wikimedia.org/wiki/File:Carrefour_16_mouvements.jpg#/media/File:Carrefour_16_mouvements.jpg"><img src="https://upload.wikimedia.org/wikipedia/commons/3/34/Carrefour_16_mouvements.jpg" alt="Carrefour_16_mouvements.jpg"></a> 
<a href="https://resource.wur.nl/upload_mm/e/e/1/fa6e4646-947a-469d-9d43-3017bdbc79f2_verkeerslichten_fff9a156_200x193.jpg"><img src="https://resource.wur.nl/upload_mm/e/e/1/fa6e4646-947a-469d-9d43-3017bdbc79f2_verkeerslichten_fff9a156_200x193.jpg" alt="Carrefour.jpg"></a></p>
<br> 
<br>
  
### AUTEURS:
========
<p>Elida MELO</p>
<p>Ermine WANKPO</p>
<p>Isabelle EYSSERIC</p>
<p>Slim BEN YAHIA</p>
<br>
<br>

### ETAT DU PROJET:
==============
<p>Version initiale</p>
<br>

### DESCRIPTION:
===========
<p>Ce système contrôle les lumières soit d'une intersection en T, soit d'une intersection en croix, soit sur les deux en même temps. Il permet aux voitures et aux piétons de circuler en toute sécurité.</p>
<br> 
<br>


### MISE EN PLACE:
=============
<p>Suivre les étapes:</p>
<ol>
  1. Télécharger le fichier <code>Synchronisation_feux_circulation</code> puis en extraire les fichiers. <br>
  2. Ouvrez votre IDE pour <code>Java</code> et importer le dossier <code>JAVA</code> qui se trouve dans <code>Synchronisation_feux_circulation</code>. <br>
  3. Exécuter le programme principal nommé <code>Main.java</code> qui se trouve dans les répertoires <code>JAVA\TrafficManager\src\ca\ulaval\tp2\glo3004</code>. <br>
  4. Une fenêtre devrait apparaître. Il faut y rentrer les parametres du système qui sont sur la droite : <br>
  <ol>
    * Le choix de l'intersection<br>
    * Le nombre de voitures     <br>
    * Le nombre de pétons       <br>
    * La fréquence de chacune des lumières (Est, Ouest, Nord et Sud)<br>
    </ol>
  5. Un fois les paramètres rentrés, appuyer sur le bouton <code>Start</code> pour démarrer le système.<br>
</ol>
<br> 
<br>


### UTILISATION:
===========
<p>Le programme propose à l’utilisateur de controler:</p>
<ol>
  1. Le système avec les boutons en haut de la fenêtre: <code>START</code>, <code>PAUSE</code>, <code>QUIT</code> <br>
  2. Les parametres du système avec le menu à droite de la fenêtre pour le choix de l'intersection, le nombre de voitures et de piétons, ainsi que la fréquences des différentes lumières.
<br>
</ol>
<p>Plus en détails, le programme offre à l’utilisateur de :</p>
<ol>
Choisir le type d'intersection qu'il veut avec <br>
  1. L'intersection en T <code>THREE_WAY</code>  <br> 
  2. L'intersection en croix <code>CROSS</code>  <br> 
  3. Les deux intersections synchronisées <code>SYNCHRO</code>  <br>
<br>
  Choisir avec un entier entre 0 et 1 000 <br>
  1. Le nombre de voitures  <br> 
  2. Le nombre de piétons   <br> 
<br>
Choisir avec un intervalle entre 500 et 10 000 <br>
  1. La fréquence de la lumière Est   <br> 
  2. La fréquence de la lumière Ouest <br> 
  3. La fréquence de la lumière Nord  <br>
  4. La fréquence de la lumière Sud   <br>
</ol>
<br>
<p>Controler le programme avec :</p>
<ol>
  1. Le bouton <code>START</code> pour démarrer le système <br>
  2. Le bouton <code>RESTART</code> qui apparaît après un <code>START</code> pour recommencer le système <br>
  3. Le bouton <code>PAUSE</code> pour mettre sur pause le système <br>
  4. Le bouton <code>RESUME</code> qui apparaît après un <code>PAUSE</code> pour continuer le système <br>
</ol>
<br> 
<br>


### RESSOURCES, DOCUMENTATION:
===========================
<p><strong>Livrable 1 : Conception à partir de la spécification FSP</strong><br>
Fichier: Livrable1Equipe33.docx<br>
Vous y trouverez les <em>principaux événements, actions et interactions</em>, les <em>entités passives</em>, l'<em>environnement interactif d'affichage</em> et la <em>structure des classes</em> pour chacune des intersections et pour les deux synchronisées.</p>
<br>
<p><strong>Livrable 2 : Implémentation de la spécification FSP en Java</strong><br>
Dossier : JAVA/TrafficManager<br>
Vous y trouverez le fichier <code>Main.java</code> qui est le programme principal, <code>ExecutionParameters.java</code> et les sept dossiers <code>car</code>, <code>controller</code>, <code>intersection</code>, <code>light</code>, <code>road</code>, <code>runnable</code> et <code>view</code>.</p>
<br>
<p><strong>Livrable 3 : Fichier README avec la présentation et les instructions du programme</strong><br>
Fichier: README.txt <br>
Vous y trouverez les informations nécessaires pour bien utiliser le programme avec sa <code>DESCRIPTION</code>, sa <code>MISE EN PLACE</code>, son <code>UTILISATION</code> et <code>RESSOURCES, DOCUMENTATION</code>pour compléter l'information. Le tout accompagné de ses <code>AUTEURS</code> et l'<code>ÉTAT DU PROJET</code> pour connaître la version du programme.</p>
<p>Pour plus d'informations sur les  feux de circulation, visitez le site web de <a href="https://fr.wikipedia.org/wiki/Feu_de_circulationl">Wikipedia</a> ou bien celui de <a href="https://www.ontario.ca/fr/document/guide-officiel-de-lautomobiliste/feux-de-circulation">Ontario</a>.</p><br>
<br>


